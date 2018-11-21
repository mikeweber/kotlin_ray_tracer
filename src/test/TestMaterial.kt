package com.weberapps.ray.tracer

import com.weberapps.ray.tracer.intersection.Intersection
import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.renderer.World

class TestMaterial(
  color: Color = Color.WHITE,
  ambient: Float         = 0.1f,
  diffuse: Float         = 0.9f,
  specular: Float        = 0.9f,
  shininess: Int         = 200,
  reflective: Float      = 0f,
  transparency: Float    = 0f,
  refractiveIndex: Float = VACUUM
) : Material(color, ambient, diffuse, specular, shininess, reflective, transparency, refractiveIndex) {
  override fun surfaceColor(hit: Intersection, light: Light, world: World?, inShadow: Boolean, refractionsLeft: Int, surfaceOffset: Float): Color {
    return Color(hit.point.x, hit.point.y, hit.point.z)
  }
}