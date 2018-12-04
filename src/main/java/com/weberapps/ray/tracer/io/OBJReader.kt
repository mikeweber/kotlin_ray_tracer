package com.weberapps.ray.tracer.io

import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.shape.Group
import com.weberapps.ray.tracer.shape.SmoothTriangle
import com.weberapps.ray.tracer.shape.Triangle
import java.io.Reader
import java.lang.Float.valueOf

class OBJReader(contents: Reader) {
  val ignoredLines: ArrayList<String> = arrayListOf()
  val vertices: ArrayList<Point> = arrayListOf(Point(0f, 0f, 0f))
  val normals: ArrayList<Vector> = arrayListOf(Vector(0f, 0f, 0f))
  var currentGroup: String? = null
  val group: Group
    get() { return groups.getOrDefault(currentGroup, Group()) }
  val groups: HashMap<String?, Group> = HashMap()

  init {
    parseFile(contents)
  }

  fun defaultGroup(): Group {
    return groups.getOrDefault(null, Group())
  }

  private fun parseFile(contents: Reader) {
    contents.forEachLine { parseLine(it) }
  }

  private fun parseLine(line: String) {
    if (line.isEmpty()) return
    val parts = line.trim().split(Regex("\\s+"))
    val head = parts[0]
    val tail = parts.drop(1)

    when(head) {
      "v"  -> addPoint(parseVertex(tail))        ?: return fail(line)
      "f"  -> addFacesToGroup(parseFace(tail))   ?: return fail(line)
      "g"  -> currentGroup = parseGroup(tail)    ?: return fail(line)
      "vn" -> addNormal(parseVectorNormal(tail)) ?: return fail(line)
      else -> return fail(line)
    }
  }

  private fun parseVertex(line: List<String>): Point? {
    if (line.size != 3) return null

    val floats = line.map { char -> valueOf(char) }
    return Point(floats[0], floats[1], floats[2])
  }

  private fun parseFace(line: List<String>): ArrayList<Triangle>? {
    val vertexIndices = line.map { part ->
      Integer.parseInt(if (part.split('/')[0] == "") part else part.split('/')[0])
    }
    val normalIndices = line.map { part ->
      if (part.split('/').size < 3) null else Integer.parseInt(part.split('/')[2])
    }
    if (vertexIndices.size < 3) return null

    val facePoints = arrayListOf<Point>()
    for (index in vertexIndices) {
      if (vertices.size <= index) println("IndexOutOfBounds: $index -- $line")
      facePoints.add(vertices[index])
    }

    val faceNormals = arrayListOf<Vector>()
    for (index in normalIndices) if (index != null) faceNormals.add(normals[index])

    return fanTriangulation(facePoints, faceNormals)
  }

  private fun fanTriangulation(facePoints: ArrayList<Point>, faceNormals: ArrayList<Vector>): ArrayList<Triangle> {
    val triangles = arrayListOf<Triangle>()

    for (index in (1..(facePoints.size - 2))) {
      val triangle = if (faceNormals.size > index + 1) {
        SmoothTriangle(facePoints[0], facePoints[index], facePoints[index + 1], faceNormals[0], faceNormals[index], faceNormals[index + 1])
      } else {
        Triangle(facePoints[0], facePoints[index], facePoints[index + 1])
      }
      triangles.add(triangle)
    }

    return triangles
  }

  private fun parseGroup(line: List<String>): String? {
    return line[0]
  }

  private fun parseVectorNormal(line: List<String>): Vector? {
    if (line.size != 3) return null

    val floats = line.map { char -> valueOf(char) }
    return Vector(floats[0], floats[1], floats[2])
  }

  private fun addPoint(point: Point?): Point? {
    if (point == null) return null

    vertices.add(point)
    return point
  }

  private fun addNormal(vector: Vector?): Vector? {
    if (vector == null) return null

    normals.add(vector)
    return vector
  }

  private fun addFacesToGroup(triangles: ArrayList<Triangle>?): ArrayList<Triangle>? {
    if (triangles == null) return null

    for (tri in triangles) {
      addFaceToGroup(tri)
    }
    return triangles
  }

  private fun addFaceToGroup(tri: Triangle?): Triangle? {
    if (tri != null) groups[currentGroup] = group.add(tri) as Group

    return tri
  }

  private fun fail(line: String) { ignoredLines.add(line) }
}