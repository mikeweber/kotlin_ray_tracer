package com.weberapps.ray.examples

import com.weberapps.ray.examples.DrawSceneWithPlanes
import java.time.Instant

fun main(args: Array<String>) {
  CubedRoom(filename = "scene${Instant.now()}.ppm", hsize = 480, vsize = 480) // , hsize=1920, vsize=1200) //, hsize = 1600, vsize = 900)
}