package io.sited.admin;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author chi
 */
public class ConsoleMenu {
    public String path;
    public String displayName;
    public String messageKey;
    public Integer displayOrder = 1;
    public List<String> rolesAllowed = Lists.newArrayList();
    public List<ConsoleMenuItem> children;

    public static class ConsoleMenuItem {
        public String path;
        public String displayName;
        public String messageKey;
        public String bundleName;
        public Integer displayOrder = 1;
        public List<String> rolesAllowed = Lists.newArrayList();
    }
}
