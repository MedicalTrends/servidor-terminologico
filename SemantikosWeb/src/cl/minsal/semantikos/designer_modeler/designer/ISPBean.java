package cl.minsal.semantikos.designer_modeler.designer;

import cl.minsal.semantikos.beans.concept.ConceptBean;
import cl.minsal.semantikos.designer_modeler.auth.AuthenticationBean;
import cl.minsal.semantikos.kernel.auth.UserManager;
import cl.minsal.semantikos.kernel.components.HelperTableManager;
import cl.minsal.semantikos.kernel.components.RelationshipManager;
import cl.minsal.semantikos.kernel.components.ispfetcher.ISPFetcher;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.*;

/**
 * Created by BluePrints Developer on 16-11-2016.
 */
@ManagedBean(name="ispbean")
@ViewScoped
public class ISPBean {


    private Boolean existe;
    private String regnum;
    private Integer ano = null;

    private HelperTable ht;


    private Map<String,String> fetchedData;

    private HelperTableRecord ispRecord = null;

    public Map<String, String> getFetchedData() {
        return fetchedData;
    }

    public void setFetchedData(Map<String, String> fetchedData) {
        this.fetchedData = fetchedData;
    }

    @EJB
    ISPFetcher ispFetcher;

    @EJB
    HelperTableManager helperTableManager;

    @EJB
    UserManager userManager;

    @EJB
    RelationshipManager relationshipManager;

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

        //fetchedData = helperTableManager.searchRecords(getISPHelperTable(),"description",regnum+"/"+ano,true).get(0).getFields();
        if(regnum.trim().equals("") || ano == null || ano == 0) {
            conceptBean.getMessageBean().messageError("Debe ingresar un valor para el dato 'RegNum' y 'RegAño'");
            return;
        }

        ispRecord = null;

        /**
         * Primero se busca un registro isp local
         */
        for (HelperTableRecord helperTableRecord : helperTableManager.searchRecords(getISPHelperTable(),"description",regnum+"/"+ano,true)) {
            ispRecord = helperTableRecord;
            break;
        }

        /**
         * Si no existe, se va a buscar a la página del registro isp
         */
        if(ispRecord==null) {
            //fetchedData = ispFetcher.getISPData(regnum+"/"+ano);
            ispRecord = new HelperTableRecord(getISPHelperTable(), ispFetcher.getISPData(regnum + "/" + ano));
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

    public void fetchData(String registro){

        RequestContext context = RequestContext.getCurrentInstance();

        String[] tokens = registro.split("/");

        regnum = tokens[0];
        ano = Integer.parseInt(tokens[1]);

        ispRecord = null;

        /**
         * Primero se busca un registro isp local
         */
        for (HelperTableRecord helperTableRecord : helperTableManager.searchRecords(getISPHelperTable(),"description",regnum+"/"+ano,true)) {
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

        if (ispRecord == null )
            return new ArrayList<String>();

        List<String> ret = new ArrayList<>(ispRecord.getFields().size());
        ret.addAll(ispRecord.getFields().keySet());
        return ret;
    }


    public void agregarISP(RelationshipDefinition relationshipDefinition){


        if(!ispRecord.isPersistent()){
            HelperTable ispHT = getISPHelperTable();
            ispRecord.setFields(mapFetchedData(ispRecord.getFields()));
            HelperTableRecord inserted = helperTableManager.insertRecord(ispHT,ispRecord,authenticationBean.getLoggedUser());
            ispRecord = helperTableManager.getRecord(ispHT,inserted.getId());
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

    private Map<String, String> mapFetchedData(Map<String, String> fetchedData) {
        Map<String, String> ret = new HashMap<String, String>();

        ret.put("registro", fetchedData.get("Registro"));
        ret.put("nombre", fetchedData.get("Nombre"));
        ret.put("referencia_de_tramite", fetchedData.get("Referencia de Tramite"));
        ret.put("equivalencia_terapeutica", fetchedData.get("Equivalencia Terapéutica"));
        ret.put("titular", fetchedData.get("Titular"));
        ret.put("estado_del_registro", fetchedData.get("Estado del Registro"));
        ret.put("resolucion_inscribase", fetchedData.get("Resolución Inscríbase"));
        ret.put("fecha_inscribase", fetchedData.get("Fecha Inscríbase"));
        ret.put("ultima_renovacion", fetchedData.get("Ultima Renovación"));
        ret.put("fecha_proxima_renovacion", fetchedData.get("Fecha Próxima renovación"));
        ret.put("regimen", fetchedData.get("Régimen"));
        ret.put("via_administracion", fetchedData.get("Vía Administración"));
        ret.put("condicion_de_venta", fetchedData.get("Condición de Venta"));
        ret.put("expende_tipo_establecimiento", fetchedData.get("Expende tipo establecimiento"));
        ret.put("indicacion", fetchedData.get("Indicación"));

        ret.put("description", fetchedData.get("Registro"));

        return ret;

    }

    public HelperTable getISPHelperTable(){

        if(ht == null) {


            Collection<HelperTable> tablas = helperTableManager.getHelperTables();


            for (HelperTable ht1 : tablas) {
                if (ht1.getName().equals("smtk_helper_table_isp")) {
                    ht = ht1;
                    break;
                }
            }
        }

        return ht;
    }

    public HelperTableRecord getIspRecord() {
        return ispRecord;
    }

    public void setIspRecord(HelperTableRecord ispRecord) {
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


    /*
verifica si el registro ya existe en la base de datos
 */
    public boolean getExisteRegistroISP(){
        if(ispRecord==null || ispRecord.getFields().size()==0)
            return false;

        List<HelperTableRecord> records = helperTableManager.searchRecords(getISPHelperTable(),"description",regnum+"/"+ano,true);

        return  records.size() >0;
    }


}
