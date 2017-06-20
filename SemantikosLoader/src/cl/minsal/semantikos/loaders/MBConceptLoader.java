package cl.minsal.semantikos.loaders;

import cl.minsal.semantikos.clients.RemoteEJBClientFactory;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.LoadException;
import cl.minsal.semantikos.model.LoadLog;
import cl.minsal.semantikos.model.SMTKLoader;
import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionType;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.tags.Tag;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.util.StringUtils;

import java.util.*;

import static cl.minsal.semantikos.model.LoadLog.ERROR;
import static cl.minsal.semantikos.model.LoadLog.INFO;
import static cl.minsal.semantikos.model.relationships.SnomedCTRelationship.ES_UN;
import static java.util.Collections.EMPTY_LIST;

/**
 * Created by root on 15-06-17.
 */
public class MBConceptLoader extends EntityLoader {

    ConceptManager conceptManager = (ConceptManager) RemoteEJBClientFactory.getInstance().getManager(ConceptManager.class);
    DescriptionManager descriptionManager = (DescriptionManager) RemoteEJBClientFactory.getInstance().getManager(DescriptionManager.class);
    TagManager tagManager = (TagManager) RemoteEJBClientFactory.getInstance().getManager(TagManager.class);
    SnomedCTManager snomedCTManager = (SnomedCTManager) RemoteEJBClientFactory.getInstance().getManager(SnomedCTManager.class);
    HelperTablesManager helperTableManager = (HelperTablesManager) RemoteEJBClientFactory.getInstance().getManager(HelperTablesManager.class);

    public static final Map<String, Integer> mbConceptFields;
    static
    {
        mbConceptFields = new HashMap<String, Integer>();
        mbConceptFields.put("CONCEPTO_ID", 0);
        mbConceptFields.put("DESCRIPCION", 1);
        mbConceptFields.put("DESC_ABREVIADA", 2);
        mbConceptFields.put("ESTADO", 3);
        mbConceptFields.put("SENSIBLE_MAYUSCULA", 4);
        mbConceptFields.put("CREAC_NOMBRE", 5);
        mbConceptFields.put("REVISADO", 6);
        mbConceptFields.put("CONSULTAR", 7);
        mbConceptFields.put("SCT_ID", 8);
        mbConceptFields.put("SCT_TERMINO", 9);
        mbConceptFields.put("DCI_DID", 10);
        mbConceptFields.put("DCI_TERMINO", 11);
        mbConceptFields.put("SINONIMO", 12);
        mbConceptFields.put("GRUPOS_JERARQUICOS", 13);
        mbConceptFields.put("SUSTANCIAS", 14);
    }

    Map<Long, ConceptSMTK> conceptSMTKMap = new HashMap<>();


    public void loadConceptFromFileLine(String line, User user) throws LoadException {

        String[] tokens = line.split(separator,-1);

        /*Recuperando datos Concepto*/

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        long id = Long.parseLong(tokens[mbConceptFields.get("CONCEPTO_ID")]);

        try {

            boolean toBeReviewed = tokens[mbConceptFields.get("REVISADO")].equals("Si");
            boolean toBeConsulted = tokens[mbConceptFields.get("CONSULTAR")].equals("Si");
            boolean autogenerated = tokens[mbConceptFields.get("CREAC_NOMBRE")].equals("Autogenerado");

            Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Medicamento Básico");
            TagSMTK tagSMTK = TagSMTKFactory.getInstance().findTagSMTKByName("producto");

            ConceptSMTK conceptSMTK = new ConceptSMTK(category);
            conceptSMTK.setToBeConsulted(toBeConsulted);
            conceptSMTK.setToBeReviewed(toBeReviewed);
            conceptSMTK.setCategory(category);
            conceptSMTK.setTagSMTK(tagSMTK);

            /*Recuperando datos Descripciones*/

            /*Recuperando descripcion preferida*/
            String term = StringUtils.normalizeSpaces(tokens[mbConceptFields.get("DESCRIPCION")]);
            boolean caseSensitive = tokens[mbConceptFields.get("SENSIBLE_MAYUSCULA")].equals("Sensible");
            DescriptionType descriptionType = DescriptionType.PREFERIDA;

            Description descriptionFavourite = new Description(conceptSMTK, term, descriptionType);
            descriptionFavourite.setCaseSensitive(caseSensitive);
            descriptionFavourite.setCreatorUser(user);
            descriptionFavourite.setAutogeneratedName(autogenerated);

            conceptSMTK.addDescription(descriptionFavourite);

            /*Recuperando descripcion FSN*/
            term = descriptionFavourite.getTerm()+" ("+tagSMTK.getName()+")";
            descriptionType = DescriptionType.FSN;

            Description descriptionFSN = new Description(conceptSMTK, term, descriptionType);
            descriptionFSN.setCaseSensitive(caseSensitive);
            descriptionFSN.setCreatorUser(user);
            descriptionFSN.setAutogeneratedName(autogenerated);

            conceptSMTK.addDescription(descriptionFSN);

            /*Recuperando Sinónimos*/
            String synonyms = tokens[mbConceptFields.get("SINONIMO")];

            String[] synonymsTokens = synonyms.split("•");

            if(!synonyms.isEmpty()) {

                for (String synonymsToken : synonymsTokens) {

                    if(synonymsToken.isEmpty() || synonymsToken.equals("\"")) {
                        continue;
                    }

                    term = StringUtils.normalizeSpaces(synonymsToken.split("-")[1]);
                    descriptionType = DescriptionType.SYNONYMOUS;

                    Description description = new Description(conceptSMTK, term, descriptionType);
                    description.setCaseSensitive(caseSensitive);
                    description.setCreatorUser(user);
                    description.setAutogeneratedName(autogenerated);

                    conceptSMTK.addDescription(description);
                }
            }

            /*Recuperando datos Relaciones*/
            String idConceptSCTName = tokens[mbConceptFields.get("SCT_ID")];


            /*Por defecto se mapea a un concepto SNOMED Genérico*/
            long idConceptSCT = 373873005;

            if(!idConceptSCTName.isEmpty()) {
                idConceptSCT = Long.parseLong(idConceptSCTName);
            }

            String relationshipType = ES_UN;

            ConceptSCT conceptSCT = snomedCTManager.getConceptByID(idConceptSCT);

            if(conceptSCT == null) {
                throw new LoadException(path.toString(), id, "Relación referencia a concepto SCT inexistente", ERROR);
            }

            /**Se obtiene la definición de relacion SNOMED CT**/
            RelationshipDefinition relationshipDefinition = RelationshipDefinitionFactory.getInstance().findRelationshipDefinitionByName(TargetDefinition.SNOMED_CT);

            Relationship relationshipSnomed = new Relationship(conceptSMTK, conceptSCT, relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

            /**Para esta definición, se obtiente el atributo tipo de relación**/
            for (RelationshipAttributeDefinition attDef : relationshipDefinition.getRelationshipAttributeDefinitions()) {

                if (attDef.isRelationshipTypeAttribute()) {
                    HelperTable helperTable = (HelperTable) attDef.getTargetDefinition();

                    List<HelperTableRow> relationshipTypes = helperTableManager.searchRows(helperTable, relationshipType);

                    RelationshipAttribute ra;

                    if (relationshipTypes.size() == 0) {
                        throw new LoadException(path.toString(), id, "No existe un tipo de relación de nombre: "+relationshipType, ERROR);
                    }

                    ra = new RelationshipAttribute(attDef, relationshipSnomed, relationshipTypes.get(0));
                    relationshipSnomed.getRelationshipAttributes().add(ra);
                }
            }

            conceptSMTK.addRelationship(relationshipSnomed);

            BasicTypeValue basicTypeValue = new BasicTypeValue(true);
            relationshipDefinition = RelationshipDefinitionFactory.getInstance().findRelationshipDefinitionByName(TargetDefinition.COMERCIALIZADO);

            Relationship relationshipMarketed = new Relationship(conceptSMTK, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

            conceptSMTK.addRelationship(relationshipMarketed);

            relationshipDefinition = category.findRelationshipDefinitionsByName(TargetDefinition.SUSTANCIA).get(0);

            /*Recuperando Sustancias*/
            String substances = tokens[mbConceptFields.get("SUSTANCIAS")];

            String[] substancesTokens = substances.split("•");

            for (String substanceToken : substancesTokens) {

                if(substanceToken.isEmpty() || substanceToken.equals("\"")) {
                    continue;
                }

                String[] substanceTokens = substanceToken.split("¦");

                String termFavourite = StringUtils.normalizeSpaces(substanceTokens[1]).trim();

                List<Description> substanceList = descriptionManager.searchDescriptionsPerfectMatch(termFavourite, Arrays.asList(new Category[]{CategoryFactory.getInstance().findCategoryByName("Fármacos - Sustancia")}), EMPTY_LIST);

                if(substanceList.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe una sustancia con preferida: "+termFavourite, ERROR);
                }

                if(!substanceList.get(0).getConceptSMTK().isModeled()) {
                    throw new LoadException(path.toString(), id, "La sustancia: "+termFavourite+" no está modelada, se descarta este MB", ERROR);
                }

                Relationship relationshipSubstance = new Relationship(conceptSMTK, substanceList.get(0).getConceptSMTK(), relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

                BasicTypeValue order = new BasicTypeValue(Integer.parseInt(substanceTokens[2].trim()));

                /**Para esta definición, se obtiente el atributo orden**/
                for (RelationshipAttributeDefinition attDef : relationshipDefinition.getRelationshipAttributeDefinitions()) {

                    if (attDef.isOrderAttribute()) {
                        RelationshipAttribute ra;

                        ra = new RelationshipAttribute(attDef, relationshipSubstance, order);
                        relationshipSubstance.getRelationshipAttributes().add(ra);
                    }
                }

                conceptSMTK.addRelationship(relationshipSubstance);
            }

            /*Recuperando Tags*/
            String tags = tokens[mbConceptFields.get("GRUPOS_JERARQUICOS")];

            String[] tagsTokens = tags.split("•");

            for (String tagToken : tagsTokens) {

                if(tagToken.isEmpty() || tagToken.equals("\"")) {
                    continue;
                }

                String tagName = StringUtils.normalizeSpaces(tagToken.split("-")[1]).trim();
                Tag tag = new Tag();

                tag.setName(tagName);

                conceptSMTK.getTags().add(tag);
            }

            conceptSMTKMap.put(id, conceptSMTK);
        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: "+e.toString(), ERROR);
        }

    }

    public void loadAllConcepts(SMTKLoader smtkLoader) {

        smtkLoader.logInfo(new LoadLog("Comprobando Conceptos MB", INFO));

        try {

            initReader(smtkLoader.MB_PATH);

            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    loadConceptFromFileLine(line, smtkLoader.getUser());
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
        }
    }

    public void persistAllConcepts(SMTKLoader smtkLoader) {

        smtkLoader.logInfo(new LoadLog("Persisitiendo Conceptos Fármacos - MB", INFO));

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
    }

    public void processConcepts(SMTKLoader smtkLoader) {
        loadAllConcepts(smtkLoader);
        persistAllConcepts(smtkLoader);
    }

}
