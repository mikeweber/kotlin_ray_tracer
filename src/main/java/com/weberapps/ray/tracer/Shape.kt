package com.weberapps.ray.tracer

interface Shape {
  var transform: Matrix
  var material: Material

  fun intersect(ray: Ray): Intersections {
    return localIntersect(ray.transform(transform.inverse()))
  }

  fun localIntersect(localRay: Ray): Intersections

  fun normal(point: Point): Vector {
    val localPoint  = Point(transform.inverse() * point)
    val localNormal = localNormal(localPoint)
    val worldNormal  = Vector(transform.inverse().transpose() * localNormal)

    return worldNormal.normalize()
  }

  fun localNormal(localPoint: Point): Vector
}