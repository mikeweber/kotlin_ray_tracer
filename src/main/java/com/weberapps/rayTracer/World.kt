package com.weberapps.rayTracer

class World(val sceneObjects: ArrayList<Shape> = arrayListOf(), val lightSources: ArrayList<Light> = arrayListOf(), private val background: Color = Color.BLACK) {
    companion object {
        fun default(): World {
            val s1 = Sphere(material = SolidColor(color = Color(0.8f, 1f, 0.6f), diffuse = 0.7f, specular = 0.2f))
            val s2 = Sphere(transform = Transformation.scale(0.5f, 0.5f, 0.5f))
            val light = Light(Point(-10f, 10f, -10f))

            return World(arrayListOf(s1, s2), arrayListOf(light))
        }
    }

    fun colorAt(ray: Ray, refractionsLeft: Int = 5): Color {
        if (refractionsLeft <= 0) return background
        val hit = intersect(ray).hit() ?: return background

        return shadeHit(hit.prepareHit(ray), refractionsLeft)
    }

    fun intersect(ray: Ray): Intersections {
        val intersections = Intersections()
        for (obj in sceneObjects) {
            intersections.add(obj.intersect(ray))
        }
        return intersections
    }

    fun shadeHit(hit: Intersection, refractionsLeft: Int = 5): Color {
        var color = Color.BLACK
        for (light in lightSources) {
            color += hit.colorAt(light, refractionsLeft, this) ?: background
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