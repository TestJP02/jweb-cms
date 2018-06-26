package io.sited.undertow.impl;

import io.sited.App;
import io.sited.util.exception.Exceptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import org.xnio.channels.StreamSourceChannel;

public class UndertowIOHandler implements HttpHandler {
    private final HttpHandler next;

    public UndertowIOHandler(App app, UndertowHttpContainer container) {
        next = new UndertowHttpHandler(app, container);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }
        try {
            if (hasBody(exchange)) {
                RequestBodyParser reader = new RequestBodyParser(exchange, next);
                StreamSourceChannel channel = exchange.getRequestChannel();
                reader.read(channel);
                if (!reader.complete()) {
                    channel.getReadSetter().set(reader);
                    channel.resumeReads();
                    return;
                }
            }
            exchange.dispatch(next);
        } catch (Throwable e) {
            if (exchange.isResponseChannelAvailable()) {
                exchange.setStatusCode(500);
                exchange.getResponseHeaders().add(new HttpString("Content-Type"), "text/plain");
                exchange.getResponseSender().send(Exceptions.stackTrace(e));
            }
        }
    }

    private boolean hasBody(HttpServerExchange exchange) {
        int length = (int) exchange.getRequestContentLength();
        if (length == 0) return false;

        HttpString method = exchange.getRequestMethod();
        return Methods.POST.equals(method) || Methods.PUT.equals(method);
    }
}