package io.sited;

import com.google.common.io.ByteStreams;
import io.sited.util.JSON;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author chi
 */
@Priority(Priorities.ENTITY_CODER)
class DefaultMessageBodyReader implements MessageBodyReader<Object> {
    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return mediaType == null
            || mediaType.isCompatible(MediaType.APPLICATION_OCTET_STREAM_TYPE)
            || mediaType.isCompatible(MediaType.TEXT_PLAIN_TYPE)
            || mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        byte[] json = ByteStreams.toByteArray(entityStream);
        return JSON.fromJSON(json, genericType);
    }
}
