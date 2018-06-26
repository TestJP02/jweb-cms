package io.sited.admin;


import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class ConsoleBundle {
    public String name;
    public String path;
    public String scriptFile;
    public List<String> scriptFiles = Lists.newArrayList();
    public List<String> messages = Lists.newArrayList();
    public ConsoleMenu menu;
    public Map<String, Object> options;
}
