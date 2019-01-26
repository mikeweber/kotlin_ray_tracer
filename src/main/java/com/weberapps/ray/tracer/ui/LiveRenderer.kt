package com.weberapps.ray.tracer.ui

import com.weberapps.ray.examples.scenes.GlassOfWaterWithPencil
import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.renderer.Camera
import com.weberapps.ray.tracer.renderer.World
import com.weberapps.ray.tracer.ui.AsyncRenderer
import com.weberapps.ray.tracer.ui.BeginRenderingEvent
import com.weberapps.ray.tracer.ui.RenderCompleteEvent
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.image.WritableImage
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.stage.Stage
import tornadofx.*

open class LiveRenderer: View("Ray Tracer Live Renderer") {
  val width = 300.0
  val height = 250.0
  val world = GlassOfWaterWithPencil()

  val image = WritableImage(width.toInt(), height.toInt())
  val canvas = Canvas(width, height)
  val scene = Scene(Pane(canvas), width, height)


  override val root = borderpane {
    center {
      hbox {
        stackpane {
          imageview(image)
        }
      }
    }
  }

  init {
    subscribe<RenderCompleteEvent> {
      image.pixelWriter.setColor(it.x, it.y, it.color)
    }

    AsyncRenderer(width.toInt(), height.toInt(), world, from = Point(2f, 4f, -5f), to = Point(0f, 2f, 0f), fieldOfView = TAU / 4)
    fire(BeginRenderingEvent)
  }
}
