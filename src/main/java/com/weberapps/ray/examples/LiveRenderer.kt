package com.weberapps.ray.examples

import com.weberapps.ray.examples.scenes.GlassOfWaterWithPencil
import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.renderer.Camera
import com.weberapps.ray.tracer.renderer.World
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.image.WritableImage
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.stage.Stage
import java.lang.Math.PI

open class LiveRenderer: Application() {
  override fun start(primaryStage: Stage) {
    var focus = 100
    var iterations = 0
    val width = 300.0
    val height = 250.0
    var hits = hashMapOf<Pair<Int, Int>, Boolean>()
    val world = GlassOfWaterWithPencil()
    val fieldOfView = TAU / 6
    val viewTransform: Matrix = Transformation.viewTransform(
      Point(0f, 0f, -5f),
      Point(0f, 0f, 0f),
      Vector(0f, 1f, 0f)
    )
    val image = WritableImage(width.toInt(), height.toInt())
    val camera = Camera(width.toInt(), height.toInt(), fieldOfView, viewTransform)
    val canvas = Canvas(width, height)
    val scene = Scene(Pane(canvas), width, height)

    primaryStage.scene = scene
    primaryStage.title = "Hello world"
    primaryStage.show()

    object: AnimationTimer() {
      override fun handle(now: Long) {
        iterations++
        if (focus > 1 && iterations % 100 == 0) {
          println("Reduce focus to $focus")
          focus--
        }
        render(canvas, world, image, hits, focus, camera)
      }
    }.start()
  }

  fun render(canvas: Canvas, world: World, image: WritableImage, hits: HashMap<Pair<Int, Int>, Boolean>, focus: Int, camera: Camera) {
    var coord = randomCoord(focus, camera.hsize, camera.vsize)
    while (hits.containsKey(coord)) coord = randomCoord(focus, camera.hsize, camera.vsize)
    hits[coord] = true

    val x = coord.first
    val y = coord.second

    image.pixelWriter.setColor(x, y, fxColorAt(x, y, world, camera))
    canvas.graphicsContext2D.drawImage(image, 0.0, 0.0)
  }

  private fun fxColorAt(x: Int, y: Int, world: World, camera: Camera): Color {
    val p = camera.colorForPixel(x, y, world)
    return Color.color(p.red.toDouble(), p.green.toDouble(), p.blue.toDouble())
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
