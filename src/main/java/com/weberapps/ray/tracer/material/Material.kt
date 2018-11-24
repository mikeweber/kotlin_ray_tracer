package com.weberapps.ray.tracer.material

import com.weberapps.ray.tracer.constants.EPSILON
import com.weberapps.ray.tracer.intersection.Intersection
import com.weberapps.ray.tracer.math.*
import com.weberapps.ray.tracer.renderer.World
import java.lang.Math.abs
import kotlin.math.pow

open class Material(
  open val color: Color           = Color.WHITE,
  open val transform: Matrix      = Matrix.eye(4),
  open val ambient: Float         = 0.1f,
  open val diffuse: Float         = 0.9f,
  open val specular: Float        = 0.9f,
  open val shininess: Int         = 200,
  open val reflective: Float      = 0f,
  open val transparency: Float    = 0f,
  open val refractiveIndex: Float = VACUUM
) {
  fun lighting(hit: Intersection, light: Light, world: World? = null, inShadow: Boolean = false, refractionsLeft: Int = 5, surfaceOffset: Float = 0.001f): Color {
    val surface   = surfaceColor(hit, light, world, inShadow, refractionsLeft, surfaceOffset)
    val reflected = reflectedColor(hit, world, refractionsLeft)
    val refracted = refractedColor(hit, world, refractionsLeft)

    if (reflective == 0f || transparency == 0f) return surface + reflected + refracted

    val reflectance = hit.schlick()
    return surface + reflected * reflectance + refracted * (1f - reflectance)
  }

  companion object {
    val VACUUM = 1f
    val AIR = 1.00029f
    val WATER = 1.333f
    val GLASS = 1.52f
    val DIAMOND = 2.417f
  }

  private fun reflectedColor(hit: Intersection, world: World? = null, refractionsLeft: Int = 5): Color {
    if (reflective == 0f || world == null) return Color.BLACK

    val reflectedRay = Ray(hit.point, hit.reflectVector)
    return world.colorAt(reflectedRay, refractionsLeft - 1) * reflective
  }

  open fun surfaceColor(hit: Intersection, light: Light, world: World? = null, inShadow: Boolean = false, refractionsLeft: Int = 5, surfaceOffset: Float = 0.001f): Color {
    val effectiveColor = color * light.intensity
    return calculateColor(effectiveColor, light, hit.point, hit.eyeVector, hit.normalVector, inShadow)
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

  private fun effectiveDiffuse(inShadow: Boolean): Float {
    return if (inShadow) 0f else diffuse
  }

  private fun effectiveSpecular(inShadow: Boolean): Float {
    return if (inShadow) 0f else specular
  }

  override fun equals(other: Any?): Boolean {
    if (other !is Material) return false

    return color == other.color
      && ambient == other.ambient
      && diffuse == other.diffuse
      && specular == other.specular
      && shininess == other.shininess
      && reflective == other.reflective
  }

  fun attributeEquals(a: Float, b: Float, eps: Float = EPSILON): Boolean {
    return abs(a - b) <= eps
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
}
