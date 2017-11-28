package cl.minsal.semantikos.model.helpertables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Andrés Farías
 */
public class HelperTableFactory implements Serializable {

    private static final HelperTableFactory instance = new HelperTableFactory();

    /** La lista de columnas */
    private List<HelperTable> helperTables;

    /** Mapa de columnas por su nombre. */
    private ConcurrentHashMap<String, HelperTable> tableByName;

    /** Mapa de columnas por su id. */
    private ConcurrentHashMap<Long, HelperTable> tableById;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private HelperTableFactory() {
        this.helperTables = new ArrayList<>();
        this.tableByName = new ConcurrentHashMap<>();
        this.tableById = new ConcurrentHashMap<>();
    }

    public static HelperTableFactory getInstance() {
        return instance;
    }

    /**
     * Este método es responsable de retornar la columna por su nombre.
     *
     * @return Retorna una HelperTableColumn
     */
    public HelperTable findColumnByName(String name) {

        if (tableByName.containsKey(name.toUpperCase())) {
            return this.tableByName.get(name.toUpperCase());
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
    public HelperTable findTableById(long id) {

        if (tableById.containsKey(id)) {
            return this.tableById.get(id);
        }
        else {
            return null;
        }
    }

    /**
     * Este método es responsable de asignar un nuevo conjunto de columnas. Al hacerlo, es necesario actualizar
     * los mapas.
     */
    public void setHelperTables(List<HelperTable> helperTables) {

        /* Se actualiza la lista */
        this.helperTables = helperTables;

        /* Se actualiza el mapa por nombres */
        this.tableByName.clear();

        for (HelperTable helperTable : helperTables) {
            this.tableByName.put(helperTable.getName(), helperTable);
            this.tableById.put(helperTable.getId(), helperTable);
        }
    }


}
