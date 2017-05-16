package cl.minsal.semantikos.model.descriptions;

import java.io.Serializable;

/**
 * Created by des01c7 on 24-10-16.
 */
public class ObservationNoValid implements Serializable {

    private Long id;
    private String description;

    /**
     * Constructor vacío, utilizado como fix para el comportamiento de jsf-primefaces al actualizar el form en ui-state-error
     **/
    @Deprecated
    public ObservationNoValid() {
    }

    public ObservationNoValid(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
