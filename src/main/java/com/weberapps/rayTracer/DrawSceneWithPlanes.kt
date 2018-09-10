package com.weberapps.rayTracer

import java.nio.file.Paths
import java.time.Duration
import java.time.Instant

class DrawSceneWithPlanes(filename: String = "scene_with_planes.ppm", val hsize: Int = 480, val vsize: Int = 270) {
    init {
        val t0 = Instant.now()
        var absolutePath = filename
        if (!filename.startsWith("/")) {
            absolutePath = Paths.get("").toAbsolutePath().toString() + "/" + filename
        }
        println("Saving render to $absolutePath")
        println("Beginning draw")
        val canvas = draw()
        println("Finished draw in ${Duration.between(t0, Instant.now())}")
        PPMGenerator(canvas).save(absolutePath)
        println("Finished save in ${Duration.between(t0, Instant.now())}")
        println("Done")
    }

    fun draw(): Canvas {
        val matte = SolidColor(Color(1f, 0.9f, 0.9f), specular = 0f)
        val mirror = ReflectiveMaterial()
        val floor = Plane(material = matte)

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

        val world = World(
                arrayListOf(floor, leftWall, rightWall, middleSphere, rightSphere, leftSphere),
                arrayListOf(
                        Light(Point(-10f, 10f, -10f))
                        // Light(Point( 10f, 10f, -10f), Color(0.05f, 0.05f, 0.05f))
                )
        )

        val viewTransform = Transformation.viewTransform(
                from = Point(3f, 2f, -5f),
                to = Point(0f, 1f, 0f),
                up = Vector(0f, 1f, 0f).normalize()
        )
        val camera = Camera(hsize, vsize, TAU / 6, transform = viewTransform)

        return camera.render(world)
    }
}