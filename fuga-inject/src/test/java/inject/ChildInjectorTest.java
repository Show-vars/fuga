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

package inject;

import fuga.inject.Inject;
import fuga.inject.Injector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChildInjectorTest {

    @Test
    public void testBindingsInherited() {
        var a = new A();
        var b = new B();
        var child = Injector.create(c -> c.bind(A.class).toInstance(a))
                .createChildInjector(c -> c.bind(B.class).toInstance(b));
        assertSame(a, child.getInstance(A.class));
        assertSame(b, child.getInstance(B.class));
    }

    @Test
    public void testParentBindingCollision() {
        var a1 = new A();
        var a2 = new A();
        var child = Injector.create(c -> c.bind(A.class).toInstance(a1))
                .createChildInjector(c -> c.bind(A.class).toInstance(a2));
        assertSame(a2, child.getInstance(A.class));
    }

    @Test
    public void testGetParent() {
        var first = Injector.create();
        var second = first.createChildInjector();
        var third = second.createChildInjector();
        var root = first.getParent();

        assertNotNull(first.getParent());
        assertNotSame(first, first.getParent());
        assertSame(first, second.getParent());
        assertSame(second, third.getParent());
        assertNull(root.getParent());
    }


    public static class A {

    }

    public static class B {

    }

    public static class C {

        private B b;

        @Inject
        public C(B b) {
            this.b = b;
        }
    }
}
