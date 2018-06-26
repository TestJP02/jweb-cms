package io.sited.util.type;

/**
 * @author chi
 */
public interface ObjectConstructor<T> {
    T newInstance(Object... args);
}
