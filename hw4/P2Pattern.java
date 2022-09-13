/** P2Pattern class
 *  @author Josh Hug & Vivant Sakore
 */

public class P2Pattern {
    /* Pattern to match a valid date of the form MM/DD/YYYY. Eg: 9/22/2019 */
    public static String P1 = "^(0?[1-9]|1[012])/(0?[1-9]|[12]\\d|3[01])/((19|20)\\d{2})";

    /** Pattern to match 61b notation for literal IntLists. */
    public static String P2 = "^\\((\\d+\\,.*)+\\d+\\)";

    /* Pattern to match a valid domain name. Eg: www.support.facebook-login.com */
    public static String P3 = "^[a-z]+\\.(([a-z]*[A-Z]*[0-9]*)+\\-*([a-z]*[A-Z]*[0-9]*)+\\.)*[a-z]{2,6}";

    /* Pattern to match a valid java variable name. Eg: _child13$ */
    public static String P4 = "^([a-z]+|[A-Z]+|\\_+|\\$+)([a-z]+|([A-Z]+)|[0-9]+|\\$|\\_+)*";

    /* Pattern to match a valid IPv4 address. Eg: 127.0.0.1 */
    public static String P5 = "(((0+([1-9]+)?|(1[0-9]?[0-9]?)+|(2[0-5]?[0-5]?))+)\\.){3}((0+([1-9]+)?|(1[0-9]?[0-9]?)+|(2[0-5]?[0-5]?))+)";

}
