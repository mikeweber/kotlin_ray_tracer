package com.weberapps.ray.tracer

class CheckeredPattern(
  override val color: com.weberapps.ray.tracer.Color = com.weberapps.ray.tracer.Color.Companion.WHITE,
  val tock: com.weberapps.ray.tracer.Color = com.weberapps.ray.tracer.Color.Companion.BLACK,
  val transform: com.weberapps.ray.tracer.Matrix = com.weberapps.ray.tracer.Matrix.Companion.eye(4),
  override val ambient: Float         = 0.1f,
  override val diffuse: Float         = 0.9f,
  override val specular: Float        = 0.9f,
  override val shininess: Int         = 200,
  override val reflective: Float      = 0.1f,
  override val transparency: Float    = 0f,
  override val refractiveIndex: Float = com.weberapps.ray.tracer.Material.Companion.VACUUM
): com.weberapps.ray.tracer.Material(color, ambient, diffuse, specular, shininess, reflective) {
  val tick get() = color

  override fun surfaceColor(hit: com.weberapps.ray.tracer.Intersection, light: com.weberapps.ray.tracer.Light, world: com.weberapps.ray.tracer.World?, inShadow: Boolean, refractionsLeft: Int, surfaceOffset: Float): com.weberapps.ray.tracer.Color {
    val effectiveColor= squareColorAtObject(hit.shape, hit.point) * light.intensity
    return calculateColor(effectiveColor, light, hit.point, hit.eyeVector, hit.normalVector, inShadow)
  }

  fun squareColorAtObject(shape: com.weberapps.ray.tracer.Shape, worldSpacePoint: com.weberapps.ray.tracer.Point): com.weberapps.ray.tracer.Color {
    val objectSpacePoint = shape.transform.inverse() * worldSpacePoint
    val patternSpacePoint = com.weberapps.ray.tracer.Point(transform.inverse() * objectSpacePoint)

    return squareColorAt(patternSpacePoint)
  }

  fun squareColorAt(point: com.weberapps.ray.tracer.Point): com.weberapps.ray.tracer.Color {
    val x = Math.floor(point.x.toDouble()).toInt()
    val y = Math.floor(point.y.toDouble()).toInt()
    val z = Math.floor(point.z.toDouble()).toInt()
    return if (((x + y + z) % 2) == 0) tick else tock
  }

  override fun equals(other: Any?): Boolean {
    if (other !is com.weberapps.ray.tracer.CheckeredPattern) return false

    return tick == other.tick
      && tock == other.tock
      && transform == other.transform
      && attributeEquals(ambient, other.ambient)
      && attributeEquals(diffuse, other.diffuse)
      && attributeEquals(specular, other.specular)
      && shininess == other.shininess
  }
}
