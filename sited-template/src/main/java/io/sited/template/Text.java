package io.sited.template;

/**
 * @author chi
 */
public class Text implements Node {
    private final String text;
    private final Integer row;
    private final Integer column;
    private final String source;

    private Element parent;

    public Text(String text, Integer row, Integer column, String source) {
        this.text = text;
        this.row = row;
        this.column = column;
        this.source = source;
    }


    public String innerText() {
        return text;
    }


    public Element parent() {
        return parent;
    }


    public void setParent(Element parent) {
        this.parent = parent;
    }


    public String name() {
        return "TEXT";
    }


    public Integer row() {
        return row;
    }


    public Integer column() {
        return column;
    }


    public String source() {
        return source;
    }
}
