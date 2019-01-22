package com.weberapps.ray.examples

import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.StackPane
import javafx.stage.Stage

class HelloWorld: Application() {
  override fun start(primaryStage: Stage?) {
    val btn = Button()
    btn.text = "say 'Hello world'"
    btn.onAction = EventHandler<ActionEvent> { println("Hello World!") }

    val root = StackPane()
    root.children.add(btn)

    val scene = Scene(root, 300.0, 250.0)

    primaryStage?.apply {
      this.title = "Hello World!"
      this.scene = scene
      this.show()
    }
  }
}
