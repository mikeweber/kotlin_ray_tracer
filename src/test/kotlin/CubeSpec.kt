import com.weberapps.rayTracer.Cube
import com.weberapps.rayTracer.Point
import com.weberapps.rayTracer.Ray
import com.weberapps.rayTracer.Vector
import junit.framework.Assert.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it

object CubeSpec: Spek({
  context("when intersecting a cube with a ray") {
    it("should return multiple points") {
      val c = Cube()
      val examples = arrayListOf<Pair<Point, Vector>>(
        Pair(Point(  5f, 0.5f,  0f), Vector(-1f,  0f,  0f)),
        Pair(Point( -5f, 0.5f,  0f), Vector( 1f,  0f,  0f)),
        Pair(Point(0.5f,   5f,  0f), Vector( 0f, -1f,  0f)),
        Pair(Point(0.5f,  -5f,  0f), Vector( 0f,  1f,  0f)),
        Pair(Point(0.5f,   0f,  5f), Vector( 0f,  0f, -1f)),
        Pair(Point(0.5f,   0f, -5f), Vector( 0f,  0f,  1f)),
        Pair(Point(  0f, 0.5f,  0f), Vector( 0f,  0f,  1f))
      )
      val expectations = arrayListOf<Pair<Float, Float>>(
        Pair( 4f, 6f),
        Pair( 4f, 6f),
        Pair( 4f, 6f),
        Pair( 4f, 6f),
        Pair( 4f, 6f),
        Pair( 4f, 6f),
        Pair(-1f, 1f)
      )
      for ((index, example) in examples.iterator().withIndex()) {
        val (origin, direction) = example
        val r = Ray(origin, direction)

        val (expectation1, expectation2) = expectations[index]
        val xs = c.localIntersect(r)
        assertEquals(2, xs.size)
        assertEquals(expectation1, xs[0].t)
        assertEquals(expectation2, xs[1].t)
      }
    }
  }
})