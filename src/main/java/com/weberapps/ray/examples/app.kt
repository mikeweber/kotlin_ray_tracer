package com.weberapps.ray.examples

import java.time.Instant

fun main(args: Array<String>) {
  DrawGlassOfWater(1920, 1200, "water_${Instant.now()}.ppm") //, hsize = 1600, vsize = 900)
  // DrawShallowPool(192, 120, "pool.ppm")
  // DrawTeapot(1920, 1200, "smooth_teapot_large.ppm")
}
