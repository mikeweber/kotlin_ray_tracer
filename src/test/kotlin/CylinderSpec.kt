import com.weberapps.ray.tracer.constants.EPSILON
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.shape.Cylinder
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals

object CylinderSpec: Spek ({
  val cyl = Cylinder()

  context("when intersecting rays") {
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
    it("should return the point with 0 at the y component") {
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
  }
})