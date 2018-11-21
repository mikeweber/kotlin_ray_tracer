package com.weberapps.ray.tracer.math

import com.weberapps.ray.tracer.constants.EPSILON
import java.lang.Math.abs

class Matrix (val rows: Int, val cols: Int) {
  var rowsAndColumns: Array<FloatArray> = Array(rows) { FloatArray(cols) }

  constructor(rows: Int, cols: Int, matrix: Array<FloatArray>) : this(rows, cols) {
    rowsAndColumns = matrix
  }

  companion object {
    fun eye(size: Int): Matrix {
      val result = Matrix(size, size)
      for (loc in 0..(size - 1)) {
        result.setElement(loc, loc, 1f)
      }
      return result
    }
  }

  fun getElement(row: Int, col: Int): Float {
    return rowsAndColumns[row][col]
  }

  fun setElement(row: Int, col: Int, element: Float) {
    rowsAndColumns[row][col] = element
  }

  fun size(): Int {
    return rows
  }

  operator fun times(other: Matrix): Matrix {
    val result = Matrix(rows, other.cols)

    for (row in 0..(rows - 1)) {
      for (col in 0..(other.cols - 1)) {
        var sum = 0f
        for (common in 0..(cols - 1)) {
          val valA = getElement(row, common)
          val valB = other.getElement(common, col)
          sum += valA * valB
        }
        result.setElement(row, col, sum)
      }
    }

    return result
  }

  operator fun times(tuple: Tuple): Tuple {
    val result = FloatArray(4)
    for (row in 0..3) {
      for (col in 0..(cols - 1)) {
        result[row] =
            getElement(row, 0) * tuple.x +
            getElement(row, 1) * tuple.y +
            getElement(row, 2) * tuple.z +
            getElement(row, 3) * tuple.w
      }
    }

    return Tuple(result[0], result[1], result[2], result[3])
  }

  operator fun div(scalar: Float): Matrix {
    val result = Matrix(rows, cols)
    for (i in 0..(rows - 1)) {
      for (j in 0..(cols - 1)) {
        result.setElement(i, j, getElement(i, j) / scalar)
      }
    }
    return result
  }

  override operator fun equals(other: Any?): Boolean {
    if (other !is Matrix) return false

    if (rows != other.rows || cols != other.cols) return false

    for (row in 0..(rows - 1)) {
      for (col in 0..(cols - 1)) {
        if (abs(getElement(row, col) - other.getElement(row, col)) > EPSILON) return false
      }
    }
    return true
  }

  fun transpose(): Matrix {
    val result = Matrix(cols, rows)
    for (row in 0..(rows - 1)) {
      for (col in 0..(cols - 1)) {
        result.setElement(col, row, getElement(row, col));
      }
    }

    return result
  }

  private fun recursiveDeterminant(row: Int = 0, col: Int = 0, sum: Float = 0f): Float {
    if (col >= cols) return sum

    val det = getElement(row, col) * cofactor(row, col)
    return recursiveDeterminant(row, col + 1, sum + det)
  }

  fun cofactor(row: Int, col: Int): Float {
    return if ((row + col) % 2 == 0) minor(row, col) else -minor(row, col)
  }

  fun minor(row: Int, col: Int): Float {
    return submatrix(row, col).determinant()
  }

  fun inverse(): Matrix {
    return cofactors().transpose() / determinant()
  }

  fun cofactors(): Matrix {
    val result = Matrix(rows, cols)
    for (i in 0..(rows - 1)) {
      for (j in 0..(cols - 1)) {
        result.setElement(i, j, cofactor(i, j))
      }
    }
    return result
  }

  fun isInvertable(): Boolean {
    return determinant() != 0f
  }

  fun determinant(): Float {
    if (rows < 2 || cols < 2) return 0f
    if (rows > 2 && cols > 2) return recursiveDeterminant()

    return getElement(0, 0) * getElement(1, 1) - getElement(0, 1) * getElement(1, 0)
  }

  fun submatrix(rowToRemove: Int, colToRemove: Int): Matrix {
    val result = Matrix(rows - 1, cols - 1)
    for (row in 0..(rows - 1)) {
      if (row == rowToRemove) continue

      val i = if (row > rowToRemove) row - 1 else row
      for (col in 0..(cols - 1)) {
        if (col == colToRemove) continue
        val j = if (col > colToRemove) col - 1 else col

        result.setElement(i, j, getElement(row, col))
      }
    }

    return result
  }
}