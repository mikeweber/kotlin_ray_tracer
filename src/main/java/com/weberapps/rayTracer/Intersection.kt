package com.weberapps.rayTracer

class Intersection(val t: Float, val shape: Shape) {
    override fun equals(other: Any?): Boolean {
        if (other !is Intersection) return false

        return shape == other.shape && closeEnough(t, other.t)
    }

    operator fun compareTo(other: Any?): Int {
        if (other !is Intersection) return 0

        if (closeEnough(t, other.t)) return 0
        return if (t < other.t) -1 else 1
    }

    private fun closeEnough(f1: Float, f2: Float, eps: Float = EPSILON): Boolean {
        return (Math.abs(f1 - f2) < eps)
    }
}