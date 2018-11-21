import com.sun.management.VMOption
import com.weberapps.rayTracer.Cube
import com.weberapps.rayTracer.Point
import com.weberapps.rayTracer.Ray
import com.weberapps.rayTracer.Vector
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals

object CubeSpec: Spek({
  context("when intersecting rays") {
    val c = Cube()

    it("should return the expected intersections") {
      val examples = arrayListOf(
        Pair(Ray(Point(  5f, 0.5f,   0f), Vector(-1f,  0f,  0f)), Pair( 4f, 6f)),
        Pair(Ray(Point( -5f, 0.5f,   0f), Vector( 1f,  0f,  0f)), Pair( 4f, 6f)),
        Pair(Ray(Point(0.5f,   5f,   0f), Vector( 0f, -1f,  0f)), Pair( 4f, 6f)),
        Pair(Ray(Point(0.5f,  -5f,   0f), Vector( 0f,  1f,  0f)), Pair( 4f, 6f)),
        Pair(Ray(Point(0.5f,   0f,   5f), Vector( 0f,  0f, -1f)), Pair( 4f, 6f)),
        Pair(Ray(Point(0.5f,   0f,  -5f), Vector( 0f,  0f,  1f)), Pair( 4f, 6f)),
        Pair(Ray(Point(  0f, 0.5f,   0f), Vector( 0f,  0f,  1f)), Pair(-1f, 1f))
      )

      for (example in examples) {
        val (r, pair2) = example
        val (t1, t2) = pair2

        val xs = c.localIntersect(r)
        assertEquals(2, xs.size)
        assertEquals(t1, xs[0].t)
        assertEquals(t2, xs[1].t)
      }
    }

    it("should have no intersections when the ray misses the cube") {
      val examples = arrayListOf(
        Ray(Point(-2f,  0f,  0f), Vector(0.2673f, 0.5345f, 0.8018f)),
        Ray(Point( 0f, -2f,  0f), Vector(0.8018f, 0.2673f, 0.5345f)),
        Ray(Point( 0f,  0f, -2f), Vector(0.5345f, 0.8018f, 0.2673f)),
        Ray(Point( 2f,  0f,  2f), Vector(     0f,      0f,     -1f)),
        Ray(Point( 0f,  2f,  2f), Vector(     0f,     -1f,      0f)),
        Ray(Point( 2f,  2f,  0f), Vector(    -1f,      0f,      0f))
      )

      for (ray in examples) {
        val xs = c.localIntersect(ray)
        assertEquals(0, xs.size)
      }
    }
  }

  context("calculating normal") {
    val c = Cube()

    it("should return the normal of the surface of a cube") {
      val examples = arrayListOf(
        Pair(Point(   1f,  0.5f, -0.8f), Vector( 1f,  0f,  0f)),
        Pair(Point(  -1f, -0.2f,  0.9f), Vector(-1f,  0f,  0f)),
        Pair(Point(-0.4f,    1f, -0.1f), Vector( 0f,  1f,  0f)),
        Pair(Point( 0.3f,   -1f, -0.7f), Vector( 0f, -1f,  0f)),
        Pair(Point(-0.6f,  0.3f,    1f), Vector( 0f,  0f,  1f)),
        Pair(Point( 0.4f,  0.4f,   -1f), Vector( 0f,  0f, -1f)),
        Pair(Point(   1f,    1f,    1f), Vector( 1f,  0f,  0f)),
        Pair(Point(  -1f,   -1f,   -1f), Vector(-1f,  0f,  0f))
      )

      for (example in examples) {
        val (point, normal) = example
        assertEquals(normal, c.localNormal(point))
      }
    }
  }
})