package com.weberapps.ray.examples.shapes

import com.weberapps.ray.tracer.material.SolidColor
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.shape.*

class Pencil(
  override var transform: Matrix = Matrix.eye(4),
  override val shapes: ArrayList<Shape> = arrayListOf(),
  override var parent: Shape? = null
): Group(transform, shapes, parent) {
  init {
    val point = Cone(
      minimum = 0f,
      maximum = 1f,
      transform = Transformation.scale(1f, 3f, 1f),
      material = SolidColor(Color(0.9f, 0.9f, 0.7f))
    )
    val shaft = Cylinder(
      minimum = 3f,
      maximum = 23f,
      material = SolidColor(Color(0.8f, 0.8f, 0.1f), specular = 0.3f, shininess = 50)
    )
    val eraser = Cylinder(
      minimum = 23f,
      maximum = 25f,
      closed = true,
      material = SolidColor(Color(1f, 0.7f, 0.75f), diffuse = 1f, specular = 0f)
    )
    add(point).add(shaft).add(eraser)
    this.transform *= Transformation.scale(0.15f)
  }
}