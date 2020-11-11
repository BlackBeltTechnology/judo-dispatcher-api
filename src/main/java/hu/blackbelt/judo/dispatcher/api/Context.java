package hu.blackbelt.judo.dispatcher.api;

public interface Context {

    Object get(String key);

    <T> T getAs(Class<T> clazz, String key);

    void put(String key, Object value);

    <T> T putIfAbsent(String key, T value);

    Object remove(String key);

    void removeAll();
}
