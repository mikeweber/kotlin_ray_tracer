package com.weberapps.ray.tracer.math

fun floatRoot(x: Float): Float {
  return Math.sqrt(x.toDouble()).toFloat()
}

fun floatRoot(x: Double): Float {
  return Math.sqrt(x).toFloat()
}