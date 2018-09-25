package app.jweb.cache.local.impl;

import app.jweb.cache.Cache;
import app.jweb.cache.CacheOptions;
import app.jweb.cache.CacheVendor;
import com.google.common.collect.Maps;
import app.jweb.ApplicationException;
import app.jweb.util.type.Types;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author chi
 */
public class CacheManager {
    private final Map<String, Cache<?>> caches = Maps.newHashMap();
    private CacheVendor vendor;

    @SuppressWarnings("unchecked")
    public <T> Cache<T> cache(Class<T> cacheClass) {
        String cacheName = cacheName(cacheClass);
        Cache<T> cache = (Cache<T>) caches.get(cacheName);
        if (cache == null) {
            throw new ApplicationException("missing cache, type={}", cacheClass);
        }
        return cache;
    }

    public void setVendor(CacheVendor vendor) {
        this.vendor = vendor;
    }

    public <T> Cache<T> create(Type type, CacheOptions options) {
        String cacheName = cacheName(type);
        Cache<T> cache = vendor.create(type, cacheName, options);
        caches.put(cacheName, cache);
        return cache;
    }

    private String cacheName(Type type) {
        return Types.className(type);
    }
}
