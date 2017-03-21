package cl.minsal.semantikos.model.browser;

import cl.minsal.semantikos.model.relationships.Target;

/**
 * Created by root on 21-03-17.
 */
public class TargetDTO {

    /**
     * Filtros estáticos
     */
    private long id;

    private long type;

    private String value;

    public TargetDTO() {
    }

    public TargetDTO(Target target) {
        this.id = target.getId();
        this.type = target.getTargetType().getIdTargetType();
        this.value = target.getRepresentation();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
