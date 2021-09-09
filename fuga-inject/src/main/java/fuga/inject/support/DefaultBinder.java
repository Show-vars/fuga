/*
 * Copyright 2019-2021 Bunjlabs
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

package fuga.inject.support;

import fuga.inject.Binder;
import fuga.inject.Key;
import fuga.inject.Scope;
import fuga.inject.binder.BindingBuilder;
import fuga.util.Assert;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

class DefaultBinder implements Binder {

    private final List<AbstractBinding<?>> bindings = new ArrayList<>();
    private final List<ScopeBinding> scopeBindings = new ArrayList<>();

    DefaultBinder() {
    }

    @Override
    public void bindScope(Class<? extends Annotation> annotationType, Scope scope) {
        Assert.notNull(annotationType);
        Assert.notNull(scope);
        scopeBindings.add(new ScopeBinding(annotationType, scope));
    }

    @Override
    public <T> BindingBuilder<T> bind(Class<T> type) {
        return bind(Key.of(type));
    }

    @Override
    public <T> BindingBuilder<T> bind(Key<T> type) {
        return new DefaultBindingBuilder<>(type, bindings);
    }

    public List<AbstractBinding<?>> getBindings() {
        return bindings;
    }

    public List<ScopeBinding> getScopeBindings() {
        return scopeBindings;
    }
}
