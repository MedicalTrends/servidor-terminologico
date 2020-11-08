package cl.minsal.semantikos.loaders;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.LoadException;
import cl.minsal.semantikos.model.LoadLog;
import cl.minsal.semantikos.model.SMTKLoader;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionType;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.modelweb.Pair;
import cl.minsal.semantikos.util.StringUtils;
import com.sun.org.apache.bcel.internal.generic.BasicType;

import java.sql.Timestamp;
import java.util.*;

import static cl.minsal.semantikos.model.LoadLog.ERROR;
import static cl.minsal.semantikos.model.LoadLog.INFO;
import static cl.minsal.semantikos.model.relationships.SnomedCTRelationship.ES_UN;

/**
 * Created by root on 15-06-17.
 */
public class SubstanceConceptLoader extends EntityLoader {

    ConceptManager conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);
    DescriptionManager descriptionManager = (DescriptionManager) ServiceLocator.getInstance().getService(DescriptionManager.class);
    RelationshipManager relationshipManager = (RelationshipManager) ServiceLocator.getInstance().getService(RelationshipManager.class);
    SnomedCTManager snomedCTManager = (SnomedCTManager) ServiceLocator.getInstance().getService(SnomedCTManager.class);
    HelperTablesManager helperTableManager = (HelperTablesManager) ServiceLocator.getInstance().getService(HelperTablesManager.class);

    public static final Map<String, Integer> substanceConceptFields;
    static
    {
        substanceConceptFields = new HashMap<String, Integer>();
        substanceConceptFields.put("CONCEPTO_ID", 0);
        substanceConceptFields.put("DESCRIPCION", 1);
        substanceConceptFields.put("DESC_ABREVIADA", 2);
        substanceConceptFields.put("SENSIBLE_MAYUSCULA", 3);
        substanceConceptFields.put("CREAC_NOMBRE", 4);
        substanceConceptFields.put("ESTADO", 5);
        substanceConceptFields.put("REVISADO", 6);
        substanceConceptFields.put("CONSULTAR", 7);
        substanceConceptFields.put("OBSERVACION", 8);
        substanceConceptFields.put("SINONIMO", 9);
        substanceConceptFields.put("SCT_ID", 10);
        substanceConceptFields.put("SCT_TERMINO", 11);
        substanceConceptFields.put("DCI_DID", 12);
        substanceConceptFields.put("DCI_TERMINO", 13);
        substanceConceptFields.put("GRUPOS_JERARQUICOS", 14);
        substanceConceptFields.put("RIESGO_TERATOGENICO", 15);
    }

    Map<String, Pair<ConceptSMTK, ConceptSMTK>> conceptSMTKMap = new HashMap<>();

    Map<String, Description> descriptionMap = new HashMap<>();


    public void loadConceptFromFileLine(String line, User user) throws LoadException {

        String[] tokens = line.split(separator,-1);

        /*Recuperando datos Concepto*/

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        String id = tokens[substanceConceptFields.get("CONCEPTO_ID")];

        try {

            Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Sustancia");
            /*Recuperando datos Descripciones*/

            /*Recuperando descripcion preferida*/
            String term = StringUtils.normalizeSpaces(tokens[substanceConceptFields.get("DESCRIPCION")]).trim();
            HelperTable helperTable;
            RelationshipDefinition relationshipDefinition;

            List<Description> descriptions = descriptionManager.searchDescriptionsPerfectMatch(term, Arrays.asList(category), null, Arrays.asList(DescriptionTypeFactory.getInstance().getFavoriteDescriptionType()));

            if(!descriptions.isEmpty()) {

                ConceptSMTK concept = descriptions.get(0).getConceptSMTK();
                concept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(concept));

                //Se crea una copia idéntica del concepto original
                ConceptSMTK _concept = new ConceptSMTK(concept.getId(), concept.getConceptID(), concept.getCategory(),
                                                        concept.isToBeReviewed(), concept.isToBeConsulted(), concept.isModeled(),
                                                        concept.isFullyDefined(), concept.isInherited(), concept.isPublished(),
                                                        concept.getObservation(), concept.getTagSMTK());

                _concept.setDescriptions(concept.getDescriptions());
                _concept.setRelationships(concept.getRelationships());

                /* Se agrega la relación faltante: DCI */
                String dciName = tokens[substanceConceptFields.get("DCI_TERMINO")];

                if(!StringUtils.isEmpty(dciName)) {

                    relationshipDefinition = category.findRelationshipDefinitionsByName("Mapear a DCI").get(0);

                    if(!concept.getRelationshipsByRelationDefinition(relationshipDefinition).isEmpty()) {
                        throw new LoadException(path.toString(), id, "Concepto: "+concept.toString()+" ya posee DCI, se descarta", INFO);
                    }

                    helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                    List<HelperTableRow> dci = helperTableManager.searchRows(helperTable, dciName);

                    if(dci.isEmpty()) {
                        throw new LoadException(path.toString(), id, "No existe un dci con glosa: "+dciName, ERROR);
                    }

                    Relationship relationshipDCI = new Relationship(concept, dci.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                    concept.addRelationship(relationshipDCI);
                }

                conceptSMTKMap.put(id, new Pair<>(_concept, concept));

            }

        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: "+e.toString(), ERROR);
        }
    }

    public void loadAllConcepts(SMTKLoader smtkLoader) {

        smtkLoader.logInfo(new LoadLog("Comprobando Conceptos Fármacos - Sustancia", INFO));

        try {

            initReader(smtkLoader.SUBSTANCE_PATH);

            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    loadConceptFromFileLine(line, smtkLoader.getUser());
                    smtkLoader.incrementConceptsProcessed(1);
                }
                catch (LoadException e) {
                    smtkLoader.logError(e);
                    e.printStackTrace();
                }
            }

            haltReader();

            smtkLoader.logTick();

        } catch (Exception e) {
            smtkLoader.logError(new LoadException(path.toString(), "", e.getMessage(), ERROR));
            e.printStackTrace();
        } catch (LoadException e) {
            e.printStackTrace();
        }
    }

    public void persistAllConcepts(SMTKLoader smtkLoader) {

        smtkLoader.logInfo(new LoadLog("Actualizando Conceptos Fármacos - Sustancia", INFO));

        Iterator it = conceptSMTKMap.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry) it.next();

            try {
                Pair<ConceptSMTK, ConceptSMTK> concepts = (Pair<ConceptSMTK, ConceptSMTK>) pair.getValue();

                conceptManager.update(concepts.getFirst(), concepts.getSecond(), smtkLoader.getUser());

                smtkLoader.incrementConceptsUpdated(1);
            }
            catch (Exception e) {
                smtkLoader.logError(new LoadException(path.toString(), pair.getKey().toString(), e.getMessage(), ERROR));
                e.printStackTrace();
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
