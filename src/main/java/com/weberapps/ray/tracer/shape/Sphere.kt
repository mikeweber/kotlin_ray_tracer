package com.weberapps.ray.tracer.shape

import com.weberapps.ray.tracer.intersection.Intersection
import com.weberapps.ray.tracer.intersection.Intersections
import com.weberapps.ray.tracer.material.IMaterial
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.material.Material

class Sphere(
  override var transform: Matrix = Matrix.eye(4),
  override var material: IMaterial = Material(),
  override var parent: Shape? = null
) :
  Shape {
  override fun equals(other: Any?): Boolean {
    if (other !is Shape) return false

    return transform == other.transform && material == other.material
  }

  override fun localIntersect(localRay: Ray): Intersections {
    val sphereToRay = localRay.origin - localCenter()
    val a = localRay.direction.dot(localRay.direction)
    val b = 2 * localRay.direction.dot(sphereToRay)
    val c = sphereToRay.dot(sphereToRay) - 1

    val discriminant = b * b - 4 * a * c
    if (discriminant < 0) return Intersections()

    val sqRtDiscriminant = Math.sqrt(discriminant.toDouble()).toFloat()
    val t1 = (-b - sqRtDiscriminant) / (2 * a)
    val t2 = (-b + sqRtDiscriminant) / (2 * a)
    val i1 = Intersection(t1, this)
    val i2 = Intersection(t2, this)

    return if (i1 < i2) {
      Intersections().add(i1).add(i2)
    } else {
      Intersections().add(i2).add(i1)
    }
  }

  override fun localNormal(localPoint: Point): Vector {
    return localPoint - localCenter()
  }

  fun localCenter(): Point { return Point(0f, 0f, 0f)
  }
}