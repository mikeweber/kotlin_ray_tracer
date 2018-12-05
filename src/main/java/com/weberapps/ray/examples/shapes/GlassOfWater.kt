package com.weberapps.ray.examples.shapes

import com.weberapps.ray.tracer.material.GLASS
import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.material.SolidColor
import com.weberapps.ray.tracer.material.WATER
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.shape.*

class GlassOfWater(
  override var transform: Matrix = Matrix.eye(4),
  override val shapes: ArrayList<Shape> = arrayListOf(),
  override var parent: Shape? = null
): Group(transform, shapes, parent) {
  init {
    val outerGlass = Cone(
      Transformation.scale(0.25f, 3f, 0.25f) * Transformation.translation(0f, -3f, 0f),
      material = SolidColor(diffuse = 0.1f, reflective = 0.7f, transparency = 1f, refractiveIndex = GLASS, shininess = 500),
      minimum = 2.9f,
      maximum = 4f,
      closed = true
    )

    val innerGlass = Cone(
      Transformation.scale(0.25f, 3f, 0.25f) * Transformation.translation(0f, -2.7f, 0f),
      material = SolidColor(diffuse = 0.1f, reflective = 0.7f, transparency = 1f, refractiveIndex = GLASS, shininess = 500),
      minimum = 2.8f,
      maximum = 4f
    )
    val water = Cone(
      transform = Transformation.scale(0.25f, 3f, 0.25f) * Transformation.translation(0f, -2.7f, 0f),
      material = SolidColor(diffuse = 0f, reflective = 0.7f, transparency = 1f, refractiveIndex = WATER, shininess = 500),
      closed = true,
      minimum = 2.8f,
      maximum = 3.4f
    )
    val glass = CSGDifference(
        outerGlass,
        innerGlass
      )

    add(glass).add(water)
  }
}
