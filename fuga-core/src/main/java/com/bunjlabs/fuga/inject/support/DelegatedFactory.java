package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Key;

public class DelegatedFactory<T> implements InternalFactory<T> {

    private final Key<? extends T> targetKey;

    DelegatedFactory(Key<? extends T> targetKey) {
        this.targetKey = targetKey;
    }

    @Override
    public T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException {
        InternalFactory<? extends T> delegatedFactory = context.getInjector().getInternalFactory(targetKey);

        return delegatedFactory.get(context, dependency);
    }
}
