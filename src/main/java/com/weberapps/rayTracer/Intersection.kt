package com.weberapps.rayTracer

class Intersection(val t: Float, val shape: Shape, var inside: Boolean = false, var point: Point = Point(0f, 0f, 0f), var eyeVector: Vector = Vector(0f, 0f, 1f), var normalVector: Vector = Vector(0f, 0f,1f)): Comparable<Intersection> {
    override fun equals(other: Any?): Boolean {
        if (other !is Intersection) return false

        return shape == other.shape && closeEnough(t, other.t)
    }

    override operator fun compareTo(other: Intersection): Int {
        if (closeEnough(t, other.t)) return 0
        return if (t < other.t) -1 else 1
    }

    fun prepareHit(ray: Ray, surfaceOffset: Float = 0.0001f): Intersection {
        point = ray.positionAt(t)
        eyeVector = -ray.direction
        normalVector = shape.normal(point)
        inside = normalVector.dot(eyeVector) < 0f
        if (inside) normalVector = -normalVector
        point = Point(point + normalVector * surfaceOffset)

        return this
    }

    private fun closeEnough(f1: Float, f2: Float, eps: Float = EPSILON): Boolean {
        return (Math.abs(f1 - f2) < eps)
    }
}