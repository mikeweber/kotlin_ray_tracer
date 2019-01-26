package com.weberapps.ray.tracer.ui

import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.renderer.Camera
import com.weberapps.ray.tracer.renderer.World
import javafx.scene.paint.Color
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import tornadofx.Controller
import kotlin.math.roundToInt

class AsyncRenderer(
  val width: Int,
  val height: Int,
  val world: World,
  fieldOfView: Double = TAU / 16,
  from: Point = Point(0f, 0f, -5f),
  to: Point = Point(0f, 0f, 0f),
  up: Vector = Vector(0f, 1f, 0f)
) : Controller() {
  init {
    subscribe<BeginRenderingEvent> {
      println("BeginRenderingEvent")
      render()
    }
  }

  private val viewTransform = Transformation.viewTransform(from, to, up)
  private val camera = Camera(width, height, fieldOfView, viewTransform)

  private fun render() {
    for (y in 0..(height - 1)) {
      for (x in 0..(width - 1)) {
        GlobalScope.async {
          val p = camera.colorForPixel(x, y, world)
          val r = floatTo8Bit(p.red)
          val g = floatTo8Bit(p.green)
          val b = floatTo8Bit(p.blue)
          val color = Color.rgb(r, g, b)

          fire(RenderCompleteEvent(x, y, color))
          p
        }
      }
    }
  }

  private fun floatTo8Bit(float: Float): Int {
    val boundFloat = if (float > 1f) 1f else { if (float < 0f) 0f else float }
    val x = (boundFloat * 255)
    val y = x.roundToInt()
    return y
  }

  private fun randomCoord(focus: Int, width: Int, height: Int): Pair<Int, Int> {
    val dir = TAU * Math.random()
    val centerX = width / 2
    val centerY = height / 2
    val dist = (randDistribution(focus) - 0.5) * Math.sqrt((centerX * centerX + centerY * centerY).toDouble())
    val x = (centerX + Math.cos(dir) * dist).toInt()
    val y = (centerY + Math.sin(dir) * dist).toInt()

    return if (inbounds(x, y, width, height)) Pair(x, y) else randomCoord(focus, width, height)
  }

  private fun inbounds(x: Int, y: Int, hsize: Int, vsize: Int) = (x in 0..(hsize - 1) && y in 0..(vsize - 1))

  private fun randomX(curveHeight: Int = 1) = randDistribution(curveHeight)
  private fun randomY(curveHeight: Int = 1) = randDistribution(curveHeight)

  private fun randDistribution(times: Int): Float {
    var x = 0.0
    for (i in 0..times) {
      x += Math.random() * 2
    }
    return (x / times - 0.5).toFloat()
  }
}
