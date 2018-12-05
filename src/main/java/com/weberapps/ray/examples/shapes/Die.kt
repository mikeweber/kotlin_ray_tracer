package com.weberapps.ray.examples.shapes

import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.material.SolidColor
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.math.floatRoot
import com.weberapps.ray.tracer.shape.*
import com.weberapps.ray.tracer.shape.Cube
import com.weberapps.ray.tracer.shape.Cylinder
import com.weberapps.ray.tracer.shape.Group
import com.weberapps.ray.tracer.shape.Shape

class Die(
  override var transform: Matrix = Matrix.eye(4),
  radius: Float = 0.15f,
  var material: Material = SolidColor(),
  override val shapes: ArrayList<Shape> = arrayListOf(),
  override var parent: Shape? = null
): Group(transform, shapes, parent) {
  init {
    val min = (-1f + radius)
    val max = ( 1f - radius)
    val adjustedMin = min / radius
    val adjustedMax = max / radius

    val corners = Group(
      shapes = arrayListOf(
        Sphere(Transformation.translation(max, max, max) * Transformation.scale(radius), material),
        Sphere(Transformation.translation(max, max, min) * Transformation.scale(radius), material),
        Sphere(Transformation.translation(max, min, max) * Transformation.scale(radius), material),
        Sphere(Transformation.translation(max, min, min) * Transformation.scale(radius), material),
        Sphere(Transformation.translation(min, max, max) * Transformation.scale(radius), material),
        Sphere(Transformation.translation(min, max, min) * Transformation.scale(radius), material),
        Sphere(Transformation.translation(min, min, max) * Transformation.scale(radius), material),
        Sphere(Transformation.translation(min, min, min) * Transformation.scale(radius), material)
      )
    )
    val sides = Group(
      shapes = arrayListOf(
        Cube(Transformation.scale(max, 1f - radius, 1f), material),
        Cube(Transformation.scale(max, 1f, 1f - radius), material),
        Cube(Transformation.scale(1f, max, 1f - radius), material)
      )
    )
    val edges = Group(
      shapes = arrayListOf(
        // sides around Y axis
        Cylinder(material = material, transform = Transformation.translation(max,  0f, max) * Transformation.scale(radius), minimum = adjustedMin, maximum = adjustedMax),
        Cylinder(material = material, transform = Transformation.translation(max,  0f, min) * Transformation.scale(radius), minimum = adjustedMin, maximum = adjustedMax),
        Cylinder(material = material, transform = Transformation.translation(min,  0f, max) * Transformation.scale(radius), minimum = adjustedMin, maximum = adjustedMax),
        Cylinder(material = material, transform = Transformation.translation(min,  0f, min) * Transformation.scale(radius), minimum = adjustedMin, maximum = adjustedMax),
        // sides around X axis
        Cylinder(material = material, transform = Transformation.translation(max, max,  0f) * Transformation.scale(radius) * Transformation.rotateX(TAU / 4), minimum = adjustedMin, maximum = adjustedMax),
        Cylinder(material = material, transform = Transformation.translation(max, min,  0f) * Transformation.scale(radius) * Transformation.rotateX(TAU / 4), minimum = adjustedMin, maximum = adjustedMax),
        Cylinder(material = material, transform = Transformation.translation(min, max,  0f) * Transformation.scale(radius) * Transformation.rotateX(TAU / 4), minimum = adjustedMin, maximum = adjustedMax),
        Cylinder(material = material, transform = Transformation.translation(min, min,  0f) * Transformation.scale(radius) * Transformation.rotateX(TAU / 4), minimum = adjustedMin, maximum = adjustedMax),
        // sides around Z axis
        Cylinder(material = material, transform = Transformation.translation( 0f, max, max) * Transformation.scale(radius) * Transformation.rotateZ(TAU / 4), minimum = adjustedMin, maximum = adjustedMax),
        Cylinder(material = material, transform = Transformation.translation( 0f, max, min) * Transformation.scale(radius) * Transformation.rotateZ(TAU / 4), minimum = adjustedMin, maximum = adjustedMax),
        Cylinder(material = material, transform = Transformation.translation( 0f, min, max) * Transformation.scale(radius) * Transformation.rotateZ(TAU / 4), minimum = adjustedMin, maximum = adjustedMax),
        Cylinder(material = material, transform = Transformation.translation( 0f, min, min) * Transformation.scale(radius) * Transformation.rotateZ(TAU / 4), minimum = adjustedMin, maximum = adjustedMax)
      )
    )

    val body = CSGUnion(
      sides,
      CSGUnion(
        corners,
        edges
      )
    )
    add(body)
  }
}