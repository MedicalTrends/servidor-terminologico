package cl.minsal.semantikos.beans.concept;

import cl.minsal.semantikos.designer_modeler.auth.AuthenticationBean;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.RelationshipManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.Target;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by des01c7 on 09-11-16.
 */

@ManagedBean(name = "changeMarketedBean")
@ViewScoped
public class ChangeMarketedBean {

    List<ConceptSMTK> conceptSMTKList;
    List<ConceptSMTK> conceptSelected;

    @EJB
    private ConceptManager conceptManager;

    @EJB
    private RelationshipManager relationshipManager;

    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    private static long ID_MARKETED=8;

    private User user;

    private Target targetSelected;

    @PostConstruct
    public void init() {
        conceptSMTKList = new ArrayList<>();
        user = authenticationBean.getLoggedUser();
    }

    public void changeMarketedEvent(ConceptSMTK conceptSMTK, RelationshipDefinition relationshipDefinition, Target target) {
        if(relationshipDefinition.getId()==ID_MARKETED && conceptSMTK.isModeled()){
            targetSelected=target;
            conceptSMTKList = conceptManager.getRelatedConcepts(conceptSMTK);
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('conceptMarketed').show();");
        }
    }


    public void changeMarketed(){
        Relationship lateastRelationship;

        for (ConceptSMTK concept: conceptSelected) {
            concept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(concept));
            for (Relationship relationship: concept.getRelationships()) {
                if(relationship.getRelationshipDefinition().getId()==ID_MARKETED){
                    BasicTypeValue basicTypeValue = (BasicTypeValue)targetSelected;
                    lateastRelationship = new Relationship(concept,new BasicTypeValue(basicTypeValue.getValue()),relationship.getRelationshipDefinition(), new ArrayList<RelationshipAttribute>(), null);
                    lateastRelationship.setCreationDate(relationship.getCreationDate());
                    lateastRelationship.setId(relationship.getId());
                    relationshipManager.updateRelationship(concept,relationship,lateastRelationship,user);
                    break;
                }
            }
        }
    }

    public void showMessage() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Advertencia", "Los cambios serán efectivos al guardar el concepto"));
    }


    public List<ConceptSMTK> getConceptSMTKList() {
        return conceptSMTKList;
    }

    public void setConceptSMTKList(List<ConceptSMTK> conceptSMTKList) {
        this.conceptSMTKList = conceptSMTKList;
    }

    public List<ConceptSMTK> getConceptSelected() {
        return conceptSelected;
    }

    public void setConceptSelected(List<ConceptSMTK> conceptSelected) {
        this.conceptSelected = conceptSelected;
    }

    public ConceptManager getConceptManager() {
        return conceptManager;
    }

    public void setConceptManager(ConceptManager conceptManager) {
        this.conceptManager = conceptManager;
    }

    public AuthenticationBean getAuthenticationBean() {
        return authenticationBean;
    }

    public void setAuthenticationBean(AuthenticationBean authenticationBean) {
        this.authenticationBean = authenticationBean;
    }
}
