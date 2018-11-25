package com.weberapps.ray.tracer.math

class Light(val position: Point, val intensity: Color = Color.WHITE) {
  override fun equals(other: Any?): Boolean {
    if (other !is Light) return false

    return position == other.position && intensity == other.intensity
  }
}