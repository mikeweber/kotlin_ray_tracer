package com.weberapps.ray.tracer

class TestShape : Shape {
  override var transform: Matrix = Matrix.eye(4)
  override var material: Material = Material()
  var savedRay: Ray = Ray(Point(0f, 0f, 0f), Vector(0f, 0f, 1f))

  override fun localIntersect(localRay: Ray): Intersections {
    savedRay = localRay
    return Intersections(0)
  }

  override fun localNormal(localPoint: Point): Vector {
    return Vector(localPoint.x, localPoint.y, localPoint.z)
  }
}