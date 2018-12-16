package app.jweb.page.search.web.web;

import app.jweb.page.web.AbstractPageController;
import app.jweb.page.web.PostInfo;
import app.jweb.util.i18n.MessageBundle;
import app.jweb.web.ClientInfo;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

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
public class PageSearchController extends AbstractPageController {
    @Inject
    MessageBundle messageBundle;

    @Inject
    ClientInfo clientInfo;

    @Inject
    UriInfo uriInfo;

    @GET
    public Response search(@QueryParam("q") String keyword) {
        return post(post(keyword), ImmutableMap.of());
    }

    private PostInfo post(String keyword) {
        String search = messageBundle.get("page.search", clientInfo.language()).orElse("search");
        return PostInfo.builder()
            .setPath(uriInfo.getPath())
            .setTitle(search + keyword)
            .setTemplatePath("template/search.html")
            .setKeywords(Lists.newArrayList(keyword))
            .setDescription(search + keyword)
            .build();
    }
}
