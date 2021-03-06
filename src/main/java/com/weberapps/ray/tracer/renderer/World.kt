package com.weberapps.ray.tracer.renderer

import com.weberapps.ray.tracer.intersection.Intersection
import com.weberapps.ray.tracer.intersection.Intersections
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.material.SolidColor
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.shape.Shape
import com.weberapps.ray.tracer.shape.Sphere

open class World(val sceneObjects: ArrayList<Shape> = arrayListOf(), val lightSources: ArrayList<Light> = arrayListOf(), private val background: Color = Color.BLACK) {
  companion object {
    fun default(): World {
      val s1 = Sphere(
        material = SolidColor(
          color = Color(0.8f, 1f, 0.6f),
          diffuse = 0.7f,
          specular = 0.2f
        )
      )
      val s2 = Sphere(transform = Transformation.scale(0.5f, 0.5f, 0.5f))
      val light = Light(Point(-10f, 10f, -10f))

      return World(arrayListOf(s1, s2), arrayListOf(light))
    }
  }

  fun add(shape: Shape): World {
    sceneObjects.add(shape)
    return this
  }

  fun addLightSource(light: Light): World {
    lightSources.add(light)
    return this
  }

  fun colorAt(ray: Ray, refractionsLeft: Int = 5): Color {
    if (refractionsLeft <= 0) return background
    val intersections = intersect(ray)
    val hit = intersections.hit() ?: return background
    val preparedHit = hit.prepareHit(ray, intersections)

    return shadeHit(preparedHit, refractionsLeft)
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
}
