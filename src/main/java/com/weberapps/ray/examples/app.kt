package com.weberapps.ray.examples

import java.time.Instant

fun main() {
  // DrawSceneWithPlanes(400, 400, "spheres_${Instant.now()}.ppm")
  DrawGlassOfWater(192, 120, "water_${Instant.now()}.ppm") //, hsize = 1600, vsize = 900)
  // DrawGlassOfWaterLive(192.0, 120.0) //, hsize = 1600, vsize = 900)
  // DrawShallowPool(192, 120, "pool.ppm")
  // DrawTeapot(1920, 1200, "smooth_teapot_large.ppm")
  // val angle = -2 * TAU / 360
  // DrawDice(800, 800, "dice_${Instant.now()}.ppm", from = Point(0f, 5f, 0f), up = Vector(Math.sin(angle).toFloat(), 0f, Math.cos(angle).toFloat()))
}
