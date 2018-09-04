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
            val s1 = Sphere(material = Material(color = Color(0.8f, 1f, 0.6f), diffuse = 0.7f, specular = 0.2f))
            val s2 = Sphere(transform = Transformation.scale(0.5f, 0.5f, 0.5f))
            val light = Light(Point(-10f, 10f, -10f), Color(1f, 1f, 1f))

            assertEquals(2, world.sceneObjects.size)
            assertEquals(1, world.lightSources.size)
            assertTrue(world.sceneObjects.contains(s1))
            assertTrue(world.sceneObjects.contains(s2))
            assertTrue(world.lightSources.contains(light))
        }
    }

    context("ray casting") {
        val world = World.default()

        it("should intersect every object in a scene") {
            val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
            val xs = world.intersect(ray)
            assertEquals(4, xs.size)
            assertEquals(  4f, xs[0].t)
            assertEquals(4.5f, xs[1].t)
            assertEquals(5.5f, xs[2].t)
            assertEquals(  6f, xs[3].t)
        }
    }
})