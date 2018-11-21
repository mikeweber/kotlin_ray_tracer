import com.weberapps.ray.tracer.intersection.Intersection
import com.weberapps.ray.tracer.material.StripePattern
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.shape.Sphere
import junit.framework.TestCase.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

object StripePatternSpec: Spek({
  context("initialization") {
    val pattern = StripePattern()

    it("should be black and white be default") {
      assertEquals(Color.WHITE, pattern.zig)
      assertEquals(Color.BLACK, pattern.zag)
    }
  }

  context("stripeAt") {
    val pattern = StripePattern()

    it("should be constant in the Y direction") {
      assertEquals(Color.WHITE, pattern.stripeAt(Point(0f, 0f, 0f)))
      assertEquals(Color.WHITE, pattern.stripeAt(Point(0f, 1f, 0f)))
      assertEquals(Color.WHITE, pattern.stripeAt(Point(0f, 2f, 0f)))
    }

    it("should be constant in the Z direction") {
      assertEquals(Color.WHITE, pattern.stripeAt(Point(0f, 0f, 0f)))
      assertEquals(Color.WHITE, pattern.stripeAt(Point(0f, 0f, 1f)))
      assertEquals(Color.WHITE, pattern.stripeAt(Point(0f, 0f, 2f)))
    }

    it("should be alternate in the X direction") {
      assertEquals(Color.WHITE, pattern.stripeAt(Point(0f, 0f, 0f)))
      assertEquals(Color.WHITE, pattern.stripeAt(Point(0.9f, 0f, 0f)))
      assertEquals(Color.BLACK, pattern.stripeAt(Point(1f, 0f, 0f)))
      assertEquals(Color.BLACK, pattern.stripeAt(Point(-0.1f, 0f, 0f)))
      assertEquals(Color.BLACK, pattern.stripeAt(Point(-1f, 0f, 0f)))
      assertEquals(Color.WHITE, pattern.stripeAt(Point(-1.1f, 0f, 0f)))
    }
  }

  context("when lighting") {
    it("should use the current stripe as the material color") {
      val m = StripePattern(ambient = 1f, diffuse = 0f, specular = 0f)
      val eyeVector = Vector(0f, 0f, -1f)
      val normalVector = Vector(0f, 0f, -1f)
      val light = Light(Point(0f, 0f, -10f))
      val hit1 = Intersection(
        1f,
        Sphere(), inside = false, point = Point(0.9f, 0f, 0f), eyeVector = eyeVector, normalVector = normalVector
      )
      val hit2 = Intersection(
        1f,
        Sphere(), inside = false, point = Point(1.1f, 0f, 0f), eyeVector = eyeVector, normalVector = normalVector
      )
      val c1 = m.lighting(hit1, light)
      val c2 = m.lighting(hit2, light)

      assertEquals(Color.WHITE, c1)
      assertEquals(Color.BLACK, c2)
    }
  }

  context("when the object has been transformed") {
    it("should move with the object") {
      val scale = Transformation.scale(2f, 2f, 2f)
      val stripes = StripePattern()
      val obj = Sphere(transform = scale, material = stripes)

      assertEquals(Color.WHITE, stripes.stripeAtObject(obj, Point(1.5f, 0f, 0f)))
    }
  }

  context("when the pattern has been transformed") {
    it("should move the pattern") {
      val scale = Transformation.scale(2f, 2f, 2f)
      val stripes = StripePattern(transform = scale)
      val obj = Sphere(material = stripes)

      assertEquals(Color.WHITE, stripes.stripeAtObject(obj, Point(1.5f, 0f, 0f)))
    }
  }

  context("when the pattern and shape have been transformed") {
    it("should move with both") {
      val scale = Transformation.scale(2f, 2f, 2f)
      val translation = Transformation.translation(0.5f, 0f, 0f)
      val stripes = StripePattern(transform = translation)
      val obj = Sphere(transform = scale, material = stripes)

      assertEquals(Color.WHITE, stripes.stripeAtObject(obj, Point(2.5f, 0f, 0f)))
    }
  }
})
