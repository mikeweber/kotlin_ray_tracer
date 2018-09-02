package com.weberapps.rayTracer

import java.lang.Math.PI
import java.lang.Math.abs
import kotlin.math.sqrt

const val EPSILON: Float = 0.00001f
const val TAU: Double = (PI * 2)
open class Tuple(val x: Float, val y: Float, val z: Float, val w: Float) {
    operator fun plus(other: Tuple): Tuple {
        return Tuple(x + other.x, y + other.y, z + other.z, w + other.w)
    }

    operator fun minus(other: Tuple): Tuple {
        return Tuple(x - other.x, y - other.y, z - other.z, w - other.w)
    }

    operator fun times(scalar: Float): Tuple {
        return Tuple(x * scalar, y * scalar, z * scalar, w * scalar)
    }

    operator fun div(scalar: Float): Tuple {
        return Tuple(x / scalar, y / scalar, z / scalar, w / scalar)
    }

    operator fun unaryMinus(): Tuple {
        return Tuple(-x, -y, -z, -w)
    }

    fun dot(other: Tuple): Float {
        return x * other.x + y * other.y + z * other.z + w * other.w;
    }

    override operator fun equals(other: Any?): Boolean {
        if (other !is Tuple) return false

        return attributeEquals(x, other.x) && attributeEquals(y, other.y) && attributeEquals(z, other.z) && attributeEquals(w, other.w)
    }

    fun magnitude(): Float {
        return sqrt(x * x + y * y + z * z + w * w)
    }

    fun normalize(): Tuple {
        val mag = magnitude()
        return Tuple(
                x / mag,
                y / mag,
                z / mag,
                w / mag
        )
    }

    fun reflect(surfaceNormal: Vector): Vector {
        return Vector(this - surfaceNormal * 2f * this.dot(surfaceNormal))
    }

    private fun attributeEquals(a: Float, b: Float, eps: Float = EPSILON): Boolean {
        return abs(a - b) <= eps
    }
}