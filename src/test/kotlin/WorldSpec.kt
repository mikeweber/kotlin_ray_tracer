import com.weberapps.rayTracer.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.*

object WorldSpec: Spek({
    context("initialization") {
        it("should be empty") {
            val world = World()
            assertEquals(0, world.sceneObjects.size)
            assertEquals(0, world.lightSources.size)
        }
    }

    context("defaultWorld") {
        val world = World.default()

        it("should have two objects and a single light source") {
            val s1 = Sphere(material = SolidColor(color = Color(0.8f, 1f, 0.6f), diffuse = 0.7f, specular = 0.2f))
            val s2 = Sphere(transform = Transformation.scale(0.5f, 0.5f, 0.5f))
            val light = Light(Point(-10f, 10f, -10f), Color.WHITE)

            assertEquals(2, world.sceneObjects.size)
            assertEquals(1, world.lightSources.size)
            assertTrue(world.sceneObjects.contains(s1))
            assertTrue(world.sceneObjects.contains(s2))
            assertTrue(world.lightSources.contains(light))
        }
    }

    context("ray casting") {
        var world = World.default()
        beforeEachTest {
            world = World.default()
        }

        it("should intersect every object in a scene") {
            val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
            val xs = world.intersect(ray)
            assertEquals(4, xs.size)
            assertEquals(  4f, xs[0].t)
            assertEquals(4.5f, xs[1].t)
            assertEquals(5.5f, xs[2].t)
            assertEquals(  6f, xs[3].t)
        }

        it("should shade an intersection") {
            val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
            val shape = world.sceneObjects[0]
            val hit = Intersection(4f, shape)
            hit.prepareHit(ray)
            val c = world.shadeHit(hit)

            assertEquals(Color(0.38066f, 0.47583f, 0.2855f), c)
        }

        it("should shade an intersection from the inside") {
            world.lightSources[0] = Light(Point(0f, 0.25f, 0f))
            val ray = Ray(Point(0f, 0f, 0f), Vector(0f, 0f, 1f))
            val shape = world.sceneObjects[1]
            val hit = Intersection(0.5f, shape)
            hit.prepareHit(ray)
            val c = world.shadeHit(hit)

            assertEquals(Color(0.90495f, 0.90495f, 0.90495f), c)
        }

        it("should return black when a ray misses") {
            val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 1f, 0f))
            val c = world.colorAt(ray)
            assertEquals(Color.BLACK, c)
        }

        it("should return a color when the ray hits") {
            val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
            val c = world.colorAt(ray)
            assertEquals(Color(0.38066f, 0.47583f, 0.2855f), c)
        }
    }

    context("when determining if a point is shadowed") {
        val world = World.default()
        val light = world.lightSources[0]

        it("should not be shadowed when nothing is colinear with a point and light") {
            val p = Point(0f, 10f, 0f)
            assertFalse(world.isShadowed(light, p))
        }

        it("should be shadowed when the point is behind an object")  {
            val p = Point(10f, -10f, 10f)
            assertTrue(world.isShadowed(light, p))
        }

        it("should not be shadowed when the light is between the point and object") {
            val p = Point(-20f, 20f, -20f)
            assertFalse(world.isShadowed(light, p))
        }

        it("should not be shadowed when the point is betwen the light and the object") {
            val p = Point(-2f, 2f, -2f)
            assertFalse(world.isShadowed(light, p))
        }
    }

    context("when shadeHit is given an intersection in a shadow") {
        it("should only return the ambient color") {
            val light = Light(Point(0f, 0f, -10f))
            val s1 = Sphere()
            val s2Transform = Transformation.translation(0f, 0f, 10f)
            val s2 = Sphere(transform = s2Transform)
            val world = World(sceneObjects = arrayListOf(s1, s2), lightSources = arrayListOf(light))
            val ray = Ray(Point(0f, 0f, 5f), Vector(0f, 0f, 1f))
            val hit = Intersection(4f, s2)
            hit.prepareHit(ray)
            val color = world.shadeHit(hit)

            assertEquals(Color(0.1f, 0.1f, 0.1f), color)
        }
    }
})