package app.jweb.util.type;

import app.jweb.ApplicationException;

import java.lang.reflect.Constructor;

/**
 * @author chi
 */
public interface Constructors {
    static <T> ObjectConstructor<T> of(Class<T> type, Class<?>... args) {
        try {
            Constructor<T> constructor = type.getDeclaredConstructor(args);
            return new DefaultObjectConstructor<>(constructor);
        } catch (NoSuchMethodException e) {
            throw new ApplicationException("failed to get constructor, type={}, args={}", type.getCanonicalName(), args, e);
        }
    }
}
