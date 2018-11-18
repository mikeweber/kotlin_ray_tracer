package com.weberapps.rayTracer

class CheckerboardMaterial(
        override val color: Color = Color.BLACK,
        val otherColor: Color = Color.WHITE,
        val transform: Matrix = Matrix.eye(4),
        override val ambient: Float = 0.1f,
        override val diffuse: Float = 0.9f,
        override val specular: Float = 0.9f,
        override val shininess: Int = 200,
        override val reflective: Float = 0.1f
) : Material() {
    override fun lighting(hit: Intersection, light: Light, world: World?, inShadow: Boolean, refractionsLeft: Int, surfaceOffset: Float): Color {
        val effectiveColor= squareAtObject(hit.shape, hit.point) * light.intensity
        return calculateColor(effectiveColor, light, hit.point, hit.eyeVector, hit.normalVector, inShadow)
    }

    fun squareAtObject(shape: Shape, worldSpacePoint: Point): Color {
        val objectSpacePoint = shape.transform.inverse() * worldSpacePoint
        val patternSpacePoint = Point(transform.inverse() * objectSpacePoint)

        return squareAt(patternSpacePoint)
    }

    fun squareAt(point: Point): Color {
        val x = Math.floor(point.x.toDouble()).toInt()
        val y = Math.floor(point.z.toDouble()).toInt()

        return if (((x + y) % 2) == 0) color else otherColor
    }

    override fun equals(other: Any?): Boolean {
        if (other !is CheckerboardMaterial) return false

        return super.equals(other) && otherColor == other.otherColor && transform == other.transform
    }
}