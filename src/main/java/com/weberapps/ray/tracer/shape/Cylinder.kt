package com.weberapps.ray.tracer.shape

import com.weberapps.ray.tracer.constants.EPSILON
import com.weberapps.ray.tracer.intersection.Intersection
import com.weberapps.ray.tracer.intersection.Intersections
import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.math.floatRoot

class Cylinder(override var transform: Matrix = Matrix.eye(4), override var material: Material = Material()) : Shape {
  override fun localIntersect(localRay: Ray): Intersections {
    val a = localRay.direction.x * localRay.direction.x + localRay.direction.z * localRay.direction.z

    // Ray is parallel to Y axis; no intersections
    if (Math.abs(a) < EPSILON) return Intersections()

    val b = 2 * localRay.origin.x * localRay.direction.x + 2 * localRay.origin.z * localRay.direction.z
    val c = localRay.origin.x * localRay.origin.x + localRay.origin.z * localRay.origin.z - 1

    val disc = b * b - 4 * a * c

    // Ray misses; no intersections
    if (disc < 0) return Intersections()

    val root = floatRoot(disc)
    val t0 = (-b - root) / (2 * a)
    val t1 = (-b + root) / (2 * a)

    return Intersections(Intersection(t0, this), Intersection(t1, this))
  }

  override fun localNormal(localPoint: Point): Vector {
    return Vector(localPoint.x, 0f, localPoint.z)
  }
}