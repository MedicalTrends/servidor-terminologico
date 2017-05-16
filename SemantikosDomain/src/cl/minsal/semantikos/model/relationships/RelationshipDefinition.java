package cl.minsal.semantikos.model.relationships;

import java.io.Serializable;
import java.util.List;

/**
 * @author Diego Soto on 27-05-16.
 */
public class RelationshipDefinition implements Serializable {

    /** ID en la base de datos */
    private long id;

    /** Nombre de la relación */
    private String name;

    /** Descripción */
    private String description;

    /** El tipo del objeto destino de la relación */
    private TargetDefinition targetDefinition;

    /** Multiplicidad de la relación */
    private Multiplicity multiplicity;

    /** Los atributos de esta relación: orden, color, vigencia... */
    private List<RelationshipAttributeDefinition> relationshipAttributeDefinitions;

    /** Relaciones que excluye esta Relación */
    private RelationshipDefinition excludes;

    /**
     * Este es el constructor mínimo con el cual se crean las RelacionesDefinitions.
     *
     * @param name             Nombre de la relación.
     * @param description      Su descripción.
     * @param multiplicity     La multiplicidad.
     * @param targetDefinition El tipo de target.
     */
    public RelationshipDefinition(String name, String description, Multiplicity multiplicity, TargetDefinition targetDefinition) {
        this.name = name;
        this.description = description;
        this.multiplicity = multiplicity;
        this.targetDefinition = targetDefinition;
        this.id = -1;
    }

    /**
     * Igual al constructor mínimo, pero permite inicializar con el ID.
     *
     * @param id               El identificador único.
     * @param name             El nombre de la relación.
     * @param description      Su descripción.
     * @param multiplicity     La multiplicidad.
     * @param targetDefinition El tipo de target.
     */
    public RelationshipDefinition(long id, String name, String description, TargetDefinition targetDefinition, Multiplicity multiplicity) {
        this(name, description, multiplicity, targetDefinition);
        this.id = id;
    }

    public int getIdCategoryDes() {
        return idCategoryDes;
    }

    public void setIdCategoryDes(int idCategoryDes) {
        this.idCategoryDes = idCategoryDes;
    }

    public String isOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    private int idCategoryDes;


    private String order;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Este método es responsable de determinar si la relación será opcional u obligatoria.
     *
     * @return Retorna <code>true</code> si el límite inferior de la multiplicidad es cero y <code>false</code> sino.
     */
    public boolean isOptional() {
        return (multiplicity.getLowerBoundary() == 0);
    }

    public RelationshipDefinition getExcludes() {
        return excludes;
    }

    public void setExcludes(RelationshipDefinition excludes) {
        this.excludes = excludes;
    }

    public TargetDefinition getTargetDefinition() {
        return targetDefinition;
    }

    public void setTargetDefinition(TargetDefinition targetDefinition) {
        this.targetDefinition = targetDefinition;
    }

    public Multiplicity getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(Multiplicity multiplicity) {
        this.multiplicity = multiplicity;
    }

    public List<RelationshipAttributeDefinition> getRelationshipAttributeDefinitions() {
        return relationshipAttributeDefinitions;
    }

    public void setRelationshipAttributeDefinitions(List<RelationshipAttributeDefinition> relationshipAttributeDefinitions) {
        this.relationshipAttributeDefinitions = relationshipAttributeDefinitions;
    }

    public boolean hasRelationshipAttributeDefinitions() {
        return !relationshipAttributeDefinitions.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o != null) if (this.id == ((RelationshipDefinition) o).getId()) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelationshipDefinition that = (RelationshipDefinition) o;

        return this.id == that.id;

    }


    @Override
    public String toString() {
        return "id: " + id + ". [" + super.toString() + "]";
    }


    public RelationshipAttributeDefinition getOrderAttributeDefinition() {
        for (RelationshipAttributeDefinition relationshipAttributeDefinition : getRelationshipAttributeDefinitions()) {
            if (relationshipAttributeDefinition.isOrderAttribute()) {
                return relationshipAttributeDefinition;
            }
        }
        return null;
    }

    public RelationshipAttributeDefinition getGroupAttributeDefinition() {
        for (RelationshipAttributeDefinition relationshipAttributeDefinition : getRelationshipAttributeDefinitions()) {
            if (relationshipAttributeDefinition.isGroupSCT()) {
                return relationshipAttributeDefinition;
            }
        }
        return null;
    }

    public boolean isRequired() {
        return getMultiplicity().getLowerBoundary() > 0;
    }

    public boolean isISP() {
        return this.getName().equalsIgnoreCase(TargetDefinition.ISP);
    }

    public boolean isATC(){

        return this.getName().equalsIgnoreCase(TargetDefinition.ATC);

        /*
        if (!this.getTargetDefinition().isHelperTable()){
            return false;
        }

        HelperTable helperTable = (HelperTable) this.targetDefinition;
        return helperTable.getId() == HelperTableSearchBR.HT_ATC_ID;
        */
    }

    public boolean isBioequivalente() {
        return this.getName().equalsIgnoreCase(TargetDefinition.BIOEQUIVALENTE);
    }

    public boolean isComercializado() {
        return this.getName().equalsIgnoreCase(TargetDefinition.COMERCIALIZADO);
    }

    public boolean isU_asist() { return this.getName().equalsIgnoreCase(TargetDefinition.U_ASIST); }

    public boolean isCondicionDeVenta() { return this.getName().equalsIgnoreCase(TargetDefinition.CONDICION_DE_VENTA); }

    public boolean isPedible(){

        return this.getName().equalsIgnoreCase(TargetDefinition.PEDIBLE);
    }

    private final String SNOMEDCT="SNOMED CT";

    public boolean isSNOMEDCT(){
        return this.getName().equalsIgnoreCase(SNOMEDCT);
    }

}
