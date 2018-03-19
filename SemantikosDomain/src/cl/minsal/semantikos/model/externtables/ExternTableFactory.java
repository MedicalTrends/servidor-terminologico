package cl.minsal.semantikos.model.externtables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Andrés Farías
 */
public class ExternTableFactory implements Serializable {

    private static final ExternTableFactory instance = new ExternTableFactory();

    /** La lista de columnas */
    private List<ExternTable> externTables;

    /** Mapa de columnas por su nombre. */
    private ConcurrentHashMap<String, ExternTable> tableByName;

    /** Mapa de columnas por su id. */
    private ConcurrentHashMap<Long, ExternTable> tableById;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private ExternTableFactory() {
        this.externTables = new ArrayList<>();
        this.tableByName = new ConcurrentHashMap<>();
        this.tableById = new ConcurrentHashMap<>();
    }

    public static ExternTableFactory getInstance() {
        return instance;
    }

    /**
     * Este método es responsable de retornar la columna por su nombre.
     *
     * @return Retorna una HelperTableColumn
     */
    public ExternTable findTableByName(String name) {

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
    public ExternTable findTableById(long id) {

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
    public void setExternTables(List<ExternTable> externTables) {

        /* Se actualiza la lista */
        this.externTables = externTables;

        /* Se actualiza el mapa por nombres */
        this.tableByName.clear();
        this.tableById.clear();

        for (ExternTable externTable : externTables) {
            this.tableByName.put(externTable.getName(), externTable);
            this.tableById.put(externTable.getId(), externTable);
        }
    }

}
