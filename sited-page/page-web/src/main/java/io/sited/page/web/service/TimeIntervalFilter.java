//package io.sited.page.web.service;
//
//import com.google.common.collect.ImmutableMap;
//import io.sited.exception.StandardException;
//import io.sited.util.i18n.MessageBundle;
//
//import javax.ws.rs.core.Request;
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.time.OffsetDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Locale;
//import java.util.Map;
//import java.util.Optional;
//
///**
// * @author chi
// */
//public class TimeIntervalFilter implements ExpressionFilter {
//    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
//    private static final ImmutableMap<String, String> MESSAGES = ImmutableMap.of("timeInterval.minuteBefore", "minute before", "timeInterval.minutesBefore", "minutes before", "timeInterval.hourBefore", "hour before", "timeInterval.hoursBefore", "hours before");
//    private final MessageBundle messageBundle;
//
//    public TimeIntervalFilter(MessageBundle messageBundle) {
//        this.messageBundle = messageBundle;
//    }
//
//    @Override
//    public Object execute(Object value, Object[] params, Map<String, Object> bindings) {
//        if (params.length < 1) {
//            throw new StandardException("invalid time interval params, missing param");
//        }
//        LocalDateTime time = ((OffsetDateTime) params[0]).toLocalDateTime();
//        LocalDateTime now = LocalDateTime.now();
//        Duration duration = Duration.between(time, now);
//        long seconds = duration.getSeconds();
//        if (seconds <= 0) {
//            return time.format(FORMATTER);
//        }
//        if (seconds - 60 < 0) {
//            return String.format("1 %s", text("timeInterval.minuteBefore", bindings));
//        }
//        long minutes = duration.toMinutes();
//        if (minutes - 60 < 0) {
//            return String.format("%d %s", minutes, text("timeInterval.minutesBefore", bindings));
//        }
//        long hours = duration.toHours();
//        if (hours - 2 < 0) {
//            return String.format("1 %s", text("timeInterval.hourBefore", bindings));
//        }
//        if (hours - 24 < 0) {
//            return String.format("%d %s", hours, text("timeInterval.hoursBefore", bindings));
//        }
//        return time.format(FORMATTER);
//    }
//
//    private String text(String key, Map<String, Object> bindings) {
//        Optional<String> optional = messageBundle.get(key, locale(bindings));
//        return optional.orElse(MESSAGES.get(key));
//    }
//
//    private Locale locale(Map<String, Object> bindings) {
//        Request session = (Request) bindings.get("request");
//        if (session == null) {
//            return Locale.getDefault();
//        }
//        return session.clientInfo().locale();
//    }
//}
