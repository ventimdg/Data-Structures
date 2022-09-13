/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    public int _column;
    public String _word;

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        _column = input.colNameToIndex(colName);
        _word = match;

    }

    @Override
    protected boolean keep() {
        return _next.getValue(_column).equals(_word);
    }
}
