package com.weberapps.ray.tracer.ui

import com.weberapps.ray.examples.scenes.GlassOfWaterWithPencil
import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.math.Point
import javafx.scene.image.WritableImage
import tornadofx.*

open class LiveRenderer: View("Ray Tracer Live Renderer") {
  private val width = 300.0
  private val height = 250.0
  private val world = GlassOfWaterWithPencil()
  private val image = WritableImage(width.toInt(), height.toInt())

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
