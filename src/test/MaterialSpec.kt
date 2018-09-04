import com.weberapps.rayTracer.*
import junit.framework.TestCase.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

object MaterialSpec: Spek({
    context("initialization") {
        it("should store properties ambient, diffuse, specular and shininess") {
            val m = Material()
            assertEquals(Color(1f, 1f, 1f), m.color)
            assertEquals(0.1f, m.ambient)
            assertEquals(0.9f, m.diffuse)
            assertEquals(0.9f, m.specular)
            assertEquals(200, m.shininess)
        }
    }

    context("Lighting a material") {
        val m = Material()
        val p = Point(0f, 0f, 0f)

        it("should return full intensity when the eye is perpendicular to the surface") {
            val eye = Vector(0f, 0f, -1f)
            val normal = Vector(0f, 0f, -1f)
            val light = Light(Point(0f, 0f, -10f), Color(1f, 1f, 1f))
            val result = m.lighting(light, p, eye, normal)
            assertEquals(Color(1.9f, 1.9f, 1.9f), result)
        }

        it("should have no specular component when eye offset 45 degrees") {
            val f = (Math.sqrt(2.0) / 2).toFloat()
            val eye = Vector(0f, f, f)
            val normal = Vector(0f, 0f, -1f)
            val light = Light(Point(0f, 0f, -10f), Color(1f, 1f, 1f))
            val result = m.lighting(light, p, eye, normal)
            assertEquals(Color(1f, 1f, 1f), result)
        }

        it("should reduce diffuse component when light source is offset 45 degrees") {
            val eye = Vector(0f, 0f, -1f)
            val normal = Vector(0f, 0f, -1f)
            val light = Light(Point(0f, 10f, -10f), Color(1f, 1f, 1f))
            val result = m.lighting(light, p, eye, normal)
            assertEquals(Color(0.7364f, 0.7364f, 0.7364f), result)
        }

        it("should have specular component at full strength when the eye is at the same angle as the reflection") {
            val f = (Math.sqrt(2.0) / 2).toFloat()
            val eye = Vector(0f, -f, -f)
            val normal = Vector(0f, 0f, -1f)
            val light = Light(Point(0f, 10f, -10f), Color(1f, 1f, 1f))
            val result = m.lighting(light, p, eye, normal)
            val expected = Color(1.6364f, 1.6364f, 1.6364f)
            assertEquals(expected, result)
        }

        it("should only have an ambient component when the eye and light are on opposite sides") {
            val eye = Vector(0f, 0f, -1f)
            val normal = Vector(0f, 0f, -1f)
            val light = Light(Point(0f, 0f, 10f), Color(1f, 1f, 1f))
            val result = m.lighting(light, p, eye, normal)
            assertEquals(Color(0.1f, 0.1f, 0.1f), result)
        }
    }
})