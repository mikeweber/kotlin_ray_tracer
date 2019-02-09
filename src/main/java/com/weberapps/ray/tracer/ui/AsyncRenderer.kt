package com.weberapps.ray.tracer.ui

import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.math.Coordinate
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.renderer.Camera
import com.weberapps.ray.tracer.renderer.World
import javafx.scene.paint.Color
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.math.roundToInt

class AsyncRenderer(
  val width: Int,
  val height: Int,
  val world: World,
  fieldOfView: Double = TAU / 6,
  from: Point = Point(0f, 0f, -5f),
  to: Point = Point(0f, 0f, 0f),
  up: Vector = Vector(0f, 1f, 0f)
) {
  private val viewTransform = Transformation.viewTransform(from, to, up)
  private val camera = Camera(width, height, fieldOfView, viewTransform)
  private val jobs = ArrayList<Job>()

  fun render(renderer: LiveRenderer) {
    val coords = prioritizedCoordinates()
    Executors.newFixedThreadPool(10).asCoroutineDispatcher().use { context ->
      for ((x, y) in coords) {
        val job = GlobalScope.launch(context) {
          val p = camera.colorForPixel(x, y, world)
          val r = floatTo8Bit(p.red)
          val g = floatTo8Bit(p.green)
          val b = floatTo8Bit(p.blue)
          val color = Color.rgb(r, g, b)

          renderer.bufferPixel(x, y, color)
        }
        jobs.add(job)
      }
    }
  }

  fun stop() {
    for (job in jobs) job.cancel()
  }

  private fun prioritizedCoordinates(): List<Coordinate> {
    val center = Coordinate(width / 2, height / 2)
    val coords = arrayListOf<Pair<Double, Coordinate>>()
    for (y in 0..(height - 1)) {
      for (x in 0..(width - 1)) {
        val coord = Coordinate(x, y)
        val dist = ((coord.x - center.x) * (coord.x - center.x) + (coord.y - center.y) * (coord.y - center.y)) * Math.random()
        coords.add(Pair(dist, Coordinate(x, y)))
      }
    }
    coords.sortBy { it.first }
    return coords.map { it.second }
  }

  private fun floatTo8Bit(float: Float): Int {
    val boundFloat = if (float > 1f) 1f else { if (float < 0f) 0f else float }
    return (boundFloat * 255).roundToInt()
  }
}
