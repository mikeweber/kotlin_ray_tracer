import com.weberapps.ray.tracer.constants.EPSILON
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.math.floatRoot
import com.weberapps.ray.tracer.shape.Cone
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals

object ConeSpec: Spek({
  context("when intersecting a ray") {
    it("should intersect") {
      val shape = Cone()
      val examples = arrayListOf(
        Triple(Ray(Point(0f, 0f, -5f), Vector(   0f,  0f, 1f).normalize()), 5f, 5f),
        Triple(Ray(Point(0f, 0f, -5f), Vector(   1f,  1f, floatRoot(73f)).normalize()), 5.066612f, 5.069449f)
      )
      for ((ray, t1, t2) in examples) {
        val xs = shape.localIntersect(ray)
        assertEquals(2, xs.size)
        assertEquals(t1, xs[0].t)
        assertEquals(t2, xs[1].t)
      }
    }

    it("should intersect when the ray is parallel with one of the halves") {
      val shape = Cone()
      val ray = Ray(Point(0f, 0f, -1f), Vector(0f, 1f, 1f).normalize())
      val xs = shape.intersect(ray)
      assertEquals(1, xs.size)
      assertEquals(0.35355f, xs[0].t, EPSILON)
    }

    it("should intersect end-caps") {
      val shape = Cone(minimum = -0.5f, maximum = 0.5f, closed = true)
      val examples = arrayListOf(
        Pair(Ray(Point(0f, 0f,    -5f), Vector(0f, 1f, 0f)), 0),
        Pair(Ray(Point(0f, 0f, -0.25f), Vector(0f, 1f, 1f)), 2),
        Pair(Ray(Point(0f, 0f, -0.25f), Vector(0f, 1f, 0f)), 4)
      )
      for ((ray, count) in examples) {
        val xs = shape.localIntersect(ray)
        assertEquals(count, xs.size)
      }
    }
  }

  context("when calculating the normal") {
    it("should return the normals of the intersections") {
      val shape = Cone()
      val examples = arrayListOf(
        Pair(Point(0f, 0f, 0f), Vector(0f, 0f, 0f)),
        Pair(Point(1f, 1f, 1f), Vector(1f, -floatRoot(2f), 1f)),
        Pair(Point(-1f, -1f, 0f), Vector(-1f, 1f, 0f))
      )

      for ((point, normal) in examples) {
        assertEquals(normal, shape.localNormal(point))
      }
    }
  }
})