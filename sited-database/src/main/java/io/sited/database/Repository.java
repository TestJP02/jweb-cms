package io.sited.database;

import java.util.List;

/**
 * @author chi
 */
public interface Repository<T> {
    Query<T> query(String query, Object... args);

    T get(Object id);

    List<T> batchGet(List ids);

    T insert(T entity);

    List<T> batchInsert(List<T> entities);

    T update(Object id, T entity);

    boolean delete(Object id);

    int batchDelete(List ids);

    int execute(String query, Object... args);
}
