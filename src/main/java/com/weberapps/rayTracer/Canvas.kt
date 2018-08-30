package com.weberapps.rayTracer

class Canvas (val width: Int, val height: Int, val defaultColor: Color = Color(0.0f, 0.0f, 0.0f)) {
    private var pixels = Array<Color>(width * height) { defaultColor }

    fun getPixel(x: Int, y: Int): Color {
        return pixels[x + y * width]
    }

    fun setPixel(x: Int, y: Int, pixel: Color) {
        pixels[x + y * width] = pixel
    }
}