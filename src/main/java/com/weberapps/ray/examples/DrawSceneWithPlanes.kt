package com.weberapps.ray.examples

import com.weberapps.ray.tracer.material.CheckeredPattern
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.material.SolidColor
import com.weberapps.ray.tracer.shape.Plane
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.shape.Sphere
import com.weberapps.ray.tracer.material.StripePattern
import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.material.GLASS
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.renderer.World

class DrawSceneWithPlanes(override val hsize: Int = 160, override val vsize: Int = 90, override var filename: String): SceneRenderer {
  init {
    save(initWorld(), from = Point(0f, 1f, -5f))
  }

  override fun initWorld(): World {
    val checkered = CheckeredPattern(reflective = 0.1f)
    val floor = Plane(material = CheckeredPattern(reflective = 0f), transform = Transformation.translation(0f, -1f, 0f))

    val leftWallTransform = Transformation.translation(0f, 0f, 5f) *
      Transformation.rotateY(-TAU / 8) *
      Transformation.rotateX(TAU / 4)
    val leftWall = Plane(transform = leftWallTransform, material = checkered)

    val rightWallTransform = Transformation.translation(0f, 0f, 5f) *
      Transformation.rotateY(TAU / 8) *
      Transformation.rotateX(TAU / 4)
    val blueRedCheckered = CheckeredPattern(tick = SolidColor(0.7f, 0.3f, 0.3f), tock = SolidColor(0.3f, 0.3f, 0.7f), reflective = 0f)
    val rightWall = Plane(transform = rightWallTransform, material = blueRedCheckered)

    val middleTransform = Transformation.translation(-0.5f, 1f, 2.5f)
    val middleMaterialTransform =
      Transformation.translation(0.05f, 0f, 0f) *
        Transformation.rotateY(-TAU / 8) *
        Transformation.rotateZ(-TAU / 16) *
        Transformation.scale(0.1f, 0.1f, 0.1f)
    val middleMaterial = StripePattern(
      zig = SolidColor(0.1f, 1f, 0.5f),
      zag = SolidColor(0.6f, 1f, 0.7f),
      transform = middleMaterialTransform,
      diffuse = 0.7f,
      specular = 0.3f,
      roughness = 0.5f,
      reflective = 0.2f
    )
    val middleSphere = Sphere(transform = middleTransform, material = middleMaterial)

    val leftTransform = Transformation.translation(-1.75f, 0.5f, 1.5f) *
      Transformation.scale(0.5f, 0.5f, 0.5f)
    val leftMaterial = SolidColor(
      color = Color(1f, 0.7f, 0f),
      diffuse = 0.7f,
      specular = 0.3f,
      reflective = 0.2f
    )
    val leftSphere = Sphere(transform = leftTransform, material = leftMaterial)

    val glassTransform1 = Transformation.translation(1f, 0f, -2f) *
      Transformation.scale(0.5f, 0.5f, 0.5f)
    val glass = SolidColor(refractiveIndex = GLASS, reflective = 0.8f, transparency = 1f, diffuse = 0.05f, specular = 0.1f)
    val glassSphere1 = Sphere(transform = glassTransform1, material = glass)

    return World(
      arrayListOf(middleSphere, floor, leftSphere, glassSphere1, rightWall, leftWall),
      arrayListOf(
        Light(Point(-10f, 10f, -10f))
      )
    )
  }
}
