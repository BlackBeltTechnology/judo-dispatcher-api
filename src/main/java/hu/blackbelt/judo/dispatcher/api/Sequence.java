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

public interface Sequence<T> {

    /**
     * Default start value.
     */
    Long DEFAULT_START = 1L;

    /**
     * Default increment.
     */
    Long DEFAULT_INCREMENT = 1L;

    /**
     * Type (discriminator) key.
     */
    String TYPE_KEY = "type";

    /**
     * Get next sequence value.
     *
     * @param sequenceName sequence name
     * @return next value
     */
    T getNextValue(String sequenceName);

    /**
     * Get current sequence value.
     *
     * @param sequenceName sequence name
     * @return current value
     */
    default T getCurrentValue(String sequenceName) {
        throw new UnsupportedOperationException("Getting current value of sequence '" + sequenceName + "' is not supported");
    }
}
