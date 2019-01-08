package app.jweb.page.web.web;

import app.jweb.message.MessagePublisher;
import app.jweb.page.api.PageWebService;
import app.jweb.page.api.page.PageResponse;
import app.jweb.page.api.page.PageStatus;
import app.jweb.page.api.statistics.PageVisitedMessage;
import app.jweb.page.web.AbstractPageController;
import app.jweb.template.Template;
import app.jweb.template.TemplateEngine;
import app.jweb.web.ClientInfo;
import app.jweb.web.NotFoundWebException;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/")
public class PageController extends AbstractPageController {
    @Inject
    MessagePublisher<PageVisitedMessage> publisher;
    @Inject
    TemplateEngine templateEngine;
    @Inject
    PageWebService pageWebService;
    @Inject
    UriInfo uriInfo;
    @Inject
    ClientInfo clientInfo;

    @GET
    public Response index(@QueryParam("draft") Boolean draft) {
        return handle(draft);
    }

    @GET
    @Path("{s:.+}")
    public Response handle(@QueryParam("draft") Boolean draft) {
        String path = normalize("/" + uriInfo.getPath());
        Optional<PageResponse> pageOptional = pageWebService.findByPath(path);
        if (pageOptional.isEmpty()) {
            return tryTemplate(path).orElseThrow(() -> new NotFoundWebException("missing page, path={}", path));
        }
        PageResponse page = pageOptional.get();
        if (page.status == PageStatus.NEW && !Boolean.TRUE.equals(draft)) {
            return tryTemplate(path).orElseThrow(() -> new NotFoundWebException("missing page, path={}", path));
        }
        Map<String, Object> bindings = Maps.newHashMap();
        notifyPageVisited(page);
        return page(page, bindings).build();
    }

    private String normalize(String path) {
        StringBuilder b = new StringBuilder();
        boolean hyphen = false;
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            if (c == '-') {
                if (!hyphen) {
                    b.append(Character.toLowerCase(c));
                    hyphen = true;
                }
            } else {
                hyphen = false;
                b.append(Character.toLowerCase(c));
            }
        }
        return b.toString();
    }

    private Optional<Response> tryTemplate(String path) {
        String templatePath = fullPath(path);
        if (templatePath.startsWith("component/") || templatePath.startsWith("template/")) {
            return Optional.empty();
        }
        Optional<Template> templateOptional = templateEngine.template(templatePath);
        if (templateOptional.isPresent()) {
            return Optional.of(template(templatePath).build());
        }
        return Optional.empty();
    }

    private String fullPath(String path) {
        if (path.endsWith(".html")) {
            return path.substring(1);
        } else {
            return path.substring(1) + ".html";
        }
    }

    private void notifyPageVisited(PageResponse page) {
        PageVisitedMessage message = new PageVisitedMessage();
        message.pageId = page.id;
        message.categoryId = page.categoryId;
        message.timestamp = OffsetDateTime.now();
        message.clientId = clientInfo.id();
        publisher.publish(message);
    }
}

