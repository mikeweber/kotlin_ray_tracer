package com.weberapps.rayTracer

class ReflectiveMaterial(refractiveIndex: Float = 1f, ambient: Float = 1f, diffuse: Float = 1f, specular: Float = 1f, shininess: Int = 1) : TransparentMaterial(refractiveIndex, ambient, diffuse, specular, shininess) {
    override fun fresnel(angleOfIncidence: Vector, normal: Vector, externalRefractiveIndex: Float): Float {
        return 1f
    }
}