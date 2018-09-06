package com.weberapps.rayTracer

class StripePattern(
        val zig: Color = Color.WHITE,
        val zag: Color = Color.BLACK,
        override val ambient: Float = 0.1f,
        override val diffuse: Float = 0.9f,
        override val specular: Float = 0.9f,
        override val shininess: Int = 200
): Material {
    override fun lighting(light: Light, position: Point, eyeVector: Vector, normalVector: Vector, inShadow: Boolean): Color {
        val effectiveColor= stripeAt(position) * light.intensity
        return calculateColor(effectiveColor, light, position, eyeVector, normalVector, inShadow)
    }

    fun stripeAt(point: Point): Color {
        return if ((Math.floor(point.x.toDouble()).toInt() % 2) == 0) zig else zag
    }

    override fun equals(other: Any?): Boolean {
        if (other !is StripePattern) return false

        return zig == other.zig &&
                zag == other.zag &&
                attributeEquals(ambient, other.ambient) &&
                attributeEquals(diffuse, other.diffuse) &&
                attributeEquals(specular, other.specular) &&
                shininess == other.shininess
    }
}