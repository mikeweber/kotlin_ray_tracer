package com.weberapps.ray.examples

import com.weberapps.ray.examples.shapes.GlassOfWater
import com.weberapps.ray.examples.shapes.Pencil
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

class DrawGlassOfWater(override val hsize: Int, override val vsize: Int, override val filename: String): SceneRenderer {
  init { save() }

  override fun initWorld(): World {
    val floor = Plane(material = CheckeredPattern(SolidColor(0.8f, 0.8f, 0.8f), SolidColor(0.3f, 0.3f, 0.3f)))
    val backdrop = Plane(
      Transformation.translation(0f, 0f, 2f) * Transformation.rotateX(TAU / 4),
      GradientMaterial(SolidColor(Color(0.2f, 0.2f, 0.6f)), SolidColor(Color(0.7f, 0.7f, 1.0f)), Transformation.rotateZ(TAU / 4), specular = 0f)
    )
    val glassOfWater = GlassOfWater()
    val pencil = Pencil(Transformation.translation(-0.6f, 0.4f, 0f) * Transformation.rotateZ(-25 * TAU / 360))
    return World(arrayListOf(glassOfWater, pencil, floor, backdrop), arrayListOf(Light(Point(3f, 3f, -5f), Color(0.8f, 0.8f, 0.8f))))
  }
}