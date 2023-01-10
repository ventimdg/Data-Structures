import static org.junit.Assert.*;
import org.junit.Test;

public class IntListTest {

    /** Sample test that verifies correctness of the IntList.list static
     *  method. The main point of this is to convince you that
     *  assertEquals knows how to handle IntLists just fine.
     */

    @Test
    public void testList() {
        IntList one = new IntList(1, null);
        IntList twoOne = new IntList(2, one);
        IntList threeTwoOne = new IntList(3, twoOne);

        IntList x = IntList.list(3, 2, 1);
        assertEquals(threeTwoOne, x);
    }

    @Test
    public void testSum() {
        assertEquals(15, IntList.sum(IntList.list(1, 2, 3, 4, 5)));
    }

    @Test
    public void testdSquareList() {
        IntList L = IntList.list(1, 2, 3);
        IntList.dSquareList(L);
        assertEquals(IntList.list(1, 4, 9), L);
    }

    /*  Do not use the "new" keyword in your tests. You can create
     *  lists using the handy IntList.list method.
     *
     *  Make sure to include test cases involving lists of various sizes
     *  on both sides of the operation. That includes the empty list, which
     *  can be instantiated, for example, with
     *  IntList empty = IntList.list().
     */
    
    @Test
    public void testSquareListRecursive() {
        IntList L = IntList.list(1, 2, 3);
        IntList B = IntList.squareListRecursive(L);
        assertEquals(B, IntList.list(1, 4, 9));
        assertEquals(L, IntList.list(1, 2, 3));
        IntList empty = IntList.list();
        assertEquals(IntList.squareListRecursive(empty), empty);
        IntList C = IntList.list(1);
        IntList D = IntList.squareListRecursive(C);
        assertEquals(D, IntList.list(1));
        assertEquals(C, IntList.list(1));
        IntList E = IntList.list(2);
        IntList F = IntList.squareListRecursive(E);
        assertEquals(F, IntList.list(4));
        assertEquals(E, IntList.list(2));
        IntList H = IntList.list(3, 4);
        IntList I = IntList.squareListRecursive(H);
        assertEquals(I, IntList.list(9, 16));
        assertEquals(H, IntList.list(3, 4));


        // TODO: test for correctness
        // TODO: test for non-destructiveness

        // TODO: remove the placeholder line below

    }

    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(IntListTest.class));
    }
}
