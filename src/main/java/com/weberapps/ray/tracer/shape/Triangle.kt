package com.weberapps.ray.tracer.shape

import com.weberapps.ray.tracer.constants.EPSILON
import com.weberapps.ray.tracer.intersection.Intersection
import com.weberapps.ray.tracer.intersection.Intersections
import com.weberapps.ray.tracer.material.IMaterial
import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Vector

class Triangle(val p1: Point, val p2: Point, val p3: Point, override var transform: Matrix = Matrix.eye(4), override var material: IMaterial = Material(), override var parent: Shape? = null): Shape {
  var e1: Vector = p2 - p1
  var e2: Vector = p3 - p1
  var normal: Vector = e2.cross(e1).normalize()

  override fun localIntersect(localRay: Ray): Intersections {
    val dirCrossE2 = localRay.direction.cross(e2)
    val det = e1.dot(dirCrossE2)
    if (Math.abs(det) < EPSILON) return Intersections()

    // Check p1-p3 boundary
    val f = 1f / det
    val p1ToOrigin = localRay.origin - p1
    val u = f * p1ToOrigin.dot(dirCrossE2)
    if (u < 0 || 1 < u) return Intersections()

    // Check p1-p2 and p2-p3 boundaries
    val originCrossE1 = p1ToOrigin.cross(e1)
    val v = f * localRay.direction.dot(originCrossE1)
    if (v < 0 || 1 < (u + v)) return Intersections()

    return Intersections(Intersection(f * e2.dot(originCrossE1), this))
  }

  override fun localNormal(localPoint: Point): Vector {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}