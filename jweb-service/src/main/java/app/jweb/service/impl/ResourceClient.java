package app.jweb.service.impl;

import app.jweb.ApplicationException;
import app.jweb.util.type.Types;
import com.google.common.collect.Sets;
import app.jweb.service.impl.exception.ExceptionResponse;
import app.jweb.service.impl.exception.ValidationExceptionResponse;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyInvocation;
import org.glassfish.jersey.client.JerseyWebTarget;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.ValidationException;
import javax.validation.metadata.ConstraintDescriptor;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

/**
 * @author chi
 */
public class ResourceClient {
    private final String serviceURL;
    private final JerseyClient client;

    public ResourceClient(String serviceURL, JerseyClient client) {
        this.serviceURL = serviceURL;
        this.client = client;
    }

    @SuppressWarnings({"unchecked", "RV"})
    public Object execute(String method, String path, Map pathParams, Map queryParams, Type bodyType, Object body, Type returnType) {
        JerseyWebTarget target = client.target(serviceURL).path(new ResourcePath(path).format(pathParams));
        Set<Map.Entry> entries = queryParams.entrySet();
        for (Map.Entry entry : entries) {
            target = target.queryParam((String) entry.getKey(), (String) entry.getValue());
        }

        JerseyInvocation.Builder request = target.request();
        request.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);

        Response response;
        switch (method) {
            case "GET":
                response = request.get();
                break;
            case "DELETE":
                response = request.delete();
                break;
            case "POST":
                response = request.post(body == null ? Entity.json(null) : Entity.json(body));
                break;
            case "PUT":
                response = request.put(body == null ? Entity.json(null) : Entity.json(body));
                break;
            default:
                throw new ApplicationException("method not support, method={}", method);
        }

        switch (response.getStatus()) {
            case 200:
                return readEntity(response, returnType);
            case 404:
                ExceptionResponse notFoundException = readEntity(response, ExceptionResponse.class);
                if (notFoundException != null) {
                    throw new NotFoundException(notFoundException.errorMessage);
                }
                throw new NotFoundException();
            case 400:
                ValidationExceptionResponse validationException = readEntity(response, ValidationExceptionResponse.class);
                if (validationException == null || validationException.path == null) {
                    throw new ValidationException(validationException == null ? "" : validationException.errorMessage);
                } else {
                    Set<? extends ConstraintViolation<?>> violations = Sets.newHashSet(new ConstraintViolationImpl<>(validationException.errorMessage));
                    throw new ConstraintViolationException(validationException.errorMessage, violations);
                }
            default:
                ExceptionResponse serverException = readEntity(response, ExceptionResponse.class);
                if (serverException != null) {
                    throw new ApplicationException(serverException.errorMessage);
                }
                throw new ApplicationException("request failed, status={}", response.getStatus());
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T readEntity(Response response, Type returnType) {
        if (returnType == void.class || returnType == Void.class) {
            return null;
        } else if (Types.isGeneric(returnType)) {
            return response.readEntity(new GenericType<>(returnType));
        } else {
            return response.readEntity((Class<T>) returnType);
        }
    }

    static class ConstraintViolationImpl<T> implements ConstraintViolation<T> {
        private final String message;

        ConstraintViolationImpl(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public String getMessageTemplate() {
            return null;
        }

        @Override
        public T getRootBean() {
            return null;
        }

        @Override
        public Class<T> getRootBeanClass() {
            return null;
        }

        @Override
        public Object getLeafBean() {
            return null;
        }

        @Override
        public Object[] getExecutableParameters() {
            return new Object[0];
        }

        @Override
        public Object getExecutableReturnValue() {
            return null;
        }

        @Override
        public Path getPropertyPath() {
            return null;
        }

        @Override
        public Object getInvalidValue() {
            return null;
        }

        @Override
        public ConstraintDescriptor<?> getConstraintDescriptor() {
            return null;
        }

        @Override
        public <U> U unwrap(Class<U> type) {
            return null;
        }
    }
}
