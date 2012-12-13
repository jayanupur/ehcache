/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.ehcache.statisticsV2.extended;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import org.junit.Test;
import org.terracotta.statistics.StatisticsManager;

import static net.sf.ehcache.CacheOperationOutcomes.GetOutcome.*;
import net.sf.ehcache.statisticsV2.extended.ExtendedStatistics.Operation;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.number.OrderingComparison.*;
import static org.junit.Assert.assertThat;

/**
 *
 * @author cdennis
 */
public class ExtendedStatisticsTest {
    
    @Test(expected = IllegalStateException.class)
    public void testExtendedStatisticsWithoutRequiredStats() {
        new ExtendedStatisticsImpl(new StatisticsManager(), 10, TimeUnit.SECONDS);
    }

    @Test
    public void testSimpleCacheStatistics() throws InterruptedException {
        CacheManager manager = new CacheManager(new Configuration().name("foo-manager"));
        try {
            Cache foo = new Cache(new CacheConfiguration().name("foo").maxEntriesLocalHeap(1000));
            manager.addCache(foo);
            
            ExtendedStatistics extendedStats = foo.getStatistics().getExtended();
            
            assertThat(extendedStats.get().component(HIT).count(), is(0L));
            assertThat(extendedStats.get().component(MISS_NOT_FOUND).count(), is(0L));
            assertThat(extendedStats.get().component(MISS_EXPIRED).count(), is(0L));
            
            foo.get("miss");
            
            assertThat(extendedStats.get().component(HIT).count(), is(0L));
            assertThat(extendedStats.get().component(MISS_NOT_FOUND).count(), is(1L));
            assertThat(extendedStats.get().component(MISS_EXPIRED).count(), is(0L));
            
            foo.put(new Element("miss", "miss"));
            foo.get("miss");
            
            assertThat(extendedStats.get().component(HIT).count(), is(1L));
            assertThat(extendedStats.get().component(MISS_NOT_FOUND).count(), is(1L));
            assertThat(extendedStats.get().component(MISS_EXPIRED).count(), is(0L));
        } finally {
            manager.shutdown();
        }
    }

    @Test
    public void testExtendedCacheTimeToDisable() throws InterruptedException {
        CacheManager manager = new CacheManager(new Configuration().name("foo-manager"));
        try {
            Cache foo = new Cache(new CacheConfiguration().name("foo").maxEntriesLocalHeap(1000));
            manager.addCache(foo);
            
            ExtendedStatistics extendedStats = foo.getStatistics().getExtended();
            extendedStats.setStatisticsTimeToDisable(2, TimeUnit.SECONDS);
            Operation missNotFound = extendedStats.get().component(MISS_NOT_FOUND);
            
            assertThat(missNotFound.count(), is(0L));
            assertThat(missNotFound.rate().value(), is(0.0));
            
            foo.get("miss");
            assertThat(missNotFound.count(), is(1L));
            assertThat(missNotFound.rate().value(), greaterThan(0.0));

            TimeUnit.SECONDS.sleep(1);
            
            foo.get("miss");
            assertThat(missNotFound.count(), is(2L));
            assertThat(missNotFound.rate().value(), greaterThan(0.0));
            
            TimeUnit.SECONDS.sleep(3);
            
            foo.get("miss");
            assertThat(missNotFound.count(), is(3L));
            assertThat(missNotFound.rate().value(), is(0.0));
        } finally {
            manager.shutdown();
        }
    }

    @Test
    public void testExtendedCacheEnable() throws InterruptedException {
        CacheManager manager = new CacheManager(new Configuration().name("foo-manager"));
        try {
            Cache foo = new Cache(new CacheConfiguration().name("foo").maxEntriesLocalHeap(1000));
            manager.addCache(foo);
            
            ExtendedStatistics extendedStats = foo.getStatistics().getExtended();
            extendedStats.setStatisticsTimeToDisable(2, TimeUnit.SECONDS);
            extendedStats.setStatisticsEnabled(true);
            Operation missNotFound = extendedStats.get().component(MISS_NOT_FOUND);
            
            assertThat(missNotFound.count(), is(0L));
            assertThat(missNotFound.rate().value(), is(0.0));
            
            foo.get("miss");
            assertThat(missNotFound.count(), is(1L));
            assertThat(missNotFound.rate().value(), greaterThan(0.0));

            TimeUnit.SECONDS.sleep(1);
            
            foo.get("miss");
            assertThat(missNotFound.count(), is(2L));
            assertThat(missNotFound.rate().value(), greaterThan(0.0));
            
            TimeUnit.SECONDS.sleep(3);
            
            foo.get("miss");
            assertThat(missNotFound.count(), is(3L));
            assertThat(missNotFound.rate().value(), greaterThan(0.0));
        } finally {
            manager.shutdown();
        }
    }

    @Test
    public void testExtendedCacheDisable() throws InterruptedException {
        CacheManager manager = new CacheManager(new Configuration().name("foo-manager"));
        try {
            Cache foo = new Cache(new CacheConfiguration().name("foo").maxEntriesLocalHeap(1000));
            manager.addCache(foo);
            
            ExtendedStatistics extendedStats = foo.getStatistics().getExtended();
            extendedStats.setStatisticsTimeToDisable(2, TimeUnit.SECONDS);
            extendedStats.setStatisticsEnabled(true);
            Operation missNotFound = extendedStats.get().component(MISS_NOT_FOUND);
            
            assertThat(missNotFound.count(), is(0L));
            assertThat(missNotFound.rate().value(), is(0.0));
            
            foo.get("miss");
            assertThat(missNotFound.count(), is(1L));
            assertThat(missNotFound.rate().value(), greaterThan(0.0));

            TimeUnit.SECONDS.sleep(1);
            
            foo.get("miss");
            assertThat(missNotFound.count(), is(2L));
            assertThat(missNotFound.rate().value(), greaterThan(0.0));
            
            TimeUnit.SECONDS.sleep(3);
            
            extendedStats.setStatisticsEnabled(false);
            foo.get("miss");
            assertThat(missNotFound.count(), is(3L));
            assertThat(missNotFound.rate().value(), is(0.0));
        } finally {
            manager.shutdown();
        }
    }
    
    @Test
    public void testExtendedCacheLatencyMeasurement() {
        CacheManager manager = new CacheManager(new Configuration().name("foo-manager"));
        try {
            Cache foo = new Cache(new CacheConfiguration().name("foo").maxEntriesLocalHeap(1000));
            manager.addCache(foo);
            
            ExtendedStatistics extendedStats = foo.getStatistics().getExtended();
            extendedStats.setStatisticsEnabled(true);
            Operation missNotFound = extendedStats.get().component(MISS_NOT_FOUND);
            
            assertThat(missNotFound.count(), is(0L));
            assertThat(missNotFound.latency().average().value(), is(Double.NaN));
            
            foo.get("miss");
            assertThat(missNotFound.count(), is(1L));
            assertThat(missNotFound.latency().average().value(), greaterThan(0.0));
            assertThat(missNotFound.latency().minimum().value(), is(missNotFound.latency().maximum().value()));
            assertThat(missNotFound.latency().minimum().value().doubleValue(), is(missNotFound.latency().average().value()));

            foo.get("miss");
            assertThat(missNotFound.count(), is(2L));
            assertThat(missNotFound.latency().average().value(), greaterThan(0.0));
            assertThat(missNotFound.latency().minimum().value().doubleValue(), lessThanOrEqualTo(missNotFound.latency().average().value()));
            assertThat(missNotFound.latency().maximum().value().doubleValue(), greaterThanOrEqualTo(missNotFound.latency().average().value()));
        } finally {
            manager.shutdown();
        }
    }
}
