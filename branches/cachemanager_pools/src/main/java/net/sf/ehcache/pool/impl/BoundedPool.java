package net.sf.ehcache.pool.impl;

import net.sf.ehcache.pool.Pool;
import net.sf.ehcache.pool.PoolableStore;
import net.sf.ehcache.pool.SizeOfEngine;
import net.sf.ehcache.pool.PoolAccessor;
import net.sf.ehcache.pool.PoolEvictor;
import net.sf.ehcache.pool.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ludovic Orban
 */
public class BoundedPool implements Pool {

    private volatile long maximumPoolSize;
    private volatile PoolEvictor<PoolableStore> evictor;
    private final List<BoundedPoolAccessor> poolAccessors;
    private final SizeOfEngine defaultSizeOfEngine;

    public BoundedPool(long maximumPoolSize, PoolEvictor<PoolableStore> evictor, SizeOfEngine defaultSizeOfEngine) {
        this.maximumPoolSize = maximumPoolSize;
        this.evictor = evictor;
        this.defaultSizeOfEngine = defaultSizeOfEngine;
        this.poolAccessors = Collections.synchronizedList(new ArrayList<BoundedPoolAccessor>());
    }

    public long getSize() {
        long total = 0L;
        for (PoolAccessor poolAccessor : poolAccessors) {
            total += poolAccessor.getSize();
        }
        return total;
    }

    public PoolAccessor createPoolAccessor(PoolableStore store) {
        BoundedPoolAccessor poolAccessor = new BoundedPoolAccessor(store, defaultSizeOfEngine, 0);
        poolAccessors.add(poolAccessor);
        return poolAccessor;
    }

    public void removePoolAccessor(PoolAccessor poolAccessor) {
        Iterator<BoundedPoolAccessor> iterator = poolAccessors.iterator();
        while (iterator.hasNext()) {
            BoundedPoolAccessor next = iterator.next();
            if (next == poolAccessor) {
                iterator.remove();
                return;
            }
        }
    }

    public Collection<PoolableStore> getPoolableStores() {
        Collection<PoolableStore> poolableStores = new ArrayList<PoolableStore>();
        for (BoundedPoolAccessor poolAccessor : poolAccessors) {
            poolableStores.add(poolAccessor.store);
        }
        return poolableStores;
    }


    public class BoundedPoolAccessor implements PoolAccessor {
        private final PoolableStore store;
        private final SizeOfEngine sizeOfEngine;
        private long size;
        private final AtomicBoolean unlinked = new AtomicBoolean();
        private final Lock lock = new ReentrantLock();

        public BoundedPoolAccessor(PoolableStore store, SizeOfEngine sizeOfEngine, long currentSize) {
            this.store = store;
            this.sizeOfEngine = sizeOfEngine;
            this.size = currentSize;
        }

        public long add(Object key, Object value, Object container) {
            if (unlinked.get()) {
                throw new IllegalStateException("BoundedPoolAccessor has been unlinked");
            }

            long sizeOf = sizeOfEngine.sizeOf(key, value, container);

            // synchronized makes the size update MT-safe but slow
            lock.lock();
            try {
                long newSize = BoundedPool.this.getSize() + sizeOf;

                if (newSize <= maximumPoolSize) {
                    // there is enough room => add & approve
                    size += sizeOf;
                    return sizeOf;
                } else {
                    // there is not enough room => evict
                    long missingSize = newSize - maximumPoolSize;
                    if (missingSize > maximumPoolSize) {
                        // this is too big to fit in the pool
                        return -1;
                    }

                    // eviction must be done outside the lock to avoid deadlocks as it may evict from other pools
                    lock.unlock();
                    try {
                        boolean successful = evictor.freeSpace(getPoolableStores(), missingSize);
                        if (!successful) {
                            // cannot free enough bytes
                            return -1;
                        }
                    } finally {
                        lock.lock();
                    }

                    size += sizeOf;
                    return sizeOf;
                }
            } finally {
                lock.unlock();
            }
        }

        public long delete(Object key, Object value, Object container) {
            if (unlinked.get()) {
                throw new IllegalStateException("BoundedPoolAccessor has been unlinked");
            }

            long sizeOf = sizeOfEngine.sizeOf(key, value, container);

            // synchronized makes the size update MT-safe but slow
            lock.lock();
            try {
                size -= sizeOf;
            } finally {
                lock.unlock();
            }

            return sizeOf;
        }

        public long replace(Role role, Object current, Object replacement) {
            if (unlinked.get()) {
                throw new IllegalStateException("BoundedPoolAccessor has been unlinked");
            }

            // synchronized makes the size update MT-safe but slow
            lock.lock();
            try {
                long sizeOf = 0;
                switch (role) {
                    case CONTAINER:
                        sizeOf += delete(null, null, current);
                        sizeOf -= add(null, null, replacement);
                        break;
                    case KEY:
                        sizeOf += delete(current, null, null);
                        sizeOf -= add(replacement, null, null);
                        break;
                    case VALUE:
                        sizeOf += delete(null, current, null);
                        sizeOf -= add(null, replacement, null);
                        break;
                }
                return sizeOf;
            } finally {
                lock.unlock();
            }
        }

        public long getSize() {
            // synchronized makes the size update MT-safe but slow
            lock.lock();
            try {
                return size;
            } finally {
                lock.unlock();
            }
        }

        public void unlink() {
            if (unlinked.compareAndSet(false, true)) {
                removePoolAccessor(this);
            }
        }

        public void clear() {
            // synchronized makes the size update MT-safe but slow
            lock.lock();
            try {
                size = 0L;
            } finally {
                lock.unlock();
            }
        }
    }

}
