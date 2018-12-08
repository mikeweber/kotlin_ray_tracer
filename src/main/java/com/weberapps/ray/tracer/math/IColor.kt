package com.weberapps.ray.tracer.math

interface IColor: ITuple {
  val red get()   = this.x
  val green get() = this.y
  val blue get()  = this.z

  fun colorDescription(): String {
    return when (this) {
      Color.BLACK -> "BLACK"
      Color.WHITE -> "WHITE"
      Color.RED   -> "RED"
      Color.GREEN -> "GREEN"
      Color.BLUE  -> "BLUE"
      else -> "$red, $green, $blue"
    }
  }

  fun colorsSize(): Int = 1

  fun sumRed(): Float {
    return red
  }

  fun sumGreen(): Float {
    return green
  }

  fun sumBlue(): Float {
    return blue
  }

  operator fun times(other: Color): Color {
    return Color(red * other.red, green * other.green, blue * other.blue)
  }

  operator fun times(scalar: Float): Color {
    return Color(red * scalar, green * scalar, blue * scalar)
  }

  operator fun plus(other: Color): Color {
    return Color(red + other.red, green + other.green, blue + other.blue)
  }

  operator fun minus(other: Color): Color {
    return Color(red - other.red, green - other.green, blue - other.blue)
  }

  operator fun div(scalar: Float): Color {
    return Color(red / scalar, green / scalar, blue / scalar)
  }
}
