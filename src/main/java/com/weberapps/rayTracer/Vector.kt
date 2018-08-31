package com.weberapps.rayTracer

class Vector : Tuple {
    constructor(x: Float, y: Float, z: Float) : super(x, y, z, 0.0f)
    constructor(tuple: Tuple) : this(tuple.x, tuple.y, tuple.z)

    fun cross(other: Vector): Vector {
        return Vector(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x
        )
    }
}
