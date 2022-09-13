package image;

import org.junit.Test;
import static org.junit.Assert.*;

/** Performs Tests of the MatrixUtils Class
 *  @author Dominic Ventimiglia
 */

public class MatrixUtilsTest {
    double error = .01;
    double a;
    double[][] b;
    double[][] c;
    /** Performs assert tests to see if the get method is correct
     */
    @Test
    public void testGet() {
        b = new double[][] {{1000000,   1000000,   1000000,   1000000},
                {1000000,     75990,     30003,  1000000},
                {1000000,     30002,   103046,   1000000},
                {1000000,     29515,     38273,   1000000},
                {1000000,     73403,     35399,   1000000},
                {1000000,  1000000,  1000000,   1000000}};
        a = 1000000;
        assertEquals(a, MatrixUtils.get(b, 5, 3), error);
        assertEquals(a, MatrixUtils.get(b, 0, 0), error);
        assertEquals(Double.POSITIVE_INFINITY, MatrixUtils.get(b, 4, 4), error);
        assertEquals(Double.POSITIVE_INFINITY, MatrixUtils.get(b, -1, 0), error);
        a = 75990;
        assertEquals(a, MatrixUtils.get(b, 1, 1), error);
    }
    /** Performs assert tests to see if the AccumulateVertical method is correct
     */
    @Test
    public void testAccumulateVertical() {
        b = new double[][] {{1000000,   1000000,   1000000,   1000000},
                {1000000,     75990,     30003,  1000000},
                {1000000,     30002,   103046,   1000000},
                {1000000,     29515,     38273,   1000000},
                {1000000,     73403,     35399,   1000000},
                {1000000,  1000000,  1000000,   1000000}};
        c = new double[][] {{1000000,   1000000,   1000000,   1000000},
                {2000000,  1075990,   1030003,   2000000},
                {2075990,   1060005,   1133049,   2030003},
                {2060005,   1089520,   1098278,   2133049},
                {2089520,   1162923,   1124919,   2098278},
                {2162923,   2124919,   2124919,   2124919}};
        assertArrayEquals(c, MatrixUtils.accumulateVertical(b));
    }
    @Test
    public void testTranspose() {
        b = new double[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        c = new double[][] {{1, 4, 7}, {2, 5, 8}, {3, 6, 9}};
        assertArrayEquals(c, MatrixUtils.transpose(b));
    }
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}
