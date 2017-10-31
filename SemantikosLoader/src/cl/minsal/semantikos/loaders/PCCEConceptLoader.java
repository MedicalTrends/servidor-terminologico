package cl.minsal.semantikos.loaders;

import cl.minsal.semantikos.clients.RemoteEJBClientFactory;
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
import static java.util.Collections.EMPTY_LIST;

/**
 * Created by root on 15-06-17.
 */
public class PCCEConceptLoader extends EntityLoader {

    ConceptManager conceptManager = (ConceptManager) RemoteEJBClientFactory.getInstance().getManager(ConceptManager.class);
    DescriptionManager descriptionManager = (DescriptionManager) RemoteEJBClientFactory.getInstance().getManager(DescriptionManager.class);
    RelationshipManager relationshipManager = (RelationshipManager) RemoteEJBClientFactory.getInstance().getManager(RelationshipManager.class);
    TagManager tagManager = (TagManager) RemoteEJBClientFactory.getInstance().getManager(TagManager.class);
    ISPFetcher ispFetcher = (ISPFetcher) RemoteEJBClientFactory.getInstance().getManager(ISPFetcher.class);
    SnomedCTManager snomedCTManager = (SnomedCTManager) RemoteEJBClientFactory.getInstance().getManager(SnomedCTManager.class);
    HelperTablesManager helperTableManager = (HelperTablesManager) RemoteEJBClientFactory.getInstance().getManager(HelperTablesManager.class);

    public static final Map<String, Integer> pcceConceptFields;
    static
    {
        pcceConceptFields = new HashMap<String, Integer>();
        pcceConceptFields.put("CONCEPTO_ID", 0);
        pcceConceptFields.put("DESCRIPCION", 1);
        pcceConceptFields.put("DESC_ABREVIADA", 2);
        pcceConceptFields.put("ESTADO", 3);
        pcceConceptFields.put("SENSIBLE_MAYUSCULA", 4);
        pcceConceptFields.put("CREAC_NOMBRE", 5);
        pcceConceptFields.put("REVISADO", 6);
        pcceConceptFields.put("CONSULTAR", 7);
        pcceConceptFields.put("SCT_ID", 8);
        pcceConceptFields.put("SCT_TERMINO", 9);
        pcceConceptFields.put("SINONIMO", 10);
        pcceConceptFields.put("GRUPOS_JERARQUICOS", 11);
        pcceConceptFields.put("PACK_MULTI_CANTIDAD", 12);
        pcceConceptFields.put("PACK_MULTI_UNIDAD_FK", 13);
        pcceConceptFields.put("PACK_MULTI_UNIDAD_DESC", 14);
        pcceConceptFields.put("MED_CLINICO_CON_ENVASE_FK", 15);
        pcceConceptFields.put("MED_CLINICO_CON_ENVASE_DESC", 16);
        pcceConceptFields.put("PRODUCTO_COMERCIAL_FK", 17);
        pcceConceptFields.put("PRODUCTO_COMERCIAL_DESC", 18);
        pcceConceptFields.put("EXISTE_EN_GS1_FK", 19);
        pcceConceptFields.put("EXISTE_EN_GS1_DESC", 20);
        pcceConceptFields.put("GTIN_GS1", 21);
    }

    Map<Long, ConceptSMTK> conceptSMTKMap = new HashMap<>();

    Map<String, Description> descriptionMap = new HashMap<>();

    public void loadConceptFromFileLine(String line, User user) throws LoadException {

        String[] tokens = line.split(separator,-1);
        long id = Long.parseLong(tokens[pcceConceptFields.get("CONCEPTO_ID")]);

        try {

            /*Recuperando datos Concepto*/

            /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
            boolean toBeReviewed = tokens[pcceConceptFields.get("REVISADO")].equals("Si");
            boolean toBeConsulted = tokens[pcceConceptFields.get("CONSULTAR")].equals("Si");
            boolean autogenerated = tokens[pcceConceptFields.get("CREAC_NOMBRE")].equals("Autogenerado");

            Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Producto Comercial con Envase");
            TagSMTK tagSMTK = TagSMTKFactory.getInstance().findTagSMTKByName("producto");

            ConceptSMTK conceptSMTK = new ConceptSMTK(category);
            conceptSMTK.setToBeConsulted(toBeConsulted);
            conceptSMTK.setToBeReviewed(toBeReviewed);
            conceptSMTK.setCategory(category);
            conceptSMTK.setTagSMTK(tagSMTK);

            /*Recuperando datos Descripciones*/

            /*Recuperando descripcion preferida*/
            String term = StringUtils.normalizeSpaces(tokens[pcceConceptFields.get("DESCRIPCION")]).trim();
            boolean caseSensitive = tokens[pcceConceptFields.get("SENSIBLE_MAYUSCULA")].equals("Sensible");
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
            term = descriptionFavourite.getTerm()+" ("+tagSMTK.getName()+")";
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

            /*Recuperando descripcion Abreviada*/
            term = StringUtils.normalizeSpaces(tokens[pcceConceptFields.get("DESC_ABREVIADA")]).trim();

            if(!StringUtils.isEmpty(term)) {
                descriptionType = DescriptionType.ABREVIADA;

                Description descriptionAbbreviated = new Description(conceptSMTK, term, descriptionType);
                descriptionAbbreviated.setCaseSensitive(caseSensitive);
                descriptionAbbreviated.setCreatorUser(user);
                descriptionAbbreviated.setAutogeneratedName(autogenerated);

                if(descriptionMap.containsKey(descriptionAbbreviated.getTerm())) {
                    SMTKLoader.logWarning(new LoadLog("Término repetido para descripción "+descriptionAbbreviated.toString()+". Se descarta descripción", INFO));
                }
                else {
                    descriptionMap.put(descriptionAbbreviated.getTerm(), descriptionAbbreviated);
                    conceptSMTK.addDescription(descriptionAbbreviated);
                }
            }

            /*Recuperando Sinónimos*/
            String synonyms = tokens[pcceConceptFields.get("SINONIMO")];
            descriptionType = DescriptionType.SYNONYMOUS;

            String[] synonymsTokens = synonyms.split("•");

            for (String synonymsToken : synonymsTokens) {

                if(synonymsToken.isEmpty() || synonymsToken.equals("\"")) {
                    continue;
                }

                term = StringUtils.normalizeSpaces(synonymsToken.split("-")[1]).trim();

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

            descriptionMap.clear();

            /*Recuperando datos Relaciones*/
            String idConceptSCTName = tokens[pcceConceptFields.get("SCT_ID")];

            /*Por defecto se mapea a un concepto SNOMED Genérico*/
            long idConceptSCT = 373873005;

            if(!idConceptSCTName.isEmpty()) {
                idConceptSCT = Long.parseLong(tokens[pcceConceptFields.get("SCT_ID")]);
            }

            String relationshipType = ES_UN;

            ConceptSCT conceptSCT = snomedCTManager.getConceptByID(idConceptSCT);

            if(conceptSCT == null) {
                throw new LoadException(path.toString(), id, "Relación referencia a concepto SCT inexistente", ERROR);
            }

            /**Se obtiene la definición de relacion SNOMED CT**/
            RelationshipDefinition relationshipDefinition = conceptSMTK.getCategory().findRelationshipDefinitionsByName(TargetDefinition.SNOMED_CT).get(0);

            RelationshipAttributeDefinition attDef;

            RelationshipAttribute ra;

            Relationship relationshipSnomed = new Relationship(conceptSMTK, conceptSCT, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

            /**Para esta definición, se obtiente el atributo tipo de relación**/
            for (RelationshipAttributeDefinition attDef1 : relationshipDefinition.getRelationshipAttributeDefinitions()) {

                if (attDef1.isRelationshipTypeAttribute()) {
                    HelperTable helperTable = (HelperTable) attDef1.getTargetDefinition();

                    List<HelperTableRow> relationshipTypes = helperTableManager.searchRows(helperTable, relationshipType);

                    if (relationshipTypes.size() == 0) {
                        throw new LoadException(path.toString(), id, "No existe un tipo de relación de nombre: "+relationshipType, ERROR);
                    }

                    ra = new RelationshipAttribute(attDef1, relationshipSnomed, relationshipTypes.get(0));
                    relationshipSnomed.getRelationshipAttributes().add(ra);
                }
            }

            /* Generando Grupo */
            BasicTypeValue group = new BasicTypeValue(0);

            RelationshipAttributeDefinition attDefGroup = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Grupo").get(0);

            RelationshipAttribute raGroup = new RelationshipAttribute(attDefGroup, relationshipSnomed, group);
            relationshipSnomed.getRelationshipAttributes().add(raGroup);

            conceptSMTK.addRelationship(relationshipSnomed);

            /* Generando Comercializado */

            BasicTypeValue basicTypeValue = new BasicTypeValue(true);

            relationshipDefinition = conceptSMTK.getCategory().findRelationshipDefinitionsByName(TargetDefinition.COMERCIALIZADO).get(0);

            Relationship relationshipMarketed = new Relationship(conceptSMTK, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

            conceptSMTK.addRelationship(relationshipMarketed);

            /*Recuperando Producto Comercial*/

            String pcName = tokens[pcceConceptFields.get("PRODUCTO_COMERCIAL_DESC")];

            if(!StringUtils.isEmpty(pcName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Producto Comercial").get(0);

                List<Description> pc = descriptionManager.searchDescriptionsPerfectMatch(StringUtils.normalizeSpaces(pcName).trim(), Arrays.asList(new Category[]{CategoryFactory.getInstance().findCategoryByName("Fármacos - Producto Comercial")}), null);

                if(pc.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un PC con preferida: "+pcName, ERROR);
                }

                if(!pc.get(0).getConceptSMTK().isModeled()) {
                    throw new LoadException(path.toString(), id, "EL PC: "+pcName+" no está modelado, se descarta este MC", ERROR);
                }

                Relationship relationshipPC = new Relationship(conceptSMTK, pc.get(0).getConceptSMTK(), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                conceptSMTK.addRelationship(relationshipPC);
            }

            /*Recuperando Medicamento Clínico con Envase*/

            String mcceName = tokens[pcceConceptFields.get("MED_CLINICO_CON_ENVASE_DESC")];

            if(!StringUtils.isEmpty(mcceName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Medicamento Clínico con Envase").get(0);

                List<Description> mcce = descriptionManager.searchDescriptionsPerfectMatch(StringUtils.normalizeSpaces(mcceName).trim(), Arrays.asList(new Category[]{CategoryFactory.getInstance().findCategoryByName("Fármacos - Medicamento Clínico con Envase")}), null);

                if(mcce.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un MCCE con preferida: "+pcName, ERROR);
                }

                if(!mcce.get(0).getConceptSMTK().isModeled()) {
                    throw new LoadException(path.toString(), id, "EL MCCE: "+pcName+" no está modelado, se descarta este MC", ERROR);
                }

                Relationship relationshipMCCE = new Relationship(conceptSMTK, mcce.get(0).getConceptSMTK(), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                conceptSMTK.addRelationship(relationshipMCCE);
            }

            /* Generando Existe GS1 */

            basicTypeValue = new BasicTypeValue(false);
            relationshipDefinition = category.findRelationshipDefinitionsByName("Existe GS1").get(0);

            Relationship relationshipExistGS1 = new Relationship(conceptSMTK, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

            conceptSMTK.addRelationship(relationshipExistGS1);

            conceptSMTKMap.put(id, conceptSMTK);
        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: "+e.toString(), ERROR);
        }

    }

    public void loadAllConcepts(SMTKLoader smtkLoader) {

        smtkLoader.logInfo(new LoadLog("Comprobando Conceptos PCCE", INFO));

        try {

            initReader(smtkLoader.PCCE_PATH);

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

        smtkLoader.logInfo(new LoadLog("Persisitiendo Conceptos PCCE", INFO));

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
