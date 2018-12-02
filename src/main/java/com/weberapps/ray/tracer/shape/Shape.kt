package com.weberapps.ray.tracer.shape

import com.weberapps.ray.tracer.intersection.Intersection
import com.weberapps.ray.tracer.intersection.Intersections
import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Vector

interface Shape {
  var transform: Matrix
  var material: Material
  var parent: Shape?

  fun intersect(ray: Ray): Intersections {
    return localIntersect(ray.transform(transform.inverse()))
  }

  fun localIntersect(localRay: Ray): Intersections

  fun normal(point: Point): Vector {
    return normalToWorld(localNormal(worldToObject(point)))
  }
  fun normal(point: Point, hit: Intersection): Vector {
    return normal(point)
  }

  fun localNormal(localPoint: Point): Vector
  fun localNormal(localPoint: Point, hit: Intersection): Vector {
    return localNormal(localPoint)
  }

  fun worldToObject(point: Point): Point {
    val localPoint = parent?.worldToObject(point) ?: point

    return Point(transform.inverse() * localPoint)
  }

  fun normalToWorld(vector: Vector): Vector {
    var normal = Vector(transform.inverse().transpose() * vector).normalize()

    return parent?.normalToWorld(normal) ?: normal
  }
}