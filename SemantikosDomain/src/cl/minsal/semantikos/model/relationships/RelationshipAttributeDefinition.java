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

    //private static final String RELATIONSHIP_TYPE_ATTRIBUTE = "tipo de relación";

    private static final String RELATIONSHIP_TYPE_ATTRIBUTE = "tipo de relación";

    private static final String UNIDAD_POTENCIA_ATTRIBUE = "unidad potencia";

    private static final String CANTIDAD_PP_ATTRIBUE = "cantidad pp";

    private static final String UNIDAD_PP_ATTRIBUE = "unidad pp";

    private static final String UNIDAD_ATTRIBUE = "unidad";

    private static final String UNIDAD_PACK_MULTI_ATTRIBUE = "unidad pack multi";

    private static final String UNIDAD_VOLUMEN_TOTAL_ATTRIBUE = "unidad volumen total";

    private static final String UNIDAD_VOLUMEN_ATTRIBUE = "unidad de volumen";

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

    public boolean isUnidadPotenciaAttribute(){
        return this.getName().equalsIgnoreCase(UNIDAD_POTENCIA_ATTRIBUE);
    }

    public boolean isCantidadPPAttribute(){
        return this.getName().equalsIgnoreCase(CANTIDAD_PP_ATTRIBUE);
    }

    public boolean isUnidadPPAttribute(){
        return this.getName().equalsIgnoreCase(UNIDAD_PP_ATTRIBUE);
    }

    public boolean isUnidadAttribute() {
        return this.getName().equalsIgnoreCase(UNIDAD_ATTRIBUE);
    }

    public boolean isUnidadLike() {
        return this.getName().toLowerCase().contains("unidad") && this.getName().toLowerCase().contains("medida");
    }

    public boolean isUnidadPackMultiAttribute(){
        return this.getName().equalsIgnoreCase(UNIDAD_PACK_MULTI_ATTRIBUE);
    }

    public boolean isUnidadVolumenTotalAttribute(){
        return this.getName().equalsIgnoreCase(UNIDAD_VOLUMEN_TOTAL_ATTRIBUE);
    }

    public boolean isUnidadVolumenAttribute(){
        return this.getName().equalsIgnoreCase(UNIDAD_VOLUMEN_ATTRIBUE);
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

    @Override
    public String toString() {
        //return "id: " + id + ". [" + super.toString() + "]";
        return "id: " + id + ". [" + name + "]";
    }
}
