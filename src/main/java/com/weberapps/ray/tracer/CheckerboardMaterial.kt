package com.weberapps.ray.tracer

class CheckerboardMaterial(
        override val color: com.weberapps.ray.tracer.Color = com.weberapps.ray.tracer.Color.Companion.BLACK,
        val otherColor: com.weberapps.ray.tracer.Color = com.weberapps.ray.tracer.Color.Companion.WHITE,
        val transform: com.weberapps.ray.tracer.Matrix = com.weberapps.ray.tracer.Matrix.Companion.eye(4),
        override val ambient: Float = 0.1f,
        override val diffuse: Float = 0.9f,
        override val specular: Float = 0.9f,
        override val shininess: Int = 200,
        override val reflective: Float = 0.1f
) : com.weberapps.ray.tracer.Material() {
    override fun surfaceColor(hit: com.weberapps.ray.tracer.Intersection, light: com.weberapps.ray.tracer.Light, world: com.weberapps.ray.tracer.World?, inShadow: Boolean, refractionsLeft: Int, surfaceOffset: Float): com.weberapps.ray.tracer.Color {
        val effectiveColor= squareAtObject(hit.shape, hit.point) * light.intensity
        return calculateColor(effectiveColor, light, hit.point, hit.eyeVector, hit.normalVector, inShadow)
    }

    fun squareAtObject(shape: com.weberapps.ray.tracer.Shape, worldSpacePoint: com.weberapps.ray.tracer.Point): com.weberapps.ray.tracer.Color {
        val objectSpacePoint = shape.transform.inverse() * worldSpacePoint
        val patternSpacePoint = com.weberapps.ray.tracer.Point(transform.inverse() * objectSpacePoint)

        return squareAt(patternSpacePoint)
    }

    fun squareAt(point: com.weberapps.ray.tracer.Point): com.weberapps.ray.tracer.Color {
        val x = Math.floor(point.x.toDouble()).toInt()
        val y = Math.floor(point.z.toDouble()).toInt()

        return if (((x + y) % 2) == 0) color else otherColor
    }

    override fun equals(other: Any?): Boolean {
        if (other !is com.weberapps.ray.tracer.CheckerboardMaterial) return false

        return super.equals(other) && otherColor == other.otherColor && transform == other.transform
    }
}