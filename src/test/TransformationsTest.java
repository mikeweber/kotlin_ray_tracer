import com.weberapps.rayTracer.Matrix;
import com.weberapps.rayTracer.Point;
import com.weberapps.rayTracer.Transformation;
import org.junit.jupiter.api.Test;

import static com.weberapps.rayTracer.TupleKt.TAU;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransformationsTest {
    @Test
    public void testTranslation() {
        Matrix transform = Transformation.Companion.translation(5f, -3f, 2f);
        Point p = new Point(-3, 4, 5);
        assertEquals(new Point(2f, 1f, 7f), transform.times(p));
    }

    @Test
    public void testScale() {
        Matrix transform = Transformation.Companion.scale(2f, 3f, 4f);
        Point p = new Point(-4f, 6f, 8f);
        assertEquals(new Point(-8f, 18f, 32f), transform.times(p));
    }

    @Test
    public void testRotateX() {
        Point p = new Point(0f, 1f, 0f);
        Matrix eighth = Transformation.Companion.rotateX(TAU / 8);
        Matrix quarter = Transformation.Companion.rotateX(TAU / 4);

        assertEquals(new Point(0f, (float)(Math.sqrt(2) / 2f), (float)(Math.sqrt(2) / 2f)), eighth.times(p));
        assertEquals(new Point(0f, 0f, 1f), quarter.times(p));
    }

    @Test
    public void testRotateY() {
        Point p = new Point(0f, 0f, 1f);
        Matrix eighth = Transformation.Companion.rotateY(TAU / 8);
        Matrix quarter = Transformation.Companion.rotateY(TAU / 4);

        assertEquals(new Point((float)(Math.sqrt(2) / 2f), 0f, (float)(Math.sqrt(2) / 2f)), eighth.times(p));
        assertEquals(new Point(1f, 0f, 0f), quarter.times(p));
    }

    @Test
    public void testRotateZ() {
        Point p = new Point(0f, 1f, 0f);
        Matrix eighth = Transformation.Companion.rotateZ(TAU / 8);
        Matrix quarter = Transformation.Companion.rotateZ(TAU / 4);

        assertEquals(new Point(-(float)(Math.sqrt(2) / 2f), (float)(Math.sqrt(2) / 2f), 0f), eighth.times(p));
        assertEquals(new Point(-1f, 0f, 0f), quarter.times(p));
    }
}
