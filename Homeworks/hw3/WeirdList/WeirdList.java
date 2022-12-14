import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** A WeirdList holds a sequence of integers.
 * @author Dominic Ventimiglia
 */
public class WeirdList {

    private int _head;
    private WeirdList _tail;

    /** The empty sequence of integers. */
    public static final WeirdList EMPTY = new WeirdListHelper(0, null);

    /** A new WeirdList whose head is HEAD and tail is TAIL. */
    public WeirdList(int head, WeirdList tail) { 
        _head = head;
        _tail = tail;
    }

    /** Returns the number of elements in the sequence that
     *  starts with THIS. */
    public int length() {
        return 1 + _tail.length();
    }

    /** Return a string containing my contents as a sequence of numerals
     *  each preceded by a blank.  Thus, if my list contains
     *  5, 4, and 2, this returns " 5 4 2". */
    @Override
    public String toString() {
        return " " + _head + _tail;
    }

    /** Part 3b: Apply FUNC.apply to every element of THIS WeirdList in
     *  sequence, and return a WeirdList of the resulting values. */
    public WeirdList map(IntUnaryFunction func) {
        return new WeirdList(func.apply(_head), _tail.map(func));
    }

    private static class WeirdListHelper extends WeirdList {
        public WeirdListHelper(int head, WeirdList tail) {
            super(head, tail);
        }
        @Override
        public int length() {
            return 0;
        }

        @Override
        public String toString() {
            return "";
        }

        @Override
        public WeirdList map(IntUnaryFunction func) {
            return this;
        }
    }

    public static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> answer = new ArrayList<>();
        List<List<Integer>> arrMat = new ArrayList<>();
        for (int[] arr : matrix) {
            ArrayList<Integer> temp = new ArrayList<>();
            for (int num : arr) {
                temp.add(num);
            }
            arrMat.add(new ArrayList<Integer>(temp));
        }
        boolean done = arrMat.size() == 0;
        spiralhelper(answer, arrMat, 0, done);
        return answer;
    }
    public static void spiralhelper(List<Integer> answer, List<List<Integer>> arrMat, int side, boolean done) {
        int len = arrMat.get(0).size();
        int height = arrMat.size();
        if (side == 0 && !done){
            for (int num : arrMat.get(0)) {
                answer.add(num);
            }
            arrMat.remove(0);
            done = arrMat.size() == 0;
            if (!done) {
                spiralhelper(answer, arrMat, 1, done);
            }
        }
        else if (side == 1 && !done){
            for (List<Integer> row : arrMat) {
                answer.add(row.get(len-1));
                row.remove(len-1);
            }
            for (List<Integer> row : arrMat) {
                if (row.size() == 0){
                    arrMat.remove(row);
                }
            }
            done = arrMat.size() == 0;
            if (!done) {
                spiralhelper(answer, arrMat, 2, done);
            }

        }
        else if (side == 2 && !done){
            List<Integer> row = arrMat.get(height-1);
            for (int i = row.size() - 1; i >= 0; i--) {
                answer.add(row.get(i));
            }
            arrMat.remove(height-1);
            done = arrMat.size() == 0;
            if (!done) {
                spiralhelper(answer, arrMat, 3, done);
            }
        }
        else if (side == 3 && !done){
            for (int i = height - 1; i >=0; i--) {
                answer.add(arrMat.get(i).get(0));
                arrMat.get(i).remove(0);
            }
            for (int i = height - 1; i >=0; i--) {
                if (arrMat.get(i).size() == 0){
                    arrMat.remove(arrMat.get(i));
                }
            }
            done = arrMat.size() == 0;
            if (!done) {
                spiralhelper(answer, arrMat, 0, done);
            }
        }
    }
}



    /*
     * You should not add any methods to WeirdList, but you will need
     * to add private fields (e.g. head).

     * But that's not all!

     * You will need to create at least one additional class for WeirdList
     * to work. This is because you are forbidden to use any of the
     * following in ANY of the code for HW3:
     *       if, switch, while, for, do, try, or the ?: operator.

     * If you'd like an obtuse hint, scroll to the very bottom of this
     * file.

     * You can create this hypothetical class (or classes) in separate
     * files like you usually do, or if you're feeling bold you can
     * actually stick them INSIDE of this class. Yes, nested classes
     * are a thing in Java.

     * As an example:
     * class Garden {
     *     private static class Potato {
     *        int n;
     *        public Potato(int nval) {
     *           n = nval;
     *        }
     *     }
     * }
     * You are NOT required to do this, just an extra thing you can
     * do if you want to avoid making a separate .java file. */


/*
 * Hint: The first non-trivial thing you'll probably do to WeirdList
 * is to fix the EMPTY static variable so that it points at something
 * useful. */







