package jump61;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Formatter;

import java.util.function.Consumer;

import static jump61.Side.*;

/** Represents the state of a Jump61 game.  Squares are indexed either by
 *  row and column (between 1 and size()), or by square number, numbering
 *  squares by rows, with squares in row 1 numbered from 0 to size()-1, in
 *  row 2 numbered from size() to 2*size() - 1, etc. (i.e., row-major order).
 *
 *  A Board may be given a notifier---a Consumer<Board> whose
 *  .accept method is called whenever the Board's contents are changed.
 *
 *  @author Dominic Ventimiglia
 */
class Board {

    /** An uninitialized Board.  Only for use by subtypes. */
    protected Board() {
        _notifier = NOP;
    }

    /** An N x N board in initial configuration. */
    Board(int N) {
        _whitesquares = N * N;
        _notifier = NOP;
        _board = new Square[N][N];
        _size = N;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                _board[i][j] = Square.square(WHITE, 0);
            }
        }
        _readonlyBoard = new ConstantBoard(this);
    }

    /** A board whose initial contents are copied from BOARD0, but whose
     *  undo history is clear, and whose notifier does nothing. */
    Board(Board board0) {
        this(board0.size());
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                Side color = board0.get(i + 1, j + 1).getSide();
                int spots = board0.get(i + 1, j + 1).getSpots();
                _board[i][j] = Square.square(color, spots);
            }
        }
        _whitesquares = board0._whitesquares;
        _redsquares = board0._redsquares;
        _bluesquares = board0._bluesquares;
        _gameover = board0._gameover;
        _nummoves = board0._nummoves;
        _boardhistory = new ArrayList<>();
        _history = -1;
        _readonlyBoard = new ConstantBoard(board0);
        _notifier = NOP;
    }

    /** Returns a readonly version of this board. */
    Board readonlyBoard() {
        return _readonlyBoard;
    }

    /** (Re)initialize me to a cleared board with N squares on a side. Clears
     *  the undo history and sets the number of moves to 0. */
    void clear(int N) {
        _size = N;
        _board = new Square[N][N];
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                _board[i][j] = Square.square(WHITE, 0);
            }
        }
        _boardhistory = new ArrayList<>();
        _history = -1;
        _nummoves = 0;
        _redsquares = 0;
        _whitesquares = N * N;
        _bluesquares = 0;
        _gameover = false;
        _readonlyBoard = new ConstantBoard(this);
        announce();
    }

    /** Copy the contents of BOARD into me. */
    void copy(Board board) {
        _board = new Square[board.size()][board.size()];
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                Side color = board.get(i + 1, j + 1).getSide();
                int spots = board.get(i + 1, j + 1).getSpots();
                _board[i][j] = Square.square(color, spots);
            }
        }
        _size = board._size;
        _nummoves = board._nummoves;
        _redsquares = board._redsquares;
        _bluesquares = board._bluesquares;
        _whitesquares = board._whitesquares;
        _gameover = board._gameover;
        _readonlyBoard = new ConstantBoard(board);
    }

    /** Copy the contents of BOARD into me, without modifying my undo
     *  history. Assumes BOARD and I have the same size. */
    private void internalCopy(Board board) {
        assert size() == board.size();
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                Side color = board.get(i + 1, j + 1).getSide();
                int spots = board.get(i + 1, j + 1).getSpots();
                _board[i][j] = Square.square(color, spots);
            }
        }
        _size = board._size;
        _nummoves = board._nummoves;
        _notifier = board._notifier;
        _redsquares = board._redsquares;
        _bluesquares = board._bluesquares;
        _whitesquares = board._whitesquares;
        _gameover = board._gameover;
        _readonlyBoard = new ConstantBoard(board);
    }

    /** Return the number of rows and of columns of THIS. */
    int size() {
        return _size;
    }

    /** Returns the contents of the square at row R, column C
     *  1 <= R, C <= size (). */
    Square get(int r, int c) {
        return get(sqNum(r, c));
    }

    /** Returns the contents of square #N, numbering squares by rows, with
     *  squares in row 1 number 0 - size()-1, in row 2 numbered
     *  size() - 2*size() - 1, etc. */
    Square get(int n) {
        return _board[row(n) - 1][col(n) - 1];
    }

    /** Returns the total number of spots on the board. */
    int numPieces() {
        int answer = 0;
        for (int i = 0; i < size() * size(); i++) {
            answer += get(i).getSpots();
        }
        return answer;
    }

    /** Returns the Side of the player who would be next to move.  If the
     *  game is won, this will return the loser (assuming legal position). */
    Side whoseMove() {
        return ((numPieces() + size()) & 1) == 0 ? RED : BLUE;
    }

    /** Return true iff row R and column C denotes a valid square. */
    final boolean exists(int r, int c) {
        return 1 <= r && r <= size() && 1 <= c && c <= size();
    }

    /** Return true iff S is a valid square number. */
    final boolean exists(int s) {
        int N = size();
        return 0 <= s && s < N * N;
    }

    /** Return the row number for square #N. */
    final int row(int n) {
        return n / size() + 1;
    }

    /** Return the column number for square #N. */
    final int col(int n) {
        return n % size() + 1;
    }

    /** Return the square number of row R, column C. */
    final int sqNum(int r, int c) {
        return (c - 1) + (r - 1) * size();
    }

    /** Return a string denoting move (ROW, COL)N. */
    String moveString(int row, int col) {
        return String.format("%d %d", row, col);
    }

    /** Return a string denoting move N. */
    String moveString(int n) {
        return String.format("%d %d", row(n), col(n));
    }

    /** Returns true iff it would currently be legal for PLAYER to add a spot
        to square at row R, column C. */
    boolean isLegal(Side player, int r, int c) {
        return isLegal(player, sqNum(r, c));
    }

    /** Returns true iff it would currently be legal for PLAYER to add a spot
     *  to square #N. */
    boolean isLegal(Side player, int n) {
        return player.playableSquare(get(n).getSide());
    }

    /** Returns true iff PLAYER is allowed to move at this point. */
    boolean isLegal(Side player) {
        return whoseMove().equals(player) && !_gameover;
    }

    /** Returns the winner of the current position, if the game is over,
     *  and otherwise null. */
    final Side getWinner() {
        if (_redsquares == size() * size()) {
            return RED;
        } else if (_bluesquares == size() * size()) {
            return BLUE;
        } else {
            return null;
        }
    }

    /** Return the number of squares of given SIDE. */
    int numOfSide(Side side) {
        if (side == BLUE) {
            return _bluesquares;
        } else if (side == RED) {
            return _redsquares;
        } else {
            return _whitesquares;
        }
    }

    /** Add a spot from PLAYER at row R, column C.  Assumes
     *  isLegal(PLAYER, R, C). */
    void addSpot(Side player, int r, int c) {
        addSpot(player, sqNum(r, c));
    }

    /** Add a spot from PLAYER at square #N.  Assumes isLegal(PLAYER, N). */
    void addSpot(Side player, int n) {
        markUndo();
        if (isLegal(player)) {
            if (get(n).getSide().equals(WHITE)) {
                _whitesquares -= 1;
                if (player == BLUE) {
                    _bluesquares += 1;
                } else {
                    _redsquares += 1;
                }
                _board[row(n) - 1][col(n) - 1]
                        = Square.square(player, get(n).getSpots() + 1);
            } else if (get(n).getSide().equals(player)) {
                _board[row(n) - 1][col(n) - 1]
                        = Square.square(player, get(n).getSpots() + 1);
                if (get(n).getSpots() > neighbors(n)) {
                    jump(n);
                }
            }
            _nummoves += 1;
            _readonlyBoard = new ConstantBoard(this);

        }
    }

    /** Set the square at row R, column C to NUM spots (0 <= NUM), and give
     *  it color PLAYER if NUM > 0 (otherwise, white). */
    void set(int r, int c, int num, Side player) {
        internalSet(r, c, num, player);
        announce();
    }

    /** Set the square at row R, column C to NUM spots (0 <= NUM), and give
     *  it color PLAYER if NUM > 0 (otherwise, white).  Does not announce
     *  changes. */
    private void internalSet(int r, int c, int num, Side player) {
        internalSet(sqNum(r, c), num, player);

    }

    /** Set the square #N to NUM spots (0 <= NUM), and give it color PLAYER
     *  if NUM > 0 (otherwise, white). Does not announce changes. */
    private void internalSet(int n, int num, Side player) {
        if (num <= 0) {
            _board[row(n) - 1][col(n) - 1] = Square.square(WHITE, num);
        } else {
            if (!get(n).getSide().equals(player)) {
                numsquares(player, n);
            }
            _board[row(n) - 1][col(n) - 1] = Square.square(player, num);
            if (num > neighbors(n)) {
                jump(n);
            }
        }
    }

    /** Undo the effects of one move (that is, one addSpot command).  One
     *  can only undo back to the last point at which the undo history
     *  was cleared, or the construction of this Board. */
    void undo() {
        if (_history >= 0) {
            copy(_boardhistory.get(_history));
            _boardhistory.remove(_history);
            _history -= 1;
        }

    }

    /** Record the beginning of a move in the undo history. */
    private void markUndo() {
        _history += 1;
        _boardhistory.add(new Board(size()));
        _boardhistory.get(_history).copy(this);
    }

    /** Add DELTASPOTS spots of side PLAYER to row R, column C,
     *  updating counts of numbers of squares of each color. */
    private void simpleAdd(Side player, int r, int c, int deltaSpots) {
        internalSet(r, c, deltaSpots + get(r, c).getSpots(), player);
    }

    /** Add DELTASPOTS spots of color PLAYER to square #N,
     *  updating counts of numbers of squares of each color. */
    private void simpleAdd(Side player, int n, int deltaSpots) {
        internalSet(n, deltaSpots + get(n).getSpots(), player);
    }

    /** Used in jump to keep track of squares needing processing.  Allocated
     *  here to cut down on allocations. */
    private final ArrayDeque<Integer> _workQueue = new ArrayDeque<>();

    /** Do all jumping on this board, assuming that initially, S is the only
     *  square that might be over-full. */
    private void jump(int S) {
        if (!_gameover && exists(S)) {
            Side turn = get(S).getSide();
            int row = row(S);
            int col = col(S);
            if (get(S).getSpots() > neighbors(S)) {
                internalSet(S, get(S).getSpots()
                        - neighbors(S), get(S).getSide());
                if (exists(row, col + 1)) {
                    _workQueue.add(S + 1);
                }
                if (exists(row, col - 1)) {
                    _workQueue.add(S - 1);
                }
                if (exists(row + 1, col)) {
                    _workQueue.add(S + size());
                }
                if (exists(row - 1, col)) {
                    _workQueue.add(S - size());
                }
                process(_workQueue, turn);
                if (!_gameover) {
                    jump(S + 1);
                    jump(S - 1);
                    jump(S + size());
                    jump(S - size());
                }
            }
        }
    }

    /** Processes all of the squares in the ArrayDeque and internally sets
     * all of them to the appropriate values.
     * @param deque contains all of the squares needed to be added to
     * @param turn whose turn it is
     */
    private void process(ArrayDeque<Integer> deque, Side turn) {
        while (!_gameover & !deque.isEmpty()) {
            int square = deque.poll();
            internalSet(square, get(square).getSpots() + 1, turn);
            if (_redsquares == size() * size()) {
                _gameover = true;
                _workQueue.clear();
            } else if (_bluesquares == size() * size()) {
                _gameover = true;
                _workQueue.clear();
            }

        }

    }


    /** Apropriately changes the ownership of a square when jumping.
     *
     * @param side the side who is gaining a a square
     * @param S the square
     */
    private void numsquares(Side side, int S) {
        if (side == BLUE && get(S).getSide() != WHITE) {
            _redsquares -= 1;
            _bluesquares += 1;
        } else if (side == RED && get(S).getSide() != WHITE) {
            _redsquares += 1;
            _bluesquares -= 1;
        } else {
            _whitesquares -= 1;
            if (side.equals(RED)) {
                _redsquares += 1;
            } else {
                _bluesquares += 1;
            }
        }
    }

    /** Returns my dumped representation. */
    @Override
    public String toString() {
        String holder = "===";
        String answer = "";
        for (int i = 0; i < size(); i++) {
            answer += holder + "\n";
            holder = "    ";
            for (int j = 0; j < size(); j++) {
                Side color = get(i + 1, j + 1).getSide();
                int spots = get(i + 1, j + 1).getSpots();
                if (color == RED) {
                    holder += spots + "r";
                } else if (color == BLUE) {
                    holder += spots + "b";
                } else {
                    holder += "1-";
                }
                if (j != size() - 1) {
                    holder += " ";
                }
            }
        }
        answer += holder + "\n";
        answer += "===" + "\n";
        return answer;
    }

    /** Returns an external rendition of me, suitable for human-readable
     *  textual display, with row and column numbers.  This is distinct
     *  from the dumped representation (returned by toString). */
    public String toDisplayString() {
        String[] lines = toString().trim().split("\\R");
        Formatter out = new Formatter();
        for (int i = 1; i + 1 < lines.length; i += 1) {
            out.format("%2d %s%n", i, lines[i].trim());
        }
        out.format("  ");
        for (int i = 1; i <= size(); i += 1) {
            out.format("%3d", i);
        }
        return out.toString();
    }

    /** Returns the number of neighbors of the square at row R, column C. */
    int neighbors(int r, int c) {
        int size = size();
        int n;
        n = 0;
        if (r > 1) {
            n += 1;
        }
        if (c > 1) {
            n += 1;
        }
        if (r < size) {
            n += 1;
        }
        if (c < size) {
            n += 1;
        }
        return n;
    }

    /** Returns the number of neighbors of square #N. */
    int neighbors(int n) {
        return neighbors(row(n), col(n));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Board)) {
            return false;
        } else {
            Board B = (Board) obj;
            boolean answer = true;
            answer = answer && _size == ((Board) obj)._size;
            for (int i = 0; i < size(); i++) {
                for (int j = 0; j < size(); j++) {
                    Side myside = get(i + 1, j + 1).getSide();
                    int myspots = get(i + 1, j + 1).getSpots();
                    Side otherside = ((Board) obj).get(i + 1, j + 1).getSide();
                    int otherspots = ((Board) obj).get(i + 1, j + 1).getSpots();
                    answer = answer && myside.equals(otherside);
                    answer = answer && myspots == otherspots;
                }
            }
            return answer;
        }
    }

    @Override
    public int hashCode() {
        return numPieces();
    }

    /** Set my notifier to NOTIFY. */
    public void setNotifier(Consumer<Board> notify) {
        _notifier = notify;
        announce();
    }

    /** Take any action that has been set for a change in my state. */
    private void announce() {
        _notifier.accept(this);
    }

    /** A notifier that does nothing. */
    private static final Consumer<Board> NOP = (s) -> { };

    /** A read-only version of this Board. */
    private ConstantBoard _readonlyBoard;

    /** Use _notifier.accept(B) to announce changes to this board. */
    private Consumer<Board> _notifier;

    /** 2-dimensional array of squares that represents a board. */
    private Square[][] _board;

    /** Arraylist that stores all the possible undoes for a certain turn. */
    private ArrayList<Board> _boardhistory = new ArrayList<>();

    /** Number respresentation of how many undoes are in boarhistory. If
     * _hitory = -1, there are no available moves to undo */
    private int _history = -1;

    /** Number respresentation of how many rows/cols the board has. */
    private int _size;

    /** Tells the number of moves in a given game. If nummoves = 0 or even
     * it is RED's turn, else it is BLUE's turn */
    private int _nummoves = 0;

    /** Tells whether the game is over or not. */
    private boolean _gameover = false;

    /** Tells how many squares are red. */
    protected int _redsquares = 0;

    /** Tells how many squares are blue. */
    protected int _bluesquares = 0;

    /** Tells how many squares are white. */
    protected int _whitesquares;


}
