package com.weberapps.ray.tracer.ui

import javafx.scene.paint.Color
import tornadofx.EventBus.RunOn.BackgroundThread
import tornadofx.FXEvent

object BeginRenderingEvent : FXEvent(BackgroundThread)

data class RenderCompleteEvent(val x: Int, val y: Int, val color: Color) : FXEvent()
