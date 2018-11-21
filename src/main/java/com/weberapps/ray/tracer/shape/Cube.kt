package com.weberapps.ray.tracer.shape

import com.weberapps.ray.tracer.constants.EPSILON
import com.weberapps.ray.tracer.intersection.Intersection
import com.weberapps.ray.tracer.intersection.Intersections
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.material.Material
import java.lang.Float.POSITIVE_INFINITY

class Cube(override var transform: Matrix = Matrix.eye(4), override var material: Material = Material()) :
  Shape {
  override fun localIntersect(localRay: Ray): Intersections {
    val (xtmin, xtmax) = checkAxis(localRay.origin.x, localRay.direction.x)
    val (ytmin, ytmax) = checkAxis(localRay.origin.y, localRay.direction.y)
    val (ztmin, ztmax) = checkAxis(localRay.origin.z, localRay.direction.z)

    val tmin = max(xtmin, ytmin, ztmin)
    val tmax = min(xtmax, ytmax, ztmax)

    if (tmin > tmax) return Intersections(0)

    return Intersections(
      2,
      arrayListOf(
        Intersection(tmin, this),
        Intersection(tmax, this)
      )
    )
  }

  override fun localNormal(localPoint: Point): Vector {
    return when (max(Math.abs(localPoint.x), Math.abs(localPoint.y), Math.abs(localPoint.z))) {
      Math.abs(localPoint.x) -> Vector(localPoint.x, 0f, 0f)
      Math.abs(localPoint.y) -> Vector(0f, localPoint.y, 0f)
      else -> Vector(0f, 0f, localPoint.z)
    }
  }

  private fun checkAxis(origin: Float, direction: Float): Pair<Float, Float> {
    val tminNumerator = -1 - origin
    val tmaxNumerator =  1 - origin

    var (tmin, tmax) = if (Math.abs(direction) > EPSILON) {
      Pair(tminNumerator / direction, tmaxNumerator / direction)
    } else {
      Pair(tminNumerator * POSITIVE_INFINITY, tmaxNumerator * POSITIVE_INFINITY)
    }

    if (tmin > tmax) return Pair(tmax, tmin)
    return Pair(tmin, tmax)
  }

  private fun min(a: Float, b: Float, c: Float): Float {
    return Math.min(Math.min(a, b), c)
  }

  private fun max(a: Float, b: Float, c: Float): Float {
    return Math.max(Math.max(a, b), c)
  }
}