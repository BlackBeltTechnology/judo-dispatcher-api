package hu.blackbelt.judo.dispatcher.api;

import java.util.Map;

/**
 * Dispacher forwarding required to a given operation.
 */
public interface Dispatcher {

    String INSTANCE_KEY_OF_BOUND_OPERATION = "__this";
    String ENTITY_TYPE_MAP_KEY = "__entityType";
    String PRINCIPAL_KEY = "__principal";
    String ACTOR_KEY = "__actor";
    String VARIABLES_KEY = "__variables";

    /**
     * Call operation with a given exchange. Exchange contains request payload (with key of operation parameter name
     * defined in ASM model) and additional parameters:
     * - ID of mapped transfer object in case of bound operations (key is defined by IdentifierProvider).
     * <p>
     * Dispatcher extends payload with the following data:
     * - mapped transfer object in case of bound operations (key is __this).
     * <p>
     * Response is also an exchange containing the following data:
     * - response transfer object (key is __output),
     * - fault transfer object with key of the given fault type (NOT IMPLEMENTED YET).
     *
     * @param operationFullyQualifiedName fully qualified name of operation
     * @param exchange                    request exchange
     * @return response exchange
     */
    Map<String, Object> callOperation(String operationFullyQualifiedName, Map<String, Object> exchange);
}
