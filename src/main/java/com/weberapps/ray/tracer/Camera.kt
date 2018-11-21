package com.weberapps.ray.tracer

class Camera(val hsize: Int, val vsize: Int, fieldOfView: Double = (com.weberapps.ray.tracer.TAU / 4), var transform: com.weberapps.ray.tracer.Matrix = com.weberapps.ray.tracer.Matrix.Companion.eye(
  4
)
) {
  var halfWidth = Math.tan(fieldOfView / 2.0)
  var halfHeight = Math.tan(fieldOfView / 2.0)
  var pixelSize: Float = 0f
  var fieldOfView: Double = (com.weberapps.ray.tracer.TAU / 4.0)
  set(value) {
    field = value
    pixelSize = calculatePixelSize()
  }

  init {
    // A bit of a hack to make sure calculatePixelSize is called on fieldOfView assignment
    this.fieldOfView = fieldOfView
  }

  fun render(world: com.weberapps.ray.tracer.World): com.weberapps.ray.tracer.Canvas {
    val canvas = com.weberapps.ray.tracer.Canvas(hsize, vsize)
    for (y in 0..(vsize - 1)) {
      for (x in 0..(hsize - 1)) {
        val ray = rayForPixel(x, y)
        val color = world.colorAt(ray)
        canvas.setPixel(x, y, color)
      }
    }
    return canvas
  }

  fun rayForPixel(px: Int, py: Int): com.weberapps.ray.tracer.Ray {
    val xOffset = (px + 0.5f) * pixelSize
    val yOffset = (py + 0.5f) * pixelSize

    val worldX = halfWidth - xOffset
    val worldY = halfHeight - yOffset

    val invertedTransform = transform.inverse()
    val pixel  = invertedTransform * com.weberapps.ray.tracer.Point(worldX.toFloat(), worldY.toFloat(), -1f)
    val origin = invertedTransform * com.weberapps.ray.tracer.Point(0f, 0f, 0f)
    val dir = (pixel - origin)
    val direction = dir.normalize()

    return com.weberapps.ray.tracer.Ray(origin, direction)
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