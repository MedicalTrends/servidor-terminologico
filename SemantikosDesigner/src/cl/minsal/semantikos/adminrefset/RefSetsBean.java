package cl.minsal.semantikos.adminrefset;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.institutions.InstitutionsBean;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.messages.MessageBean;
import cl.minsal.semantikos.users.AuthenticationBean;
import cl.minsal.semantikos.concept.ConceptBean;

import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.audit.AuditAction;
import cl.minsal.semantikos.model.audit.ConceptAuditAction;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cl.minsal.semantikos.model.audit.AuditActionType.REFSET_UPDATE;
import static java.lang.System.currentTimeMillis;

/**
 * @author Gustavo Punucura on 20-09-16.
 */
@ManagedBean(name = "refsetsBean")
@ViewScoped
public class RefSetsBean implements Serializable {

    static private final Logger logger = LoggerFactory.getLogger(InstitutionsBean.class);

    private RefSet refSetToCreate;

    private List<RefSet> refSetList;

    private List<RefSet> conceptRefSetList;

    private List<Category> categories;

    private Category categorySelected;

    private LazyDataModel<ConceptSMTK> conceptsToCategory;

    private LazyDataModel<ConceptSMTK> conceptsToDescription;

    private String pattern;

    private RefSet refSetEdit;

    private RefSet refSetSelect;

    private List<RefSet> refSetListInstitution;

    private Institution institutionSelected;

    private Map<Long, AuditAction> refsetHistoryConcept;

    private Map<Long, AuditAction> conceptBindToRefsetHistory;

    private ConceptSMTK conceptSMTK;

    private List<RefSet> refsetFilter;

    private List<ConceptSMTK> conceptSMTKListSelected;

    private List<ConceptSMTK> conceptSMTKListSelectedEdit;

    @ManagedProperty(value = "#{authenticationBean}")
    private transient AuthenticationBean authenticationBean;

    @ManagedProperty(value = "#{conceptBean}")
    private ConceptBean conceptBean;

    @ManagedProperty(value = "#{messageBean}")
    private transient MessageBean messageBean;

    //@EJB
    AuditManager auditManager = (AuditManager) ServiceLocator.getInstance().getService(AuditManager.class);

    //@EJB
    CategoryManager categoryManager = (CategoryManager) ServiceLocator.getInstance().getService(CategoryManager.class);

    //@EJB
    ConceptManager conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);

    //@EJB
    RefSetManager refSetManager = (RefSetManager) ServiceLocator.getInstance().getService(RefSetManager.class);




    @PostConstruct
    public void init() {

        categories = categoryManager.getCategories();
        refSetList = refSetManager.getAllRefSets();
        /**
         * solo administrarán los refset de su establecimiento/institución.
         */
        refSetList = refSetManager.getRefsetByUser(authenticationBean.getLoggedUser());
        refsetHistoryConcept = new HashMap<>();
        conceptBindToRefsetHistory = new HashMap<>();
        selectInstitutionMINSAL();
        refSetListInstitution = refSetManager.getRefsetByInstitution((institutionSelected == null) ? new Institution() : institutionSelected);
        refSetToCreate = new RefSet(null, new Institution(), null);

    }

    /**
     * Método encargado de obtener Refset según su institución
     */
    public void reloadRefsetByInstitution() {
        refSetListInstitution = refSetManager.getRefsetByInstitution((institutionSelected == null) ? new Institution() : institutionSelected);

    }

    /**
     * Método encargado de ver si el usuario posee la institución MINSAL en su perfil
     */
    public void selectInstitutionMINSAL() {
        for (Institution institution : authenticationBean.getLoggedUser().getInstitutions()) {
            if (institution.getName().equals("MINSAL")) {
                institutionSelected = institution;
                break;
            }

        }
    }

    /**
     *
     */
    public void createRefset() {

        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (refSetToCreate.getInstitution() != null && refSetToCreate.getName().length() > 0) {
            if(!existRefSetsEqualsInstitution(refSetToCreate)) {

                try {
                    refSetToCreate = refSetManager.createRefSet(refSetToCreate, authenticationBean.getLoggedUser());
                }
                catch (EJBException e) {
                    logger.error("error al eliminar establecimiento",e);
                    facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
                }
                refSetToCreate = new RefSet(null, new Institution(), null);
                conceptsToCategory = null;
                conceptsToDescription = null;
                refSetList = refSetManager.getAllRefSets();
                messageBean.messageSuccess("Éxito", "El RefSet ha sido guardado exitosamente.");
            } else {
                messageBean.messageError("No se pueden crear 2 RefSets con el mismo nombre y en la misma institución");
            }

        } else {
            messageBean.messageError("Falta información para crear el RefSet");
        }

    }

    public boolean existRefSetsEqualsInstitution(RefSet refSet){
        for (RefSet set : refSetList) {
            if(set.getName().equals(refSet.getName()) && set.getInstitution().getName().equals(refSet.getInstitution().getName())){
                return true;
            }
        }
        return false;
    }


    /**
     * Método encargado de invalidar el RefSet seleccionado por el usuario
     */
    public void invalidRefset(RefSet refSetSelected) {
        refSetSelected.setValidityUntil(new Timestamp(currentTimeMillis()));
        refSetManager.updateRefSet(refSetSelected, authenticationBean.getLoggedUser());
        refSetList = refSetManager.getAllRefSets();
    }

    /**
     * Método encargado de validate el RefSet seleccionado por el usuario
     */
    public void validRefset(RefSet refSetSelected) {
        try {
            refSetSelected.setValidityUntil(null);
            refSetManager.updateRefSet(refSetSelected, authenticationBean.getLoggedUser());
            refSetList = refSetManager.getAllRefSets();
        }
        catch (EJBException e) {
            messageBean.messageError(e.getMessage());
        }
    }

    /**
     * Método encargado de agregar conceptos a un RefSet
     *
     * @param refSet      refset que almacenara el concepto
     * @param conceptSMTK Concepto seleccionado para vincularse a un Refset
     */
    public void addConcept(RefSet refSet, ConceptSMTK conceptSMTK) {
        if(refSet==null){
            messageBean.messageError("Debe seleccionar un RefSet");
           return;
        }
        if (refSet.isPersistent()) {
            if(!existConceptRefSet(refSet,conceptSMTK)){
                refSet.bindConceptTo(conceptSMTK);
                refSetManager.bindConceptToRefSet(conceptSMTK, refSet, authenticationBean.getLoggedUser());
            }else{
                messageBean.messageError("No se pueden repetir los conceptos en el mismo RefSet");
            }

        } else {
            if (conceptSMTK != null) {
                refSet.bindConceptTo(conceptSMTK);
                this.conceptSMTK = null;
            } else {
                messageBean.messageError("Debe seleccionar un concepto");
            }
        }
        if (conceptRefSetList != null) {
            conceptRefSetList = refSetManager.getRefsetsBy(conceptBean.getConcept());
            conceptBean.setRefsetEditConcept(true);
        }
    }

    private boolean existConceptRefSet(RefSet refSet, ConceptSMTK conceptSMTK){
        for (ConceptSMTK smtk : refSet.getConcepts()) {
            if(smtk.equals(conceptSMTK)){
                return true;
            }
        }


        return false;
    }


    /**
     * Método encargado de agregar conceptos a un RefSet
     *
     * @param refSet      refset que almacenara el concepto
     */
    public void addConcept(RefSet refSet) {
        ConceptSMTK conceptNoValid = conceptManager.getNoValidConcept();
        ConceptSMTK conceptPending = conceptManager.getPendingConcept();
        if(!conceptSMTKListSelectedEdit.isEmpty()){
            for (ConceptSMTK smtk : conceptSMTKListSelectedEdit) {
                if(smtk.equals(conceptNoValid) || smtk.equals(conceptPending)){
                    messageBean.messageError("El concepto no válido o pendiente, no se puede agregar a un RefSets");
                    return;
                }
            }
            for (ConceptSMTK smtk : conceptSMTKListSelectedEdit) {
                addConcept(refSet,smtk);
            }
        }
        if(!conceptSMTKListSelected.isEmpty()){
            for (ConceptSMTK smtk : conceptSMTKListSelected) {
                if(smtk.equals(conceptNoValid) || smtk.equals(conceptPending)){
                    messageBean.messageError("El concepto no válido o pendiente, no se puede agregar a un RefSets");
                    return;
                }
            }
            for (ConceptSMTK smtk : conceptSMTKListSelected) {
                addConcept(refSet,smtk);
            }
        }
    }

    /**
     * Método encargado de eliminar un Concepto que se encuentra en un RefSet
     *
     * @param refSet
     * @param conceptSMTK
     */

    public void removeConcept(RefSet refSet, ConceptSMTK conceptSMTK) {
        refSet.unbindConceptTo(conceptSMTK);
        if (refSet.isPersistent()) {
            refSetManager.unbindConceptToRefSet(conceptSMTK, refSet, authenticationBean.getLoggedUser());
        }
        if (conceptRefSetList != null) {
            conceptRefSetList = refSetManager.getRefsetsBy(conceptBean.getConcept());
            conceptBean.setRefsetEditConcept(true);
        }
    }

    /**
     * Método encargado de cargar el historial del concepto de acuerdo al RefSet
     */

    public void loadHistoryConcept() {
        List<ConceptAuditAction> auditActions = auditManager.getConceptAuditActions(conceptBean.getConcept(), false);

        for (RefSet refset : conceptRefSetList) {
            for (ConceptAuditAction conceptAuditAction : auditActions) {
                if (conceptAuditAction.getAuditActionType().getId() == REFSET_UPDATE.getId()) {
                    if (conceptAuditAction.getAuditableEntity().getId() == refset.getId()) {
                        refsetHistoryConcept.put(refset.getId(), conceptAuditAction);
                    }
                }

            }
        }
    }

    /**
     * Método encargado de obtener el ingreso de los conceptos al RefSet
     *
     * @param refsetConsult
     */
    public void loadHistoryRefset(RefSet refsetConsult) {

        for (ConceptSMTK conceptSMTK : refsetConsult.getConcepts()) {
            List<ConceptAuditAction> auditActions = auditManager.getConceptAuditActions(conceptSMTK, false);

            for (ConceptAuditAction conceptAuditAction : auditActions) {
                if (conceptAuditAction.getAuditActionType().getId() == REFSET_UPDATE.getId()) {
                    if (conceptAuditAction.getAuditableEntity().getId() == refsetConsult.getId()) {
                        conceptBindToRefsetHistory.put(conceptSMTK.getId(), conceptAuditAction);
                    }
                }
            }
        }

    }


    public RefSet getRefSetToCreate() {
        return refSetToCreate;
    }

    public void setRefSetToCreate(RefSet refSetToCreate) {
        this.refSetToCreate = refSetToCreate;
    }

    public List<RefSet> getRefSetList() {
        return refSetList;
    }

    public void setRefSetList(List<RefSet> refSetList) {
        this.refSetList = refSetList;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Category getCategorySelected() {
        return categorySelected;
    }

    public void setCategorySelected(Category categorySelected) {
        this.categorySelected = categorySelected;
    }


    public CategoryManager getCategoryManager() {
        return categoryManager;
    }

    public LazyDataModel<ConceptSMTK> getConceptsToCategory() {
        return conceptsToCategory;
    }

    public void setConceptsToCategory(LazyDataModel<ConceptSMTK> conceptsToCategory) {
        this.conceptsToCategory = conceptsToCategory;
    }

    public LazyDataModel<ConceptSMTK> getConceptsToDescription() {
        return conceptsToDescription;
    }

    public void setConceptsToDescription(LazyDataModel<ConceptSMTK> conceptsToDescription) {
        this.conceptsToDescription = conceptsToDescription;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }


    public RefSet getRefSetEdit() {
        return refSetEdit;
    }

    public void setRefSetEdit(RefSet refSetEdit) {
        this.refSetEdit = refSetEdit;
    }

    public AuthenticationBean getAuthenticationBean() {
        return authenticationBean;
    }

    public void setAuthenticationBean(AuthenticationBean authenticationBean) {
        this.authenticationBean = authenticationBean;
    }

    public List<RefSet> getConceptRefSetList() {
        conceptRefSetList = refSetManager.getRefsetsBy(conceptBean.getConcept());
        loadHistoryConcept();
        return conceptRefSetList;
    }

    public void setConceptRefSetList(List<RefSet> conceptRefSetList) {
        this.conceptRefSetList = conceptRefSetList;
    }

    public ConceptBean getConceptBean() {
        return conceptBean;
    }

    public void setConceptBean(ConceptBean conceptBean) {
        this.conceptBean = conceptBean;
    }

    public RefSet getRefSetSelect() {
        return refSetSelect;
    }

    public void setRefSetSelect(RefSet refSetSelect) {
        this.refSetSelect = refSetSelect;
    }

    public Map<Long, AuditAction> getRefsetHistoryConcept() {
        return refsetHistoryConcept;
    }

    public void setRefsetHistoryConcept(Map<Long, AuditAction> refsetHistoryConcept) {
        this.refsetHistoryConcept = refsetHistoryConcept;
    }

    public List<RefSet> getRefSetListInstitution() {
        return refSetListInstitution;
    }

    public void setRefSetListInstitution(List<RefSet> refSetListInstitution) {
        this.refSetListInstitution = refSetListInstitution;
    }

    public Institution getInstitutionSelected() {
        return institutionSelected;
    }

    public void setInstitutionSelected(Institution institutionSelected) {
        this.institutionSelected = institutionSelected;
    }

    public Map<Long, AuditAction> getConceptBindToRefsetHistory() {
        return conceptBindToRefsetHistory;
    }

    public void setConceptBindToRefsetHistory(Map<Long, AuditAction> conceptBindToRefsetHistory) {
        this.conceptBindToRefsetHistory = conceptBindToRefsetHistory;
    }

    public ConceptSMTK getConceptSMTK() {
        return conceptSMTK;
    }

    public void setConceptSMTK(ConceptSMTK conceptSMTK) {
        this.conceptSMTK = conceptSMTK;
    }

    public void setMessageBean(MessageBean messageBean) {
        this.messageBean = messageBean;
    }

    public List<RefSet> getRefsetFilter() {
        return refsetFilter;
    }

    public void setRefsetFilter(List<RefSet> refsetFilter) {
        this.refsetFilter = refsetFilter;
    }

    public List<ConceptSMTK> getConceptSMTKListSelected() {
        return conceptSMTKListSelected;
    }

    public void setConceptSMTKListSelected(List<ConceptSMTK> conceptSMTKListSelected) {
        this.conceptSMTKListSelected = conceptSMTKListSelected;
    }

    public List<ConceptSMTK> getConceptSMTKListSelectedEdit() {
        return conceptSMTKListSelectedEdit;
    }

    public void setConceptSMTKListSelectedEdit(List<ConceptSMTK> conceptSMTKListSelectedEdit) {
        this.conceptSMTKListSelectedEdit = conceptSMTKListSelectedEdit;
    }
}
