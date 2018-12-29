package app.jweb.web.impl;

import app.jweb.template.TemplateFunctions;
import app.jweb.util.i18n.MessageBundle;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

/**
 * @author chi
 */
public class WebTemplateFunctions extends TemplateFunctions {
    private final MessageBundle messageBundle;

    public WebTemplateFunctions(MessageBundle messageBundle) {
        this.messageBundle = messageBundle;
    }

    public String i18n(String messageKey, String language) {
        Optional<String> message = messageBundle.get(messageKey, language);
        return message.orElse(messageKey);
    }

    public String fromNow(OffsetDateTime timestamp, String language) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d", Locale.forLanguageTag(language));
        Duration duration = Duration.between(timestamp, OffsetDateTime.now());
        long seconds = duration.getSeconds();
        if (seconds <= 0) {
            return timestamp.format(formatter);
        }
        if (seconds - 60 < 0) {
            return String.format("1 %s", i18n("timeInterval.minuteBefore", language));
        }
        long minutes = duration.toMinutes();
        if (minutes - 60 < 0) {
            return String.format("%d %s", minutes, i18n("timeInterval.minutesBefore", language));
        }
        long hours = duration.toHours();
        if (hours - 2 < 0) {
            return String.format("1 %s", i18n("timeInterval.hourBefore", language));
        }
        if (hours - 24 < 0) {
            return String.format("%d %s", hours, i18n("timeInterval.hoursBefore", language));
        }
        return timestamp.format(formatter);
    }
}
