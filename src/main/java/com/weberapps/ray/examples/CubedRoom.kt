package com.weberapps.ray.examples

import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.material.CheckeredPattern
import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.renderer.Camera
import com.weberapps.ray.tracer.renderer.Canvas
import com.weberapps.ray.tracer.renderer.PPMGenerator
import com.weberapps.ray.tracer.renderer.World
import com.weberapps.ray.tracer.shape.Cube
import java.nio.file.Paths
import java.time.Duration
import java.time.Instant

class CubedRoom(val filename: String, val hsize: Int, val vsize: Int) {
  init {
    val t0 = Instant.now()
    saveCanvas(render(initWorld()))
    println("Finished in ${Duration.between(t0, Instant.now())}")
  }

  fun saveCanvas(canvas: Canvas, saveAs: String = filename) {
    var absolutePath = saveAs
    if (!absolutePath.startsWith("/")) {
      absolutePath = Paths.get("").toAbsolutePath().toString() + "/" + saveAs
    }
    println("Saving render to $absolutePath")
    PPMGenerator(canvas).save(absolutePath)
  }

  fun initWorld(): World {
    val cube1 = Cube(
      transform = Transformation.scale(2f, 1f, 2f) *
        Transformation.rotateY(TAU / 8),
      material = CheckeredPattern(reflective = 0.4f, diffuse = 1f, shininess = 100)
    )
    val cube2 = Cube(
      transform = Transformation.scale(1f, 2f, 1f),
      material = Material(color = Color(0.3f, 0.3f, 0.6f))
    )
    val light = Light(Point(Math.sqrt(2.0).toFloat() - 0.08f, 0.92f, Math.sqrt(2.0).toFloat() - 0.08f))

    return World(arrayListOf(cube1, cube2), arrayListOf(light))
  }

  fun render(
    world: World,
    from: Point = Point(0f, 0f, -0.92f),
    focus: Point = Point(0f, 1f, 0f)
  ): Canvas {
    val viewTransform = Transformation.viewTransform(
      from = from,
      to = focus,
      up = Vector(0f, 1f, 0f).normalize()
    )
    val camera =
      Camera(hsize, vsize, TAU / 6, transform = viewTransform)

    return camera.render(world)
  }
}