import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        assertEquals(9, MultiArr.maxValue(new int[][] {{1,3,4}, {1}, {5,6,7,8}, {7,9} }));
        assertEquals(20, MultiArr.maxValue(new int[][] {{20,3,4}, {1}, {5,6,7,8}, {7,9} }));
        assertEquals(17, MultiArr.maxValue(new int[][] {{1,3,4}, {1}, {5,17,7,8}, {7,9} }));
        assertEquals(22, MultiArr.maxValue(new int[][] {{1,3,4}, {22}, {5,6,7,8}, {22,9} }));

    }

    @Test
    public void testAllRowSums() {
        assertArrayEquals(new int[] {8, 1, 26, 16}, MultiArr.allRowSums(new int[][] {{1,3,4}, {1}, {5,6,7,8}, {7,9} }));
        assertArrayEquals(new int[] {}, MultiArr.allRowSums(new int[][] {}));
        assertArrayEquals(new int[] {0, 0, 0, 0}, MultiArr.allRowSums(new int[][] {{}, {}, {}, {} }));
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
