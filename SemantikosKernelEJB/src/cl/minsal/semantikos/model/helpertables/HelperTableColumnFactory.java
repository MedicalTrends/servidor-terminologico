package cl.minsal.semantikos.model.helpertables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andrés Farías
 */
public class HelperTableColumnFactory {

    private static final HelperTableColumnFactory instance = new HelperTableColumnFactory();

    /** La lista de columnas */
    private List<HelperTableColumn> helperTableColumns;

    /** Mapa de columnas por su nombre. */
    private Map<String, HelperTableColumn> columnByName;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private HelperTableColumnFactory() {
        this.helperTableColumns = new ArrayList<>();
        this.columnByName = new HashMap<>();
    }

    public static HelperTableColumnFactory getInstance() {
        return instance;
    }

    /**
     * Este método es responsable de retornar la columna por su nombre.
     *
     * @return Retorna una HelperTableColumn
     */
    public HelperTableColumn findColumnByName(String name) {

        if (columnByName.containsKey(name.toUpperCase())) {
            return this.columnByName.get(name.toUpperCase());
        }
        else {
            return null;
        }
    }

    /**
     * Este método es responsable de asignar un nuevo conjunto de columnas. Al hacerlo, es necesario actualizar
     * los mapas.
     */
    public void setHelperTableColumns(List<HelperTableColumn> helperTableColumns) {

        /* Se actualiza la lista */
        this.helperTableColumns = helperTableColumns;

        /* Se actualiza el mapa por nombres */
        this.columnByName.clear();

        for (HelperTableColumn helperTableColumn : helperTableColumns) {
            this.columnByName.put(helperTableColumn.getDescription(), helperTableColumn);
        }
    }

}
