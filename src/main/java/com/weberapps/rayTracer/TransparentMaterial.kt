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

        val direction = refract(-hit.eyeVector, hit.normalVector, 1f) ?: return null
        val point = Point(hit.point - hit.normalVector * 2f * surfaceOffset)
        val nextRay = Ray(point, direction)
        return world.colorAt(nextRay, refractionsLeft - 1)
    }

    private fun refract(angleOfIncidence: Vector, normal: Vector, externalRefractiveIndex: Float = 1f): Vector? {
        var cosi = Math.max(-1f, Math.min(angleOfIncidence.dot(normal), 1f))
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