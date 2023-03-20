package hu.blackbelt.judo.dispatcher.api;

/*-
 * #%L
 * Judo Dispatcher API
 * %%
 * Copyright (C) 2018 - 2022 BlackBelt Technology
 * %%
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 * #L%
 */

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
    String HEADERS_KEY = "__headers";

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

    /**
     * Performs a coercion from an input type to a desired output type.
     *
     * @param sourceValue source value
     * @param targetClass target class
     * @param <S> source type
     * @param <T> target type
     * @return target value
     */
    <S, T> T coerce(S sourceValue, Class<T> targetClass);

    /**
     * Performs a coercion from an input type to a desired output type.
     *
     * @param sourceValue source value
     * @param targetClassName target class name
     * @param <S> source type
     * @param <T> target type
     * @return target value
     */
    <S, T> T coerce(S sourceValue, String targetClassName);
}
