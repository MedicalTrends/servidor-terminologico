package cl.minsal.semantikos.model;

import cl.minsal.semantikos.clients.ServiceLocator;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by root on 08-06-17.
 */
public class SMTKLoader {

    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    //public static final String PATH_PREFIX = Paths.get(".").toAbsolutePath().normalize().toString().concat("/SemantikosLoader/resources/");

    //public static final String PATH_PREFIX = "/resources/";

    private static final String ROOT = "/datafiles/";
    //private static final String ENV = "test/";
    private static final String ENV_DRUGS = "drugs/";

    private static final String SUBSTANCE = "substance/";

    /*Datafiles Sustancias*/
    public static final String SUBSTANCE_PATH= ROOT+ENV_DRUGS+SUBSTANCE+"01_Sustancias.Base_test.txt";

    private CategoryManager categoryManager = (CategoryManager) ServiceLocator.getInstance().getService(CategoryManager.class);
    private TagSMTKManager tagSMTKManager = (TagSMTKManager) ServiceLocator.getInstance().getService(TagSMTKManager.class);
    private DescriptionManager descriptionManager = (DescriptionManager) ServiceLocator.getInstance().getService(DescriptionManager.class);
    private RelationshipManager relationshipManager = (RelationshipManager) ServiceLocator.getInstance().getService(RelationshipManager.class);
    private UserManager userManager = (UserManager) ServiceLocator.getInstance().getService(UserManager.class);

    private TagSMTKFactory tagSMTKFactory;
    private CategoryFactory categoryFactory;
    private RelationshipDefinitionFactory relationshipDefinitionFactory;

    private DescriptionTypeFactory descriptionTypeFactory;

    private static final Logger logger = java.util.logging.Logger.getLogger(SMTKLoader.class.getName() );

    /** Fecha */
    private Timestamp date;

    /** Usuario */
    private User user;

    private int total = 0;
    private int processed = 0;
    private int updated = 0;

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public int getProcessed() {
        return processed;
    }

    public void setProcessed(int processed) {
        this.processed = processed;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
/**
     * Datos de control del proceso de carga
     */

    /**
     *
     * Errores
     */
    private List<LoadLog> logs = new ArrayList<>();

    public SMTKLoader() {

        setUser(userManager.getUser(2));

        categoryFactory =  categoryManager.getCategoryFactory();
        tagSMTKFactory = tagSMTKManager.getTagSMTKFactory();
        descriptionTypeFactory = descriptionManager.getDescriptionTypeFactory();

        TagSMTKFactory.getInstance().setTagsSMTK(tagSMTKFactory.getTagsSMTK());
        TagSMTKFactory.getInstance().setTagsSMTKByName(tagSMTKFactory.getTagsSMTKByName());

        CategoryFactory.getInstance().setCategories(categoryFactory.getCategories());
        CategoryFactory.getInstance().setCategoriesByName(categoryFactory.getCategoriesByName());

        DescriptionTypeFactory.getInstance().setDescriptionTypes(descriptionTypeFactory.getDescriptionTypes());
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

    public List<LoadLog> getLogs() {
        return logs;
    }

    public void setLogs(List<LoadLog> logs) {
        this.logs = logs;
    }

    public static void logInfo(LoadLog log) {
        logger.log(Level.INFO, log.toString());
    }

    public static void logTick() {
        logger.log(Level.INFO, ExtendedAscii.printChar(10004));
    }

    public static void logError(LoadLog log) {
        logger.log(Level.SEVERE, log.toString());
    }

    public static void logWarning(LoadLog log) {
        logger.log(Level.WARNING, log.toString());
    }

    public void incrementConceptsProcessed(int n) {
        int processedBefore = (int)(((float)this.processed/(float)total)*100);
        this.processed = this.processed + n;
        int processedAfter = (int)(((float)this.processed/(float)total)*100);

        if(processedBefore < processedAfter ) {
            logger.log(Level.INFO, "Procesados: "+ processedAfter+ " %");
        }
    }

    public void incrementConceptsUpdated(int n) {
        int updatedBefore = (int)(((float)this.updated/(float)total)*100);
        this.updated = this.updated + n;
        int updatedAfter = (int)(((float)this.updated/(float)total)*100);

        if(updatedBefore < updatedAfter ) {
            logger.log(Level.INFO, "Actualizados: "+ updatedAfter+ " %");
        }
    }

    public void process() throws Exception {
        try {
            Initializer initializer = new Initializer();
            SubstanceConceptLoader substanceConceptLoader = new SubstanceConceptLoader();

            initializer.checkSubstanceDataFiles(this);
            substanceConceptLoader.processConcepts(this);

            logger.info("Proceso finalizado!");
        } catch (LoadException e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage());
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
