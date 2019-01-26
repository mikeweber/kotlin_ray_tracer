package com.weberapps.ray.tracer.ui

import tornadofx.View
import tornadofx.hbox

class MainView: View() {
  override val root = hbox {
    LiveRenderer()
  }
}
