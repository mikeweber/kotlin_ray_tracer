package com.weberapps.ray.tracer.math

class Tuple(override val x: Float, override val y: Float, override val z: Float, override val w: Float): ITuple {
  operator fun plus(other: ITuple)  = Tuple(x + other.x, y + other.y, z + other.z, w + other.w)
  operator fun minus(other: ITuple) = Tuple(x - other.x, y - other.y, z - other.z, w - other.w)
  operator fun times(other: ITuple) = Tuple(x * other.x, y * other.y, z * other.z, w * other.w)
  operator fun times(scalar: Float) = Tuple(x * scalar, y * scalar, z * scalar, w * scalar)
  operator fun div(scalar: Float)   = Tuple(x / scalar, y / scalar, z / scalar, w / scalar)
  operator fun unaryMinus()         = Tuple(-x, -y, -z, -w)
  override fun toString()           = "Tuple($x, $y, $z, $w)"

  override fun equals(other: Any?): Boolean {
    if (other !is Tuple) return false

    return attributeEquals(x, other.x) && attributeEquals(y, other.y) && attributeEquals(z, other.z) && attributeEquals(w, other.w)
  }
}