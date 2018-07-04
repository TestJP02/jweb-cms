package io.sited.page.search.web.web;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.sited.page.web.AbstractPageWebController;
import io.sited.page.web.PageInfo;
import io.sited.util.i18n.MessageBundle;
import io.sited.web.ClientInfo;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author chi
 */
@Path("/search")
public class PageSearchController extends AbstractPageWebController {
    @Inject
    MessageBundle messageBundle;

    @Inject
    ClientInfo clientInfo;

    @Inject
    UriInfo uriInfo;

    @GET
    public Response search(@QueryParam("q") String keyword) {
        return page(page(keyword), ImmutableMap.of());
    }

    private PageInfo page(String keyword) {
        String search = messageBundle.get("page.search", clientInfo.language()).orElse("search");
        return PageInfo.builder()
            .setPath(uriInfo.getPath())
            .setTitle(search + keyword)
            .setTemplatePath("template/search.html")
            .setKeywords(Lists.newArrayList(keyword))
            .setDescription(search + keyword)
            .build();
    }
}
