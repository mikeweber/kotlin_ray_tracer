package com.weberapps.rayTracer

class StripePattern(
  val zig: Color = Color.WHITE,
  val zag: Color = Color.BLACK,
  val transform: Matrix = Matrix.eye(4),
  override val ambient: Float = 0.1f,
  override val diffuse: Float = 0.9f,
  override val specular: Float = 0.9f,
  override val shininess: Int = 200,
  override val reflective: Float = 0f
): Material(zig, ambient, diffuse, specular, shininess, reflective) {
  override fun surfaceColor(hit: Intersection, light: Light, world: World?, inShadow: Boolean, refractionsLeft: Int, surfaceOffset: Float): Color {
    val effectiveColor= stripeAtObject(hit.shape, hit.point) * light.intensity
    return calculateColor(effectiveColor, light, hit.point, hit.eyeVector, hit.normalVector, inShadow)
  }

  fun stripeAtObject(shape: Shape, worldSpacePoint: Point): Color {
    val objectSpacePoint = shape.transform.inverse() * worldSpacePoint
    val patternSpacePoint = Point(transform.inverse() * objectSpacePoint)

    return stripeAt(patternSpacePoint)
  }

  fun stripeAt(point: Point): Color {
    return if ((Math.floor(point.x.toDouble()).toInt() % 2) == 0) zig else zag
  }

  override fun equals(other: Any?): Boolean {
    if (other !is StripePattern) return false

    return zig == other.zig
      && zag == other.zag
      && attributeEquals(ambient, other.ambient)
      && attributeEquals(diffuse, other.diffuse)
      && attributeEquals(specular, other.specular)
      && shininess == other.shininess
  }
}