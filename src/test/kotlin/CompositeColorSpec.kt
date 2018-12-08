import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.CompositeColor
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals

object CompositeColorSpec: Spek({
  context("when calculating color") {
    val c1 = Color(1f, 3f, 5f)
    val c2 = Color(3f, 3f, 3f)
    val c3 = Color(5f, 3f, 1f)
    val c4 = Color(7f, 11f, 15f)
    val c5 = Color(4f, 5f, 6f)

    it("should return the color when there's only one") {
      val c = Color(3f, 4f, 5f)
      val composite = CompositeColor(c)
      assertEquals(c, composite.color)
    }

    it("should return the average color when there are multiple") {
      val composite = CompositeColor(c1, c2, c3)
      assertEquals(Color(3f, 3f, 3f), composite.color)
    }

    it("should sum up all of the colors in the list") {
      val composite = CompositeColor(c1, CompositeColor(c2, c3), c4)
      assertEquals(4, composite.colorsSize())
      assertEquals(16f, composite.sumRed())
      assertEquals(20f, composite.sumGreen())
      assertEquals(24f, composite.sumBlue())
    }

    it("should be able to add a composite color") {
      val composite1 = CompositeColor(c1, c2)
      val composite2 = CompositeColor(c3, c4, c5)
      val composite3 = CompositeColor(composite1, composite2)
      assertEquals(Color(4f, 5f, 6f), composite3.color)
    }
  }
})
