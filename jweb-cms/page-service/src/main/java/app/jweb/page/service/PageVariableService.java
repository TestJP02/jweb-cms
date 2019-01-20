package app.jweb.page.service;

import app.jweb.database.Query;
import app.jweb.database.Repository;
import app.jweb.message.MessagePublisher;
import app.jweb.page.api.variable.CreateVariableRequest;
import app.jweb.page.api.variable.DeleteVariableRequest;
import app.jweb.page.api.variable.UpdateVariableRequest;
import app.jweb.page.api.variable.VariableChangedMessage;
import app.jweb.page.api.variable.VariableQuery;
import app.jweb.page.api.variable.VariableStatus;
import app.jweb.page.domain.PageVariable;
import app.jweb.util.JSON;
import app.jweb.util.collection.QueryResponse;
import com.google.common.base.Strings;

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
    public void delete(DeleteVariableRequest request) {
        for (String id : request.ids) {
            PageVariable pageVariable = get(id);
            repository.delete(id);
            VariableChangedMessage message = new VariableChangedMessage();
            message.name = pageVariable.name;
            publisher.publish(message);
        }
    }
}
