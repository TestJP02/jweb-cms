package io.sited.user.service;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.sited.database.Query;
import io.sited.database.Repository;
import io.sited.user.api.group.CreateUserGroupRequest;
import io.sited.user.api.group.UpdateUserGroupRequest;
import io.sited.user.api.group.UserGroupQuery;
import io.sited.user.api.user.UserGroupStatus;
import io.sited.user.domain.UserGroup;
import io.sited.util.collection.QueryResponse;
import io.sited.util.exception.Exceptions;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.time.OffsetDateTime.now;

/**
 * @author chi
 */
public class UserGroupService {
    @Inject
    Repository<UserGroup> repository;

    public UserGroup get(String id) {
        return repository.get(id);
    }

    public List<UserGroup> batchGet(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return ImmutableList.of();
        }
        StringBuilder sql = new StringBuilder(64);
        List<Object> params = Lists.newArrayList();
        sql.append("SELECT t FROM UserGroup t WHERE t.id IN (");
        int index = 0;
        for (String id : ids) {
            if (index != 0) {
                sql.append(',');
            }
            sql.append('?').append(index++);
            params.add(id);
        }
        sql.append(')');
        return repository.query(sql.toString(), params.toArray()).find();
    }

    public Optional<UserGroup> findByName(String name) {
        return repository.query("SELECT t FROM UserGroup t WHERE t.name = ?0", name).findOne();
    }

    public QueryResponse<UserGroup> find(UserGroupQuery query) {
        Query<UserGroup> dbQuery = repository.query("SELECT t FROM UserGroup t WHERE 1=1");
        int index = 0;
        if (!Strings.isNullOrEmpty(query.query)) {
            dbQuery.append("AND t.name LIKE ?" + index++, "%" + query.query + "%");
        }
        if (query.status != null) {
            dbQuery.append("AND t.status = ?" + index, query.status);
        }
        return dbQuery.sort("t.updatedTime", true).limit(query.page, query.limit).findAll();
    }

    public List<UserGroup> find() {
        return repository.query("SELECT t FROM UserGroup t").find();
    }

    public boolean isUserGroupExists(String name) {
        return findByName(name).isPresent();
    }

    @Transactional
    public UserGroup create(CreateUserGroupRequest request) {
        if (isUserGroupExists(request.name)) {
            throw Exceptions.badRequestException("name", "user.error.userGroupExists");
        }
        UserGroup userGroup = new UserGroup();
        userGroup.id = UUID.randomUUID().toString();
        userGroup.name = request.name;
        if (request.roles != null) {
            userGroup.roles = Joiner.on(",").join(request.roles);
        }
        userGroup.description = request.description;
        userGroup.status = request.status;
        userGroup.createdTime = now();
        userGroup.createdBy = request.requestBy;
        userGroup.updatedTime = now();
        userGroup.updatedBy = request.requestBy;
        repository.insert(userGroup);
        return userGroup;
    }

    @Transactional
    public UserGroup update(String id, UpdateUserGroupRequest request) {
        UserGroup userGroup = this.get(id);
        if (!Strings.isNullOrEmpty(request.name)) {
            userGroup.name = request.name;
        }
        if (!Strings.isNullOrEmpty(request.description)) {
            userGroup.description = request.description;
        }
        if (request.roles != null) {
            userGroup.roles = Joiner.on(",").join(request.roles);
        }
        userGroup.updatedTime = now();
        userGroup.updatedBy = request.requestBy;
        repository.update(id, userGroup);
        return userGroup;
    }

    @Transactional
    public boolean delete(String id, String requestBy) {
        UserGroup userGroup = get(id);
        if (userGroup.status.equals(UserGroupStatus.INACTIVE)) {
            return repository.delete(userGroup.id);
        } else {
            userGroup.status = UserGroupStatus.INACTIVE;
            userGroup.updatedBy = requestBy;
            userGroup.updatedTime = now();
            repository.update(userGroup.id, userGroup);
            return true;
        }
    }

    @Transactional
    public void revert(String id, String requestBy) {
        UserGroup userGroup = get(id);
        if (!userGroup.status.equals(UserGroupStatus.INACTIVE)) {
            throw Exceptions.badRequestException("status", "user.error.invalidUserGroupStatus");
        }
        userGroup.status = UserGroupStatus.ACTIVE;
        userGroup.updatedBy = requestBy;
        userGroup.updatedTime = now();
        repository.update(id, userGroup);
    }
}
