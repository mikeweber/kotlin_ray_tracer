package com.weberapps.rayTracer

class CheckeredPattern(
  override val color: Color = Color.WHITE,
  val tock: Color = Color.BLACK,
  val transform: Matrix = Matrix.eye(4),
  override val ambient: Float = 0.1f,
  override val diffuse: Float = 0.9f,
  override val specular: Float = 0.9f,
  override val shininess: Int = 200,
  override val reflective: Float = 0.1f
): Material(color, ambient, diffuse, specular, shininess, reflective) {
  val tick get() = color

  override fun surfaceColor(hit: Intersection, light: Light, world: World?, inShadow: Boolean, refractionsLeft: Int, surfaceOffset: Float): Color {
    val effectiveColor= squareColorAtObject(hit.shape, hit.point) * light.intensity
    return calculateColor(effectiveColor, light, hit.point, hit.eyeVector, hit.normalVector, inShadow)
  }

  fun squareColorAtObject(shape: Shape, worldSpacePoint: Point): Color {
    val objectSpacePoint = shape.transform.inverse() * worldSpacePoint
    val patternSpacePoint = Point(transform.inverse() * objectSpacePoint)

    return squareColorAt(patternSpacePoint)
  }

  fun squareColorAt(point: Point): Color {
    val x = Math.floor(point.x.toDouble()).toInt()
    val y = Math.floor(point.y.toDouble()).toInt()
    val z = Math.floor(point.z.toDouble()).toInt()
    return if (((x + y + z) % 2) == 0) tick else tock
  }

  override fun equals(other: Any?): Boolean {
    if (other !is CheckeredPattern) return false

    return tick == other.tick
      && tock == other.tock
      && transform == other.transform
      && attributeEquals(ambient, other.ambient)
      && attributeEquals(diffuse, other.diffuse)
      && attributeEquals(specular, other.specular)
      && shininess == other.shininess
  }
}
