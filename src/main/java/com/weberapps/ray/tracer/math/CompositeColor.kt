package com.weberapps.ray.tracer.math

class CompositeColor(private vararg val colors: Color): IColor {
  override val x get() = color.x
  override val y get() = color.y
  override val z get() = color.z
  override val w get() = color.w
  val color: Color
    get() {
      if (colors.isEmpty()) return Color.BLACK

      return Color(
        colors.map { it.x }.sum() / size,
        colors.map { it.y }.sum() / size,
        colors.map { it.z }.sum() / size
      )
    }
  val size: Int get() = colors.size

  override fun equals(other: Any?): Boolean {
    if (other !is IColor) return false

    return attributeEquals(red, other.red)
      && attributeEquals(green, other.green)
      && attributeEquals(blue, other.blue)
  }

  override fun toString(): String {
    return "CompositeColor[$size](${colorDescription()})"
  }
}