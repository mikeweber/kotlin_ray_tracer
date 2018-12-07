package com.weberapps.ray.examples.shapes

import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.material.SolidColor
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.shape.*

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

    val corners = CSGUnion(
      arrayListOf(
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
    val sides = CSGUnion(
      Cube(Transformation.scale(max, 1f - radius, 1f), material),
      CSGUnion(
        Cube(Transformation.scale(max, 1f, 1f - radius), material),
        Cube(Transformation.scale(1f, max, 1f - radius), material)
      )
    )
    val edges = CSGUnion(
      arrayListOf(
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
    val pips = CSGUnion(
     arrayListOf(
       side1Pips(),
       side2Pips(),
       side3Pips(),
       side4Pips(),
       side5Pips(),
       side6Pips()
     )
    )

    val outline = CSGUnion(corners, edges)
    val body = CSGUnion(sides, outline)
    val die = CSGDifference(body, pips)
    add(outline)
  }

  private fun side1Pips(): Group {
    return groupOfPips(4)
  }

  private fun side2Pips(): Group {
    return groupOfPips(2, 6).apply { transform = Transformation.rotateX(TAU / 4) }
  }

  private fun side3Pips(): Group {
    return groupOfPips(2, 4, 6).apply { transform = Transformation.rotateY(TAU / 4) }
  }

  private fun side4Pips(): Group {
    return groupOfPips(1, 2, 6, 7).apply { transform = Transformation.rotateY(-TAU / 4) }
  }

  private fun side5Pips(): Group {
    return groupOfPips(1, 2, 4, 6, 7).apply { transform = Transformation.rotateX(-TAU / 4) }
  }

  private fun side6Pips(): Group {
    return groupOfPips(1, 2, 3, 5, 6, 7).apply { transform = Transformation.rotateY(TAU / 2) }
  }

  // pip layout
  // 1   2
  // 3 4 5
  // 6   7
  private fun groupOfPips(vararg pipIds: Int): Group {
    val top = 0.6f
    val mid = 0f
    val bot = -top

    val allPips = listOf(
      pip(mid, mid),
      pip(bot, top),
      pip(top, top),
      pip(bot, mid),
      pip(mid, mid),
      pip(top, mid),
      pip(bot, bot),
      pip(top, bot)
    )

    val group = Group()
      for (id in pipIds) {
        group.add(allPips[id])
      }
    return group
  }

  private fun pip(x: Float, y: Float): Shape {
    val scale = 0.2f
    return Sphere(Transformation.translation(x, y, -1f) * Transformation.scale(scale, scale, scale / 2f))
  }
}