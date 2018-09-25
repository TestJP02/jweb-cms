package app.jweb.redis;

import app.jweb.cache.CacheModule;
import app.jweb.redis.impl.NoneCacheVendor;
import app.jweb.redis.impl.RedisOptions;
import app.jweb.AbstractModule;
import app.jweb.redis.impl.RedisCacheVendor;

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
