package cl.minsal.semantikos.model.relationships;

import java.io.Serializable;

/**
 * @author Andrés Farías
 */
public class RelationshipAttributeDefinition implements Serializable {

    /** Identificador único de la entidad */
    private long id;

    /** El valor del atributo de la relación */
    private TargetDefinition target;

    /** Nombre del atributo */
    private String name;

    /** Multiplicidad */
    private Multiplicity multiplicity;

    private static final String ORDER_ATTRIBUTE = "orden";

    private static final String RELATIONSHIP_TYPE_ATTRIBUTE = "tipo de relación";

    private static final long GRUOUP_SCT = 29;

    public RelationshipAttributeDefinition(long id, TargetDefinition target, String name, Multiplicity multiplicity) {
        this.id = id;
        this.target = target;
        this.name = name;
        this.multiplicity = multiplicity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TargetDefinition getTargetDefinition() {
        return target;
    }

    public void setTargetDefinition(TargetDefinition target) {
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Multiplicity getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(Multiplicity multiplicity) {
        this.multiplicity = multiplicity;
    }

    public boolean isOrderAttribute(){
        return this.getName().equalsIgnoreCase(ORDER_ATTRIBUTE);
    }

    public boolean isRelationshipTypeAttribute(){
        return this.getName().equalsIgnoreCase(RELATIONSHIP_TYPE_ATTRIBUTE);
    }

    public boolean isGroupSCT(){
        return this.id==GRUOUP_SCT;
    }

    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass() )
            return false;

        RelationshipAttributeDefinition that = (RelationshipAttributeDefinition) o;

        return (id == that.getId());
    }

    @Override
    public int hashCode() {
        return new Long(id).hashCode();
    }
}
