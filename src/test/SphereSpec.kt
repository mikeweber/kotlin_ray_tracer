import com.weberapps.rayTracer.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.assertEquals

object SphereSpec : Spek({
    context("intersection") {
        var sphere = Sphere()

        beforeEachTest {
            sphere = Sphere()
        }

        it("should intersect through the middle") {
            val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
            val xs = sphere.intersect(ray)
            assertEquals(2, xs.size)
            assertEquals(4f, xs[0].t)
            assertEquals(6f, xs[1].t)
            assertEquals(sphere, xs[0].shape)
            assertEquals(sphere, xs[1].shape)
        }

        it("should intersect at the tangent") {
            val ray = Ray(Point(0f, 1f, -5f), Vector(0f, 0f, 1f))
            val xs = sphere.intersect(ray)
            assertEquals(2, xs.size)
            assertEquals(5f, xs[0].t)
            assertEquals(5f, xs[1].t)
        }

        it("should not have any intersections") {
            val ray = Ray(Point(0f, 2f, -5f), Vector(0f, 0f, 1f))
            val xs = sphere.intersect(ray)
            assertEquals(0, xs.size)
        }

        it("should intersect when originating in the sphere") {
            val ray = Ray(Point(0f, 0f, 0f), Vector(0f, 0f, 1f))
            val xs = sphere.intersect(ray)
            assertEquals(2, xs.size)
            assertEquals(-1f, xs[0].t)
            assertEquals( 1f, xs[1].t)
        }

        it("should intersect when the sphere is behind the ray") {
            val ray = Ray(Point(0f, 0f, 5f), Vector(0f, 0f, 1f))
            val xs = sphere.intersect(ray)
            assertEquals(2, xs.size)
            assertEquals(-6f, xs[0].t)
            assertEquals(-4f, xs[1].t)
        }

        it("should intersect a scaled sphere") {
            val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
            sphere.transform = Transformation.scale(2f, 2f, 2f)
            val xs = sphere.intersect(ray)

            assertEquals(2, xs.size)
            assertEquals(3f, xs[0].t)
            assertEquals(7f, xs[1].t)
        }

        it("should not intersect a translated sphere") {
            val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
            sphere.transform = Transformation.translation(5f, 0f, 0f)
            val xs = sphere.intersect(ray)

            assertEquals(0, xs.size)
        }

        it("should intersect a translated sphere") {
            val ray = Ray(Point(5f, 0f, -5f), Vector(0f, 0f, 1f))
            sphere.transform = Transformation.translation(5f, 0f, 0f)
            val xs = sphere.intersect(ray)

            assertEquals(2, xs.size)
            assertEquals(4f, xs[0].t)
            assertEquals(6f, xs[1].t)
        }
    }

    context("transformation") {
        var sphere = Sphere()
        beforeEachTest {
            sphere = Sphere()
        }

        it("should be able to change the transform") {
            val t = Transformation.translation(2f, 3f, 4f)
            sphere.transform = t
            assertEquals(t, sphere.transform)
        }

        it("should return the identity matrix by default") {
            assertEquals(Matrix.eye(4), sphere.transform)
        }
    }
})