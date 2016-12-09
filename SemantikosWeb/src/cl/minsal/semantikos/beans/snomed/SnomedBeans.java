package cl.minsal.semantikos.beans.snomed;

import cl.minsal.semantikos.beans.messages.MessageBean;
import cl.minsal.semantikos.model.ConceptSMTKWeb;
import cl.minsal.semantikos.model.businessrules.ConceptDefinitionalGradeBRInterface;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 * Created by des01c7 on 02-12-16.
 */
@ManagedBean( name = "snomedBean")
@ViewScoped
public class SnomedBeans {

    private static final long ID_RELATIONSHIP_DEFINITION_SNOMED_CT = 101;
    private static final long ID_RELATIONSHIP_ATTRIBUTE_DEFINITION_TYPE_RELTIONSHIP_SNOMED_CT = 25;
    private static final long ID_TYPE_IS_MAPPING = 2;

    @ManagedProperty( value="#{messageBean}")
    MessageBean messageBean;

    public MessageBean getMessageBean() {
        return messageBean;
    }

    public void setMessageBean(MessageBean messageBean) {
        this.messageBean = messageBean;
    }

    @EJB
    private ConceptDefinitionalGradeBRInterface conceptDefinitionalGradeBR;


    /**
     * Metodo encargado de validar si la relacion que recibe por parametro es de tipo Es un Mapeo.
     *
     * @param relationship relacion a validar.
     * @return retorna true o false segun corresponda.
     */
    public boolean isMapping(Relationship relationship) {
        if (relationship.getRelationshipDefinition().getId() == ID_RELATIONSHIP_DEFINITION_SNOMED_CT) {
            for (RelationshipAttribute relationshipAttribute : relationship.getRelationshipAttributes()) {
                if (relationshipAttribute.getRelationAttributeDefinition().getId() == ID_RELATIONSHIP_ATTRIBUTE_DEFINITION_TYPE_RELTIONSHIP_SNOMED_CT) {
                    HelperTableRecord typeRelationship = (HelperTableRecord) relationshipAttribute.getTarget();
                    if (typeRelationship.getId() == ID_TYPE_IS_MAPPING) return true;
                }
            }
        }
        return false;
    }

    /**
     * Metodo encargado de ver si existe una relacion Es un mapeo en el concepto que se esta creando o editando.
     *
     * @return retorna true o false segun corresponda.
     */
    public boolean existRelationshipISAMapping(ConceptSMTKWeb concept) {

        for (Relationship relationship : concept.getRelationshipsWeb()) {
            boolean isAMapping= isMapping(relationship);
            if(isAMapping){
                return true;
            }
        }
        return false;
    }

    public boolean existRelationshipToSCT(ConceptSMTKWeb concept) {
        for (Relationship relationship : concept.getRelationshipsWeb()) {
            if (!relationship.getRelationshipDefinition().getTargetDefinition().isCrossMapType() && (relationship.getRelationshipDefinition().getId() == ID_RELATIONSHIP_DEFINITION_SNOMED_CT)) {
                return true;
            }
        }
        return false;
    }


}
