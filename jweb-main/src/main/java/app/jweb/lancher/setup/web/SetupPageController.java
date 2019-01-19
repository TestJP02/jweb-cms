package app.jweb.lancher.setup.web;

import app.jweb.App;
import app.jweb.lancher.setup.service.BundleMessageBuilder;
import app.jweb.util.JSON;
import app.jweb.util.i18n.MessageBundle;
import app.jweb.web.AbstractWebController;
import app.jweb.web.RequestInfo;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author Jonathan.Guo
 */
@Path("/{s:.*}")
public class SetupPageController extends AbstractWebController {
    @Inject
    App app;

    @Inject
    RequestInfo requestInfo;

    @GET
    public Response page() {
        String language = requestInfo.queryParam("language").orElse(Locale.US.toLanguageTag());
        HashMap<String, Object> bindings = Maps.newHashMap();
        bindings.put("script", script(language));
        bindings.put("language", language);
        return template("setup.html", bindings).build();
    }

    private String script(String language) {
        StringBuilder b = new StringBuilder();
        MessageBundle messageBundle = app.message().messageBundle("conf/messages/setup").orElse(null);
        b.append("window.app.messages=").append(JSON.toJSON(new BundleMessageBuilder(messageBundle, language).build())).append(';');
        return b.toString();
    }

}
