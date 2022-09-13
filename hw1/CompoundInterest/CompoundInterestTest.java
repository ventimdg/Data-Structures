import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.

        assertEquals(0, 0); */
        assertEquals(1, CompoundInterest.numYears(2022));
        assertEquals(0, CompoundInterest.numYears(CompoundInterest.THIS_YEAR));
        assertEquals(100, CompoundInterest.numYears(2121));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(105, CompoundInterest.futureValue(100, 5, 2022), tolerance);
        assertEquals(100, CompoundInterest.futureValue(100, 5, CompoundInterest.THIS_YEAR), tolerance);
        assertEquals(157.35, CompoundInterest.futureValue(100, 12, 2025), tolerance);
        assertEquals(33930.21, CompoundInterest.futureValue(100, 6, 2121), tolerance);
        assertEquals(12.544, CompoundInterest.futureValue(10, 12, 2023), tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(103.95, CompoundInterest.futureValueReal(100, 5, 2022, 1), tolerance);
        assertEquals(100, CompoundInterest.futureValueReal(100, 5, CompoundInterest.THIS_YEAR, 2), tolerance);
        assertEquals(139.30, CompoundInterest.futureValueReal(100, 12, 2025, 3), tolerance);
        assertEquals(572.41, CompoundInterest.futureValueReal(100, 6, 2121,  4), tolerance);
        assertEquals(11.80, CompoundInterest.futureValueReal(10, 12, 2023, 3), tolerance);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(5000, CompoundInterest.totalSavings(5000, 2021, 13), tolerance);
        assertEquals(12900, CompoundInterest.totalSavings(6000, 2022, 15), tolerance);
        assertEquals(16550, CompoundInterest.totalSavings(5000, 2023, 10), tolerance);
        assertEquals(33455.296, CompoundInterest.totalSavings(7000, 2024, 12), tolerance);
        assertEquals(72711.14576, CompoundInterest.totalSavings(11000, 2025, 14), tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
         double tolerance = 0.01;
        assertEquals(5000, CompoundInterest.totalSavingsReal(5000, 2021, 13, 1), tolerance);
        assertEquals(12642, CompoundInterest.totalSavingsReal(6000, 2022, 15, 2), tolerance);
        assertEquals(15571.895, CompoundInterest.totalSavingsReal(5000, 2023, 10, 3), tolerance);
        assertEquals(29599.10476, CompoundInterest.totalSavingsReal(7000, 2024, 12, 4), tolerance);
        assertEquals(59223.68267, CompoundInterest.totalSavingsReal(11000, 2025, 14, 5), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
