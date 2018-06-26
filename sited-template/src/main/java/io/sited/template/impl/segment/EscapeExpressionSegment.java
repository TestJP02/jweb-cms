package io.sited.template.impl.segment;

import com.google.common.html.HtmlEscapers;
import io.sited.template.Expression;
import io.sited.template.impl.Segment;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author chi
 */
public class EscapeExpressionSegment implements Segment {
    private final Expression expression;

    public EscapeExpressionSegment(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void output(Map<String, Object> bindings, OutputStream outputStream) throws IOException {
        Object value = expression.eval(bindings);
        String content = value == null ? "" : HtmlEscapers.htmlEscaper().escape(value.toString());
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
    }
}
