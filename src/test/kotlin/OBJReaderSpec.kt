import com.weberapps.ray.tracer.io.OBJReader
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.shape.Triangle
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.StringReader

object OBJReaderSpec: Spek({
  context("when parsing files") {
    it("should ignore unrecognized lines") {
      val gibberish = """
        There was a young lady named Bright
        who traveled much faster than light.
        She set out one day
        in a relative way,
        and came back the previous night.
      """.trimIndent()

      val reader = StringReader(gibberish)
      val parser = OBJReader(reader)
      assertEquals(5, parser.ignoredLines.size)
    }

    it("should parse vertex records") {
      val fileContents = """
        v -1 1 0
        v -1.0000 0.5000 0.0000
        v 1 0 0
        v 1 1 0
      """.trimIndent()

      val parser = OBJReader(StringReader(fileContents))
      assertEquals(5, parser.vertices.size)
      assertEquals(Point(-1f,   1f, 0f), parser.vertices[1])
      assertEquals(Point(-1f, 0.5f, 0f), parser.vertices[2])
      assertEquals(Point( 1f,   0f, 0f), parser.vertices[3])
      assertEquals(Point( 1f,   1f, 0f), parser.vertices[4])
    }

    it("should parse faces") {
      val fileContents = """
        v -1 1 0
        v -1 0 0
        v 1 0 0
        v 1 1 0

        f 1 2 3
        f 1 3 4
      """.trimIndent()

      val parser = OBJReader(StringReader(fileContents))
      assertEquals(5, parser.vertices.size)

      val group = parser.group
      assertEquals(2, group.shapes.size)

      val t1 = group.shapes[0] as Triangle
      val t2 = group.shapes[1] as Triangle

      assertEquals(parser.vertices[1], t1.p1)
      assertEquals(parser.vertices[2], t1.p2)
      assertEquals(parser.vertices[3], t1.p3)

      assertEquals(parser.vertices[1], t2.p1)
      assertEquals(parser.vertices[3], t2.p2)
      assertEquals(parser.vertices[4], t2.p3)
    }

    it("should be able to break down polygons into triangles") {
      val fileContents = """
        v -1 1 0
        v -1 0 0
        v 1 0 0
        v 1 1 0
        v 0 2 0

        f 1 2 3 4 5
      """.trimIndent()

      val parser = OBJReader(StringReader(fileContents))
      assertEquals(6, parser.vertices.size)
      assertEquals(3, parser.group.shapes.size)

      val t1 = parser.group.shapes[0] as Triangle
      val t2 = parser.group.shapes[1] as Triangle
      val t3 = parser.group.shapes[2] as Triangle

      assertEquals(parser.vertices[1], t1.p1)
      assertEquals(parser.vertices[2], t1.p2)
      assertEquals(parser.vertices[3], t1.p3)

      assertEquals(parser.vertices[1], t2.p1)
      assertEquals(parser.vertices[3], t2.p2)
      assertEquals(parser.vertices[4], t2.p3)

      assertEquals(parser.vertices[1], t3.p1)
      assertEquals(parser.vertices[4], t3.p2)
      assertEquals(parser.vertices[5], t3.p3)
    }
  }
})