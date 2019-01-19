package app.jweb.lancher.setup.service;

import app.jweb.ApplicationException;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public class Console {
    private final Map<String, ConsoleBundle> consoleModules = Maps.newHashMap();
    private final Map<String, ConsoleBundle> scriptFiles = Maps.newHashMap();

    public Console install(ConsoleBundle consoleBundle) {
        consoleModules.put(consoleBundle.name, consoleBundle);
        if (!Strings.isNullOrEmpty(consoleBundle.scriptFile)) {
            scriptFiles.put(consoleBundle.scriptFile, consoleBundle);
        }
        return this;
    }

    public ConsoleBundle bundle(String bundleName) {
        ConsoleBundle consoleBundle = consoleModules.get(bundleName);
        if (consoleBundle == null) {
            throw new ApplicationException("missing console bundle, name={}", bundleName);
        }
        return consoleBundle;
    }

    public Optional<ConsoleBundle> findByScriptFile(String scriptFile) {
        return Optional.ofNullable(scriptFiles.get(scriptFile));
    }

    public List<ConsoleBundle> bundles() {
        return ImmutableList.copyOf(consoleModules.values());
    }

}
