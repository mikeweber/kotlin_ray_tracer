package com.weberapps.rayTracer

import java.nio.file.Paths
import java.time.Duration
import java.time.Instant
import kotlin.math.PI

class DrawSceneWithPlanes(var filename: String, val hsize: Int = 160, val vsize: Int = 90) {
    init {
        val t0 = Instant.now()
        saveCanvas(renderFocus())
        println("Finished in ${Duration.between(t0, Instant.now())}")
    }

    fun saveCanvas(canvas: Canvas, saveAs: String = filename) {
        var absolutePath = saveAs
        if (!absolutePath.startsWith("/")) {
            absolutePath = Paths.get("").toAbsolutePath().toString() + "/" + saveAs
        }
        println("Saving render to $absolutePath")
        PPMGenerator(canvas).save(absolutePath)
    }

    fun drawAnimation(t0: Instant = Instant.now()) {
        val world = initWorld()
        println("Beginning draw")
        val sphere = world.sceneObjects[0]
        for (i in 0..29) {
            val t1 = Instant.now()
            filename = "step$i.ppm"
            val velocity = 0.1f
            val dt = 1f
            val theta = velocity * dt / PI * 2
            sphere.transform = Transformation.translation(dt * velocity, 0f, 0f) * sphere.transform * Transformation.rotateZ(-theta)
            var absolutePath = filename
            if (!filename.startsWith("/")) {
                absolutePath = Paths.get("").toAbsolutePath().toString() + "/steps/" + filename
            }
            println("Saving render $i to $absolutePath")
            val canvas = render(world)
            println("Finished draw $i in ${Duration.between(t1, Instant.now())}")
            val t2 = Instant.now()
            PPMGenerator(canvas).save(absolutePath)
            println("Finished save in ${Duration.between(t2, Instant.now())}")
        }
        println("Finished animation in ${Duration.between(t0, Instant.now())}")
    }

    fun renderFocus(): Canvas {
        var canvas = Canvas(hsize, vsize)
        val world = initWorld()
        val focus = Point(world.sceneObjects[0].transform * Point(0f, 0f, 0f))
        var renderCount = 0
        val spacing = 0.03f
        for (i in -1..1) {
            for (j in -1..1) {
                val t0 = Instant.now()
                canvas += render(world, from = Point(i * spacing, 2f + j * spacing, -5f), focus = focus)
                println("Finished render $renderCount in ${Duration.between(t0, Instant.now())}")
                renderCount++
            }
        }
        return canvas / renderCount.toFloat()
    }

    fun render(
            world: World,
            from: Point = Point(0f, 2f, -5f),
            focus: Point = Point(0f, 1f, 0f)
    ): Canvas {
        val viewTransform = Transformation.viewTransform(
                from = from,
                to = focus,
                up = Vector(0f, 1f, 0f).normalize()
        )
        val camera = Camera(hsize, vsize, TAU / 6, transform = viewTransform)

        return camera.render(world)
    }

    fun initWorld(): World {
        val matte = SolidColor(Color(1f, 0.9f, 0.9f), specular = 0f)
        val mirror = ReflectiveMaterial()
        val floor = Plane(material = matte)

        val backWallTransform = Transformation.rotateX(TAU / 4)
        val backWall = Plane(transform = backWallTransform, material = mirror)
        val leftWallTransform = Transformation.translation(0f, 0f, 5f) *
                Transformation.rotateY(-TAU / 8) *
                Transformation.rotateX(TAU / 4)
        val leftWall = Plane(transform = leftWallTransform, material = mirror)

        val rightWallTransform = Transformation.translation(0f, 0f, 5f) *
                Transformation.rotateY(TAU / 8) *
                Transformation.rotateX(TAU / 4)
        val rightWall = Plane(transform = rightWallTransform, material = matte)

        val middleTransform = Transformation.translation(-0.5f, 1f, 0.5f)
        val middleMaterialTransform =
                Transformation.translation(0.05f, 0f, 0f) *
                        Transformation.rotateY(-TAU / 8) *
                        Transformation.rotateZ(-TAU / 16) *
                        Transformation.scale(0.1f, 0.1f, 0.1f)
        val middleMaterial = StripePattern(
                zig = Color(0.1f, 1f, 0.5f),
                zag = Color(0.6f, 1f, 0.7f),
                transform = middleMaterialTransform,
                diffuse = 0.7f,
                specular = 0.3f
        )
        val middleSphere = Sphere(transform = middleTransform, material = middleMaterial)

        val rightTransform = Transformation.translation(1.5f, 0.5f, -0.5f) *
                Transformation.scale(0.5f, 0.5f, 0.5f)
        val rightMaterial = SolidColor(
                color = Color(0.5f, 1f, 0.1f),
                diffuse = 0.7f,
                specular = 0.7f
        )
        val rightSphere = Sphere(transform = rightTransform, material = rightMaterial)

        val leftTransform = Transformation.translation(-1.75f, 0.5f, -0.5f) *
                Transformation.scale(0.5f, 0.5f, 0.5f)
        val leftMaterial = SolidColor(
                color = Color(1f, 0.7f, 0f),
                diffuse = 0.7f,
                specular = 0.3f
        )
        val leftSphere = Sphere(transform = leftTransform, material = leftMaterial)

        val glassTransform1 = Transformation.translation(1f, 1f, -2f) *
                Transformation.scale(1f, 1f, 1f)
        val glass = TransparentMaterial(1.3f)
        val glassSphere1 = Sphere(transform = glassTransform1, material = glass)

        val mirrorTransform = Transformation.translation(-1.0f, 0.8f, 4.7f) *
                Transformation.scale(0.8f, 0.8f, 0.8f)
        val mirrorSphere = Sphere(transform = mirrorTransform, material = mirror)

        return World(
                arrayListOf(middleSphere, floor, rightSphere, leftSphere, rightWall, leftWall),
                arrayListOf(
                        Light(Point(-10f, 10f, -10f))
                        // Light(Point( 10f, 10f, -10f), Color(0.05f, 0.05f, 0.05f))
                )
        )
    }
}