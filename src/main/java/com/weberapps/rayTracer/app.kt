package com.weberapps.rayTracer

import java.time.Instant

fun main(args: Array<String>) {
    DrawSceneWithPlanes(filename = "scene${Instant.now()}.ppm", hsize = 480, vsize = 270) //, hsize = 1600, vsize = 900) //, hsize = 480, vsize = 270)
}