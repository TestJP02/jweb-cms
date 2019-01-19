package app.jweb.lancher.setup.service;


import app.jweb.util.i18n.MessageBundle;

import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public class ScriptModuleMessageBundle implements MessageBundle {
    private final ConsoleBundle consoleBundle;
    private final MessageBundle messageManager;

    public ScriptModuleMessageBundle(ConsoleBundle consoleBundle, MessageBundle messageManager) {
        this.consoleBundle = consoleBundle;
        this.messageManager = messageManager;
    }

    @Override
    public Optional<String> get(String s) {
        return Optional.empty();
    }

    @Override
    public Optional<String> get(String key, String locale) {
        return messageManager.get(key, locale);
    }

    @Override
    public List<String> keys() {
        return messageManager.keys();
    }
}
