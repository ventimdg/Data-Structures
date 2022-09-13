/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {

    public int _column;
    public String _reference;

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        _column = input.colNameToIndex(colName);
        _reference = ref;
    }

    @Override
    protected boolean keep() {
        return _next.getValue(_column).compareTo(_reference) > 0;
    }

    // FIXME: Add instance variables?
}
