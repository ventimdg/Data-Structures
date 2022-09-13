package arrays;

import org.junit.Test;

import static org.junit.Assert.*;

/** Performs tests of the Array class
 *  @author Dominic Ventimiglia
 */

public class ArraysTest {
    int[] a;
    int[] b;
    int[] c;
    int[][] d;

    /** Performs assert tests to see if the catenate method is correct
     */
    @Test
    public void testCatenate() {
        a = new int[]{1, 2, 3};
        b = new int[]{4, 5, 6};
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, Arrays.catenate(a, b));
        assertArrayEquals(a, Arrays.catenate(a, c));
        assertArrayEquals(b, Arrays.catenate(c, b));
        assertArrayEquals(c, Arrays.catenate(c, c));
    }

    /** Performs assert tests to see if the remove method is correct
     */
    @Test
    public void testRemove() {
        assertArrayEquals(new int[] {0, 3}, Arrays.remove(new int[] {0, 1, 2, 3}, 1, 2));
        assertArrayEquals(null, Arrays.remove(new int[] {0, 1, 2, 3}, 1, 4));
        assertArrayEquals(new int[] {0}, Arrays.remove(new int[] {0, 1, 2, 3}, 1, 3));
        assertArrayEquals(new int[] {0, 1, 2, 3}, Arrays.remove(new int[] {0, 1, 2, 3}, 2, 0));
        assertArrayEquals(new int[] {0, 1, 2}, Arrays.remove(new int[] {0, 1, 2, 3}, 3, 1));
    }
    /** Performs assert tests to see if the
     * natural runs method is correct
     */
    @Test
    public void testNaturalRuns() {
        a = new int[]{1, 3, 7, 5, 4, 6, 9, 10};
        d = new int[][]{{1, 3, 7}, {5}, {4, 6, 9, 10}};
        assertArrayEquals(d, Arrays.naturalRuns(a));
        a = new int[]{1, 3, 7, 5, 4, 6, 9, 10, 10, 11};
        d = new int[][] {{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}};
        assertArrayEquals(d, Arrays.naturalRuns(a));
        a = new int[]{10, 9, 8, 7, 6, 5};
        d = new int[][] {{10}, {9}, {8}, {7}, {6}, {5}};
        assertArrayEquals(d, Arrays.naturalRuns(a));
        a = new int[]{1, 2, 3, 4, 5, 6};
        d = new int[][] {{1, 2, 3, 4, 5, 6}};
        assertArrayEquals(d, Arrays.naturalRuns(a));
        a = new int[]{};
        d = new int[][] {};
        assertArrayEquals(d, Arrays.naturalRuns(a));


    }
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
