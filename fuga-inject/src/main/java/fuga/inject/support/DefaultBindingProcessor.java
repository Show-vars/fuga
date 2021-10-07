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

import fuga.common.Key;
import fuga.common.annotation.AnnotationUtils;
import fuga.common.errors.ErrorMessages;
import fuga.inject.*;
import fuga.inject.bindings.*;

import java.lang.reflect.Constructor;
import java.util.stream.Collectors;

class DefaultBindingProcessor extends AbstractBindingProcessor {

    DefaultBindingProcessor(Container container, ErrorMessages errorMessages) {
        super(container, errorMessages);
    }

    private <T> InternalFactory<T> scope(Key<T> key, Scoping scoping, InternalFactory<T> internalFactory) {
        if (scoping == null || scoping == Scoping.UNSCOPED) {
            return internalFactory;
        }

        Scope scope;
        if (scoping.getScopeInstance() != null) {
            scope = scoping.getScopeInstance();
        } else {
            var scopeBinding = getScopeBinding(scoping.getScopeAnnotation());
            if (scopeBinding == null) {
                Errors.noScopeBinding(getErrorMessages(), scoping.getScopeAnnotation());
                return internalFactory;
            }

            scope = scopeBinding.getScope();
        }

        var providerAdapter = new ProviderToInternalFactoryAdapter<>(internalFactory);
        scheduleInitialization(providerAdapter);
        var scopedProvider = scope.scope(key, providerAdapter);
        if (scopedProvider == null) {
            Errors.noScopedProvider(getErrorMessages(), scoping);
            return internalFactory;
        }

        return new ProviderInstanceFactory<>(scopedProvider);
    }


    private <T> InternalFactory<T> provisionListener(Key<T> key, InternalFactory<T> internalFactory) {
        var keyedWatchings = getKeyedWatchings(key);
        var matchedWatchings = getMatchedWatchings(key);

        var keyedInterceptors = keyedWatchings.stream()
                .map(AbstractKeyedWatching::getInterceptor)
                .collect(Collectors.toUnmodifiableList());

        var matchedInterceptors = matchedWatchings.stream()
                .map(AbstractMatchedWatching::getInterceptor)
                .collect(Collectors.toUnmodifiableList());

        if (keyedInterceptors.isEmpty() && matchedInterceptors.isEmpty()) {
            return internalFactory;
        } else {
            return new InterceptedFactory<>(key, internalFactory, keyedInterceptors, matchedInterceptors);
        }
    }

    @Override
    public <T> boolean process(Binding<T> binding) {
        return binding.acceptVisitor(new AbstractBindingVisitor<>((AbstractBinding<T>) binding) {

            @Override
            public Boolean visit(InstanceBinding<? extends T> binding) {
                var instance = binding.getInstance();
                var internalFactory = scope(key, scoping,
                        provisionListener(key, new ProviderInstanceFactory<>(() -> instance)));
                putBinding(new InstanceBindingImpl<>(key, instance, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ConstructorBinding<? extends T> binding) {
                var injectionPoint = binding.getInjectionPoint();
                @SuppressWarnings("unchecked")
                var constructor = (Constructor<T>) injectionPoint.getMember();
                var constructorInjector = new ConstructorInjector<>(injectionPoint, new ReflectConstructionProxy<>(constructor));
                var internalFactory = scope(key, scoping,
                        provisionListener(key, new ConstructorFactory<>(constructorInjector)));
                putBinding(new ConstructorBindingImpl<>(key, injectionPoint, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ProviderKeyBinding<? extends T> binding) {
                var providerKey = binding.getProviderKey();
                if (providerKey.equals(binding.getKey())) {
                    Errors.recursiveProviderType(getErrorMessages());
                    return false;
                }
                var internalFactory = scope(key, scoping,
                        provisionListener(key, new DelegatedProviderFactory<>(providerKey)));
                putBinding(new ProviderKeyBindingImpl<>(key, providerKey, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ProviderBinding<? extends T> binding) {
                var provider = binding.getProvider();
                var internalFactory = scope(key, scoping,
                        provisionListener(key, new ProviderInstanceFactory<>(provider::get)));
                putBinding(new ProviderBindingImpl<>(key, provider, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ComposerKeyBinding<? extends T> binding) {
                var composerKey = binding.getComposerKey();
                if (composerKey.equals(binding.getKey())) {
                    Errors.recursiveComposerType(getErrorMessages());
                    return false;
                }
                var internalFactory = scope(key, scoping,
                        provisionListener(key, new DelegatedComposerFactory<>(composerKey)));
                putBinding(new ComposerKeyBindingImpl<>(key, composerKey, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ComposerBinding<? extends T> binding) {
                var composer = binding.getComposer();
                var internalFactory = scope(key, scoping,
                        provisionListener(key, new ComposerInstanceFactory<>(composer)));
                putBinding(new ComposerBindingImpl<>(key, composer, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(LinkedKeyBinding<? extends T> binding) {
                if (binding.getKey().equals(binding.getLinkedKey())) {
                    Errors.recursiveBinding(getErrorMessages(), binding);
                }
                var linkKey = binding.getLinkedKey();
                var internalFactory = scope(key, scoping,
                        provisionListener(key, new DelegatedKeyFactory<>(linkKey)));
                putBinding(new LinkedKeyBindingImpl<>(key, linkKey, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(UntargettedBinding<? extends T> binding) {
                var providedBy = AnnotationUtils.findAnnotation(key.getRawType(), ProvidedBy.class);
                if (providedBy != null) {
                    @SuppressWarnings("unchecked")
                    var providerKey = (Key<? extends Provider<? extends T>>) Key.of(providedBy.value());
                    if (providerKey.equals(binding.getKey())) {
                        Errors.recursiveProviderType(getErrorMessages());
                        return false;
                    }
                    var internalFactory = scope(key, scoping, new DelegatedProviderFactory<>(providerKey));
                    putBinding(new ProviderKeyBindingImpl<>(key, providerKey, internalFactory));
                    return true;
                }

                var composedBy = AnnotationUtils.findAnnotation(key.getRawType(), ComposedBy.class);
                if (composedBy != null) {
                    var composerKey = Key.of(composedBy.value());
                    if (composerKey.equals(binding.getKey())) {
                        Errors.recursiveComposerType(getErrorMessages());
                        return false;
                    }
                    var internalFactory = scope(key, scoping, new DelegatedComposerFactory<>(composerKey));
                    putBinding(new ComposerKeyBindingImpl<>(key, composerKey, internalFactory));
                    return true;
                }

                var injectionPoint = InjectionPoint.forConstructorOf(key.getFullType());
                @SuppressWarnings("unchecked")
                var constructor = (Constructor<T>) injectionPoint.getMember();
                var constructorInjector = new ConstructorInjector<>(injectionPoint, new ReflectConstructionProxy<>(constructor));
                var internalFactory = scope(key, scoping,
                        provisionListener(key, new ConstructorFactory<>(constructorInjector)));
                putBinding(new ConstructorBindingImpl<>(key, injectionPoint, internalFactory));
                return true;
            }
        });
    }
}
