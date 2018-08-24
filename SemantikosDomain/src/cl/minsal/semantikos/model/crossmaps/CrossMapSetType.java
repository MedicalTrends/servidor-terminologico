package cl.minsal.semantikos.model.crossmaps;

import java.io.Serializable;

/**
 * @author Andrés Farías on 11/18/16.
 */
public enum CrossMapSetType implements Serializable {

    CIE_9(1,"CIE-9"),
    CIE_10(2,"CIE-10")
    ;
    /** Identificador único de la base de datos */
    private long id;

    /** Nombre o descripción del cambio */
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    CrossMapSetType(long id, String name) {

        this.id = id;
        this.name = name;
    }
}
