package cl.minsal.semantikos.model.relationships;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.audit.AuditableEntity;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.crossmaps.*;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Una Relación es una asociación con significado entre 2 cosas.</p>
 * <p>
 * En el Modelo General de Terminologías los Conceptos pueden estar relacionados entre sí o con otras entidades,
 * entonces, una Relación es una asociación entre un Concepto Origen y un Objeto Destino.</p>
 * <p>Cada Relación agrega información sobre el Concepto Origen.</p>
 * Hay 2 Tipos de Relaciones:
 * •	Definitorias
 * •	De Atributos
 *
 * @author Andrés Farías
 */
public class Relationship extends PersistentEntity implements AuditableEntity, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Relationship.class);

    /** ID de negocio (Solo utilizado en relaciones SCT) */
    private String idRelationship;

    /** El concepto origen de esta relación */
    private ConceptSMTK sourceConcept;

    /** La definición de esta relación */
    private RelationshipDefinition relationshipDefinition;

    /** El elemento destino de esta relación */
    private Target target;

    /** La relación es Vigente (valida) hasta la fecha... */
    private Timestamp validityUntil;

    /** Fecha en que fue creada la relación */
    private Timestamp creationDate;

    /** Indica si ha sufrido modificaciones que requieran un update */
    private boolean toBeUpdated = false;

    /** Lista de Atributos de la relacion * */
    private List<RelationshipAttribute> relationshipAttributes;

    /**
     * Este es el constructor mínimo con el cual se crean las Relaciones
     *
     * @param sourceConcept          El concepto origen de la relación.
     * @param relationshipDefinition Definición de la relación.
     * @param relationshipAttributes Lista de Atributos
     */
    @Deprecated
    public Relationship(ConceptSMTK sourceConcept, RelationshipDefinition relationshipDefinition, List<RelationshipAttribute> relationshipAttributes) {
        this.sourceConcept = sourceConcept;
        this.relationshipDefinition = relationshipDefinition;
        this.relationshipAttributes = relationshipAttributes;
    }

    /**
     * Este es el constructor mínimo con el cual se crean las Relaciones
     *
     * @param sourceConcept          El concepto origen de la relación.
     * @param target                 El valor de la relación (destino)
     * @param relationshipDefinition Definición de la relación.
     * @param relationshipAttributes Lista de Atributos
     */
    public Relationship(ConceptSMTK sourceConcept, Target target, RelationshipDefinition relationshipDefinition, List<RelationshipAttribute> relationshipAttributes, Timestamp validityUntil) {
        this.sourceConcept = sourceConcept;
        this.target = target;
        this.relationshipDefinition = relationshipDefinition;
        this.relationshipAttributes = relationshipAttributes;
    }

    /**
     * Igual al constructor mínimo, pero permite inicializar con el ID.
     *
     * @param id                     Identificador único de la relación.
     * @param sourceConcept          El concepto origen de la relación.
     * @param target                 El valor de la relación (destino)
     * @param relationshipDefinition Definición de la relación.
     * @param validityUntil          Fecha de vigencia hasta. Normalmente nulo si está vigente.
     */
    public Relationship(@NotNull long id, @NotNull ConceptSMTK sourceConcept, @NotNull Target target,
                        @NotNull RelationshipDefinition relationshipDefinition, Timestamp validityUntil, List<RelationshipAttribute> relationshipAttributes) {
        this(sourceConcept, target, relationshipDefinition, relationshipAttributes, validityUntil);

        /* Basic ID validation */
        if (id < 0) {
            throw new IllegalArgumentException("El ID de una relación no puede ser negativo o cero.");
        } else {
            super.setId(id);
        }
    }

    public String getIdRelationship() {
        return idRelationship;
    }

    public void setIdRelationship(String idRelationship) {
        this.idRelationship = idRelationship;
    }

    public ConceptSMTK getSourceConcept() {
        return sourceConcept;
    }

    public void setSourceConcept(ConceptSMTK sourceConcept) {
        this.sourceConcept = sourceConcept;
    }

    public RelationshipDefinition getRelationshipDefinition() {
        return relationshipDefinition;
    }

    public void setRelationshipDefinition(RelationshipDefinition relationshipDefinition) {
        this.relationshipDefinition = relationshipDefinition;
    }

    public Timestamp getValidityUntil() {
        return validityUntil;
    }

    public void setValidityUntil(Timestamp validityUntil) {
        this.validityUntil = validityUntil;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        if (target != null) {
            this.target = target;
        }
    }

    public boolean isToBeUpdated() {
        return toBeUpdated;
    }

    public void setToBeUpdated(boolean toBeUpdated) {
        this.toBeUpdated = toBeUpdated;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Este método es responsable de determinar si la relación tiene un valor consistente con su definición.
     *
     * @return <code>true</code> si la relación es consistente con su definición o <code>false</code> si no.
     */
    public boolean isConsistent() {
        return isConsistent(this.relationshipDefinition, this.target);
    }

    /**
     * Este método es responsable de determinar si la relación tiene un valor consistente con su definición.
     *
     * @param relationshipDefinition La definición de la relación.
     * @param target                 El valor destino de la relación.
     *
     * @return <code>true</code> si la relación es consistente con su definición o <code>false</code> si no.
     */
    private static boolean isConsistent(RelationshipDefinition relationshipDefinition, Target target) {
        if (relationshipDefinition.getTargetDefinition().isBasicType()) {
            return (target instanceof BasicTypeValue);
        }

        return relationshipDefinition.getTargetDefinition().isHelperTable() && (target instanceof HelperTableRow);
    }

    /**
     * Este método es responsable de determinar si la relación está bien definida, es decir, si posee un valor de
     * target y que éste es consistente con el Target Definition.
     *
     * @return <code>true</code> si está bien definida y <code>false</code> sino.
     */
    public boolean isWellDefined() {
        return target != null && this.isConsistent();
    }

    /**
     * Este método es responsable de retornar todas las relaciones de este concepto que son de un cierto tipo de
     * relación.
     *
     * @param relationshipAttributeDefinition El tipo de relación al que pertenecen las relaciones a retornar.
     *
     * @return Una <code>java.util.List</code> de relaciones de tipo <code>relationshipAttribute</code>.
     */
    public List<RelationshipAttribute> getAttributesByAttributeDefinition(RelationshipAttributeDefinition relationshipAttributeDefinition) {
        List<RelationshipAttribute> someAttributes = new ArrayList<>();
        for (RelationshipAttribute attribute : relationshipAttributes) {
            if (attribute.getRelationAttributeDefinition().equals(relationshipAttributeDefinition) && attribute.getTarget() != null) {
                someAttributes.add(attribute);
            }
        }
        return someAttributes;
    }

    /**
     * Este método es responsable de retornar todas las relaciones de este concepto que son de un cierto tipo de
     * relación.
     *
     * @param value El tipo de relación al que pertenecen las relaciones a retornar.
     *
     * @return Una <code>java.util.List</code> de relaciones de tipo <code>relationshipAttribute</code>.
     */
    public List<RelationshipAttribute> getBasicAttributesLike(boolean value) {

        List<RelationshipAttribute> someAttributes = new ArrayList<>();

        for (RelationshipAttribute attribute : relationshipAttributes) {
            if (attribute.getRelationAttributeDefinition().getTargetDefinition().isBasicType()) {
                BasicTypeValue basicTypeValue = (BasicTypeValue) target;
                if(basicTypeValue.getValue().equals(value)) {
                    someAttributes.add(attribute);
                }
            }
        }

        return someAttributes;
    }

    /**
     * Este método es responsable de determinar si esta relación es de tipo definitoria o atributo
     *
     * @return <code>true</code> si es de atributo y <code>false</code>.
     */
    public boolean isAttribute() throws Exception {

        /* Si es de tipo Snomed, hay que ver el valor de su atributo */
        if (SnomedCTRelationship.isSnomedCTRelationship(this)) {
            SnomedCTRelationship relationshipSCT = SnomedCTRelationship.createSnomedCT(this);
            return relationshipSCT.isDefinitional();
        }

        /* En cualquier otro caso es un atributo */
        return true;
    }

    /**
     * Este método es responsable de determinar si esta relación es válida
     *
     * @return <code>true</code> si es válida y <code>false</code> si no lo es.
     */
    public boolean isValid() {
        return (getValidityUntil() == null || getValidityUntil().after(new Timestamp(System.currentTimeMillis())));
    }

    public List<RelationshipAttribute> getRelationshipAttributes() {
        return relationshipAttributes;
    }

    public void setRelationshipAttributes(List<RelationshipAttribute> relationshipAttributes) {
        this.relationshipAttributes = relationshipAttributes;
    }

    public RelationshipAttribute getOrderAttribute() {
        for (RelationshipAttribute relationshipAttribute : getRelationshipAttributes()) {
            if (relationshipAttribute.getRelationAttributeDefinition().getName().equalsIgnoreCase("orden")) {
                return relationshipAttribute;
            }
        }
        return null;
    }

    public Integer getOrder() {

        RelationshipAttribute attribute = getOrderAttribute();

        if (attribute != null) {
            BasicTypeValue basicTypeValue = (BasicTypeValue) attribute.getTarget();
            return Integer.parseInt(basicTypeValue.getValue().toString());
        } else {
            return 0;
        }
    }

    public void setOrder(int order){
        RelationshipAttribute attribute = getOrderAttribute();

        if (attribute != null) {
            attribute.setTarget(new BasicTypeValue(order));
        }
    }

    public boolean isMultiplicitySatisfied(RelationshipAttributeDefinition attributeDefinition) {
        return this.getAttributesByAttributeDefinition(attributeDefinition).size() >= attributeDefinition.getMultiplicity().getLowerBoundary();
    }

    @Override
    public String toString() {
        return this.sourceConcept + " --" +  relationshipDefinition.getName() + "--> " + this.getTarget();
    }

    public Crossmap toCrossMap() throws Exception {

        if (!this.getRelationshipDefinition().getTargetDefinition().isCrossMapType()) {
            throw new IllegalArgumentException("Esta relación no puede ser transformada a una Relación SnomedCT");
        }

        Crossmap crossmap = (Crossmap) this;
        if (crossmap.is(CrossMapType.INDIRECT)){
            return new IndirectCrossmap(getId(),  this.sourceConcept, (CrossmapSetMember) target, this.relationshipDefinition, this.validityUntil);
        }


         if (this.isPersistent() && crossmap.is(CrossMapType.DIRECT)){
            return new DirectCrossmap(getId(), this.sourceConcept, (CrossmapSetMember) this.target, this.relationshipDefinition, this.validityUntil);
        }

        throw new Exception("UN CASO NO CONTEMPLADO");
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;

        if (o == null || !this.getClass().isAssignableFrom(o.getClass())) return false;

        /* Ahora comparamos con otra relacion */
        Relationship relationship = (Relationship) o;

        /* Se comparan las relaciones excepto por su concepto destino */
        if (!equalsButConceptSource(relationship)) return false;

        /* Se compara el concepto origen */
        return (this.getSourceConcept().getId() == relationship.getSourceConcept().getId());
    }

    /**
     * Este método es responsabled e comparar dos relaciones, sin exigir que su concepto de origen sea igual.
     *
     * @param relationship La relación contra la cual se hace la comparación.
     *
     * @return <code>true</code> si son idénticas, a excepción de sus conceptos origen.
     */
    public boolean equalsButConceptSource(Relationship relationship) {

        /* Si ambas están persistidas y no tienen el mismo ID, entonces son distintas */
        if (this.isPersistent() && relationship.isPersistent() && this.getId() != relationship.getId()) return false;

        /* Si alguna de ellas no está persistida, comparamos 1. tipo relacion, 2. sus atributos, y 3. el destino */

        /* 1. Se compara el tipo de relación y sus atributos */
        if (!this.getRelationshipDefinition().equals(relationship.getRelationshipDefinition())) return false;

        /* 2. Si no tienen los mismos atributos */
        if(this.getRelationshipAttributes().size() != relationship.getRelationshipAttributes().size()) {
            return false;
        }

        for (RelationshipAttribute attribute : this.getRelationshipAttributes()) {
            if (!relationship.getRelationshipAttributes().contains(attribute)) {
                return false;
            }
        }

        /* 3. Se compara el target */
        return this.getTarget().equals(relationship.getTarget());

    }

    @Override
    public int hashCode() {
        int result = sourceConcept != null ? sourceConcept.hashCode() : 0;
        result = 31 * result + (relationshipDefinition != null ? relationshipDefinition.hashCode() : 0);
        result = 31 * result + (target != null ? target.hashCode() : 0);
        result = 31 * result + (validityUntil != null ? validityUntil.hashCode() : 0);
        result = 31 * result + (toBeUpdated ? 1 : 0);
        result = 31 * result + (relationshipAttributes != null ? relationshipAttributes.hashCode() : 0);
        return result;
    }

    public RelationshipAttribute getAttribute(RelationshipAttributeDefinition definition) {
        for (RelationshipAttribute attribute : getRelationshipAttributes()) {
            if (definition.equals(attribute.getRelationAttributeDefinition()))
                return attribute;
        }

        return null;
    }

}
