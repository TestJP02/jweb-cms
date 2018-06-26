package io.sited.template.impl;

import io.sited.util.collection.SortedList;
import io.sited.template.ElementProcessor;

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
