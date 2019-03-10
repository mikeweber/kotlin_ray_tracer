package com.weberapps.ray.tracer.material

import com.weberapps.ray.tracer.math.*
import com.weberapps.ray.tracer.shape.Shape

class SolidColor(
  val color: Color                    = Color.WHITE,
  override val transform: Matrix      = Matrix.eye(4),
  override val ambient: Float         = 0.1f,
  override val diffuse: Float         = 0.9f,
  override val specular: Float        = 0.9f,
  override val shininess: Int         = 200,
  override val reflective: Float      = 0f,
  override val transparency: Float    = 0f,
  override val refractiveIndex: Float = VACUUM,
  override val roughness: Float       = 0f,
  override val spp: Int               = 1
): Material {
  constructor(r: Float, g: Float, b: Float): this(Color(r, g, b))

  override fun effectiveColor(shape: Shape, worldSpacePoint: Point, light: Light): Color {
    return color * light.intensity
  }

  override fun patternAt(patternSpacePoint: Point): Color {
    return color
  }

  override fun equals(other: Any?): Boolean {
    if (other !is SolidColor) return false

    return super.equals(other)
      && color == other.color
  }
}
