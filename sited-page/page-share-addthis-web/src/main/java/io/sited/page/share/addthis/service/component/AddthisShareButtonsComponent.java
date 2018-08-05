package io.sited.page.share.addthis.service.component;


import com.google.common.collect.Lists;
import io.sited.page.share.addthis.AddthisOptions;
import io.sited.page.web.AbstractPageComponent;
import io.sited.page.web.Bindings;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.StringAttribute;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chi
 */
public class AddthisShareButtonsComponent extends AbstractPageComponent {
    private final AddthisOptions addthisOptions;

    public AddthisShareButtonsComponent(AddthisOptions addthisOptions) {
        super("addthis-share-buttons", "component/addthis-share-buttons/addthis-share-buttons.html", Lists.newArrayList(new StringAttribute("title", null)));
        this.addthisOptions = addthisOptions;
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream outputStream) throws IOException {
        if (addthisOptions.id != null) {
            bindings.putAll(attributes);
            bindings.put("id", addthisOptions.id);
            template().output(bindings, outputStream);
        }
    }
}
