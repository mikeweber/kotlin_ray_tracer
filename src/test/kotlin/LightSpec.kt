import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Light
import junit.framework.TestCase.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

object LightSpec: Spek({
  it("should have a position and intensity") {
    val point = Point(0f, 0f, 0f)
    val color = Color.WHITE
    val light = Light(point, color)
    assertEquals(point, light.position)
    assertEquals(color, light.intensity)
  }
})