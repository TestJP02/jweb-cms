package io.sited;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class ModuleRegistry implements Iterable<AbstractModule> {
    final Map<String, AbstractModule> modules = Maps.newHashMap();
    final Map<String, String> overrides = Maps.newHashMap();
    MutableGraph<String> graph;

    public void createGraph() {
        graph = GraphBuilder.directed().allowsSelfLoops(false).build();
        for (String moduleName : activeModules()) {
            List<String> dependencies = dependencies(moduleName, module(moduleName).dependencies());
            if (dependencies.isEmpty()) {
                graph.addNode(moduleName);
            } else {
                dependencies.forEach(dependency -> graph.putEdge(dependency, moduleName));
            }
        }
        validate(graph);
    }

    private void validate(MutableGraph<String> graph) {
        Map<String, Set<String>> dependencies = Maps.newHashMap();
        for (String moduleName : graph.nodes()) {
            if (!dependencies.containsKey(moduleName)) {
                dependencies.put(moduleName, Sets.newHashSet());
            }
            for (String dependency : dependencies(moduleName, module(moduleName).dependencies())) {
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

    public boolean isInstalled(String name) {
        return modules.containsKey(name);
    }

    public void install(AbstractModule module) {
        if (module == null) {
            throw new ApplicationException("module can't be null");
        }
        String name = module.name();
        if (isInstalled(name)) {
            throw new ApplicationException("module installed, name={}, type={}, installed={}", name, module.getClass(), module(name).getClass());
        } else {
            Collection<AbstractModule> values = modules.values();
            for (AbstractModule installedModule : values) {
                if (module.getClass().isAssignableFrom(installedModule.getClass())) {
                    overrides.put(module.name(), installedModule.name());
                }

                if (installedModule.getClass().isAssignableFrom(module.getClass())) {
                    overrides.put(installedModule.name(), module.name());
                }
            }
            modules.put(name, module);
        }
    }

    @Override
    public Iterator<AbstractModule> iterator() {
        Set<String> visited = Sets.newLinkedHashSet();
        Deque<String> stack = Lists.newLinkedList(activeModules());

        while (!stack.isEmpty()) {
            String moduleName = stack.pollFirst();
            Set<String> dependencies = graph.predecessors(moduleName);
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

    private List<String> activeModules() {
        Set<String> overrides = Sets.newHashSet(this.overrides.values());
        return modules.keySet().stream().filter(name -> !overrides.contains(name)).collect(Collectors.toList());
    }

    private String activeModuleName(String moduleName) {
        for (Map.Entry<String, String> entry : overrides.entrySet()) {
            if (entry.getValue().equals(moduleName)) {
                return entry.getKey();
            }
        }
        return moduleName;
    }

    private List<String> dependencies(String moduleName, List<String> dependencies) {
        return dependencies.stream().filter(name -> !moduleName.equals(name) && modules.containsKey(name)).collect(Collectors.toList());
    }

    protected List<String> dependencies(String moduleName) {
        String activeModuleName = activeModuleName(moduleName);
        Set<String> all = Sets.newHashSet(graph.predecessors(activeModuleName));
        for (String dependency : graph.predecessors(activeModuleName)) {
            all.addAll(dependencies(dependency));
        }
        return ImmutableList.copyOf(all);
    }

    private boolean isOverrided(String moduleName) {
        return overrides.containsKey(moduleName);
    }

    @SuppressWarnings("unchecked")
    public AbstractModule module(String moduleName) {
        if (isOverrided(moduleName)) {
            String overrideModuleName = overrides.get(moduleName);
            return module(overrideModuleName);
        }

        AbstractModule module = modules.get(moduleName);
        if (module == null) {
            throw new ApplicationException("missing module, name={}", moduleName);
        }
        return module;
    }
}
