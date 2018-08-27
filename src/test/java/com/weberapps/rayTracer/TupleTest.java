package com.weberapps.rayTracer;

import org.junit.jupiter.api.Test;

import static java.lang.Math.sqrt;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TupleTest {
    @Test
    public void testTupleInit() {
        Tuple tuple = new Tuple(3.0f, 4.0f, 5.0f, 1.0f);
        assertEquals(3.0f, tuple.getX());
        assertEquals(4.0f, tuple.getY());
        assertEquals(5.0f, tuple.getZ());
        assertEquals(1.0f, tuple.getW());
    }

    @Test
    public void testPointInit() {
        Tuple point = new Point(3.0f, 4.0f, 5.0f);
        assertEquals(3.0f, point.getX());
        assertEquals(4.0f, point.getY());
        assertEquals(5.0f, point.getZ());
        assertEquals(1.0f, point.getW());
    }

    @Test
    public void testVectorInit() {
        Tuple vector = new Vector(3.0f, 4.0f, 5.0f);
        assertEquals(3.0f, vector.getX());
        assertEquals(4.0f, vector.getY());
        assertEquals(5.0f, vector.getZ());
        assertEquals(0.0f, vector.getW());
    }

    @Test
    public void testTupleAddition() {
        Tuple a1 = new Tuple(3.0f, -2.0f, 5.0f, 1.0f);
        Tuple a2 = new Tuple(-2.0f, 3.0f, 1.0f, 0.0f);
        assertEquals(new Tuple(1.0f, 1.0f, 6.0f, 1.0f), a1.plus(a2));
        assertEquals(new Tuple(1.0f, 1.0f, 6.0f, 1.0f), a2.plus(a1));
    }

    @Test
    public void testPointToPointSubtraction() {
        Tuple p1 = new Point(3.0f, 2.0f, 1.0f);
        Tuple p2 = new Point(5.0f, 6.0f, 7.0f);
        Tuple result = p1.minus(p2);
        assertEquals(new Vector(-2.0f, -4.0f, -6.0f), result);
    }

    @Test
    public void testPointToVectorSubtraction() {
        Tuple p1 = new Point(3.0f, 2.0f, 1.0f);
        Tuple v1 = new Vector(5.0f, 6.0f, 7.0f);
        Tuple result = p1.minus(v1);
        assertEquals(new Point(-2.0f, -4.0f, -6.0f), result);
    }

    @Test
    public void testVectorToVectorSubtraction() {
        Tuple v1 = new Vector(3.0f, 2.0f, 1.0f);
        Tuple v2 = new Vector(5.0f, 6.0f, 7.0f);
        Tuple result = v1.minus(v2);
        assertEquals(new Vector(-2.0f, -4.0f, -6.0f), result);
    }

    @Test
    public void testTupleNegation() {
        Tuple a1 = new Tuple(2.0f, -4.0f, 6.0f, -8.0f);
        assertEquals(new Tuple(-2.0f, 4.0f, -6.0f, 8.0f), a1.unaryMinus());
    }

    @Test
    public void testTupleScalarMultiplication() {
        Tuple a1 = new Tuple(2.0f, -4.0f, 6.0f, -8.0f);
        assertEquals(new Tuple(4.0f, -8.0f, 12.0f, -16.0f), a1.times(2));
        assertEquals(new Tuple(1.0f, -2.0f, 3.0f, -4.0f), a1.times(0.5f));
    }

    @Test
    public void testTupleScalarDivision() {
        Tuple a1 = new Tuple(2.0f, -4.0f, 6.0f, -8.0f);
        assertEquals(new Tuple(1.0f, -2.0f, 3.0f, -4.0f), a1.div(2));
    }

    @Test
    public void testTupleMagnitude() {
        assertEquals(1, new Vector(1.0f, 0.0f, 0.0f).magnitude());
        assertEquals(1, new Vector(0.0f, 1.0f, 0.0f).magnitude());
        assertEquals(1, new Vector(0.0f, 0.0f, 1.0f).magnitude());
        assertEquals(sqrt(77), new Vector(4.0f, 5.0f, 6.0f).magnitude(), 0.0001f);
    }

    @Test
    public void testTupleNormalization() {
        assertEquals(new Vector(1, 0, 0), new Vector(4, 0, 0).normalize());
        assertEquals(new Vector(0, 1, 0), new Vector(0, 8, 0).normalize());
        assertEquals(new Vector(0, 0, 1), new Vector(0, 0, 9).normalize());
        Tuple expected = new Vector(0.26726f, 0.53452f, 0.80178f);
        Tuple result = new Vector(1, 2, 3).normalize();
        assertEquals(expected, result);
    }

    @Test
    public void testDotProduct() {
        Tuple a1 = new Vector(1, 2, 3);
        Tuple a2 = new Vector(2, 3, 4);

        assertEquals(20, a1.dot(a2));
    }

    @Test
    public void testVectorCrossProduct() {
        Vector a1 = new Vector(1, 2, 3);
        Vector a2 = new Vector(2, 3, 4);

        assertEquals(new Vector(-1, 2, -1), a1.cross(a2));
        assertEquals(new Vector(1, -2, 1), a2.cross(a1));
    }

    @Test
    public void testColorTuple() {
        Color c = new Color(-0.5f, 0.4f, 1.7f);
        assertEquals(-0.5f, c.getRed());
        assertEquals(0.4f, c.getGreen());
        assertEquals(1.7f, c.getBlue());
    }

    @Test
    public void testColorAddition() {
        Color c1 = new Color(0.9f, 0.6f, 0.75f);
        Color c2 = new Color(0.7f, 0.1f, 0.25f);
        assertEquals(new Color(1.6f, 0.7f, 1.0f), c1.plus(c2));
        assertEquals(new Color(1.6f, 0.7f, 1.0f), c2.plus(c1));
    }

    @Test
    public void testColorSubtraction() {
        Color c1 = new Color(0.9f, 0.6f, 0.75f);
        Color c2 = new Color(0.7f, 0.1f, 0.25f);
        assertEquals(new Color(0.2f, 0.5f, 0.5f), c1.minus(c2));
        assertEquals(new Color(-0.2f, -0.5f, -0.5f), c2.minus(c1));
    }

    @Test
    public void testColorMultiplicationByScalar() {
        Color c = new Color(0.2f, 0.3f, 0.4f);
        assertEquals(new Color(0.4f, 0.6f, 0.8f), c.times(2));
    }

    @Test
    public void testColorMultiplicationByColor() {
        Color c1 = new Color(1, 0.2f, 0.4f);
        Color c2 = new Color(0.9f, 1, 0.1f);
        assertEquals(new Color(0.9f, 0.2f, 0.04f), c1.times(c2));
        assertEquals(new Color(0.9f, 0.2f, 0.04f), c2.times(c1));
    }
}