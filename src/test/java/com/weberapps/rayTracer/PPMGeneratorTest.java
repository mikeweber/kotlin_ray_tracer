package com.weberapps.rayTracer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PPMGeneratorTest {
    @Test
    public void testHeader() {
        String expected = "P3\n5 3\n255";
        Canvas c = new Canvas(5, 3);
        String[] result = new PPMGenerator(c).generate().split("\n");
        assertEquals("P3", result[0]);
        assertEquals("5 3", result[1]);
        assertEquals("255", result[2]);
    }

    @Test
    public void testImageOutput() {
        Color red = new Color(1, 0, 0);
        Color green = new Color(0, 1, 0);
        Color blue = new Color(0, 0, 1);
        String expected = "P3\n5 3\n255\n0 0 0 255 0 0 0 0 0 0 0 0 0 0 0\n0 0 0 0 0 0 0 255 0 0 0 0 0 0 0\n0 0 0 0 0 0 0 0 0 0 0 0 0 0 255";
        Canvas c = new Canvas(5, 3);

        c.setPixel(1, 0, red);
        c.setPixel(2, 1, green);
        c.setPixel(4, 2, blue);

        assertEquals(expected, new PPMGenerator(c).generate());
    }
}
