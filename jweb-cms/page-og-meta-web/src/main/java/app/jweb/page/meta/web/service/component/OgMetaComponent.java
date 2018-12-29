package app.jweb.page.meta.web.service.component;

import app.jweb.page.web.PostInfo;
import app.jweb.template.AbstractComponent;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.web.RequestInfo;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class OgMetaComponent extends AbstractComponent {
    public OgMetaComponent() {
        super("og-meta", ImmutableList.of());
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        Object page = bindings.get("page");
        if (page instanceof PostInfo) {
            PostInfo pageInfo = (PostInfo) page;
            RequestInfo request = (RequestInfo) bindings.get("request");

            String type = pageInfo.fields().get("type");
            if (Strings.isNullOrEmpty(type)) {
                type = "article";
            }
            String metas = "<meta property=\"og:title\" content=\"" + pageInfo.title() + "\"/>"
                + "<meta property=\"og:type\" content=\"" + type + "\"/>"
                + "<meta property=\"og:image\" content=\"" + pageInfo.imageURL() + "\"/>"
                + "<meta property=\"og:url\" content=\"" + request.uri() + "\"/>"
                + "<meta property=\"og:description\" content=\"" + pageInfo.description() + "\"/>";
            out.write(metas.getBytes(Charsets.UTF_8));
        }
    }
}
