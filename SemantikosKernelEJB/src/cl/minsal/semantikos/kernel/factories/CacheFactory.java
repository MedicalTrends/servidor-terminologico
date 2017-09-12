package cl.minsal.semantikos.kernel.factories;

import cl.minsal.semantikos.model.ConceptSMTK;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by root on 12-04-17.
 */
public class CacheFactory {

    static private final Logger logger = LoggerFactory.getLogger(CacheFactory.class);

    private static final CacheFactory instance = new CacheFactory();

    private static CacheManager cacheManager;

    private Cache<Long, ConceptSMTK> cache;

    public Cache<Long, ConceptSMTK> getCache() {
        return cache;
    }

    public void setCache(Cache<Long, ConceptSMTK> cache) {
        this.cache = cache;
    }

    public static CacheFactory getInstance() {
        return instance;
    }

    /**
     * Constructor privado para el Singleton del Factory.

     */
    private CacheFactory() {

    }

    public void initCache() {

         cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("preConfigured",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, ConceptSMTK.class,
                                ResourcePoolsBuilder.heap(10000))
                                .build())
                .build(true);

        Cache<Long, ConceptSMTK> preConfigured = cacheManager.getCache("preConfigured", Long.class, ConceptSMTK.class);

        cache = cacheManager.createCache("cache",
                            CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, ConceptSMTK.class,
                            ResourcePoolsBuilder.heap(10000)).build());

        //myCache.put(1L, "da one!");
        //String value = myCache.get(1L);

        //cacheManager.close();
    }

}
