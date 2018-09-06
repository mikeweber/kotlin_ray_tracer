import com.weberapps.rayTracer.*
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
            val c1 = m.lighting(light, Point(0.9f, 0f, 0f), eyeVector, normalVector, false)
            val c2 = m.lighting(light, Point(1.1f, 0f, 0f), eyeVector, normalVector, false)

            assertEquals(Color.WHITE, c1)
            assertEquals(Color.BLACK, c2)
        }
    }
})
