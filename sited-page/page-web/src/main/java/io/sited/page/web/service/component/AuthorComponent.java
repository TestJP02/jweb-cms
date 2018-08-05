package io.sited.page.web.service.component;

import com.google.common.collect.Lists;
import io.sited.page.api.PageWebService;
import io.sited.page.api.page.PageQuery;
import io.sited.page.web.AbstractPageComponent;
import io.sited.page.web.Bindings;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.StringAttribute;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chi
 */
public class AuthorComponent extends AbstractPageComponent {
    @Inject
    PageWebService pageWebService;

    public AuthorComponent() {
        super("author", "component/author/author.html", Lists.newArrayList(
            new StringAttribute("title", null),
            new StringAttribute("userId", null)));
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        bindings.putAll(attributes);

        String userId = attributes.get("userId");
        if (userId == null) {
            userId = bindings.page().userId();
        }

        bindings.put("userId", userId);
        PageQuery query = new PageQuery();
        query.userId = userId;
        query.limit = 5;
        query.sortingField = "updatedTime";
        query.desc = true;
        bindings.put("pages", pageWebService.find(query));

        template().output(bindings, out);
    }
}
