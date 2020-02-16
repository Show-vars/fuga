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

import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.InjectionPoint;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.bindings.ConstructorBinding;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

class ConstructorBindingImpl<T> extends AbstractBinding<T> implements ConstructorBinding<T> {

    private final InjectionPoint injectionPoint;

    ConstructorBindingImpl(Key<T> key, Scoping scoping, InjectionPoint injectionPoint) {
        super(key, scoping);
        this.injectionPoint = injectionPoint;
    }

    ConstructorBindingImpl(Key<T> key, InjectionPoint injectionPoint, InternalFactory<T> internalFactory) {
        super(key, internalFactory);
        this.injectionPoint = injectionPoint;
    }

    @Override
    public InjectionPoint getInjectionPoint() {
        return injectionPoint;
    }

    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected AbstractBinding<T> withScoping(Scoping scoping) {
        return new ConstructorBindingImpl<>(getKey(), scoping, injectionPoint);
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(ConstructorBinding.class)
                .add("key", getKey())
                .add("injectionPoint", injectionPoint)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ConstructorBinding) {
            ConstructorBinding<?> other = (ConstructorBinding<?>) o;
            return getKey().equals(other.getKey())
                    && Objects.equals(injectionPoint, other.getInjectionPoint());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), injectionPoint);
    }
}
