package cl.minsal.semantikos.beans.concept;

import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.model.ConceptSMTK;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * Created by des01c7 on 02-02-17.
 */
@ManagedBean(name = "conceptSpecialBean")
@ViewScoped
public class ConceptSpecialBean {

    private ConceptSMTK conceptSMTKPending;
    private ConceptSMTK conceptSMTKNotValid;

    @EJB
    private ConceptManager conceptManager;

    @PostConstruct
    public void init(){
        conceptSMTKNotValid= conceptManager.getNoValidConcept();
        conceptSMTKPending= conceptManager.getPendingConcept();
    }

    public ConceptSMTK getConceptSMTKPending() {
        return conceptSMTKPending;
    }

    public void setConceptSMTKPending(ConceptSMTK conceptSMTKPending) {
        this.conceptSMTKPending = conceptSMTKPending;
    }

    public ConceptSMTK getConceptSMTKNotValid() {
        return conceptSMTKNotValid;
    }

    public void setConceptSMTKNotValid(ConceptSMTK conceptSMTKNotValid) {
        this.conceptSMTKNotValid = conceptSMTKNotValid;
    }

    public boolean isSpecial(ConceptSMTK conceptSMTK){
        if(conceptSMTK.getId()== conceptSMTKNotValid.getId()){
            return true;
        }
        if(conceptSMTK.getId()== conceptSMTKPending.getId()){
            return true;
        }
        return false;
    }

    public ConceptManager getConceptManager() {
        return conceptManager;
    }

    public void setConceptManager(ConceptManager conceptManager) {
        this.conceptManager = conceptManager;
    }
}
