package app.jweb.page.service;

import app.jweb.database.Query;
import app.jweb.database.Repository;
import app.jweb.message.MessagePublisher;
import app.jweb.page.api.component.CreateSavedComponentRequest;
import app.jweb.page.api.component.DeleteSavedComponentRequest;
import app.jweb.page.api.component.SavedComponentChangedMessage;
import app.jweb.page.api.component.SavedComponentQuery;
import app.jweb.page.api.component.SavedComponentStatus;
import app.jweb.page.api.component.UpdateSavedComponentRequest;
import app.jweb.page.domain.PageSavedComponent;
import app.jweb.util.JSON;
import app.jweb.util.collection.QueryResponse;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
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
        pageComponent.name = request.name;
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
    public void delete(DeleteSavedComponentRequest request) {
        List<PageSavedComponent> pageSavedComponents = batchGet(request.ids);
        for (PageSavedComponent pageSavedComponent : pageSavedComponents) {
            pageSavedComponent.status = SavedComponentStatus.INACTIVE;
            notifyPageSavedComponentChanged(pageSavedComponent);
        }
        repository.batchDelete(request.ids);
    }

    public List<PageSavedComponent> batchGet(List<String> savedComponentsId) {
        return repository.batchGet(savedComponentsId);
    }

    public void notifyPageSavedComponentChanged(PageSavedComponent pageComponent) {
        SavedComponentChangedMessage message = new SavedComponentChangedMessage();
        message.id = pageComponent.id;
        message.name = pageComponent.name;
        message.componentName = pageComponent.componentName;
        message.displayName = pageComponent.displayName;
        message.attributes = pageComponent.attributes == null ? ImmutableMap.of() : JSON.fromJSON(pageComponent.attributes, Map.class);
        message.status = pageComponent.status;
        message.createdBy = pageComponent.createdBy;
        message.createdTime = pageComponent.createdTime;
        message.updatedBy = pageComponent.updatedBy;
        message.updatedTime = pageComponent.updatedTime;
        publisher.publish(message);
    }
}
