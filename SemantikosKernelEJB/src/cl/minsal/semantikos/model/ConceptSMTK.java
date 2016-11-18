package cl.minsal.semantikos.model;

import cl.minsal.semantikos.model.audit.AuditableEntity;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.businessrules.ConceptStateBusinessRulesContainer;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Esta clase representa al Concepto Semantikos.
 *
 * @author Diego Soto.
 */
public class ConceptSMTK extends PersistentEntity implements Target, AuditableEntity {

    /** El valor que posee un CONCEPT_ID que no ha sido definido */
    public static final String CONCEPT_ID_UNDEFINED = "-1";

    /** El valor de negocio del concept_id */
    private String conceptID = CONCEPT_ID_UNDEFINED;

    /** La categoría del concepto */
    private Category category;

    /** Si debe ser revisado */
    private boolean isToBeReviewed;

    /** Si debe ser consultado? */
    private boolean isToBeConsulted;

    /** Si el concepto se encuentra modelado o no */
    private boolean modeled;

    /**
     * Este campo establece si el concepto está completamente definido o si es primitivo. Por defecto, el concepto se
     * considera primitivo
     */
    private boolean isFullyDefined;

    /** Determina si el concepto está publicado o no */
    private boolean isPublished;

    /** Fecha hasta la cual el concepto se encuentra vigente */
    private Timestamp validUntil;

    /** Otros descriptores */
    private List<Description> descriptions = new ArrayList<>();

    /** Observación del Concepto */
    private String observation;

    /**
     * Relaciones. Las relaciones se cargan de manera perezosa. Para no almacenar una lista nula (y así evitar
     * NullPointerException) se maneja también un booleano que indica si las relaciones fueron o no cargadas.
     */
    private List<Relationship> relationships = new ArrayList<>();

    /* Relaciones cargadas */
    private boolean relationshipsLoaded = false;

    /** The concept's labels */
    private List<Label> labels = new ArrayList<>();

    /** Lista de etiquetas */
    private List<Tag> tags = new ArrayList<>();

    /**
     * Este método es responsable de determinar si el estado de publicación del concepto cambio recientemente.
     */
    private boolean justPublished = false;

    /** El Tag Semántikos que tiene asociado el concepto */
    private TagSMTK tagSMTK;

    /** RefSets a los que pertenece el concepto */
    private List<RefSet> refsets;

    public ConceptSMTK() {
        super(PersistentEntity.NON_PERSISTED_ID);
    }

    /**
     * La categoría es la mínima información que se le puede dar a un concepto.
     */
    public ConceptSMTK(Category category) {
        super(PersistentEntity.NON_PERSISTED_ID);

        /* La categoría del concepto */
        this.category = category;

        /* El concepto parte con su estado inicial */
        this.descriptions = new ArrayList<>();

        this.modeled = false;
        this.isFullyDefined = false;
        this.isPublished = false;
        this.isToBeConsulted = false;
        this.isToBeReviewed = false;

        /** Categoría del concepto */
        this.category = null;
        this.relationshipsLoaded = true;

        /* La observación asociada al concepto inicialmente es vacía */
        this.observation = "";
    }

    public ConceptSMTK(Category category, @NotNull Description... descriptions) {
        this(category);
        this.descriptions.addAll(Arrays.asList(descriptions));
    }

    public ConceptSMTK(Category category, boolean modeled, Description... descriptions) {
        this(category, descriptions);
        this.modeled = modeled;
    }

    /**
     * Este es el constructor más completo, ideal de utilizar al crear objetos persistentes.
     *
     * @param id              Identificador único.
     * @param conceptID       CONCEPT_ID del concepto.
     * @param category        Su categoría.
     * @param isToBeReviewed  Si debe ser revisado.
     * @param isToBeConsulted Si debe ser consultado.
     * @param modeled         Si se encuentra modelado.
     * @param isFullyDefined  Si es Completamente definido.
     * @param isPublished     Si se encuentra publicado
     * @param observation     La observación.
     * @param tagSMTK         El Tag Semántikos asociado al concepto.
     * @param descriptions    Sus descripciones.
     */
    public ConceptSMTK(long id, String conceptID, Category category, boolean isToBeReviewed, boolean isToBeConsulted, boolean modeled, boolean isFullyDefined, boolean isPublished, String observation, TagSMTK tagSMTK, Description... descriptions) {
        this(category, modeled, descriptions);

        this.setId(id);

        this.conceptID = conceptID;
        this.category = category;
        this.isToBeReviewed = isToBeReviewed;
        this.isToBeConsulted = isToBeConsulted;
        this.isFullyDefined = isFullyDefined;
        this.isPublished = isPublished;
        this.observation = observation;
        this.tagSMTK = tagSMTK;

        /* Se indica que no se han cargado sus relaciones */
        this.relationshipsLoaded = false;
    }

    /**
     * Constructor canónico para un concepto smtk
     *
     * @param conceptID       El conceptID (identificador de negocio) de este concepto
     * @param category        La categoría a la cual pertenece este concepto
     * @param isToBeReviewed  ¿Es para ser revisado?
     * @param isToBeConsulted ¿Es para ser consultado?
     * @param modeled         El estado de este concepto
     * @param isFullyDefined  ¿Completamente definido?
     * @param isPublished     ¿Publicado?
     * @param descriptions    Las descripciones para este concepto
     */
    public ConceptSMTK(String conceptID, Category category, boolean isToBeReviewed, boolean isToBeConsulted, boolean modeled, boolean isFullyDefined, boolean isPublished, String observation, TagSMTK tagSMTK, Description... descriptions) {
        this(NON_PERSISTED_ID, conceptID, category, isToBeReviewed, isToBeConsulted, modeled, isFullyDefined, isPublished, observation, tagSMTK, descriptions);

        /* Se indica que no se han cargado sus relaciones */
        this.relationshipsLoaded = true;
    }

    public List<Description> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<Description> descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * Este método es responsable de recuperar las relaciones del concepto.
     *
     * @return Una lista con las relaciones del concepto.
     */
    public List<Relationship> getRelationships() {

        if (!relationshipsLoaded) {
            throw new BusinessRuleException("Las relaciones de este concepto no han sido cargadas aun.");
        }

        return relationships;
    }

    /**
     * Este método es responsable de retornar todas las relaciones de este concepto que son de un cierto tipo de
     * relación.
     *
     * @param relationshipDefinition El tipo de relación al que pertenecen las relaciones a retornar.
     *
     * @return Una <code>java.util.List</code> de relaciones de tipo <code>relationshipDefinition</code>.
     */
    public List<Relationship> getRelationshipsByRelationDefinition(RelationshipDefinition relationshipDefinition) {
        List<Relationship> someRelationships = new ArrayList<>();
        for (Relationship relationship : relationships) {
            if (relationship.getRelationshipDefinition().equals(relationshipDefinition)) {
                someRelationships.add(relationship);
            }
        }
        return someRelationships;
    }

    /**
     * Este método es responsable de retornar las relaciones de tipo SNOMED_CT.
     *
     * @return Una lista de relaciones a SnomedCT
     */
    public List<SnomedCTRelationship> getRelationshipsSnomedCT() {

        List<SnomedCTRelationship> snomedRelationships = new ArrayList<>();
        for (Relationship relationship : relationships) {
            if (relationship.getRelationshipDefinition().getTargetDefinition().isSnomedCTType()) {
                snomedRelationships.add(relationship.toSnomedCT());
            }
        }

        return snomedRelationships;
    }

    public List<Relationship> getRelationshipsBasicType() {
        List<Relationship> snomedRelationships = new ArrayList<>();
        for (Relationship relationship : relationships) {
            if (relationship.getRelationshipDefinition().getTargetDefinition().isBasicType()) {
                snomedRelationships.add(relationship);
            }
        }
        return snomedRelationships;
    }

    public List<Relationship> getRelationshipsNonBasicType() {
        List<Relationship> snomedRelationships = new ArrayList<>();
        for (Relationship relationship : relationships) {
            if (!relationship.getRelationshipDefinition().getTargetDefinition().isBasicType()) {
                snomedRelationships.add(relationship);
            }
        }
        return snomedRelationships;
    }

    /**
     * Este método es responsable de retornar todas las relaciones válidas de este concepto
     *
     * @return Una <code>java.util.List</code> de relaciones de tipo <code>relationshipDefinition</code>.
     */
    public List<Relationship> getValidRelationships() {
        List<Relationship> someRelationships = new ArrayList<>();
        for (Relationship relationship : relationships) {
            if (relationship.isValid()) {
                someRelationships.add(relationship);
            }
        }
        return someRelationships;
    }

    /**
     * Este método es responsable de retornar todas las relaciones de tipo atributo.
     *
     * @return Una <code>java.util.List</code> de relaciones que son del tipo Atributo.
     */
    public List<Relationship> getAttributes() {
        List<Relationship> someRelationships = new ArrayList<>();
        for (Relationship relationship : relationships) {
            if (relationship.isAttribute()) {
                someRelationships.add(relationship);
            }
        }
        return someRelationships;
    }

    /**
     * Este método es responsable de retornar todas las relaciones válidas de este concepto y que son de un cierto tipo
     * de
     * relación.
     *
     * @param relationshipDefinition El tipo de relación al que pertenecen las relaciones a retornar.
     *
     * @return Una <code>java.util.List</code> de relaciones de tipo <code>relationshipDefinition</code>.
     */
    public List<Relationship> getValidRelationshipsByRelationDefinition(RelationshipDefinition relationshipDefinition) {
        List<Relationship> someRelationships = new ArrayList<>();
        for (Relationship relationship : relationships) {
            if (relationship.getRelationshipDefinition().equals(relationshipDefinition) && relationship.isValid()) {
                someRelationships.add(relationship);
            }
        }
        return someRelationships;
    }

    /**
     * Este método es responsable de retornar todas las descripciones válidas de este concepto y que son de un cierto
     * tipo
     *
     * @return Una <code>java.util.List</code> de relaciones de tipo <code>description</code>.
     */
    public List<Description> getValidDescriptions() {
        List<Description> someDescriptions = new ArrayList<>();
        for (Description description : descriptions) {
            if (description.isValid()) {
                someDescriptions.add(description);
            }
        }
        return someDescriptions;
    }

    /**
     * Este método determina si existen instancias de relaciones asociadas a una definición de relación
     *
     * @param relationshipDefinition El tipo de relación al que pertenecen las relaciones a retornar.
     *
     * @return Un <code>java.lang.boolean</code>
     */
    public boolean hasRelationships(RelationshipDefinition relationshipDefinition) {
        return !getRelationshipsByRelationDefinition(relationshipDefinition).isEmpty();
    }

    public void setRelationships(List<Relationship> relationships) {
        this.relationships = relationships;
        this.relationshipsLoaded = true;
    }

    public boolean isRelationshipsLoaded() {
        return relationshipsLoaded;
    }

    public String getConceptID() {
        return conceptID;
    }

    public void setConceptID(String conceptID) {
        this.conceptID = conceptID;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isToBeReviewed() {
        return isToBeReviewed;
    }

    public void setToBeReviewed(boolean toBeReviewed) {
        this.isToBeReviewed = toBeReviewed;
    }

    public boolean isToBeConsulted() {
        return isToBeConsulted;
    }

    public void setToBeConsulted(boolean toBeConsulted) {
        this.isToBeConsulted = toBeConsulted;
    }

    public boolean isFullyDefined() {
        return isFullyDefined;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Timestamp getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Timestamp validUntil) {
        this.validUntil = validUntil;
    }

    /**
     * Este método es responsable de establecer si un concepto es completamente definido.
     */
    public void setFullyDefined(boolean fullyDefined) {

        /* Antes de asignarle la propiedad, ser verifica si cumple las reglas de negocio */
        new ConceptStateBusinessRulesContainer().apply(this);

        /* Como se han validado las reglas de negocio, se realiza la asignación */
        this.isFullyDefined = fullyDefined;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {

        /* Si no estaba publicada se marca como "recién publicada" */
        if (!this.isPublished && published) {
            this.justPublished = true;
        }

        this.isPublished = published;
    }

    public List<Label> getLabels() {
        return labels;
    }

    /**
     * Este método es responsable de agregar una etiqueta al concepto.
     *
     * @param label La etiqueta a ser agregada.
     *
     * @return <code>true</code> si se agrega y <code>false</code> si no.
     */
    public boolean addLabel(@NotNull Label label) {
        return this.labels.add(label);
    }

    /**
     * Este método es responsable de agregar una etiqueta al concepto.
     *
     * @param description La descripción a ser agregada.
     */
    public void addDescription(Description description) {

        /** La descripción asume el estado modelado si el concepto está en modelado */
        if (this.isModeled()){
            description.setModeled(true);
        }

        this.descriptions.add(description);
    }

    /**
     * Este método es responsable de agregar una etiqueta al concepto.
     *
     * @param label La etiqueta a eliminar.
     *
     * @return <code>true</code> si se elimina y <code>false</code> si no.
     */
    public boolean removeLabel(@NotNull Label label) {
        return this.labels.remove(label);
    }

    public void removeDescription(Description description) {
        this.descriptions.remove(description);
    }

    /**
     * Este método es responsable de agregar una relación a un concepto.
     *
     * @param relationship La relación que es agregada.
     */
    public void addRelationship(Relationship relationship) {
        this.getRelationships().add(relationship);
    }

    /**
     * Este método es responsable de agregar un Tag (Etiqueta) a un concepto.
     *
     * @param tag La relación que es agregada.
     */
    public void addTag(Tag tag) {
        this.getTags().add(tag);
    }


    /**
     * Este método es responsable de remover una relación a un concepto.
     *
     * @param relationship La relación que es removida.
     */
    public void removeRelationship(Relationship relationship) {
        this.getRelationships().remove(relationship);
    }

    /**
     * <p>
     * Este método es responsable de retornar la <i>descripción preferida</i>. Basados en la regla de negocio que dice
     * que un concepto debe siempre tener una y solo una descripción preferida.</p>
     * <p>
     * Si el concepto no tuviera descripción preferida, se retorna una descripción "sin tipo".
     * </p>
     *
     * @return La descripción preferida.
     */
    public Description getDescriptionFavorite() {
        for (Description description : descriptions) {
            if (description.getDescriptionType().getName().equalsIgnoreCase("preferida")) {
                return description;
            }
        }

        /* En este punto, no se encontró una descripción preferida, y se arroja una excepción */
        throw new BusinessRuleException("Concepto sin descripción preferida");
    }


    /**
     * <p>
     * Este método es responsable de determinar si este concepto tiene una <i>descripción preferida</i>. Basados en la
     * regla de negocio que dice que un concepto debe siempre tener una y solo una descripción preferida.</p>
     *
     * @return <code>true</code> si el concepto tiene Descripción Preferida y <code>false</code> sino.
     */
    public boolean hasFavouriteDescription() {
        for (Description description : descriptions) {
            DescriptionType favoriteDescriptionType = DescriptionTypeFactory.getInstance().getFavoriteDescriptionType();
            if (description.getDescriptionType().equals(favoriteDescriptionType)) {
                return true;
            }
        }

        /* En este punto, no se encontró una descripción preferida, y se arroja una excepción */
        return false;
    }

    /**
     * <p>
     * Este método es responsable de retornar la <i>descripción FSN</i>. Basados en la regla de negocio que dice
     * que un concepto debe siempre tener una y solo una descripción FSN.</p>
     * <p>
     * Si el concepto no tuviera descripción preferida, se retorna una descripción "sin tipo".
     * </p>
     *
     * @return La descripción preferida.
     */
    public Description getDescriptionFSN() {
        for (Description description : descriptions) {
            DescriptionType descriptionType = description.getDescriptionType();
            if (descriptionType.getName().equalsIgnoreCase("FSN")) {
                return description;
            }
        }

        /* En este punto, no se encontró una descripción preferida, y se arroja una excepción */
        throw new BusinessRuleException("Concepto sin descripción FSN");
    }

    @Override
    public String toString() {

        if (descriptions.isEmpty()) {
            return "ID=" + this.getId() + " - ConceptID=" + this.getConceptID();
        }

        if (this.hasFavouriteDescription()) {
            Description descriptionFavorite = getDescriptionFavorite();
            return "Preferida: " + descriptionFavorite.getTerm();
        }
        Description aDescription = this.descriptions.get(1);
        return aDescription.getDescriptionType().getName() + ": " + aDescription.getTerm();
    }

    /**
     * Este método es responsable de determinar si el concepto pertenece a una cierta categoría.
     *
     * @param category La categoría a la cual se desea determinar si pertenece o no.
     *
     * @return <code>true</code> si está asociado a dicha categoría y <code>false</code> sino.
     */
    public boolean belongsTo(Category category) {

        /* Si no tiene categoría, no pertenece a ninguna */
        return this.category != null && this.category.equals(category);
    }

    /**
     * Este método es responsable de entregar una lista con todas las relaciones que tienen como destino un cierto tipo
     * de objeto: conceptos SMTK, conceptos Snomed CT, Tablas Auxiliares, Terminologías externas, etc.
     *
     * @param targetType El tipo de destino al cual apuntan las relaciones.
     *
     * @return Una lista con todas las relaciones de este concepto que tienen como destino un objeto del tipo de destino
     * <code>targetType</code>.
     */
    public List<Relationship> getRelationshipsTo(TargetType targetType) {

        List<Relationship> sctRelations = new ArrayList<>();
        for (Relationship relationship : this.getRelationships()) {
            if (relationship.getTarget().getTargetType().equals(targetType)) {
                sctRelations.add(relationship);
            }
        }

        return sctRelations;
    }

    /**
     * Este método es responsable de determinar si <code>this</code> concepto posee al menos las mismas
     * <code>relationships</code>. El contener quiere decir que el concepto tiene relaciones con el mismo
     * RelationshipDefinition y con el mismo destino (<code>Target</code>).
     *
     * @param relationships Las relaciones que se desea evaluar si están contenidas en el concepto fuente.
     *
     * @return <code>true</code> si el concepto contiene estas relaciones y <code>false</code> sino.
     */
    public boolean contains(List<Relationship> relationships) {

        for (Relationship relationship : relationships) {
            if (!this.contains(relationship)) {
                return false;
            }
        }

        /* En este punto todas las relaciones tienen una equivalente */
        return true;
    }

    /**
     * Este método es responsable de validar si este Concepto posee una relación del mismo tipo de relación (mismo
     * <code>RelationshipDefinition</code>) y cuyo destino es el mismo igualmente.
     *
     * @param relationship La relación que se desea determinar si el concepto posee una equivalente.
     *
     * @return <code>true</code> si contiene una relación asi y <code>false</code> sino.
     */
    protected boolean contains(Relationship relationship) {
        return this.getRelationships().contains(relationship);
    }

    public boolean isModeled() {
        return this.modeled;
    }

    public void setModeled(boolean modeled) {
        this.modeled = modeled;
    }

    /**
     * Este método es responsable de indicar si el el concepto sufrió un cambio en su estado de publicación: de no
     * publicado a publicado.
     *
     * @return <code>true</code> si fue recién publicado y <code>false</code> sino.
     */
    public boolean isJustPublished() {
        return justPublished;
    }

    public TagSMTK getTagSMTK() {
        return tagSMTK;
    }

    public void setTagSMTK(TagSMTK tagSMTK) {
        this.tagSMTK = tagSMTK;
    }

    public List<RefSet> getRefsets() {
        return refsets;
    }

    public void setRefsets(List<RefSet> refsets) {
        this.refsets = refsets;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof ConceptSMTK) && (String.valueOf(conceptID) != null)
                ? String.valueOf(conceptID).equals(String.valueOf(((ConceptSMTK) other).conceptID))
                : (other == this);
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.SMTK;
    }

    @Override
    public Target copy() {
        ConceptSMTK conceptSMTK = new ConceptSMTK(this.getCategory());
        conceptSMTK.setId(this.getId());
        conceptSMTK.setConceptID(this.getConceptID());
        conceptSMTK.setDescriptions(this.getDescriptions());
        conceptSMTK.setRelationships(this.relationships);
        return conceptSMTK;
    }
}
