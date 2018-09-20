import com.weberapps.rayTracer.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.*

object CameraSpec: Spek({
  context("initialization") {
    it("should be initialized with canvas sizes and field of view") {
      val c = Camera(160, 120, TAU / 4)
      assertEquals(160, c.hsize)
      assertEquals(120, c.vsize)
      assertEquals(TAU / 4, c.fieldOfView)
      assertEquals(Matrix.eye(4), c.transform)
    }

    it("should calculate pixel size for a horizontal canvas") {
      val c = Camera(200, 125)
      assertEquals(0.01f, c.pixelSize, 0.001f)
    }

    it("should calculate the pixel size for a vertical canvas") {
      val c = Camera(125, 200)
      assertEquals(0.01f, c.pixelSize, 0.001f)
    }
  }

  context("constructing rays") {
    var c = Camera(201, 101)
    beforeEachTest {
      c = Camera(201, 101)
    }

    it("should be able to pass through the center of the canvas") {
      val ray = c.rayForPixel(100, 50)
      assertEquals(Point(0f, 0f, 0f), ray.origin)
      assertEquals(Vector(0f, 0f, -1f), ray.direction)
    }

    it("should be able to pass through a corner of the canvas") {
      val ray = c.rayForPixel(0, 0)
      assertEquals(Point(0f, 0f, 0f), ray.origin)
      assertEquals(Vector(0.66519f, 0.33259f, -0.66851f), ray.direction)
    }

    it("should work with a transformed camera") {
      c.transform = Transformation.rotateY(TAU / 8) * Transformation.translation(0f, -2f, 5f)
      val r = c.rayForPixel(100, 50)
      val f = (Math.sqrt(2.0) / 2).toFloat()
      assertEquals(Point(0f, 2f, -5f), r.origin)
      assertEquals(Vector(f, 0f, -f), r.direction)
    }
  }

  context("render") {
    it("should be able to render a world") {
      val w = World.default()
      val from = Point(0f, 0f, -5f)
      val to = Point(0f, 0f, 0f)
      val up = Vector(0f, 1f, 0f)
      val c = Camera(11, 11, transform = Transformation.viewTransform(from, to, up))
      val image = c.render(w)
      assertEquals(Color(0.38066f, 0.47583f, 0.2855f), image.getPixel(5, 5))
    }
  }
})