package com.weberapps.ray.tracer.shape

import com.weberapps.ray.tracer.math.Matrix

open class Group(
  override var transform: Matrix = Matrix.eye(4),
  override val shapes: ArrayList<Shape> = arrayListOf(),
  override var parent: Shape? = null
): IGroup {
  fun add(shape: Shape): Group {
    shape.parent = this
    shapes.add(shape)
    return this
  }

  override fun equals(other: Any?): Boolean{
    if (other !is Group) return false

    return super.equals(other) && shapes == other.shapes
  }
}
