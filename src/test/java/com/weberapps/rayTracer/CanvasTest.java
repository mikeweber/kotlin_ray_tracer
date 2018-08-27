package com.weberapps.rayTracer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CanvasTest {
    @Test
    public void testInitialization() {
        Canvas c = new Canvas(10, 20);
        assertEquals(10, c.getWidth());
        assertEquals(20, c.getHeight());
        Color p1 = c.getPixel(5, 10);
        assertEquals(new Color(0, 0, 0), p1);
    }

    @Test
    public void testWriteToCanvas() {
        Canvas c = new Canvas(10, 20);
        Color red = new Color(1, 0, 0);
        Color black = new Color(0, 0, 0);
        c.setPixel(2, 3, red);
        assertEquals(red, c.getPixel(2, 3));
        assertEquals(black, c.getPixel(3, 3));
        assertEquals(black, c.getPixel(1, 3));
        assertEquals(black, c.getPixel(2, 2));
        assertEquals(black, c.getPixel(2, 4));
    }
}
