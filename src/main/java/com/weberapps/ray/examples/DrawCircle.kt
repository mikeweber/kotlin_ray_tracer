package com.weberapps.ray.examples

import com.weberapps.ray.tracer.renderer.Canvas
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.io.PPMGenerator
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.shape.Sphere
import java.nio.file.Paths

class DrawCircle(filename: String = "circle.ppm") {
  init {
    var absolutePath = filename
    if (!filename.startsWith("/")) {
      absolutePath = Paths.get("").toAbsolutePath().toString() + "/" + filename
    }
    println("Saving circle to ${absolutePath}")
    PPMGenerator(draw()).save(absolutePath)
    println("Done")
  }

  private fun draw(): Canvas {
    val rayOrigin = Point(0f, 0f, -5f)
    val wallZ = 10f
    val wallSize = 7f

    val sphere = Sphere()
    val canvas = Canvas(500, 500, Color.WHITE)
    val pixelSize = wallSize / canvas.width
    val half = wallSize / 2
    val red = Color(1f, 0f, 0f)

    for (y in 0..(canvas.height - 1)) {
      val worldY = half - pixelSize * y
      for (x in 0..(canvas.width - 1)) {
        val worldX = pixelSize * x - half
        val target = Point(worldX, worldY, wallZ)
        val ray = Ray(rayOrigin, (target - rayOrigin).normalize())
        val xs = sphere.intersect(ray)

        if (xs.hit() == null) continue

        canvas.setPixel(x, y, red)
      }
    }

    return canvas
  }
}