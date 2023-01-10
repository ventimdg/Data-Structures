import java.util.ArrayList;
import java.util.HashMap;

/** Functions to increment and sum the elements of a WeirdList. */
class WeirdListClient {

    /** Return the result of adding N to each element of L. */
    static WeirdList add(WeirdList L, int n) {
        return L.map( (int x) -> x + n);
    }

    /** Return the sum of all the elements in L. */
    static int sum(WeirdList L) {
        sumHelper func = new sumHelper(0);
        L.map(func);
        return func.returnanswer();
    }

    private static class sumHelper implements IntUnaryFunction {

        private int answer;

        public sumHelper(int x) {
            answer = x;
        }

        @Override
        public int apply(int x) {
            answer += x;
            return x;
        }

        public int returnanswer() {
            return answer;
        }
    }

    /* IMPORTANT: YOU ARE NOT ALLOWED TO USE RECURSION IN ADD AND SUM
     *
     * As with WeirdList, you'll need to add an additional class or
     * perhaps more for WeirdListClient to work. Again, you may put
     * those classes either inside WeirdListClient as private static
     * classes, or in their own separate files.

     * You are still forbidden to use any of the following:
     *       if, switch, while, for, do, try, or the ?: operator.
     *
     * HINT: Try checking out the IntUnaryFunction interface.
     *       Can we use it somehow?
     */
    public static boolean prereq(int[][] input) {
        HashMap<Integer, ArrayList<Integer>> helper = new HashMap<>();
        for (int[] pair : input) {
            if (helper.containsKey(pair[1]) && helper.get(pair[1]).contains(pair[0])){
                return false ;
            }
            else if (helper.containsKey(pair[0])) {
                helper.get(pair[0]).add(pair[1]);
            } else {
                helper.put(pair[0], new ArrayList<>());
                helper.get(pair[0]).add(pair[1]);
            }
        }
        return true;
    }




























}
