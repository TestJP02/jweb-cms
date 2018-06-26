package io.sited.cache.local.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import io.sited.cache.CacheOptions;
import io.sited.cache.CacheVendor;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * @author chi
 */
public class LocalCacheVendor implements CacheVendor {
    @Override
    public <T> io.sited.cache.Cache<T> create(Type type, String cacheName, CacheOptions options) {
        Cache<String, T> cache = CacheBuilder.newBuilder().maximumSize(options.maxElements).expireAfterWrite(options.expireTime.toMillis(), TimeUnit.MILLISECONDS).build();
        return new LocaleCache<>(cache);
    }

    static class LocaleCache<T> implements io.sited.cache.Cache<T> {
        private final Cache<String, T> cache;

        LocaleCache(Cache<String, T> cache) {
            this.cache = cache;
        }

        @Override
        public Optional<T> get(String key) {
            return Optional.ofNullable(cache.getIfPresent(key));
        }

        @Override
        public Map<String, T> batchGet(List<String> keys) {
            Map<String, T> values = Maps.newHashMap();
            keys.forEach(key -> values.put(key, cache.getIfPresent(key)));
            return values;
        }

        @Override
        public void put(String key, T value) {
            cache.put(key, value);
        }

        @Override
        public void batchPut(Map<String, T> values) {
            values.forEach(cache::put);
        }

        @Override
        public void delete(String key) {
            cache.invalidate(key);
        }

        @Override
        public void batchDelete(List<String> keys) {
            keys.forEach(cache::invalidate);
        }
    }
}
