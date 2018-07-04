package io.sited.page.rating.web.component;

import io.sited.page.rating.api.RatingWebService;
import io.sited.page.rating.api.rating.RatingResponse;
import io.sited.page.web.AbstractPageComponent;
import io.sited.page.web.ComponentBindings;
import io.sited.page.web.PageInfo;
import io.sited.template.Attributes;
import io.sited.template.Children;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chi
 */
public class PageRatingComponent extends AbstractPageComponent {
    @Inject
    RatingWebService ratingWebService;

    public PageRatingComponent() {
        super("page-rating", "component/page-rating/page-rating.html");
    }

    @Override
    public void output(ComponentBindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);
        PageInfo page = bindings.page();
        RatingResponse ratingResponse = ratingWebService.findByPageId(page.id()).orElse(defaultRating(page.id()));
        bindings.put("rating", ratingResponse);
        template().output(bindings, out);
    }

    private RatingResponse defaultRating(String pageId) {
        RatingResponse ratingResponse = new RatingResponse();
        ratingResponse.pageId = pageId;
        ratingResponse.averageScore = 0d;
        ratingResponse.totalScored = 0;
        return ratingResponse;
    }
}
