package com.weberapps.rayTracer

import java.lang.Math.abs
import kotlin.math.pow

open class Material(
        open val color: Color           = Color.WHITE,
        open val ambient: Float         = 0.1f,
        open val diffuse: Float         = 0.9f,
        open val specular: Float        = 0.9f,
        open val shininess: Int         = 200,
        open val reflective: Float      = 0f
) {
    fun lighting(hit: Intersection, light: Light, world: World? = null, inShadow: Boolean = false, refractionsLeft: Int = 5, surfaceOffset: Float = 0.001f): Color {
        return reflectedColor(hit, world, refractionsLeft) * reflective + surfaceColor(hit, light, world, inShadow, refractionsLeft, surfaceOffset) * (1f - reflective)
    }

    fun reflectedColor(hit: Intersection, world: World? = null, refractionsLeft: Int = 5): Color {
        if (reflective == 0f || world == null) return Color.BLACK

        val reflectedRay = Ray(hit.point, hit.reflectVector)
        return world.colorAt(reflectedRay, refractionsLeft - 1)
    }

    fun reflect(angleOfIncidence: Vector, normal: Vector): Vector {
        return angleOfIncidence - normal * angleOfIncidence.dot(normal) * 2f
    }

    fun clamp(min: Float, value: Float, max: Float): Float {
        return Math.max(min, Math.min(value, max))
    }

  open fun surfaceColor(hit: Intersection, light: Light, world: World? = null, inShadow: Boolean = false, refractionsLeft: Int = 5, surfaceOffset: Float = 0.001f): Color {
    val effectiveColor = color * light.intensity
    return calculateColor(effectiveColor, light, hit.point, hit.eyeVector, hit.normalVector, inShadow)
  }

    fun calculateColor(effectiveColor: Color, light: Light, position: Point, eyeVector: Vector, normalVector: Vector, inShadow: Boolean): Color {
        val lightVector       = (light.position - position).normalize()
        val ambientComponent  = effectiveColor * ambient
        val lightDotNormal    = lightVector.dot(normalVector)
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
}
