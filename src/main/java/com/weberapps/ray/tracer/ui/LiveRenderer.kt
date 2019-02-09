package com.weberapps.ray.tracer.ui

import com.weberapps.ray.examples.scenes.GlassOfWaterWithPencil
import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.math.Point
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.stage.Stage

open class LiveRenderer: Application() {
  private val width = 1600.0
  private val height = 900.0
  private val world = GlassOfWaterWithPencil()
  private val buffer = ArrayList<Triple<Int, Int, Color>>()
  private val renderer = AsyncRenderer(width.toInt(), height.toInt(), world, from = Point(2f, 4f, -5f), to = Point(0f, 2f, 0f), fieldOfView = TAU / 4)

  override fun start(primaryStage: Stage) {
    primaryStage.title = "Ray Tracer"

    val root = Group()
    val scene = Scene(root)
    primaryStage.scene = scene
    primaryStage.width = width
    primaryStage.height = height
    primaryStage.show()

    val canvas = Canvas(width, height)
    root.children.add(canvas)
    val gc = canvas.graphicsContext2D
    startAnimation(gc)

    renderer.render(this)
  }

  override fun stop() {
    renderer.stop()
  }

  @Synchronized fun bufferPixel(x: Int, y: Int, color: Color) {
    buffer.add(Triple(x, y, color))
  }

  private
  fun startAnimation(gc: GraphicsContext) = object : AnimationTimer() {
    override fun handle(currentNanoTime: Long) {
      drawBuffer(gc)
    }
  }.start()

  @Synchronized fun drawBuffer(gc: GraphicsContext) {
    for (pixel in buffer) {
      gc.pixelWriter.setColor(pixel.first, pixel.second, pixel.third)
    }
    buffer.clear()
  }
}
