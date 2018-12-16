package app.jweb.main.web;

import app.jweb.post.api.PostWebService;
import app.jweb.post.api.post.PostQuery;
import app.jweb.post.api.post.PostResponse;
import app.jweb.util.collection.QueryResponse;
import app.jweb.web.AbstractWebController;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * @author chi
 */
@Path("/test")
public class TestController extends AbstractWebController {
    @Inject
    PostWebService pageWebService;

    @GET
    public Response get() {
        QueryResponse<PostResponse> pages = pageWebService.find(new PostQuery());
        Map<String, Object> bindings = Maps.newHashMap();
        bindings.put("pages", pages);
        return template("template/test.html", bindings);
    }
}
