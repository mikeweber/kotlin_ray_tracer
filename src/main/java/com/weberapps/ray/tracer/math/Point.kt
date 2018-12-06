package com.weberapps.ray.tracer.math

class Point(override val x: Float, override val y: Float, override val z: Float, override val w: Float = 1f) : ITuple {
  constructor(tuple: ITuple) : this(tuple.x, tuple.y, tuple.z)

  operator fun times(scalar: Float) = Point(x * scalar, y * scalar, z * scalar)
  operator fun div(scalar: Float)   = Point(x / scalar, y / scalar, z / scalar)
  operator fun plus(other: Vector)  = Point(x + other.x, y + other.y, z + other.z)
  operator fun unaryMinus()         = Point(-x, -y, -z)
  operator fun minus(other: Vector) = Point(x - other.x, y - other.y, z - other.z)
  operator fun minus(other: Point)  = Vector(x - other.x, y - other.y, z - other.z)
  override fun toString()           = "Point($x, $y, $z)"

  override fun equals(other: Any?): Boolean {
    if (other !is ITuple) return false

    return hasSameElements(other)
  }
}