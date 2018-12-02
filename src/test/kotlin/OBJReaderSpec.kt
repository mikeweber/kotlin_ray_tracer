import com.weberapps.ray.tracer.io.OBJReader
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.shape.Triangle
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import java.io.File
import java.io.StringReader
import java.nio.file.Paths

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

    it("should parse faces with normals") {
      val fileContents = """
        v 0 1 0
        v -1 0 0
        v 1 0 0

        vn -1 0 0
        vn 1 0 0
        vn 0 1 0

        f 1//3 2//1 3//2
        f 1/0/3 2/102/1 3/14/2
      """.trimIndent()

      val parser = OBJReader(StringReader(fileContents))
      val g = parser.defaultGroup()

      assertEquals(2, g.shapes.size)
      val t1 = g.shapes[0] as Triangle
      val t2 = g.shapes[1] as Triangle

      assertEquals(parser.vertices[1], t1.p1)
      assertEquals(parser.vertices[2], t1.p2)
      assertEquals(parser.vertices[3], t1.p3)

      assertEquals(parser.normals[3], t2.p1)
      assertEquals(parser.normals[1], t2.p2)
      assertEquals(parser.normals[2], t2.p3)

      assertEquals(t1, t2)
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

    it("should group triangles") {
      val file = File(Paths.get("").toAbsolutePath().toString() + "/src/test/resources/triangles.obj")
      val parser = OBJReader(file.reader())
      val g1 = parser.groups["FirstGroup"]
      val g2 = parser.groups["SecondGroup"]
      assertNotNull(g1)
      assertNotNull(g2)
      val t1 = g1!!.shapes[0] as Triangle
      val t2 = g2!!.shapes[0] as Triangle

      assertEquals(parser.vertices[1], t1.p1)
      assertEquals(parser.vertices[2], t1.p2)
      assertEquals(parser.vertices[3], t1.p3)

      assertEquals(parser.vertices[1], t2.p1)
      assertEquals(parser.vertices[3], t2.p2)
      assertEquals(parser.vertices[4], t2.p3)
    }

    it("should parse vertex normals") {
      val fileContents = """
        vn 0 0 1
        vn 0.707 0 -0.707
        vn 1 2 3
      """.trimIndent()

      val parser = OBJReader(StringReader(fileContents))

      assertEquals(Vector(    0f, 0f,      1f), parser.normals[1])
      assertEquals(Vector(0.707f, 0f, -0.707f), parser.normals[1])
      assertEquals(Vector(    1f, 2f,      3f), parser.normals[1])
    }
  }
})