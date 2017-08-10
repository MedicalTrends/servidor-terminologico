package cl.minsal.semantikos.loaders;

import cl.minsal.semantikos.clients.RemoteEJBClientFactory;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.DescriptionManager;
import cl.minsal.semantikos.kernel.components.HelperTablesManager;
import cl.minsal.semantikos.kernel.components.SnomedCTManager;
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
import cl.minsal.semantikos.util.StringUtils;
import com.sun.org.apache.bcel.internal.generic.BasicType;

import java.util.*;

import static cl.minsal.semantikos.model.LoadLog.ERROR;
import static cl.minsal.semantikos.model.LoadLog.INFO;
import static cl.minsal.semantikos.model.relationships.SnomedCTRelationship.ES_UN;

/**
 * Created by root on 15-06-17.
 */
public class SubstanceConceptLoader extends EntityLoader {

    ConceptManager conceptManager = (ConceptManager) RemoteEJBClientFactory.getInstance().getManager(ConceptManager.class);
    DescriptionManager descriptionManager = (DescriptionManager) RemoteEJBClientFactory.getInstance().getManager(DescriptionManager.class);
    SnomedCTManager snomedCTManager = (SnomedCTManager) RemoteEJBClientFactory.getInstance().getManager(SnomedCTManager.class);
    HelperTablesManager helperTableManager = (HelperTablesManager) RemoteEJBClientFactory.getInstance().getManager(HelperTablesManager.class);

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

    Map<Long, ConceptSMTK> conceptSMTKMap = new HashMap<>();

    Map<String, Description> descriptionMap = new HashMap<>();


    public void loadConceptFromFileLine(String line, User user) throws LoadException {

        String[] tokens = line.split(separator,-1);

        /*Recuperando datos Concepto*/

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        long id = Long.parseLong(tokens[substanceConceptFields.get("CONCEPTO_ID")]);

        try {

            boolean toBeReviewed = tokens[substanceConceptFields.get("REVISADO")].equals("Si");
            boolean toBeConsulted = tokens[substanceConceptFields.get("CONSULTAR")].equals("Si");
            boolean autogenerated = tokens[substanceConceptFields.get("CREAC_NOMBRE")].equals("Autogenerado");
            String observation = tokens[substanceConceptFields.get("OBSERVACION")];

            Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Sustancia");
            TagSMTK tagSMTK = TagSMTKFactory.getInstance().findTagSMTKByName("sustancia");

            ConceptSMTK conceptSMTK = new ConceptSMTK(category);
            conceptSMTK.setToBeConsulted(toBeConsulted);
            conceptSMTK.setToBeReviewed(toBeReviewed);
            conceptSMTK.setCategory(category);
            conceptSMTK.setTagSMTK(tagSMTK);
            conceptSMTK.setObservation(observation);

            /*Recuperando datos Descripciones*/

            /*Recuperando descripcion preferida*/
            String term = StringUtils.normalizeSpaces(tokens[substanceConceptFields.get("DESCRIPCION")]);
            boolean caseSensitive = tokens[substanceConceptFields.get("SENSIBLE_MAYUSCULA")].equals("Sensible");
            DescriptionType descriptionType = DescriptionType.PREFERIDA;

            Description descriptionFavourite = new Description(conceptSMTK, term, descriptionType);
            descriptionFavourite.setCaseSensitive(caseSensitive);
            descriptionFavourite.setCreatorUser(user);
            descriptionFavourite.setAutogeneratedName(autogenerated);

            if(descriptionMap.containsKey(descriptionFavourite.getTerm())) {
                SMTKLoader.logWarning(new LoadLog("Término repetido para descripción "+descriptionFavourite.toString()+". Se descarta descripción", INFO));
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
            String synonyms = tokens[substanceConceptFields.get("SINONIMO")];

            if (!synonyms.isEmpty()) {

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

            RelationshipDefinition relationshipDefinition;

            /*Recuperando datos Relaciones*/
            String idConceptSCTName = tokens[substanceConceptFields.get("SCT_ID")];

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
                relationshipDefinition = conceptSMTK.getCategory().findRelationshipDefinitionsByName(TargetDefinition.SNOMED_CT).get(0);

                Relationship relationshipSnomed = new Relationship(conceptSMTK, conceptSCT, relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

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

                conceptSMTK.addRelationship(relationshipSnomed);

            }

            BasicTypeValue basicTypeValue = new BasicTypeValue(true);
            relationshipDefinition = conceptSMTK.getCategory().findRelationshipDefinitionsByName(TargetDefinition.COMERCIALIZADO).get(0);

            Relationship relationshipMarketed = new Relationship(conceptSMTK, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

            conceptSMTK.addRelationship(relationshipMarketed);

            conceptSMTKMap.put(id, conceptSMTK);
        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: "+e.toString(), ERROR);
        }
    }

    public void loadAllConcepts(SMTKLoader smtkLoader) {

        smtkLoader.logInfo(new LoadLog("Comprobando Conceptos Fármacos - Sustancia", INFO));

        smtkLoader.setConceptsProcessed(0);

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
            smtkLoader.logError(new LoadException(path.toString(), null, e.getMessage(), ERROR));
            e.printStackTrace();
        } catch (LoadException e) {
            e.printStackTrace();
        }
    }

    public void persistAllConcepts(SMTKLoader smtkLoader) {

        smtkLoader.logInfo(new LoadLog("Persisitiendo Conceptos Fármacos - Sustancia", INFO));

        smtkLoader.setConceptsProcessed(0);

        Iterator it = conceptSMTKMap.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry) it.next();

            try {
                conceptManager.persist((ConceptSMTK)pair.getValue(), smtkLoader.getUser());
                smtkLoader.incrementConceptsProcessed(1);
            }
            catch (Exception e) {
                smtkLoader.logError(new LoadException(path.toString(), (Long) pair.getKey(), e.getMessage(), ERROR));
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
