import com.weberapps.ray.tracer.intersection.Intersection
import com.weberapps.ray.tracer.intersection.Intersections
import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.renderer.World
import com.weberapps.ray.tracer.shape.Plane
import com.weberapps.ray.tracer.shape.Sphere
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.*

object IntersectionSpec : Spek({
  context("initialization") {
    it("should have the object intersected and when") {
      val sphere = Sphere()
      val i = Intersection(3.5f, sphere)
      assertEquals(3.5f, i.t)
      assertEquals(sphere, i.shape)
    }
  }

  context("precompute hits") {
    it("should precompute the state of an intersection") {
      val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
      val sphere = Sphere()
      val hit = Intersection(4f, sphere)
      val comps = hit.prepareHit(ray)
      assertEquals(Point(0f, 0f, -1.0001f), comps.point)
      assertEquals(Vector(0f, 0f, -1f), comps.eyeVector)
      assertEquals(Vector(0f, 0f, -1f), comps.normalVector)
    }

    it("should set the inside flag to false when the ray originates outside the shape") {
      val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
      val sphere = Sphere()
      val hit = Intersection(4f, sphere)
      val comps = hit.prepareHit(ray)
      assertFalse(comps.inside)
    }

    it("should set the inside flag to true when the ray originates inside the shape") {
      val ray = Ray(Point(0f, 0f, 0f), Vector(0f, 0f, 1f))
      val sphere = Sphere()
      val hit = Intersection(1f, sphere)
      val comps = hit.prepareHit(ray)
      assertEquals(Point(0f, 0f, 0.9999f), comps.point)
      assertEquals(Vector(0f, 0f, -1f), comps.eyeVector)
      assertTrue(comps.inside)
      assertEquals(Vector(0f, 0f, -1f), comps.normalVector)
    }

    it("should return a point slightly above the normal of the point") {
      val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
      val sphere = Sphere()
      val hit = Intersection(4f, sphere)
      val comps = hit.prepareHit(ray)
      assertTrue(comps.point.z < -1f)
      assertTrue(comps.point.z > -1.1f)
    }

    it("should precompute the reflection vector") {
      val f = (Math.sqrt(2.0) / 2.0).toFloat()
      val shape = Plane()
      val ray = Ray(Point(0f, 1f, -1f), Vector(0f, -f, f))
      val hit = Intersection(Math.sqrt(2.0).toFloat(), shape)
      val comps = hit.prepareHit(ray)
      assertEquals(Vector(0f, f, f), comps.reflectVector)
    }

    it("should precompute a point below the surface") {
      val shape = Sphere()
      val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
      val hit = Intersection(4f, shape)
      val xs = Intersections(1, arrayListOf(hit))

      assertEquals(-0.95f, hit.prepareHit(ray, xs).underPoint.z, 0.05f)
    }
  }

  context("when determining if a point is shadowed") {
    val world = World.default()
    val light = world.lightSources[0]

    it("should not be shadowed when nothing is colinear with a point and light") {
      val p = Point(0f, 10f, 0f)
      val int = Intersection(1f, Sphere(), point = p)
      assertFalse(int.isShadowed(light, world))
    }

    it("should be shadowed when the point is behind an object")  {
      val p = Point(10f, -10f, 10f)
      val int = Intersection(1f, Sphere(), point = p)
      assertTrue(int.isShadowed(light, world))
    }

    it("should not be shadowed when the light is between the point and object") {
      val p = Point(-20f, 20f, -20f)
      val int = Intersection(1f, Sphere(), point = p)
      assertFalse(int.isShadowed(light, world))
    }

    it("should not be shadowed when the point is betwen the light and the object") {
      val p = Point(-2f, 2f, -2f)
      val int = Intersection(1f, Sphere(), point = p)
      assertFalse(int.isShadowed(light, world))
    }
  }

  context("reflectance") {
    val glass = Material(refractiveIndex = 1.52f)
    val sphere = Sphere(material = glass)

    it("should return 1 when total internal reflection") {
      val f = (Math.sqrt(2.0) / 2.0).toFloat()
      val r = Ray(Point(0f, 0f, f), Vector(0f, 1f, 0f))
      val xs = Intersections(
        2,
        arrayListOf(
          Intersection(-f, sphere),
          Intersection(f, sphere)
        )
      )

      val comps = xs[1].prepareHit(r, xs)
      assertTrue(comps.n1 > 1f)
      val reflectance = comps.schlick()
      assertEquals(1f, reflectance)
    }

    it("should have low reflectance when the ray is a perpendicular intersection") {
      val r = Ray(Point(0f, 0f, 0f), Vector(0f, 1f, 0f))
      val xs = Intersections(
        2,
        arrayListOf(
          Intersection(-1f, sphere),
          Intersection(1f, sphere)
        )
      )

      val comps = xs[1].prepareHit(r, xs)
      val reflectance = comps.schlick()
      assertEquals(0.04f, reflectance, 0.01f)
    }

    it("should be reflective when the ray is at a shallow intersection") {
      val r = Ray(Point(0f, 0.99f, -2f), Vector(0f, 0f, 1f))
      val xs = Intersections(
        2,
        arrayListOf(Intersection(1.8589f, sphere))
      )

      val comps = xs[0].prepareHit(r, xs)
      val reflectance = comps.schlick()
      assertEquals(0.4901f, reflectance, 0.0001f)
    }
  }
})