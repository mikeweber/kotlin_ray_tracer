import com.weberapps.rayTracer.*
import junit.framework.TestCase.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

object TransparentMaterialSpec: Spek ({
  context("reflect") {
    it("should reflect directly back when facing a normal head on") {
      val m = Material(reflective = 1f)
      assertEquals(Vector(0f, 0f, 1f), m.reflect(Vector(0f, 0f, -1f), Vector(0f, 0f, 1f)))
    }

    it("should reflect a vector at a 45 degree angle back to 45 degrees the other way") {
      val m = Material(reflective = 1f)
      assertEquals(Vector(-1f, 0f, 1f).normalize(), m.reflect(Vector(-1f, 0f, -1f).normalize(), Vector(0f, 0f, 1f)))
    }

    it("should reflect 90 degrees right when the normal is at a 45 degree angle to the right") {
      val m = Material(reflective = 1f)
      assertEquals(Vector(1f, 0f, 0f).normalize(), m.reflect(Vector(0f, 0f, -1f), Vector(1f, 0f, 1f).normalize()))
    }
  }
})