package app.jweb.template.impl;

import app.jweb.template.Children;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class EmptyChildren implements Children {
    @Override
    public void output(Map<String, Object> bindings, OutputStream out) throws IOException {
    }
}
