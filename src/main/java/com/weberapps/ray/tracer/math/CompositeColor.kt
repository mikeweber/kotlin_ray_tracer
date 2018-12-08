package com.weberapps.ray.tracer.math

class CompositeColor(private vararg val colors: IColor): IColor {

  override val x get() = color.x
  override val y get() = color.y
  override val z get() = color.z
  override val w get() = color.w
  val color: Color
    get() {
      if (colors.isEmpty()) return Color.BLACK

      val length = colorsSize()

      return Color(
        sumRed()   / length,
        sumGreen() / length,
        sumBlue()  / length
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

  override fun colorsSize(): Int {
    return colors.map { it.colorsSize() }.sum()
  }

  override fun sumRed(): Float {
    return colors.map { it.sumRed() }.sum()
  }

  override fun sumGreen(): Float {
    return colors.map { it.sumGreen() }.sum()
  }

  override fun sumBlue(): Float {
    return colors.map { it.sumBlue() }.sum()
  }
}
