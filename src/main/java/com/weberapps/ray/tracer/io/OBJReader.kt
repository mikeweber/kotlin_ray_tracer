package com.weberapps.ray.tracer.io

import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.shape.Group
import com.weberapps.ray.tracer.shape.Triangle
import sun.awt.image.IntegerInterleavedRaster
import java.io.Reader
import java.lang.Float.valueOf
import javax.print.attribute.IntegerSyntax

class OBJReader(contents: Reader) {
  val ignoredLines: ArrayList<String> = arrayListOf()
  val vertices: ArrayList<Point> = arrayListOf(Point(0f, 0f, 0f))
  val group: Group = com.weberapps.ray.tracer.shape.Group()

  init {
    parseFile(contents)
  }

  private fun parseFile(contents: Reader) {
    contents.forEachLine { parseLine(it) }
  }

  private fun parseLine(line: String) {
    if (line.isEmpty()) return

    when(line[0]) {
      'v' -> {
        val point = parseVertex(line)
        if (point == null) {
          fail(line)
        } else {
          vertices.add(point)
        }
      }
      'f' -> {
        val face = parseFace(line)
        if (face == null) {
          fail(line)
        } else {
          for (f in face) {
            group.add(f)
          }
        }
      }
      else -> fail(line)
    }
  }

  private fun fail(line: String) { ignoredLines.add(line) }

  private fun parseVertex(line: String): Point? {
    val floats = line.split(" ").subList(1, 4).map { char -> valueOf(char) }
    if (floats.size != 3) return null

    return Point(floats[0], floats[1], floats[2])
  }

  private fun parseFace(line: String): ArrayList<Triangle>? {
    val split = line.split(" ")
    val indices = split.subList(1, split.size).map { char -> Integer.parseInt(char)} as ArrayList<Int>
    if (indices.size < 3) return null
    val facePoints = arrayListOf<Point>()
    for (index in indices) {
      facePoints.add(vertices[index])
    }

    return fanTriangulation(facePoints)
  }

  private fun fanTriangulation(facePoints: ArrayList<Point>): ArrayList<Triangle> {
    val triangles = arrayListOf<Triangle>()

    for (index in (1..(facePoints.size - 2))) {
      val triangle = Triangle(facePoints[0], facePoints[index], facePoints[index + 1])
      triangles.add(triangle)
    }

    return triangles
  }
}