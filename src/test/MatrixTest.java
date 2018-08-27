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
}
