import com.weberapps.rayTracer.Point
import com.weberapps.rayTracer.TAU
import com.weberapps.rayTracer.Transformation
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.assertEquals
object TransformationsSpec: Spek({
    it("should be able to create a translation") {
        val transform = Transformation.translation(5f, -3f, 2f)
        val p = Point(-3f, 4f, 5f)
        assertEquals(Point(2f, 1f, 7f), transform *p)
    }

    it("should be able to scale") {
        val transform = Transformation.scale(2f, 3f, 4f)
        val p = Point(-4f, 6f, 8f)
        assertEquals(Point(-8f, 18f, 32f), transform * p)
    }

    it("should be able to rotate around the X axis") {
        val p = Point(0f, 1f, 0f)
        val eighth = Transformation.rotateX(TAU / 8)
        val quarter = Transformation.rotateX(TAU / 4)

        assertEquals(Point(0f, (Math.sqrt(2.0) / 2f).toFloat(), (Math.sqrt(2.0) / 2f).toFloat()), eighth * p)
        assertEquals(Point(0f, 0f, 1f), quarter * p)
    }

    it("should be able to rotate around the Y axis") {
        val p = Point(0f, 0f, 1f)
        val eighth = Transformation.rotateY(TAU / 8)
        val quarter = Transformation.rotateY(TAU / 4)

        assertEquals(Point((Math.sqrt(2.0) / 2f).toFloat(), 0f, (Math.sqrt(2.0) / 2f).toFloat()), eighth * p)
        assertEquals(Point(1f, 0f, 0f), quarter * p)
    }

    it("should be able to rotate around the Z axis") {
        val p = Point(0f, 1f, 0f)
        val eighth = Transformation.rotateZ(TAU / 8)
        val quarter = Transformation.rotateZ(TAU / 4)

        assertEquals(Point(-(Math.sqrt(2.0) / 2f).toFloat(), (Math.sqrt(2.0) / 2f).toFloat(), 0f), eighth.times(p))
        assertEquals(Point(-1f, 0f, 0f), quarter.times(p))
    }
})