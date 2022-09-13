import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class ECHashStringSetTest  {

    @Test
    public void testPutandcontains() {
        ECHashStringSet tester = new ECHashStringSet();
        tester.put("b");
        tester.put("a");
        tester.put("c");
        tester.put("d");
        tester.put("e");
        tester.put("f");
        tester.put("g");
        assertTrue(tester.contains("a"));
        assertTrue(tester.contains("b"));
        assertTrue(tester.contains("c"));
        assertFalse(tester.contains("l"));
    }

    @Test
    public void testAslist() {
        ECHashStringSet tester = new ECHashStringSet();
        tester.put("b");
        tester.put("a");
        tester.put("c");
        tester.put("g");
        tester.put("e");
        tester.put("d");
        tester.put("f");
        tester.put("k");
        tester.put("i");
        tester.put("j");
        tester.put("h");
        tester.put("l");
        tester.put("k");
        tester.put("d");
        tester.put("f");
        tester.put("k");
        tester.put("i");
        tester.put("j");
        tester.put("h");
        ArrayList<String> answer = new ArrayList<>();
        answer.add("a");
        answer.add("b");
        answer.add("c");
        answer.add("d");
        answer.add("e");
        answer.add("f");
        answer.add("g");
        answer.add("h");
        answer.add("i");
        answer.add("j");
        answer.add("k");
        answer.add("l");
        List testlist = tester.asList();
        for (String str : answer) {
            assertTrue(testlist.contains(str));
        }
    }
}
