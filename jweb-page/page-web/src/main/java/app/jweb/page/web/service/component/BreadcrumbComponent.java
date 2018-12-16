package app.jweb.page.web.service.component;

import app.jweb.page.web.AbstractPostComponent;
import app.jweb.page.web.Bindings;
import app.jweb.page.web.PostInfo;
import app.jweb.post.api.PostCategoryWebService;
import app.jweb.post.api.category.CategoryResponse;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author chi
 */
public class BreadcrumbComponent extends AbstractPostComponent {
    @Inject
    PostCategoryWebService postCategoryWebService;

    public BreadcrumbComponent() {
        super("breadcrumb", "component/post-breadcrumb/post-breadcrumb.html");
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        PostInfo post = bindings.post();
        if (post.categoryId() != null) {
            List<CategoryResponse> categories = Lists.newArrayList();
            categories.addAll(postCategoryWebService.parents(post.categoryId()));
            categories.add(postCategoryWebService.get(post.categoryId()));
            bindings.put("categories", categories);
            template().output(bindings, out);
        }
    }
}
