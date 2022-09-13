package jump61;

import java.util.ArrayList;
import java.util.Random;

import static jump61.Side.*;

/** An automated Player.
 *  @author P. N. Hilfinger
 */
class AI extends Player {

    /** A new player of GAME initially COLOR that chooses moves automatically.
     *  SEED provides a random-number seed used for choosing moves.
     */
    AI(Game game, Side color, long seed) {
        super(game, color);
        _random = new Random(seed);
    }

    @Override
    String getMove() {
        Board board = getGame().getBoard();
        assert getSide() == board.whoseMove();
        int choice = searchForMove();
        getGame().reportMove(board.row(choice), board.col(choice));
        return String.format("%d %d", board.row(choice), board.col(choice));
    }



    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private int searchForMove() {
        Board work = new Board(getBoard());
        assert getSide() == work.whoseMove();
        _foundMove = -1;
        if (getSide() == RED) {
            minMax(work, 4, true, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        } else {
            minMax(work, 4, true, -1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        return _foundMove;
    }


    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int minMax(Board board, int depth, boolean saveMove,
                       int sense, int alpha, int beta) {
        ArrayList<Integer> moves = validmoves(board, board.whoseMove());
        if (board.numOfSide(RED) == board.size() * board.size()
                || board.numOfSide(BLUE) == board.size() * board.size()
                || depth == 0) {
            return staticEval(board, Integer.MAX_VALUE - 1);
        }
        int bestSoFar = sense * -1 * Integer.MAX_VALUE;
        for (int move : moves) {
            Side mover = board.whoseMove();
            board.addSpot(board.whoseMove(), move);
            int response = minMax(board, depth - 1, false,
                    sense * -1, alpha, beta);
            if (mover == RED) {
                if (response > bestSoFar) {
                    if (saveMove) {
                        _foundMove = move;
                    }
                    bestSoFar = response;
                    alpha = Math.max(alpha, bestSoFar);
                    if (alpha >= beta) {
                        board.undo();
                        return bestSoFar;
                    }
                }
            } else {
                if (response < bestSoFar) {
                    if (saveMove) {
                        _foundMove = move;
                    }
                    bestSoFar = response;
                    beta = Math.min(beta, bestSoFar);
                    if (alpha >= beta) {
                        board.undo();
                        return bestSoFar;
                    }
                }
            }
            board.undo();
        }
        return bestSoFar;
    }

    /**Returns an Arraylist of values representing all valid
     * moves for a palyer color mover.
     * @param b the board
     * @param mover player who needs to move
     * @return
     */
    static ArrayList<Integer> validmoves(Board b, Side mover) {
        ArrayList<Integer> answer  = new ArrayList<>();
        for (int i = 0; i < b.size() * b.size(); i++) {
            Side square = b.get(i).getSide();
            if (square == mover || square == WHITE) {
                answer.add(i);
            }
        }
        return answer;
    }


    /** Return a heuristic estimate of the value of board position B.
     *  Use WINNINGVALUE to indicate a win for Red and -WINNINGVALUE to
     *  indicate a win for Blue. */
    private int staticEval(Board b, int winningValue) {

        if (b.numOfSide(RED) == b.size() * b.size()) {
            return winningValue;
        } else if (b.numOfSide(BLUE) == b.size() * b.size()) {
            return winningValue * -1;
        }
        return b.numOfSide(RED) - b.numOfSide(BLUE);
    }


    /** A random-number generator used for move selection. */
    private Random _random;

    /** Used to convey moves discovered by minMax. */
    private int _foundMove;

}
