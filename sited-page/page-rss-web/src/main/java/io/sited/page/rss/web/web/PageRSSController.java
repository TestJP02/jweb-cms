package io.sited.page.rss.web.web;

import com.google.common.base.Charsets;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import io.sited.ApplicationException;
import io.sited.page.api.PageCategoryWebService;
import io.sited.page.api.PageWebService;
import io.sited.page.api.category.CategoryResponse;
import io.sited.page.api.page.PageQuery;
import io.sited.page.api.page.PageResponse;
import io.sited.page.api.page.PageStatus;
import io.sited.page.rss.web.PageRSSOptions;
import io.sited.util.MediaTypes;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author chi
 */
@Path("/feed.xml")
public class PageRSSController {
    @Inject
    PageRSSOptions options;

    @Inject
    PageWebService pageWebService;

    @Inject
    PageCategoryWebService pageCategoryWebService;

    @Inject
    UriInfo uriInfo;

    @GET
    public Response get() throws IOException, FeedException {
        CategoryResponse category = pageCategoryWebService.findByPath("/").orElseThrow(() -> new NotFoundException("missing category, path=/"));

        PageQuery pageQuery = new PageQuery();
        pageQuery.page = 1;
        pageQuery.categoryId = category.id;
        pageQuery.limit = options.displayCount;
        pageQuery.status = PageStatus.ACTIVE;
        QueryResponse<PageResponse> pages = pageWebService.find(pageQuery);

        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle(category.displayName);
        feed.setLink(category.path);
        feed.setDescription(category.description);
        feed.setEntries(pages.items.stream().map(page -> {
            try {
                return entry(uriInfo.resolve(new URI("/")), page);
            } catch (URISyntaxException e) {
                throw new ApplicationException(e);
            }
        }).collect(Collectors.toList()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        SyndFeedOutput syndFeedOutput = new SyndFeedOutput();
        syndFeedOutput.output(feed, new OutputStreamWriter(output, Charsets.UTF_8));
        return Response.ok(output.toByteArray())
            .type(MediaTypes.getMediaType("xml")).build();
    }

    private SyndEntry entry(URI baseURI, PageResponse pageView) {
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle(pageView.title);
        entry.setLink(baseURI.resolve(pageView.path).toString());
        entry.setPublishedDate(Date.from(pageView.updatedTime.toInstant()));
        SyndContent content = new SyndContentImpl();
        content.setType("text/plain");
        content.setValue(pageView.description);
        entry.setDescription(content);
        return entry;
    }
}
