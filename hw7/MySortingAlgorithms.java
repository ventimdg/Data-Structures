import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int items = Math.min(array.length, k);
            for (int i = 0; i < items; i++) {
                for (int j = i; j > 0; j--) {
                    if (array[j] < array[j-1]) {
                        swap(array, j, j-1);
                    } else {
                        break;
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int items = Math.min(array.length, k);
            for (int i = 0; i < items; i++) {
                int min = i;
                for (int j = i + 1; j < items; j++) {
                    if (array[j] < array[min]) {
                        min = j;
                    }
                }
                swap(array, i, min);
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int items = Math.min(array.length, k);
            sorthelper(array, 0, items);
        }

        public void sorthelper(int[] array, int left, int right) {
            int slice = (left + right) / 2;
            if(right - left > 1){
                sorthelper(array,left,slice);
                sorthelper(array,slice,right);
                for (int i = slice; i < right; i ++) {
                    int temp = array[i];
                    int j;
                    for (j = i-1; j >= left; j--) {
                        if (array[j] <= temp) {
                            break;
                        }
                        array[j+1]=array[j];
                    }
                    array[j+1]=temp;
                }
            }
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            if (k <= 1 || a == null) {
                return;
            }
            int biggest = 0;
            for (int i = 1; i < k; i++) {
                biggest = Math.max(biggest, a[i]);
            }
            int length = 0;
            while(biggest>0) {
                length += 1;
                biggest = biggest >> 2;
            }
            for (int i = 0; i < length; i++) {
                helper(a, k, 2, i);
            }
        }

        private void helper(int[] a, int k, int shifter, int num) {
            int[] counts = new int[(int) Math.pow(2,shifter)];
            int[] answer = new int[k];
            int mask = (1 << (num+1)*shifter) - 1;
            int shiftnum = shifter*num;
            for (int i = 0; i < k; i++) {
                int c = (a[i] & mask) >> (shiftnum);
                counts[c] += 1;
            }
            for (int i = 0; i < counts.length-1; i++) {
                counts[i+1] += counts[i];
            }

            for (int i = k-1; i >= 0; i--) {
                int c = (a[i] & mask) >> (shiftnum);
                answer[counts[c]-- -1] = a[i];
            }
            System.arraycopy(answer, 0, a, 0, k);
        }


        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
