package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author
 */
public abstract class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    abstract Permutation getNewPermutation(String cycles, Alphabet alphabet);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet(String chars);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet();

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }

    @Test(expected = EnigmaException.class)
    public void testbadparen() {
        getNewPermutation("(AB) () (C)", getNewAlphabet("ABCD"));
    }

    @Test(expected = EnigmaException.class)
    public void testdupcycle() {
        getNewPermutation("(BA) (CD) (AC)", getNewAlphabet("ABCD"));
    }

    @Test(expected = EnigmaException.class)
    public void testweridcyc() {
        getNewPermutation("(BACD) (C)", getNewAlphabet("ABCD"));
    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation d = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        d.permute('E');
    }

    @Test(expected = EnigmaException.class)
    public void testemptyAlphabet() {
        getNewPermutation("(BACD)", getNewAlphabet(""));
    }

    @Test(expected = EnigmaException.class)
    public void testbadcycle() {
        getNewPermutation("(BACAD)", getNewAlphabet("ABCD"));
    }

    @Test(expected = EnigmaException.class)
    public void testdoublecycle() {
        getNewPermutation("(BACCD)", getNewAlphabet("ABCD"));
    }

    @Test(expected = EnigmaException.class)
    public void testdoubleAlphabet() {
        getNewPermutation("(BACD)", getNewAlphabet("ABCDD"));
    }

    @Test(expected = EnigmaException.class)
    public void testdublAlphabet() {
        getNewPermutation("(BACD)", getNewAlphabet("ABBCD"));
    }

    @Test(expected = EnigmaException.class)
    public void testNoParenCycle() {
        getNewPermutation("BACD", getNewAlphabet("ABCD"));
    }

    @Test(expected = EnigmaException.class)
    public void testwhite1() {
        getNewPermutation("(BA CD)", getNewAlphabet("ABCD"));
    }

    @Test(expected = EnigmaException.class)
    public void testCycleOutofAlphabet() {
        getNewPermutation("(BACDE)", getNewAlphabet("ABCD"));
    }

    @Test
    public void testAlphabet() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals(p.alphabet().size(), 4);
        assertEquals(p.alphabet().toChar(0), 'A');
        assertEquals(p.alphabet().toChar(3), 'D');
        assertEquals(p.alphabet().toChar(1), 'B');
        assertEquals(p.alphabet().toInt('D'), 3);
        assertEquals(p.alphabet().toInt('B'), 1);
        assertEquals(p.alphabet().toInt('A'), 0);
        assertFalse(p.alphabet().contains('E'));
        assertTrue(p.alphabet().contains('A'));
        assertTrue(p.alphabet().contains('D'));
        assertTrue(p.alphabet().contains('B'));
        Permutation z = getNewPermutation("", getNewAlphabet(""));
        assertFalse(z.alphabet().contains('A'));
    }

    @Test
    public void testInvertChar() {
        Permutation d = getNewPermutation("(ACE) (GBJI) (DFH)", getNewAlphabet("ABCDEFGHIJK"));
        assertEquals(d.invert('A'), 'E');
        assertEquals(d.invert('G'), 'I');
        assertEquals(d.invert('D'), 'H');
        assertEquals(d.invert('K'), 'K');
        assertEquals(d.invert('J'), 'B');
        assertEquals(d.invert(0), 4);
        assertEquals(d.invert(0), 4);
        assertEquals(d.invert(6), 8);
        assertEquals(d.invert(3), 7);
        assertEquals(d.invert(10), 10);
        assertEquals(d.invert(9), 1);
        assertEquals(d.invert(0), 4);
        Permutation j = getNewPermutation("(ABCD)", getNewAlphabet("ABCD"));
        assertEquals('A', j.invert(j.invert('C')));
        assertEquals('D', j.invert(j.invert('B')));
        assertEquals(0, j.invert(j.invert(2)));
        assertEquals(3, j.invert(j.invert(1)));
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        Alphabet alphabet = p.alphabet();
        assertEquals('B', p.invert('A'));
        assertEquals('D', p.invert('B'));
        assertEquals('C', p.invert('D'));
        assertEquals(1, p.invert(0));
        assertEquals(1, p.invert(4));
        assertEquals(1, p.invert(-4));
        assertEquals(3, p.invert(1));
        assertEquals(3, p.invert(5));
        assertEquals(3, p.invert(-3));
        assertEquals(2, p.invert(3));
        assertEquals(2, p.invert(7));
        assertEquals(2, p.invert(-1));
        assertEquals(alphabet, p.alphabet());
        Permutation x = getNewPermutation("(BACD)", getNewAlphabet("ABCDE"));
        assertEquals('E', x.invert('E'));
        assertEquals(4, x.invert(4));
        assertEquals(4, x.invert(-1));
        assertEquals(4, x.invert(9));
        Permutation y = getNewPermutation("", getNewAlphabet("ABCDE"));
        assertEquals('A', y.invert('A'));
        assertEquals('B', y.invert('B'));
        assertEquals('C', y.invert('C'));
        assertEquals('D', y.invert('D'));
        assertEquals('E', y.invert('E'));
        assertEquals(0, y.invert(0));
        assertEquals(1, y.invert(1));
        assertEquals(2, y.invert(2));
        assertEquals(3, y.invert(3));
        assertEquals(4, y.invert(4));
    }
    @Test
    public void testSize() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals(4, p.size());
        Permutation x = getNewPermutation("", getNewAlphabet(""));
        assertEquals(0, x.size());
        Permutation l = getNewPermutation("", getNewAlphabet("ABCD"));
        assertEquals(4, l.size());
    }

    @Test
    public void testPermute() {
        Permutation d = getNewPermutation("(ACE) (GBJI) (DFH)", getNewAlphabet("ABCDEFGHIJK"));
        assertEquals(d.permute('E'), 'A');
        assertEquals(d.permute('I'), 'G');
        assertEquals(d.permute('H'), 'D');
        assertEquals(d.permute('K'), 'K');
        assertEquals(d.permute('B'), 'J');
        assertEquals(d.permute(4), 0);
        assertEquals(d.permute(4), 0);
        assertEquals(d.permute(8), 6);
        assertEquals(d.permute(7), 3);
        assertEquals(d.permute(10), 10);
        assertEquals(d.permute(1), 9);
        assertEquals(d.permute(4), 0);
        Permutation j = getNewPermutation("(ABCD)", getNewAlphabet("ABCD"));
        assertEquals('C', j.permute(j.permute('A')));
        assertEquals('A', j.permute(j.permute('C')));
        assertEquals(2, j.permute(j.permute(0)));
        assertEquals(0, j.permute(j.permute(2)));
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals('C', p.permute('A'));
        assertEquals('A', p.permute('B'));
        assertEquals('B', p.permute('D'));
        assertEquals(2, p.permute(0));
        assertEquals(2, p.permute(4));
        assertEquals(2, p.permute(-4));
        assertEquals(0, p.permute(1));
        assertEquals(0, p.permute(5));
        assertEquals(0, p.permute(-3));
        assertEquals(1, p.permute(3));
        assertEquals(1, p.permute(7));
        assertEquals(1, p.permute(-1));
        Permutation x = getNewPermutation("(BACD)", getNewAlphabet("ABCDE"));
        assertEquals('E', x.permute('E'));
        assertEquals(4, x.permute(4));
        assertEquals(4, x.permute(-1));
        assertEquals(4, x.permute(9));
        Permutation y = getNewPermutation("", getNewAlphabet("ABCDE"));
        assertEquals('A', y.permute('A'));
        assertEquals('B', y.permute('B'));
        assertEquals('C', y.permute('C'));
        assertEquals('D', y.permute('D'));
        assertEquals('E', y.permute('E'));
        assertEquals(0, y.permute(0));
        assertEquals(1, y.permute(1));
        assertEquals(2, y.permute(2));
        assertEquals(3, y.permute(3));
        assertEquals(4, y.permute(4));
    }

    @Test
    public void testDerangement() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertTrue(p.derangement());
        Permutation n = getNewPermutation("(BA) (CD)", getNewAlphabet("ABCD"));
        assertTrue(n.derangement());
        Permutation x = getNewPermutation("(BACD)", getNewAlphabet("ABCDE"));
        assertFalse(x.derangement());
        Permutation y = getNewPermutation("", getNewAlphabet("ABCDE"));
        assertFalse(y.derangement());
        Permutation z = getNewPermutation("(BAC) (D)", getNewAlphabet("ABCD"));
        assertFalse(z.derangement());
    }
}
