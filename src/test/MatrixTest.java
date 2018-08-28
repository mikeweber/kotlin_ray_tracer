import com.weberapps.rayTracer.Matrix;
import com.weberapps.rayTracer.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatrixTest {
    @Test
    public void testMatrixInitialization() {
        float[][] arrays = new float[][] {
            new float[] { 1.0f, 2.0f, 3.0f, 4.0f },
            new float[] { 5.5f, 6.5f, 7.5f, 8.5f },
            new float[] { 9.0f, 10.0f, 11.0f, 12.0f },
            new float[] { 13.5f, 14.5f, 15.5f, 16.5f }
        };
        Matrix m = new Matrix(4, 4, arrays);
        assertEquals(1.0f, m.getElement(0, 0));
        assertEquals(4.0f, m.getElement(0, 3));
        assertEquals(5.5f, m.getElement(1, 0));
        assertEquals(7.5f, m.getElement(1, 2));
        assertEquals(11f, m.getElement(2, 2));
        assertEquals(13.5f, m.getElement(3, 0));
        assertEquals(15.5f, m.getElement(3, 2));
    }

    @Test
    public void testEmptyMatrixInitialization() {
        Matrix m = new Matrix(3, 2);
        assertEquals(0f, m.getElement(0, 0));
        assertEquals(0f, m.getElement(2, 1));
    }

    @Test
    public void testTwoByTwoMatrix() {
        Matrix m = new Matrix(2, 2);
        assertEquals(2, m.size());
    }

    @Test
    public void testThreeByThreeMatrix() {
        Matrix m = new Matrix(3, 3);
        assertEquals(3, m.size());
    }

    @Test
    public void testMatrixToMatrixMultiplcation() {
        float[][] aArrays = new float[][] {
                new float[] { 1f, 2f, 3f, 4f },
                new float[] { 2f, 3f, 4f, 5f },
                new float[] { 3f, 4f, 5f, 6f },
                new float[] { 4f, 5f, 6f, 7f }
        };
        float[][] bArrays = new float[][] {
                new float[] { 0f, 1f, 2f, 4f },
                new float[] { 1f, 2f, 4f, 8f },
                new float[] { 2f, 4f, 8f, 16f },
                new float[] { 4f, 8f, 16f, 32f }
        };
        float[][] expectedArrays = new float[][] {
                new float[] { 24f, 49f, 98f, 196f },
                new float[] { 31f, 64f, 128f, 256f },
                new float[] { 38f, 79f, 158f, 316f },
                new float[] { 45f, 94f, 188f, 376f }
        };

        Matrix a = new Matrix(4, 4, aArrays);
        Matrix b = new Matrix(4, 4, bArrays);
        Matrix expected = new Matrix(4, 4, expectedArrays);
        Matrix result = a.times(b);
        assertEquals(expected, result);
    }

    @Test
    public void testMatrixToTupleMultiplcation() {
        float[][] matrixArray = new float[][] {
                new float[] { 1f, 2f, 3f, 4f },
                new float[] { 2f, 4f, 4f, 2f },
                new float[] { 8f, 6f, 4f, 1f },
                new float[] { 0f, 0f, 0f, 1f }
        };
        Matrix matrix = new Matrix(4, 4, matrixArray);
        Tuple tuple = new Tuple(1f, 2f, 3f, 1f);
        Tuple result = matrix.times(tuple);
        Tuple expected = new Tuple(18f, 24f, 33f, 1f);
        assertEquals(expected, result);
    }

    @Test
    public void testIdentityMatrixMultiplication() {
        float[][] matrixArrays = new float[][] {
                new float[] { 0f, 1f, 2f, 4f },
                new float[] { 1f, 2f, 4f, 8f },
                new float[] { 2f, 4f, 8f, 16f },
                new float[] { 4f, 8f, 16f, 32f }
        };
        Matrix m = new Matrix(4, 4, matrixArrays);
        Matrix eye = Matrix.Companion.eye(4);
        assertEquals(m, m.times(eye));
    }

    @Test
    public void testTupleIdentityMultiplication() {
        Tuple a = new Tuple(1f, 2f, 3f, 4f);
        Matrix eye = Matrix.Companion.eye(4);
        assertEquals(a, eye.times(a));
    }

    @Test
    public void testMatrixTranspose() {
        Matrix m = new Matrix(4, 4, new float[][] {
                new float[] { 0f, 9f, 3f, 0f },
                new float[] { 9f, 8f, 0f, 8f },
                new float[] { 1f, 8f, 5f, 3f },
                new float[] { 0f, 0f, 5f, 8f }
        });
        Matrix expected = new Matrix(4, 4, new float[][] {
                new float[] { 0f, 9f, 1f, 0f },
                new float[] { 9f, 8f, 8f, 0f },
                new float[] { 3f, 0f, 5f, 5f },
                new float[] { 0f, 8f, 3f, 8f }
        });
        assertEquals(expected, m.transpose());
    }

    @Test
    public void testMatrixDeterminantIsZeroWhenMatrixIsTooSmall() {
        Matrix m1 = new Matrix(1, 4, new float[][] {
                new float[] { 1f, 5f, 4f, 3f },
        });
        Matrix m2 = new Matrix(4, 1, new float[][] {
                new float[] {  1f },
                new float[] { -3f },
                new float[] { -3f },
                new float[] {  4f }
        });

        assertEquals(0f, m1.determinant());
        assertEquals(0f, m2.determinant());
    }

    @Test
    public void testMatrixDeterminant() {
        Matrix m = new Matrix(2, 2, new float[][] {
                new float[] { 1f, 5f },
                new float[] { -3f, 2f }
        });
        assertEquals(17f, m.determinant());
    }

    @Test
    public void testSubmatrix() {
        Matrix m = new Matrix(3, 3, new float[][] {
                new float[] { 1f, 5f, 0f },
                new float[] { -3f, 2f, 7f },
                new float[] { 0f, 6f, -3f }
        });
        Matrix expected = new Matrix(2, 2, new float[][] {
                new float[] { -3f, 2f },
                new float[] { 0f, 6f }
        });
        Matrix result = m.submatrix(0, 2);

        assertEquals(expected, result);
    }

    @Test
    public void testMinor() {
        Matrix m = new Matrix(3, 3, new float[][] {
                new float[] { 3f, 5f, 0f },
                new float[] { 2f, -1f, -7f },
                new float[] { 6f, -1f, 5f }
        });
        Matrix submatrix = new Matrix(2, 2, new float[][] {
                new float[] { 5f, 0f },
                new float[] { -1f, 5f }
        });
        assertEquals(25, submatrix.determinant());
        assertEquals(25, m.minor(1, 0));
    }

    @Test
    public void testCofactor() {
        Matrix m = new Matrix(3, 3, new float[][] {
                new float[] { 3f, 5f, 0f },
                new float[] { 2f, -1f, -7f },
                new float[] { 6f, -1f, 5f }
        });
        assertEquals(-12, m.minor(0, 0));
        assertEquals(-12, m.cofactor(0, 0));
        assertEquals(25, m.minor(1, 0));
        assertEquals(-25, m.cofactor(1, 0));
    }

    @Test
    public void testDeterminantOfThreeByThreeMatrix() {
        Matrix m = new Matrix(3, 3, new float[][] {
                new float[] { 1f, 2f, 6f },
                new float[] { -5f, 8f, -4f },
                new float[] { 2f, 6f, 4f }
        });
        assertEquals(56, m.cofactor(0, 0));
        assertEquals(12, m.cofactor(0, 1));
        assertEquals(-46, m.cofactor(0, 2));
        assertEquals(-196, m.determinant());
    }

    @Test
    public void testDeterminantOfFourByFourMatrix() {
        Matrix m = new Matrix(4, 4, new float[][] {
                new float[] { -2f, -8f, 3f, 5f },
                new float[] { -3f, 1f, 7f, 3f },
                new float[] { 1f, 2f, -9f, 6f },
                new float[] { -6f, 7f, 7f, -9f }
        });
        assertEquals(690, m.cofactor(0, 0));
        assertEquals(447, m.cofactor(0, 1));
        assertEquals(210, m.cofactor(0, 2));
        assertEquals( 51, m.cofactor(0, 3));
        assertEquals(-4071, m.determinant());
    }

    @Test
    public void testIsInvertable() {
        Matrix m = new Matrix(4, 4, new float[][] {
                new float[] { 6f, 4f, 4f, 4f },
                new float[] { 5f, 5f, 7f, 6f },
                new float[] { 4f, -9f, 3f, -7f },
                new float[] { 9f, 1f, 7f, -6f }
        });
        assertEquals(-2120, m.determinant());
        assert(m.isInvertable());
    }

    @Test void testIsNotInvertable() {
        Matrix m = new Matrix(4, 4, new float[][] {
                new float[] { -4f, 2f, -2f, -3f },
                new float[] { 9f, 6f, 2f, 6f },
                new float[] { 0f, -5f, 1f, -5f },
                new float[] { 0f, 0f, 0f, 0f }
        });
        assertEquals(0, m.determinant());
        assert(!m.isInvertable());
    }

    @Test void testInverse() {
        Matrix a = new Matrix(4, 4, new float[][] {
                new float[] { -5f, 2f, 6f, -8f },
                new float[] { 1f, -5f, 1f, 8f },
                new float[] { 7f, 7f, -6f, -7f },
                new float[] { 1f, -3f, 7f, 4f }
        });
        Matrix b = a.inverse();
        Matrix expected = new Matrix(4, 4, new float[][] {
                new float[] { 0.21805f,  0.45113f,  0.24060f, -0.04511f },
                new float[] {-0.80827f, -1.45677f, -0.44361f,  0.52068f },
                new float[] {-0.07895f, -0.22368f, -0.05263f,  0.19737f },
                new float[] {-0.52256f, -0.81391f, -0.30075f,  0.30639f }
        });
        Matrix cofactors = a.cofactors();
        Matrix expectedCofactors = new Matrix(4, 4, new float[][] {
                new float[] {  116f, -430f,  -42f, -278f },
                new float[] {  240f, -775f, -119f, -433f },
                new float[] {  128f, -236f,  -28f, -160f },
                new float[] {  -24f,  277f,  105f,  163f }
        });
        assertEquals(expectedCofactors, cofactors);
        assertEquals(532, a.determinant());
        assertEquals(-160, a.cofactor(2, 3));
        assertEquals(-160f / 532f, b.getElement(3, 2));
        assertEquals(105, a.cofactor(3, 2));
        assertEquals(105f / 532f, b.getElement(2, 3));
        assertEquals(expected, b);
    }
}
