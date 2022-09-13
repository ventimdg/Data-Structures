import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Dominic Ventimiglia
 */
public class BSTStringSetTest  {

    @Test
    public void testPutandcontains() {
        BSTStringSet tester = new BSTStringSet();
        tester.put("b");
        tester.put("a");
        tester.put("c");
        assertTrue(tester.contains("a"));
        assertTrue(tester.contains("b"));
        assertTrue(tester.contains("c"));
        assertFalse(tester.contains("d"));
    }

    @Test
    public void testAslist() {

        BSTStringSet tester = new BSTStringSet();
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
        assertEquals(testlist, answer);
    }

    @Test
    public void testIterator() {
        BSTStringSet tester = new BSTStringSet();
        tester.put("apple");
        tester.put("banana");
        tester.put("cantaloupe");
        tester.put("kiwi");
        tester.put("strawberry");
        tester.put("tangerine");
        BSTStringSet other = new BSTStringSet();
        BSTStringSet.BSThelper(tester, other, "bean", "sza");
        Iterator<String> placeholder = tester.iterator();
        Iterator<String> otherph = tester.iterator("bean", "sza");
        tester.put("apple");
        String x = null;
        while (placeholder.hasNext()) {
            x = placeholder.next();
        }
        while (otherph.hasNext()) {
            x = otherph.next();
        }
        assertEquals(x, "strawberry");
    }
}
