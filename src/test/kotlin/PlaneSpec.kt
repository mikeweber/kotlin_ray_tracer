import com.weberapps.rayTracer.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.assertEquals

object PlaneSpec : Spek({
  context("localNormal") {
    it("should have the same normal everywhere") {
      val p = Plane()
      val n1 = p.localNormal(Point(0f, 0f, 0f))
      val n2 = p.localNormal(Point(10f, 0f, -10f))
      val n3 = p.localNormal(Point(-5f, 0f, 150f))

      assertEquals(Vector(0f, 1f, 0f), n1)
      assertEquals(Vector(0f, 1f, 0f), n2)
      assertEquals(Vector(0f, 1f, 0f), n3)
    }
  }

  context("localIntersect") {
    val p = Plane()

    it("should not intersect when the ray is parallel") {
      val r = Ray(Point(0f, 10f, 0f), Vector(0f, 0f, 1f))
      val xs = p.localIntersect(r)
      assertEquals(0, xs.size)
    }

    it("should not count as an intersect when the ray is co-planar") {
      val r = Ray(Point(0f, 0f, 0f), Vector(0f, 0f, 1f))
      val xs = p.localIntersect(r)
      assertEquals(0, xs.size)
    }

    it("should intersect from above the plane") {
      val r = Ray(Point(0f, 1f, 0f), Vector(0f, -1f, 0f))
      val xs = p.localIntersect(r)
      assertEquals(1, xs.size)
      assertEquals(1f, xs[0].t)
      assertEquals(p, xs[0].shape)
    }

    it("should intersect from below the plane") {
      val r = Ray(Point(0f, -1f, 0f), Vector(0f, 1f, 0f))
      val xs = p.localIntersect(r)
      assertEquals(1, xs.size)
      assertEquals(1f, xs[0].t)
      assertEquals(p, xs[0].shape)
    }
  }
})
