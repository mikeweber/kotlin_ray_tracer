package com.weberapps.rayTracer

import java.lang.Math.abs

class Material(
        val color: Color = Color(1f, 1f, 1f),
        val ambient: Float = 0.1f,
        val diffuse: Float = 0.9f,
        val specular: Float = 0.9f,
        val shininess: Int = 200
) {
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