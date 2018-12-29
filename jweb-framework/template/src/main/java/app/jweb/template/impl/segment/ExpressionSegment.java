package app.jweb.template.impl.segment;

import app.jweb.template.Expression;
import app.jweb.template.impl.Segment;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author chi
 */
public class ExpressionSegment implements Segment {
    private final Expression expression;

    public ExpressionSegment(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void output(Map<String, Object> bindings, OutputStream writer) throws IOException {
        Object value = expression.eval(bindings);
        String content = value == null ? "" : value.toString();
        writer.write(content.getBytes(StandardCharsets.UTF_8));
    }
}
