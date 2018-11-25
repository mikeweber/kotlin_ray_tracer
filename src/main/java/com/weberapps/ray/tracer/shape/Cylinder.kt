package com.weberapps.ray.tracer.shape

import com.weberapps.ray.tracer.constants.EPSILON
import com.weberapps.ray.tracer.intersection.Intersection
import com.weberapps.ray.tracer.intersection.Intersections
import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.math.*
import java.lang.Float.NEGATIVE_INFINITY
import java.lang.Float.POSITIVE_INFINITY

class Cylinder(
  override var transform: Matrix = Matrix.eye(4),
  override var material: Material = Material(),
  var minimum: Float = NEGATIVE_INFINITY,
  var maximum: Float = POSITIVE_INFINITY,
  var closed: Boolean = false,
  override var parent: Shape? = null
) : Shape {
  override fun localIntersect(localRay: Ray): Intersections {
    return intersectCaps(localRay).add(intersectWalls(localRay))
  }

  private fun intersectWalls(localRay: Ray): Intersections {
    val a = localRay.direction.x * localRay.direction.x + localRay.direction.z * localRay.direction.z

    // Ray is parallel to Y axis; no intersections with the side walls
    if (almostZero(a)) return Intersections()

    val b = 2 * localRay.origin.x * localRay.direction.x + 2 * localRay.origin.z * localRay.direction.z
    val c = localRay.origin.x * localRay.origin.x + localRay.origin.z * localRay.origin.z - 1

    val disc = b * b - 4 * a * c

    // Ray misses; no intersections
    if (disc < 0) return Intersections()

    val root = floatRoot(disc)
    var t = Pair((-b - root) / (2 * a), (-b + root) / (2 * a))

    if (t.first > t.second) t = swap(t)
    return addIntersections(localRay, t)
  }

  private fun addIntersections(ray: Ray, t: Pair<Float, Float>): Intersections {
    var xs = Intersections()
    for (tx in t.toList()) {
      val y = ray.origin.y + tx * ray.direction.y

      if (minimum < y && y < maximum) xs = xs.add(tx, this)
    }
    return xs
  }

  private fun intersectCaps(ray: Ray): Intersections {
    if (!closed) return Intersections()

    return addCapIntersections(ray, maximum, addCapIntersections(ray, minimum))
  }

  private fun addCapIntersections(ray: Ray, y: Float, xs: Intersections = Intersections()): Intersections {
    val t = (y - ray.origin.y) / ray.direction.y
    if (!checkCap(ray, t)) return xs

    return xs.add(t, this)
  }

  private fun checkCap(ray: Ray, t: Float): Boolean {
    val x = ray.origin.x + t * ray.direction.x
    val z = ray.origin.z + t * ray.direction.z

    return (x * x + z * z) <= 1f
  }

  private fun swap(pair: Pair<Float, Float>): Pair<Float, Float> {
    return Pair(pair.second, pair.first)
  }

  override fun localNormal(localPoint: Point): Vector {
    val dist = localPoint.x * localPoint.x + localPoint.z * localPoint.z

    return if (dist < 1f && localPoint.y >= maximum - EPSILON) {
      Vector(0f, 1f, 0f)
    } else if (dist < 1f && localPoint.y <= minimum + EPSILON) {
      Vector(0f, -1f, 0f)
    } else {
      Vector(localPoint.x, 0f, localPoint.z)
    }
  }
}