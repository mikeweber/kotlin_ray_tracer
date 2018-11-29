package com.weberapps.ray.examples

import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.material.CheckeredPattern
import com.weberapps.ray.tracer.material.DIAMOND
import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.material.StripePattern
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
import com.weberapps.ray.tracer.shape.Cylinder
import java.nio.file.Paths
import java.time.Duration
import java.time.Instant

class CubedRoom(val filename: String, val hsize: Int, val vsize: Int) {
  init {
    val t0 = Instant.now()
    saveCanvas(render(initWorld(), Point(0f, 0f, -2.5f), Point(0f, 0f, 0f)))
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
    val floorAndCeiling = Cube(
      transform = Transformation.translation(0f, 0f, -3f) *
        Transformation.scale(2f, 1f, 5f),
      material = CheckeredPattern(reflective = 0.4f, diffuse = 1f, transform = Transformation.scale(0.25f, 0.5f, 0.1f))
    )
    val walls = Cube(
      transform = Transformation.translation(0f, 0f, -3f) *
        Transformation.scale(1f, 2f, 4f),
      material = Material(color = Color(0.3f, 0.3f, 0.6f), shininess = 1000, diffuse = 0.5f, ambient = 0.3f)
    )
    val column1 = Cylinder(
      transform = Transformation.translation(0.7f, 0f, 0.7f) *
        Transformation.rotateY(TAU / 8) *
        Transformation.scale(0.1f, 1f, 0.1f),
      closed = true,
      minimum = -1f,
      maximum = 1f,
      material = StripePattern(Color(0.3f, 0.7f, 0.3f), Color(0.3f, 0.5f, 0.3f), transform = Transformation.rotateZ(-TAU / 14) * Transformation.scale(0.3f, 1f, 1f), reflective = 0.3f)
    )
    val column2 = Cylinder(
      transform = Transformation.translation(-0.7f, 0f, 0.7f) *
        Transformation.rotateY(-TAU / 8) *
        Transformation.scale(0.1f, 1f, 0.1f),
      closed = true,
      minimum = -1f,
      maximum = 1f,
      material = StripePattern(Color(0.3f, 0.7f, 0.3f), Color(0.3f, 0.5f, 0.3f), transform = Transformation.rotateZ(-TAU / 8) * Transformation.scale(0.3f, 1f, 1f), reflective = 0.3f)
    )
    val column3 = Cylinder(
      transform = Transformation.translation(-0.7f, 0f, -0.7f) *
        Transformation.scale(0.1f, 1f, 0.1f),
      closed = true,
      minimum = -1f,
      maximum = 1f,
      material = Material(transparency = 1f, reflective = 0.4f, refractiveIndex = DIAMOND, specular = 1f, shininess = 500, diffuse = 0.1f, ambient = 0f)
    )
    val column4 = Cylinder(
      transform = Transformation.translation(0.7f, 0f, -0.7f) *
        Transformation.scale(0.1f, 1f, 0.1f),
      closed = true,
      minimum = -1f,
      maximum = 1f,
      material = Material(transparency = 1f, reflective = 0.4f, refractiveIndex = DIAMOND, specular = 1f, shininess = 500, diffuse = 0.1f, ambient = 0f)
    )
    val light = Light(Point(0.5f, 0.7f, -4f))

    return World(arrayListOf(floorAndCeiling, walls, column1, column2, column3, column4), arrayListOf(light))
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
