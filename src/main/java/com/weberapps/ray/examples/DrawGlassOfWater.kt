package com.weberapps.ray.examples

import com.weberapps.ray.examples.shapes.GlassOfWater
import com.weberapps.ray.examples.shapes.Pencil
import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.material.CheckeredPattern
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.renderer.Camera
import com.weberapps.ray.tracer.renderer.Canvas
import com.weberapps.ray.tracer.renderer.PPMGenerator
import com.weberapps.ray.tracer.renderer.World
import com.weberapps.ray.tracer.shape.Plane
import java.nio.file.Paths
import java.time.Duration
import java.time.Instant

class DrawGlassOfWater(val hsize: Int, val vsize: Int, val filename: String) {
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

  fun render(
    world: World,
    from:  Point = Point(3f, 4f, -5f),
    focus: Point = Point(0f, 1f,  0f)
  ): Canvas {
    val viewTransform = Transformation.viewTransform(
      from = from,
      to = focus,
      up = Vector(0f, 1f, 0f).normalize()
    )
    val camera = Camera(hsize, vsize, TAU / 6, transform = viewTransform)

    return camera.render(world)
  }

  fun initWorld(): World {
    val floor = Plane(Transformation.translation(0f, 0f, 5f), CheckeredPattern(Color(0.8f, 0.8f, 0.8f), Color(0.3f, 0.3f, 0.3f)))
    val backdrop = Plane(Transformation.translation(0f, 0f, 2f) * Transformation.rotateX(TAU / 4), CheckeredPattern(Color(0.2f, 0.2f, 0.6f), Color(0.5f, 0.5f, 0.9f)))
    val glassOfWater = GlassOfWater()
    val pencil = Pencil(Transformation.translation(-0.6f, 0.1f, 0f) * Transformation.rotateZ(-25 * TAU / 360))
    return World(arrayListOf(glassOfWater, pencil, floor, backdrop), arrayListOf(Light(Point(3f, 3f, -5f), Color(0.8f, 0.8f, 0.8f))))
  }
}