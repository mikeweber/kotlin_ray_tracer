package com.weberapps.rayTracer

class Canvas (val width: Int, val height: Int) {
    private var pixels = Array<Color>(width * height) { Color(0.0f, 0.0f, 0.0f) }

    fun getPixel(x: Int, y: Int): Color {
        return pixels[x + y * width]
    }

    fun setPixel(x: Int, y: Int, pixel: Color) {
        pixels[x + y * width] = pixel
    }
}