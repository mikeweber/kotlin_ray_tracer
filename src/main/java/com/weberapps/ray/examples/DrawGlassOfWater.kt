package com.weberapps.ray.examples

import com.weberapps.ray.examples.scenes.GlassOfWaterWithPencil
import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.math.Point

class DrawGlassOfWater(override val hsize: Int, override val vsize: Int, override val filename: String): SceneRenderer {
  init { save(from = Point(2f, 4f, -5f), to = Point(0f, 2f, 0f), fieldOfView = TAU / 4) }

  override fun initWorld() = GlassOfWaterWithPencil()
}
