package com.weberapps.rayTracer

class World(val sceneObjects: ArrayList<Shape> = arrayListOf(), val lightSources: ArrayList<Light> = arrayListOf()) {
    companion object {
        fun default(): World {
            val s1 = Sphere(material = Material(color = Color(0.8f, 1f, 0.6f), diffuse = 0.7f, specular = 0.2f))
            val s2 = Sphere(transform = Transformation.scale(0.5f, 0.5f, 0.5f))
            val light = Light(Point(-10f, 10f, -10f))

            return World(arrayListOf(s1, s2), arrayListOf(light))
        }
    }

    fun colorAt(ray: Ray): Color {
        val intersections = intersect(ray)
        val hit = intersections.hit() ?: return Color.BLACK

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
        var color = Color.BLACK
        for (light in lightSources) {
            color += hit.shape.material.lighting(light, hit.point, hit.eyeVector, hit.normalVector, isShadowed(light, hit.point))
        }
        return color
    }

    fun isShadowed(light: Light, point: Point): Boolean {
        val v = light.position - point
        val dist = v.magnitude()
        val dir = v.normalize()

        val ray = Ray(point, dir)
        val hit = intersect(ray).hit() ?: return false

        return hit.t < dist
    }
}