package cl.minsal.semantikos.model.relationships;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;

import javax.ejb.EJBException;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
 * Esta clase representa una relación hacia un concepto Snomed-CT.
 *
 * @author Andrés Farías created on 9/27/16.
 */
public class SnomedCTRelationship extends Relationship {

    public static final String ES_UN_MAPEO_DE = "es un mapeo";
    public static final String ES_UN = "es un[a]";
    private static final long TIPO_RELACION_HELPER_TABLE_ID = 18;

    public SnomedCTRelationship(ConceptSMTK sourceConcept, ConceptSCT conceptSCT, RelationshipDefinition relationshipDefinition, List<RelationshipAttribute> relationshipAttributes, Timestamp validityUntil) {
        super(sourceConcept, conceptSCT, relationshipDefinition, relationshipAttributes, validityUntil);
    }

    public SnomedCTRelationship(@NotNull long id, @NotNull ConceptSMTK sourceConcept, @NotNull ConceptSCT conceptSCT, @NotNull RelationshipDefinition relationshipDefinition, List<RelationshipAttribute> relationshipAttributes, Timestamp validityUntil) {
        super(id, sourceConcept, conceptSCT, relationshipDefinition, validityUntil, relationshipAttributes);
    }

    /**
     * Este método permite crear una relación de tipo SnomedCT a partir de una relación normal.
     *
     * @param relationship La relación a SnomedCT.
     *
     * @return Un objeto de tipo <code>SnomedCTRelationship</code> creado a partir de la relación.
     */
    public static SnomedCTRelationship createSnomedCT(Relationship relationship) {

        /* Se valida que la relación SI sea SnomedCT */
        if (!SnomedCTRelationship.isSnomedCTRelationship(relationship)) {
            throw new IllegalArgumentException("No es posible crear una relación a SnomedCT a partir de esta relación: " + relationship);
        }

        /* Los campos para crear el nuevo objeto */
        ConceptSMTK sourceConcept = relationship.getSourceConcept();
        ConceptSCT conceptSCT = (ConceptSCT) relationship.getTarget();
        RelationshipDefinition relationshipDefinition = relationship.getRelationshipDefinition();
        List<RelationshipAttribute> relationshipAttributes = relationship.getRelationshipAttributes();
        Timestamp validityUntil = relationship.getValidityUntil();

        if (relationship.isPersistent()) {
            long id = relationship.getId();
            new SnomedCTRelationship(id, sourceConcept, conceptSCT, relationshipDefinition, relationshipAttributes, validityUntil);
        }
        return new SnomedCTRelationship(sourceConcept, conceptSCT, relationshipDefinition, relationshipAttributes, validityUntil);
    }

    /**
     * Este método, muy importante en el modelo de negocio, permite determinar si <em>esta</em> relación es definitoria
     * (<em>definitional</em> en inglés) o de atributo.
     * <p> La definición qu ese ha considerado consiste en que la relación tiene un atributo a una tabla auxiliar
     * llamada TIPO_RELACION, con un valor particular: "es un" o "es un mapeo de".</p>
     *
     * @return <code>true</code> si es definitoria, y <code>false</code> sino.
     */
    public boolean isDefinitional() {
        return this.isES_UN() || this.isES_UN_MAPEO();
    }

    public boolean isES_UN() {
        return getSnomedCTRelationshipType().equalsIgnoreCase(ES_UN);
    }

    public boolean isES_UN_MAPEO() {
        return getSnomedCTRelationshipType().equalsIgnoreCase(ES_UN_MAPEO_DE);
    }

    /**
     * Este método es responsable de validar si una relación es del tipo Snomed CT de tipo ES UN MAPEO
     *
     * @param relationship La relación que se desea validar.
     *
     * @return <code>true</code> si la relación es una relación Snomed CT
     */
    public static boolean isES_UN_MAPEO(Relationship relationship) {

        /* Se valida que la relación sea de tipo Snomed CT, sino la validación está clara */
        if (!relationship.getRelationshipDefinition().getTargetDefinition().isSnomedCTType()) {
            return false;
        }

        /* Es una relación SCT. Se realiza el cast y se verifica su tipo */
        SnomedCTRelationship snomedCTRelationship = (SnomedCTRelationship) relationship;
        return snomedCTRelationship.isES_UN_MAPEO();
    }

    public String getSnomedCTRelationshipType() {

        /* Se obtienen los atributos de la relación y se itera por ellos para buscar las del tipo que sirve*/
        List<RelationshipAttribute> relationshipAttributes = this.getRelationshipAttributes();
        for (RelationshipAttribute relationshipAttribute : relationshipAttributes) {

            /* El atributo debe ser de tipo Helper Table (en particular a la tabla de tipos de relaciones */
            if (relationshipAttribute.getRelationAttributeDefinition().getTargetDefinition().isHelperTable()) {
                HelperTable table = (HelperTable) relationshipAttribute.getRelationAttributeDefinition().getTargetDefinition();

                //se valida el tipo de la tabla
                if(table.getId()==TIPO_RELACION_HELPER_TABLE_ID) {
                    HelperTableRow row = (HelperTableRow) relationshipAttribute.getTarget();
                    return row.getDescription();
                }
            }
        }

        throw new EJBException("Esta relación no posee un tipo de relación Snomed-CT");
    }

    /**
     * Entrega el valor del grupo SnomedCT de esta relacion SnomedCT
     * @return
     */
    public String getGroup() {
        List<RelationshipAttribute> attributes = getRelationshipAttributes();

        if ( attributes != null ) {
            for ( RelationshipAttribute attribute : attributes ) {
                if ( attribute.getTarget() != null
                        && attribute.getTarget() instanceof BasicTypeValue
                        && attribute.getRelationAttributeDefinition() != null
                        && "Grupo".equals(attribute.getRelationAttributeDefinition().getName())) {
                    return String.valueOf(((BasicTypeValue) attribute.getTarget()).getValue());
                }
            }
        }

        return null;
    }

    @Override
    public ConceptSCT getTarget() {
        return (ConceptSCT) super.getTarget();
    }

    /**
     * Este método es responsable de encapsular la consulta de si una relación es de tipo Snomed CT.
     *
     * @param relationship La relación en cuestión.
     *
     * @return <code>true</code> si es una relación SnomedCT y <code>false</code> sino.
     */
    public static boolean isSnomedCTRelationship(Relationship relationship) {
        return relationship.getRelationshipDefinition().getTargetDefinition().isSnomedCTType();
    }

    @Override
    public String toString() {
        return this.getSourceConcept() + " --SCT[" + getSnomedCTRelationshipType() + "]--> " + getTarget();

    }
}
