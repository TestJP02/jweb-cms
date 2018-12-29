package app.jweb.page.rating.web.component;

import app.jweb.page.rating.api.RatingWebService;
import app.jweb.page.rating.api.rating.RatingResponse;
import app.jweb.page.web.AbstractPostComponent;
import app.jweb.page.web.Bindings;
import app.jweb.page.web.PostInfo;
import app.jweb.template.Attributes;
import app.jweb.template.Children;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chi
 */
public class PageRatingComponent extends AbstractPostComponent {
    @Inject
    RatingWebService ratingWebService;

    public PageRatingComponent() {
        super("page-rating", "component/page-rating/page-rating.html");
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);
        PostInfo page = bindings.post();
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
