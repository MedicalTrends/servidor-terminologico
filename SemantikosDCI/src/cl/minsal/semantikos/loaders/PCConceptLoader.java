package cl.minsal.semantikos.loaders;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.LoadException;
import cl.minsal.semantikos.model.LoadLog;
import cl.minsal.semantikos.model.SMTKLoader;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableColumn;
import cl.minsal.semantikos.model.helpertables.HelperTableData;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.TargetDefinition;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.modelweb.Pair;
import cl.minsal.semantikos.modelweb.RelationshipWeb;
import cl.minsal.semantikos.util.StringUtils;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cl.minsal.semantikos.model.LoadLog.ERROR;
import static cl.minsal.semantikos.model.LoadLog.INFO;
import static cl.minsal.semantikos.model.LoadLog.WARNING;

/**
 * Created by root on 15-06-17.
 */
public class PCConceptLoader extends EntityLoader {

    ConceptManager conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);
    DescriptionManager descriptionManager = (DescriptionManager) ServiceLocator.getInstance().getService(DescriptionManager.class);
    RelationshipManager relationshipManager = (RelationshipManager) ServiceLocator.getInstance().getService(RelationshipManager.class);
    SnomedCTManager snomedCTManager = (SnomedCTManager) ServiceLocator.getInstance().getService(SnomedCTManager.class);
    HelperTablesManager helperTableManager = (HelperTablesManager) ServiceLocator.getInstance().getService(HelperTablesManager.class);
    ISPFetcher ispFetcher = (ISPFetcher) ServiceLocator.getInstance().getService(ISPFetcher.class);

    private static final Logger logger = java.util.logging.Logger.getLogger(PCConceptLoader.class.getName() );

    public static final Map<String, Integer> pcConceptFields;
    static
    {
        pcConceptFields = new LinkedHashMap<>();
        pcConceptFields.put("ConceptID", 0);
        pcConceptFields.put("Term", 1);
        pcConceptFields.put("RegNUM", 2);
        pcConceptFields.put("RegAÑO", 3);
    }

    Map<String, Pair<ConceptSMTK, ConceptSMTK>> conceptSMTKMap = new HashMap<>();

    Map<String, Description> descriptionMap = new HashMap<>();

    private HelperTableRow ispRecord = null;

    private Map<String,String> fetchedData;

    public void loadConceptFromFileLine(String line, User user) throws LoadException, ParseException {

        String[] tokens = line.split(separator,-1);

        /*Recuperando datos Concepto*/

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        String conceptID = StringUtils.normalizeSpaces(tokens[pcConceptFields.get("ConceptID")]).trim();

        try {

            Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Producto Comercial");
            /*Recuperando datos Descripciones*/

            /*Recuperando descripcion preferida*/
            String term = StringUtils.normalizeSpaces(tokens[pcConceptFields.get("Term")]).trim();
            HelperTable helperTable;
            RelationshipDefinition relationshipDefinition;

            //ConceptSMTK concept = conceptManager.getConceptByCONCEPT_ID(conceptID);

            List<Description> descriptions = descriptionManager.searchDescriptionsPerfectMatch(term, Arrays.asList(new Category[]{category}), null);

            if(descriptions.isEmpty()) {
                throw new LoadException(path.toString(), conceptID, "No existe un concepto de preferida '" + term + "'", ERROR);
            }

            ConceptSMTK concept = descriptions.get(0).getConceptSMTK();

            if(!concept.getCategory().equals(category)) {
                throw new LoadException(path.toString(), conceptID, "Concepto " + concept.toString() + " No pertenece a categoría " + category.toString(), ERROR);
            }

            concept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(concept));

            //Se crea una copia idéntica del concepto original
            ConceptSMTK _concept = new ConceptSMTK(concept.getId(), concept.getConceptID(), concept.getCategory(),
                    concept.isToBeReviewed(), concept.isToBeConsulted(), concept.isModeled(),
                    concept.isFullyDefined(), concept.isInherited(), concept.isPublished(),
                    concept.getObservation(), concept.getTagSMTK());

            _concept.setDescriptions(concept.getDescriptions());
            _concept.setRelationships(concept.getRelationships());

            /* Se agrega la relación faltante: ISP */
            /*Recuperando registros ISP*/
            relationshipDefinition = category.findRelationshipDefinitionsByName("ISP").get(0);

            String regNum = tokens[pcConceptFields.get("RegNUM")].replaceAll("\\p{C}", "");

            String regAno = tokens[pcConceptFields.get("RegAÑO")].replaceAll("\\p{C}", "");

            String regnumRegano = regNum + "/" + regAno;

            ispRecord = null;

            helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

            /**
             * Primero se busca un registro isp local
             */
            for (HelperTableRow helperTableRecord : helperTableManager.searchRows(helperTable, regnumRegano)) {
                ispRecord = helperTableRecord;
                break;
            }

            /**
             * Si no existe, se va a buscar a la página del registro isp
             */
            if (ispRecord == null) {

                ispRecord = new HelperTableRow(helperTable);
                int count = 1;

                while (true) {

                    try {
                        //SMTKLoader.logWarning(new LoadLog((count)+"° intento solicitud registro ISP para: "+regnumRegano, INFO));
                        fetchedData = ispFetcher.getISPData(regnumRegano);
                        count++;
                        if (!fetchedData.isEmpty()) {
                            //SMTKLoader.logWarning(new LoadLog("Registro ISP OK para: "+regnumRegano, INFO));
                            break;
                        }
                        if (count == 2) {
                            throw new LoadException(path.toString(), conceptID, "Registro ISP Falló para: " + regnumRegano, ERROR);
                        }
                    } catch (Exception e) {
                        // handle exception
                        SMTKLoader.logWarning(new LoadLog((count + 1) + "° intento solicitud registro ISP para: " + regnumRegano, INFO));
                    }
                }

                if (!fetchedData.isEmpty()) {
                    mapIspRecord(helperTable, ispRecord, fetchedData);
                    helperTableManager.insertRow(ispRecord, user.getEmail());
                    ispRecord = helperTableManager.searchRows(helperTable, regnumRegano).get(0);
                    Relationship relationshipISP = new Relationship(concept, ispRecord, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                    for (Relationship rel : concept.getRelationshipsByRelationDefinition(category.findRelationshipDefinitionsByName(TargetDefinition.ISP).get(0))) {

                        HelperTableRow relIspRecord = (HelperTableRow) rel.getTarget();

                        String regnumBD = relIspRecord.getDescription().split("/")[0];
                        String reganoBD = relIspRecord.getDescription().split("/")[1];

                        // Si es el mismo regNum se modifica el destino de la relación
                        if(regNum.equals(regnumBD) && !regAno.equals(reganoBD)) {
                            log(new LoadException(path.toString(), conceptID, "Concepto '" + concept.toString() + " se modifica relación '" + rel + "' por '" + relationshipISP + "'", INFO, "A"));
                            Relationship r = new RelationshipWeb(rel.getId(), rel, rel.getRelationshipAttributes());
                            r.setTarget(ispRecord);
                            r.setCreationDate(rel.getCreationDate());
                            concept.getRelationships().remove(rel);
                            concept.getRelationships().add(r);
                            break;
                        }

                        // Si es distinto regNum se agrega una nueva relación
                        /*
                        if(!regNum.equals(regnumBD) && !regAno.equals(reganoBD)) {
                            log(new LoadException(path.toString(), conceptID, "Concepto '" + concept.toString() + " se agrega nueva relación '" + relationshipISP + "'", INFO, "N"));
                            concept.addRelationship(relationshipISP);
                            break;
                        }
                        */

                    }

                    // Si el concepto no tiene relaciones a ISP se agrega la relación
                    if(!concept.getRelationships().contains(relationshipISP)) {
                        log(new LoadException(path.toString(), conceptID, "Concepto '" + concept.toString() + " se agrega nueva relación '" + relationshipISP + "'", INFO, "N"));
                        concept.addRelationship(relationshipISP);
                    }

                }

            } else {
                /**
                 * Si se encuentra, se verifica que no exista actualmente una relación con este destino
                 */
                for (Relationship relationship : relationshipManager.findRelationshipsLike(relationshipDefinition, ispRecord)) {

                    if (relationship.getRelationshipDefinition().isISP()) {
                        throw new LoadException(path.toString(), conceptID, "Concepto '" + relationship.getSourceConcept().toString() + " ya tiene una relación a ISP con el destino '" + ispRecord.getDescription() + "'", WARNING, "M");
                    }
                }


                Relationship relationshipISP = new Relationship(concept, ispRecord, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                for (Relationship rel : concept.getRelationshipsByRelationDefinition(category.findRelationshipDefinitionsByName(TargetDefinition.ISP).get(0))) {

                    HelperTableRow relIspRecord = (HelperTableRow) rel.getTarget();

                    String regnumBD = relIspRecord.getDescription().split("/")[0];
                    String reganoBD = relIspRecord.getDescription().split("/")[1];

                    // Si es el mismo regNum se modifica el destino de la relación
                    if(regNum.equals(regnumBD) && !regAno.equals(reganoBD)) {
                        log(new LoadException(path.toString(), conceptID, "Concepto '" + concept.toString() + " se modifica relación '" + rel + "' por '" + relationshipISP + "'", INFO, "A"));
                        Relationship r = new RelationshipWeb(rel.getId(), rel, rel.getRelationshipAttributes());
                        r.setCreationDate(rel.getCreationDate());
                        r.setTarget(ispRecord);
                        concept.getRelationships().remove(rel);
                        concept.getRelationships().add(r);
                        break;
                    }

                    // Si es distinto regNum se agrega una nueva relación
                    /*
                    if(!regNum.equals(regnumBD) && !regAno.equals(reganoBD)) {
                        log(new LoadException(path.toString(), conceptID, "Concepto '" + concept.toString() + " se agrega nueva relación '" + relationshipISP + "'", INFO, "N"));
                        concept.addRelationship(relationshipISP);
                        break;
                    }
                    */

                }

                // Si el concepto no tiene relaciones a ISP se agrega la relación
                if(!concept.getRelationships().contains(relationshipISP)) {
                    log(new LoadException(path.toString(), conceptID, "Concepto '" + concept.toString() + " se agrega nueva relación '" + relationshipISP + "'", INFO, "N"));
                    concept.addRelationship(relationshipISP);
                }

            }

            conceptSMTKMap.put(conceptID, new Pair<>(_concept, concept));


        }
        catch (Exception e) {
            throw new LoadException(path.toString(), conceptID, "Error desconocido: "+e.toString(), ERROR);
        }
    }

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
                            cell.setDateValue(new java.sql.Timestamp(parsedDate.getTime()));
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

    public void loadAllConcepts(SMTKLoader smtkLoader) {

        smtkLoader.logInfo(new LoadLog("Comprobando Conceptos Fármacos - Producto Comercial", INFO));

        try {

            initReader(smtkLoader.ISP_PATH);
            initWriter("ISP.csv");

            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    loadConceptFromFileLine(line, smtkLoader.getUser());
                    smtkLoader.incrementConceptsProcessed(1);
                }
                catch (LoadException e) {
                    smtkLoader.logError(e);
                    log(e);
                    logger.log(Level.SEVERE, e.getDescription());
                    //e.printStackTrace();
                }
            }

            haltReader();

            smtkLoader.logTick();

        } catch (Exception e) {
            //smtkLoader.logError(new LoadException(path.toString(), "", e.getMessage(), ERROR));
            logger.log(Level.SEVERE, e.getMessage());
            //logger.log(Level.SEVERE, e.getMessage());
            //e.printStackTrace();
        } catch (LoadException e) {
            log(new LoadException(path.toString(), "", e.getMessage(), ERROR));
            //e.printStackTrace();
        }
    }

    public void persistAllConcepts(SMTKLoader smtkLoader) {

        smtkLoader.logInfo(new LoadLog("Actualizando Conceptos Fármacos - Producto Comercial", INFO));

        Iterator it = conceptSMTKMap.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry) it.next();

            try {
                Pair<ConceptSMTK, ConceptSMTK> concepts = (Pair<ConceptSMTK, ConceptSMTK>) pair.getValue();

                conceptManager.update(concepts.getFirst(), concepts.getSecond(), smtkLoader.getUser());

                smtkLoader.incrementConceptsUpdated(1);
            }
            catch (Exception e) {
                //smtkLoader.logError(new LoadException(path.toString(), pair.getKey().toString(), e.getMessage(), ERROR));
                log(new LoadException(path.toString(), pair.getKey().toString(), e.getMessage(), ERROR));
                logger.log(Level.SEVERE, e.getMessage());
                //e.printStackTrace();
            }

            it.remove(); // avoids a ConcurrentModificationException
        }

        smtkLoader.logTick();
    }

    public void processConcepts(SMTKLoader smtkLoader) {
        loadAllConcepts(smtkLoader);
        persistAllConcepts(smtkLoader);
    }

}
