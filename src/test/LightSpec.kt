import com.weberapps.rayTracer.Color
import com.weberapps.rayTracer.Point
import com.weberapps.rayTracer.Light
import junit.framework.TestCase.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

object LightSpec: Spek({
    it("should have a position and intensity") {
        val point = Point(0f, 0f, 0f)
        val color = Color(1f, 1f, 1f)
        val light = Light(point, color)
        assertEquals(point, light.position)
        assertEquals(color, light.intensity)
    }
})