package com.weberapps.ray.tracer.math

import com.weberapps.ray.tracer.constants.EPSILON

interface ITuple {
  val x: Float
  val y: Float
  val z: Float
  val w: Float
  val magnitude get() = floatRoot(x * x + y * y + z * z + w * w)

  fun dot(other: ITuple)             = x * other.x + y * other.y + z * other.z + w * other.w

  fun normalize(): ITuple {
    val mag = magnitude
    return Tuple(
      x / mag,
      y / mag,
      z / mag,
      w / mag
    )
  }

  fun hasSameElements(other: ITuple): Boolean {
    return attributeEquals(x, other.x)
      && attributeEquals(y, other.y)
      && attributeEquals(z, other.z)
      && attributeEquals(w, other.w)
  }

  fun attributeEquals(a: Float, b: Float, eps: Float = EPSILON) = Math.abs(a - b) <= eps
}
