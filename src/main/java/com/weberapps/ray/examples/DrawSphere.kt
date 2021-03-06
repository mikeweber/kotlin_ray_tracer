package com.weberapps.ray.examples

import com.weberapps.ray.tracer.renderer.Canvas
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.material.SolidColor
import com.weberapps.ray.tracer.io.PPMGenerator
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.shape.Sphere
import java.nio.file.Paths

class DrawSphere(filename: String = "sphere.ppm") {
  init {
    var absolutePath = filename
    if (!filename.startsWith("/")) {
      absolutePath = Paths.get("").toAbsolutePath().toString() + "/" + filename
    }
    println("Saving sphere to ${absolutePath}")
    PPMGenerator(draw()).save(absolutePath)
    println("Done")
  }

  private fun draw(): Canvas {
    val color = Color(0.1f, 0.2f, 1f)
    val sphere = Sphere()
    val material = SolidColor(color = color)
    sphere.material = material
    val light = Light(Point(-10f, 10f, 10f))
    val canvas = Canvas(500, 500)
    val rayOrigin = Point(0f, 0f, -5f)
    val wallSize = 7f
    val wallZ = 10f
    val pixelSize = wallSize / canvas.width
    val half = wallSize / 2

    for (y in 0..(canvas.height - 1)) {
      val worldY = half - pixelSize * y
      for (x in 0..(canvas.width - 1)) {
        val worldX = pixelSize * x - half
        val target = Point(worldX, worldY, wallZ)
        val ray = Ray(rayOrigin, (target - rayOrigin).normalize())
        val xs = sphere.intersect(ray)

        val hit = xs.hit() ?: continue

        canvas.setPixel(x, y, hit.prepareHit(ray).colorAt(light) ?: Color.BLACK)
      }
    }

    return canvas
  }
}