package com.weberapps.rayTracer


// For reference of refractiveIndices:
// air: 1.0
// water: 1.3
// glass: 1.5
// diamond: 1.8
class TransparentMaterial(color: Color = Color.WHITE, ambient: Float = 0f, diffuse: Float = 0f, specular: Float = 0f, shininess: Int = 0, reflective: Float = 1f, val refractiveIndex: Float) : Material(color, ambient, diffuse, specular, shininess, reflective) {
    override fun lighting(hit: Intersection, light: Light, world: World?, inShadow: Boolean, refractionsLeft: Int, surfaceOffset: Float): Color {
        val effectiveColor = color * light.intensity
        var refractionColor = calculateColor(effectiveColor, light, hit.point, hit.eyeVector, hit.normalVector, inShadow)
        var reflectivity = fresnel(hit.rayVector, hit.normalVector) * reflective
        if (world != null && reflectivity < 1f) {
            val direction = refract(hit.rayVector, hit.normalVector, 1f) ?: return Color.BLACK
            val point = Point(hit.point - hit.normalVector * 2f * surfaceOffset)
            val nextRay = Ray(point, direction)
            refractionColor = world.colorAt(nextRay, refractionsLeft - 1)
        }

        return reflectedColor(hit, world, refractionsLeft) * reflective + refractionColor * (1f - reflectivity)
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