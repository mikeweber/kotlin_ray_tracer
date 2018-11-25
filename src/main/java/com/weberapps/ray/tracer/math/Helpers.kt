package com.weberapps.ray.tracer.math

import com.weberapps.ray.tracer.constants.EPSILON

fun floatRoot(x: Float): Float {
  return Math.sqrt(x.toDouble()).toFloat()
}

fun floatRoot(x: Double): Float {
  return Math.sqrt(x).toFloat()
}

fun almostZero(x: Float): Boolean {
  return Math.abs(x) < EPSILON
}