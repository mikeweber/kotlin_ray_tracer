import com.weberapps.ray.tracer.*
import com.weberapps.ray.tracer.material.SolidColor
import com.weberapps.ray.tracer.math.Matrix
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.math.Color
import junit.framework.TestCase.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.assertNull

object ShapeSpec: Spek({
  context("TestShape") {
    var shape = TestShape()
    beforeEachTest {
      shape = TestShape()
    }

    it("should have a default transform") {
      assertEquals(Matrix.eye(4), shape.transform)
    }

    it("should have a default material") {
      assertEquals(SolidColor(), shape.material)
    }

    it("should be able to set the transform") {
      val transform = Transformation.translation(1f, 0f, 0f)
      shape.transform = transform
      assertEquals(transform, shape.transform)
    }

    it("should be able to set the material") {
      val material = SolidColor(Color.BLUE)
      shape.material = material
      assertEquals(material, shape.material)
    }

    it("should not have a parent object by default") {
      assertNull(shape.parent)
    }

    it("should save the transformed ray") {
      val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
      val s = TestShape()
      s.transform = Transformation.scale(2f, 2f, 2f)
      s.intersect(ray)

      assertEquals(Point(0f, 0f, -2.5f), s.savedRay.origin)
      assertEquals(Vector(0f, 0f, 0.5f), s.savedRay.direction)
    }
  }

  context("normal") {
    var s = TestShape()
    beforeEachTest {
      s = TestShape()
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
})