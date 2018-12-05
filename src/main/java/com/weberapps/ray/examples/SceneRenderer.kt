package com.weberapps.ray.examples

import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.renderer.Camera
import com.weberapps.ray.tracer.renderer.Canvas
import com.weberapps.ray.tracer.io.PPMGenerator
import com.weberapps.ray.tracer.renderer.World
import java.nio.file.Paths
import java.time.Duration
import java.time.Instant

interface SceneRenderer {
  val hsize: Int
  val vsize: Int
  val filename: String

  fun initWorld(): World

  fun save(
    world: World = initWorld(),
    from: Point = Point(0f, 0f, -5f),
    to: Point = Point(0f, 0f, 0f),
    up: Vector = Vector(0f, 1f, 0f),
    fieldOfView: Double = TAU / 6
  ) {
    val t0 = Instant.now()
    saveCanvas(render(world, from, to, up, fieldOfView))
    println("Finished in ${Duration.between(t0, Instant.now())}")
  }

  private fun saveCanvas(canvas: Canvas, saveAs: String = filename) {
    var absolutePath = saveAs
    if (!absolutePath.startsWith("/")) {
      absolutePath = Paths.get("").toAbsolutePath().toString() + "/" + saveAs
    }
    println("Saving render to $absolutePath")
    PPMGenerator(canvas).save(absolutePath)
  }

  private fun render(
    world: World,
    from: Point = Point(3f, 4f, -5f),
    focus: Point = Point(0f, 1f,  0f),
    up: Vector = Vector(0f, 1f, 0f),
    fieldOfView: Double = TAU / 6
  ): Canvas {
    val viewTransform = Transformation.viewTransform(
      from = from,
      to = focus,
      up = up.normalize()
    )
    val camera = Camera(hsize, vsize, fieldOfView, viewTransform)

    return camera.coroutineRender(world)
  }
}
