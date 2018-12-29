package app.jweb;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class ModuleRepository2 {
    private final Map<String, ModuleNode> roots = Maps.newHashMap();

    public void install(AbstractModule module) {
        if (module == null) {
            throw new ApplicationException("module can't be null");
        }
        List<String> names = Splitter.on('.').splitToList(module.name());
        Map<String, ModuleNode> nodes = roots;
        ModuleNode parent = null;
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            ModuleNode current = nodes.get(name);
            if (current == null) {
                current = new ModuleNode();
                current.name = name;
                current.parent = parent;
                nodes.put(name, current);
            }

            if (i == names.size() - 1) {
                if (current.module != null) {
                    throw new ApplicationException("duplicate module, name={}, module={}, exist={}", module.name(), module.getClass().getCanonicalName(), current.module.getClass().getCanonicalName())
                }
                current.module = module;
            } else {
                nodes = current.children;
                parent = current;
            }
        }
    }

    private boolean isInstalled(String name){

    }


    public enum ModuleStatus {
        LOADED, CONFIGURED, STARTED, OVERRIDE
    }

    public static class ModuleNode {
        public String name;
        public ModuleNode parent;
        public Map<String, ModuleNode> children = Maps.newHashMap();
        public AbstractModule module;
        public ModuleStatus status;
    }
}
