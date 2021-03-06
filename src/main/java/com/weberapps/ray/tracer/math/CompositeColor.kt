package com.weberapps.ray.tracer.math

import kotlinx.coroutines.Deferred

class CompositeColor(vararg colors: IColor): IColor {
  init {
    for (color in colors) {
      add(color)
    }
  }
  private val colors = ArrayList<IColor>()
  private val asyncColors = ArrayList<Deferred<IColor>>()
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

  fun add(color: IColor): CompositeColor {
    if(color is CompositeColor) {
      for (c in color.colors) {
        add(c)
      }
    } else {
      colors.add(color)
    }
    return this
  }

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

  fun addAsyncColor(dc: Deferred<IColor>): CompositeColor {
    asyncColors.add(dc)
    return this
  }

  suspend fun resolveColors() {
    for (c in asyncColors) {
      val finalColor = c.await()
      colors.add(finalColor)
    }
  }
}
