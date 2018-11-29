package com.weberapps.ray.tracer

import com.weberapps.ray.tracer.intersection.Intersections
import com.weberapps.ray.tracer.material.IMaterial
import com.weberapps.ray.tracer.material.SolidColor
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.shape.Shape

class TestShape : Shape {
  override var transform: Matrix = Matrix.eye(4)
  override var material: IMaterial = SolidColor()
  override var parent: Shape? = null

  var savedRay: Ray =
    Ray(Point(0f, 0f, 0f), Vector(0f, 0f, 1f))

  override fun localIntersect(localRay: Ray): Intersections {
    savedRay = localRay
    return Intersections(0)
  }

  override fun localNormal(localPoint: Point): Vector {
    return Vector(localPoint.x, localPoint.y, localPoint.z)
  }
}