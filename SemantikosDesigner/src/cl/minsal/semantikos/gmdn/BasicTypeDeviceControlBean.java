package cl.minsal.semantikos.gmdn;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.concept.ConceptBean;
import cl.minsal.semantikos.concept.SMTKTypeBean;
import cl.minsal.semantikos.kernel.components.GmdnManager;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.gmdn.CollectiveTerm;
import cl.minsal.semantikos.model.gmdn.DeviceType;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.modelweb.RelationshipDefinitionWeb;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gustavo Punucura
 * @created 26-07-16.
 */

@ManagedBean(name = "deviceControlBean")
@ViewScoped
public class BasicTypeDeviceControlBean implements Serializable {

    @ManagedProperty(value = "#{conceptBean}")
    private ConceptBean conceptBean;

    //@EJB
    /**
     * Constructor por defecto para la inicialización de componentes.
     */
    public BasicTypeDeviceControlBean() {

    }

    public ConceptBean getConceptBean() {
        return conceptBean;
    }

    public void setConceptBean(ConceptBean conceptBean) {
        this.conceptBean = conceptBean;
    }

    public void updateRelationship(Relationship relationship) {

        BasicTypeValue defaultBasicTypeValue = new BasicTypeValue(false);

        for (RelationshipAttribute attribute : relationship.getRelationshipAttributes()) {
            if (attribute.getRelationAttributeDefinition().getTargetDefinition().isBasicType()) {
                BasicTypeValue basicTypeValue = (BasicTypeValue) attribute.getTarget();
                if(basicTypeValue.getValue().equals(true)) {
                    relationship.setTarget(basicTypeValue);
                    relationship.getRelationshipDefinition().getMultiplicity().setLowerBoundary(1);
                    for (RelationshipDefinitionWeb relationshipDefinitionWeb : conceptBean.getOrderedRelationshipDefinitions()) {
                        if(relationshipDefinitionWeb.getId() == relationship.getRelationshipDefinition().getId()) {
                            relationshipDefinitionWeb.getMultiplicity().setLowerBoundary(1);
                        }
                    }
                    return;
                }
            }
        }

        relationship.setTarget(defaultBasicTypeValue);

        relationship.getRelationshipDefinition().getMultiplicity().setLowerBoundary(2);

        for (RelationshipDefinitionWeb relationshipDefinitionWeb : conceptBean.getOrderedRelationshipDefinitions()) {
            if(relationshipDefinitionWeb.getId() == relationship.getRelationshipDefinition().getId()) {
                relationshipDefinitionWeb.getMultiplicity().setLowerBoundary(2);
            }
        }
    }
}
