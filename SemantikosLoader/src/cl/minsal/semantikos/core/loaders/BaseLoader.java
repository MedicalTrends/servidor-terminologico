package cl.minsal.semantikos.core.loaders;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.LoadException;
import cl.minsal.semantikos.model.LoadLog;
import cl.minsal.semantikos.model.SMTKLoader;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionType;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.modelweb.ConceptSMTKWeb;
import cl.minsal.semantikos.modelweb.Pair;
import cl.minsal.semantikos.modelweb.RelationshipWeb;
import cl.minsal.semantikos.util.StringUtils;

import javax.ejb.EJBException;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cl.minsal.semantikos.model.LoadLog.ERROR;
import static cl.minsal.semantikos.model.LoadLog.INFO;

/**
 * Created by root on 08-06-17.
 */
public abstract class BaseLoader {

    private static final Logger logger = Logger.getLogger(BaseLoader.class.getName() );

    Path path;

    public BufferedReader reader;

    FileWriter fw;

    static BufferedWriter writer;

    protected static String separator = ";";

    protected static String newline = System.getProperty("line.separator");

    protected static String attrSeparator = "\\|";

    protected ConceptSMTKWeb oldConcept;

    protected ConceptSMTKWeb newConcept;

    protected Map<String, ConceptSMTK> conceptsForPersist = new HashMap<>();

    protected Map<String, Pair<ConceptSMTKWeb, ConceptSMTKWeb>> conceptsForUpdate = new HashMap<>();

    protected String[] tokens;

    public static Map<String, Integer> fields = new HashMap<>();

    protected static List<RelationshipDefinition> nonUpdateableDefinitions = new ArrayList<>();

    Map<String, Description> descriptionMap = new HashMap<>();

    protected Category category;

    private static final String ROOT = "/datafiles/";
    private static final String ENV = "master/";
    //private static final String ENV = "test/";

    public static final String dataFile = ROOT + ENV + "TFC_1.3.txt";

    public BaseLoader(User user) {
        this.user = user;
    }

    public BaseLoader(Category category, User user) {
        this.user = user;
        this.category = category;
    }

    protected User user;

    ConceptManager conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);
    DescriptionManager descriptionManager = (DescriptionManager) ServiceLocator.getInstance().getService(DescriptionManager.class);
    RelationshipManager relationshipManager = (RelationshipManager) ServiceLocator.getInstance().getService(RelationshipManager.class);
    SnomedCTManager snomedCTManager = (SnomedCTManager) ServiceLocator.getInstance().getService(SnomedCTManager.class);
    HelperTablesManager helperTableManager = (HelperTablesManager) ServiceLocator.getInstance().getService(HelperTablesManager.class);

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void initReader(String path) throws LoadException {

        this.path = Paths.get(path);
        try {
            //reader = Files.newBufferedReader(this.path, Charset.defaultCharset());
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)));

            // 1a Línea contiene la categoría, saltar a la siguiente línea
            reader.readLine();

            /**
             * Descartar header
             */
            String line = reader.readLine();

            if(line == null) {
                throw new LoadException(path, "", "Archivo sin cabecera!!", ERROR);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void haltReader() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //header es el archivo
    public void assertHeader(List<String> fields, List<String> header) throws LoadException {
        for (String field : fields) {
            if(!header.contains(field)) {
                String msg = "El encabezado no contiene el campo '" + field + "'";
                LoadException ex = new LoadException(dataFile, "", msg, ERROR);
                throw ex;
            }
        }
    }

    public void initWriter(String path) throws LoadException {

        try {
            fw = new FileWriter(path);

            writer = new BufferedWriter(fw);
            writer.write("CONCEPTO_ID;TIPO;STATUS;MENSAJE");
            writer.write(newline);
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void haltWriter() {
        try {
            writer.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(LoadException ex) {

        try {
            if(ex.getMessage() != null) {
                writer.write(ex.getIdConcept() + separator + ex.getAction() + separator + ex.getType() + separator + ex.getMessage() );
            }
            else {
                writer.write(ex.getIdConcept() + separator + ex.getAction() + separator + ex.getType() + separator + ex.getMessage() );
            }

            writer.write(newline);
            writer.flush();

            Level level = null;

            switch (ex.getType()) {
                case LoadException.ERROR:
                    level = Level.SEVERE;
                    break;
                case LoadException.INFO:
                    level = Level.INFO;
                    break;
            }

            logger.log(level, ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void verifyGeneralMARules(String type) throws LoadException {

        String conceptID = StringUtils.normalizeSpaces(tokens[fields.get("CONCEPTO_ID")]).trim();

        try {
            ConceptSMTK conceptSMTK = conceptManager.getConceptByCONCEPT_ID(conceptID);

            String msg = "Verificando Concepto '" + conceptSMTK.toString() + "'";
            log(new LoadException(dataFile, conceptID, msg, LoadException.INFO));

            /*Recuperando descripcion preferida*/
            String term = StringUtils.normalizeSpaces(tokens[fields.get("DESCRIPCION")]).trim();

            if(conceptSMTK.getDescriptionFavorite().getTerm().equals(term)) {
                String message = "La descripción '" + term + "' no coincide con la descripción preferida '" + conceptSMTK.getDescriptionFavorite().getTerm() + "'";
                throw new LoadException(dataFile, conceptID, message, ERROR, type);
            }
        }
        catch (EJBException e) {
            throw new LoadException(dataFile, conceptID, e.getMessage(), ERROR, type);
        }

        String msg = "Concepto '" + conceptID + "' OK";
        log(new LoadException(dataFile, conceptID, msg, LoadException.INFO));
    }

    public void init(String type, Category category, String term) throws LoadException {

        this.category = category;
        String conceptID = StringUtils.normalizeSpaces(tokens[fields.get("CONCEPTO_ID")]).trim();

        switch (type) {
            case "M":
                //...solo Verificar concepto y salir
                //verifyGeneralMARules(type);
                break;
            case "A":
                /*Si es una actualización*/
                //verifyGeneralMARules(type);

                String msg = "Cargando actualización concepto '" + conceptID + "'";
                log(new LoadException(dataFile, conceptID, msg, LoadException.INFO, type));

                //Se buscan descripciones con glosa suministrada
                List<Description> descriptions = descriptionManager.searchDescriptionsPerfectMatch(term, Arrays.asList(new Category[]{category}), null);

                if(!descriptions.isEmpty()) {
                    ConceptSMTK conceptSMTK = descriptions.get(0).getConceptSMTK();
                    conceptSMTK.setRelationships(relationshipManager.getRelationshipsBySourceConcept(conceptSMTK));

                    oldConcept = new ConceptSMTKWeb(conceptSMTK);
                    //Se crea una copia idéntica del concepto original
                    newConcept = new ConceptSMTKWeb(oldConcept);
                }
                else {
                    // Si no se encuentran Lanzar excepción
                    String message = "No existe un concepto con descripción preferida '" + term + "'";
                    throw new LoadException(dataFile, conceptID, message, LoadException.ERROR, type);
                }
                break;
            case "N":
                msg = "Creando nuevo concepto '" + conceptID + "'";
                log(new LoadException(dataFile, conceptID, msg, LoadException.INFO, type));

                boolean toBeReviewed = tokens[fields.get("REVISADO")].equals("Si");
                boolean toBeConsulted = tokens[fields.get("CONSULTAR")].equals("Si");
                boolean autogenerated = tokens[fields.get("CREAC_NOMBRE")].equals("Autogenerado");
                TagSMTK tagSMTK;

                if(category.getName().equals("Fármacos - Sustancia")) {
                    tagSMTK = TagSMTKFactory.getInstance().findTagSMTKByName("sustancia");
                }
                else {
                    tagSMTK = TagSMTKFactory.getInstance().findTagSMTKByName("producto");
                }

                ConceptSMTK conceptSMTK = new ConceptSMTK(category);

                conceptSMTK.setFullyDefined(false);
                conceptSMTK.setToBeConsulted(toBeConsulted);
                conceptSMTK.setToBeReviewed(toBeReviewed);
                conceptSMTK.setCategory(category);
                conceptSMTK.setTagSMTK(tagSMTK);

                /*Recuperando datos Descripciones*/
                boolean caseSensitive = tokens[fields.get("SENSIBLE_MAYUSCULA")].equals("Sensible");
                DescriptionType descriptionType = DescriptionType.PREFERIDA;

                Description descriptionFavourite = new Description(conceptSMTK, term, descriptionType);
                descriptionFavourite.setCaseSensitive(caseSensitive);
                descriptionFavourite.setCreatorUser(user);
                descriptionFavourite.setAutogeneratedName(autogenerated);

                if(descriptionMap.containsKey(descriptionFavourite.getTerm())) {
                    SMTKLoader.logWarning(new LoadLog("Término repetido para descripción " + descriptionFavourite.toString() + ". Se descarta descripción", INFO));
                }
                else {
                    descriptionMap.put(descriptionFavourite.getTerm(), descriptionFavourite);
                    conceptSMTK.addDescription(descriptionFavourite);
                }

                /*Recuperando descripcion FSN*/
                term = descriptionFavourite.getTerm() + " (" + tagSMTK.getName() + ")";
                descriptionType = DescriptionType.FSN;

                Description descriptionFSN = new Description(conceptSMTK, term, descriptionType);
                descriptionFSN.setCaseSensitive(caseSensitive);
                descriptionFSN.setCreatorUser(user);
                descriptionFSN.setAutogeneratedName(autogenerated);

                if(descriptionMap.containsKey(descriptionFSN.getTerm())) {
                    SMTKLoader.logWarning(new LoadLog("Término repetido para descripción "+descriptionFSN.toString()+". Se descarta descripción", INFO));
                }
                else {
                    descriptionMap.put(descriptionFSN.getTerm(), descriptionFSN);
                    conceptSMTK.addDescription(descriptionFSN);
                }

                /*Recuperando Sinónimos*/
                String synonyms = tokens[fields.get("SINONIMO")];

                if (!StringUtils.isEmpty(synonyms)) {

                    String[] synonymsTokens = synonyms.split("•");

                    for (String synonymsToken : synonymsTokens) {

                        if(synonymsToken.isEmpty() || synonymsToken.equals("\"")) {
                            continue;
                        }

                        term = StringUtils.normalizeSpaces(synonymsToken.split("-")[1]).trim();
                        descriptionType = DescriptionType.SYNONYMOUS;

                        Description description = new Description(conceptSMTK, term, descriptionType);
                        description.setCaseSensitive(caseSensitive);
                        description.setCreatorUser(user);
                        description.setAutogeneratedName(autogenerated);

                        if(descriptionMap.containsKey(description.getTerm())) {
                            SMTKLoader.logWarning(new LoadLog("Término repetido para descripción "+description.toString()+". Se descarta descripción", INFO));
                        }
                        else {
                            descriptionMap.put(description.getTerm(), description);
                            conceptSMTK.addDescription(description);
                        }
                    }
                }

                descriptionMap.clear();

                newConcept = new ConceptSMTKWeb(conceptSMTK);

                break;
        }
    }

    public void addRelationship(Relationship relationship, String type) throws LoadException {

        String conceptID = StringUtils.normalizeSpaces(tokens[fields.get("CONCEPTO_ID")]).trim();

        // Este método agrega la nueva relación de acuerdo al tipo de acción indicado
        // Previamente se debe validar que la definición sea actualizable. Esto debe ser manejado en las clases especificas
        switch (type) {
            case "M": // Si es de tipo mantención no se hace nada
                break;
            case "A": // Si es de tipo actualización
                // Si la multiplicidad es 1
                if(relationship.getRelationshipDefinition().getMultiplicity().isSimple()) {
                    // Si existen relaciones para esta definición
                    List<RelationshipWeb> relationships = newConcept.getValidRelationshipsWebByRelationDefinition(relationship.getRelationshipDefinition());

                    if(!relationships.isEmpty()) {
                        if(!relationships.contains(relationship)) {
                            // Modificar el target de la relación
                            relationships.get(0).setTarget(relationship.getTarget());
                            String msg = "Se modifica destino de relación para la definición '" + relationship.getRelationshipDefinition().getName() + "' de '" + relationships.get(0).getTarget().toString() + "' a '" + relationship.getTarget().toString() +"'";
                            log(new LoadException(dataFile, conceptID, msg, INFO, type));
                        }
                        else {
                            String msg = "Relación '" + relationship.toString() + "' se mantiene";
                            log(new LoadException(dataFile, conceptID, msg, INFO, type));
                        }
                    }
                    else {
                        // Agregar la relación
                        String msg = "Se agrega relación '" + relationship.toString() + "'";
                        log(new LoadException(dataFile, conceptID, msg, INFO, type));
                        newConcept.addRelationshipWeb(new RelationshipWeb(relationship, relationship.getRelationshipAttributes()));
                    }
                }
                // Si la multiplicidad es N
                else {
                    if(!newConcept.getRelationships().contains(relationship)) {
                        if(!isUpdateable(relationship)) {
                            String message = "Relación '" + relationship.toString() + "' la definición no es actualizable";
                            throw new LoadException(dataFile, conceptID, message, ERROR, type);
                        }
                        String msg = "Se agrega relación '" + relationship.toString() + "'";
                        log(new LoadException(dataFile, conceptID, msg, INFO, type));
                        newConcept.addRelationshipWeb(new RelationshipWeb(relationship, relationship.getRelationshipAttributes()));
                    }
                    else {
                        String msg = "Relación '" + relationship.toString() + "' se mantiene";
                        log(new LoadException(dataFile, conceptID, msg, INFO, type));
                    }
                }
                break;
            case "N": // Si es de tipo creación
                if(!newConcept.getValidRelationshipsWebByRelationDefinition(relationship.getRelationshipDefinition()).isEmpty()) {
                    // Si ya existe una relacion para esta definicion lanzar excepción
                    String message = "Ya existe una relación para la definición: '" + relationship.getRelationshipDefinition().getName() + "'";
                    throw new LoadException(dataFile, conceptID, message, ERROR, type);
                }
                // Agregar la relación
                String msg = "Se agrega relación '" + relationship.toString() + "'";
                log(new LoadException(dataFile, conceptID, msg, INFO, type));
                newConcept.addRelationshipWeb(new RelationshipWeb(relationship, relationship.getRelationshipAttributes()));
                break;
        }
    }

    public boolean isUpdateable(Relationship relationship) {
        if(nonUpdateableDefinitions.contains(relationship.getRelationshipDefinition())) {
            return false;
        }
        return true;
    }

    public void addConcept(String type) {

        String conceptID = StringUtils.normalizeSpaces(tokens[fields.get("CONCEPTO_ID")]).trim();

        // Este método agrega el concepto de acuerdo al tipo de acción indicado
        switch (type) {
            case "M": // Si es de tipo mantención no se hace nada
                break;
            case "A": // Si es de tipo actualización
                String msg = "Carga Actualización Concepto '" + conceptID + "' OK";
                log(new LoadException(dataFile, conceptID, msg, LoadException.INFO, type));
                conceptsForUpdate.put(conceptID, new Pair<>(oldConcept, newConcept));
                break;
            case "N": // Si es de tipo creación
                msg = "Carga Nuevo Concepto '" + conceptID + "' OK";
                log(new LoadException(dataFile, conceptID, msg, LoadException.INFO, type));
                conceptsForPersist.put(conceptID, newConcept);
                break;
        }
    }

    public abstract void loadConceptFromFileLine(String line) throws LoadException;

    public void loadAllConcepts(SMTKLoader smtkLoader) {

        smtkLoader.printInfo(new LoadLog("Comprobando Conceptos " + category, INFO));

        String conceptID = "";

        smtkLoader.setConceptsProcessed(0);

        try {

            initReader(dataFile);
            initWriter(category.getName());

            //conceptID = StringUtils.normalizeSpaces(tokens[fields.get("CONCEPTO_ID")]).trim();

            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    loadConceptFromFileLine(line);
                    smtkLoader.incrementConceptsProcessed(1);
                }
                catch (LoadException e) {
                    //smtkLoader.logError(e);
                    log(e);
                    e.printStackTrace();
                }
            }

            haltReader();

            smtkLoader.printTick();

        } catch (Exception e) {
            //smtkLoader.printError(new LoadException(path.toString(), "", e.getMessage(), ERROR));
            log(new LoadException(path.toString(), conceptID, e.getMessage(), ERROR));
            //e.printStackTrace();
        } catch (LoadException e) {
            //e.printStackTrace();
            log(e);
        }
    }

    public void persistAllConcepts(SMTKLoader smtkLoader) {

        smtkLoader.printInfo(new LoadLog("Persisitiendo Conceptos " + category, INFO));
        String conceptID = StringUtils.normalizeSpaces(tokens[fields.get("CONCEPTO_ID")]).trim();
        String type = StringUtils.normalizeSpaces(tokens[fields.get("TIPO")]).trim();

        smtkLoader.setConceptsProcessed(0);

        Iterator it = conceptsForPersist.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry) it.next();

            try {
                String msg = "Persistiendo Concepto '" + conceptID + "'";
                log(new LoadException(dataFile, conceptID, msg, INFO, "N"));
                conceptManager.persist((ConceptSMTK) pair.getValue(), smtkLoader.getUser());
                smtkLoader.incrementConceptsProcessed(1);
            }
            catch (Exception e) {
                LoadException ex = new LoadException(path.toString(), pair.getKey().toString(), e.getMessage(), ERROR, "N");
                log(ex);
                smtkLoader.printError(ex);
                //e.printStackTrace();
            }

            it.remove(); // avoids a ConcurrentModificationException
        }

        it = conceptsForUpdate.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry) it.next();

            try {
                String msg = "Actualizando Concepto '" + conceptID + "'";
                log(new LoadException(dataFile, conceptID, msg, INFO, "A"));

                Pair<ConceptSMTK, ConceptSMTK> concepts = (Pair<ConceptSMTK, ConceptSMTK>)pair.getValue();

                conceptManager.update(concepts.getFirst(), concepts.getSecond(), smtkLoader.getUser());

                smtkLoader.incrementConceptsUpdated(1);
            }
            catch (Exception e) {
                //smtkLoader.logError(new LoadException(path.toString(), (Long) pair.getKey(), e.getMessage(), ERROR));
                log(new LoadException(path.toString(), conceptID, e.getMessage(), ERROR, "A"));
                //e.printStackTrace();
            }

            it.remove(); // avoids a ConcurrentModificationException
        }

        smtkLoader.printTick();
    }

    public void processConcepts(SMTKLoader smtkLoader) {
        smtkLoader.setProcessed(0);
        loadAllConcepts(smtkLoader);
        persistAllConcepts(smtkLoader);
    }

}
