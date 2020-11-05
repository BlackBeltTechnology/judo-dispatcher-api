package hu.blackbelt.judo.dispatcher.api;

import java.util.Map;

/**
 * Environment variable resolver.
 */
public interface EnvironmentVariableResolver {

    <T> T resolve(Class<T> type, String category, String key, Map<String, Object> context);
}
