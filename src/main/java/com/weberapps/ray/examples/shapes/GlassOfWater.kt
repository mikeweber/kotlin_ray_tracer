package com.weberapps.ray.examples.shapes

import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.shape.Cone
import com.weberapps.ray.tracer.shape.Group
import com.weberapps.ray.tracer.shape.Shape

class GlassOfWater(transform: Matrix = Matrix.eye(4), shapes: ArrayList<Shape> = arrayListOf(), material: Material = Material(), parent: Shape? = null) :
  Group(transform, shapes, material, parent) {
  init {
    val glass = Cone(
      transform = Transformation.translation(0f, -9f, 0f) * Transformation.scale(0.25f, 3f, 0.25f),
      material = Material(diffuse = 0.1f, reflective = 0.7f, transparency = 1f, refractiveIndex = Material.GLASS, shininess = 500),
      minimum = 3f,
      maximum = 4f
    )
    val water = Cone(
      transform = Transformation.translation(0f, -9.1f, 0f) * Transformation.scale(0.2f, 3f, 0.2f),
      material = Material(diffuse = 0f, reflective = 0.7f, transparency = 1f, refractiveIndex = Material.WATER, shininess = 500),
      closed = true,
      minimum = 3f,
      maximum = 3.7f
    )
    add(glass).add(water)
  }
}