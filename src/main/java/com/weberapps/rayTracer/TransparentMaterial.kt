package com.weberapps.rayTracer


// For reference of refractiveIndices:
// vacuum: 1.0
// air: 1.00029
// water: 1.333
// glass: 1.52
// diamond: 2.417
class TransparentMaterial(color: Color = Color.WHITE, ambient: Float = 0f, diffuse: Float = 0f, specular: Float = 0f, shininess: Int = 0, val refractiveIndex: Float) : Material(color, ambient, diffuse, specular, shininess, 0f) {
  override fun surfaceColor(hit: Intersection, light: Light, world: World?, inShadow: Boolean, refractionsLeft: Int, surfaceOffset: Float): Color {
    val effectiveColor = color * light.intensity
    var reflectivity = fresnel(hit.rayVector, hit.normalVector)

    return refractionColor(effectiveColor, reflectivity, light, hit, world, inShadow, refractionsLeft, surfaceOffset)
  }

  private fun refractionColor(effectiveColor: Color, reflectivity: Float, light: Light, hit: Intersection, world: World?, inShadow: Boolean, refractionsLeft: Int, surfaceOffset: Float): Color {
    var refractionColor = calculateColor(effectiveColor, light, hit.point, hit.eyeVector, hit.normalVector, inShadow)
    if (world == null || reflectivity < 1f) return refractionColor

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

  private fun fresnel(angleOfIncidence: Vector, normal: Vector, externalRefractiveIndex: Float = 1f): Float {
    var cosi = clamp(-1f, angleOfIncidence.dot(normal), 1f)
    // refractiveIndex
    val eta = if (cosi > 0f) {
      refractiveIndex / externalRefractiveIndex
    } else {
      externalRefractiveIndex / refractiveIndex
    }
    val sint = eta * Math.sqrt(Math.max(0.0, (1 - cosi * cosi).toDouble())).toFloat()
    if (sint >= 1) return 1f
    val cost = Math.sqrt(Math.max(0.0, (1 - sint * sint).toDouble())).toFloat()
    cosi = Math.abs(cosi)
    val rs = ((refractiveIndex * cosi) - (externalRefractiveIndex * cost)) / ((refractiveIndex * cosi) + (externalRefractiveIndex * cost))
    val rp = ((externalRefractiveIndex * cosi) - (refractiveIndex * cost)) / ((externalRefractiveIndex * cosi) + (refractiveIndex * cost))

    return (rs * rs + rp * rp) / 2
  }
}