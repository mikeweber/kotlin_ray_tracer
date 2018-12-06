package com.weberapps.ray.tracer.math

class Color(override val x: Float = 0f, override val y: Float = 0f, override val z: Float = 0f, override val w: Float = 1f) : IColor {
  companion object {
    val BLACK = Color(0f, 0f, 0f)
    val WHITE = Color(1f, 1f, 1f)
    val RED   = Color(1f, 0f, 0f)
    val GREEN = Color(0f, 1f, 0f)
    val BLUE  = Color(0f, 0f, 1f)
  }
  override fun toString() = "Color(${colorDescription()})"

  override fun equals(other: Any?): Boolean {
    if (other !is Color) return false

    return hasSameElements(other)
  }
}