package com.weberapps.rayTracer

class TestShape : Shape {
    override var transform: Matrix = Matrix.eye(4)
    override var material: Material = Material()
    var savedRay: Ray = Ray(Point(0f, 0f, 0f), Vector(0f, 0f, 1f))

    override fun localIntersect(ray: Ray): Intersections {
        savedRay = ray
        return Intersections(0)
    }

    override fun localNormal(point: Point): Vector {
        return Vector(point.x, point.y, point.z)
    }
}