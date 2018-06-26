package io.sited.cache;

import com.google.common.collect.ImmutableList;
import io.sited.AbstractModule;
import io.sited.Binder;
import io.sited.Configurable;
import io.sited.cache.local.impl.CacheConfigImpl;
import io.sited.cache.local.impl.CacheManager;
import io.sited.cache.local.impl.LocalCacheVendor;

import java.util.List;

/**
 * @author chi
 */
public class CacheModule extends AbstractModule implements Configurable<CacheConfig> {
    private final CacheManager cacheManager = new CacheManager();

    @Override
    public void configure() {
        cacheManager.setVendor(new LocalCacheVendor());
        bind(CacheManager.class).toInstance(cacheManager);
    }

    @Override
    public List<String> declareRoles() {
        return ImmutableList.of();
    }

    @Override
    public CacheConfig configurator(AbstractModule module, Binder binder) {
        return new CacheConfigImpl(binder, cacheManager);
    }
}
