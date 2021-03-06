package com.weberapps.ray.tracer.renderer

import com.weberapps.ray.tracer.math.Color
import kotlinx.coroutines.Deferred

class Canvas(val width: Int, val height: Int, private val defaultColor: Color = Color(
  0.0f,
  0.0f,
  0.0f
)
) {
  private var pixels = Array(width * height) { defaultColor }
  private var deferredPixels = Array<Deferred<Color>?>(width * height) { null }

  operator fun plusAssign(other: Canvas) {
    if (width != other.width || height != other.height) return

    for ((index, value) in pixels.iterator().withIndex()) {
      pixels[index] = value + other.pixels[index]
    }
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

  suspend fun resolvePixels() {
    for ((i, p) in deferredPixels.withIndex()) pixels[i] = p?.await() ?: defaultColor
  }

  fun setDeferredPixel(x: Int, y: Int, deferred: Deferred<Color>) {
    deferredPixels[x + y * width] = deferred
  }

  fun setPixel(x: Int, y: Int, pixel: Color) {
    pixels[x + y * width] = pixel
  }
}
