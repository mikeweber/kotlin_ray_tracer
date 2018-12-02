package com.weberapps.ray.tracer.shape

import com.weberapps.ray.tracer.intersection.Intersection
import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.material.SolidColor
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Vector

class SmoothTriangle(
  p1: Point,
  p2: Point,
  p3: Point,
  val n1: Vector,
  val n2: Vector,
  val n3: Vector,
  transform: Matrix = Matrix.eye(4),
  material: Material = SolidColor(),
  parent: Shape? = null
) : Triangle(p1, p2, p3, transform, material, parent) {
  override fun normal(point: Point, hit: Intersection): Vector {
    return normalToWorld(localNormal(worldToObject(point), hit))
  }

  override fun localNormal(localPoint: Point, hit: Intersection): Vector {
    if (hit.u == null || hit.v == null) return Vector(0f, 0f, 0f)

    return n2 * hit.u + n3 * hit.v + n1 * (1 - hit.u - hit.v)
  }
}
