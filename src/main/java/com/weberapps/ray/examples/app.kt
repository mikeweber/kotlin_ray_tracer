package com.weberapps.ray.examples

import java.time.Instant

fun main(args: Array<String>) {
  DrawGlassOfWater(filename = "scene${Instant.now()}.ppm", hsize=192, vsize=120) //, hsize = 1600, vsize = 900)
}
