import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.CompositeColor
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals

object CompositeColorSpec: Spek({
  context("when calculating color") {
    it("should return the color when there's only one") {
      val c = Color(3f, 4f, 5f)
      val composite = CompositeColor(c)
      assertEquals(c, composite.color)
    }

    it("should return the average color when there are multiple") {
      val c1 = Color(1f, 3f, 5f)
      val c2 = Color(3f, 3f, 3f)
      val c3 = Color(5f, 3f, 1f)

      val composite = CompositeColor(c1, c2, c3)
      assertEquals(Color(3f, 3f, 3f), composite.color)
    }
  }
})