package com.weberapps.rayTracer

class Sphere(override var transform: Matrix = Matrix.eye(4), override var material: Material = Material()) : Shape {
    override fun equals(other: Any?): Boolean {
        if (other !is Shape) return false

        return transform == other.transform && material == other.material
    }
}