import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.shape.RingMaterial
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals

object RingMaterialSpec: Spek({
  context("when calculating the ring patter") {
    it("should use the x and z coordinates") {
      val pattern = RingMaterial()
      assertEquals(Color.WHITE, pattern.patternAt(Point(0f, 0f, 0f)))
      assertEquals(Color.BLACK, pattern.patternAt(Point(1f, 0f, 0f)))
      assertEquals(Color.BLACK, pattern.patternAt(Point(0f, 0f, 1f)))
      assertEquals(Color.WHITE, pattern.patternAt(Point(0.706f, 0f, 0.706f))) // Slightly less than sqrt(2)
      assertEquals(Color.BLACK, pattern.patternAt(Point(0.708f, 0f, 0.708f))) // Slightly more than sqrt(2)
    }
  }
})