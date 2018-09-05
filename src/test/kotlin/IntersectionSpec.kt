import com.weberapps.rayTracer.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.*

class IntersectionSpec : Spek({
    context("initialization") {
        it("should have the object intersected and when") {
            val sphere = Sphere()
            val i = Intersection(3.5f, sphere)
            assertEquals(3.5f, i.t)
            assertEquals(sphere, i.shape)
        }
    }

    context("precompute hits") {
        it("should precompute the state of an intersection") {
            val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
            val sphere = Sphere()
            val hit = Intersection(4f, sphere)
            hit.prepareHit(ray)
            assertEquals(Point(0f, 0f, -1.0001f), hit.point)
            assertEquals(Vector(0f, 0f, -1f), hit.eyeVector)
            assertEquals(Vector(0f, 0f, -1f), hit.normalVector)
        }

        it("should set the inside flag to false when the ray originates outside the shape") {
            val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
            val sphere = Sphere()
            val hit = Intersection(4f, sphere)
            hit.prepareHit(ray)
            assertFalse(hit.inside)
        }

        it("should set the inside flag to true when the ray originates inside the shape") {
            val ray = Ray(Point(0f, 0f, 0f), Vector(0f, 0f, 1f))
            val sphere = Sphere()
            val hit = Intersection(1f, sphere)
            hit.prepareHit(ray)
            assertEquals(Point(0f, 0f, 0.9999f), hit.point)
            assertEquals(Vector(0f, 0f, -1f), hit.eyeVector)
            assertTrue(hit.inside)
            assertEquals(Vector(0f, 0f, -1f), hit.normalVector)
        }

        it("should return a point slightly above the normal of the point") {
            val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
            val sphere = Sphere()
            val hit = Intersection(4f, sphere)
            hit.prepareHit(ray)
            assertTrue(hit.point.z < -1f)
            assertTrue(hit.point.z > -1.1f)
        }
    }
})