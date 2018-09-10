package com.weberapps.rayTracer

// For reference of refractiveIndices:
// air: 1.0
// water: 1.3
// glass: 1.5
// diamond: 1.8
open class TransparentMaterial(
        private val refractiveIndex: Float = 1f,
        override val ambient: Float        = 0f,
        override val diffuse: Float        = 0f,
        override val specular: Float       = 0f,
        override val shininess: Int        = 0
) : Material {
    override fun lighting(hit: Intersection, light: Light, world: World?, inShadow: Boolean, refractionsLeft: Int, surfaceOffset: Float): Color? {
        if (world == null) return null

        val kr = fresnel(-hit.eyeVector, hit.normalVector)
        var refractionColor = Color.BLACK
        if (kr < 1f) {
            val direction = refract(-hit.eyeVector, hit.normalVector, 1f) ?: return null
            val point = Point(hit.point - hit.normalVector * 2f * surfaceOffset)
            val nextRay = Ray(point, direction)
            refractionColor = world.colorAt(nextRay, refractionsLeft - 1)
        }

        val reflectionDirection = reflect(-hit.eyeVector, hit.normalVector).normalize()
        val reflectionPoint = hit.point + hit.normalVector * surfaceOffset
        val reflectionColor = world.colorAt(Ray(reflectionPoint, reflectionDirection), refractionsLeft - 1)

        return reflectionColor * kr + refractionColor * (1f - kr)
    }

    private fun reflect(angleOfIncidence: Vector, normal: Vector): Vector {
        return angleOfIncidence - Vector(normal * angleOfIncidence.dot(normal) ) * 2f
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

    open protected fun fresnel(angleOfIncidence: Vector, normal: Vector, externalRefractiveIndex: Float = 1f): Float {
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

    private fun clamp(min: Float, value: Float, max: Float): Float {
        return Math.max(min, Math.min(value, max))
    }
}