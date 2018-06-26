package io.sited.undertow.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.sited.App;
import io.sited.util.exception.Exceptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import org.glassfish.jersey.internal.PropertiesDelegate;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.ContainerUtils;

import javax.ws.rs.core.SecurityContext;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chi
 */
public class UndertowHttpHandler implements HttpHandler {
    private final App app;
    private final UndertowHttpContainer container;

    public UndertowHttpHandler(App app, UndertowHttpContainer container) {
        this.app = app;
        this.container = container;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        try {
            ContainerRequest request = createContainerRequest(exchange);
            request.setWriter(new UndertowResponseWriter(exchange, container));
            container.getApplicationHandler().handle(request);
        } catch (Throwable e) {
            if (exchange.isResponseChannelAvailable()) {
                exchange.setStatusCode(500);
                exchange.getResponseHeaders().add(new HttpString("Content-Type"), "text/plain");
                exchange.getResponseSender().send(Exceptions.stackTrace(e));
            }
        }
    }

    private ContainerRequest createContainerRequest(HttpServerExchange exchange) {
        String queryString = exchange.getQueryString();
        String path = exchange.getRequestPath() + (Strings.isNullOrEmpty(queryString) ? "" : "?" + queryString);
        path = path.length() > 0 && path.charAt(0) == '/' ? path.substring(1) : path;
        URI requestUri = URI.create(app.baseURL() + ContainerUtils.encodeUnsafeCharacters(path));

        ContainerRequest requestContext = new ContainerRequest(
            URI.create(app.baseURL()), requestUri, exchange.getRequestMethod().toString(), getSecurityContext(),
            new PropertiesDelegate() {
                private final Map<String, Object> properties = new HashMap<>();

                @Override
                public Object getProperty(String name) {
                    return properties.get(name);
                }

                @Override
                public Collection<String> getPropertyNames() {
                    return properties.keySet();
                }

                @Override
                public void setProperty(String name, Object object) {
                    properties.put(name, object);
                }

                @Override
                public void removeProperty(String name) {
                    properties.remove(name);
                }
            });

        RequestBodyParser.RequestBody body = exchange.getAttachment(RequestBodyParser.REQUEST_BODY);
        if (body != null) {
            requestContext.setEntityStream(new ByteArrayInputStream(body.body()));
        }

        exchange.getRequestHeaders().forEach(header -> {
            requestContext.headers(header.getHeaderName().toString(), Lists.newArrayList(header));
        });

        requestContext.header("X-Client-IP", clientIP(exchange));
        return requestContext;
    }

    private String clientIP(HttpServerExchange exchange) {
        String xForwardedFor = exchange.getRequestHeaders().getFirst(Headers.X_FORWARDED_FOR_STRING);

        if (Strings.isNullOrEmpty(xForwardedFor)) {
            return exchange.getSourceAddress().getAddress().getHostAddress();
        }
        int index = xForwardedFor.indexOf(',');
        if (index > 0)
            return xForwardedFor.substring(0, index);
        return xForwardedFor;
    }

    private SecurityContext getSecurityContext() {
        return new SecurityContext() {
            @Override
            public boolean isUserInRole(final String role) {
                return false;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public Principal getUserPrincipal() {
                return null;
            }

            @Override
            public String getAuthenticationScheme() {
                return null;
            }
        };
    }
}
