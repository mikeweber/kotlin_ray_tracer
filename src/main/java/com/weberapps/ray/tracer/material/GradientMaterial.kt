package com.weberapps.ray.tracer.material

import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.shape.Shape

class GradientMaterial(
  private val start: Material         = SolidColor(Color.WHITE),
  private val end: Material           = SolidColor(Color.BLACK),
  override val transform: Matrix      = Matrix.eye(4),
  override val ambient: Float         = 0.1f,
  override val diffuse: Float         = 0.9f,
  override val specular: Float        = 0.9f,
  override val shininess: Int         = 200,
  override val reflective: Float      = 0f,
  override val transparency: Float    = 0f,
  override val refractiveIndex: Float = VACUUM,
  override val roughness: Float       = 0f
): Material {
  override fun effectiveColor(shape: Shape, worldSpacePoint: Point, light: Light): Color {
    return patternAt(patternSpacePoint(shape, worldSpacePoint)) * light.intensity
  }

  override fun patternAt(patternSpacePoint: Point): Color {
    val color = start.patternAt(patternSpacePoint)
    val distance = end.patternAt(patternSpacePoint) - color
    val fraction = patternSpacePoint.x - Math.floor(patternSpacePoint.x.toDouble()).toFloat()

    return color + distance * fraction
  }

  override fun equals(other: Any?): Boolean {
    if (other !is GradientMaterial) return false

    return super.equals(other)
      && start == other.start
      && end == other.end
  }
}
