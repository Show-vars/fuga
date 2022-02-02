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

package fuga.util;

import fuga.lang.TypeLiteral;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;

public class TypeLiteralTest {

    @Test
    public void testNull() {
        assertThrows(IllegalArgumentException.class, () -> TypeLiteral.of((Type) null));
        assertThrows(IllegalArgumentException.class, () -> TypeLiteral.of((Class<?>) null));
    }

    @Test
    public void testRaw() {
        var sample = TypeLiteral.of(Sample.class);
        assertSame(Sample.class, sample.getType());
        assertSame(Sample.class, sample.getRawType());
    }

    @Test
    public void testType() {
        var sample = TypeLiteral.of(Integer.TYPE);
        assertSame(int.class, sample.getType());
        assertSame(Integer.TYPE, sample.getRawType());
    }

    @Test
    public void testSuper() {
        var sample = TypeLiteral.of(Sample.class);
        var sup = TypeLiteral.of(Super.class);
        assertSame(Super.class, sample.getSuperType().getRawType());
        assertEquals(sup, sample.getSuperType());
    }

    @Test
    public void testInterfaces() {
        var sample = TypeLiteral.of(Sample.class);
        var ia = TypeLiteral.of(IA.class);
        var ib = TypeLiteral.of(IB.class);
        var ic = TypeLiteral.of(IC.class);
        assertArrayEquals(new TypeLiteral[]{ia, ib, ic}, sample.getInterfaces());
    }

    @Test
    public void testCast() {
        var sample = TypeLiteral.of(Sample.class);
        var sup = sample.as(Super.class);
        var ib = sample.as(IB.class);
        assertSame(Super.class, sup.getRawType());
        assertSame(IB.class, ib.getRawType());
        assertEquals(TypeLiteral.of(Super.class), sup);
        assertEquals(TypeLiteral.of(IB.class), ib);
    }

    // TODO: resolve type and wildcard variables
    /*
    @Test
    public void testGenerics() {
        var sample = FullType.of(SampleGeneric.class);
        var i = FullType.of(Integer.class);
        var o = FullType.of(Object.class);
        assertArrayEquals(new FullType[]{i, o}, sample.getGenerics());
        assertEquals(i, sample.getGeneric(0));
        assertEquals(o, sample.getGeneric(1));
    }
    */

    interface IA {

    }

    interface IB {

    }

    interface IC {

    }

    static class Super {

    }

    static class Sample extends Super implements IA, IB, IC {

    }

    static class SampleGeneric<Integer, Object> {

    }
}
