package app.jweb.service;

import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author chi
 */
public interface ServiceConfig {
    <T> ServiceConfig service(Class<T> clientClass, String remoteServerURL);

    <T> ServiceConfig service(Class<T> serviceClass, Class<? extends T> serviceImplClass);

    ServiceConfig bindExceptionMapper(Class<ExceptionMapper<? extends Throwable>> exceptionMapperClass);
}
