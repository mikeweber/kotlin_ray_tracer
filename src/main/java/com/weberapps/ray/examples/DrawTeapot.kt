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
import com.weberapps.ray.tracer.shape.MaterializedShape
import com.weberapps.ray.tracer.shape.Plane
import java.io.File
import java.nio.file.Paths

class DrawTeapot(override val hsize: Int, override val vsize: Int, override val filename: String) : SceneRenderer {
  init { save(from = Point(8f, 20f, -30f), to = Point(0f, 8f, 0f)) }

  override fun initWorld(): World {
    val parser =  OBJReader(File(Paths.get("").toAbsolutePath().toString() + "/teapot-low.obj").reader())
    val world = World(lightSources = arrayListOf(Light(Point(10f, 24f, -30f))))
    val offwhite = SolidColor(248f / 255f, 248f / 255f, 1f)
    for (g in parser.groups.values) {
      for (s in g.shapes) (s as MaterializedShape).material = offwhite

      g.transform *= Transformation.rotateX(-TAU / 4)
      world.add(g)
    }
    val floor = Plane(material = CheckeredPattern(SolidColor(0.8f, 0.8f, 0.8f), SolidColor(0.3f, 0.3f, 0.3f)))
    val backdrop = Plane(
      Transformation.translation(0f, 0f, 20f) * Transformation.rotateX(TAU / 4),
      GradientMaterial(SolidColor(0.2f, 0.2f, 0.6f), SolidColor(0.7f, 0.7f, 1.0f), Transformation.rotateZ(TAU / 4), specular = 0f)
    )
    return world.add(floor).add(backdrop)
  }
}