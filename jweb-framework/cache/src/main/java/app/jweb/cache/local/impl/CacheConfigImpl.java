package app.jweb.cache.local.impl;

import app.jweb.cache.Cache;
import app.jweb.cache.CacheOptions;
import app.jweb.cache.CacheVendor;
import app.jweb.Binder;
import app.jweb.cache.CacheConfig;
import app.jweb.util.type.Types;

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
