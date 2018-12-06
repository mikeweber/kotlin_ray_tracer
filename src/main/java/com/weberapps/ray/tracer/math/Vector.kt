package com.weberapps.ray.tracer.math

class Vector(override val x: Float = 0f, override val y: Float = 0f, override val z: Float = 0f, override val w: Float = 0f) : ITuple {
  constructor(tuple: ITuple) : this(tuple.x, tuple.y, tuple.z)

  operator fun plus(other: Vector)   = Vector(x + other.x, y + other.y, z + other.z)
  operator fun plus(other: Point)    = Point(x + other.x, y + other.y, z + other.z)
  operator fun minus(other: Vector)  = Vector(x - other.x, y - other.y, z - other.z)
  operator fun times(scalar: Float)  = Vector(x * scalar, y * scalar, z * scalar)
  operator fun div(scalar: Float)    = Vector(x / scalar, y / scalar, z / scalar)
  operator fun unaryMinus()          = Vector(-x, -y, -z)
  fun reflect(surfaceNormal: Vector) = Vector(this - surfaceNormal * 2f * this.dot(surfaceNormal))
  override fun toString()            = "Vector($x, $y, $z)"

  override fun equals(other: Any?): Boolean {
    if (other !is Vector) return false

    return hasSameElements(other)
  }

  fun cross(other: Vector): Vector {
    return Vector(
      y * other.z - z * other.y,
      z * other.x - x * other.z,
      x * other.y - y * other.x
    )
  }

  override fun normalize(): Vector {
    val mag = magnitude
    return Vector(
      x / mag,
      y / mag,
      z / mag
    )
  }
}
