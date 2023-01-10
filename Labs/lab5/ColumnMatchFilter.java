import javax.swing.text.TableView;

/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {

    public int _column1;
    public int _column2;

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        _column1 = input.colNameToIndex(colName1);
        _column2 = input.colNameToIndex(colName2);
    }

    @Override
    protected boolean keep() {
        return _next.getValue(_column2).equals(_next.getValue(_column1));
    }

}
