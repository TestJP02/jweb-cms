package app.jweb;

public interface Profile {
    <T> T options(String name, Class<T> optionClass);
}