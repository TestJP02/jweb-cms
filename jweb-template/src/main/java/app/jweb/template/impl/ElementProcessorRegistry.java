package app.jweb.template.impl;

import app.jweb.template.ElementProcessor;
import app.jweb.util.collection.SortedList;

import java.util.Comparator;
import java.util.List;

/**
 * @author chi
 */
public class ElementProcessorRegistry {
    private final SortedList<ElementProcessor> processors = new SortedList<>(Comparator.comparingInt(ElementProcessor::priority));

    public void add(ElementProcessor elementProcessor) {
        processors.add(elementProcessor);
    }

    public List<ElementProcessor> processors() {
        return processors;
    }
}
