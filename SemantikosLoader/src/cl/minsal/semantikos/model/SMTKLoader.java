package cl.minsal.semantikos.model;

import cl.minsal.semantikos.LoaderView;
import cl.minsal.semantikos.loaders.BasicConceptLoader;
import cl.minsal.semantikos.loaders.Initializer;
import cl.minsal.semantikos.model.users.User;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by root on 08-06-17.
 */
public class SMTKLoader extends SwingWorker<Void, String> {

    public static final String PATH_PREFIX =  Paths.get(".").toAbsolutePath().normalize().toString().concat("/SemantikosLoader/resources/");

    public static final String BASIC_CONCEPTS_PATH=PATH_PREFIX.concat("datafiles/basic/Conceptos_VIG_SMTK.txt");
    public static final String BASIC_DESCRIPTIONS_PATH=PATH_PREFIX.concat("datafiles/basic/Descripciones_VIG_STK.txt");
    public static final String BASIC_RELATIONSHIPS_PATH=PATH_PREFIX.concat("datafiles/basic/Relaciones_Conceptos_VIG_STK.txt");

    private static final Logger logger = java.util.logging.Logger.getLogger(SMTKLoader.class.getName() );

    private JTextArea logsComponent;

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
     * Datos de control del proceso de carga
     */
    private JTextField conceptsTotal;
    private JTextField conceptsProcessed;

    /**
     *
     * Errores
     */
    private List<LoadLog> logs = new ArrayList<>();


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

    public List<LoadLog> getLogs() {
        return logs;
    }

    public void setLogs(List<LoadLog> logs) {
        this.logs = logs;
    }

    public JTextArea getLogsComponent() {
        return logsComponent;
    }

    public void setLogsComponent(JTextArea logsComponent) {
        this.logsComponent = logsComponent;
    }

    public void log(LoadLog log) {

        System.out.println(log.toString());
        logger.info(log.toString());
        logsComponent.append(log.toString());
        logsComponent.append("\n");
        //refreshConsole(log.toString());
        //LoaderView.refreshConsole("HOLA");
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            Initializer initializer = new Initializer();
            BasicConceptLoader basicConceptLoader = new BasicConceptLoader();

            initializer.checkDataFiles(this);
            basicConceptLoader.processConcepts(this);
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
        logsComponent.append(chunks.get(0));
    }



}
