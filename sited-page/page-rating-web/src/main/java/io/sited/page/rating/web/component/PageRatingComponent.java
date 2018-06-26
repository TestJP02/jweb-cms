package io.sited.page.rating.web.component;

import com.google.common.collect.ImmutableList;
import io.sited.page.api.page.PageResponse;
import io.sited.page.rating.api.RatingWebService;
import io.sited.page.rating.api.rating.RatingResponse;
import io.sited.template.Children;
import io.sited.template.ObjectAttribute;
import io.sited.template.TemplateComponent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chi
 */
public class PageRatingComponent extends TemplateComponent {
    @Inject
    RatingWebService ratingWebService;

    public PageRatingComponent() {
        super("page-rating", "component/page-rating/page-rating.html", ImmutableList.of(
            new ObjectAttribute<>("page", PageResponse.class, null)));
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> scopedBindings = new HashMap<>();
        scopedBindings.putAll(bindings);
        scopedBindings.putAll(attributes);
        PageResponse page = (PageResponse) attribute("page").value(attributes);
        if (page == null) {
            page = (PageResponse) bindings.get("page");
        }
        if (page == null) {
            return;
        }
        RatingResponse ratingResponse = ratingWebService.findByPageId(page.id).orElse(defaultRating(page.id));
        scopedBindings.put("rating", ratingResponse);
        template().output(scopedBindings, out);
    }

    private RatingResponse defaultRating(String pageId) {
        RatingResponse ratingResponse = new RatingResponse();
        ratingResponse.pageId = pageId;
        ratingResponse.averageScore = 0d;
        ratingResponse.totalScored = 0;
        return ratingResponse;
    }
}
