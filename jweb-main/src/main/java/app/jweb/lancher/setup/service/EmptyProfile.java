package app.jweb.lancher.setup.service;


import app.jweb.ApplicationException;
import app.jweb.Profile;
import app.jweb.util.JSON;
import app.jweb.util.YAML;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author chi
 */
public class EmptyProfile implements Profile {
    @Override
    public <T> T options(String s, Class<T> aClass) {
        return JSON.convert(defaultOptions(aClass), aClass);
    }

    @SuppressWarnings("unchecked")
    private <T> Map<String, Object> defaultOptions(Class<T> type) {
        try {
            return (Map) YAML.OBJECT_MAPPER.convertValue(type.getDeclaredConstructor().newInstance(), Map.class);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException var3) {
            throw new ApplicationException(var3);
        }
    }
}
