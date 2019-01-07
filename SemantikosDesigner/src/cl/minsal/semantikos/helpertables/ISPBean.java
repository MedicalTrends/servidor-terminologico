package cl.minsal.semantikos.helpertables;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.concept.ConceptBean;
import cl.minsal.semantikos.kernel.components.UserManager;
import cl.minsal.semantikos.modelweb.RelationshipWeb;
import cl.minsal.semantikos.users.AuthenticationBean;
import cl.minsal.semantikos.kernel.components.HelperTablesManager;
import cl.minsal.semantikos.kernel.components.RelationshipManager;
import cl.minsal.semantikos.kernel.components.ISPFetcher;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.exceptions.RowInUseException;
import cl.minsal.semantikos.model.helpertables.*;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.Target;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.sql.Timestamp;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by BluePrints Developer on 16-11-2016.
 */
@ManagedBean(name="ispbean")
@SessionScoped
public class ISPBean {


    static private final long ISP_TABLE_ID=9;

    private Boolean existe = false;
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

    @ManagedProperty(value = "#{helperTableBean}")
    private HelperTableBean helperTableBean;

    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    @ManagedProperty(value = "#{conceptBean}")
    private ConceptBean conceptBean;

    //@EJB
    ISPFetcher ispFetcher = (ISPFetcher) ServiceLocator.getInstance().getService(ISPFetcher.class);

    //@EJB
    HelperTablesManager helperTablesManager = (HelperTablesManager) ServiceLocator.getInstance().getService(HelperTablesManager.class);

    //@EJB
    UserManager userManager = (UserManager) ServiceLocator.getInstance().getService(UserManager.class);

    //@EJB
    RelationshipManager relationshipManager = (RelationshipManager) ServiceLocator.getInstance().getService(RelationshipManager.class);;


    @PostConstruct
    public void init() {

        // Se setea en duro la opcionalidad de la relación, esta debería ser opcional.}

    }

    public void setOptionality(RelationshipDefinition relationshipDefinition) {

        if(existe) {
            return;
        }

        if (conceptBean.getConcept().getValidRelationshipsWebByRelationDefinition(relationshipDefinition).isEmpty()) {
            relationshipDefinition.getMultiplicity().setLowerBoundary(0);
            existe = false;
        } else {
            relationshipDefinition.getMultiplicity().setLowerBoundary(1);
            existe = true;
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

    public void fetchData() {

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
                try {
                    mapIspRecord(ispHelperTable, ispRecord, fetchedData);
                } catch (ParseException e) {
                    conceptBean.getMessageBean().messageError(e.getMessage());
                    return;
                }
            }

        }
        /*
        else {
            // Si se encuentra, se verifica que no exista actualmente una relación con este destino
            for (Relationship relationship : relationshipManager.findRelationshipsLike(relationshipDefinition,ispRecord)) {
                if(relationship.isPersistent() && relationship.getRelationshipDefinition().isISP()) {
                    conceptBean.getMessageBean().messageError("Para agregar una relación a ISP, la dupla ProductoComercial-Regnum/RegAño deben ser únicos. Registro referenciado por concepto " + relationship.getSourceConcept().getDescriptionFavorite());
                    return;
                }
            }
        }
        */

        context.execute("PF('ispfetcheddialog').show();");
    }

    /*
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
    */

    public void mapIspRecord(HelperTable ispHelperTable, HelperTableRow ispRecord, Map<String,String> fetchedRecord) throws ParseException {

        final Collator instance = Collator.getInstance();

        // This strategy mean it'll ignore the accents
        instance.setStrength(Collator.NO_DECOMPOSITION);

        for (HelperTableColumn helperTableColumn : ispHelperTable.getColumns()) {
            for (String s : fetchedRecord.keySet()) {
                if(instance.compare(helperTableColumn.getDescription().trim().toLowerCase(), s.trim().toLowerCase()) == 0) {
                    HelperTableData cell = new HelperTableData();
                    cell.setColumn(helperTableColumn);
                    if(helperTableColumn.getDescription().toLowerCase().contains("fecha") ||
                            helperTableColumn.getDescription().toLowerCase().contains("ultima") ) {
                        if(!fetchedRecord.get(s).trim().isEmpty()) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            Date parsedDate = dateFormat.parse(fetchedRecord.get(s));
                            cell.setDateValue(new Timestamp(parsedDate.getTime()));
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
        ispRecord.setDescription(ispRecord.getCellByColumnName("registro").toString());
        ispRecord.setValid(true);
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
        if(existe) {
            relationshipDefinition.getMultiplicity().setLowerBoundary(1);
        }
        else {
            if(!conceptBean.getConcept().getRelationshipsByRelationDefinition(relationshipDefinition).isEmpty()) {
                setExiste(true);
                conceptBean.getMessageBean().messageError("Existen relaciones para la definición: '" + relationshipDefinition.getName() + "'");
                return;
            }
            relationshipDefinition.getMultiplicity().setLowerBoundary(0);
            for (Relationship r : conceptBean.getConcept().getRelationshipsByRelationDefinition(relationshipDefinition)) {
                conceptBean.removeRelationship(relationshipDefinition, r);
            }
        }
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
            } catch (RowInUseException e) {
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

    public List<Relationship> findRelationshipsLike(Relationship relationship, RelationshipDefinition relationshipDefinition) {

        List<Relationship> relationshipsLike = new ArrayList<>();

        if(relationship.getTarget() != null) {

            for (Relationship relationshipLike : relationshipManager.findRelationshipsLike(relationship, relationshipDefinition)) {
                if(!relationshipLike.getSourceConcept().equals(relationship.getSourceConcept())) {
                    relationshipsLike.add(relationship);
                }
            }
        }

        return relationshipsLike;
    }

}
