package app.jweb.service.impl;

import app.jweb.AbstractModule;
import app.jweb.ApplicationException;
import app.jweb.util.type.ClassValidator;
import com.google.common.collect.Lists;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * @author chi
 */
public class ResourceMethodParser {
    private final AbstractModule module;
    private final Class<?> controllerClass;
    private final List<Type> allowedReturnTypes = Lists.newArrayList();
    private final List<Type> allowedParamTypes = Lists.newArrayList();
    private final List<Type> allowedFieldTypes = Lists.newArrayList();
    private final String rootPath;

    public ResourceMethodParser(Class<?> controllerClass, AbstractModule module) {
        this.module = module;
        this.controllerClass = controllerClass;

        Path path = controllerClass.getDeclaredAnnotation(Path.class);
        if (path == null) {
            throw new ApplicationException("missing @Path, type={}", controllerClass);
        }
        rootPath = path.value();
    }

    public ResourceMethodParser allowReturnType(Type... returnTypes) {
        allowedReturnTypes.addAll(Arrays.asList(returnTypes));
        return this;
    }

    public ResourceMethodParser allowParamType(Type... paramTypes) {
        allowedParamTypes.addAll(Arrays.asList(paramTypes));
        return this;
    }

    public ResourceMethodParser allowFieldType(Type... fieldTypes) {
        allowedFieldTypes.addAll(Arrays.asList(fieldTypes));
        return this;
    }

    public List<ResourceMethod> parse() {
        List<ResourceMethod> handlerMethods = Lists.newArrayList();
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (isHandleMethod(method)) {
                ResourceMethod handlerMethod = new ResourceMethod(rootPath, method, controllerClass, module);
                validate(handlerMethod);
                handlerMethods.add(handlerMethod);
            }
        }
        return handlerMethods;
    }

    private void validate(ResourceMethod handlerMethod) {
        validateHttpMethod(handlerMethod);
        validatePath(handlerMethod);
        validateRequestBodyType(handlerMethod);
        validateReturnType(handlerMethod);
        validateRoles(handlerMethod);
        validateContextParamTypes(handlerMethod);
    }

    private void validateContextParamTypes(ResourceMethod handlerMethod) {
        Type[] genericParameterTypes = handlerMethod.method().getGenericParameterTypes();
        for (int i = 0; i < genericParameterTypes.length; i++) {
            Type type = genericParameterTypes[i];
            if (isContextParam(handlerMethod.method().getParameterAnnotations()[i]) && !allowedParamTypes.contains(type)) {
                throw new ApplicationException("invalid handler param, module={}, controllerClass={}, method={}, expected={}, actual={}",
                    module.getClass().getCanonicalName(), controllerClass.getCanonicalName(), handlerMethod.method().getName(), allowedFieldTypes, type);
            }
        }
    }

    private boolean isContextParam(Annotation... annotations) {
        for (Annotation annotation : annotations) {
            if (Context.class.isAssignableFrom(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }

    private void validateRoles(ResourceMethod handlerMethod) {
        if (!module.declareRoles().containsAll(handlerMethod.rolesAllowed())) {
            throw new ApplicationException("invalid @RolesAllowed, module={}, controllerClass={}, method={}, expected={}, actual={}",
                module.getClass().getCanonicalName(), controllerClass.getCanonicalName(), handlerMethod.method().getName(), module.declareRoles(), handlerMethod.rolesAllowed());
        }
    }

    private void validateReturnType(ResourceMethod handlerMethod) {
        Type returnType = handlerMethod.method().getGenericReturnType();
        List<Type> allowedTypes = Lists.newArrayList(allowedReturnTypes);
        allowedTypes.addAll(allowedFieldTypes);
        validator(returnType, allowedTypes).validate();
    }

    private void validateRequestBodyType(ResourceMethod handlerMethod) {
        Type requestBodyType = handlerMethod.requestBodyType();
        if (requestBodyType != null) {
            validator(requestBodyType, allowedFieldTypes).validate();
        }
    }

    private void validatePath(ResourceMethod handlerMethod) {
        List<String> routePathVariables = new ResourcePath(handlerMethod.routePath()).variableNames;
        List<String> pathParams = handlerMethod.pathParams();
        if (!routePathVariables.containsAll(pathParams) || !pathParams.containsAll(routePathVariables)) {
            throw new ApplicationException("invalid @PathParam, module={}, controllerClass={}, method={}, expected={}, actual={}",
                module.getClass().getCanonicalName(), controllerClass.getCanonicalName(), handlerMethod.method().getName(), routePathVariables, pathParams);
        }
    }

    private void validateHttpMethod(ResourceMethod handlerMethod) {
        if (handlerMethod.httpMethod() == null) {
            throw new ApplicationException("handler method missing http method annotation (@GET, @PUT, @POST, @DELETE), module={}, controllerClass={}, method={}",
                module.getClass().getCanonicalName(), controllerClass.getCanonicalName(), handlerMethod.method().getName());
        }
    }

    private boolean isHandleMethod(Method method) {
        return method.isAnnotationPresent(Path.class);
    }

    private ClassValidator validator(Type type, List<Type> allowedTypes) {
        ClassValidator validator = new ClassValidator(type);
        validator.allowGeneric();
        validator.allowEnum();
        allowedTypes.forEach(validator::allow);
        allowedFieldTypes.forEach(validator::allow);
        return validator;
    }
}
