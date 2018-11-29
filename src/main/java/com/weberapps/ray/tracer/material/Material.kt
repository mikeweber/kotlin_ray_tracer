package com.weberapps.ray.tracer.material

import com.weberapps.ray.tracer.intersection.Intersection
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.renderer.World
import com.weberapps.ray.tracer.shape.Shape
import kotlin.math.pow

interface Material {
  val transform: Matrix
  val reflective: Float
  val transparency: Float
  val ambient: Float
  val diffuse: Float
  val specular: Float
  val shininess: Int
  val refractiveIndex: Float

  fun lighting(hit: Intersection, light: Light, world: World? = null, inShadow: Boolean = false, refractionsLeft: Int = 5, surfaceOffset: Float = 0.001f): Color {
    val surface   = surfaceColor(hit, light, world, inShadow, refractionsLeft, surfaceOffset)
    val reflected = reflectedColor(hit, world, refractionsLeft)
    val refracted = refractedColor(hit, world, refractionsLeft)

    if (reflective == 0f || transparency == 0f) return surface + reflected + refracted

    val reflectance = hit.schlick()
    return surface + reflected * reflectance + refracted * (1f - reflectance)
  }

  fun surfaceColor(hit: Intersection, light: Light, world: World? = null, inShadow: Boolean = false, refractionsLeft: Int = 5, surfaceOffset: Float = 0.001f): Color {
    return calculateColor(effectiveColor(hit.shape, hit.point, light), light, hit.point, hit.eyeVector, hit.normalVector, inShadow)
  }

  fun effectiveColor(shape: Shape, worldSpacePoint: Point, light: Light = Light(Point(0f, 0f, 0f), Color.BLACK)): Color
  fun patternAt(patternSpacePoint: Point): Color

  fun patternSpacePoint(shape: Shape, worldSpacePoint: Point): Point {
    val objectSpacePoint = shape.worldToObject(worldSpacePoint)
    return Point(transform.inverse() * objectSpacePoint)
  }

  fun calculateColor(effectiveColor: Color, light: Light, position: Point, eyeVector: Vector, normalVector: Vector, inShadow: Boolean): Color {
    val lightVector     = (light.position - position).normalize()
    val ambientComponent  = effectiveColor * ambient
    val lightDotNormal  = lightVector.dot(normalVector)
    var diffuseComponent  = Color.BLACK
    var specularComponent = Color.BLACK

    if (lightDotNormal > 0f) {
      diffuseComponent = effectiveColor * lightDotNormal * effectiveDiffuse(inShadow)
      val reflectionVector = (-lightVector).reflect(normalVector)
      val reflectDotEye = reflectionVector.dot(eyeVector).pow(shininess)
      if (reflectDotEye > 0f) {
        specularComponent = light.intensity * effectiveSpecular(inShadow) * reflectDotEye
      }
    }

    return specularComponent + ambientComponent + diffuseComponent
  }

  private fun reflectedColor(hit: Intersection, world: World? = null, refractionsLeft: Int = 5): Color {
    if (reflective == 0f || world == null) return Color.BLACK

    val reflectedRay = Ray(hit.point, hit.reflectVector)
    return world.colorAt(reflectedRay, refractionsLeft - 1) * reflective
  }

  private fun effectiveDiffuse(inShadow: Boolean): Float {
    return if (inShadow) 0f else diffuse
  }

  private fun effectiveSpecular(inShadow: Boolean): Float {
    return if (inShadow) 0f else specular
  }

  fun refractedColor(hit: Intersection, world: World? = null, refractionsLeft: Int = 5): Color {
    if (refractionsLeft <= 0 || transparency <= 0f || world == null) return Color.BLACK

    val nRatio = hit.n1 / hit.n2
    val cosI = hit.eyeVector.dot(hit.normalVector)
    val sin2T = nRatio * nRatio * (1f - cosI * cosI)
    if (sin2T > 1f) return Color.BLACK

    val cosT = Math.sqrt(1.0 - sin2T)
    val direction = hit.normalVector * (nRatio * cosI - cosT).toFloat() - hit.eyeVector * nRatio
    val refractRay = Ray(hit.underPoint, direction)

    val result = world.colorAt(refractRay, refractionsLeft - 1)
    return result * transparency
  }

  fun equals(other: Material): Boolean {
    return transform == other.transform
      && ambient == other.ambient
      && diffuse == other.diffuse
      && specular == other.specular
      && shininess == other.shininess
      && reflective == other.reflective
      && transparency == other.transparency
      && refractiveIndex == other.refractiveIndex
  }
}