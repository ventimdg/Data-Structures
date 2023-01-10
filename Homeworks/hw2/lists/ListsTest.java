package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** Performs tests of the Lists Class
 *
 *  @author Dominc Ventimiglia
 */

public class ListsTest {
    IntList d;
    int[][] a;
    /** Tests naturalRuns method
     */
    @Test
    public void testNaturalRuns() {
        d = IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11);
        a = new int[][] {{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}};
        assertEquals(IntListList.list(a), Lists.naturalRuns(d));
        d = IntList.list(10, 9, 8, 7, 6, 5);
        a = new int[][] {{10}, {9}, {8}, {7}, {6}, {5}};
        assertEquals(IntListList.list(a), Lists.naturalRuns(d));
        d = IntList.list(1, 2, 3, 4, 5, 6);
        a = new int[][] {{1, 2, 3, 4, 5, 6}};
        assertEquals(IntListList.list(a), Lists.naturalRuns(d));
        d = IntList.list();
        a = new int[][] {};
        assertEquals(IntListList.list(a), Lists.naturalRuns(d));
    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
