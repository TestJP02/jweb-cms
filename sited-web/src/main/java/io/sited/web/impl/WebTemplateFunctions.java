package io.sited.web.impl;

import io.sited.template.TemplateFunctions;
import io.sited.util.i18n.MessageBundle;
import io.sited.web.WebOptions;

import java.util.Optional;

/**
 * @author chi
 */
public class WebTemplateFunctions extends TemplateFunctions {
    private final MessageBundle messageBundle;
    private final WebOptions webOptions;

    public WebTemplateFunctions(MessageBundle messageBundle, WebOptions webOptions) {
        this.messageBundle = messageBundle;
        this.webOptions = webOptions;
    }

    public String i18n(String messageKey) {
        Optional<String> message = messageBundle.get(messageKey);
        return message.orElse(messageKey);
    }

    public String i18n(String messageKey, String language) {
        Optional<String> message = messageBundle.get(messageKey, language);
        return message.orElse(messageKey);
    }

    public String cdn(String url) {
        if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("//") || webOptions.cdnBaseURLs.isEmpty()) {
            return url;
        }
        return webOptions.cdnBaseURLs.get(Math.abs(url.hashCode() % webOptions.cdnBaseURLs.size())) + url;
    }
}
