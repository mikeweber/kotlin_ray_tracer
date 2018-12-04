package com.weberapps.ray.tracer.shape

import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.material.SolidColor
import com.weberapps.ray.tracer.math.Matrix

class CSGUnion(
  override var left: Shape,
  override var right: Shape,
  override val shapes: ArrayList<Shape> = arrayListOf(),
  override var transform: Matrix = Matrix.eye(4),
  override var parent: Shape? = null
): CSGOperation {
  init {
    left.parent = this
    right.parent = this
  }

  override fun intersectionAllowed(leftHit: Boolean, inLeft: Boolean, inRight: Boolean): Boolean {
    return (leftHit && !inRight) || (!leftHit && !inLeft)
  }
}
