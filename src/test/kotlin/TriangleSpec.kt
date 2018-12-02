import com.weberapps.ray.tracer.intersection.Intersection
import com.weberapps.ray.tracer.intersection.Intersections
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.shape.SmoothTriangle
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

  context("calculation of normal") {
    it("should find the normal of a triangle") {
      val triangle = Triangle(Point(0f, 1f, 0f), Point(-1f, 0f, 0f), Point(1f, 0f, 0f))
      val n1 = triangle.localNormal(Point(   0f,  0.5f, 0f))
      val n2 = triangle.localNormal(Point(-0.5f, 0.75f, 0f))
      val n3 = triangle.localNormal(Point( 0.5f, 0.25f, 0f))

      assertEquals(triangle.normal, n1)
      assertEquals(triangle.normal, n2)
      assertEquals(triangle.normal, n3)
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

  context("when smoothing normals") {
    val p1 = Point( 0f, 1f, 0f)
    val p2 = Point(-1f, 0f, 0f)
    val p3 = Point( 1f, 0f, 0f)
    val n1 = Vector( 0f, 1f, 0f)
    val n2 = Vector(-1f, 0f, 0f)
    val n3 = Vector( 1f, 0f, 0f)
    val tri = SmoothTriangle(p1, p2, p3, n1, n2, n3)

    it("should initialize a smooth triangle") {
      assertEquals(p1, tri.p1)
      assertEquals(p2, tri.p2)
      assertEquals(p3, tri.p3)

      assertEquals(n1, tri.n1)
      assertEquals(n2, tri.n2)
      assertEquals(n3, tri.n3)
    }

    it("should create and intersection that encapsulates u and v") {
      val s = Triangle(Point(0f, 1f, 0f), Point(-1f, 0f, 0f), Point(1f, 0f, 0f))
      val i = Intersection(3.5f, s, u = 0.2f, v = 0.4f)
      assertEquals(0.2f, i.u)
      assertEquals(0.4f, i.v)
    }

    it("should store the u/v value when intersecting a smooth triangle") {
      val r = Ray(Point(-0.2f, 0.3f, -2f), Vector(0f, 0f, 1f))
      val xs = tri.localIntersect(r)
      assertEquals(0.45f, xs[0].u)
      assertEquals(0.25f, xs[0].v)
    }

    it("should use u/v to interpolate the normal") {
      val i = Intersection(1f, tri, u = 0.45f, v = 0.25f)
      val n = tri.normal(Point(0f, 0f, 0f), i)
      assertEquals(Vector(-0.5547f, 0.83205f, 0f), n)
    }

    it("should prepare the normal of a smooth triangle") {
      val i = Intersection(1f, tri, u = 0.45f, v = 0.25f)
      val r = Ray(Point(-0.2f, 0.3f, -2f), Vector(0f, 0f, 1f))
      val xs = Intersections(i)
      val comps = i.prepareHit(r, xs)

      assertEquals(Vector(-0.5547f, 0.83205f, 0f), comps.normalVector)
    }
  }
})