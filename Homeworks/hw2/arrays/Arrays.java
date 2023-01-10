package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author Dominic Ventimiglia
 */
class Arrays {

    /* C1. */

    /**
     * Returns a new array consisting of the elements of A followed by the
     * the elements of B.
     */
    static int[] catenate(int[] A, int[] B) {
        int[] answer;
        if (A == null) {
            return B;
        } else if (B == null) {
            return A;
        } else {
            answer = new int[A.length + B.length];
            for (int i = 0; i < A.length; i += 1) {
                answer[i] = A[i];
            }
            for (int i = 0; i < B.length; i += 1) {
                answer[A.length + i] = B[i];
            }
        }
        return answer;
    }

    /* C2. */

    /**
     * Returns the array formed by removing LEN items from A,
     * beginning with item #START. If the start + len
     * is out of bounds for our array, you
     * can return null.
     * Example: if A is [0, 1, 2, 3] and start is 1 and len is 2, the
     * result should be [0, 3].
     */
    static int[] remove(int[] A, int start, int len) {
        int[] answer = new int[A.length - len];
        if (start + len > A.length || A == null) {
            return null;
        } else {
            for (int i = 0; i < start; i += 1) {
                answer[i] = A[i];
            }
            for (int i = start + len; i < A.length; i += 1) {
                answer[i - len] = A[i];
            }
        }
        return answer;
    }

    /* C3. */

    /**
     * Returns the array of arrays formed by breaking up A into
     * maximal ascending lists, without reordering.
     * For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     * returns the three-element array
     * {{1, 3, 7}, {5}, {4, 6, 9, 10}}.
     */
    static int[][] naturalRuns(int[] A) {
        int[][] answer = new int[0][];
        return  naturalRunsHelper(A, answer);
    }

    /** Natural runs helper function that
     * keeps track of the answer list
     * while allowing the rest of the list
     * that has not been operated
     * on to be stored.
     */
    static int[][] naturalRunsHelper(int[] A, int[][] answer) {
        int[] cut;
        int[] rest;
        if (A == null) {
            return answer;
        } else {
            for (int i = 0; i < A.length; i++) {
                if (i + 1 == A.length || A[i + 1] <= A[i]) {
                    int[][] newanswer = new int[answer.length + 1][];
                    cut = new int[i + 1];
                    rest = new int[A.length - i - 1];
                    System.arraycopy(A, 0, cut, 0, i + 1);
                    System.arraycopy(A, i + 1, rest, 0, A.length - i - 1);
                    System.arraycopy(answer, 0, newanswer, 0, answer.length);
                    newanswer[answer.length] = cut;
                    return naturalRunsHelper(rest, newanswer);
                }
            }
        }
        return answer;
    }
}
