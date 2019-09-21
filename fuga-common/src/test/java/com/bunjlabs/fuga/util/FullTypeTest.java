package com.bunjlabs.fuga.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FullTypeTest {

    @Test
    public void testNull() {
        assertThrows(IllegalArgumentException.class, () -> FullType.of(null));
    }

    @Test
    public void testRaw() {
        var sample = FullType.of(Sample.class);
        assertSame(Sample.class, sample.getType());
        assertSame(Sample.class, sample.getRawType());
    }

    @Test
    public void testType() {
        var sample = FullType.of(Integer.TYPE);
        assertSame(int.class, sample.getType());
        assertSame(Integer.TYPE, sample.getRawType());
    }

    @Test
    public void testSuper() {
        var sample = FullType.of(Sample.class);
        var sup = FullType.of(Super.class);
        assertSame(Super.class, sample.getSuperType().getRawType());
        assertEquals(sup, sample.getSuperType());
    }

    @Test
    public void testInterfaces() {
        var sample = FullType.of(Sample.class);
        var ia = FullType.of(IA.class);
        var ib = FullType.of(IB.class);
        var ic = FullType.of(IC.class);
        assertArrayEquals(new FullType[]{ia, ib, ic}, sample.getInterfaces());
    }

    @Test
    public void testCast() {
        var sample = FullType.of(Sample.class);
        var sup = sample.as(Super.class);
        var ib = sample.as(IB.class);
        assertSame(Super.class, sup.getRawType());
        assertSame(IB.class, ib.getRawType());
        assertEquals(FullType.of(Super.class), sup);
        assertEquals(FullType.of(IB.class), ib);
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
