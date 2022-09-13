/** Multidimensional array 
 *  @author Zoe Plaxco
 */

public class MultiArr {

    /**
    {{“hello”,"you",”world”} ,{“how”,”are”,”you”}} prints:
    Rows: 2
    Columns: 3
    
    {{1,3,4},{1},{5,6,7,8},{7,9}} prints:
    Rows: 4
    Columns: 4
    */
    public static void printRowAndCol(int[][] arr) {
        int rows = arr.length;
        int columns = 0;
        for (int i = 0; i < arr.length; i += 1 ) {
            if (arr[i].length > columns) {
                columns = arr[i].length;
            }
        }
        System.out.println("Rows:" + " " + rows);
        System.out.println("Columns:" + " " + columns);

    }

    /**
    @param arr: 2d array
    @return maximal value present anywhere in the 2d array
    */
    public static int maxValue(int[][] arr) {
        int answer = arr[0][0];
        for (int i = 0; i < arr.length; i += 1 ) {
            for (int j = 0; j < arr[i].length; j += 1) {
                if (arr[i][j] > answer) {
                    answer = arr[i][j];
                }
            }
        }
        return answer;
    }

    /**Return an array where each element is the sum of the 
    corresponding row of the 2d array*/
    public static int[] allRowSums(int[][] arr) {
        int[] answer = new int[arr.length];
        for (int i = 0; i < arr.length; i += 1 ) {
            for (int j = 0; j < arr[i].length; j += 1) {
                answer[i] += arr[i][j];
            }
        }
        return answer;
    }

    static final int[][] TESTER = {{1,3,4},{1},{5,6,7,8},{7,9}};

    public static void main(String[] ignored) {
        printRowAndCol(TESTER);
    }
}