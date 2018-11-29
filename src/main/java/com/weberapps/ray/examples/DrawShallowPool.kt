package com.weberapps.ray.examples

import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.material.CheckeredPattern
import com.weberapps.ray.tracer.material.RingMaterial
import com.weberapps.ray.tracer.material.SolidColor
import com.weberapps.ray.tracer.material.StripePattern
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.renderer.World
import com.weberapps.ray.tracer.shape.Plane

class DrawShallowPool(override val hsize: Int, override val vsize: Int, override val filename: String): SceneRenderer {
  override fun initWorld(): World {
    val floorMaterial = CheckeredPattern(
      StripePattern(
        SolidColor(Color(0.8f, 0.2f, 0.2f)),
        SolidColor(Color(0.5f, 0.1f, 0.1f)),
        Transformation.rotateZ(TAU / 4)
      ),
      RingMaterial(
        SolidColor(Color.WHITE),
        SolidColor(Color.BLACK)
      )
    )
    val floor = Plane(material = floorMaterial)
    return World(arrayListOf(floor), arrayListOf(Light(Point(3f, 3f, -5f), Color.WHITE)))
  }

  init { save() }
}