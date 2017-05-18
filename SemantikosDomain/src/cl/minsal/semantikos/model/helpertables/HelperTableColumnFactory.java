package cl.minsal.semantikos.model.helpertables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andrés Farías
 */
public class HelperTableColumnFactory implements Serializable {

    public static final long U_VOLUMEN_ID = 12L;
    public static final long COLUMN_U_VOL = 12L;

    public static final long U_POTENCIA_ID = 9L;
    public static final long COLUMN_U_POTENCIA = 13L;

    public static final long U_UNIDAD_CANTIDAD_ID = 15L;
    public static final long COLUMN_U_UNIDAD_CANTIDAD = 15L;

    public static final long U_PACK_MULTI_ID = 16L;
    public static final long COLUMN_U_PACK_MULTI = 14L;

    public static final long U_VOLUMEN_TOT_ID = 17L;
    public static final long COLUMN_U_VOLUMEN_TOT = 12L;

    public static final long U_PP_ID = 11L;
    //public static final long COLUMN_U_PP = ?L;

    public static final long U_ASIST_ID = 61L;
    public static final long COLUMN_U_ASIST = 11L;

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
