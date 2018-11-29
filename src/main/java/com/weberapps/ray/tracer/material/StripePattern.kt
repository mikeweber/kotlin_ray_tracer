package com.weberapps.ray.tracer.material

import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.shape.Shape

class StripePattern(
  val zig: Color                      = Color.WHITE,
  val zag: Color                      = Color.BLACK,
  override val transform: Matrix      = Matrix.eye(4),
  override val ambient: Float         = 0.1f,
  override val diffuse: Float         = 0.9f,
  override val specular: Float        = 0.9f,
  override val shininess: Int         = 200,
  override val reflective: Float      = 0f,
  override val transparency: Float    = 0f,
  override val refractiveIndex: Float = VACUUM
): Material {
  override fun effectiveColor(shape: Shape, worldSpacePoint: Point, light: Light): Color {
    return patternAt(patternSpacePoint(shape, worldSpacePoint)) * light.intensity
  }

  fun patternAt(point: Point): Color {
    return if ((Math.floor(point.x.toDouble()).toInt() % 2) == 0) zig else zag
  }

  override fun equals(other: Any?): Boolean {
    if (other !is StripePattern) return false

    return super.equals(other)
      && zig == other.zig
      && zag == other.zag
  }
}