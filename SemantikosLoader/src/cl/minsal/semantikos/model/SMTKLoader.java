package cl.minsal.semantikos.model;

import cl.minsal.semantikos.clients.RemoteEJBClientFactory;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.loaders.*;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.relationships.RelationshipDefinitionFactory;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.utils.ExtendedAscii;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by root on 08-06-17.
 */
public class SMTKLoader extends SwingWorker<Void, String> {

    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public static final String PATH_PREFIX = Paths.get(".").toAbsolutePath().normalize().toString().concat("/SemantikosLoader/resources/");

    private static final String ROOT = "datafiles/";
    //private static final String ENV = "test/";
    private static final String ENV = "basic/";
    private static final String ENV_DRUGS = "drugs/";

    private static final String SUBSTANCE = "substance/";
    private static final String MB = "MB/";
    private static final String MC = "MC/";
    private static final String MCCE = "MCCE/";
    private static final String GFP = "GFP/";
    private static final String FP = "FP/";
    private static final String PC = "PC/";
    private static final String PCCE = "PCCE/";

    /*Datafiles conceptos básicos*/
    public static final String BASIC_CONCEPTS_PATH=PATH_PREFIX+ROOT+ENV+"Conceptos_VIG_SMTK.txt";
    public static final String BASIC_DESCRIPTIONS_PATH=PATH_PREFIX+ROOT+ENV+"Descripciones_VIG_STK.txt";
    public static final String BASIC_RELATIONSHIPS_PATH=PATH_PREFIX+ROOT+ENV+"Relaciones_Conceptos_VIG_STK.txt";
    /*Datafiles Sustancias*/
    public static final String SUBSTANCE_PATH=PATH_PREFIX+ROOT+ENV_DRUGS+SUBSTANCE+"01_Sustancias.Base.txt";
    /*Datafiles MB*/
    public static final String MB_PATH=PATH_PREFIX+ROOT+ENV_DRUGS+MB+"02_Medicamento_Basico.Base.txt";
    /*Datafiles MC*/
    public static final String MC_PATH=PATH_PREFIX+ROOT+ENV_DRUGS+MC+"03_Medicamento_Clinico.Base.txt";
    public static final String MC_VIAS_ADM_PATH=PATH_PREFIX+ROOT+ENV_DRUGS+MC+"03_Medicamento_Clinico.Via_Administracion.txt";
    /*Datafiles MCCE*/
    public static final String MCCE_PATH=PATH_PREFIX+ROOT+ENV_DRUGS+MCCE+"04_Medicamento_Clinico_Con_Envase.Base.txt";
    /*Datafiles GFP*/
    public static final String GFP_PATH=PATH_PREFIX+ROOT+ENV_DRUGS+GFP+"05_Grupo_Familia_Producto.Base.txt";
    /*Datafiles FP*/
    public static final String FP_PATH=PATH_PREFIX+ROOT+ENV_DRUGS+FP+"06_Familia_Producto.Base.txt";
    /*Datafiles PC*/
    public static final String PC_PATH=PATH_PREFIX+ROOT+ENV_DRUGS+PC+"07_Producto_Comercial.Base.txt";
    /*Datafiles PCCE*/
    public static final String PCCE_PATH=PATH_PREFIX+ROOT+ENV_DRUGS+PCCE+"08_Producto_Comercial_Con_Envase.Base.txt";

    private CategoryManager categoryManager = (CategoryManager) RemoteEJBClientFactory.getInstance().getManager(CategoryManager.class);
    private TagSMTKManager tagSMTKManager = (TagSMTKManager) RemoteEJBClientFactory.getInstance().getManager(TagSMTKManager.class);
    private DescriptionManager descriptionManager = (DescriptionManager) RemoteEJBClientFactory.getInstance().getManager(DescriptionManager.class);
    private RelationshipManager relationshipManager = (RelationshipManager) RemoteEJBClientFactory.getInstance().getManager(RelationshipManager.class);
    private UserManager userManager = (UserManager) RemoteEJBClientFactory.getInstance().getManager(UserManager.class);

    private TagSMTKFactory tagSMTKFactory;
    private CategoryFactory categoryFactory;
    private RelationshipDefinitionFactory relationshipDefinitionFactory;

    private DescriptionTypeFactory descriptionTypeFactory;

    private static final Logger logger = java.util.logging.Logger.getLogger(SMTKLoader.class.getName() );

    private static JTextArea infoLogs;

    private static JTextArea errorLogs;

    /** Fecha */
    private Timestamp date;

    /** Usuario */
    private User user;

    /** Rutas de los datafiles (componentes) del snapshot
     *
     */
    private String basicConceptPath = BASIC_CONCEPTS_PATH;
    private String basicDescriptionPath = BASIC_DESCRIPTIONS_PATH;
    private String basicRelationshipPath = BASIC_RELATIONSHIPS_PATH;

    /**
     * Datos de ingreso del proceso de carga
     */
    private JTextField userName;
    private JTextField timeStamp;

    /**
     * Datos de control del proceso de carga
     */
    private JTextField conceptsTotal;
    private JTextField conceptsProcessed;

    /**
     *
     * Errores
     */
    private List<LoadLog> logs = new ArrayList<>();

    public SMTKLoader(JTextArea infoLogs, JTextArea errorLogs, JTextField conceptsTotal, JTextField conceptsProcessed, JTextField userName, JTextField timeStamp) {

        this.infoLogs = infoLogs;
        this.errorLogs = errorLogs;
        this.conceptsTotal = conceptsTotal;
        this.conceptsProcessed = conceptsProcessed;
        this.userName = userName;
        this.timeStamp = timeStamp;

        setUser(userManager.getUser(2));

        userName.setText(user.getEmail());
        timeStamp.setText(format.format(new Timestamp(System.currentTimeMillis())));

        categoryFactory =  categoryManager.getCategoryFactory();
        tagSMTKFactory = tagSMTKManager.getTagSMTKFactory();
        descriptionTypeFactory = descriptionManager.getDescriptionTypeFactory();
        relationshipDefinitionFactory = relationshipManager.getRelationshipDefinitionFactory();

        TagSMTKFactory.getInstance().setTagsSMTK(tagSMTKFactory.getTagsSMTK());
        TagSMTKFactory.getInstance().setTagsSMTKByName(tagSMTKFactory.getTagsSMTKByName());

        CategoryFactory.getInstance().setCategories(categoryFactory.getCategories());
        CategoryFactory.getInstance().setCategoriesByName(categoryFactory.getCategoriesByName());

        DescriptionTypeFactory.getInstance().setDescriptionTypes(descriptionTypeFactory.getDescriptionTypes());

        RelationshipDefinitionFactory.getInstance().setRelationshipDefinitions(relationshipDefinitionFactory.getRelationshipDefinitions());
        RelationshipDefinitionFactory.getInstance().setRelationshipDefinitionByName(relationshipDefinitionFactory.getRelationshipDefinitionByName());
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBasicConceptPath() {
        return basicConceptPath;
    }

    public void setBasicConceptPath(String basicConceptPath) {
        this.basicConceptPath = basicConceptPath;
    }

    public String getBasicDescriptionPath() {
        return basicDescriptionPath;
    }

    public void setBasicDescriptionPath(String basicDescriptionPath) {
        this.basicDescriptionPath = basicDescriptionPath;
    }

    public String getBasicRelationshipPath() {
        return basicRelationshipPath;
    }

    public void setBasicRelationshipPath(String basicRelationshipPath) {
        this.basicRelationshipPath = basicRelationshipPath;
    }

    public JTextField getConceptsTotal() {
        return conceptsTotal;
    }

    public void setConceptsTotal(JTextField conceptsTotal) {
        this.conceptsTotal = conceptsTotal;
    }

    public void setConceptsTotal(int conceptsTotal) {
        this.getConceptsTotal().setText(String.valueOf(conceptsTotal));
    }

    public JTextField getConceptsProcessed() {
        return conceptsProcessed;
    }

    public void setConceptsProcessed(JTextField conceptsProcessed) {
        this.conceptsProcessed = conceptsProcessed;
    }

    public void setConceptsProcessed(int conceptsProcessed) {
        this.getConceptsProcessed().setText(String.valueOf(conceptsProcessed));
    }

    public void incrementConceptsProcessed(int n) {
        int conceptsProcessed = Integer.parseInt(this.getConceptsProcessed().getText())+n;
        this.getConceptsProcessed().setText(String.valueOf(conceptsProcessed));

    }

    public JTextField getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(JTextField timeStamp) {
        this.timeStamp = timeStamp;
    }

    public JTextField getUserName() {
        return userName;
    }

    public void setUserName(JTextField userName) {
        this.userName = userName;
    }

    public List<LoadLog> getLogs() {
        return logs;
    }

    public void setLogs(List<LoadLog> logs) {
        this.logs = logs;
    }

    public JTextArea getInfoLogs() {
        return infoLogs;
    }

    public void setInfoLogs(JTextArea infoLogs) {
        this.infoLogs = infoLogs;
    }

    public JTextArea getErrorLogs() {
        return errorLogs;
    }

    public void setErrorLogs(JTextArea errorLogs) {
        this.errorLogs = errorLogs;
    }

    public static void logInfo(LoadLog log) {
        logger.info(log.toString());
        infoLogs.append(log.toString());
    }

    public static void logTick() {
        infoLogs.append(ExtendedAscii.printChar(10004));
        infoLogs.append("\n");
    }

    public static void logError(LoadLog log) {
        logger.info(log.toString());
        errorLogs.append(log.toString());
        errorLogs.append("\n");
    }

    public static void logWarning(LoadLog log) {
        logger.info(log.toString());
        errorLogs.append(log.toString());
        errorLogs.append("\n");
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {

            Initializer initializer = new Initializer();
            BasicConceptLoader basicConceptLoader = new BasicConceptLoader();
            SubstanceConceptLoader substanceConceptLoader = new SubstanceConceptLoader();
            MBConceptLoader mbConceptLoader = new MBConceptLoader();
            MCConceptLoader mcConceptLoader = new MCConceptLoader();
            MCCEConceptLoader mcceConceptLoader = new MCCEConceptLoader();
            GFPConceptLoader gfpConceptLoader = new GFPConceptLoader();
            FPConceptLoader fpConceptLoader = new FPConceptLoader();
            PCConceptLoader pcConceptLoader = new PCConceptLoader();
            PCCEConceptLoader pcceConceptLoader = new PCCEConceptLoader();

            /*
            initializer.checkBasicConceptsDataFiles(this);
            basicConceptLoader.processConcepts(this);
            */

            /*
            initializer.checkSubstanceDataFiles(this);
            substanceConceptLoader.processConcepts(this);
            */

            /*
            initializer.checkMBDataFiles(this);
            mbConceptLoader.processConcepts(this);
            */

            /*
            initializer.checkMCDataFiles(this);
            mcConceptLoader.processConcepts(this);

            initializer.checkMCCEDataFiles(this);
            mcceConceptLoader.processConcepts(this);
            */

            /*
            initializer.checkGFPDataFiles(this);
            gfpConceptLoader.processConcepts(this);
            */

            /*
            initializer.checkFPDataFiles(this);
            fpConceptLoader.processConcepts(this);
            */

            initializer.checkPCDataFiles(this);
            pcConceptLoader.processConcepts(this);

            /*
            initializer.checkPCCEDataFiles(this);
            pcceConceptLoader.processConcepts(this);
            */

            JOptionPane.showMessageDialog(null, "Carga de conceptos finalizada!");
        } catch (LoadException e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage());
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        infoLogs.append(chunks.get(0));
    }

}
