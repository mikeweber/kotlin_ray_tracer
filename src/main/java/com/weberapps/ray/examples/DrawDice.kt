package com.weberapps.ray.examples

import com.weberapps.ray.examples.shapes.Die
import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.material.CheckeredPattern
import com.weberapps.ray.tracer.material.GradientMaterial
import com.weberapps.ray.tracer.material.SolidColor
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.renderer.World
import com.weberapps.ray.tracer.shape.Plane

class DrawDice(override val hsize: Int, override val vsize: Int, override val filename: String) : SceneRenderer {
  init {
    save(from = Point(2f, 2f, -4f), to = Point(0f, 0f, 0f), fieldOfView = TAU / 10)
  }

  override fun initWorld(): World {
    val floor = Plane(material = CheckeredPattern(SolidColor(0.8f, 0.8f, 0.8f), SolidColor(0.3f, 0.3f, 0.3f)), transform = Transformation.translation(0f, -1f, 0f))
    val backdrop = Plane(
      Transformation.translation(0f, 0f, 4f) * Transformation.rotateX(TAU / 4),
      GradientMaterial(SolidColor(0.2f, 0.2f, 0.6f), SolidColor(0.7f, 0.7f, 1.0f), Transformation.rotateZ(TAU / 4), specular = 0f)
    )
    return World(arrayListOf(Die(material = SolidColor(Color(0.4f, 0.7f, 0.4f), specular = 0.5f)), floor,  backdrop), arrayListOf(Light(Point(3f, 3f, -5f))))
  }
}