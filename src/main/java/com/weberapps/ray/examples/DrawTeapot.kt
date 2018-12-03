package com.weberapps.ray.examples

import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.io.OBJReader
import com.weberapps.ray.tracer.material.CheckeredPattern
import com.weberapps.ray.tracer.material.GradientMaterial
import com.weberapps.ray.tracer.material.SolidColor
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.renderer.World
import com.weberapps.ray.tracer.shape.Plane
import java.io.File
import java.nio.file.Paths

class DrawTeapot(override val hsize: Int, override val vsize: Int, override val filename: String) : SceneRenderer {
  init { save(from = Point(0f, 6f, -10f), to = Point(0f, 2f, 0f)) }

  override fun initWorld(): World {
    val parser =  OBJReader(File(Paths.get("").toAbsolutePath().toString() + "/teapot-low.obj").reader())
    val world = World(lightSources = arrayListOf(Light(Point(5f, 3f, -5f))))
    for (g in parser.groups.values) {
      g.material = SolidColor(139f / 255f, 64f / 255f, 0f)
      world.add(g)
    }
    val floor = Plane(material = CheckeredPattern(SolidColor(0.8f, 0.8f, 0.8f), SolidColor(0.3f, 0.3f, 0.3f), transform = Transformation.scale(0.25f)))
    val backdrop = Plane(
      Transformation.translation(0f, 0f, 10f) * Transformation.rotateX(TAU / 4),
      GradientMaterial(SolidColor(0.2f, 0.2f, 0.6f), SolidColor(0.7f, 0.7f, 1.0f), Transformation.rotateZ(TAU / 4), specular = 0f)
    )
    world.add(floor).add(backdrop)
    return world
  }
}