import com.weberapps.rayTracer.Color
import com.weberapps.rayTracer.Material
import junit.framework.TestCase.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

object MaterialSpec: Spek({
    context("initialization") {
        it("should store properties ambient, diffuse, specular and shininess") {
            val m = Material()
            assertEquals(Color(1f, 1f, 1f), m.color)
            assertEquals(0.1f, m.ambient)
            assertEquals(0.9f, m.diffuse)
            assertEquals(0.9f, m.specular)
            assertEquals(200, m.shininess)
        }
    }
})