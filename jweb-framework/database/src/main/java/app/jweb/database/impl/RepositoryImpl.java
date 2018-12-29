package app.jweb.database.impl;

import app.jweb.database.Database;
import app.jweb.database.Repository;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import app.jweb.database.Query;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.TypedQuery;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author chi
 */
public class RepositoryImpl<T> implements Repository<T> {
    private final Class<T> entityClass;
    private final Database database;

    private final String entityName;
    private final String idFieldName;

    public RepositoryImpl(Class<T> entityClass, Database database) {
        this.entityClass = entityClass;
        this.database = database;

        entityName = entityName(entityClass);
        idFieldName = idFieldName(entityClass);
    }

    private String entityName(Class<?> entityClass) {
        String name = entityClass.getDeclaredAnnotation(Entity.class).name();
        if (Strings.isNullOrEmpty(name)) {
            return entityClass.getSimpleName();
        } else {
            return name;
        }
    }

    private String idFieldName(Class<?> entityClass) {
        for (Field field : entityClass.getFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field.getName();
            }
        }
        return null;
    }


    @Override
    public Query<T> query(String query, Object... args) {
        return new QueryImpl<>(entityClass, entityName, idFieldName, database, query, args);
    }

    @Override
    public T get(Object id) {
        EntityManager em = database.em();
        try {
            T entity = em.find(entityClass, id);
            em.detach(entity);
            return entity;
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> batchGet(List ids) {
        if (ids.isEmpty()) {
            return ImmutableList.of();
        }
        EntityManager em = database.em();
        try {
            TypedQuery<T> query = em.createQuery(new SQLBuilder(entityName, idFieldName, "").findByIdsSQL(ids.size()), entityClass);
            for (int i = 0; i < ids.size(); i++) {
                query.setParameter(i, ids.get(i));
            }
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public T insert(T entity) {
        EntityManager em = database.em();
        try {
            em.persist(entity);
            return entity;
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> batchInsert(List<T> entities) {
        EntityManager em = database.em();
        try {
            for (T entity : entities) {
                em.persist(entity);
            }
            return entities;
        } finally {
            em.close();
        }
    }

    @Override
    public T update(Object id, T entity) {
        EntityManager em = database.em();
        try {
            return em.merge(entity);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(Object id) {
        return execute(new SQLBuilder(entityName, idFieldName, "").deleteByIdSQL(), id) == 1;
    }

    @Override
    public int batchDelete(List ids) {
        if (ids.isEmpty()) {
            return 0;
        }
        EntityManager em = database.em();
        try {
            javax.persistence.Query query = em.createQuery(new SQLBuilder(entityName, idFieldName, "").deleteByIdsSQL(ids.size()));
            for (int i = 0; i < ids.size(); i++) {
                query.setParameter(i, ids.get(i));
            }
            return query.executeUpdate();
        } finally {
            em.close();
        }
    }

    @Override
    public int execute(String sql, Object... params) {
        EntityManager em = database.em();
        try {
            javax.persistence.Query query = em.createQuery(sql);
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
            return query.executeUpdate();
        } finally {
            em.close();
        }
    }
}
