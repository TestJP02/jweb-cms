package app.jweb.service.impl;


import app.jweb.AbstractModule;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author chi
 */
public class ResourceMethod {
    private static final Set<Class<? extends Annotation>> PARAM_ANNOTATIONS = Sets.newHashSet(PathParam.class, QueryParam.class,
        HeaderParam.class, CookieParam.class, MatrixParam.class, FormParam.class);
    private final String httpMethod;
    private final String path;
    private final List<String> rolesAllowed;
    private final AbstractModule module;
    private final Class<?> controllerClass;
    private final Method method;
    private final Type requestBodyType;
    private final List<String> pathParams;

    public ResourceMethod(String rootPath, Method method, Class<?> controllerClass, AbstractModule module) {
        this.method = method;
        this.controllerClass = controllerClass;
        this.module = module;
        this.rolesAllowed = rolesAllowed(method);
        this.path = path(rootPath, method);
        this.httpMethod = httpMethod(method);
        this.requestBodyType = requestBodyType(method);
        this.pathParams = pathParams(method);
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return method != null && method.isAnnotationPresent(annotationClass);
    }

    public <T extends Annotation> Optional<T> annotation(Class<T> annotationClass) {
        return method == null ? Optional.empty() : Optional.ofNullable(method.getDeclaredAnnotation(annotationClass));
    }

    public String httpMethod() {
        return httpMethod;
    }

    private String httpMethod(Method method) {
        if (method.isAnnotationPresent(GET.class)) {
            return HttpMethod.GET;
        } else if (method.isAnnotationPresent(POST.class)) {
            return HttpMethod.POST;
        } else if (method.isAnnotationPresent(PUT.class)) {
            return HttpMethod.PUT;
        } else if (method.isAnnotationPresent(DELETE.class)) {
            return HttpMethod.DELETE;
        } else {
            return null;
        }
    }

    public Method method() {
        return method;
    }

    public String routePath() {
        return path;
    }

    private String path(String rootPath, Method method) {
        Path path = method.getDeclaredAnnotation(Path.class);
        if (path == null) {
            return null;
        }
        return rootPath + path.value();
    }

    public List<String> rolesAllowed() {
        return rolesAllowed;
    }

    private List<String> rolesAllowed(Method method) {
        RolesAllowed rolesAllowed = method.getDeclaredAnnotation(RolesAllowed.class);
        if (rolesAllowed == null) {
            return ImmutableList.of();
        }
        return ImmutableList.copyOf(rolesAllowed.value());
    }

    public AbstractModule module() {
        return module;
    }

    public Class<?> controllerClass() {
        return controllerClass;
    }

    public Type requestBodyType() {
        return requestBodyType;
    }

    private Type requestBodyType(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            if (isBodyParam(annotations)) {
                return method.getGenericParameterTypes()[i];
            }
        }
        return null;
    }

    private boolean isBodyParam(Annotation... annotations) {
        for (Annotation annotation : annotations) {
            if (PARAM_ANNOTATIONS.contains(annotation.annotationType())) {
                return false;
            }
        }
        return true;
    }


    public List<String> pathParams() {
        return pathParams;
    }

    private List<String> pathParams(Method method) {
        List<String> pathParams = Lists.newArrayList();
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (PathParam.class.isAssignableFrom(annotation.annotationType())) {
                    PathParam pathParam = (PathParam) annotation;
                    pathParams.add(pathParam.value());
                }
            }
        }
        return ImmutableList.copyOf(pathParams);
    }
}
