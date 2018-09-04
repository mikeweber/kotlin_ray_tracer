package com.weberapps.rayTracer

class World(sceneObjects: Array<Shape>, lightSources: Array<Light>) {
    var sceneObjects: Array<Shape> = sceneObjects
    var lightSources: Array<Light> = lightSources

    constructor() : this(arrayOf(), arrayOf())

    companion object {
        fun defaultWorld(): World {
            val s1 = Sphere(material = Material(color = Color(0.8f, 1f, 0.6f), diffuse = 0.7f, specular = 0.2f))
            val s2 = Sphere(Transformation.scale(0.5f, 0.5f, 0.5f))
            val light = Light(Point(-10f, 10f,-10f), Color(1f, 1f, 1f))
            return World(arrayOf(s1, s2), arrayOf(light))
        }
    }

    fun intersect(ray: Ray): Intersections {
        var intersections = Intersections()
        for (obj in sceneObjects) {
            intersections.add(obj.intersect(ray))
        }

        return intersections
    }
}