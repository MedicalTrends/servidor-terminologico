package cl.minsal.semantikos.designer_modeler.designer;

import cl.minsal.semantikos.designer_modeler.auth.AuthenticationBean;
import cl.minsal.semantikos.kernel.components.HelperTableManager;
import cl.minsal.semantikos.kernel.components.ispfetcher.ISPFetcher;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.*;

/**
 * Created by BluePrints Developer on 16-11-2016.
 */
@ManagedBean(name="ispbean")
@ViewScoped
public class ISPBean {


    private Boolean existe = true;
    private String regnum;
    private int ano;


    private Map<String,String> fetchedData;


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



    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    @ManagedProperty(value = "#{conceptBean}")
    private ConceptBean conceptBean;


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

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public void fetchData(){
        fetchedData = ispFetcher.getISPData(regnum+"/"+ano);
    }


    public void updateExiste(){
    }


    public List<String> getMapKeys(){

        if (fetchedData == null )
            return new ArrayList<String>();

        List<String> ret = new ArrayList<>(fetchedData.size());
        ret.addAll(fetchedData.keySet());
        return ret;
    }


    public void agregarISP(RelationshipDefinition relationshipDefinition){
        HelperTable ispHT = getISPHelperTable();

        HelperTableRecord record = new HelperTableRecord(ispHT, mapFetchedData(fetchedData));

        HelperTableRecord inserted = helperTableManager.insertRecord(ispHT,record,authenticationBean.getLoggedUser());

        HelperTableRecord refreshed = helperTableManager.getRecord(ispHT,inserted.getId());

        conceptBean.setSelectedHelperTableRecord(refreshed);

        conceptBean.addRelationship(relationshipDefinition,refreshed);

        clean();

    }

    private void clean() {
       existe = true;
       regnum = "";
       ano = 0;
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

        Collection<HelperTable> tablas = helperTableManager.getHelperTables();


        for (HelperTable ht: tablas) {
            if(ht.getName().equals("smtk_helper_table_isp"))
                return ht;
        }

        return null;
    }

}
