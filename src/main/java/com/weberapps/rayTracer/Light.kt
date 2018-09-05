package com.weberapps.rayTracer

class Light(val position: Point, val intensity: Color = Color(1f, 1f, 1f)) {
    override fun equals(other: Any?): Boolean {
        if (other !is Light) return false

        return position == other.position && intensity == other.intensity
    }
}