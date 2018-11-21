import com.weberapps.ray.tracer.*
import junit.framework.TestCase.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

object MaterialSpec: Spek({
  context("initialization") {
    it("should store properties ambient, diffuse, specular and shininess") {
      val m = Material()
      assertEquals(Color.WHITE, m.color)
      assertEquals(0.1f, m.ambient)
      assertEquals(0.9f, m.diffuse)
      assertEquals(0.9f, m.specular)
      assertEquals(200, m.shininess)
      assertEquals(0f, m.reflective)
    }
  }

  context("Lighting a material") {
    val m = Material()
    val p = Point(0f, 0f, 0f)

    it("should return full intensity when the eye is perpendicular to the surface") {
      val eye = Vector(0f, 0f, -1f)
      val normal = Vector(0f, 0f, -1f)
      val light = Light(Point(0f, 0f, -10f), Color.WHITE)
      val hit = Intersection(t = 1f, shape = Sphere(), point = p, eyeVector = eye, normalVector = normal)
      val result = m.lighting(hit, light)
      assertEquals(Color(1.9f, 1.9f, 1.9f), result)
    }

    it("should have no specular component when eye offset 45 degrees") {
      val f = (Math.sqrt(2.0) / 2).toFloat()
      val eye = Vector(0f, f, f)
      val normal = Vector(0f, 0f, -1f)
      val light = Light(Point(0f, 0f, -10f), Color.WHITE)
      val hit = Intersection(t = 1f, shape = Sphere(), point = p, eyeVector = eye, normalVector = normal)
      val result = m.lighting(hit, light)
      assertEquals(Color.WHITE, result)
    }

    it("should reduce diffuse component when light source is offset 45 degrees") {
      val eye = Vector(0f, 0f, -1f)
      val normal = Vector(0f, 0f, -1f)
      val light = Light(Point(0f, 10f, -10f), Color.WHITE)
      val hit = Intersection(t = 1f, shape = Sphere(), point = p, eyeVector = eye, normalVector = normal)
      val result = m.lighting(hit, light)
      assertEquals(Color(0.7364f, 0.7364f, 0.7364f), result)
    }

    it("should have specular component at full strength when the eye is at the same angle as the reflection") {
      val f = (Math.sqrt(2.0) / 2).toFloat()
      val eye = Vector(0f, -f, -f)
      val normal = Vector(0f, 0f, -1f)
      val light = Light(Point(0f, 10f, -10f), Color.WHITE)
      val hit = Intersection(t = 1f, shape = Sphere(), point = p, eyeVector = eye, normalVector = normal)
      val result = m.lighting(hit, light)
      val expected = Color(1.63638f, 1.63638f, 1.63638f)
      assertEquals(expected, result)
    }

    it("should only have an ambient component when the eye and light are on opposite sides") {
      val eye = Vector(0f, 0f, -1f)
      val normal = Vector(0f, 0f, -1f)
      val light = Light(Point(0f, 0f, 10f), Color.WHITE)
      val hit = Intersection(t = 1f, shape = Sphere(), point = p, eyeVector = eye, normalVector = normal)
      val result = m.lighting(hit, light)
      assertEquals(Color(0.1f, 0.1f, 0.1f), result)
    }

    it("should be darker in a shadow") {
      val eye = Vector(0f, 0f, -1f)
      val normal = Vector(0f, 0f, -1f)
      val light = Light(Point(0f, 0f, -10f))
      val hit = Intersection(t = 1f, shape = Sphere(), point = p, eyeVector = eye, normalVector = normal)
      val result = m.lighting(hit, light, inShadow = true)
      assertEquals(Color(0.1f, 0.1f, 0.1f), result)
    }
  }
})
