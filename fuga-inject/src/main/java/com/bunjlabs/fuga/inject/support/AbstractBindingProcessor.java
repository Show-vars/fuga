package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.common.errors.ErrorMessages;
import com.bunjlabs.fuga.inject.Binding;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

abstract class AbstractBindingProcessor implements BindingProcessor {

    private final Container container;
    private final ErrorMessages errorMessages;
    private final List<Initializable> uninitialized = new LinkedList<>();

    AbstractBindingProcessor(Container container, ErrorMessages errorMessages) {
        this.container = container;
        this.errorMessages = errorMessages;
    }

    ScopeBinding getScopeBinding(Class<? extends Annotation> annotationType) {
        return container.getScopeBinding(annotationType);
    }

    <T> void putBinding(Binding<T> binding) {
        container.putBinding(binding);
    }

    void scheduleInitilization(Initializable initializable) {
        uninitialized.add(initializable);
    }

    List<Initializable> getUninitialized() {
        return uninitialized;
    }

    ErrorMessages getErrorMessages() {
        return errorMessages;
    }
}
