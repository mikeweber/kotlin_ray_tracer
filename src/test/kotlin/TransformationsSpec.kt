import com.weberapps.rayTracer.*
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

    context("view transformation") {
        it("should be the identity matrix by default") {
            val from = Point(0f, 0f, 0f)
            val to  = Point(0f, 0f, -1f)
            val up = Vector(0f, 1f, 0f)
            val t = Transformation.viewTransform(from, to, up)
            assertEquals(Matrix.eye(4), t)
        }

        it("should return the transformation matrix for looking down the positive z axis") {
            val from = Point(0f, 0f, 0f)
            val to  = Point(0f, 0f, 1f)
            val up = Vector(0f, 1f, 0f)
            val t = Transformation.viewTransform(from, to, up)
            assertEquals(Transformation.scale(-1f, 1f, -1f), t)
        }

        it("should actually move the world, not the eye") {
            val from = Point(0f, 0f, 8f)
            val to = Point(0f, 0f, 0f)
            val up = Vector(0f, 1f, 0f)
            val t = Transformation.viewTransform(from, to, up)
            assertEquals(Transformation.translation(0f, 0f, -8f), t)
        }

        it("should be able to handle an arbitrary transformation") {
            val from = Point(1f, 3f, 2f)
            val to = Point(4f, -2f, 8f)
            val up = Vector(1f, 1f, 0f)
            val t = Transformation.viewTransform(from, to, up)
            val expected = Matrix(4, 4, arrayOf(
                    floatArrayOf(-0.50709f, 0.50709f,  0.67612f, -2.36643f),
                    floatArrayOf( 0.76772f, 0.60609f,  0.12122f, -2.82843f),
                    floatArrayOf(-0.35857f, 0.59761f, -0.71714f,  0.00000f),
                    floatArrayOf( 0.00000f, 0.00000f,  0.00000f,  1.00000f)
            ))

            assertEquals(expected, t)
        }
    }
})