package com.weberapps.ray.tracer.shape

import com.weberapps.ray.tracer.material.IMaterial
import com.weberapps.ray.tracer.material.VACUUM
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point

class RingMaterial(
  val tick: Color                     = Color.WHITE,
  val tock: Color                     = Color.BLACK,
  override val transform: Matrix      = Matrix.eye(4),
  override val ambient: Float         = 0.1f,
  override val diffuse: Float         = 0.9f,
  override val specular: Float        = 0.9f,
  override val shininess: Int         = 200,
  override val reflective: Float      = 0.1f,
  override val transparency: Float    = 0f,
  override val refractiveIndex: Float = VACUUM
) : IMaterial {
  override fun effectiveColor(shape: Shape, worldSpacePoint: Point, light: Light): Color {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  fun patternAt(objectSpacePoint: Point): Color {
    val sqrt = Math.sqrt((objectSpacePoint.x * objectSpacePoint.x + objectSpacePoint.z * objectSpacePoint.z).toDouble())
    val floor = Math.floor(sqrt)
    return if ((floor % 2) == 0.0) tick else tock
  }
}