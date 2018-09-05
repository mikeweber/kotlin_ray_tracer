package com.weberapps.rayTracer

import java.nio.file.Paths

class DrawScene(filename: String = "scene.ppm") {
    init {
        draw()
        var absolutePath = filename
        if (!filename.startsWith("/")) {
            absolutePath = Paths.get("").toAbsolutePath().toString() + "/" + filename
        }
        println("Saving circle to ${absolutePath}")
        PPMGenerator(draw()).save(absolutePath)
        println("Done")
    }

    fun draw(): Canvas {
        val squash = Transformation.scale(10f, 0.01f, 10f)
        val matte = Material(Color(1f, 0.9f, 0.9f), specular = 0f)
        val floor = Sphere(transform = squash, material = matte)

        val leftWallTransform = Transformation.translation(0f, 0f, 5f) *
                Transformation.rotateY(-TAU / 8) *
                Transformation.rotateX(TAU / 4) *
                Transformation.scale(10f, 0.01f, 10f)
        val leftWall = Sphere(transform = leftWallTransform, material = matte)

        val rightWallTransform = Transformation.translation(0f, 0f, 5f) *
                Transformation.rotateY(TAU / 8) *
                Transformation.rotateX(TAU / 4) *
                Transformation.scale(10f, 0.01f, 10f)
        val rightWall = Sphere(transform = rightWallTransform, material = matte)

        val middleTransform = Transformation.translation(-0.5f, 1f, 0.5f)
        val middleMaterial = Material(
                color = Color(0.1f, 1f, 0.5f),
                diffuse = 0.7f,
                specular = 0.3f
        )
        val middleSphere = Sphere(transform = middleTransform, material = middleMaterial)

        val rightTransform = Transformation.translation(1.5f, 0.5f, -0.5f) *
                Transformation.scale(0.5f, 0.5f, 0.5f)
        val rightMaterial = Material(
                color = Color(0.5f, 1f, 0.1f),
                diffuse = 0.7f,
                specular = 0.7f
        )
        val rightSphere = Sphere(transform = rightTransform, material = rightMaterial)

        val leftTransform = Transformation.translation(-1.5f, 0.5f, -0.5f) *
                Transformation.scale(0.5f, 0.5f, 0.5f)
        val leftMaterial = Material(
                color = Color(0.8f, 0.8f, 0.1f),
                diffuse = 0.7f,
                specular = 0.3f
        )
        val leftSphere = Sphere(transform = leftTransform, material = leftMaterial)

        val world = World(
                arrayListOf(floor, leftWall, rightWall, middleSphere, rightSphere, leftSphere),
                arrayListOf(Light(Point(-10f, 10f, -10f)))
        )

        val viewTransform = Transformation.viewTransform(
                Point(0f, 1.5f, -5f),
                Point(0f, 1f, 0f),
                Vector(-0.2f, 1f, 0f).normalize()
        )
        val camera = Camera(1600, 900, TAU / 6, transform = viewTransform)

        return camera.render(world)
    }
}