package com.weberapps.ray.examples

import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Vector
import java.time.Instant

fun main(args: Array<String>) {
  // DrawGlassOfWater(1920, 1200, "water_${Instant.now()}.ppm") //, hsize = 1600, vsize = 900)
  // DrawShallowPool(192, 120, "pool.ppm")
  // DrawTeapot(1920, 1200, "smooth_teapot_large.ppm")
  DrawDice(800, 800, "dice_${Instant.now()}.ppm")
}
