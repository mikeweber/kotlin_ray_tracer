package com.weberapps.rayTracer

import java.lang.Math.abs

class Matrix (val rows: Int, val cols: Int) {
    var rowsAndColumns: Array<FloatArray> = Array(rows) { FloatArray(cols) }

    constructor(rows: Int, cols: Int, matrix: Array<FloatArray>) : this(rows, cols) {
        rowsAndColumns = matrix
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
        var result = FloatArray(4)
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
}