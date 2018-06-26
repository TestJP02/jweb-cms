package io.sited.page.api;


import io.sited.page.api.content.PageContentResponse;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

/**
 * @author chi
 */
@Path("/api/page/content")
public interface PageContentWebService {
    @Path("/pageId/{pageId}")
    @GET
    PageContentResponse getByPageId(@PathParam("pageId") String pageId);

    @Path("/batch-get")
    @PUT
    List<PageContentResponse> batchGetByPageIds(List<String> pageIds);
}
