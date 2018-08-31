import com.weberapps.rayTracer.Intersection
import com.weberapps.rayTracer.Sphere
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.assertEquals

class IntersectionSpec : Spek({
    context("initialization") {
        it("should have the object intersected and when") {
            val sphere = Sphere()
            val i = Intersection(3.5f, sphere)
            assertEquals(3.5f, i.t)
            assertEquals(sphere, i.shape)
        }
    }
})