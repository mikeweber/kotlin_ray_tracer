import com.weberapps.rayTracer.Matrix
import com.weberapps.rayTracer.Tuple
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.*

object MatrixSpec: Spek({
  it("can initialize with a list of rows and columns") {
    val m = Matrix(4, 4,
      arrayOf(
        floatArrayOf(1.0f, 2.0f, 3.0f, 4.0f),
        floatArrayOf(5.5f, 6.5f, 7.5f, 8.5f),
        floatArrayOf(9.0f, 10.0f, 11.0f, 12.0f),
        floatArrayOf(13.5f, 14.5f, 15.5f, 16.5f)
      )
    )

    assertEquals( 1.0f, m.getElement(0, 0))
    assertEquals( 4.0f, m.getElement(0, 3))
    assertEquals( 5.5f, m.getElement(1, 0))
    assertEquals( 7.5f, m.getElement(1, 2))
    assertEquals(  11f, m.getElement(2, 2))
    assertEquals(13.5f, m.getElement(3, 0))
    assertEquals(15.5f, m.getElement(3, 2))
  }

  it("initializes an empty matrix") {
    val m = Matrix(3, 2)
    assertEquals(0f, m.getElement(0, 0))
    assertEquals(0f, m.getElement(2, 1))
  }

  it("generates a 2x2 matrix") {
    val m = Matrix(2, 2)
    assertEquals(2, m.size())
  }

  it("generates a 3x3 matrix") {
    val m = Matrix(3, 3)
    assertEquals(3, m.size())
  }

  it("can multiply matrices") {
    val a = Matrix(4, 4,
      arrayOf(
        floatArrayOf(1f, 2f, 3f, 4f),
        floatArrayOf(2f, 3f, 4f, 5f),
        floatArrayOf(3f, 4f, 5f, 6f),
        floatArrayOf(4f, 5f, 6f, 7f)
      )
    )
    val b = Matrix(4, 4,
      arrayOf(
        floatArrayOf(0f, 1f,  2f,  4f),
        floatArrayOf(1f, 2f,  4f,  8f),
        floatArrayOf(2f, 4f,  8f, 16f),
        floatArrayOf(4f, 8f, 16f, 32f)
      )
    )
    val expected = Matrix(4, 4,
      arrayOf(
        floatArrayOf(24f, 49f,  98f, 196f),
        floatArrayOf(31f, 64f, 128f, 256f),
        floatArrayOf(38f, 79f, 158f, 316f),
        floatArrayOf(45f, 94f, 188f, 376f)
      )
    )
    val result = a * b
    assertEquals(expected, result)
  }

  it("can multiply a matrix and a tuple") {
    val matrix = Matrix(4, 4,
      arrayOf(
        floatArrayOf(1f, 2f, 3f, 4f),
        floatArrayOf(2f, 4f, 4f, 2f),
        floatArrayOf(8f, 6f, 4f, 1f),
        floatArrayOf(0f, 0f, 0f, 1f)
      )
    )
    val tuple = Tuple(1f, 2f, 3f, 1f)
    val result = matrix * tuple
    val expected = Tuple(18f, 24f, 33f, 1f)
    assertEquals(expected, result)
  }

  it("can multiply by the identity matrix") {
    val m = Matrix(4, 4,
      arrayOf(
        floatArrayOf(0f, 1f, 2f, 4f),
        floatArrayOf(1f, 2f, 4f, 8f),
        floatArrayOf(2f, 4f, 8f, 16f),
        floatArrayOf(4f, 8f, 16f, 32f)
      )
    )
    val eye = Matrix.eye(4)
    assertEquals(m, m * eye)
  }

  it("can multiply a tuple by the identity matrix") {
    val a = Tuple(1f, 2f, 3f, 4f)
    val eye = Matrix.eye(4)
    assertEquals(a, eye * a)
  }

  it("can transpose a matrix") {
    val m = Matrix(4, 4,
      arrayOf(
        floatArrayOf(0f, 9f, 3f, 0f),
        floatArrayOf(9f, 8f, 0f, 8f),
        floatArrayOf(1f, 8f, 5f, 3f),
        floatArrayOf(0f, 0f, 5f, 8f)
      )
    )
    val expected = Matrix(4, 4,
      arrayOf(
        floatArrayOf(0f, 9f, 1f, 0f),
        floatArrayOf(9f, 8f, 8f, 0f),
        floatArrayOf(3f, 0f, 5f, 5f),
        floatArrayOf(0f, 8f, 3f, 8f)
      )
    )
    assertEquals(expected, m.transpose())
  }

  context("determinant") {
    it("is zero when the matrix is too small") {
      val m1 = Matrix(1, 4,
        arrayOf(
          floatArrayOf(1f, 5f, 4f, 3f)
        )
      )
      val m2 = Matrix(4, 1,
        arrayOf(
          floatArrayOf(1f),
          floatArrayOf(3f),
          floatArrayOf(3f),
          floatArrayOf(4f)
        )
      )

      assertEquals(0f, m1.determinant())
      assertEquals(0f, m2.determinant())
    }

    it("calculates the determinant") {

      val m = Matrix(2, 2,
        arrayOf(
          floatArrayOf( 1f, 5f),
          floatArrayOf(-3f, 2f)
        )
      )
      assertEquals(17f, m.determinant())
    }

    it("can calculate the determinant of a 3x3 matrix") {
      val m = Matrix(3, 3,
        arrayOf(
          floatArrayOf( 1f, 2f,  6f),
          floatArrayOf(-5f, 8f, -4f),
          floatArrayOf( 2f, 6f,  4f)
        )
      )
      assertEquals(  56f, m.cofactor(0, 0))
      assertEquals(  12f, m.cofactor(0, 1))
      assertEquals( -46f, m.cofactor(0, 2))
      assertEquals(-196f, m.determinant())
    }

    it("can calculate the determinant of a 4x4 matrix") {
      val m = Matrix(4, 4,
        arrayOf(
          floatArrayOf(-2f, -8f,  3f,  5f),
          floatArrayOf(-3f,  1f,  7f,  3f),
          floatArrayOf( 1f,  2f, -9f,  6f),
          floatArrayOf(-6f,  7f,  7f, -9f)
        )
      )
      assertEquals(  690f, m.cofactor(0, 0))
      assertEquals(  447f, m.cofactor(0, 1))
      assertEquals(  210f, m.cofactor(0, 2))
      assertEquals(   51f, m.cofactor(0, 3))
      assertEquals(-4071f, m.determinant())
    }
  }

  context("submatrix") {
    val m = Matrix(3, 3,
      arrayOf(
        floatArrayOf( 1f, 5f,  0f),
        floatArrayOf(-3f, 2f,  7f),
        floatArrayOf( 0f, 6f, -3f)
      )
    )
    val expected = Matrix(2, 2,
      arrayOf(
        floatArrayOf(-3f, 2f),
        floatArrayOf( 0f, 6f)
      )
    )

    val result = m.submatrix(0, 2)
    assertEquals(expected, result)
  }

  context("minor") {
    val m = Matrix(3, 3,
      arrayOf(
        floatArrayOf(3f,  5f,  0f),
        floatArrayOf(2f, -1f, -7f),
        floatArrayOf(6f, -1f,  5f)
      )
    )
    val submatrix = Matrix(2, 2,
      arrayOf(
        floatArrayOf( 5f, 0f),
        floatArrayOf(-1f, 5f)
      )
    )
    assertEquals(25f, submatrix.determinant())
    assertEquals(25f, m.minor(1, 0))
  }

  context("cofactor") {
    val m = Matrix(3, 3,
        arrayOf(
            floatArrayOf(3f,  5f,  0f),
            floatArrayOf(2f, -1f, -7f),
            floatArrayOf(6f, -1f,  5f)
        )
    )
    assertEquals(-12f, m.minor(0, 0))
    assertEquals(-12f, m.cofactor(0, 0))
    assertEquals( 25f, m.minor(1, 0))
    assertEquals(-25f, m.cofactor(1, 0))
  }

  context("inversion") {
    it("knows when a matrix is invertable") {
      val m = Matrix(4, 4,
        arrayOf(
          floatArrayOf(6f,  4f, 4f,  4f),
          floatArrayOf(5f,  5f, 7f,  6f),
          floatArrayOf(4f, -9f, 3f, -7f),
          floatArrayOf(9f,  1f, 7f, -6f)
        )
      )
      assertEquals(-2120f, m.determinant())
      assertTrue(m.isInvertable())
    }

    it("knows when a matrix is not invertable") {
      val m = Matrix(4, 4,
        arrayOf(
          floatArrayOf(-4f,  2f, -2f, -3f),
          floatArrayOf( 9f,  6f,  2f,  6f),
          floatArrayOf( 0f, -5f,  1f, -5f),
          floatArrayOf( 0f,  0f,  0f,  0f)
        )
      )
      assertEquals(0f, m.determinant())
      assertFalse(m.isInvertable())
    }

    it("can invert a matrix") {
      val a = Matrix(4, 4,
        arrayOf(
          floatArrayOf(-5f,  2f,  6f, -8f),
          floatArrayOf( 1f, -5f,  1f,  8f),
          floatArrayOf( 7f,  7f, -6f, -7f),
          floatArrayOf( 1f, -3f,  7f,  4f)
        )
      )
      val b = a.inverse()
      val expected = Matrix(4, 4,
        arrayOf(
          floatArrayOf( 0.21805f,  0.45113f,  0.24060f, -0.04511f),
          floatArrayOf(-0.80827f, -1.45677f, -0.44361f,  0.52068f),
          floatArrayOf(-0.07895f, -0.22368f, -0.05263f,  0.19737f),
          floatArrayOf(-0.52256f, -0.81391f, -0.30075f,  0.30639f)
        )
      )
      val cofactors = a.cofactors();
      val expectedCofactors = Matrix(4, 4,
        arrayOf(
          floatArrayOf(116f, -430f,  -42f, -278f),
          floatArrayOf(240f, -775f, -119f, -433f),
          floatArrayOf(128f, -236f,  -28f, -160f),
          floatArrayOf(-24f,  277f,  105f,  163f)
        )
      )
      assertEquals(expectedCofactors, cofactors)
      assertEquals( 532f, a.determinant())
      assertEquals(-160f, a.cofactor(2, 3))
      assertEquals(-160f / 532f, b.getElement(3, 2))
      assertEquals( 105f, a.cofactor(3, 2))
      assertEquals( 105f / 532f, b.getElement(2, 3))
      assertEquals(expected, b)
    }

    it("can multiply by the inverse") {
      val a = Matrix(4, 4,
        arrayOf(
          floatArrayOf( 3f, -9f,  7f,  3f),
          floatArrayOf( 3f, -8f,  2f, -9f),
          floatArrayOf(-4f,  4f,  4f,  1f),
          floatArrayOf(-6f,  5f, -1f,  1f)
        )
      )
      val b = Matrix(4, 4,
        arrayOf(
          floatArrayOf(8f,  2f,  2f,  2f),
          floatArrayOf(3f, -1f,  7f,  0f),
          floatArrayOf(7f,  0f,  5f,  4f),
          floatArrayOf(6f, -2f,  0f,  5f)
        )
      )
      val c = a * b
      assertEquals(a, c * b.inverse())
    }

    it("can multiply by the identify matrix") {
      val a = Matrix(4, 4,
        arrayOf(
          floatArrayOf( 3f, -9f,  7f,  3f),
          floatArrayOf( 3f, -8f,  2f, -9f),
          floatArrayOf(-4f,  4f,  4f,  1f),
          floatArrayOf(-6f,  5f, -1f,  1f)
        )
      )
      val c = a * Matrix.eye(4)
      assertEquals(a, c)
    }
  }
})