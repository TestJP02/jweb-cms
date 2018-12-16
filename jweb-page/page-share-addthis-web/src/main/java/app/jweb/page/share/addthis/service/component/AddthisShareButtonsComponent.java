package app.jweb.page.share.addthis.service.component;


import app.jweb.page.share.addthis.AddthisOptions;
import app.jweb.page.web.AbstractPostComponent;
import app.jweb.page.web.Bindings;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.StringAttribute;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chi
 */
public class AddthisShareButtonsComponent extends AbstractPostComponent {
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
