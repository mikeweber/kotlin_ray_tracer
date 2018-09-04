package com.weberapps.rayTracer

class Point : Tuple {
    constructor(x: Float, y: Float, z: Float) : super(x, y, z, 1.0f)
    constructor(tuple: Tuple) : this(tuple.x, tuple.y, tuple.z)

    override operator fun times(scalar: Float): Point {
        return Point(x * scalar, y * scalar, z * scalar)
    }

    override operator fun div(scalar: Float): Point {
        return Point(x / scalar, y / scalar, z / scalar)
    }

    override operator fun unaryMinus(): Point {
        return Point(-x, -y, -z)
    }
}