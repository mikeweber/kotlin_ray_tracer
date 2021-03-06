package com.weberapps.ray.tracer.material

import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.shape.Shape

class CheckeredPattern(
  val tick: Material                  = SolidColor(Color.WHITE),
  val tock: Material                  = SolidColor(Color.BLACK),
  override val transform: Matrix      = Matrix.eye(4),
  override val ambient: Float         = 0.1f,
  override val diffuse: Float         = 0.9f,
  override val specular: Float        = 0.9f,
  override val shininess: Int         = 200,
  override val reflective: Float      = 0.1f,
  override val transparency: Float    = 0f,
  override val refractiveIndex: Float = VACUUM,
  override val roughness: Float       = 0f
): Material {
  override fun effectiveColor(shape: Shape, worldSpacePoint: Point, light: Light): Color {
    return patternAt(patternSpacePoint(shape, worldSpacePoint)) * light.intensity
  }

  override fun patternAt(patternSpacePoint: Point): Color {
    val x = Math.floor(patternSpacePoint.x.toDouble()).toInt()
    val y = Math.floor(patternSpacePoint.y.toDouble()).toInt()
    val z = Math.floor(patternSpacePoint.z.toDouble()).toInt()

    val material = if (((x + y + z) % 2) == 0) tick else tock
    return material.patternAt(patternSpacePoint)
  }

  override fun equals(other: Any?): Boolean {
    if (other !is CheckeredPattern) return false

    return super.equals(other)
      && tick == other.tick
      && tock == other.tock
  }
}
