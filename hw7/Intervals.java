import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/** HW #7, Sorting ranges.
 *  @author Dominic Ventimiglia
  */
public class Intervals {
    /** Assuming that INTERVALS contains two-element arrays of integers,
     *  <x,y> with x <= y, representing intervals of ints, this returns the
     *  total length covered by the union of the intervals. */
    public static int coveredLength(List<int[]> intervals) {
       int answer = 0;
       int min = Integer.MIN_VALUE;
       int max = Integer.MIN_VALUE;
        for (int[] interval : intervals) {
            if (interval[0] > max || interval[1] < min) {
                answer += max - min;
                min = interval[0];
                max = interval[1];
            } else if (interval[0] < min && interval[1] > max) {
                min = interval[0];
                max = interval[1];
            } else if (interval[0] < min && interval[1] >= min && interval[1] <= max) {
                min = interval[0];
            } else if (interval[1] > max && interval[0] >= min && interval[0] <= max) {
                max = interval[1];
            }
        }
        answer += (max - min);
        return answer;
    }

    /** Test intervals. */
    static final int[][] INTERVALS = {
        {19, 30},  {8, 15}, {3, 10}, {6, 12}, {4, 5},
    };
    /** Covered length of INTERVALS. */
    static final int CORRECT = 23;

    /** Performs a basic functionality test on the coveredLength method. */
    @Test
    public void basicTest() {
        assertEquals(CORRECT, coveredLength(Arrays.asList(INTERVALS)));
    }

    /** Runs provided JUnit test. ARGS is ignored. */
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Intervals.class));
    }

}
