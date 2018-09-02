package com.weberapps.rayTracer

class Sphere(override var transform: Matrix = Matrix.eye(4)) : Shape {
    fun normal(point: Point): Vector {
        val objectPoint = transform.inverse() * point
        val objectNormal = objectPoint- center()
        val worldNormal = transform.inverse().transpose() * objectNormal

        return Vector(worldNormal.normalize())
    }
}