package io.sited.page.search.web.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.page.api.page.PageResponse;
import io.sited.page.web.AbstractPageWebController;
import io.sited.util.i18n.MessageBundle;
import io.sited.web.ClientInfo;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * @author chi
 */
@Path("/search")
public class PageSearchController extends AbstractPageWebController {
    @Inject
    MessageBundle messageBundle;

    @Inject
    ClientInfo clientInfo;

    @GET
    public Response search(@QueryParam("q") String keyword) {
        PageResponse page = new PageResponse();
        String search = messageBundle.get("page.search", clientInfo.language()).orElse("search");
        page.title = search + keyword;
        page.description = search + keyword;
        page.templatePath = "template/search.html";
        page.keywords = Lists.newArrayList(keyword);
        page.tags = Lists.newArrayList();

        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("keyword", keyword);
        return page(page, bindings);
    }
}
