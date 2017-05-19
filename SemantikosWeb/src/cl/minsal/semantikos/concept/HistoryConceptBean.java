package cl.minsal.semantikos.concept;

import cl.minsal.semantikos.clients.RemoteEJBClientFactory;
import cl.minsal.semantikos.kernel.components.AuditManager;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.audit.ConceptAuditAction;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

/**
 * Created by des01c7 on 24-08-16.
 */

@ManagedBean(name = "historyConcept")
@ViewScoped
public class HistoryConceptBean {

    //@EJB
    AuditManager auditManager = (AuditManager) RemoteEJBClientFactory.getInstance().getManager(AuditManager.class);

    //@EJB
    ConceptManager conceptManager = (ConceptManager) RemoteEJBClientFactory.getInstance().getManager(ConceptManager.class);

    private List<ConceptAuditAction> auditAction;

    private ConceptSMTK conceptSMTK;

    @PostConstruct
    public void init(){
        conceptSMTK=conceptManager.getConceptByID(80614);
        auditAction=auditManager.getConceptAuditActions(conceptSMTK,true);
    }

    public List<ConceptAuditAction> getAuditAction() {
        return auditAction;
    }

    public void setAuditAction(List<ConceptAuditAction> auditAction) {
        this.auditAction = auditAction;
    }

    public ConceptSMTK getConceptSMTK() {
        return conceptSMTK;
    }

    public void setConceptSMTK(ConceptSMTK conceptSMTK) {
        this.conceptSMTK = conceptSMTK;
    }
}
