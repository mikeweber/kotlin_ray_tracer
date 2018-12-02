package com.weberapps.ray.examples

import com.weberapps.ray.tracer.io.OBJReader
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.renderer.World
import java.io.File
import java.nio.file.Paths

class DrawTeapot(override val hsize: Int, override val vsize: Int, override val filename: String) : SceneRenderer {
  init { save() }

  override fun initWorld(): World {
    val parser =  OBJReader(File(Paths.get("").toAbsolutePath().toString() + "/teapot.obj").reader())
    val world = World(lightSources = arrayListOf(Light(Point(5f, 3f, -5f))))
    for (g in parser.groups.values) {
      world.add(g)
    }
    return world
  }
}