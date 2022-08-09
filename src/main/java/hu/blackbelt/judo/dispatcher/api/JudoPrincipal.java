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

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.security.Principal;
import java.util.Map;

@Getter
@Builder
public class JudoPrincipal implements Principal {

    @NonNull
    private String name;

    private String realm;

    private String client;

    @NonNull
    private Map<String, Object> attributes;

    @Override
    public String getName() {
        return name;
    }
}
