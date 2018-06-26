package io.sited.template;

import java.util.List;

/**
 * @author chi
 */
public interface Document {
    String title();

    List<Element> children();
}
