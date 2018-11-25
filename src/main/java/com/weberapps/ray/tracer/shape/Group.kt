package com.weberapps.ray.tracer.shape

import com.weberapps.ray.tracer.intersection.Intersections
import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Vector

class Group(
  override var transform: Matrix = Matrix.eye(4),
  val shapes: ArrayList<Shape> = arrayListOf(),
  override var material: Material = Material(),
  override var parent: Shape? = null
) :Shape {
  override fun localIntersect(localRay: Ray): Intersections {
    return shapes.fold(Intersections()) { xs, shape -> xs.add(shape.intersect(localRay)) }
  }

  override fun localNormal(localPoint: Point): Vector {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  fun add(shape: Shape): Group {
    shape.parent = this
    shapes.add(shape)
    return this
  }
}