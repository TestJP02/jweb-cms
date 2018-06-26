package io.sited.template;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public interface Children {
    void output(Map<String, Object> bindings, OutputStream out) throws IOException;
}
