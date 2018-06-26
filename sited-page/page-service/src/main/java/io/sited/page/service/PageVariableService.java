package io.sited.page.service;

import com.google.common.base.Strings;
import io.sited.database.Query;
import io.sited.database.Repository;
import io.sited.message.MessagePublisher;
import io.sited.page.api.variable.CreateVariableRequest;
import io.sited.page.api.variable.UpdateVariableRequest;
import io.sited.page.api.variable.VariableChangedMessage;
import io.sited.page.api.variable.VariableQuery;
import io.sited.page.api.variable.VariableStatus;
import io.sited.page.domain.PageVariable;
import io.sited.util.JSON;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author chi
 */
public class PageVariableService {
    @Inject
    Repository<PageVariable> repository;
    @Inject
    MessagePublisher<VariableChangedMessage> publisher;

    public PageVariable get(String id) {
        return repository.get(id);
    }

    public QueryResponse<PageVariable> find(VariableQuery variableQuery) {
        Query<PageVariable> query = repository.query("SELECT t FROM PageVariable t WHERE 1=1");
        int index = 0;
        if (!Strings.isNullOrEmpty(variableQuery.name)) {
            query.append("AND t.name LIKE ?" + index++, "%" + variableQuery.name + "%");
        }
        if (variableQuery.status != null) {
            query.append(" AND t.status = ?" + index, variableQuery.status);
        }
        return query.limit(variableQuery.page, variableQuery.limit).findAll();
    }

    public List<PageVariable> find() {
        return repository.query("SELECT t FROM PageVariable t WHERE t.status=?0", VariableStatus.ACTIVE).find();
    }

    @Transactional
    public PageVariable create(CreateVariableRequest request) {
        PageVariable pageVariable = new PageVariable();
        pageVariable.id = UUID.randomUUID().toString();
        pageVariable.name = request.name;
        pageVariable.status = VariableStatus.ACTIVE;
        pageVariable.fields = request.fields == null ? null : JSON.toJSON(request.fields);
        pageVariable.createdTime = OffsetDateTime.now();
        pageVariable.createdBy = request.requestBy;
        repository.insert(pageVariable);

        VariableChangedMessage message = new VariableChangedMessage();
        message.name = request.name;
        publisher.publish(message);

        return pageVariable;
    }

    @Transactional
    public PageVariable update(String id, UpdateVariableRequest request) {
        PageVariable pageVariable = get(id);
        if (request.fields != null) {
            pageVariable.fields = JSON.toJSON(request.fields);
        }
        if (!Strings.isNullOrEmpty(request.name)) {
            pageVariable.name = request.name;
        }
        pageVariable.updatedTime = OffsetDateTime.now();
        pageVariable.updatedBy = request.requestBy;

        VariableChangedMessage message = new VariableChangedMessage();
        message.name = request.name;
        publisher.publish(message);

        return repository.update(id, pageVariable);
    }

    @Transactional
    public boolean delete(String id, String requestBy) {
        PageVariable pageVariable = get(id);
        if (pageVariable.status.equals(VariableStatus.INACTIVE)) {
            VariableChangedMessage message = new VariableChangedMessage();
            message.name = pageVariable.name;
            publisher.publish(message);

            return repository.delete(id);
        } else {
            pageVariable.status = VariableStatus.INACTIVE;
            pageVariable.updatedBy = requestBy;
            pageVariable.updatedTime = OffsetDateTime.now();
            repository.update(id, pageVariable);

            VariableChangedMessage message = new VariableChangedMessage();
            message.name = pageVariable.name;
            publisher.publish(message);

            return true;
        }
    }
}
