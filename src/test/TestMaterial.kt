package com.weberapps.ray.tracer

import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.material.VACUUM
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.shape.Shape

class TestMaterial(
  override val transform: Matrix      = Matrix.eye(4),
  override val ambient: Float         = 0.1f,
  override val diffuse: Float         = 0.9f,
  override val specular: Float        = 0.9f,
  override val shininess: Int         = 200,
  override val reflective: Float      = 0f,
  override val transparency: Float    = 0f,
  override val refractiveIndex: Float = VACUUM,
  override val roughness: Float       = 0f
) : Material {
  override fun effectiveColor(shape: Shape, worldSpacePoint: Point, light: Light): Color {
    return patternAt(worldSpacePoint)
  }

  override fun patternAt(patternSpacePoint: Point): Color {
    return Color(patternSpacePoint.x, patternSpacePoint.y, patternSpacePoint.z)
  }
}
