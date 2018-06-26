package io.sited.page.web;

import io.sited.page.api.PageArchiveWebService;
import io.sited.page.api.archive.PageArchiveQuery;
import io.sited.page.api.archive.PageArchiveResponse;
import io.sited.page.domain.PageArchive;
import io.sited.page.service.PageArchiveService;
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
    public Optional<PageArchiveResponse> findByYearAndMonth(Integer year, Integer month) {
        return pageArchiveService.findByYearAndMonth(year, month).map(this::response);
    }

    private PageArchiveResponse response(PageArchive pageArchive) {
        PageArchiveResponse response = new PageArchiveResponse();
        response.id = pageArchive.id;
        response.year = pageArchive.year;
        response.month = pageArchive.month;
        response.count = pageArchive.count;
        response.createdTime = pageArchive.createdTime;
        response.updatedTime = pageArchive.updatedTime;
        response.createdBy = pageArchive.createdBy;
        response.updatedBy = pageArchive.updatedBy;
        return response;
    }
}
