package hu.blackbelt.judo.dispatcher.api;

/**
 * Environment variable resolver.
 */
public interface VariableResolver {

    <T> T resolve(Class<T> type, String category, String key);
}
