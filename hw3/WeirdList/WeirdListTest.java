import org.junit.Test;
import static org.junit.Assert.*;

import ucb.junit.textui;

import java.util.ArrayList;

/** Tests of WeirdList.
 *  @author Josh Hug
 *  @author P. N. Hilfinger
 */
public class WeirdListTest {
    @Test
    public void testList() {
        WeirdList wl1 = new WeirdList(5, WeirdList.EMPTY);
        WeirdList wl2 = new WeirdList(6, wl1);
        WeirdList wl3 = new WeirdList(10, wl2);
        assertEquals(3, wl3.length());
        assertEquals(1, wl1.length());

        assertEquals(" 10 6 5", wl3.toString());
    }

    @Test
    public void testreq() {
        assertEquals(true, WeirdListClient.prereq(new int[][] {{1, 0}, {1, 2}}));
        assertEquals(false, WeirdListClient.prereq(new int[][] {{1, 0}, {0, 1}}));

    }

    @Test
    public void testpermute(){
        WeirdList.spiralOrder(new int[][] {{3}, {2}});

    }

    public static void main(String[] args) {
        System.exit(textui.runClasses(WeirdListTest.class));
    }

}
