package com.weberapps.rayTracer

import kotlin.math.abs

class Plane(override var transform: Matrix = Matrix.eye(4), override var material: Material = Material()) : Shape {
  override fun localIntersect(localRay: Ray): Intersections {
    if (abs(localRay.direction.y) < EPSILON) return Intersections(0)

    val t = -localRay.origin.y / localRay.direction.y
    return Intersections().add(Intersection(t, this))
  }

  override fun localNormal(localPoint: Point): Vector {
    return Vector(0f, 1f, 0f)
  }
}