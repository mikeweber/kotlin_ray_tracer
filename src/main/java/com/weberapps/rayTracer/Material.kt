package com.weberapps.rayTracer

import java.lang.Math.abs
import kotlin.math.pow

class Material(
        val color: Color = Color(1f, 1f, 1f),
        val ambient: Float = 0.1f,
        val diffuse: Float = 0.9f,
        val specular: Float = 0.9f,
        val shininess: Int = 200
) {
    fun lighting(light: Light, position: Point, eyeVector: Vector, normalVector: Vector): Color {
        val effectiveColor = color * light.intensity
        val lightVector = (light.position - position).normalize()
        val ambientComponent = effectiveColor * ambient
        val lightDotNormal = lightVector.dot(normalVector)
        var diffuseComponent = Color(0f, 0f, 0f)
        var specularComponent = Color(0f, 0f, 0f)

        if (lightDotNormal > 0f) {
            diffuseComponent = effectiveColor * lightDotNormal * diffuse
            val reflectionVector = (-lightVector).reflect(normalVector)
            val reflectDotEye = reflectionVector.dot(eyeVector).pow(shininess)
            if (reflectDotEye > 0f) {
                specularComponent = light.intensity * specular * reflectDotEye
            }
        }

        return specularComponent + ambientComponent + diffuseComponent
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Material) return false

        return color == other.color &&
                attributeEquals(ambient, other.ambient) &&
                attributeEquals(diffuse, other.diffuse) &&
                attributeEquals(specular, other.specular) &&
                shininess == other.shininess
    }


    private fun attributeEquals(a: Float, b: Float, eps: Float = EPSILON): Boolean {
        return abs(a - b) <= eps
    }
}