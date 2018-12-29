package app.jweb.inject;

import org.junit.jupiter.api.Test;

import javax.inject.Provider;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author chi
 */
class ProviderSupplierBrideBuilderTest {
    @Test
    void build() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        ProviderSupplierBrideBuilder<Object> builder = new ProviderSupplierBrideBuilder<>(TestProvider.class);
        Class<? extends Supplier<Object>> supplierClass = builder.build();
        Supplier<Object> supplier = supplierClass.getDeclaredConstructor().newInstance();
        Field field = supplier.getClass().getDeclaredField("provider");
        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            field.setAccessible(true);
            return null;
        });
        field.set(supplier, new TestProvider());
        assertNotNull(supplier.get());
    }

    public static class TestProvider implements Provider<Object> {
        @Override
        public Object get() {
            return new Object();
        }
    }
}