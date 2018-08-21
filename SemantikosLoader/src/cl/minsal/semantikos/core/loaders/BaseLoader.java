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
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import cl.minsal.semantikos.modelweb.Pair;
import cl.minsal.semantikos.util.StringUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static cl.minsal.semantikos.model.LoadLog.ERROR;
import static cl.minsal.semantikos.model.LoadLog.INFO;

/**
 * Created by root on 08-06-17.
 */
public class BaseLoader {

    private static final Logger logger = Logger.getLogger(BaseLoader.class.getName() );

    Path path;

    BufferedReader reader;

    FileWriter fw;

    BufferedWriter writer;

    public static String separator = ";";

    private static String newline = System.getProperty("line.separator");

    protected ConceptSMTK oldConcept;

    protected ConceptSMTK newConcept;

    protected Map<Long, ConceptSMTK> conceptsForPersist;

    protected Map<Long, Pair<ConceptSMTK, ConceptSMTK>> conceptsForUpdate;

    protected String[] tokens;

    Map<String, Description> descriptionMap = new HashMap<>();

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
                throw new LoadException(path, null, "Archivo sin cabecera!!", ERROR);
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
            writer.write("ID_CONCEPT;CAUSA");
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
            if(ex.getMessage()!=null) {
                writer.write(ex.getIdConcept() + separator + ex.getMessage());
            }
            else {
                writer.write(ex.getIdConcept() + separator + ex.getMessage());
            }

            writer.write(newline);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void brExistsConceptID(ConceptSMTK conceptSMTK) {

    }

    public void brConsistentConceptIDPreferedTerm(ConceptSMTK conceptSMTK) {

    }

    public void init(String type, Category category, String term, Map<String, Integer> fields) {
        switch (type) {
            case "M":
                //...Verificar concepto y salir
                break;
            case "A":
                /*Si es una actualización*/
                List<Description> descriptions = descriptionManager.searchDescriptionsPerfectMatch(term, Arrays.asList(new Category[]{category}), null);

                if(!descriptions.isEmpty()) {
                    oldConcept = descriptions.get(0).getConceptSMTK();
                    oldConcept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(oldConcept));

                    //Se crea una copia idéntica del concepto original
                    newConcept = new ConceptSMTK(oldConcept.getId(), oldConcept.getConceptID(), oldConcept.getCategory(),
                            oldConcept.isToBeReviewed(), oldConcept.isToBeConsulted(), oldConcept.isModeled(),
                            oldConcept.isFullyDefined(), oldConcept.isInherited(), oldConcept.isPublished(),
                            oldConcept.getObservation(), oldConcept.getTagSMTK());

                    newConcept.setDescriptions(oldConcept.getDescriptions());
                    newConcept.setRelationships(oldConcept.getRelationships());
                }
                break;
            case "N":
                boolean toBeReviewed = tokens[fields.get("REVISADO")].equals("Si");
                boolean toBeConsulted = tokens[fields.get("CONSULTAR")].equals("Si");
                boolean autogenerated = tokens[fields.get("CREAC_NOMBRE")].equals("Autogenerado");

                TagSMTK tagSMTK = TagSMTKFactory.getInstance().findTagSMTKByName("sustancia");

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
                    SMTKLoader.logWarning(new LoadLog("Término repetido para descripción "+descriptionFavourite.toString()+". Se descarta descripción", INFO));
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

                descriptionMap.clear();
                break;
        }
    }

    public void addRelationship(Relationship relationship, String type) {
        switch (type) {
            case "M":
                break;
            case "A":
                break;
            case "N":
                break;
        }
    }

}
