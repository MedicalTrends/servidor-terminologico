package cl.minsal.semantikos.model.helpertables;

/**
 * @author Andres Farías on 7/27/16.
 */
public class HelperTableColumn {

    private String columnName;
    private final boolean isPK;
    private final boolean isSearchable;
    private final boolean isShowable;

    /**
     * This is the default and only constructor for HelperTableColumn.
     *
     * @param columnName The column physic name.
     * @param isPK Is it a PK?
     * @param isSearchable is it searchable?
     * @param isShowable is to be shown or retrieved?
     */
    public HelperTableColumn(String columnName, boolean isPK, boolean isSearchable, boolean isShowable) {
        this.columnName = columnName;
        this.isPK = isPK;
        this.isSearchable = isSearchable;
        this.isShowable = isShowable;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public boolean isShowable() {
        return isShowable;
    }

    public boolean isPK() {
        return isPK;
    }

    public boolean isSearchable() {
        return isSearchable;
    }
}
