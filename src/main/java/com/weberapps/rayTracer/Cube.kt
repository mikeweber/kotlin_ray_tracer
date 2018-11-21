package com.weberapps.rayTracer

import java.lang.Float.POSITIVE_INFINITY

class Cube(override var transform: Matrix = Matrix.eye(4), override var material: Material = Material()) : Shape {
  override fun localIntersect(localRay: Ray): Intersections {
    val (xtmin, xtmax) = checkAxis(localRay.origin.x, localRay.direction.x)
    val (ytmin, ytmax) = checkAxis(localRay.origin.y, localRay.direction.y)
    val (ztmin, ztmax) = checkAxis(localRay.origin.z, localRay.direction.z)

    val tmin = Math.max(Math.max(xtmin, ytmin), ztmin)
    val tmax = Math.min(Math.min(xtmax, ytmax), ztmax)

    return Intersections(2, arrayListOf(Intersection(tmin, this), Intersection(tmax, this)))
  }

  override fun localNormal(localPoint: Point): Vector {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  private fun checkAxis(origin: Float, direction: Float): Pair<Float, Float> {
    val tminNumerator = -1 - origin
    val tmaxNumerator =  1 - origin

    var minMax = if (Math.abs(direction) >= EPSILON) {
      Pair(tminNumerator / direction, tmaxNumerator / direction)
    } else {
      Pair(tminNumerator * POSITIVE_INFINITY, tmaxNumerator * POSITIVE_INFINITY)
    }
    if (minMax.first <= minMax.second) return minMax

    return Pair(minMax.second, minMax.first)
  }
}