package io.sited.redis;

import io.sited.AbstractModule;
import io.sited.cache.CacheModule;
import io.sited.redis.impl.NoneCacheVendor;
import io.sited.redis.impl.RedisOptions;
import io.sited.redis.impl.RedisCacheVendor;

/**
 * @author chi
 */
public class RedisModule extends AbstractModule {
    @Override
    protected void configure() {
        RedisOptions options = options("redis", RedisOptions.class);
        if (Boolean.TRUE.equals(options.enabled)) {
            module(CacheModule.class).setVendor(new RedisCacheVendor(options));
        } else {
            module(CacheModule.class).setVendor(new NoneCacheVendor());
        }
    }
}
