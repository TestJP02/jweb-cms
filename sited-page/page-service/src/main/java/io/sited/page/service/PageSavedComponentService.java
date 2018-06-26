package io.sited.page.service;

import com.google.common.base.Strings;
import io.sited.database.Query;
import io.sited.database.Repository;
import io.sited.message.MessagePublisher;
import io.sited.page.api.component.CreateSavedComponentRequest;
import io.sited.page.api.component.SavedComponentChangedMessage;
import io.sited.page.api.component.SavedComponentQuery;
import io.sited.page.api.component.SavedComponentStatus;
import io.sited.page.api.component.UpdateSavedComponentRequest;
import io.sited.page.domain.PageSavedComponent;
import io.sited.util.JSON;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class PageSavedComponentService {
    @Inject
    Repository<PageSavedComponent> repository;
    @Inject
    MessagePublisher<SavedComponentChangedMessage> publisher;

    public PageSavedComponent get(String id) {
        return repository.get(id);
    }

    public Optional<PageSavedComponent> findById(String id) {
        return repository.query("SELECT t FROM PageSavedComponent t WHERE t.id=?0 AND t.status=?1", id, SavedComponentStatus.ACTIVE).findOne();
    }

    public Optional<PageSavedComponent> findByName(String name) {
        return repository.query("SELECT t FROM PageSavedComponent t WHERE t.name=?0 AND t.status=?1", name, SavedComponentStatus.ACTIVE).findOne();
    }

    public QueryResponse<PageSavedComponent> find(SavedComponentQuery savedComponentQuery) {
        Query<PageSavedComponent> query = repository.query("SELECT t FROM PageSavedComponent t WHERE 1=1");
        int index = 0;
        if (!Strings.isNullOrEmpty(savedComponentQuery.query)) {
            query.append("AND t.name like ?" + index++, '%' + savedComponentQuery.query + '%');
        }
        if (savedComponentQuery.status != null) {
            query.append("AND t.status=?" + index, savedComponentQuery.status);
        }
        query.sort("t.name");
        return query.limit(savedComponentQuery.page, savedComponentQuery.limit).findAll();
    }

    public QueryResponse<PageSavedComponent> find() {
        return repository.query("SELECT t FROM PageSavedComponent t WHERE t.status=?0", SavedComponentStatus.ACTIVE).findAll();
    }

    @Transactional
    public PageSavedComponent create(CreateSavedComponentRequest request) {
        PageSavedComponent pageComponent = new PageSavedComponent();
        pageComponent.id = UUID.randomUUID().toString();
        pageComponent.name = request.name;
        pageComponent.componentName = request.componentName;
        pageComponent.displayName = request.displayName;
        pageComponent.status = SavedComponentStatus.ACTIVE;
        pageComponent.attributes = request.attributes == null ? null : JSON.toJSON(request.attributes);
        pageComponent.updatedBy = request.requestBy;
        pageComponent.createdBy = request.requestBy;
        pageComponent.createdTime = OffsetDateTime.now();
        pageComponent.updatedTime = OffsetDateTime.now();
        repository.insert(pageComponent);

        SavedComponentChangedMessage message = new SavedComponentChangedMessage();
        message.id = pageComponent.id;
        message.name = pageComponent.name;
        message.componentName = pageComponent.componentName;
        message.displayName = pageComponent.displayName;
        message.attributes = request.attributes;
        message.status = pageComponent.status;
        message.createdBy = pageComponent.createdBy;
        message.createdTime = pageComponent.createdTime;
        message.updatedBy = pageComponent.updatedBy;
        message.updatedTime = pageComponent.updatedTime;

        publisher.publish(message);

        return pageComponent;
    }

    @Transactional
    public PageSavedComponent update(String id, UpdateSavedComponentRequest request) {
        PageSavedComponent pageComponent = get(id);
        pageComponent.displayName = request.displayName;
        pageComponent.status = SavedComponentStatus.ACTIVE;
        pageComponent.attributes = request.attributes == null ? null : JSON.toJSON(request.attributes);
        pageComponent.updatedBy = request.requestBy;
        pageComponent.updatedTime = OffsetDateTime.now();
        repository.update(pageComponent.id, pageComponent);

        SavedComponentChangedMessage message = new SavedComponentChangedMessage();
        message.id = pageComponent.id;
        message.name = pageComponent.name;
        message.componentName = pageComponent.componentName;
        message.displayName = pageComponent.displayName;
        message.attributes = request.attributes;
        message.status = pageComponent.status;
        message.createdBy = pageComponent.createdBy;
        message.createdTime = pageComponent.createdTime;
        message.updatedBy = pageComponent.updatedBy;
        message.updatedTime = pageComponent.updatedTime;
        publisher.publish(message);

        return pageComponent;
    }

    @Transactional
    public void delete(String id, String requestBy) {
        PageSavedComponent pageComponent = get(id);
        if (pageComponent.status == SavedComponentStatus.ACTIVE) {
            pageComponent.status = SavedComponentStatus.INACTIVE;
            pageComponent.updatedTime = OffsetDateTime.now();
            pageComponent.updatedBy = requestBy;
            repository.update(id, pageComponent);
        } else {
            repository.delete(id);
        }
    }

    public List<PageSavedComponent> batchGet(List<String> savedComponentsId) {
        return repository.batchGet(savedComponentsId);
    }
}
