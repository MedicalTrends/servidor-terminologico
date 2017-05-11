package cl.minsal.semantikos.designer_modeler.designer;

import cl.minsal.semantikos.beans.concept.ConceptBean;
import cl.minsal.semantikos.beans.helpertables.HelperTableBean;
import cl.minsal.semantikos.designer_modeler.auth.AuthenticationBean;
import cl.minsal.semantikos.kernel.auth.UserManager;
import cl.minsal.semantikos.kernel.components.HelperTablesManager;
import cl.minsal.semantikos.kernel.components.HelperTablesManagerImpl;
import cl.minsal.semantikos.kernel.components.RelationshipManager;
import cl.minsal.semantikos.kernel.components.ispfetcher.ISPFetcher;
import cl.minsal.semantikos.kernel.factories.HelperTableRecordFactory;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.helpertables.*;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.Target;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by BluePrints Developer on 16-11-2016.
 */
@ManagedBean(name="ispbean")
@ViewScoped
public class ISPBean {


    static private final long ISP_TABLE_ID=9;

    private Boolean existe;
    private String regnum;
    private Integer ano = null;

    private Map<String,String> fetchedData;

    private HelperTableRow ispRecord = null;

    public Map<String, String> getFetchedData() {
        return fetchedData;
    }

    public void setFetchedData(Map<String, String> fetchedData) {
        this.fetchedData = fetchedData;
    }

    @EJB
    ISPFetcher ispFetcher;

    @EJB
    HelperTablesManager helperTablesManager;

    @EJB
    UserManager userManager;

    @EJB
    RelationshipManager relationshipManager;

    @EJB
    HelperTableRecordFactory helperTableRecordFactory;

    @ManagedProperty(value = "#{helperTableBean}")
    private HelperTableBean helperTableBean;

    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    @ManagedProperty(value = "#{conceptBean}")
    private ConceptBean conceptBean;


    @PostConstruct
    public void init() {
        // Se setea en duro la opcionalidad de la relación, esta debería ser opcional.
        for (RelationshipDefinition rd : conceptBean.getCategory().getRelationshipDefinitions()) {
            if(rd.isISP()) {
                rd.getMultiplicity().setLowerBoundary(0);
                if(conceptBean.getConcept().isPersistent()){
                    List<Relationship> relationshipList =conceptBean.getConcept().getRelationshipsByRelationDefinition(rd);
                    if(relationshipList.size()>0){
                        existe=true;
                    }
                }
            }
        }

    }


    public AuthenticationBean getAuthenticationBean() {
        return authenticationBean;
    }


    public void setAuthenticationBean(AuthenticationBean authenticationBean) {
        this.authenticationBean = authenticationBean;
    }

    public ConceptBean getConceptBean() {
        return conceptBean;
    }

    public void setConceptBean(ConceptBean conceptBean) {
        this.conceptBean = conceptBean;
    }

    public Boolean getExiste() {
        return existe;
    }

    public void setExiste(Boolean existe) {
        this.existe = existe;
    }

    public String getRegnum() {
        return regnum;
    }

    public void setRegnum(String regnum) {
        this.regnum = regnum;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public void fetchData(){

        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext fContext = FacesContext.getCurrentInstance();

        RelationshipDefinition relationshipDefinition = (RelationshipDefinition) UIComponent.getCurrentComponent(fContext).getAttributes().get("relationshipDefinition");

        HelperTable ispHelperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

        if(regnum.trim().equals("") || ano == null || ano == 0) {
            conceptBean.getMessageBean().messageError("Debe ingresar un valor para el dato 'RegNum' y 'RegAño'");
            return;
        }

        ispRecord = null;

        /**
         * Primero se busca un registro isp local
         */
        for (HelperTableRow helperTableRecord : helperTablesManager.searchRows(ispHelperTable,regnum+"/"+ano)) {
            ispRecord = helperTableRecord;
            break;
        }

        /**
         * Si no existe, se va a buscar a la página del registro isp
         */
        if(ispRecord==null) {

            ispRecord = new HelperTableRow(ispHelperTable);
            fetchedData = ispFetcher.getISPData(regnum + "/" + ano);

            if(!fetchedData.isEmpty()) {
                mapIspRecord(ispHelperTable, ispRecord, fetchedData);
            }

        }
        else {
        /**
         * Si se encuentra, se verifica que no exista actualmente una relación con este destino
         */
            for (Relationship relationship : relationshipManager.findRelationshipsLike(relationshipDefinition,ispRecord)) {
                if(relationship.getRelationshipDefinition().isISP()) {
                    conceptBean.getMessageBean().messageError("Para agregar una relación a ISP, la dupla ProductoComercial-Regnum/RegAño deben ser únicos. Registro referenciado por concepto " + relationship.getSourceConcept().getDescriptionFavorite());
                    return;
                }
            }
        }

        context.execute("PF('ispfetcheddialog').show();");
    }

    public void mapIspRecord(HelperTable ispHelperTable, HelperTableRow ispRecord, Map<String,String> fetchedRecord){

        for (HelperTableColumn helperTableColumn : ispHelperTable.getColumns()) {
            for (String s : fetchedRecord.keySet()) {
                if(helperTableColumn.getDescription().toLowerCase().contains(s.toLowerCase())) {
                    HelperTableData cell = new HelperTableData();
                    cell.setColumn(helperTableColumn);
                    if(helperTableColumn.getDescription().toLowerCase().contains("fecha") ||
                       helperTableColumn.getDescription().toLowerCase().contains("ultima") ) {
                        if(!fetchedRecord.get(s).trim().isEmpty()) {
                            cell.setDateValue(new Timestamp(Date.parse(fetchedRecord.get(s))));
                        }
                    }
                    else {
                        cell.setStringValue(fetchedRecord.get(s));
                    }
                    ispRecord.getCells().add(cell);
                    break;
                }
            }
        }
    }

    public void fetchData(String registro){

        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext fContext = FacesContext.getCurrentInstance();

        RelationshipDefinition relationshipDefinition = (RelationshipDefinition) UIComponent.getCurrentComponent(fContext).getAttributes().get("relationshipDefinition");

        HelperTable ispHelperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

        String[] tokens = registro.split("/");

        regnum = tokens[0];
        ano = Integer.parseInt(tokens[1]);

        ispRecord = null;

        /**
         * Primero se busca un registro isp local
         */
        for (HelperTableRow helperTableRecord : helperTablesManager.searchRows(ispHelperTable,regnum+"/"+ano)) {
            ispRecord = helperTableRecord;
            break;
        }

        context.execute("PF('dialogISP').show();");
    }


    public void updateOptionality(RelationshipDefinition relationshipDefinition){
        if(existe)
            relationshipDefinition.getMultiplicity().setLowerBoundary(1);
        else
            relationshipDefinition.getMultiplicity().setLowerBoundary(0);
    }


    public List<String> getMapKeys(){

        if (fetchedData == null )
            return new ArrayList<String>();


        List<String> result = new ArrayList<>();
        result.addAll(fetchedData.keySet());

        return result;
    }


    public void agregarISP(RelationshipDefinition relationshipDefinition){

        if(!ispRecord.isPersistent()){

            ispRecord.setDescription(ispRecord.getCellByColumnName("registro").toString());
            ispRecord.setValid(true);

            HelperTableRow inserted = null;

            inserted = helperTablesManager.insertRow(ispRecord,authenticationBean.getEmail());
            ispRecord = inserted;
        }
        else {
            HelperTableRow updated = null;
            try {
                updated = helperTablesManager.updateRow(ispRecord,authenticationBean.getEmail());
            } catch (HelperTablesManagerImpl.RowInUseException e) {
                e.printStackTrace();
            }
            ispRecord = updated;
        }

        conceptBean.setSelectedHelperTableRecord(ispRecord);
        conceptBean.addRelationship(relationshipDefinition,ispRecord);
        clean();
    }

    private void clean() {
       existe = true;
       regnum = "";
       ano = null;
       ispRecord = null;
    }

    public HelperTableRow getIspRecord() {
        return ispRecord;
    }

    public void setIspRecord(HelperTableRow ispRecord) {
        this.ispRecord = ispRecord;
    }


    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public RelationshipManager getRelationshipManager() {
        return relationshipManager;
    }

    public void setRelationshipManager(RelationshipManager relationshipManager) {
        this.relationshipManager = relationshipManager;
    }


    public HelperTableBean getHelperTableBean() {
        return helperTableBean;
    }

    public void setHelperTableBean(HelperTableBean helperTableBean) {
        this.helperTableBean = helperTableBean;
    }

    public List<Relationship> findRelationshipsLike(ConceptSMTK sourceConcept, RelationshipDefinition relationshipDefinition, Target target) {

        List<Relationship> relationshipsLike = new ArrayList<>();

        if(target != null) {

            for (Relationship relationship : relationshipManager.findRelationshipsLike(relationshipDefinition, target)) {
                if(!relationship.getSourceConcept().equals(sourceConcept)) {
                    relationshipsLike.add(relationship);
                }
            }
        }

        return relationshipsLike;
    }

}
