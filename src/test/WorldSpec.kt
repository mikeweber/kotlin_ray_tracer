import com.weberapps.rayTracer.*
import junit.framework.TestCase.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

object WorldSpec: Spek({
    context("initialization") {
        val w = World()

        it("should start with empty lists") {
            assertEquals(0, w.sceneObjects.size)
            assertEquals(0, w.lightSources.size)
        }
    }

    context("default world") {
        val world = World.defaultWorld()

        it("should build a world with two objects and a single light source") {
            val s1 = Sphere(material = Material(color = Color(0.8f, 1f, 0.6f), diffuse = 0.7f, specular = 0.2f))
            val s2 = Sphere(Transformation.scale(0.5f, 0.5f, 0.5f))
            val light = Light(Point(-10f, 10f,-10f), Color(1f, 1f, 1f))

            assertEquals(2, world.sceneObjects.size)
            assertEquals(1, world.lightSources.size)
            assert(world.sceneObjects.contains(s1))
            assert(world.sceneObjects.contains(s2))
            assert(world.lightSources.contains(light))
        }

        it("should return four intersections when a ray goes through the origin") {
            val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
            val xs = world.intersect(ray)
            assertEquals(4, xs.size)
            assertEquals(  4f, xs[0].t, EPSILON)
            assertEquals(4.5f, xs[1].t, EPSILON)
            assertEquals(5.5f, xs[2].t, EPSILON)
            assertEquals(  6f, xs[3].t, EPSILON)
        }
    }
})