import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.shape.Triangle
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals

object TriangleSpec: Spek({
  context("initialization") {
    it("should take 3 points") {
      val p1 = Point( 0f, 1f, 0f)
      val p2 = Point(-1f, 0f, 0f)
      val p3 = Point( 1f, 0f, 0f)
      val t = Triangle(p1, p2, p3)

      assertEquals(p1, t.p1)
      assertEquals(p2, t.p2)
      assertEquals(p3, t.p3)
      assertEquals(Vector(-1f, -1f, 0f), t.e1)
    }
  }

  context("ray intersections") {
    val t = Triangle(Point(0f, 1f, 0f), Point(-1f, 0f, 0f), Point(1f, 0f, 0f))

    it("should miss when the ray is parallel") {
      val r = Ray(Point(0f, -1f, -2f), Vector(0f, 1f, 0f))
      val xs = t.localIntersect(r)
      assertEquals(0, xs.size)
    }

    it("should miss when aimed towards the p1-p3 edge") {
      val r = Ray(Point(1f, 1f, -2f), Vector(0f, 0f, 1f))
      val xs = t.localIntersect(r)
      assertEquals(0, xs.size)
    }

    it("should miss when aimed towards the p1-p2 edge") {
      val r = Ray(Point(-1f, 1f, -2f), Vector(0f, 0f, 1f))
      val xs = t.localIntersect(r)
      assertEquals(0, xs.size)
    }

    it("should miss when aimed towards the p2-p3 edge") {
      val r = Ray(Point(0f, -1f, -2f), Vector(0f, 0f, 1f))
      val xs = t.localIntersect(r)
      assertEquals(0, xs.size)
    }

    it("should hit when aimed at the triangle") {
      val r = Ray(Point(0f, 0.5f, -2f), Vector(0f, 0f, 1f))
      val xs = t.localIntersect(r)
      assertEquals(1, xs.size)
      assertEquals(2f, xs[0].t)
    }
  }
})