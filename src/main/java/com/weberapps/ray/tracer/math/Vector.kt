package com.weberapps.ray.tracer.math

import com.weberapps.ray.tracer.constants.TAU

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

  fun random(roughness: Float): Vector {
    val zRotation = TAU / 60 * randDistribution(8)
    val yRotation = TAU * Math.random()
    val randomVector = rotateFromUp(zRotation, yRotation)

    return randomVector.rotateTo(this)
  }

  // https://math.stackexchange.com/questions/180418/calculate-rotation-matrix-to-align-vector-a-to-vector-b-in-3d
  fun rotateTo(target: Vector): Vector {
    val a = Vector(0f, 1f, 0f)
    val b = target.normalize()
    val v = a.cross(b)
    val s = v.magnitude
    val c = a.dot(b)

    // Calculate the rotation to rotate the straight up vector to this vector,
    // then apply that same rotation to the random vector
    val skew = Matrix(4, 4)
    skew.setElement(0, 1, -v.z)
    skew.setElement(0, 2, -v.y)
    skew.setElement(1, 0,  v.z)
    skew.setElement(1, 2, -v.x)
    skew.setElement(2, 0, -v.y)
    skew.setElement(2, 1,  v.x)
    val rotation = Matrix.eye(4) + (skew + ((skew * skew) * ((1 - c) / (s * s))))


    return Vector(rotation * this).normalize()
  }

  fun rotateFromUp(zRotation: Double, yRotation: Double): Vector {
    return Vector(Transformation.rotateY(yRotation) * Transformation.rotateZ(zRotation) * Vector(0f, 1f, 0f)).normalize()
  }

  private fun randDistribution(times: Int): Float {
    var x = 0.0
    for (i in 0..times) {
      x += Math.random() * 2
    }
    return (x / times - 0.5).toFloat()
  }
}
