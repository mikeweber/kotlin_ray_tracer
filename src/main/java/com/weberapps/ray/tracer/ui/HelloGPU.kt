package com.weberapps.ray.tracer.ui

import com.weberapps.ray.examples.scenes.GlassOfWaterWithPencil
import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.math.Point
import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL30.*
import org.lwjgl.glfw.GLFW.nglfwGetFramebufferSize
import org.lwjgl.system.MemoryUtil.memAddress
import org.lwjgl.glfw.GLFW.glfwShowWindow
import org.lwjgl.glfw.GLFW.glfwMakeContextCurrent
import org.lwjgl.glfw.GLFW.glfwSetWindowPos
import org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor
import org.lwjgl.glfw.GLFW.glfwGetVideoMode
import java.nio.IntBuffer

class HelloGPU {
  private var width = 300
  private var height = 300
  private val world = GlassOfWaterWithPencil()
  private val buffer = Array(width * height) { Triple(0, 0, 0) }
  private val renderer = AsyncRenderer(width, height, world, from = Point(2f, 4f, -5f), to = Point(2f, 0f, -1f), fieldOfView = TAU / 16)

  fun run() {
    val window = createWindow()
    val tex = createTexture(window)
    renderer.render { x, y, r, g, b ->
      buffer[x + (height - 1 - y) * width] = Triple(r, g, b)
    }
    loop(window, tex)

    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)

    // Terminate GLFW and free the error callback
    glfwTerminate()
    glfwSetErrorCallback(null)?.free()
  }

  private fun createWindow(): Long {
    GLFWErrorCallback.createPrint(System.err).set()
    if (!glfwInit()) throw IllegalStateException("Unable to initialize GLFW")

    // Configure GLFW
    glfwDefaultWindowHints() // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

    // Create the window
    val window: Long = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL)
    if (window == NULL) throw RuntimeException("Failed to create the GLFW window")

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window) { window, key, scancode, action, mods ->
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
        glfwSetWindowShouldClose(window, true) // We will detect this in the rendering loop
    }

    val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())
    glfwSetWindowPos(window, (vidmode!!.width() - width) / 2, (vidmode.height() - height) / 2)
    glfwMakeContextCurrent(window)
    glfwShowWindow(window)

    // Get the thread stack and push a new frame
    stackPush().use { stack ->
      val framebufferSize = stack.mallocInt(2)
      nglfwGetFramebufferSize(window, memAddress(framebufferSize), memAddress(framebufferSize) + 4)
    } // the stack frame is popped automatically

    GL.createCapabilities()

    // Make the OpenGL context current
    glfwMakeContextCurrent(window)
    // Enable v-sync
    glfwSwapInterval(1)
    // Make the window visible
    glfwShowWindow(window)

    return window
  }

  private fun createTexture(window: Long): Int {
    val texSize = 300
    // Set state and create all needed GL resources
    glEnable(GL_TEXTURE_2D)

    val tex = glGenTextures()
    glBindTexture(GL_TEXTURE_2D, tex)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, texSize, texSize, 0, GL_RGB, GL_UNSIGNED_BYTE, null as IntBuffer?)
    glBindTexture(GL_TEXTURE_2D, tex)

    val checkSize = 20
    val bb = BufferUtils.createByteBuffer(  3 * texSize * texSize)
    for (y in 0 until texSize) {
      for (x in 0 until texSize) {
        val byte = if ((x / checkSize + y / checkSize) % 2 == 0) 255.toByte() else 0.toByte()
        for (b in 0 until 3) bb.put(byte)
      }
    }
    bb.rewind()
    glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, texSize, texSize, GL_RGB, GL_UNSIGNED_BYTE, bb)

    return tex
  }

  private fun loop(window: Long, tex: Int) {
    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities()

    // Set the clear color
    glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while ( !glfwWindowShouldClose(window) ) {
      glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer

      refreshTexture()
      render()

      glfwSwapBuffers(window) // swap the color buffers

      // Poll for window events. The key callback above will only be
      // invoked during this call.
      glfwPollEvents()
    }
  }

  private fun refreshTexture() {
    val bb = BufferUtils.createByteBuffer(3 * width * height)
    for ((r, g, b) in buffer) {
      bb.put(r.toByte())
      bb.put(g.toByte())
      bb.put(b.toByte())
    }
    bb.rewind()
    glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, width, height, GL_RGB, GL_UNSIGNED_BYTE, bb)
  }

  private fun render() {
    glBegin(GL_QUADS)
    glTexCoord2f(0.0f, 0.0f)
    glVertex2f(-1.0f, -1.0f)
    glTexCoord2f(1.0f, 0.0f)
    glVertex2f(1.0f, -1.0f)
    glTexCoord2f(1.0f, 1.0f)
    glVertex2f(1.0f, 1.0f)
    glTexCoord2f(0.0f, 1.0f)
    glVertex2f(-1.0f, 1.0f)
    glEnd()
  }
}