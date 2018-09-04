package com.weberapps.rayTracer

class Color : Tuple {
    constructor(red: Float, green: Float, blue: Float): super(red, green, blue, 0.0f)
    constructor(red: Float, green: Float, blue: Float, alpha: Float): super(red, green, blue, alpha)

    val red get() = this.x

    val green get() = this.y

    val blue get() = this.z

    val alpha get() = this.w

    operator fun times(other: Color): Color {
        return Color(red * other.red, green * other.green, blue * other.blue)
    }

    override operator fun times(scalar: Float): Color {
        return Color(red * scalar, green * scalar, blue * scalar)
    }

    override operator fun plus(other: Color): Color {
        return Color(red + other.red, green + other.green, blue + other.blue)
    }
}