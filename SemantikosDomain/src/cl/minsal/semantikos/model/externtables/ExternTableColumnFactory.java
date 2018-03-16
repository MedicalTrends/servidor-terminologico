package cl.minsal.semantikos.model.externtables;

import cl.minsal.semantikos.model.helpertables.HelperTableColumn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Andrés Farías
 */
public class ExternTableColumnFactory implements Serializable {

    private static final ExternTableColumnFactory instance = new ExternTableColumnFactory();

    /** La lista de columnas */
    private List<ExternTableColumn> externTableColumns;

    /** Mapa de columnas por su nombre. */
    private ConcurrentHashMap<String, ExternTableColumn> columnByName;

    /** Mapa de columnas por su id. */
    private ConcurrentHashMap<Long, ExternTableColumn> columnById;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private ExternTableColumnFactory() {
        this.externTableColumns = new ArrayList<>();
        this.columnByName = new ConcurrentHashMap<>();
        this.columnById = new ConcurrentHashMap<>();
    }

    public static ExternTableColumnFactory getInstance() {
        return instance;
    }

    /**
     * Este método es responsable de retornar la columna por su nombre.
     *
     * @return Retorna una HelperTableColumn
     */
    public ExternTableColumn findColumnByName(String name) {

        if (columnByName.containsKey(name.toUpperCase())) {
            return this.columnByName.get(name.toUpperCase());
        }
        else {
            return null;
        }
    }

    /**
     * Este método es responsable de retornar la columna por su nombre.
     *
     * @return Retorna una HelperTableColumn
     */
    public ExternTableColumn findColumnById(long id) {

        if (columnById.containsKey(id)) {
            return this.columnById.get(id);
        }
        else {
            return null;
        }
    }

    /**
     * Este método es responsable de asignar un nuevo conjunto de columnas. Al hacerlo, es necesario actualizar
     * los mapas.
     */
    public void setExternTableColumns(List<ExternTableColumn> helperTableColumns) {

        /* Se actualiza la lista */
        this.externTableColumns = helperTableColumns;

        /* Se actualiza el mapa por nombres */
        this.columnByName.clear();

        for (ExternTableColumn helperTableColumn : helperTableColumns) {
            this.columnByName.put(helperTableColumn.getName(), helperTableColumn);
            this.columnById.put(helperTableColumn.getId(), helperTableColumn);
        }
    }

    public List<ExternTableColumn> findColumnsByExternTable(ExternTable externTable) {

        List<ExternTableColumn> columns = new ArrayList<>();

        for (ExternTableColumn helperTableColumn : externTableColumns) {
            if(helperTableColumn.getExternTable().equals(externTable)) {
                columns.add(helperTableColumn);
            }
        }

        return columns;
    }

}
