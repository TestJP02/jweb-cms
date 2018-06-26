package io.sited.database.impl;

import io.sited.ApplicationException;
import io.sited.database.Repository;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

/**
 * @author chi
 */
@Singleton
public class TestService {
    @Inject
    Repository<TestEntity> repository;

    public TestEntity get(String id) {
        return repository.get(id);
    }

    @Transactional
    public void create(TestEntity testEntity) {
        repository.insert(testEntity);
    }

    @Transactional
    public void update(TestEntity testEntity) {
        repository.update(testEntity.id, testEntity);
    }

    @Transactional
    public void delete(String id) {
        repository.delete(id);
    }

    @Transactional
    public void nestTransaction(TestEntity testEntity) {
        testEntity.name = "Test1";
        update(testEntity);

        testEntity.name = "Test2";
        update(testEntity);
    }

    @Transactional
    public void rollback(TestEntity testEntity) {
        testEntity.name = "Test1";
        update(testEntity);

        throw new ApplicationException("rollback");
    }

    @Transactional(dontRollbackOn = ApplicationException.class)
    public void notRollback(TestEntity testEntity) {
        testEntity.name = "Test1";
        update(testEntity);

        throw new ApplicationException("rollback");
    }

    @Transactional(rollbackOn = ApplicationException.class)
    public void rollbackON(TestEntity testEntity) {
        testEntity.name = "Test1";
        update(testEntity);

        throw new ApplicationException("rollback");
    }
}
