package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.*;

import java.util.LinkedList;
import java.util.List;

public class InternalInjectorBuilder {

    private final List<Unit> units = new LinkedList<>();
    private InjectorImpl parent = null;

    public InternalInjectorBuilder withUnits(Iterable<Unit> units) {
        units.forEach(this.units::add);
        return this;
    }

    public InternalInjectorBuilder withParent(InjectorImpl injector) {
        this.parent = injector;
        return this;
    }

    public Injector build() {
        if (parent == null) {
            parent = createRootInjector();
        }

        var container = new InheritedContainer(parent.getContainer());
        var bindingProcessor = new DefaultBindingProcessor(container);
        var scopeBindingProcessor = new DefaultScopeBindingProcessor(container);

        for (var unit : units) {
            setupUnit(unit, bindingProcessor, scopeBindingProcessor);
        }

        var injector = new InjectorImpl(parent, container);
        bindingProcessor.getUninitialized().forEach(i -> i.initialize(injector));

        return injector;
    }

    private void setupUnit(Unit unit, BindingProcessor bindingProcessor, ScopeBindingProcessor scopeBindingProcessor) {
        var configuration = new DefaultConfiguration();

        try {
            unit.setup(configuration);
        } catch (RuntimeException e) {
            throw new ConfigurationException("Unable to setup unit " + unit, e);
        }

        for (var innerUnit : configuration.getInstalledUnits()) {
            setupUnit(innerUnit, bindingProcessor, scopeBindingProcessor);
        }

        for (AbstractBinding<?> binding : configuration.getBindings()) {
            bindingProcessor.process(binding);
        }

        for (ScopeBinding scopeBinding : configuration.getScopeBindings()) {
            scopeBindingProcessor.process(scopeBinding);
        }
    }

    private InjectorImpl createRootInjector() {
        var container = new InheritedContainer(Container.EMPTY);
        var bindingProcessor = new DefaultBindingProcessor(container);
        var scopeBindingProcessor = new DefaultScopeBindingProcessor(container);

        setupUnit(new RootUnit(), bindingProcessor, scopeBindingProcessor);

        return new InjectorImpl(null, container);
    }

    private static class RootUnit implements Unit {

        @Override
        public void setup(Configuration c) {
            c.bindScope(Singleton.class, new SingletonScope());
        }
    }
}
