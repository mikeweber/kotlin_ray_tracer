package com.weberapps.ray.tracer.math

class Color : Tuple {
  constructor(red: Float, green: Float, blue: Float): super(red, green, blue, 0.0f)
  constructor(red: Float, green: Float, blue: Float, alpha: Float): super(red, green, blue, alpha)

  val red get() = this.x

  val green get() = this.y

  val blue get() = this.z

  val alpha get() = this.w

  companion object {
    val BLACK = Color(0f, 0f, 0f)
    val WHITE = Color(1f, 1f, 1f)
    val RED   = Color(1f, 0f, 0f)
    val GREEN = Color(0f, 1f, 0f)
    val BLUE  = Color(0f, 0f, 1f)
  }

  override fun toString(): String {
    val description = when (this) {
      BLACK -> "BLACK"
      WHITE -> "WHITE"
      RED   -> "RED"
      GREEN -> "GREEN"
      BLUE  -> "BLUE"
      else  -> "$red, $green, $blue"
    }

    return "Color($description)"
  }

  operator fun times(other: Color): Color {
    return Color(red * other.red, green * other.green, blue * other.blue)
  }

  override operator fun times(scalar: Float): Color {
    return Color(red * scalar, green * scalar, blue * scalar)
  }

  operator fun plus(other: Color): Color {
    return Color(red + other.red, green + other.green, blue + other.blue)
  }

  override operator fun div(scalar: Float): Color {
    return Color(red / scalar, green / scalar, blue / scalar)
  }
}