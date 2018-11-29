import com.weberapps.ray.tracer.intersection.Intersection
import com.weberapps.ray.tracer.intersection.Intersections
import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.shape.Sphere
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

  context("normal") {
    var s = Sphere()
    beforeEachTest {
      s = Sphere()
    }

    it("should return a point on the X axis") {
      val n = s.normal(Point(1f, 0f, 0f))
      assertEquals(Vector(1f, 0f, 0f), n)
    }

    it("should return a point on the Y axis") {
      val n = s.normal(Point(0f, 1f, 0f))
      assertEquals(Vector(0f, 1f, 0f), n)
    }

    it("should return a point on the Z axis") {
      val n = s.normal(Point(0f, 0f, 1f))
      assertEquals(Vector(0f, 0f, 1f), n)
    }

    it("should be able to return a non-axial point") {
      val f = (Math.sqrt(3.0) / 3).toFloat()
      val n = s.normal(Point(f, f, f))
      assertEquals(Vector(f, f, f), n)
    }

    it("should be a normalized vector") {
      val f = (Math.sqrt(3.0) / 3).toFloat()
      val n = s.normal(Point(f, f, f))
      assertEquals(n.normalize(), n)
    }

    it("should be able to be calculated for a translated sphere") {
      s.transform = Transformation.translation(0f, 5f, 0f)
      val n = s.normal(Point(1f, 5f, 0f))
      assertEquals(Vector(1f, 0f, 0f), n)
    }

    it("should be able to be calculated for a scaled sphere") {
      s.transform = Transformation.scale(1f, 0.5f, 1f)
      val f = (Math.sqrt(2.0) / 2).toFloat()
      val n = s.normal(Point(0f, f, -f))
      assertEquals(Vector(0f, 0.97014f, -0.24254f), n)
    }
  }

  context("material property") {
    it("has a default material") {
      val s = Sphere()
      assertEquals(Material(), s.material)
    }

    it("can be assigned a material") {
      val s = Sphere()
      val m = Material(ambient = 0.3f)
      s.material = m
      assertEquals(m, s.material)
    }
  }

  context("glass sphere") {
    it("correctly calculates n1 and n2 at various intersections") {
      val aTransform = Transformation.scale(2f, 2f, 2f)
      val aMaterial = Material(refractiveIndex = 1.5f)
      val bTransform = Transformation.translation(0f, 0f, -0.25f)
      val bMaterial = Material(refractiveIndex = 2f)
      val cTransform = Transformation.translation(0f, 0f, 0.25f)
      val cMaterial = Material(refractiveIndex = 2.5f)

      val a = Sphere(aTransform, aMaterial)
      val b = Sphere(bTransform, bMaterial)
      val c = Sphere(cTransform, cMaterial)

      val ray = Ray(Point(0f, 0f, -4f), Vector(0f, 0f, 1f))
      val xs = Intersections(
        6,
        arrayListOf(
          Intersection(2.00f, a),
          Intersection(2.75f, b),
          Intersection(3.25f, c),
          Intersection(4.75f, b),
          Intersection(5.25f, c),
          Intersection(6.00f, a)
        )
      )
      val expectedValues = arrayListOf(
        floatArrayOf(1.00029f, 1.5f),
        floatArrayOf(1.5f, 2.0f),
        floatArrayOf(2.0f, 2.5f),
        floatArrayOf(2.5f, 2.5f),
        floatArrayOf(2.5f, 1.5f),
        floatArrayOf(1.5f, 1.00029f)
      )
      for ((index, hit) in xs.iterator().withIndex()) {
        val comps = hit.prepareHit(ray, xs)
        assertEquals(expectedValues[index][0], comps.n1)
        assertEquals(expectedValues[index][1], comps.n2)
      }
    }
  }
})
