package com.weberapps.ray.tracer.material

import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.shape.Shape

class GradientMaterial(
  val color: Color                    = Color.WHITE,
  val endColor: Color                 = Color.BLACK,
  override val transform: Matrix      = Matrix.eye(4),
  override val ambient: Float         = 0.1f,
  override val diffuse: Float         = 0.9f,
  override val specular: Float        = 0.9f,
  override val shininess: Int         = 200,
  override val reflective: Float      = 0f,
  override val transparency: Float    = 0f,
  override val refractiveIndex: Float = VACUUM
): IMaterial {
  override fun effectiveColor(shape: Shape, worldSpacePoint: Point, light: Light): Color {
    return gradientColorAtObject(patternSpacePoint(shape, worldSpacePoint)) * light.intensity
  }

  fun gradientColorAtObject(point: Point): Color {
    val distance = endColor - color
    val fraction = point.x - Math.floor(point.x.toDouble()).toFloat()

    return color + distance * fraction
  }

  override fun equals(other: Any?): Boolean {
    if (other !is GradientMaterial) return false

    return super.equals(other)
      && color == other.color
      && endColor == other.endColor
  }
}