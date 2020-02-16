/*
 * Copyright 2019-2020 Bunjlabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.common.errors.ErrorMessages;

abstract class AbstractScopeBindingProcessor implements ScopeBindingProcessor {

    private final Container container;
    private final ErrorMessages errorMessages;

    AbstractScopeBindingProcessor(Container container, ErrorMessages errorMessages) {
        this.container = container;
        this.errorMessages = errorMessages;
    }

    void putScopeBinding(ScopeBinding scopeBinding) {
        container.putScopeBinding(scopeBinding);
    }

    ErrorMessages getErrorMessages() {
        return errorMessages;
    }
}
