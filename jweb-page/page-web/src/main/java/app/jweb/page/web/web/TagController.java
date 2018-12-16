package app.jweb.page.web.web;

import app.jweb.page.web.AbstractPageController;
import app.jweb.page.web.PostInfo;
import app.jweb.post.api.PostTagWebService;
import app.jweb.post.api.tag.PostTagResponse;
import com.google.common.collect.ImmutableMap;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author chi
 */
@Path("/tag")
public class TagController extends AbstractPageController {
    @Inject
    PostTagWebService postTagWebService;

    @Path("/{tag}")
    @GET
    public Response tag(@PathParam("tag") String tag) {
        PostTagResponse postTagResponse = postTagWebService.findByName(tag).orElseThrow(() -> new NotFoundException("missing tag, tag=" + tag));
        return post(post(postTagResponse), ImmutableMap.of());
    }

    private PostInfo post(PostTagResponse tag) {
        return PostInfo.builder()
            .setTitle(tag.displayName)
            .setPath("/tag/" + tag.name)
            .setTemplatePath("template/tag.html")
            .build();
    }
}
