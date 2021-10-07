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

package fuga.inject;

import fuga.common.Key;
import fuga.inject.builder.BindingBuilder;
import fuga.inject.builder.KeyedWatchingBuilder;
import fuga.inject.builder.MatchedWatchingBuilder;
import fuga.lang.FullType;
import fuga.util.Matcher;

import java.lang.annotation.Annotation;

public interface Binder {

    void bindScope(Class<? extends Annotation> annotationType, Scope scope);

    <T> KeyedWatchingBuilder<T> watch(Class<T> type);

    <T> KeyedWatchingBuilder<T> watch(Key<T> type);

    MatchedWatchingBuilder watch(Matcher<FullType<?>> matcher);

    <T> BindingBuilder<T> bind(Class<T> type);

    <T> BindingBuilder<T> bind(Key<T> type);
}
