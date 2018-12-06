import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Tuple
import com.weberapps.ray.tracer.math.Vector
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.assertEquals

object TupleSpec: Spek ({
  it("should be able to init a Tuple") {
    val tuple = Tuple(3.0f, 4.0f, 5.0f, 1.0f)
    assertEquals(3.0f, tuple.x)
    assertEquals(4.0f, tuple.y)
    assertEquals(5.0f, tuple.z)
    assertEquals(1.0f, tuple.w)
  }

  it("should be able to init a Point") {
    val point = Point(3.0f, 4.0f, 5.0f)
    assertEquals(3.0f, point.x)
    assertEquals(4.0f, point.y)
    assertEquals(5.0f, point.z)
    assertEquals(1.0f, point.w)
  }

  it("should be able to init a Vector") {
    val vector = Vector(3.0f, 4.0f, 5.0f)
    assertEquals(3.0f, vector.x)
    assertEquals(4.0f, vector.y)
    assertEquals(5.0f, vector.z)
    assertEquals(0.0f, vector.w)
  }

  it("should be able to add tuples together") {
    val a1 = Tuple(3.0f, -2.0f, 5.0f, 1.0f)
    val a2 = Tuple(-2.0f, 3.0f, 1.0f, 0.0f)
    assertEquals(Tuple(1.0f, 1.0f, 6.0f, 1.0f), a1 + a2)
    assertEquals(Tuple(1.0f, 1.0f, 6.0f, 1.0f), a2 + a1)
  }

  it("should be able to subtract tuples") {
    val p1 = Point(3.0f, 2.0f, 1.0f)
    val p2 = Point(5.0f, 6.0f, 7.0f)
    val result = p1 - p2
    assertEquals(Vector(-2.0f, -4.0f, -6.0f), result)
  }

  it("should be able to subtract a vector from a point") {
    val p1 = Point(3.0f, 2.0f, 1.0f)
    val v1 = Vector(5.0f, 6.0f, 7.0f)
    val result = p1 - v1
    assertEquals(Point(-2.0f, -4.0f, -6.0f), result)
  }

  it("should be able to subtract a vector from a vector") {
    val v1 = Vector(3.0f, 2.0f, 1.0f)
    val v2 = Vector(5.0f, 6.0f, 7.0f)
    val result = v1 - v2
    assertEquals(Vector(-2.0f, -4.0f, -6.0f), result)
  }

  it("should be able to negate a tuple") {
    val a1 = Tuple(2.0f, -4.0f, 6.0f, -8.0f)
    assertEquals(Tuple(-2.0f, 4.0f, -6.0f, 8.0f), -a1)
  }

  it("should be able to multiply a tuple by a scalar") {
    val a1 = Tuple(2.0f, -4.0f, 6.0f, -8.0f)
    assertEquals(Tuple(4.0f, -8.0f, 12.0f, -16.0f), a1 * 2f)
    assertEquals(Tuple(1.0f, -2.0f, 3.0f, -4.0f), a1 * 0.5f)
  }

  it("should be able to divide a tuple by a scalar") {
    val a1 = Tuple(2.0f, -4.0f, 6.0f, -8.0f)
    assertEquals(Tuple(1.0f, -2.0f, 3.0f, -4.0f), a1 / 2f)
  }

  it("should be able to determine the tuple magnitude") {
    assertEquals(1f, Vector(1.0f, 0.0f, 0.0f).magnitude)
    assertEquals(1f, Vector(0.0f, 1.0f, 0.0f).magnitude)
    assertEquals(1f, Vector(0.0f, 0.0f, 1.0f).magnitude)
    assertEquals(Math.sqrt(77.0).toFloat(), Vector(4.0f, 5.0f, 6.0f).magnitude, 0.0001f)
  }

  it("should be able to normalize a tuple") {
    assertEquals(Vector(1f, 0f, 0f), Vector(4f, 0f, 0f).normalize())
    assertEquals(Vector(0f, 1f, 0f), Vector(0f, 8f, 0f).normalize())
    assertEquals(Vector(0f, 0f, 1f), Vector(0f, 0f, 9f).normalize())
    val expected = Vector(0.26726f, 0.53452f, 0.80178f)
    val result = Vector(1f, 2f, 3f).normalize()
    assertEquals(expected, result)
  }

  it("should be able to calculate the dot product of vectors") {
    val a1 = Vector(1f, 2f, 3f)
    val a2 = Vector(2f, 3f, 4f)

    assertEquals(20f, a1.dot(a2))
  }

  it("should be able to calculate cross product") {
    val a1 = Vector(1f, 2f, 3f)
    val a2 = Vector(2f, 3f, 4f)

    assertEquals(Vector(-1f, 2f, -1f), a1.cross(a2))
    assertEquals(Vector(1f, -2f, 1f), a2.cross(a1))
  }

  it("should be able to create a color") {
    val c = Color(-0.5f, 0.4f, 1.7f)
    assertEquals(-0.5f, c.red)
    assertEquals(0.4f, c.green)
    assertEquals(1.7f, c.blue)
  }

  it("should be able to add colors together") {
    val c1 = Color(0.9f, 0.6f, 0.75f)
    val c2 = Color(0.7f, 0.1f, 0.25f)
    assertEquals(Color(1.6f, 0.7f, 1.0f), c1 + c2)
    assertEquals(Color(1.6f, 0.7f, 1.0f), c2 + c1)
  }

  it("should be able to subtract colors") {
    val c1 = Color(0.9f, 0.6f, 0.75f)
    val c2 = Color(0.7f, 0.1f, 0.25f)
    assertEquals(Color(0.2f, 0.5f, 0.5f), c1 - c2)
    assertEquals(Color(-0.2f, -0.5f, -0.5f), c2 - c1)
  }

  it("should be able to multiply colors by a scalar") {
    val c = Color(0.2f, 0.3f, 0.4f)
    assertEquals(Color(0.4f, 0.6f, 0.8f), c * 2f)
  }

  it("should be able to multiply a color by a color") {
    val c1 = Color(1f, 0.2f, 0.4f)
    val c2 = Color(0.9f, 1f, 0.1f)
    assertEquals(Color(0.9f, 0.2f, 0.04f), c1 * c2)
    assertEquals(Color(0.9f, 0.2f, 0.04f), c2 * c1)
  }

  context("reflection") {
    it("should reflect a vector approaching at 45 degrees") {
      val v = Vector(1f, -1f, 0f)
      val n = Vector(0f, 1f, 0f)
      val r = v.reflect(n)
      assertEquals(Vector(1f, 1f, 0f), r)
    }

    it("should reflect a vector off of a slanted surface") {
      val v = Vector(0f, -1f, 0f)
      val f = (Math.sqrt(2.0) / 2).toFloat()
      val n = Vector(f, f, 0f)
      val r = v.reflect(n)
      assertEquals(Vector(1f, 0f, 0f), r)
    }
  }
})