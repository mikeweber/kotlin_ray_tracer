package com.weberapps.rayTracer

import java.lang.Math.abs
import kotlin.math.pow

interface Material {
    val ambient: Float
    val diffuse: Float
    val specular: Float
    val shininess: Int

    fun lighting(hit: Intersection, light: Light, world: World? = null, inShadow: Boolean = false, refractionsLeft: Int = 5, surfaceOffset: Float = 0.0001f): Color?

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

    fun attributeEquals(a: Float, b: Float, eps: Float = EPSILON): Boolean {
        return abs(a - b) <= eps
    }
}