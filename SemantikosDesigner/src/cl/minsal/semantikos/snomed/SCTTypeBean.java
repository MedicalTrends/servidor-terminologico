package cl.minsal.semantikos.snomed;

import cl.minsal.semantikos.clients.RemoteEJBClientFactory;
import cl.minsal.semantikos.kernel.components.SnomedCTManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCTType;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Ajax;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;

import static java.util.Collections.emptyList;

/**
 * @author Gustavo Punucura
 * @created 26-07-16.
 */

@ManagedBean(name = "sctBean")
@ViewScoped
public class SCTTypeBean implements Serializable {

    //@EJB
    private SnomedCTManager cstManager = (SnomedCTManager) RemoteEJBClientFactory.getInstance().getManager(SnomedCTManager.class);

    private String pattern;

    /** El concepto seleccionado en la JSP */
    private ConceptSCT conceptSelected;

    /** Elementos ... */
    private Map<Long, LazyDataModel<ConceptSMTK>> conceptSearchMap;

    private List<ConceptSMTK> conceptSel;

    private String searchOption = "term";

    private Integer relationshipGroup = null;

    List<Integer> relationshipGroups = Arrays.asList(new Integer[] {0, 1, 2, 3, 4});

    /**
     * Constructor por defecto para la inicialización de componentes.
     */
    public SCTTypeBean() {
        conceptSearchMap = new HashMap<Long, LazyDataModel<ConceptSMTK>>();
    }

    public List<ConceptSMTK> getConceptSel() {
        return conceptSel;
    }

    public void setConceptSel(List<ConceptSMTK> conceptSel) {
        this.conceptSel = conceptSel;
    }

    /**
     * Este método realiza la búsqueda del auto-complete, recuperando todos los conceptos (mostrando su toString()) SCT
     * cuyas descripciones coinciden con el patrón buscado.
     *
     * @param patron El patrón de búsqueda.
     *
     * @return Una lista con los conceptos a desplegar.
     */
    public List<ConceptSCT> getConceptSearchInput(String patron) {

        pattern = patron;

        List<ConceptSCT> concepts = new ArrayList<>();

        if( !validateQueryResultSize() )
            return concepts;

        /* Si el patrón viene vacío o es menor a tres caracteres, no se hace nada */
        if ( searchOption.equals("term") &&  ( patron == null || patron.trim().length() < 3 ) ) {
            return emptyList();
        }

        /* La búsqueda empieza aquí */
        if(searchOption.equals("term")) {
            concepts = cstManager.findConceptsByPattern(patron, relationshipGroup);
        }
        else{
            try{
                concepts = cstManager.findConceptsByConceptID(new Long(patron), relationshipGroup);
            }
            catch (NumberFormatException e){
                return null;
            }
        }
        return concepts;
    }


    public boolean validateQueryResultSize() {

        RequestContext rContext = RequestContext.getCurrentInstance();
        FacesContext context = FacesContext.getCurrentInstance();

        if ( searchOption.equals("term") &&  pattern.trim().length() >= 3  ) {

            if (cstManager.countConceptByPattern(pattern, relationshipGroup) > 10000) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Resultados exceden los 10.000 registros. El despliegue puede tardar"));
                Ajax.update("mainForm:growl");
                //rContext.execute("PF('dialogSCT').show();");
                return true;
            }

            return true;

        }

        return true;

    }

    public List<ConceptSCT> triggerSearch() {
        return getConceptSearchInput(pattern);
    }

    public Integer getRelationshipGroup() {
        return relationshipGroup;
    }

    public void setRelationshipGroup(Integer relationshipGroup) {
        this.relationshipGroup = relationshipGroup;
    }

    public SnomedCTManager getCstManager() {
        return cstManager;
    }

    public void setCstManager(SnomedCTManager cstManager) {
        this.cstManager = cstManager;
    }

    @PostConstruct
    public void init() {
        relationshipGroup=0;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getSearchOption() {
        return searchOption;
    }

    public void setSearchOption(String searchOption) {
        this.searchOption = searchOption;
    }

    public List<Integer> getRelationshipGroups() {
        return relationshipGroups;
    }

    public void setRelationshipGroups(List<Integer> relationshipGroups) {
        this.relationshipGroups = relationshipGroups;
    }

    public ConceptSCT getConceptSelected() {
        return conceptSelected;
    }

    public void setConceptSelected(ConceptSCT conceptSelected) {
        this.conceptSelected = conceptSelected;
    }

    public String highlightTerm(String term) {
        String[] searchList = pattern.split(" ");
        String[] replacementList = pattern.split(" ");
        for (int i = 0; i< replacementList.length; ++i) {
            replacementList[i] = "<b>"+replacementList[i]+"</b>";
        }
        return StringUtils.replaceEach(term, searchList, replacementList);
    }

    public List<DescriptionSCT> getOtherDescriptions(ConceptSCT concept){

        if(concept == null)
            return null;

        List<DescriptionSCT> otherDescriptions = new ArrayList<DescriptionSCT>();

        for (DescriptionSCT description : concept.getDescriptions()) {
            if(!description.getDescriptionType().equals(DescriptionSCTType.FSN) && !description.equals(concept.getDescriptionFavouriteSynonymous())) {
                otherDescriptions.add(description);
            }
        }

        return otherDescriptions;
    }

}
