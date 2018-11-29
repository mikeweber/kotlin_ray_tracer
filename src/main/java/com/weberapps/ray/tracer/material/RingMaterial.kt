package com.weberapps.ray.tracer.material

import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.floatRoot
import com.weberapps.ray.tracer.shape.Shape

class RingMaterial(
  private val tick: Material          = SolidColor(Color.WHITE),
  private val tock: Material          = SolidColor(Color.BLACK),
  override val transform: Matrix      = Matrix.eye(4),
  override val ambient: Float         = 0.1f,
  override val diffuse: Float         = 0.9f,
  override val specular: Float        = 0.9f,
  override val shininess: Int         = 200,
  override val reflective: Float      = 0.1f,
  override val transparency: Float    = 0f,
  override val refractiveIndex: Float = VACUUM
) : Material {
  override fun effectiveColor(shape: Shape, worldSpacePoint: Point, light: Light): Color {
    return patternAt(patternSpacePoint(shape, worldSpacePoint)) * light.intensity
  }

  override fun patternAt(patternSpacePoint: Point): Color {
    val sqrt = floatRoot(patternSpacePoint.x * patternSpacePoint.x + patternSpacePoint.z * patternSpacePoint.z)
    val floor = Math.floor(sqrt.toDouble())
    val tickOrTock = if ((floor % 2) == 0.0) tick else tock

    return tickOrTock.patternAt(patternSpacePoint)
  }
}