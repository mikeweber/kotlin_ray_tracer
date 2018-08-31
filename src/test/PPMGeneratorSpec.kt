import com.weberapps.rayTracer.Canvas
import com.weberapps.rayTracer.Color
import com.weberapps.rayTracer.PPMGenerator
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.assertEquals

object PPMGeneratorSpec : Spek({
    it("should generate a header") {
        val c = Canvas(5, 3)
        val result = PPMGenerator(c).generate().split("\n")
        assertEquals("P3", result[0])
        assertEquals("5 3", result[1])
        assertEquals("255", result[2])
    }

    it("should generate the full image") {
        val red = Color(1f, 0f, 0f)
        val green = Color(0f, 1f, 0f)
        val blue = Color(0f, 0f, 1f)
        val expected = "P3\n5 3\n255\n0 0 0 255 0 0 0 0 0 0 0 0 0 0 0\n0 0 0 0 0 0 0 255 0 0 0 0 0 0 0\n0 0 0 0 0 0 0 0 0 0 0 0 0 0 255\n"
        val c = Canvas(5, 3)

        c.setPixel(1, 0, red)
        c.setPixel(2, 1, green)
        c.setPixel(4, 2, blue)

        assertEquals(expected, PPMGenerator(c).generate())
    }
})