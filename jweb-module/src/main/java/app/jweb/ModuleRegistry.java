package app.jweb;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class ModuleRegistry implements Iterable<AbstractModule> {
    private final Map<String, ModuleEntry> modules = Maps.newHashMap();

    public void validate() {
        Map<String, Set<String>> dependencies = Maps.newHashMap();
        for (String moduleName : modules.keySet()) {
            if (!dependencies.containsKey(moduleName)) {
                dependencies.put(moduleName, Sets.newHashSet());
            }
            for (String dependency : dependencies(moduleName)) {
                dependencies.get(moduleName).add(dependency);

                if (dependencies.containsKey(dependency)) {
                    dependencies.get(moduleName).addAll(dependencies.get(dependency));
                }
            }
            if (dependencies.get(moduleName).contains(moduleName)) {
                throw new ApplicationException("cycle module dependency, name={}", moduleName);
            }
        }
    }

    boolean isInstalled(String name) {
        return modules.containsKey(name);
    }

    public void install(AbstractModule module) {
        if (module == null) {
            throw new ApplicationException("module can't be null");
        }
        if (isInstalled(module.name())) {
            throw new ApplicationException("module installed, name={}, type={}, installed={}", module.name(), module.getClass(), module(module.name()).getClass());
        }

        boolean installed = false;
        for (ModuleEntry entry : ImmutableList.copyOf(modules.values())) {
            if (module.getClass().isAssignableFrom(entry.module.getClass())) {
                ModuleEntry moduleEntry = new ModuleEntry();
                moduleEntry.module = module;
                moduleEntry.implementation = entry.module;
                modules.remove(entry.module.name());
                modules.put(module.name(), moduleEntry);
                installed = true;
            } else if (entry.module.getClass().isAssignableFrom(module.getClass())) {
                entry.implementation = module;
                installed = true;
            }
        }

        if (!installed) {
            ModuleEntry moduleEntry = new ModuleEntry();
            moduleEntry.module = module;
            modules.put(module.name(), moduleEntry);
        }
    }

    @Override
    public Iterator<AbstractModule> iterator() {
        Set<String> visited = Sets.newLinkedHashSet();
        Deque<String> stack = Lists.newLinkedList(modules.keySet());
        while (!stack.isEmpty()) {
            String moduleName = stack.pollFirst();

            List<String> dependencies = dependencies(moduleName);
            boolean satisfied = true;
            for (String dependency : dependencies) {
                if (!visited.contains(dependency)) {
                    satisfied = false;
                    break;
                }
            }
            if (satisfied) {
                visited.add(moduleName);
            } else {
                stack.addLast(moduleName);
            }
        }
        return visited.stream().map(this::module).collect(Collectors.toList()).iterator();
    }

    private List<String> dependencies(String moduleName) {
        AbstractModule module = module(moduleName);
        return module.dependencies().stream().filter(name -> isInstalled(name) && !Objects.equals(name, moduleName)).collect(Collectors.toList());
    }

    public List<String> recursiveDependencies(String moduleName) {
        Deque<String> deque = Lists.newLinkedList();
        deque.add(moduleName);
        Set<String> visited = Sets.newHashSet();
        while (!deque.isEmpty()) {
            String current = deque.pollFirst();
            if (!Objects.equals(current, moduleName)) {
                visited.add(current);
            }
            List<String> dependencies = dependencies(current);
            for (String dependency : dependencies) {
                if (!visited.contains(dependency)) {
                    deque.add(dependency);
                }
            }
        }
        return Lists.newArrayList(visited);
    }

    @SuppressWarnings("unchecked")
    public AbstractModule module(String moduleName) {
        ModuleEntry moduleEntry = modules.get(moduleName);
        if (moduleEntry == null) {
            throw new ApplicationException("missing module, name={}", moduleName);
        }
        return moduleEntry.module();
    }

    static class ModuleEntry {
        public AbstractModule module;
        public AbstractModule implementation;

        public String name() {
            return module.name();
        }

        public AbstractModule module() {
            if (implementation == null) {
                return module;
            }
            return implementation;
        }

        public List<String> dependencies() {
            if (implementation == null) {
                return module.dependencies();
            }
            return implementation.dependencies();
        }
    }
}
