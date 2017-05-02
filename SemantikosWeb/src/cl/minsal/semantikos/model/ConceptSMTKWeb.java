package cl.minsal.semantikos.model;

import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Diego Soto
 */
public class ConceptSMTKWeb extends ConceptSMTK {

    //Atributos básicos del concepto que son pasados a la vista

    //Descripciones que son pasadas a la vista
    List<DescriptionWeb> descriptionsWeb = new ArrayList<DescriptionWeb>();

    //Relaciones que son pasadas a la vista
    List<RelationshipWeb> relationshipsWeb = new ArrayList<RelationshipWeb>();


    boolean editable = false;


    //Este es el constructor mínimo
    public ConceptSMTKWeb(ConceptSMTK conceptSMTK) {

        // Crear un nuevo concepto con su información básica
        super(conceptSMTK.getConceptID(), conceptSMTK.getCategory(), conceptSMTK.isToBeReviewed(), conceptSMTK.isToBeConsulted(), conceptSMTK.isModeled(),
                conceptSMTK.isFullyDefined(), conceptSMTK.isInherited(), conceptSMTK.isPublished(), conceptSMTK.getObservation(), conceptSMTK.getTagSMTK());

        // Agregar descripciones y relaciones
        if(conceptSMTK.isPersistent()){
            this.setId(conceptSMTK.getId());
            // Si el concepto esta persistido clonar las descripciones con su id
            for (Description description : conceptSMTK.getValidDescriptions())
                addDescriptionWeb(new DescriptionWeb(this, description.getId(), description));
            // Si el concepto esta persistido clonar las relaciones con su id
            for (Relationship relationship : conceptSMTK.getValidRelationships())
                addRelationshipWeb(new RelationshipWeb(this, relationship.getId(), relationship, relationship.getRelationshipAttributes(), relationship.getCreationDate()));
            for (Tag tag: conceptSMTK.getTags()) {
                addTag(tag);
            }
        }
        else{
            // Si el concepto no esta persistido clonar las descripciones sin su id
            for (Description description : conceptSMTK.getValidDescriptions()) {
                addDescriptionWeb(new DescriptionWeb(this, description));
            }
        }
    }

    public List<DescriptionWeb> getDescriptionsWeb() {
        return descriptionsWeb;
    }

    public List<RelationshipWeb> getRelationshipsWeb() {
        return relationshipsWeb;
    }

    public void setDescriptionsWeb(List<DescriptionWeb> descriptionsWeb) {
        this.descriptionsWeb = descriptionsWeb;
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
    public DescriptionWeb getValidDescriptionFSN() {
        for (DescriptionWeb descriptionWeb : getDescriptionsWeb()) {
            DescriptionType descriptionType = descriptionWeb.getDescriptionType();
            if (descriptionType.getName().equalsIgnoreCase("FSN") && descriptionWeb.isValid()) {
                return descriptionWeb;
            }
        }
        /* En este punto, no se encontró una descripción preferida, y se arroja una excepción */
        throw new BusinessRuleException("BR-UNK", "Concepto sin descripción FSN");
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
    public DescriptionWeb getValidDescriptionFavorite() {
        for (DescriptionWeb descriptionWeb : getDescriptionsWeb()) {
            if (descriptionWeb.getDescriptionType().getName().equalsIgnoreCase("preferida") && descriptionWeb.isValid()) {
                return descriptionWeb;
            }
        }

        /* En este punto, no se encontró una descripción preferida, y se arroja una excepción */
        throw new BusinessRuleException("BR-UNK", "Concepto sin descripción preferida");
    }

    /**
     * <p>
     * Este método es responsable de retornar la <i>descripción abreviada</i>. Basados en la regla de negocio que dice
     * que un concepto debe siempre tener una y solo una descripción abreviada.</p>
     * <p>
     * Si el concepto no tuviera descripción preferida, se retorna una descripción "sin tipo".
     * </p>
     *
     * @return La descripción abreviada.
     */
    public DescriptionWeb getValidDescriptionAbbreviated() {
        for (DescriptionWeb descriptionWeb : getDescriptionsWeb()) {
            if (descriptionWeb.getDescriptionType().getName().equalsIgnoreCase("abreviada") && descriptionWeb.isValid()) {
                return descriptionWeb;
            }
        }
        return null;
    }

    /**
     * Este método restorna todas ls descripciones que no son FSN o Preferidas.
     *
     * @return Una lista con todas las descripciones no FSN o Preferidas.
     */
    public List<DescriptionWeb> getValidDescriptionsButFSNandFavorite() {

        List<DescriptionWeb> otherDescriptions = new ArrayList<DescriptionWeb>();
        DescriptionType fsnType = DescriptionTypeFactory.getInstance().getFSNDescriptionType();
        DescriptionType favoriteType = DescriptionTypeFactory.getInstance().getFavoriteDescriptionType();

        for (DescriptionWeb descriptionWeb : getDescriptionsWeb()) {
            if (!descriptionWeb.getDescriptionType().equals(fsnType) && !descriptionWeb.getDescriptionType().equals(favoriteType) && descriptionWeb.isValid()) {
                otherDescriptions.add(descriptionWeb);
            }
        }

        return otherDescriptions;
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


    @Override
    public String toString() {

        return super.toString();
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
    public List<RelationshipWeb> getValidRelationshipsWebByRelationDefinition(RelationshipDefinition relationshipDefinition) {
        List<RelationshipWeb> someRelationships = new ArrayList<RelationshipWeb>();
        for (RelationshipWeb relationship : relationshipsWeb) {
            if (relationship.getRelationshipDefinition().equals(relationshipDefinition) && relationship.isValid()) {
                someRelationships.add(relationship);
            }
        }
        Collections.sort(someRelationships);
        return someRelationships;
    }

    /**
     * Este método es responsable de retornar todas las relaciones válidas persistidas de este concepto
     *
     * @return Una <code>java.util.List</code> de relaciones persistidas
     */
    public List<RelationshipWeb> getValidPersistedRelationshipsWeb() {
        List<RelationshipWeb> someRelationships = new ArrayList<RelationshipWeb>();
        for (RelationshipWeb relationship : relationshipsWeb) {
            if (relationship.isPersistent() && relationship.isValid()) {
                someRelationships.add(relationship);
            }
        }
        return someRelationships;
    }

    public void setRelationshipsWeb(List<RelationshipWeb> relationships) {
        //super.setRelationships(relationships);
        for (Relationship relationship : this.getValidRelationships())
            this.relationshipsWeb.add(new RelationshipWeb(relationship.getId(), relationship));
    }

    /**
     * Este método es responsable de agregar una relación web al concepto.
     *
     * @param relationship La relación que es agregada.
     */
    public void addRelationshipWeb(RelationshipWeb relationship) {
        this.addRelationship(relationship);
        this.relationshipsWeb.add(relationship);
    }

    /**
     * Este método es responsable de agregar un tag al concepto.
     *
     * @param tag El tag que es agregado.
     */
    public void addTag(Tag tag) {
        super.addTag(tag);
    }


    /**
     * Este método es responsable de remover una descripción a un concepto.
     *
     * @param description La descripción que es removida.
     */
    public void removeDescriptionWeb(Description description) {
        this.getDescriptions().remove(description);
        this.descriptionsWeb.remove(new DescriptionWeb(description.getId(), description));
    }


    public void addDescriptionWeb(DescriptionWeb descriptionWeb) {
        this.addDescription(descriptionWeb);
        this.descriptionsWeb.add(descriptionWeb);
    }

    public void initRelationship(RelationshipDefinitionWeb relationshipDefinitionWeb){
        /*
        for (RelationshipWeb relationshipWeb : getValidRelationshipsWebByRelationDefinition(relationshipDefinitionWeb)) {
            relationshipWeb.setTarget(relationshipDefinitionWeb.getDefaultValue());
        }
        */
        Relationship r = new Relationship(this, relationshipDefinitionWeb.getDefaultValue(), relationshipDefinitionWeb, new ArrayList<RelationshipAttribute>(), null);

        for (RelationshipAttributeDefinitionWeb relAttrDefWeb : relationshipDefinitionWeb.getRelationshipAttributeDefinitionWebs()){
            r.getRelationshipAttributes().add(new RelationshipAttribute(relAttrDefWeb.getRelationshipAttributeDefinition(), r, relAttrDefWeb.getDefaultValue()));
        }

        addRelationshipWeb(new RelationshipWeb(this, r.getId(), r, r.getRelationshipAttributes()));
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * Este método es responsable de remover una relación a un concepto.
     *
     * @param relationship La relación que es removida.
     */
    public void removeRelationshipWeb(Relationship relationship) {
        this.getRelationships().remove(relationship);
        this.relationshipsWeb.remove(relationship);
    }


    //Los siguientes métodos están destinados a obtener información sobre las modificaciones que ha sufrido
    //el concepto, dada una imagen original del concepto

    /** Este método es responsable de obtener las descripciones que han sido removidas del concepto original
     * @param: Un <code>cl.minsal.semantikos.model.ConceptSMTKWeb</code> con la imagen original del concepto
     * @return Una <code>java.util.List</code> de descripciones removidas
     */
    public List<DescriptionWeb> getRemovedDescriptionsWeb(ConceptSMTKWeb _concept){

        List<DescriptionWeb> removedDescriptions = new ArrayList<DescriptionWeb>();
        boolean isDescriptionFound;

        //Primero se buscan todas las descripciones persistidas originales
        for (DescriptionWeb initDescription : _concept.getDescriptionsWeb()) {
            isDescriptionFound = false;
            //Por cada descripción original se busca su descripcion vista correlacionada
            for (DescriptionWeb finalDescription : this.getDescriptionsWeb()) {
                //Si la descripcion correlacionada no es encontrada, significa que fué eliminada
                if (initDescription.getId() == finalDescription.getId()) {
                    isDescriptionFound = true;
                    break;
                }
            }
            if(!isDescriptionFound)
                removedDescriptions.add(initDescription);
        }
        return  removedDescriptions;
    }

    /** Este método es responsable de obtener las descripciones que han sido modificadas respecto al concepto original
    * @param: Un <code>cl.minsal.semantikos.model.ConceptSMTKWeb</code> con la imagen original del concepto
    * @return Una <code>java.util.List</code> de <code>cl.minsal.semantikos.util.Pair</code> de descripciones modificadas
     */
    public List<Pair<DescriptionWeb, DescriptionWeb>> getModifiedDescriptionsWeb(ConceptSMTKWeb _concept) {

        List<Pair<DescriptionWeb, DescriptionWeb>> descriptionsForUpdate = new ArrayList<Pair<DescriptionWeb, DescriptionWeb>>();

        //Primero se buscan todas las descripciones persistidas originales
        for (DescriptionWeb initDescription : _concept.getDescriptionsWeb()) {
            //Por cada descripción original se busca su descripcion vista correlacionada
            for (DescriptionWeb finalDescription : this.getDescriptionsWeb()) {
                //Si la descripcion correlacionada sufrio alguna modificación agregar el par (init, final)
                if (initDescription.getId() == finalDescription.getId() && !finalDescription.equals(initDescription) /*finalDescription.hasBeenModified()*/) {
                    descriptionsForUpdate.add(new Pair(initDescription, finalDescription));
                }
            }
        }
        return descriptionsForUpdate;
    }

    /**
     * Este método es responsable de retornar todas las descripciones no persistidas de este concepto
     *
     * @return Una <code>java.util.List</code> de descripciones no persistidas
     */
    public List<DescriptionWeb> getUnpersistedDescriptions() {
        List<DescriptionWeb> someDescriptions = new ArrayList<DescriptionWeb>();
        for (DescriptionWeb description : this.getDescriptionsWeb()) {
            if (!description.isPersistent()) {
                someDescriptions.add(description);
            }
        }
        return someDescriptions;
    }

    /** Este método es responsable de obtener las relaciones que han sido removidas del concepto original
    *  @param: Un <code>cl.minsal.semantikos.model.ConceptSMTKWeb</code> con la imagen original del concepto
    *  @return Una <code>java.util.List</code> de relaciones removidas
     */
    public List<RelationshipWeb> getRemovedRelationshipsWeb(ConceptSMTKWeb _concept){

        List<RelationshipWeb> removedRelationships = new ArrayList<RelationshipWeb>();
        boolean isRelationshipFound;

        //Primero se buscan todas las relaciones persistidas originales
        for (RelationshipWeb initRelationship : _concept.getRelationshipsWeb()) {
            isRelationshipFound = false;
            //Por cada descripción original se busca su descripcion vista correlacionada
            for (RelationshipWeb finalRelationship : this.getRelationshipsWeb()) {
                //Si la descripcion correlacionada no es encontrada, significa que fué eliminada
                if (initRelationship.getId() == finalRelationship.getId()) {
                    isRelationshipFound = true;
                    break;
                }
            }
            if(!isRelationshipFound)
                removedRelationships.add(initRelationship);
        }
        return removedRelationships;
    }

    /** Este método es responsable de obtener las descripciones que han sido modificadas respecto al concepto original
     * @param: Un <code>cl.minsal.semantikos.model.ConceptSMTKWeb</code> con la imagen original del concepto
     * @return Una <code>java.util.List</code> de <code>cl.minsal.semantikos.util.Pair</code> de relaciones modificadas
     */
    public List<Pair<RelationshipWeb, RelationshipWeb>> getModifiedRelationships(ConceptSMTKWeb _concept) {

        List<Pair<RelationshipWeb, RelationshipWeb>> relationshipsForUpdate = new ArrayList<Pair<RelationshipWeb, RelationshipWeb>>();

        //Primero se buscan todas las descripciones persistidas originales
        for (RelationshipWeb initRelationship : _concept.getRelationshipsWeb()) {
            //Por cada descripción original se busca su descripcion vista correlacionada
            for (RelationshipWeb finalRelationship : this.getRelationshipsWeb()) {
                //Si la descripcion correlacionada sufrio alguna modificación agregar el par (init, final)
                if (initRelationship.getId() == finalRelationship.getId() && !finalRelationship.equals(initRelationship) /*finalDescription.hasBeenModified()*/) {
                    relationshipsForUpdate.add(new Pair(initRelationship, finalRelationship));
                }
            }
        }
        return relationshipsForUpdate;
    }


    /**
     * Este método es responsable de retornar todas las relaciones no persistidas de este concepto
     *
     * @return Una <code>java.util.List</code> de relaciones persistidas
     */
    public List<RelationshipWeb> getUnpersistedRelationshipsWeb() {
        List<RelationshipWeb> someRelationships = new ArrayList<RelationshipWeb>();
        for (RelationshipWeb relationship : relationshipsWeb) {
            if (!relationship.isPersistent()) {
                someRelationships.add(relationship);
            }
        }
        return someRelationships;
    }

    // Los siguientes métodos son de utilidad en la restauración del concepto a su estado original
    public void removeUnpersistedDescriptions(){

        Iterator<Description> it = getDescriptions().iterator();

        while (it.hasNext()) {
            Description description = it.next(); // must be called before you can call i.remove()
            if(!description.isPersistent() && !description.getDescriptionType().getName().equalsIgnoreCase("FSN") && !description.getDescriptionType().getName().equalsIgnoreCase("Preferida"))
                it.remove();
        }

        Iterator<DescriptionWeb> it2 = descriptionsWeb.iterator();

        while (it2.hasNext()) {
            DescriptionWeb descriptionWeb = it2.next(); // must be called before you can call i.remove()
            if(!descriptionWeb.isPersistent() && !descriptionWeb.getDescriptionType().getName().equalsIgnoreCase("FSN") && !descriptionWeb.getDescriptionType().getName().equalsIgnoreCase("Preferida"))
                it2.remove();
        }

    }

    public void restore(ConceptSMTKWeb _concept){

        super.setToBeReviewed(_concept.isToBeReviewed());
        super.setToBeConsulted(_concept.isToBeConsulted());
        super.setTags(_concept.getTags());
        super.setTagSMTK(_concept.getTagSMTK());
        super.setObservation("");

        //Remover descripciones agregadas no persistidas
        removeUnpersistedDescriptions();

        //Restaurar descripciones a su estado original
        for (DescriptionWeb descriptionWeb : descriptionsWeb) {
            for (DescriptionWeb _descriptionWeb : _concept.getDescriptionsWeb()) {
                if(descriptionWeb.getId()==_descriptionWeb.getId() && descriptionWeb.isPersistent() )
                    descriptionWeb.restore(_descriptionWeb);
            }
        }

        //Restaurar descripciones removidas
        for (DescriptionWeb descriptionWeb : getRemovedDescriptionsWeb(_concept)) {
            addDescriptionWeb(descriptionWeb);
        }

        //Remover relaciones
        this.getRelationships().clear();
        this.relationshipsWeb.clear();

        //Restaurar relaciones a su estado original
        for (RelationshipWeb relationshipWeb : _concept.getRelationshipsWeb()) {
            this.addRelationshipWeb(relationshipWeb);
        }

        //TODO: Hacer lo mismo para las relaciones y presumiblemente tambien para los tags
    }

    /**
     * Este método es responsable de obtener el atributo orden, dada la definicion de relacion y el orden en cuestion
     * @param order
     * @return
     */
    public RelationshipAttribute getAttributeOrder(RelationshipDefinition relationshipDefinition, int order){

        for (RelationshipWeb relationshipWeb : getValidRelationshipsWebByRelationDefinition(relationshipDefinition)) {
            if(relationshipWeb.getRelationshipDefinition().getOrderAttributeDefinition() != null){
                for (RelationshipAttribute attribute : relationshipWeb.getRelationshipAttributes()) {
                    if(attribute.getRelationAttributeDefinition().isOrderAttribute()){
                        BasicTypeValue basicTypeValue = (BasicTypeValue) attribute.getTarget();
                        if(basicTypeValue.getValue().equals(new Integer(order)))
                            return attribute;
                    }
                }
            }
        }
        return null;
    }

    public RelationshipAttribute getAttributeOrderNoWeb(RelationshipDefinition relationshipDefinition, int order){

        for (Relationship relationship : getValidRelationshipsByRelationDefinition(relationshipDefinition)) {
            if(relationship.getRelationshipDefinition().getOrderAttributeDefinition() != null){
                for (RelationshipAttribute attribute : relationship.getRelationshipAttributes()) {
                    if(attribute.getRelationAttributeDefinition().isOrderAttribute()){
                        BasicTypeValue basicTypeValue = (BasicTypeValue) attribute.getTarget();
                        if(basicTypeValue.getValue().equals(new Integer(order)))
                            return attribute;
                    }
                }
            }
        }
        return null;
    }

    public boolean isMultiplicitySatisfied(RelationshipDefinition relationshipDefinition){
        for (RelationshipWeb relationshipWeb : getValidRelationshipsWebByRelationDefinition(relationshipDefinition)) {
            if(relationshipDefinition.getTargetDefinition().isSMTKType()){
                if(relationshipWeb.getTarget()==null)
                    return false;
            }
            if(relationshipDefinition.getTargetDefinition().isBasicType()){
                BasicTypeValue basicTypeValue = (BasicTypeValue)relationshipWeb.getTarget();
                if(basicTypeValue.getValue()==null)
                    return false;
                if(basicTypeValue.getValue().equals(""))
                    return false;
            }
            for (RelationshipAttributeDefinition relationshipAttributeDefinition: relationshipDefinition.getRelationshipAttributeDefinitions()) {
                if(relationshipWeb.getAttributesByAttributeDefinition(relationshipAttributeDefinition).size()<relationshipAttributeDefinition.getMultiplicity().getLowerBoundary()) return false;
            }

        }

        return this.getValidRelationshipsWebByRelationDefinition(relationshipDefinition).size()>=relationshipDefinition.getMultiplicity().getLowerBoundary();
    }


    public ConceptSMTK getConceptSMTK() {
        ConceptSMTK conceptSMTK =  new ConceptSMTK(this.getId(), this.getConceptID(), this.getCategory(), this.isToBeReviewed(), this.isToBeConsulted(), this.isModeled(), this.isFullyDefined(), this.isInherited(), this.isPublished(), this.getObservation(), this.getTagSMTK());
        conceptSMTK.setDescriptions(super.getDescriptions());
        conceptSMTK.setRelationships(super.getRelationships());
        conceptSMTK.setRefsets(this.getRefsets());
        conceptSMTK.setTags(this.getTags());
        return conceptSMTK;
    }

}