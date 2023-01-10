/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {

    public int _column;
    public String _substring;

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        _column = input.colNameToIndex(colName);
        _substring = subStr;
    }

    @Override
    protected boolean keep() {
        return _next.getValue(_column).contains(_substring);
    }

    // FIXME: Add instance variables?
}
