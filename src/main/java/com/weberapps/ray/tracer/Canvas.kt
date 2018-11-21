package com.weberapps.ray.tracer

class Canvas(val width: Int, val height: Int, private val defaultColor: com.weberapps.ray.tracer.Color = com.weberapps.ray.tracer.Color(
  0.0f,
  0.0f,
  0.0f
)
) {
  private var pixels = Array(width * height) { defaultColor }

  operator fun plusAssign(other: com.weberapps.ray.tracer.Canvas) {
    if (width != other.width || height != other.height) return

    for ((index, value) in pixels.iterator().withIndex()) {
      pixels[index] = value + other.pixels[index]
    }
  }

  operator fun div(scalar: Float): com.weberapps.ray.tracer.Canvas {
    val newCanvas = com.weberapps.ray.tracer.Canvas(width, height)
    for (x in 0..(width - 1)) {
      for (y in 0..(height - 1)) {
        newCanvas.setPixel(x, y, getPixel(x, y) / scalar.toFloat())
      }
    }
    return newCanvas
  }

  fun getPixel(x: Int, y: Int): com.weberapps.ray.tracer.Color {
    return pixels[x + y * width]
  }

  fun setPixel(x: Int, y: Int, pixel: com.weberapps.ray.tracer.Color) {
    pixels[x + y * width] = pixel
  }
}