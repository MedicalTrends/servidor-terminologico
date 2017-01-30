package cl.minsal.semantikos.model.helpertables;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.relationships.TargetDefinition;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Andrés Farías
 */
public class HelperTable extends PersistentEntity implements TargetDefinition {

    /** Columna de sistema con la llave primaria para cada registro de una tabla auxiliar */
    public static final HelperTableColumn SYSTEM_COLUMN_ID = new HelperTableColumn("id", true, true, true);

    /** Columna de sistema con la descripción para cada registro de una tabla auxiliar */
    public static final HelperTableColumn SYSTEM_COLUMN_DESCRIPTION = new HelperTableColumn("description", false, true, true);

    /** Columna de sistema con la fecha de creación para cada registro de una tabla auxiliar */
    public static final HelperTableColumn SYSTEM_COLUMN_CREATION_DATE = new HelperTableColumn("creation_date", false, true, true);

    /** Columna de sistema con la fecha hasta la cual cada registro se encuentra vigente de una tabla auxiliar */
    public static final HelperTableColumn SYSTEM_COLUMN_VALIDITY_UNTIL = new HelperTableColumn("validity_until", false, true, true);

    /** Columna de sistema del usuario que modificó por última vez un registro de una tabla auxiliar */
    public static final HelperTableColumn SYSTEM_COLUMN_USER = new HelperTableColumn("user", false, true, true);

    /** Un nombre legible por humanos para la Tabla Auxiliar */
    private String name;

    /* Una breve descripción de la tabla auxiliar */
    private String description;

    /** El nombre de la tabla física */
    private String tablaName;

    /** El nombre de las columnas que posee la tabla física */
    private Collection<HelperTableColumn> columns;

    /**
     * Este constructor permite crear un objeto <code>HelperTable</code> que no ha sido persistido en la base de datos
     * (sin ID).
     */
    public HelperTable(@NotNull String name, String description, String tablaName, @NotNull Collection<HelperTableColumn> columns) {
        this(-1, name, description, tablaName, columns);
    }

    /**
     * Constructor completo. Tiene los principales valores por defecto.
     *
     * @param id          Identificador único de la tabla.
     * @param name        Nombre de la tabla
     * @param description Descripción de la tabla.
     * @param tablaName   Nombre de la Tabla en la Base de Datos.
     * @param columns     Columnas de la tabla auxiliar.
     */
    public HelperTable(long id, @NotNull String name, String description, String tablaName, @NotNull Collection<HelperTableColumn> columns) {
        super(id);
        this.name = name;
        this.description = description;
        this.tablaName = tablaName;
        this.columns = new ArrayList<>(columns);
    }

    public Collection<HelperTableColumn> getColumns() {
        return columns;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTablaName() {
        return tablaName;
    }

    /**
     * Este método es responsable de retornar aquellas columnas que han sido definidas como <i>mostrables</i>.
     *
     * @return Un objeto <code>java.util.List</code> con las columnas parametrizadas como mostrables para esa tabla.
     */
    public List<HelperTableColumn> getShowableColumns() {
        List<HelperTableColumn> showableColumns = new ArrayList<>();
        for (HelperTableColumn column : columns) {
            if (column.isShowable()) {
                showableColumns.add(column);
            }
        }

        return showableColumns;
    }

    /**
     * Este método es responsable de retornar una lista con los nombres de todas las columnas.
     *
     * @return Un <code>java.util.List</code> con los nombres de las columnas.
     */
    public List<String> getAllColumnsNames() {
        return extractColumnsName(columns);
    }

    /**
     * Este método es responsable de retornar la columna con el nombre dado como parámetro.
     *
     * @param columnName El nombre de la columna que se desea recuperar.
     *
     * @return La columna cuyo nombre coincide con <code>columnName</code>.
     *
     * @throws java.lang.IllegalArgumentException Cuando la tabla no tiene una columna de nombre <code>columnName</code>.
     */
    public HelperTableColumn getColumnByName(String columnName) {

        for (HelperTableColumn column : columns) {
            if (column.getColumnName().equalsIgnoreCase(columnName)) {
                return column;
            }
        }

        throw new IllegalArgumentException("La tabla auxiliar " + this + " no posee una columna de nombre " + columnName);
    }

    /**
     * Este método es responsable de retornar una lista con los nombres las columnas dadas como parámetro.
     *
     * @param columns Las columnas cuyos nombres se desea obtener
     *
     * @return Un <code>java.util.List</code> con los nombres de las columnas.
     */
    public List<String> extractColumnsName(Collection<HelperTableColumn> columns) {
        List<String> columnNames = new ArrayList<>();
        for (HelperTableColumn column : columns) {
            columnNames.add(column.getColumnName());
        }

        return columnNames;
    }

    @Override
    public boolean isBasicType() {
        return false;
    }

    @Override
    public boolean isSMTKType() {
        return false;
    }

    @Override
    public boolean isHelperTable() {
        return true;
    }

    @Override
    public boolean isSnomedCTType() {
        return false;
    }

    @Override
    public boolean isCrossMapType() {
        return false;
    }

    /**
     * Este método es responsable de retornar el nombre de las columnas mostrables.
     *
     * @return Un arreglo de <code>String</code> con los nombres de las columnas mostrables.
     */
    public List<String> getShowableColumnsNames() {

        List<String> showableColumnsNames = new ArrayList<>();
        for (HelperTableColumn showableColumn : getShowableColumns()) {
            showableColumnsNames.add(showableColumn.getColumnName());
        }

        return showableColumnsNames;
    }

    /**
     * Este método es responsable de retornar las columnas de sistema de toda tabla auxiliar.
     *
     * @return Una colección con las columnas de sistema.
     */
    public static Collection<HelperTableColumn> getSystemColumns() {

        Collection<HelperTableColumn> systemColumns = new ArrayList<>();
        systemColumns.add(SYSTEM_COLUMN_ID);
        systemColumns.add(SYSTEM_COLUMN_DESCRIPTION);
        systemColumns.add(SYSTEM_COLUMN_CREATION_DATE);
        systemColumns.add(SYSTEM_COLUMN_VALIDITY_UNTIL);
        systemColumns.add(SYSTEM_COLUMN_USER);

        return systemColumns;
    }

    public boolean isISP() {
        return this.getName().equals(HelperTableFactory.HT_ISP_NAME);
    }

    public boolean isATC(){
        return this.getName().equals(HelperTableFactory.HT_ATC_NAME);
    }


    @Override
    public String toString() {
        return "HelperTable{" +
                "name='" + name + '\'' +
                '}';
    }
}
