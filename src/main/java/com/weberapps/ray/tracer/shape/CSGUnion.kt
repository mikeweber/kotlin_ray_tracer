package com.weberapps.ray.tracer.shape

import com.weberapps.ray.tracer.math.Matrix

class CSGUnion(
  override var left: Shape,
  override var right: Shape,
  override val shapes: ArrayList<Shape> = arrayListOf(),
  override var transform: Matrix = Matrix.eye(4),
  override var parent: Shape? = null
): CSGOperation {
  constructor(shapes: List<Shape>): this(shapes[0], shapes.subList(1, shapes.size))
  constructor(left: Shape, shapes: List<Shape>): this(left, shapes[0]) {
    right = if (shapes.size > 1) {
      CSGUnion(shapes)
    } else {
      shapes[0]
    }
  }
  init {
    left.parent = this
    right.parent = this
  }

  override fun intersectionAllowed(leftHit: Boolean, inLeft: Boolean, inRight: Boolean): Boolean {
    return (leftHit && !inRight) || (!leftHit && !inLeft)
  }
}
