import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.math.Vector
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.assertEquals

object RaySpec : Spek({
  context("initialization") {
    it("should have an origin and direction") {
      val origin = Point(1f, 2f, 3f)
      val direction = Vector(4f, 5f, 6f)
      val r = Ray(origin, direction)
      assertEquals(origin, r.origin)
      assertEquals(direction, r.direction)
    }
  }

  context("calculating a point") {
    val ray = Ray(Point(2f, 3f, 4f), Vector(1f, 0f, 0f))

    it("should return a new position based on time") {
      assertEquals(Point(2f, 3f, 4f), ray.positionAt(0f))
      assertEquals(Point(3f, 3f, 4f), ray.positionAt(1f))
      assertEquals(Point(1f, 3f, 4f), ray.positionAt(-1f))
      assertEquals(Point(4.5f, 3f, 4f), ray.positionAt(2.5f))
    }
  }

  context("translating a ray") {
    it("should translate the origin") {
      val origin = Point(1f, 2f, 3f)
      val direction = Vector(0f, 1f, 0f)
      val ray = Ray(origin, direction)
      val translation = Transformation.translation(3f, 4f, 5f)
      val translatedRay = ray.transform(translation)

      assertEquals(Point(4f, 6f, 8f), translatedRay.origin)
      assertEquals(direction, translatedRay.direction)
    }
  }

  context("scaling a ray") {
    it("should scale the origin and direction") {
      val origin = Point(1f, 2f, 3f)
      val direction = Vector(0f, 1f, 0f)
      val ray = Ray(origin, direction)
      val scale = Transformation.scale(2f, 3f, 4f)
      val translatedRay = ray.transform(scale)

      assertEquals(Point(2f, 6f, 12f), translatedRay.origin)
      assertEquals(Vector(0f, 3f, 0f), translatedRay.direction)
    }
  }
})