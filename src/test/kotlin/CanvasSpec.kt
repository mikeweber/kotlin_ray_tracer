import com.weberapps.rayTracer.Canvas
import com.weberapps.rayTracer.Color
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.assertEquals

object CanvasSpec: Spek({
  given("a canvas") {
    on("initialization") {
      val c = Canvas(10, 20)

      it("initializes a canvas with given size and all black") {
        assertEquals(10, c.width)
        assertEquals(20, c.height)
        val p1 = c.getPixel(5, 10)
        assertEquals(Color.BLACK, p1)
      }
    }

    on("write") {
      val c = Canvas(10, 20)

      it("changes the pixel color at the given location") {
        c.setPixel(2, 3, Color.RED)
        assertEquals(Color.RED, c.getPixel(2, 3))
        assertEquals(Color.BLACK, c.getPixel(3, 3))
        assertEquals(Color.BLACK, c.getPixel(1, 3))
        assertEquals(Color.BLACK, c.getPixel(2, 2))
        assertEquals(Color.BLACK, c.getPixel(2, 4))
      }
    }
  }
})