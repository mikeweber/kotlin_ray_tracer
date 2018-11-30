package com.weberapps.ray.examples

import com.weberapps.ray.tracer.renderer.Camera
import com.weberapps.ray.tracer.renderer.Canvas
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.material.SolidColor
import com.weberapps.ray.tracer.io.PPMGenerator
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.shape.Sphere
import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.renderer.World
import java.nio.file.Paths
import java.time.Duration
import java.time.Instant

class DrawScene(filename: String = "scene_with_multiple_shadows.ppm", hsize: Int = 480, vsize: Int = 270) {
  init {
    val t0 = Instant.now()
    var absolutePath = filename
    if (!filename.startsWith("/")) {
      absolutePath = Paths.get("").toAbsolutePath().toString() + "/" + filename
    }
    println("Saving circle to $absolutePath")
    println("Beginning draw")
    PPMGenerator(draw(hsize, vsize)).save(absolutePath)
    println("Finished draw in ${Duration.between(t0, Instant.now())}")
    println("Done")
  }

  fun draw(hsize: Int, vsize: Int): Canvas {
    val squash = Transformation.scale(10f, 0.01f, 10f)
    val matte = SolidColor(Color(1f, 0.9f, 0.9f), specular = 0f)
    val floor = Sphere(transform = squash, material = matte)

    val leftWallTransform = Transformation.translation(0f, 0f, 5f) *
      Transformation.rotateY(-TAU / 8) *
      Transformation.rotateX(TAU / 4) *
      Transformation.scale(10f, 0.01f, 10f)
    val leftWall = Sphere(transform = leftWallTransform, material = matte)

    val rightWallTransform = Transformation.translation(0f, 0f, 5f) *
      Transformation.rotateY(TAU / 8) *
      Transformation.rotateX(TAU / 4) *
      Transformation.scale(10f, 0.01f, 10f)
    val rightWall = Sphere(transform = rightWallTransform, material = matte)

    val middleTransform = Transformation.translation(-0.5f, 1f, 0.5f)
    val middleMaterial = SolidColor(
      color = Color(0.1f, 1f, 0.5f),
      diffuse = 0.7f,
      specular = 0.3f
    )
    val middleSphere = Sphere(transform = middleTransform, material = middleMaterial)

    val rightTransform = Transformation.translation(1.5f, 0.5f, -0.5f) *
      Transformation.scale(0.5f, 0.5f, 0.5f)
    val rightMaterial = SolidColor(
      color = Color(0.5f, 1f, 0.1f),
      diffuse = 0.7f,
      specular = 0.7f
    )
    val rightSphere = Sphere(transform = rightTransform, material = rightMaterial)

    val leftTransform = Transformation.translation(-1.75f, 0.5f, -0.5f) *
      Transformation.scale(0.5f, 0.5f, 0.5f)
    val leftMaterial = SolidColor(
      color = Color(1f, 0.7f, 0f),
      diffuse = 0.7f,
      specular = 0.3f
    )
    val leftSphere = Sphere(transform = leftTransform, material = leftMaterial)

    val world = World(
      arrayListOf(floor, leftWall, rightWall, middleSphere, rightSphere, leftSphere),
      arrayListOf(
        Light(
          Point(-10f, 10f, -10f),
          Color(0.5f, 0.5f, 0.5f)
        ),
        Light(
          Point(10f, 10f, -10f),
          Color(0.5f, 0.5f, 0.5f)
        )
      )
    )

    val viewTransform = Transformation.viewTransform(
      Point(0f, 1.5f, -5f),
      Point(0f, 1f, 0f),
      Vector(0f, 1f, 0f).normalize()
    )
    val camera =
      Camera(hsize, vsize, TAU / 6, transform = viewTransform)

    return camera.render(world)
  }
}