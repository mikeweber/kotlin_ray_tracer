package com.weberapps.rayTracer

class Vector : Tuple {
    constructor(x: Float, y: Float, z: Float) : super(x, y, z, 0f)
    constructor(tuple: Tuple) : this(tuple.x, tuple.y, tuple.z)

    operator fun plus(other: Vector): Vector {
        return Vector(x + other.x, y + other.y, z + other.z)
    }

    operator fun minus(other: Vector): Vector {
        return Vector(x - other.x, y - other.y, z - other.z)
    }

    override operator fun times(scalar: Float): Vector {
        return Vector(x * scalar, y * scalar, z * scalar)
    }

    override operator fun div(scalar: Float): Vector {
        return Vector(x / scalar, y / scalar, z / scalar)
    }

    override operator fun unaryMinus(): Vector {
        return Vector(-x, -y, -z)
    }

    fun cross(other: Vector): Vector {
        return Vector(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x
        )
    }

    override fun normalize(): Vector {
        val mag = magnitude()
        return Vector(
                x / mag,
                y / mag,
                z / mag
        )
    }
}
