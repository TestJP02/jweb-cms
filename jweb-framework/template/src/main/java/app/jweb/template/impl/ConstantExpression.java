package app.jweb.template.impl;

import app.jweb.template.Expression;

import java.util.Map;

/**
 * @author chi
 */
public class ConstantExpression implements Expression {
    private final Object object;

    public ConstantExpression(Object object) {
        this.object = object;
    }

    @Override
    public Object eval(Map<String, Object> bindings) {
        return object;
    }
}
