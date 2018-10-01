import com.weberapps.rayTracer.*
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
      hit.prepareHit(ray)
      assertEquals(Point(0f, 0f, -1.0001f), hit.point)
      assertEquals(Vector(0f, 0f, -1f), hit.eyeVector)
      assertEquals(Vector(0f, 0f, -1f), hit.normalVector)
    }

    it("should set the inside flag to false when the ray originates outside the shape") {
      val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
      val sphere = Sphere()
      val hit = Intersection(4f, sphere)
      hit.prepareHit(ray)
      assertFalse(hit.inside)
    }

    it("should set the inside flag to true when the ray originates inside the shape") {
      val ray = Ray(Point(0f, 0f, 0f), Vector(0f, 0f, 1f))
      val sphere = Sphere()
      val hit = Intersection(1f, sphere)
      hit.prepareHit(ray)
      assertEquals(Point(0f, 0f, 0.9999f), hit.point)
      assertEquals(Vector(0f, 0f, -1f), hit.eyeVector)
      assertTrue(hit.inside)
      assertEquals(Vector(0f, 0f, -1f), hit.normalVector)
    }

    it("should return a point slightly above the normal of the point") {
      val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
      val sphere = Sphere()
      val hit = Intersection(4f, sphere)
      hit.prepareHit(ray)
      assertTrue(hit.point.z < -1f)
      assertTrue(hit.point.z > -1.1f)
    }

    it("should precompute the reflection vector") {
      val f = (Math.sqrt(2.0) / 2.0).toFloat()
      val shape = Plane()
      val ray = Ray(Point(0f, 1f, -1f), Vector(0f, -f, f))
      val hit = Intersection(Math.sqrt(2.0).toFloat(), shape)
      hit.prepareHit(ray)
      assertEquals(Vector(0f, f, f), hit.reflectVector)
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
})