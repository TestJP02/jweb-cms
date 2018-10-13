package app.jweb;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Collection;
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
        for (String moduleName : exposedModules()) {
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
        ModuleEntry current = new ModuleEntry();
        current.module = module;
        current.overrided = false;
        current.exposed = true;

        if (isInstalled(module.name())) {
            throw new ApplicationException("module installed, name={}, type={}, installed={}", module.name(), module.getClass(), module(module.name()).getClass());
        }

        Collection<ModuleEntry> entries = modules.values();
        for (ModuleEntry entry : entries) {
            if (module.getClass().isAssignableFrom(entry.module.getClass())) {
                entry.exposed = false;

                current.overrided = true;
                current.implementation = entry.module.name();

                current.implementation = entry.module.name();
            } else if (entry.module.getClass().isAssignableFrom(module.getClass())) {
                current.exposed = false;

                entry.overrided = true;
                entry.implementation = module.name();
            }
        }
        modules.put(module.name(), current);
    }

    @Override
    public Iterator<AbstractModule> iterator() {
        Set<String> visited = Sets.newLinkedHashSet();
        Deque<String> stack = Lists.newLinkedList(exposedModules());

        while (!stack.isEmpty()) {
            String moduleName = stack.pollFirst();

            if (isOverrided(moduleName)) {
                AbstractModule implementation = module(moduleName);
                if (visited.contains(implementation.name())) {
                    visited.add(moduleName);
                } else {
                    stack.addLast(implementation.name());
                    stack.addLast(moduleName);
                }
            } else {
                List<String> dependencies = dependencies(moduleName);
                boolean satisfied = true;

                for (String dependency : dependencies) {
                    if (isOverrided(dependency)) {
                        AbstractModule implementation = module(moduleName);
                        if (visited.contains(implementation.name())) {
                            visited.add(moduleName);
                        } else {
                            stack.addLast(implementation.name());
                            stack.addLast(moduleName);
                        }
                    } else {
                        if (!visited.contains(module(dependency).name())) {
                            satisfied = false;
                            break;
                        }
                    }
                }
                if (satisfied) {
                    visited.add(moduleName);
                } else {
                    stack.addLast(moduleName);
                }
            }

        }
        return visited.stream().filter(this::isExposed).map(this::module).collect(Collectors.toList()).iterator();
    }

    private boolean isOverrided(String moduleName) {
        return modules.get(moduleName).overrided;
    }

    private boolean isExposed(String moduleName) {
        return modules.get(moduleName).exposed;
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
        if (moduleEntry.overrided) {
            return module(moduleEntry.implementation);
        }
        return moduleEntry.module;
    }

    private List<String> exposedModules() {
        return modules.values().stream().filter(module -> module.exposed).map(module -> module.module.name()).collect(Collectors.toList());
    }

    private static class ModuleEntry {
        public AbstractModule module;
        public Boolean overrided;
        public Boolean exposed;
        public String implementation;
    }
}
