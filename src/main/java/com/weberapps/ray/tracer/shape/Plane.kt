package com.weberapps.ray.tracer.shape

import com.weberapps.ray.tracer.constants.EPSILON
import com.weberapps.ray.tracer.intersection.Intersection
import com.weberapps.ray.tracer.intersection.Intersections
import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.material.SolidColor
import kotlin.math.abs

class Plane(
  override var transform: Matrix = Matrix.eye(4),
  override var material: Material = SolidColor(),
  override var parent: Shape? = null
): MaterializedShape {
  override fun localIntersect(localRay: Ray): Intersections {
    if (abs(localRay.direction.y) < EPSILON) return Intersections(0)

    val t = -localRay.origin.y / localRay.direction.y
    return Intersections(
      1,
      arrayListOf(Intersection(t, this))
    )
  }

  override fun localNormal(localPoint: Point): Vector {
    return Vector(0f, 1f, 0f)
  }
}