package com.weberapps.rayTracer


// For reference of refractiveIndices:
// vacuum: 1.0
// air: 1.00029
// water: 1.333
// glass: 1.52
// diamond: 2.417
class TransparentMaterial(color: Color = Color.WHITE, ambient: Float = 0f, diffuse: Float = 0f, specular: Float = 0f, shininess: Int = 0, refractiveIndex: Float, transparency: Float = 1f) : Material(color, ambient, diffuse, specular, shininess, 0f, transparency, refractiveIndex) {
  override fun surfaceColor(hit: Intersection, light: Light, world: World?, inShadow: Boolean, refractionsLeft: Int, surfaceOffset: Float): Color {
    val effectiveColor = color * light.intensity
    var reflectivity = hit.schlick()

    return refractedColor(effectiveColor, reflectivity, light, hit, world, inShadow, refractionsLeft, surfaceOffset)
  }

  private fun refractedColor(effectiveColor: Color, reflectivity: Float, light: Light, hit: Intersection, world: World?, inShadow: Boolean, refractionsLeft: Int, surfaceOffset: Float): Color {
    var refractedColor = calculateColor(effectiveColor, light, hit.point, hit.eyeVector, hit.normalVector, inShadow)
    if (world == null || reflectivity < 1f) return refractedColor

    val direction = refract(hit.rayVector, hit.normalVector, 1f) ?: return Color.BLACK
    val point = Point(hit.point - hit.normalVector * 2f * surfaceOffset)
    val nextRay = Ray(point, direction)
    return world.colorAt(nextRay, refractionsLeft - 1)
  }

  private fun refract(angleOfIncidence: Vector, normal: Vector, externalRefractiveIndex: Float = 1f): Vector? {
    var cosi = clamp(-1f, angleOfIncidence.dot(normal), 1f)
    var n = normal
    val eta = if (cosi < 0f) {
      cosi = -cosi
      externalRefractiveIndex / refractiveIndex
    } else {
      n = -normal
      refractiveIndex / externalRefractiveIndex
    }
    val k = 1 - eta * eta * (1 - cosi * cosi)
    // if k < 0, total internal reflection; no refraction
    if (k < 0) return null

    return angleOfIncidence * eta + n * (eta * cosi - Math.sqrt(k.toDouble()).toFloat())
  }
}