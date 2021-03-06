/**
 *  Copyright Terracotta, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.sf.ehcache.management.sampled;

import net.sf.ehcache.util.counter.sampled.SampledCounter;
import net.sf.ehcache.util.counter.sampled.SampledRateCounter;


/**
 * An interface for exposing cache statistics.
 * Extends from both {@link LiveCacheStatistics} and {@link LegacyCacheStatistics}
 *
 * @author <a href="mailto:asanoujam@terracottatech.com">Abhishek Sanoujam</a>
 * @author <a href="mailto:byoukste@terracottatech.com">byoukste</a>
 */
public interface CacheSampler extends LegacyCacheStatistics {
    /**
     * Is the cache enabled?
     */
    boolean isEnabled();

    /**
     * Enabled/disable bulk-load mode for this node.
     */
    void setNodeBulkLoadEnabled(boolean bulkLoadEnabled);

    /**
     * Is the cache in bulk-load mode cluster-wide?
     */
    boolean isClusterBulkLoadEnabled();

    /**
     * Is the cache in bulk-load mode locally?
     */
    boolean isNodeBulkLoadEnabled();

    /**
     * Enabled/disable the cache.
     */
    void setEnabled(boolean enabled);

    /**
     * Removes all cached items.
     */
    void removeAll();

    /**
     * Flushes all cache items from memory to the disk store, and from the
     * DiskStore to disk.
     */
    void flush();

    /**
     * Gets the status attribute of the Cache.
     *
     * @return The status value from the Status enum class
     */
    String getStatus();

    /**
     * Is the cache configured with Terracotta clustering?
     *
     * @return true if clustered with terracotta
     */
    boolean isTerracottaClustered();

    /**
     * Returns a textual description of a Terracotta-clustered cache's consistency mode.
     *
     * @return "STRONG", "EVENTUAL", or "na" if the cache is not Terracotta-clustered
     */
    String getTerracottaConsistency();

    /**
     * Configuration property accessor
     *
     * @return Max entries local heap config setting value
     */
    long getMaxEntriesLocalHeap();

    /**
     * setMaxEntriesLocalHeap
     *
     * @param maxEntries
     */
    void setMaxEntriesLocalHeap(long maxEntries);

    /**
     * Configuration property accessor
     *
     * @return Max bytes local heap config setting value
     */
    long getMaxBytesLocalHeap();

    /**
     * setMaxBytesLocalHeap
     *
     * @param maxBytes
     */
    void setMaxBytesLocalHeap(long maxBytes);

    /**
     * setMaxBytesLocalHeap
     *
     * @param maxBytes
     */
    void setMaxBytesLocalHeapAsString(String maxBytes);

    /**
     * Configuration property accessor
     *
     * @return Max bytes local heap config setting value as string
     */
    String getMaxBytesLocalHeapAsString();

    /**
     * Configuration property accessor
     *
     * @return Max bytes local offheap config setting value
     */
    long getMaxBytesLocalOffHeap();

    /**
     * Configuration property accessor
     *
     * @return Max bytes local offheap config setting value as string
     */
    String getMaxBytesLocalOffHeapAsString();

    /**
     * Configuration property accessor
     *
     * @return Max entries local disk config setting value
     */
    long getMaxEntriesLocalDisk();

    /**
     * setMaxEntriesLocalDisk
     *
     * @param maxEntries
     */
    void setMaxEntriesLocalDisk(long maxEntries);

    /**
     * Configuration property accessor
     *
     * @return Max elements on disk config setting value
     */
    int getMaxElementsOnDisk();

    /**
     * Configuration property accessor
     *
     * @return Max entries in cache config setting value
     */
    int getMaxEntriesInCache();

    /**
     * setMaxElementsOnDisk
     *
     * @param maxElements
     */
    void setMaxElementsOnDisk(int maxElements);

    /**
     * setMaxEntriesInCache
     *
     * @param maxEntriesInCache
     */
    void setMaxEntriesInCache(int maxEntries);

    /**
     * Configuration property accessor
     *
     * @return Max bytes local disk config setting value
     */
    long getMaxBytesLocalDisk();

    /**
     * setMaxBytesLocalDisk
     *
     * @param maxBytes
     */
    void setMaxBytesLocalDisk(long maxBytes);

    /**
     * setMaxBytesLocalDisk
     *
     * @param maxBytes
     */
    void setMaxBytesLocalDiskAsString(String maxBytes);

    /**
     * Configuration property accessor
     *
     * @return Max bytes local disk config setting value as string
     */
    String getMaxBytesLocalDiskAsString();

    /**
     * Configuration property accessor
     *
     * @return a String representation of the policy
     */
    String getMemoryStoreEvictionPolicy();

    /**
     * setMemoryStoreEvictionPolicy
     *
     * @param evictionPolicy
     */
    void setMemoryStoreEvictionPolicy(String evictionPolicy);

    /**
     * Configuration property accessor
     *
     * @return true if set to eternal in config
     */
    boolean isEternal();

    /**
     * setEternal
     *
     * @param eternal
     */
    void setEternal(boolean eternal);

    /**
     * Configuration property accessor
     *
     * @return TTI in config
     */
    long getTimeToIdleSeconds();

    /**
     * setTimeToIdleSeconds
     *
     * @param tti
     */
    void setTimeToIdleSeconds(long tti);

    /**
     * Configuration property accessor
     *
     * @return TTL in config
     */
    long getTimeToLiveSeconds();

    /**
     * setTimeToLiveSeconds
     *
     * @param ttl
     */
    void setTimeToLiveSeconds(long ttl);

    /**
     * Configuration property accessor
     *
     * @return true if overflow to disk specified in config
     */
    boolean isOverflowToDisk();

    /**
     * setOverflowToDisk
     *
     * @param overflowToDisk
     */
    void setOverflowToDisk(boolean overflowToDisk);

    /**
     * Configuration property accessor
     *
     * @return true if configured with disk persistence
     */
    boolean isDiskPersistent();

    /**
     * setDiskPersistent
     *
     * @param diskPersistent
     */
    void setDiskPersistent(boolean diskPersistent);

    /**
     * Configuration property accessor
     *
     * @return Value for disk expiry thread interval in seconds specified in config
     */
    long getDiskExpiryThreadIntervalSeconds();

    /**
     * setDiskExpiryThreadIntervalSeconds
     *
     * @param seconds
     */
    void setDiskExpiryThreadIntervalSeconds(long seconds);

    /**
     * Configuration property accessor
     *
     * @return true if logging is enabled on the cache
     */
    boolean isLoggingEnabled();

    /**
     * setLoggingEnabled
     *
     * @param enabled
     */
    void setLoggingEnabled(boolean enabled);

    /**
     * Configuration property accessor
     *
     * @return true if the cache is pinned
     * @see net.sf.ehcache.config.PinningConfiguration
     */
    boolean isPinned();

    /**
     * Configuration property accessor
     *
     * @return the store to which this cache is pinned
     * @see net.sf.ehcache.config.PinningConfiguration
     */
    String getPinnedToStore();

    /**
     * Is there a registered Write-behind CacheWriter
     */
    boolean getHasWriteBehindWriter();

    /**
     * Returns the total length of all write-behind queues for this cache
     *
     * @return writer-behind queue length
     */
    long getWriterQueueLength();

    /**
     * Returns the maximum size of any write-behind queues.
     *
     * @return Maximum elements that can be queued for processing by the write-behind writer
     * @see net.sf.ehcache.config.CacheWriterConfiguration#getWriteBehindMaxQueueSize()
     */
    int getWriterMaxQueueSize();

    /**
     * Returns the number of configured write-behind queues/threads.
     *
     * @return Number of configured processing queues/threads for use by the write-behind writer
     * @see net.sf.ehcache.config.CacheWriterConfiguration#getWriteBehindConcurrency()
     */
    int getWriterConcurrency();

    /**
     * Is the cache a transactional one
     *
     * @see net.sf.ehcache.config.CacheConfiguration.TransactionalMode
     */
    boolean getTransactional();

    /**
     * @return Xa commit rate
     */
    long getTransactionCommitRate();

    /**
     * @return Xa rollback rate
     */
    long getTransactionRollbackRate();

    /**
     * Is the cache configured for search
     *
     * @see net.sf.ehcache.config.Searchable
     */
    boolean getSearchable();

    /**
     * @return search rate
     */
    long getCacheSearchRate();

    /**
     * @return search time
     */
    long getAverageSearchTime();

    /**
     * @return hit rate
     */
    long getCacheHitRate();

    /**
     * @return in-memory hit rate
     */
    long getCacheInMemoryHitRate();

    /**
     * @return off-heap hit rate
     */
    long getCacheOffHeapHitRate();

    /**
     * @return on-disk hit rate
     */
    long getCacheOnDiskHitRate();

    /**
     * @return miss rate
     */
    long getCacheMissRate();

    /**
     * @return in-memory miss rate
     */
    long getCacheInMemoryMissRate();

    /**
     * @return off-heap miss rate
     */
    long getCacheOffHeapMissRate();

    /**
     * @return on-disk miss rate
     */
    long getCacheOnDiskMissRate();

    /**
     * @return put rate
     */
    long getCachePutRate();

    /**
     * @return update rate
     */
    long getCacheUpdateRate();

    /**
     * @return remove rate
     */
    long getCacheRemoveRate();

    /**
     * @return eviction rate
     */
    long getCacheEvictionRate();

    /**
     * @return expiration rate
     */
    long getCacheExpirationRate();

    /**
     * @return average get time (nanos.)
     */
    long getAverageGetTime();
    

    /**
     * Get the {@link SampledCounter} for cache hit
     *
     * @return the {@code SampledCounter} for cache hit count
     */
    SampledCounter getCacheHitSample();

    /**
     * Get the {@link SampledCounter} for cache hit ratio
     *
     * @return the {@code SampledCounter} for cache hit ratio
     */
    SampledCounter getCacheHitRatioSample();

    /**
     * Get the {@link SampledCounter} for in-memory cache hit
     *
     * @return the {@code SampledCounter} for cache hit count in memory
     */
    SampledCounter getCacheHitInMemorySample();

    /**
     * Get the {@link SampledCounter} for off-heap cache hit
     *
     * @return the {@code SampledCounter} for cache hit count in off-heap
     */
    SampledCounter getCacheHitOffHeapSample();

    /**
     * Get the {@link SampledCounter} for on-disk cache hit
     *
     * @return the {@code SampledCounter} for cache hit count on disk
     */
    SampledCounter getCacheHitOnDiskSample();

    /**
     * Get the {@link SampledCounter} for cache miss
     *
     * @return the {@code SampledCounter} for cache miss count
     */
    SampledCounter getCacheMissSample();

    /**
     * Get the {@link SampledCounter} for in-memory cache miss
     *
     * @return the {@code SampledCounter} for cache miss count in memory
     */
    SampledCounter getCacheMissInMemorySample();

    /**
     * Get the {@link SampledCounter} for off-heap cache miss
     *
     * @return the {@code SampledCounter} for cache miss count in off-heap
     */
    SampledCounter getCacheMissOffHeapSample();

    /**
     * Get the {@link SampledCounter} for on-disk cache miss
     *
     * @return the {@code SampledCounter} for cache miss count on disk
     */
    SampledCounter getCacheMissOnDiskSample();

    /**
     * Get the {@link SampledCounter} for cache miss as result of the element getting
     * expired
     *
     * @return the {@code SampledCounter} for cache miss count and the reason for miss
     *         being the element got expired
     */
    SampledCounter getCacheMissExpiredSample();

    /**
     * Get the {@link SampledCounter} for cache miss as result of the element not found
     * in cache
     *
     * @return the {@code SampledCounter} for cache miss not found count
     */
    SampledCounter getCacheMissNotFoundSample();

    /**
     * Get the {@link SampledCounter} element evicted from cache
     *
     * @return the {@code SampledCounter} for element evicted count
     */
    SampledCounter getCacheElementEvictedSample();

    /**
     * Get the {@link SampledCounter} element removed from cache
     *
     * @return the {@code SampledCounter} for element removed count
     */
    SampledCounter getCacheElementRemovedSample();

    /**
     * Get the {@link SampledCounter} element expired from cache
     *
     * @return Most recent value for element expired count
     */
    SampledCounter getCacheElementExpiredSample();

    /**
     * Get the {@link SampledCounter} element puts in the cache
     *
     * @return the {@code SampledCounter} for number of element puts
     */
    SampledCounter getCacheElementPutSample();

    /**
     * Get the {@link SampledCounter} element updates , i.e. put() on elements with
     * already existing keys in the cache
     *
     * @return the {@code SampledCounter}d value for element update count
     */
    SampledCounter getCacheElementUpdatedSample();

    /**
     * Get the {@link SampledRateCounter} for average time taken for get() operation in the
     * cache
     *
     * @return the {@code SampledRateCounter} of average get time taken for a get operation
     */
    SampledRateCounter getAverageGetTimeSample();

    /**
     * Get the {@link SampledRateCounter} for average search execution time for searches finishing within the last sample period
     *
     * @return the {@code SampledRateCounter} of average search time taken
     */
    SampledRateCounter getAverageSearchTimeSample();

    /**
     * Get the {@link SampledCounter} for number of searches that have finished in the interval
     *
     * @return the {@code SampledCounter} for number of searches
     */
    SampledCounter getSearchesPerSecondSample();

    /**
     * Get the {@link SampledCounter} for number of XA Transaction commits that have completed in the interval
     *
     * @return the {@code SampledCounter} for number XA Transaction commits
     */
    SampledCounter getCacheXaCommitsSample();

    /**
     * Get the {@link SampledCounter} for number of XA Transaction rollbacks that have completed in the interval
     *
     * @return the {@code SampledCounter} for number XA Transaction rollbacks
     */
    SampledCounter getCacheXaRollbacksSample();
}