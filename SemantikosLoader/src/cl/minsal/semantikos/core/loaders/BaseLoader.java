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
import cl.minsal.semantikos.util.StringUtils;

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

    BufferedWriter writer;

    public static String separator = ";";

    private static String newline = System.getProperty("line.separator");

    protected ConceptSMTK oldConcept;

    protected ConceptSMTK newConcept;

    protected Map<String, ConceptSMTK> conceptsForPersist;

    protected Map<String, Pair<ConceptSMTK, ConceptSMTK>> conceptsForUpdate;

    protected String[] tokens;

    public static Map<String, Integer> fields = new HashMap<>();

    protected static List<RelationshipDefinition> nonUpdateableDefinitions = new ArrayList<>();

    Map<String, Description> descriptionMap = new HashMap<>();

    protected Category category;

    private static final String ROOT = "/datafiles/";
    private static final String ENV = "master/";
    //private static final String ENV = "test/";

    public static final String dataFile = ROOT + ENV + "TFC.txt";

    public BaseLoader(User user) {
        this.user = user;
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

    public boolean assertHeader(List<String> fields, List<String> header) {
        for (String field : fields) {
            if(!header.contains(field)) {
                return false;
            }

        }
        return true;
    }

    public void initWriter(String path) throws LoadException {

        try {
            fw = new FileWriter(path);

            writer = new BufferedWriter(fw);
            writer.write("CONCEPTO_ID;STATUS;MENSAJE");
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

    public void log(LoadException ex) {
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

            logger.log(level, ex.getIdConcept() + separator + ex.getAction() + separator + ex.getType() + separator + ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void verifyGeneralMARules(String type) throws LoadException {

        String conceptID = StringUtils.normalizeSpaces(tokens[fields.get("CONCEPTO_ID")]).trim();
        ConceptSMTK conceptSMTK = conceptManager.getConceptByCONCEPT_ID(conceptID);

        if(conceptManager.getConceptByCONCEPT_ID(conceptID) == null) {
            String message = "No existe un concepto de conceptID = " + conceptID;
            throw new LoadException(dataFile, conceptID, message, LoadException.ERROR, type);
        }
        else {
            /*Recuperando descripcion preferida*/
            String term = StringUtils.normalizeSpaces(tokens[fields.get("DESCRIPCION")]).trim();

            if(conceptSMTK.getDescriptionFavorite().getTerm().equals(term)) {
                String message = "La descripción '" + term + "' no coincide con la descripción preferida '" + conceptSMTK.getDescriptionFavorite().getTerm() + "'";
                throw new LoadException(dataFile, conceptID, message, LoadException.ERROR, type);
            }
        }
    }

    public void init(String type, Category category, String term) throws LoadException {

        this.category = category;

        switch (type) {
            case "M":
                //...solo Verificar concepto y salir
                verifyGeneralMARules(type);
                break;
            case "A":
                /*Si es una actualización*/
                verifyGeneralMARules(type);

                //Se buscan descripciones con glosa suministrada
                List<Description> descriptions = descriptionManager.searchDescriptionsPerfectMatch(term, Arrays.asList(new Category[]{category}), null);

                if(!descriptions.isEmpty()) {
                    oldConcept = descriptions.get(0).getConceptSMTK();
                    oldConcept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(oldConcept));

                    //Se crea una copia idéntica del concepto original
                    newConcept = new ConceptSMTKWeb(oldConcept);
                }
                else {
                    // Si no se encuentran Lanzar excepción
                    String message = "No existe un concepto con descripción preferida '" + term + "'";
                    String conceptID = StringUtils.normalizeSpaces(tokens[fields.get("CONCEPTO_ID")]).trim();
                    throw new LoadException(dataFile, conceptID, message, LoadException.ERROR);
                }
                break;
            case "N":
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

                newConcept = new ConceptSMTK(category);
                newConcept.setToBeConsulted(toBeConsulted);
                newConcept.setToBeReviewed(toBeReviewed);
                newConcept.setCategory(category);
                newConcept.setTagSMTK(tagSMTK);

                /*Recuperando datos Descripciones*/
                boolean caseSensitive = tokens[fields.get("SENSIBLE_MAYUSCULA")].equals("Sensible");
                DescriptionType descriptionType = DescriptionType.PREFERIDA;

                Description descriptionFavourite = new Description(newConcept, term, descriptionType);
                descriptionFavourite.setCaseSensitive(caseSensitive);
                descriptionFavourite.setCreatorUser(user);
                descriptionFavourite.setAutogeneratedName(autogenerated);

                if(descriptionMap.containsKey(descriptionFavourite.getTerm())) {
                    SMTKLoader.logWarning(new LoadLog("Término repetido para descripción " + descriptionFavourite.toString() + ". Se descarta descripción", INFO));
                }
                else {
                    descriptionMap.put(descriptionFavourite.getTerm(), descriptionFavourite);
                    newConcept.addDescription(descriptionFavourite);
                }

                /*Recuperando descripcion FSN*/
                term = descriptionFavourite.getTerm() + " (" + tagSMTK.getName() + ")";
                descriptionType = DescriptionType.FSN;

                Description descriptionFSN = new Description(newConcept, term, descriptionType);
                descriptionFSN.setCaseSensitive(caseSensitive);
                descriptionFSN.setCreatorUser(user);
                descriptionFSN.setAutogeneratedName(autogenerated);

                if(descriptionMap.containsKey(descriptionFSN.getTerm())) {
                    SMTKLoader.logWarning(new LoadLog("Término repetido para descripción "+descriptionFSN.toString()+". Se descarta descripción", INFO));
                }
                else {
                    descriptionMap.put(descriptionFSN.getTerm(), descriptionFSN);
                    newConcept.addDescription(descriptionFSN);
                }

                /*Recuperando Sinónimos*/
                String synonyms = tokens[fields.get("SINONIMO")];

                if (!synonyms.isEmpty()) {

                    String[] synonymsTokens = synonyms.split("•");

                    for (String synonymsToken : synonymsTokens) {

                        if(synonymsToken.isEmpty() || synonymsToken.equals("\"")) {
                            continue;
                        }

                        term = StringUtils.normalizeSpaces(synonymsToken.split("-")[1]).trim();
                        descriptionType = DescriptionType.SYNONYMOUS;

                        Description description = new Description(newConcept, term, descriptionType);
                        description.setCaseSensitive(caseSensitive);
                        description.setCreatorUser(user);
                        description.setAutogeneratedName(autogenerated);

                        if(descriptionMap.containsKey(description.getTerm())) {
                            SMTKLoader.logWarning(new LoadLog("Término repetido para descripción "+description.toString()+". Se descarta descripción", INFO));
                        }
                        else {
                            descriptionMap.put(description.getTerm(), description);
                            newConcept.addDescription(description);
                        }
                    }
                }

                descriptionMap.clear();
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
                    List<Relationship> relationships = newConcept.getRelationshipsByRelationDefinition(relationship.getRelationshipDefinition());

                    if(!relationships.isEmpty()) {
                        // Modificar el target de la relación
                        relationships.get(0).setTarget(relationship.getTarget());
                        String msg = "Se modifica destinto de relación para la definición '" + relationship.getRelationshipDefinition().getName() + "' de '" + relationships.get(0).getTarget().toString() + "' a '" + relationship.getTarget().toString() +"'";
                        log(new LoadException(dataFile, conceptID, msg, INFO, type));
                    }
                    else {
                        // Agregar la relación
                        String msg = "Se agrega relación '" + relationship.toString() + "'";
                        log(new LoadException(dataFile, conceptID, msg, INFO, type));
                        newConcept.getRelationships().add(relationship);
                    }
                }
                // Si la multiplicidad es N
                else {
                    if(!newConcept.getRelationships().contains(relationship)) {
                        if(!isUpdateable(relationship)) {
                            String message = "Relación '" + relationship.toString() + "' la definición no es actualizable";
                            throw new LoadException(dataFile, conceptID, message, ERROR, type);
                        }
                    }
                    String msg = "Se agrega relación '" + relationship.toString() + "'";
                    log(new LoadException(dataFile, conceptID, msg, INFO, type));
                    newConcept.getRelationships().add(relationship);
                }
                break;
            case "N": // Si es de tipo creación
                if(!newConcept.getRelationshipsByRelationDefinition(relationship.getRelationshipDefinition()).isEmpty()) {
                    // Si ya existe una relacion para esta definicion lanzar excepción
                    String message = "Ya existe una relación para la definición: '" + relationship.getRelationshipDefinition().getName() + "'";
                    throw new LoadException(dataFile, conceptID, message, ERROR, type);
                }
                // Agregar la relación
                String msg = "Se agrega relación '" + relationship.toString() + "'";
                log(new LoadException(dataFile, conceptID, msg, INFO, type));
                newConcept.getRelationships().add(relationship);
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
                conceptsForUpdate.put(conceptID, new Pair<>(oldConcept, newConcept));
                break;
            case "N": // Si es de tipo creación
                conceptsForPersist.put(conceptID, newConcept);
                break;
        }
    }

    public abstract void loadConceptFromFileLine(String line) throws LoadException;

    public void loadAllConcepts(SMTKLoader smtkLoader) {

        smtkLoader.printInfo(new LoadLog("Comprobando Conceptos " + category, INFO));

        smtkLoader.setConceptsProcessed(0);

        try {

            initReader(smtkLoader.SUBSTANCE_PATH);
            initWriter(category.getName());

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
            log(new LoadException(path.toString(), "", e.getMessage(), ERROR));
            //e.printStackTrace();
        } catch (LoadException e) {
            //e.printStackTrace();
            log(e);
        }
    }

    public void persistAllConcepts(SMTKLoader smtkLoader) {

        smtkLoader.printInfo(new LoadLog("Persisitiendo Conceptos " + category, INFO));

        smtkLoader.setConceptsProcessed(0);

        Iterator it = conceptsForPersist.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry) it.next();

            try {
                conceptManager.persist((ConceptSMTK)pair.getValue(), smtkLoader.getUser());
                smtkLoader.incrementConceptsProcessed(1);
            }
            catch (Exception e) {
                smtkLoader.printError(new LoadException(path.toString(), pair.getKey().toString(), e.getMessage(), ERROR));
                e.printStackTrace();
            }

            it.remove(); // avoids a ConcurrentModificationException
        }

        it = conceptsForUpdate.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry) it.next();

            try {
                Pair<ConceptSMTK, ConceptSMTK> concepts = (Pair<ConceptSMTK, ConceptSMTK>)pair.getValue();

                conceptManager.update(concepts.getFirst(), concepts.getSecond(), smtkLoader.getUser());

                smtkLoader.incrementConceptsUpdated(1);
            }
            catch (Exception e) {
                //smtkLoader.logError(new LoadException(path.toString(), (Long) pair.getKey(), e.getMessage(), ERROR));
                log(new LoadException(path.toString(), (Long) pair.getKey(), e.getMessage(), ERROR));
                e.printStackTrace();
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
