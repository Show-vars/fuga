/*
 * Copyright 2019 Bunjlabs
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
import com.bunjlabs.fuga.inject.Binding;

import java.lang.annotation.Annotation;

abstract class Errors {

    static void missingScopeAnnotation(ErrorMessages errorMessages, Class<? extends Annotation> annotation) {
        errorMessages.addMessage("%s should be annotated with @ScopeAnnotation.", annotation);
    }

    static void missingRuntimeRetention(ErrorMessages errorMessages, Class<? extends Annotation> annotation) {
        errorMessages.addMessage("%s should be annotated with @Retention(RUNTIME).", annotation);
    }

    static void recursiveBinding(ErrorMessages errorMessages, Binding<?> binding) {
        errorMessages.addMessage("Binding %s points to itself.", binding);
    }

    static void noScopeBinding(ErrorMessages errorMessages, Scoping scoping) {
        errorMessages.addMessage("No %s scope binding found.", scoping);
    }

    static void recursiveProviderType(ErrorMessages errorMessages, Scoping scoping) {
        errorMessages.addMessage("@ProvidedBy points to the same class it annotates.", scoping);
    }

    static void recursiveProviderType(ErrorMessages errorMessages) {
        errorMessages.addMessage("@ProvidedBy points to the same class it annotates.");
    }

    static void recursiveComposerType(ErrorMessages errorMessages) {
        errorMessages.addMessage("@ComposedBy points to the same class it annotates.");
    }
}
