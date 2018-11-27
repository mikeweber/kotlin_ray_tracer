package com.weberapps.ray.examples.shapes

import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.shape.Cone
import com.weberapps.ray.tracer.shape.Cylinder
import com.weberapps.ray.tracer.shape.Group
import com.weberapps.ray.tracer.shape.Shape

class Pencil(transform: Matrix = Matrix.eye(4), shapes: ArrayList<Shape> = arrayListOf(), material: Material = Material(), parent: Shape? = null) :
  Group(transform, shapes, material, parent) {
  init {
    val point = Cone(
      minimum = 0f,
      maximum = 1f,
      transform = Transformation.scale(1f, 3f, 1f),
      material = Material(Color(0.9f, 0.9f, 0.7f))
    )
    val shaft = Cylinder(
      minimum = 3f,
      maximum = 23f,
      material = Material(Color(0.8f, 0.8f, 0.1f), specular = 0.3f, shininess = 50)
    )
    val eraser = Cylinder(
      minimum = 23f,
      maximum = 25f,
      closed = true,
      material = Material(Color(1f, 0.7f, 0.75f), diffuse = 1f, specular = 0f)
    )
    add(point).add(shaft).add(eraser)
    this.transform *= Transformation.scale(0.15f)
  }
}