package com.weberapps.ray.tracer.shape

import com.weberapps.ray.tracer.intersection.Intersections
import com.weberapps.ray.tracer.math.Ray

interface CSGOperation: IGroup {
  var left: Shape
  var right: Shape

  override fun localIntersect(localRay: Ray): Intersections {
    return filterIntersections(left.intersect(localRay).add(right.intersect(localRay)))
  }

  fun filterIntersections(xs: Intersections): Intersections {
    var inLeft = false
    var inRight = false

    val result = Intersections()
    for (intersection in xs) {
      val leftHit = left.includes(intersection.shape)

      if (intersectionAllowed(leftHit, inLeft, inRight)) {
        result.add(intersection)
      }

      if (leftHit) {
        inLeft = !inLeft
      } else {
        inRight = !inRight
      }
    }

    return result
  }

  fun intersectionAllowed(leftHit: Boolean, inLeft: Boolean, inRight: Boolean): Boolean

  override fun includes(other: Shape): Boolean {
    return equals(other) || left.includes(other) || right.includes(other)
  }

  fun equals(other: CSGOperation): Boolean {
    return super.equals(other) && left == other.left && right == other.right && shapes == other.shapes
  }
}
