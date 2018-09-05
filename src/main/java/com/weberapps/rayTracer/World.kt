package com.weberapps.rayTracer

class World(val sceneObjects: ArrayList<Shape> = arrayListOf(), val lightSources: ArrayList<Light> = arrayListOf()) {
    companion object {
        fun default(): World {
            val s1 = Sphere(material = Material(color = Color(0.8f, 1f, 0.6f), diffuse = 0.7f, specular = 0.2f))
            val s2 = Sphere(transform = Transformation.scale(0.5f, 0.5f, 0.5f))
            val light = Light(Point(-10f, 10f, -10f), Color(1f, 1f, 1f))

            return World(arrayListOf(s1, s2), arrayListOf(light))
        }
    }

    fun colorAt(ray: Ray): Color {
        val intersections = intersect(ray)
        val hit = intersections.hit() ?: return Color(0f, 0f, 0f)

        return shadeHit(hit.prepareHit(ray))
    }

    fun intersect(ray: Ray): Intersections {
        val intersections = Intersections()
        for (obj in sceneObjects) {
            intersections.add(obj.intersect(ray))
        }
        return intersections
    }

    fun shadeHit(hit: Intersection): Color {
        var color = Color(0f, 0f, 0f)
        for (light in lightSources) {
            color += hit.shape.material.lighting(light, hit.point, hit.eyeVector, hit.normalVector)
        }
        return color
    }
}