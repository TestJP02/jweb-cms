package io.sited.page.archive.web;


import io.sited.page.archive.api.PageArchiveWebService;
import io.sited.page.archive.api.archive.PageArchiveQuery;
import io.sited.page.archive.api.archive.PageArchiveResponse;
import io.sited.page.archive.domain.PageArchive;
import io.sited.page.archive.service.PageArchiveService;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @author chi
 */
public class PageArchiveWebServiceImpl implements PageArchiveWebService {
    @Inject
    PageArchiveService pageArchiveService;

    @Override
    public QueryResponse<PageArchiveResponse> find(PageArchiveQuery pageArchiveQuery) {
        return pageArchiveService.find(pageArchiveQuery).map(this::response);
    }

    @Override
    public Optional<PageArchiveResponse> findByName(String name) {
        return pageArchiveService.findByName(name).map(this::response);
    }

    private PageArchiveResponse response(PageArchive pageArchive) {
        PageArchiveResponse response = new PageArchiveResponse();
        response.id = pageArchive.id;
        response.name = pageArchive.name;
        response.timestamp = pageArchive.timestamp;
        response.count = pageArchive.count;
        response.createdTime = pageArchive.createdTime;
        response.updatedTime = pageArchive.updatedTime;
        response.createdBy = pageArchive.createdBy;
        response.updatedBy = pageArchive.updatedBy;
        return response;
    }
}
