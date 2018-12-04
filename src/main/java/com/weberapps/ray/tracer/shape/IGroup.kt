package com.weberapps.ray.tracer.shape

import com.weberapps.ray.tracer.intersection.Intersections
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Vector

interface IGroup: Shape {
  val shapes: ArrayList<Shape>

  override fun localIntersect(localRay: Ray): Intersections {
    return shapes.fold(Intersections()) { xs, shape -> xs.add(shape.intersect(localRay)) }
  }

  override fun localNormal(localPoint: Point): Vector {
    TODO("Groups don't have local normals")
  }

  override fun includes(shape: Shape): Boolean {
    return equals(shape) || shapes.any { other -> other.includes(shape) }
  }
}
