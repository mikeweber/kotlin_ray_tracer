package com.weberapps.rayTracer

import kotlin.math.abs

class Plane(override var transform: Matrix = Matrix.eye(4), override var material: Material = Material()) : Shape {
    override fun localIntersect(ray: Ray): Intersections {
        if (abs(ray.direction.y) < EPSILON) return Intersections(0)

        val t = -ray.origin.y / ray.direction.y
        return Intersections().add(Intersection(t, this))
    }

    override fun localNormal(point: Point): Vector {
        return Vector(0f, 1f, 0f)
    }
}