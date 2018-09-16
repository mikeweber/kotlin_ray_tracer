package com.weberapps.rayTracer

class Canvas(val width: Int, val height: Int, private val defaultColor: Color = Color(0.0f, 0.0f, 0.0f)) {
    private var pixels = Array(width * height) { defaultColor }

    operator fun plus(other: Canvas): Canvas {
        if (width != other.width || height != other.height) return Canvas(0, 0)

        val newCanvas = Canvas(width, height)
        for (x in 0..(width - 1)) {
            for (y in 0..(height - 1)) {
                newCanvas.setPixel(x, y, getPixel(x, y) + other.getPixel(x, y))
            }
        }
        return newCanvas
    }

    operator fun div(scalar: Float): Canvas {
        val newCanvas = Canvas(width, height)
        for (x in 0..(width - 1)) {
            for (y in 0..(height - 1)) {
                newCanvas.setPixel(x, y, getPixel(x, y) / scalar)
            }
        }
        return newCanvas
    }

    fun getPixel(x: Int, y: Int): Color {
        return pixels[x + y * width]
    }

    fun setPixel(x: Int, y: Int, pixel: Color) {
        pixels[x + y * width] = pixel
    }
}