package com.weberapps.rayTracer

class Intersection(val t: Float, val shape: Shape): Comparable<Intersection> {
    override fun equals(other: Any?): Boolean {
        if (other !is Intersection) return false

        return shape == other.shape && closeEnough(t, other.t)
    }

    override operator fun compareTo(other: Intersection): Int {
        if (other !is Intersection) return 0

        if (closeEnough(t, other.t)) return 0
        return if (t < other.t) -1 else 1
    }

    private fun closeEnough(f1: Float, f2: Float, eps: Float = EPSILON): Boolean {
        return (Math.abs(f1 - f2) < eps)
    }
}