import com.weberapps.ray.tracer.material.GradientMaterial
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Point
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals

object GradientMaterialSpec: Spek({
  context("when rendering the pattern") {
    it("should linearly interpolate between colors") {
      val pattern = GradientMaterial(Color.WHITE, Color.BLACK)

      assertEquals(Color(0.75f, 0.75f, 0.75f), pattern.gradientColorAtObject(Point(0.25f, 0f, 0f)))
      assertEquals(Color( 0.5f,  0.5f,  0.5f), pattern.gradientColorAtObject(Point( 0.5f, 0f, 0f)))
      assertEquals(Color(0.25f, 0.25f, 0.25f), pattern.gradientColorAtObject(Point(0.75f, 0f, 0f)))
    }
  }
})