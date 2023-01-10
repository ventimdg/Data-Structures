package jump61;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/** Unit tests of Boards.
 *  @author Dominic Ventimiglia
 */

public class AITest {

    @Test
    public void testValidMoves() {
        Board b = new Board(4);
        ArrayList<Integer> answer = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            answer.add(i);
        }
        assertEquals(answer, AI.validmoves(b, b.whoseMove()));
    }
}
