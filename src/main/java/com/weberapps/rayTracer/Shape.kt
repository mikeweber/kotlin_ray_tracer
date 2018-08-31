package com.weberapps.rayTracer

interface Shape {
    var transform: Matrix

    fun intersect(ray: Ray): Intersections {
        val transformedRay = ray.transform(transform.inverse())
        val sphereToRay = transformedRay.origin - center()
        val a = transformedRay.direction.dot(transformedRay.direction)
        val b = 2 * transformedRay.direction.dot(sphereToRay)
        val c = sphereToRay.dot(sphereToRay) - 1

        val discriminant = b * b - 4 * a * c
        if (discriminant < 0) return Intersections()

        val sqRtDiscriminant = Math.sqrt(discriminant.toDouble()).toFloat()
        val t1 = (-b - sqRtDiscriminant) / (2 * a)
        val t2 = (-b + sqRtDiscriminant) / (2 * a)
        val i1 = Intersection(t1, this)
        val i2 = Intersection(t2, this)

        return if (i1 < i2) {
            Intersections().add(i1).add(i2)
        } else {
            Intersections().add(i2).add(i1)
        }
    }

    private fun center(): Point {
        return Point(0f, 0f, 0f)
    }
}