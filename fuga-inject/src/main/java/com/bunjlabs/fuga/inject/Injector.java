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

package com.bunjlabs.fuga.inject;

public interface Injector {

    Injector getParent();

    Injector createChildInjector(Unit... units);

    Injector createChildInjector(Iterable<Unit> units);

    <T> Binding<T> getBinding(Class<T> type);

    <T> Binding<T> getBinding(Key<T> key);

    <T> Provider<T> getProvider(Class<T> type);

    <T> Provider<T> getProvider(Key<T> key);

    <T> T getInstance(Class<T> type);

    <T> T getInstance(Key<T> key);
}
