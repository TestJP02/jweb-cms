package io.sited.cache.local.impl;

import io.sited.Binder;
import io.sited.cache.Cache;
import io.sited.cache.CacheConfig;
import io.sited.cache.CacheOptions;
import io.sited.cache.CacheVendor;
import io.sited.util.type.Types;

import java.lang.reflect.Type;

/**
 * @author chi
 */
public class CacheConfigImpl implements CacheConfig {
    private final Binder binder;
    private final CacheManager cacheManager;

    public CacheConfigImpl(Binder binder, CacheManager cacheManager) {
        this.binder = binder;
        this.cacheManager = cacheManager;
    }

    @Override
    public <T> Cache<T> create(Type type, CacheOptions options) {
        Cache<T> cache = cacheManager.create(type, options);
        binder.bind(Types.generic(Cache.class, type)).toInstance(cache);
        return cache;
    }

    @Override
    public void setVendor(CacheVendor vendor) {
        cacheManager.setVendor(vendor);
    }
}
