package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Injector;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.Provider;
import com.bunjlabs.fuga.inject.ProvisionException;

public class DefaultInjector implements Injector {
    private final Container container;

    public DefaultInjector(Container container) {
        this.container = container;
    }

    <T> InternalFactory<T> getInternalFactory(Key<T> key) {
        AbstractBinding<T> binding = getBinding(key);
        return binding.getInternalFactory();
    }

    @Override
    public <T> AbstractBinding<T> getBinding(Class<T> type) {
        return getBinding(Key.of(type));
    }

    @Override
    public <T> AbstractBinding<T> getBinding(Key<T> key) {
        AbstractBinding<T> binding = container.getExplicitBinding(key);

        if (binding != null) {
            return binding;
        }

        throw new ProvisionException("No binding found for " + key);
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> type) {
        return getProvider(Key.of(type));
    }

    @Override
    public <T> Provider<T> getProvider(Key<T> key) {
        AbstractBinding<T> binding = getBinding(key);
        InternalFactory<T> internalFactory = binding.getInternalFactory();

        return () -> {
            var context = new InjectorContext(this);
            context.enterRequester(Key.of(Injector.class));
            try {
                return internalFactory.get(context, Dependency.of(binding.getKey()));
            } catch (InternalProvisionException e) {
                throw e.toProvisionException();
            } finally {
                context.exitRequester();
            }
        };
    }

    @Override
    public <T> T getInstance(Class<T> type) {
        return getInstance(Key.of(type));
    }

    @Override
    public <T> T getInstance(Key<T> key) {
        return getProvider(key).get();
    }

}
