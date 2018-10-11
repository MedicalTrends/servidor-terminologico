package cl.minsal.semantikos.loaders;

import cl.minsal.semantikos.clients.ServiceLocator;
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
public class FPConceptLoader extends EntityLoader {

    ConceptManager conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);
    DescriptionManager descriptionManager = (DescriptionManager) ServiceLocator.getInstance().getService(DescriptionManager.class);
    SnomedCTManager snomedCTManager = (SnomedCTManager) ServiceLocator.getInstance().getService(SnomedCTManager.class);
    HelperTablesManager helperTableManager = (HelperTablesManager) ServiceLocator.getInstance().getService(HelperTablesManager.class);

    public static final Map<String, Integer> fpConceptFields;
    static
    {
        fpConceptFields = new HashMap<String, Integer>();
        fpConceptFields.put("CONCEPTO_ID", 0);
        fpConceptFields.put("DESCRIPCION", 1);
        fpConceptFields.put("DESC_ABREVIADA", 2);
        fpConceptFields.put("ESTADO", 3);
        fpConceptFields.put("SENSIBLE_MAYUSCULA", 4);
        fpConceptFields.put("CREAC_NOMBRE", 5);
        fpConceptFields.put("REVISADO", 6);
        fpConceptFields.put("CONSULTAR", 7);
        fpConceptFields.put("SCT_ID", 8);
        fpConceptFields.put("SCT_TERMINO", 9);
        fpConceptFields.put("SINONIMO", 10);
        fpConceptFields.put("GRUPOS_JERARQUICOS", 11);
        fpConceptFields.put("GRUPO_FAMILIA_PRODUCTO_FK", 12);
        fpConceptFields.put("GRUPO_FAMILIA_PRODUCTO_DESC", 13);
        fpConceptFields.put("GRUPO_FAMILIA_GENERICA_FK", 14);
        fpConceptFields.put("GRUPO_FAMILIA_GENERICA_DESC", 15);
    }

    Map<Long, ConceptSMTK> conceptSMTKMap = new HashMap<>();

    Map<String, Description> descriptionMap = new HashMap<>();

    public void loadConceptFromFileLine(String line, User user) throws LoadException {

        String[] tokens = line.split(separator,-1);

        /*Recuperando datos Concepto*/

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        long id = Long.parseLong(tokens[fpConceptFields.get("CONCEPTO_ID")]);

        try {

            boolean toBeReviewed = tokens[fpConceptFields.get("REVISADO")].equals("Si");
            boolean toBeConsulted = tokens[fpConceptFields.get("CONSULTAR")].equals("Si");
            boolean autogenerated = tokens[fpConceptFields.get("CREAC_NOMBRE")].equals("Autogenerado");

            Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Familia de Productos");
            TagSMTK tagSMTK = TagSMTKFactory.getInstance().findTagSMTKByName("producto");

            ConceptSMTK conceptSMTK = new ConceptSMTK(category);
            conceptSMTK.setToBeConsulted(toBeConsulted);
            conceptSMTK.setToBeReviewed(toBeReviewed);
            conceptSMTK.setCategory(category);
            conceptSMTK.setTagSMTK(tagSMTK);

            /*Recuperando datos Descripciones*/

            /*Recuperando descripcion preferida*/
            String term = StringUtils.normalizeSpaces(tokens[fpConceptFields.get("DESCRIPCION")]).trim();
            boolean caseSensitive = tokens[fpConceptFields.get("SENSIBLE_MAYUSCULA")].equals("Sensible");
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
            String synonyms = tokens[fpConceptFields.get("SINONIMO")];

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
            String idConceptSCTName = tokens[fpConceptFields.get("SCT_ID")];

            /*Por defecto se mapea a un concepto SNOMED Genérico (GRUPO)*/
            long idConceptSCT = 246261001;

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

                Relationship relationshipSnomed = new Relationship(conceptSMTK, conceptSCT, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

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

                RelationshipAttributeDefinition attDefGroup = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Grupo").get(0);

                RelationshipAttribute raGroup = new RelationshipAttribute(attDefGroup, relationshipSnomed, group);
                relationshipSnomed.getRelationshipAttributes().add(raGroup);

                conceptSMTK.addRelationship(relationshipSnomed);

            }

            BasicTypeValue basicTypeValue = new BasicTypeValue(true);

            relationshipDefinition = conceptSMTK.getCategory().findRelationshipDefinitionsByName(TargetDefinition.COMERCIALIZADO).get(0);

            Relationship relationshipMarketed = new Relationship(conceptSMTK, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

            conceptSMTK.addRelationship(relationshipMarketed);

            /*Recuperando Familia Genérica*/

            String genericFamilyName = tokens[fpConceptFields.get("GRUPO_FAMILIA_GENERICA_DESC")];

            if(!genericFamilyName.isEmpty()) {

                basicTypeValue = new BasicTypeValue(genericFamilyName.trim().equals("Si"));
                relationshipDefinition = category.findRelationshipDefinitionsByName("Familia Genérica").get(0);

                Relationship relationshipSaleCondition = new Relationship(conceptSMTK, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                conceptSMTK.addRelationship(relationshipSaleCondition);
            }

            /*Recuperando GFP*/

            String gfpName = tokens[fpConceptFields.get("GRUPO_FAMILIA_PRODUCTO_DESC")];

            if(!StringUtils.isEmpty(gfpName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Grupo de Familia de Producto").get(0);

                List<Description> gfp = descriptionManager.searchDescriptionsPerfectMatch(StringUtils.normalizeSpaces(gfpName).trim(), Arrays.asList(new Category[]{CategoryFactory.getInstance().findCategoryByName("Fármacos - Grupo de Familia de Producto")}), null, null);

                if(gfp.isEmpty()) {
                    SMTKLoader.logError(new LoadException(path.toString(), id, "No existe un GFP con preferida: "+gfpName, ERROR));
                    //throw new LoadException(path.toString(), id, "No existe un GFP con preferida: "+gfpName, ERROR);
                }
                else {
                    if(!gfp.get(0).getConceptSMTK().isModeled()) {
                        throw new LoadException(path.toString(), id, "EL GFP: "+gfpName+" no está modelado, se descarta esta FP", ERROR);
                    }

                    Relationship relationshipGFP = new Relationship(conceptSMTK, gfp.get(0).getConceptSMTK(), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));
                    conceptSMTK.addRelationship(relationshipGFP);
                }
            }

            conceptSMTKMap.put(id, conceptSMTK);
        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: "+e.toString(), ERROR);
        }
    }

    public void loadAllConcepts(SMTKLoader smtkLoader) {

        //smtkLoader.logInfo(new LoadLog("Comprobando Conceptos FP", INFO));
        smtkLoader.printInfo(new LoadLog("Comprobando Conceptos FP", INFO));
        smtkLoader.setConceptsProcessed(0);

        try {

            initReader(smtkLoader.FP_PATH);

            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    loadConceptFromFileLine(line, smtkLoader.getUser());
                    smtkLoader.incrementConceptsProcessed(1);
                }
                catch (LoadException e) {
                    //smtkLoader.logError(e);
                    smtkLoader.printError(e);
                    e.printStackTrace();
                }
            }

            haltReader();

            //smtkLoader.logTick();
            smtkLoader.printTick();

        } catch (Exception e) {
            //smtkLoader.logError(new LoadException(path.toString(), null, e.getMessage(), ERROR));
            smtkLoader.printError(new LoadException(path.toString(), "", e.getMessage(), ERROR));
            e.printStackTrace();
        } catch (LoadException e) {
            e.printStackTrace();
        }
    }

    public void persistAllConcepts(SMTKLoader smtkLoader) {

        //smtkLoader.logInfo(new LoadLog("Persisitiendo Conceptos FP", INFO));
        smtkLoader.printInfo(new LoadLog("Persisitiendo Conceptos FP", INFO));
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
    }

    public void processConcepts(SMTKLoader smtkLoader) {
        smtkLoader.setProcessed(0);
        loadAllConcepts(smtkLoader);
        persistAllConcepts(smtkLoader);
    }

}
