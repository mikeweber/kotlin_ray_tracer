import com.weberapps.ray.tracer.constants.EPSILON
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.shape.Cylinder
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.lang.Float.NEGATIVE_INFINITY
import java.lang.Float.POSITIVE_INFINITY

object CylinderSpec: Spek ({
  context("when intersecting rays") {
    val cyl = Cylinder()

    it("should be able to miss") {
      val examples = arrayListOf(
        Ray(Point( 1f,  0f,  0f), Vector(0f, 1f, 0f).normalize()),
        Ray(Point( 0f,  0f,  0f), Vector(0f, 1f, 0f).normalize()),
        Ray(Point( 0f,  0f, -5f), Vector(1f, 1f, 1f).normalize())
      )

      for (ray in examples) {
        val xs = cyl.localIntersect(ray)
        assertEquals(0, xs.size)
      }
    }

    it("should return intersections") {
      val examples = arrayListOf(
        Pair(Ray(Point(  1f, 0f, -5f), Vector(  0f, 0f, 1f).normalize()), Pair(      5f,       5f)),
        Pair(Ray(Point(  0f, 0f, -5f), Vector(  0f, 0f, 1f).normalize()), Pair(      4f,       6f)),
        Pair(Ray(Point(0.5f, 0f, -5f), Vector(0.1f, 1f, 1f).normalize()), Pair(6.808f, 7.08869f))
      )

      for ((ray, ts) in examples) {
        val (t0, t1) = ts
        val xs = cyl.localIntersect(ray)

        assertEquals(2, xs.size)
        assertEquals(t0, xs[0].t, EPSILON)
        assertEquals(t1, xs[1].t, EPSILON)
      }
    }
  }

  context("when calculating normal") {
    it("should return the point perpendicular to the y axis") {
      val cyl = Cylinder()
      val examples = arrayListOf(
        Pair(Point( 1f,  0f,  0f), Vector( 1f, 0f,  0f)),
        Pair(Point( 0f,  5f, -1f), Vector( 0f, 0f, -1f)),
        Pair(Point( 0f, -2f,  1f), Vector( 0f, 0f,  1f)),
        Pair(Point(-1f,  1f,  0f), Vector(-1f, 0f,  0f))
      )

      for ((point, normal) in examples) {
        assertEquals(normal, cyl.localNormal(point))
      }
    }

    it("should return a vector pointing in the y direction") {
      val cyl = Cylinder(minimum = 1f, maximum = 2f, closed = true)
      val examples = arrayListOf(
        Pair(Point(  0f, 1f,   0f), Vector(0f, -1f, 0f)),
        Pair(Point(0.5f, 1f,   0f), Vector(0f, -1f, 0f)),
        Pair(Point(  0f, 1f, 0.5f), Vector(0f, -1f, 0f)),
        Pair(Point(  0f, 2f,   0f), Vector(0f,  1f, 0f)),
        Pair(Point(0.5f, 2f,   0f), Vector(0f,  1f, 0f)),
        Pair(Point(  0f, 2f, 0.5f), Vector(0f,  1f, 0f))
      )

      for ((point, normal) in examples) {
        assertEquals(normal, cyl.localNormal(point))
      }
    }
  }

  context("truncating cylinders") {
    it("should have no minimum or maximum by default") {
      val cyl = Cylinder()
      assertEquals(POSITIVE_INFINITY, cyl.maximum)
      assertEquals(NEGATIVE_INFINITY, cyl.minimum)
    }

    it("should be able to set a minimum and maximum") {
      val cyl = Cylinder(maximum = 2f, minimum = 1f)
      val examples = arrayListOf(
        Pair(Ray(Point(0f, 1.5f,  0f), Vector(0.1f, 1f, 0f).normalize()), 0),
        Pair(Ray(Point(0f,   3f, -5f), Vector(  0f, 0f, 1f)), 0),
        Pair(Ray(Point(0f,   0f, -5f), Vector(  0f, 0f, 1f)), 0),
        Pair(Ray(Point(0f,   2f, -5f), Vector(  0f, 0f, 1f)), 0),
        Pair(Ray(Point(0f,   1f, -5f), Vector(  0f, 0f, 1f)), 0),
        Pair(Ray(Point(0f, 1.5f, -2f), Vector(  0f, 0f, 1f)), 2)
      )

      for ((ray, count) in examples) {
        val xs = cyl.localIntersect(ray)
        assertEquals(count, xs.size)
      }
    }

    it("should be able to be capped") {
      val cyl = Cylinder(maximum = 1f, minimum = 0f, closed = true)
      assertTrue(cyl.closed)
    }

    it("should intersect the capped cylinder") {
      val cyl = Cylinder(maximum = 2f, minimum = 1f, closed = true)
      val examples = arrayListOf(
        Pair(Ray(Point(0f,  3f,  0f), Vector(0f, -1f, 0f).normalize()), 2),
        Pair(Ray(Point(0f,  3f, -2f), Vector(0f, -1f, 2f).normalize()), 2),
        Pair(Ray(Point(0f,  3.99f, -2f), Vector(0f, -1f, 1f).normalize()), 2), // corner case
        Pair(Ray(Point(0f,  0f, -2f), Vector(0f,  1f, 2f).normalize()), 2),
        Pair(Ray(Point(0f, -0.99f, -2f), Vector(0f,  1f, 1f).normalize()), 2)  // corner case
      )

      for ((ray, count) in examples) {
        val xs = cyl.localIntersect(ray)
        assertEquals(count, xs.size)
      }
    }
  }
})
