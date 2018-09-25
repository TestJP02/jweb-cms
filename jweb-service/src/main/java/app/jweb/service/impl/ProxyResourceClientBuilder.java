package app.jweb.service.impl;

import app.jweb.AbstractModule;
import app.jweb.ApplicationException;
import app.jweb.resource.Resource;
import app.jweb.util.collection.QueryResponse;
import app.jweb.util.type.ClassCompiler;
import app.jweb.util.type.CodeBuilder;
import app.jweb.util.type.Constructors;
import app.jweb.util.type.Types;
import com.google.common.collect.Maps;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chi
 */
public class ProxyResourceClientBuilder<T> {
    private static final AtomicInteger INDEX = new AtomicInteger();
    private final Class<T> serviceClass;
    private final AbstractModule module;
    private final ResourceClient client;

    public ProxyResourceClientBuilder(Class<T> serviceClass, AbstractModule module, ResourceClient client) {
        this.serviceClass = serviceClass;
        this.module = module;
        this.client = client;
    }

    public T build() {
        ClassCompiler<T> compiler = new ClassCompiler<>(serviceClass, serviceClass.getCanonicalName() + "$Client" + INDEX.incrementAndGet());
        compiler.addField(new CodeBuilder().append("final {} client;", ResourceClient.class.getCanonicalName()).build());
        compiler.constructor(new Class[]{ResourceClient.class}, "this.client = $1;");
        ResourceMethodParser resourceMethodParser = parser();
        for (ResourceMethod resourceMethod : resourceMethodParser.parse()) {
            String method = method(resourceMethod);
            compiler.addMethod(method);
        }
        return Constructors.of(compiler.compile(), ResourceClient.class).newInstance(client);
    }

    private ResourceMethodParser parser() {
        return new ResourceMethodParser(serviceClass, module)
            .allowReturnType(void.class, Void.class, Optional.class, List.class, Map.class, Resource.class, Response.class, QueryResponse.class)
            .allowFieldType(List.class, Map.class, Integer.class, String.class, Double.class, Long.class, Boolean.class, OffsetDateTime.class, LocalDate.class, LocalTime.class, Locale.class, Resource.class, Object.class, byte[].class);
    }

    private String method(ResourceMethod resourceMethod) {
        CodeBuilder builder = new CodeBuilder();

        Map<String, Integer> pathParamIndexes = Maps.newHashMap();
        Map<String, Integer> queryParamIndexes = Maps.newHashMap();

        Type returnType = resourceMethod.method().getGenericReturnType();
        Integer bodyIndex = null;
        builder.append("public {} {}(", Types.rawClass(resourceMethod.method().getGenericReturnType()).getCanonicalName(), resourceMethod.method().getName());
        Annotation[][] annotations = resourceMethod.method().getParameterAnnotations();
        Class<?>[] parameterTypes = resourceMethod.method().getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> paramClass = parameterTypes[i];
            if (i > 0) builder.append(", ");
            builder.append("{} param{}", paramClass.getCanonicalName(), i);

            PathParam pathParam = pathParam(annotations[i]);
            if (pathParam != null) {
                pathParamIndexes.put(pathParam.value(), i);
                continue;
            }

            QueryParam queryParam = query(annotations[i]);
            if (queryParam != null) {
                queryParamIndexes.put(queryParam.value(), i);
                continue;
            }

            if (bodyIndex != null) {
                throw new ApplicationException("Web service allow one body only, class={}, method={}", serviceClass.getCanonicalName(), resourceMethod.method().getName());
            }
            bodyIndex = i;
        }

        builder.append(") {\n");
        builder.indent(1).append("Object body = {};\n", bodyIndex == null ? "null" : "param" + bodyIndex);

        builder.indent(1).append("java.util.Map pathParams = new java.util.HashMap();\n");
        pathParamIndexes.forEach((name, index) ->
            builder.indent(1).append("pathParams.put(\"{}\", param{}.toString());\n", name, index));

        builder.indent(1).append("java.util.Map queryParams = new java.util.HashMap();\n");
        queryParamIndexes.forEach((name, index) ->
            builder.indent(1).append("queryParams.put(\"{}\", param{}.toString());\n", name, index));

        String returnTypeLiteral = returnType == void.class ? Void.class.getCanonicalName() : Types.rawClass(returnType).getCanonicalName();

        builder.indent(1).append("{} response = ({}) client.execute(\"{}\", \"{}\", pathParams, queryParams, {}, body, {});\n",
            returnTypeLiteral,
            returnTypeLiteral,
            resourceMethod.httpMethod(),
            resourceMethod.routePath(),
            resourceMethod.requestBodyType() == null ? null : Types.typeVariableLiteral(resourceMethod.requestBodyType()),
            Types.typeVariableLiteral(returnType));

        if (returnType != void.class) builder.indent(1).append("return response;\n");

        builder.append("}");
        return builder.build();
    }

    private PathParam pathParam(Annotation... annotations) {
        if (annotations.length == 0) return null;
        for (Annotation annotation : annotations) {
            if (annotation instanceof PathParam) return (PathParam) annotation;
        }
        return null;
    }

    private QueryParam query(Annotation... annotations) {
        if (annotations.length == 0) return null;
        for (Annotation annotation : annotations) {
            if (annotation instanceof QueryParam) return (QueryParam) annotation;
        }
        return null;
    }
}
