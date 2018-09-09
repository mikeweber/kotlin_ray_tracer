package com.weberapps.rayTracer

class SolidColor(
        val color: Color = Color.WHITE,
        override val ambient: Float = 0.1f,
        override val diffuse: Float = 0.9f,
        override val specular: Float = 0.9f,
        override val shininess: Int = 200
) : Material {
    override fun lighting(hit: Intersection, light: Light, world: World?, inShadow: Boolean, refractionsLeft: Int, surfaceOffset: Float): Color? {
        val effectiveColor = color * light.intensity
        return calculateColor(effectiveColor, light, hit.point, hit.eyeVector, hit.normalVector, inShadow)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is SolidColor) return false

        return color == other.color &&
                attributeEquals(ambient, other.ambient) &&
                attributeEquals(diffuse, other.diffuse) &&
                attributeEquals(specular, other.specular) &&
                shininess == other.shininess
    }

}