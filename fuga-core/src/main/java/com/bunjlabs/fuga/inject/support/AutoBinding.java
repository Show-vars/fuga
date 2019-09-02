package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

public class AutoBinding<T> extends AbstractBinding<T> {

    AutoBinding(Key<T> key) {
        super(key);
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(this)
                .add("key", getKey())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof InstanceBinding) {
            InstanceBinding<?> other = (InstanceBinding<?>) o;
            return getKey().equals(other.getKey());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }
}
