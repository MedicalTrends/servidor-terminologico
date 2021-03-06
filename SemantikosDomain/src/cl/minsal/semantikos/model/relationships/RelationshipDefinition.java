package cl.minsal.semantikos.model.relationships;

import java.io.Serializable;
import java.util.ArrayList;
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
        //return "id: " + id + ". [" + super.toString() + "]";
        return "id: " + id + ". [" + name + "]";
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

    public RelationshipAttributeDefinition getPPAttributeDefinition() {
        for (RelationshipAttributeDefinition relationshipAttributeDefinition : getRelationshipAttributeDefinitions()) {
            if (relationshipAttributeDefinition.isUnidadPPAttribute()) {
                return relationshipAttributeDefinition;
            }
        }
        return null;
    }


    public List<RelationshipAttributeDefinition> findRelationshipAttributeDefinitionsByName(String name) {
        List<RelationshipAttributeDefinition> someRelationshipAttributeDefinitions = new ArrayList<>();

        for (RelationshipAttributeDefinition relationshipAttributeDefinition : getRelationshipAttributeDefinitions()) {
            if(relationshipAttributeDefinition.getName().equalsIgnoreCase(name)) {
                someRelationshipAttributeDefinitions.add(relationshipAttributeDefinition);
            }
        }

        return someRelationshipAttributeDefinitions;
    }

    public List<RelationshipAttributeDefinition> findRelationshipAttributeDefinitionsById(long id) {
        List<RelationshipAttributeDefinition> someRelationshipAttributeDefinitions = new ArrayList<>();

        for (RelationshipAttributeDefinition relationshipAttributeDefinition : getRelationshipAttributeDefinitions()) {
            if(relationshipAttributeDefinition.getId() == id) {
                someRelationshipAttributeDefinitions.add(relationshipAttributeDefinition);
            }
        }

        return someRelationshipAttributeDefinitions;
    }

    public boolean isRequired() {
        return getMultiplicity().getLowerBoundary() > 0;
    }

    public boolean isSubstance() {
        return this.getName().equalsIgnoreCase(TargetDefinition.SUSTANCIA);
    }

    public boolean isMB() {
        return this.getName().equalsIgnoreCase(TargetDefinition.MB);
    }

    public boolean isDB() {
        return this.getName().equalsIgnoreCase(TargetDefinition.DB);
    }

    public boolean isISP() {
        return this.getName().equalsIgnoreCase(TargetDefinition.ISP);
    }

    public boolean isATC(){
        return this.getName().equalsIgnoreCase(TargetDefinition.ATC);
    }

    public boolean isGTIN(){
        return this.getName().equalsIgnoreCase(TargetDefinition.GTINGS1);
    }

    public boolean isDCI(){
        return this.getName().equalsIgnoreCase(TargetDefinition.DCI);
    }

    public boolean isBioequivalente() {
        return this.getName().equalsIgnoreCase(TargetDefinition.BIOEQUIVALENTE);
    }

    public boolean isComercializado() {
        return this.getName().equalsIgnoreCase(TargetDefinition.COMERCIALIZADO);
    }

    public boolean isU_asist() { return this.getName().equalsIgnoreCase(TargetDefinition.U_ASIST); }

    public boolean isCondicionDeVenta() { return this.getName().equalsIgnoreCase(TargetDefinition.CONDICION_DE_VENTA); }

    public boolean isPedible() {

        return this.getName().equalsIgnoreCase(TargetDefinition.PEDIBLE);
    }

    public boolean isFFA() { return this.getName().equalsIgnoreCase(TargetDefinition.FFA); }

    public boolean isFFADisp() { return this.getName().equalsIgnoreCase(TargetDefinition.FFA_DISP); }

    public boolean isMCCE() { return this.getName().equalsIgnoreCase(TargetDefinition.MCCE); }

    public boolean isMCSpecial() {
        return this.getName().equalsIgnoreCase(TargetDefinition.MC_SPECIAL);
    }

    public boolean isClasificacionDeRiesgo() {
        return this.getName().equalsIgnoreCase(TargetDefinition.CLASIFICACION_DE_RIESGO);
    }

    public boolean isDIPrimario() {
        return this.getName().equalsIgnoreCase(TargetDefinition.DI_PRIMARIO);
    }

    public boolean isCantidadPP() {
        return this.getName().equalsIgnoreCase(TargetDefinition.CANTIDAD_PP);
    }

    public boolean isCantidadVolumenTotal() {
        return this.getName().equalsIgnoreCase(TargetDefinition.CANTIDAD_VOLUMEN_TOTAL);
    }

    private final String SNOMEDCT="SNOMED CT";

    public boolean isSNOMEDCT(){
        return this.getName().equalsIgnoreCase(SNOMEDCT);
    }

    public boolean isCIE10() {
        return this.getName().equalsIgnoreCase(TargetDefinition.CIE10);
    }

    public boolean isGMDN() {
        return this.getName().equalsIgnoreCase(TargetDefinition.GMDN);
    }

    public boolean isAttributeSpecial() {

        for (String s : name.split(" ")) {
            if(s.equalsIgnoreCase("Especial")) {
                return true;
            }
        }

        return false;
    }

    public boolean isAttributeLaboratory() {

        for (String s : name.split(" ")) {
            if(s.equalsIgnoreCase("Laboratorio")) {
                return true;
            }
        }

        return false;
    }

    public boolean isAttributeHelperTableCode() {

        return this.isATC() || this.isDCI();

    }


    public boolean hasAttributePP() {

        for (RelationshipAttributeDefinition relationshipAttributeDefinition : relationshipAttributeDefinitions) {

            for (String s : relationshipAttributeDefinition.getName().split(" ")) {
                if(s.equalsIgnoreCase("PP")) {
                    return true;
                }
            }
        }

        return false;
    }

}
