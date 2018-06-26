package io.sited.cache.local.impl;

import com.google.common.collect.Maps;
import io.sited.cache.Cache;
import io.sited.cache.CacheVendor;
import io.sited.ApplicationException;
import io.sited.util.type.Types;

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

    public <T> Cache<T> create(Type type, io.sited.cache.CacheOptions options) {
        String cacheName = cacheName(type);
        Cache<T> cache = vendor.create(type, cacheName, options);
        caches.put(cacheName, cache);
        return cache;
    }

    private String cacheName(Type type) {
        return Types.className(type);
    }
}
