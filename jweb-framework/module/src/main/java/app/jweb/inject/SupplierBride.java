package app.jweb.inject;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.function.Supplier;

/**
 * @author chi
 */
public class SupplierBride<T> implements Supplier<T> {
    @Inject
    Provider<T> provider;

    @Override
    public T get() {
        return provider.get();
    }
}
