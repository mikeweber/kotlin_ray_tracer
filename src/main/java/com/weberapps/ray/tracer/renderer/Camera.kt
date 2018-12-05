package com.weberapps.ray.tracer.renderer

import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Matrix
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Camera(val hsize: Int, val vsize: Int, fieldOfView: Double = (TAU / 4), var transform: Matrix = Matrix.eye(4)) {
  var halfWidth = Math.tan(fieldOfView / 2.0)
  var halfHeight = Math.tan(fieldOfView / 2.0)
  var pixelSize: Float = 0f
  var fieldOfView: Double = (TAU / 4.0)
  set(value) {
    field = value
    pixelSize = calculatePixelSize()
  }

  init {
    // A bit of a hack to make sure calculatePixelSize is called on fieldOfView assignment
    this.fieldOfView = fieldOfView
  }

  fun render(world: World): Canvas {
    val canvas = Canvas(hsize, vsize)

    for (y in 0..(vsize - 1)) {
      for (x in 0..(hsize - 1)) {
        canvas.setPixel(x, y, colorForPixel(x, y, world))
      }
    }

    return canvas
  }

  fun coroutineRender(world: World): Canvas {
    val canvas = Canvas(hsize, vsize)

    for (y in 0..(vsize - 1)) {
      for (x in 0..(hsize - 1)) {
        val color = GlobalScope.async {
          colorForPixel(x, y, world)

        }
        canvas.setDeferredPixel(x, y, color)
      }
    }
    runBlocking { canvas.resolvePixels() }

    return canvas
  }

  fun colorForPixel(px: Int, py: Int, world: World): Color {
    val ray = rayForPixel(px, py)
    return world.colorAt(ray)
  }

  fun rayForPixel(px: Int, py: Int): Ray {
    val xOffset = (px + 0.5f) * pixelSize
    val yOffset = (py + 0.5f) * pixelSize

    val worldX = halfWidth - xOffset
    val worldY = halfHeight - yOffset

    val invertedTransform = transform.inverse()
    val pixel  = invertedTransform * Point(worldX.toFloat(), worldY.toFloat(), -1f)
    val origin = invertedTransform * Point(0f, 0f, 0f)
    val dir = (pixel - origin)
    val direction = dir.normalize()

    return Ray(origin, direction)
  }

  private fun calculatePixelSize(): Float {
    val halfView = fieldOfView / 2f
    halfWidth = Math.tan(halfView)
    halfHeight = Math.tan(halfView)
    val aspect = hsize.toFloat() / vsize.toFloat()

    if (aspect >= 1) {
      halfHeight /= aspect
    } else {
      halfWidth *= aspect
    }

    return (halfWidth * 2 / hsize).toFloat()
  }
}
