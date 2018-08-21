package cl.minsal.semantikos.core.loaders;

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
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;

import static cl.minsal.semantikos.model.LoadLog.ERROR;
import static cl.minsal.semantikos.model.LoadLog.INFO;
import static cl.minsal.semantikos.model.relationships.SnomedCTRelationship.ES_UN;

/**
 * Created by root on 15-06-17.
 */
public class SubstanceLoader extends BaseLoader {

    public static final Map<String, Integer> substanceFields;
    static
    {
        substanceFields = new HashMap<String, Integer>();
        substanceFields.put("CONCEPTO_ID", 0);
        substanceFields.put("DESCRIPCION", 1);
        substanceFields.put("TIPO", 2);
        substanceFields.put("DESC_ABREVIADA", 3);
        substanceFields.put("SENSIBLE_MAYUSCULA", 4);
        substanceFields.put("CREAC_NOMBRE", 5);
        substanceFields.put("ESTADO", 6);
        substanceFields.put("REVISADO", 7);
        substanceFields.put("SCT_ID", 8);
        substanceFields.put("SCT_TERMINO", 9);
    }

    Map<Long, ConceptSMTK> conceptSMTKMap = new HashMap<>();


    public void loadConceptFromFileLine(String line, User user) throws LoadException {

        tokens = line.split(separator,-1);

        /*Recuperando datos Concepto*/

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        long id = Long.parseLong(tokens[substanceFields.get("CONCEPTO_ID")]);

        //ConceptSMTK oldConcept;
        //ConceptSMTK newConcept;

        try {

            /*Estableciendo categoría*/
            Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Sustancia");

            /*Recuperando tipo*/
            String type = tokens[substanceFields.get("TIPO")];

            /*Recuperando ConceptID*/
            String conceptID = StringUtils.normalizeSpaces(tokens[substanceFields.get("CONCEPTO_ID")]).trim();

            /*Recuperando descripcion preferida*/
            String term = StringUtils.normalizeSpaces(tokens[substanceFields.get("DESCRIPCION")]).trim();

            init(type, category, term, substanceFields);

            RelationshipDefinition relationshipDefinition;

            /*Recuperando datos Relaciones*/
            String idConceptSCTName = tokens[substanceFields.get("SCT_ID")];

            /*Por defecto se mapea a un concepto SNOMED Genérico*/
            long idConceptSCT = 373873005;

            if(!idConceptSCTName.isEmpty()) {
                idConceptSCT = Long.parseLong(idConceptSCTName);
            }

            if(true) {

                String relationshipType = ES_UN;

                ConceptSCT conceptSCT = snomedCTManager.getConceptByID(idConceptSCT);

                if (conceptSCT == null) {
                    throw new LoadException(path.toString(), id, "Relación referencia a concepto SCT inexistente", ERROR);
                }

                /**Se obtiene la definición de relacion SNOMED CT**/
                relationshipDefinition = newConcept.getCategory().findRelationshipDefinitionsByName(TargetDefinition.SNOMED_CT).get(0);

                Relationship relationshipSnomed = new Relationship(newConcept, conceptSCT, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                /**Para esta definición, se obtiente el atributo tipo de relación**/
                for (RelationshipAttributeDefinition attDef : relationshipDefinition.getRelationshipAttributeDefinitions()) {

                    if (attDef.isRelationshipTypeAttribute()) {
                        HelperTable helperTable = (HelperTable) attDef.getTargetDefinition();

                        List<HelperTableRow> relationshipTypes = helperTableManager.searchRows(helperTable, relationshipType);

                        RelationshipAttribute ra;

                        if (relationshipTypes.size() == 0) {
                            throw new LoadException(path.toString(), id, "No existe un tipo de relación de nombre: " + relationshipType, ERROR);
                        }

                        ra = new RelationshipAttribute(attDef, relationshipSnomed, relationshipTypes.get(0));
                        relationshipSnomed.getRelationshipAttributes().add(ra);
                    }
                }

                /* Generando Grupo */
                BasicTypeValue group = new BasicTypeValue(0);

                RelationshipAttributeDefinition attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Grupo").get(0);

                RelationshipAttribute ra = new RelationshipAttribute(attDef, relationshipSnomed, group);
                relationshipSnomed.getRelationshipAttributes().add(ra);

                newConcept.addRelationship(relationshipSnomed);

            }

            BasicTypeValue basicTypeValue = new BasicTypeValue(true);
            relationshipDefinition = newConcept.getCategory().findRelationshipDefinitionsByName(TargetDefinition.COMERCIALIZADO).get(0);

            Relationship relationshipMarketed = new Relationship(newConcept, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

            newConcept.addRelationship(relationshipMarketed);

            addConcept();

            conceptSMTKMap.put(id, conceptSMTK);
        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: "+e.toString(), ERROR);
        }
    }

    public void loadAllConcepts(SMTKLoader smtkLoader) {

        //smtkLoader.logInfo(new LoadLog("Comprobando Conceptos Fármacos - Sustancia", INFO));

        smtkLoader.printInfo(new LoadLog("Comprobando Conceptos Fármacos - Sustancia", INFO));

        smtkLoader.setConceptsProcessed(0);

        try {

            initReader(smtkLoader.SUBSTANCE_PATH);
            initWriter("Fármacos - Sustancia");

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

            //smtkLoader.logTick();
            smtkLoader.printTick();

        } catch (Exception e) {
            //smtkLoader.logError(new LoadException(path.toString(), null, e.getMessage(), ERROR));
            smtkLoader.printError(new LoadException(path.toString(), null, e.getMessage(), ERROR));
            e.printStackTrace();
        } catch (LoadException e) {
            e.printStackTrace();
        }
    }

    public void persistAllConcepts(SMTKLoader smtkLoader) {

        //smtkLoader.logInfo(new LoadLog("Persisitiendo Conceptos Fármacos - Sustancia", INFO));
        smtkLoader.printInfo(new LoadLog("Persisitiendo Conceptos Fármacos - Sustancia", INFO));

        smtkLoader.setConceptsProcessed(0);

        Iterator it = conceptSMTKMap.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry) it.next();

            try {
                conceptManager.persist((ConceptSMTK)pair.getValue(), smtkLoader.getUser());
                smtkLoader.incrementConceptsProcessed(1);
            }
            catch (Exception e) {
                //smtkLoader.logError(new LoadException(path.toString(), (Long) pair.getKey(), e.getMessage(), ERROR));
                smtkLoader.printError(new LoadException(path.toString(), (Long) pair.getKey(), e.getMessage(), ERROR));
                e.printStackTrace();
            }

            it.remove(); // avoids a ConcurrentModificationException
        }

        //smtkLoader.logTick();
        smtkLoader.printTick();
    }

    public void processConcepts(SMTKLoader smtkLoader) {
        smtkLoader.setProcessed(0);
        loadAllConcepts(smtkLoader);
        persistAllConcepts(smtkLoader);
    }

}
