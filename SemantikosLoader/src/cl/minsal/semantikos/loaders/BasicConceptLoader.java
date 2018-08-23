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

import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.util.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cl.minsal.semantikos.model.LoadLog.ERROR;
import static cl.minsal.semantikos.model.LoadLog.INFO;
import static cl.minsal.semantikos.model.relationships.SnomedCTRelationship.ES_UN_MAPEO_DE;

/**
 * Created by root on 08-06-17.
 */
public class BasicConceptLoader extends EntityLoader {

    private static final Logger logger = Logger.getLogger(BasicConceptLoader.class.getName() );

    ConceptManager conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);
    DescriptionManager descriptionManager = (DescriptionManager) ServiceLocator.getInstance().getService(DescriptionManager.class);
    SnomedCTManager snomedCTManager = (SnomedCTManager) ServiceLocator.getInstance().getService(SnomedCTManager.class);
    HelperTablesManager helperTableManager = (HelperTablesManager) ServiceLocator.getInstance().getService(HelperTablesManager.class);

    public static final Map<String, Integer> basicConceptFields;
    static
    {
        basicConceptFields = new HashMap<String, Integer>();
        basicConceptFields.put("STK_CONCEPTO", 0);
        basicConceptFields.put("CATEGORIA", 1);
        basicConceptFields.put("CATEGORIA_TEXTO", 2);
        basicConceptFields.put("ESTADO", 3);
        basicConceptFields.put("VALIDADO", 4);
        basicConceptFields.put("MODELADO", 5);
        basicConceptFields.put("USUARIO_VALIDA", 6);
        basicConceptFields.put("FECHA_VALIDA", 7);
        basicConceptFields.put("CONSULTAR", 8);
        basicConceptFields.put("OBSERVACION", 9);
        basicConceptFields.put("COMERCIALIZADO", 10);
        basicConceptFields.put("GRADO_DEFINICION", 11);
        basicConceptFields.put("FECHA_REGISTRO", 12);
        basicConceptFields.put("USUARIO_REGISTRO", 13);
    }

    public static final Map<String, Integer> basicDescriptionFields;
    static
    {
        basicDescriptionFields = new HashMap<String, Integer>();
        basicDescriptionFields.put("STK_DESCRIPCION", 0);
        basicDescriptionFields.put("STK_CONCEPTO", 1);
        basicDescriptionFields.put("TERMINO", 2);
        basicDescriptionFields.put("tipo", 3);
        basicDescriptionFields.put("SENS_MAYUSC", 4);
        basicDescriptionFields.put("CREAC_NOMBRE", 5);
        basicDescriptionFields.put("ESTADO", 6);
        basicDescriptionFields.put("FECHA_REGISTRO", 7);
        basicDescriptionFields.put("USUARIO_REGISTRO", 8);
        basicDescriptionFields.put("MODELADO", 9);
        basicDescriptionFields.put("forma", 10);
        basicDescriptionFields.put("USOS", 11);
    }

    public static final Map<String, Integer> basicRelationshipFields;
    static
    {
        basicRelationshipFields = new HashMap<String, Integer>();
        basicRelationshipFields.put("ID_RELACION", 0);
        basicRelationshipFields.put("FECHA", 1);
        basicRelationshipFields.put("ESTADO", 2);
        basicRelationshipFields.put("STK_CONCEPTOORIGEN", 3);
        basicRelationshipFields.put("ID_TIPO_RELACION", 4);
        basicRelationshipFields.put("DSC_TIPO_RELACION", 5);
        basicRelationshipFields.put("ID_SCT_DESTINO", 6);
        basicRelationshipFields.put("RELATIONSHIPGROUP", 7);
        //basicRelationshipFields.put("FECHA_REGISTRO", 7);
        basicRelationshipFields.put("USUARIO_REGISTRO", 8);
        basicRelationshipFields.put("MODELADO", 9);
    }

    Map<Long, ConceptSMTK> conceptSMTKMap = new HashMap<>();

    Map<String, Description> descriptionMap = new HashMap<>();

    public void loadConceptFromFileLine(String line) throws LoadException {

        String[] tokens = line.split(separator,-1);

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        long id = Long.parseLong(tokens[basicConceptFields.get("STK_CONCEPTO")]);
        String categoryName = tokens[basicConceptFields.get("CATEGORIA_TEXTO")];
        boolean toBeConsulted = tokens[basicConceptFields.get("CONSULTAR")].equals("1");

        Category category = CategoryFactory.getInstance().findCategoryByName(categoryName);

        if(category == null) {
            throw new LoadException(path.toString(), id, "No existe una categoría de nombre: "+categoryName, ERROR);
        }

        ConceptSMTK conceptSMTK = new ConceptSMTK(category);
        conceptSMTK.setToBeConsulted(toBeConsulted);
        //conceptSMTK.setConceptID(conceptManager.generateConceptId(id));
        conceptSMTK.setCategory(category);

        conceptSMTKMap.put(id, conceptSMTK);
    }

    public void loadDescriptionFromFileLine(String line, User user) throws LoadException {

        String[] tokens = line.split(separator,-1);

            /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        long id = Long.parseLong(tokens[basicDescriptionFields.get("STK_DESCRIPCION")]);

        try {

            long idConcept = Long.parseLong(tokens[basicDescriptionFields.get("STK_CONCEPTO")]);

            String term = StringUtils.normalizeSpaces(tokens[basicDescriptionFields.get("TERMINO")]).trim();
            String idDescriptionTypeString = tokens[basicDescriptionFields.get("tipo")];
            int idDescriptionType;
            boolean caseSensitive = tokens[basicDescriptionFields.get("SENS_MAYUSC")].equals("1");

            DescriptionType descriptionType = DescriptionTypeFactory.TYPELESS_DESCRIPTION_TYPE;

            ConceptSMTK conceptSMTK = conceptSMTKMap.get(idConcept);

            if(conceptSMTK == null) {
                throw new LoadException(path.toString(), idConcept, "Descripción referencia a concepto inexistente", ERROR);
            }

            try {
                idDescriptionType = Integer.parseInt(idDescriptionTypeString);
            }
            catch (NumberFormatException e) {
                throw new LoadException(path.toString(), idConcept, "Tipo de descripcion invalido", ERROR);
            }

            switch(idDescriptionType) {
                case 1:
                    descriptionType = DescriptionTypeFactory.getInstance().getFavoriteDescriptionType();
                    break;
                case 2:
                    descriptionType = DescriptionTypeFactory.getInstance().getSynonymDescriptionType();
                    break;
                case 3:
                    descriptionType = DescriptionTypeFactory.getInstance().getFSNDescriptionType();
                    /*Si es FSN, deducir TagSMTK*/
                    TagSMTK tagSMTK = TagSMTKFactory.getInstance().assertTagSMTK(term);

                    if(tagSMTK == null) {
                        throw new LoadException(path.toString(), id, "El FSN: '"+term+"' no contiene un Tag Semántiko conocido: "+idDescriptionType, ERROR);
                    }

                    conceptSMTK.setTagSMTK(tagSMTK);

                    break;
                default:
                    throw new LoadException(path.toString(), id, "Tipo de descripción desconocido: "+idDescriptionType, ERROR);
            }

            Description description = new Description(conceptSMTK, term, descriptionType);
            //description.setDescriptionId(descriptionManager.generateDescriptionId(id));
            description.setCaseSensitive(caseSensitive);
            description.setCreatorUser(user);

            if(descriptionMap.containsKey(conceptSMTK.getCategory().getId()+description.getTerm())) {
                if(description.getDescriptionType().equals(DescriptionType.PREFERIDA) ||
                   description.getDescriptionType().equals(DescriptionType.FSN)) {
                    SMTKLoader.logWarning(new LoadLog("Término repetido para descripción "+description.toString()+". Se reemplaza por descripcion actual", INFO));
                    conceptSMTK.replaceDescriptionByTerm(description);
                }
                else {
                    SMTKLoader.logWarning(new LoadLog("Término repetido para descripción "+description.toString()+". Se descarta descripcion", INFO));
                }
            }
            else {
                descriptionMap.put(conceptSMTK.getCategory().getId()+description.getTerm(), description);
                conceptSMTK.addDescription(description);
            }

        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: "+e.toString(), ERROR);
        }

    }

    public void loadRelationshipFromFileLine(String line) throws LoadException {

        String[] tokens = line.split(separator,-1);

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        long id = Long.parseLong(tokens[basicRelationshipFields.get("ID_RELACION")]);

        try {

            long idConceptSMTK = Long.parseLong(tokens[basicRelationshipFields.get("STK_CONCEPTOORIGEN")]);
            long idConceptSCT = Long.parseLong(tokens[basicRelationshipFields.get("ID_SCT_DESTINO")]);
            String relationshipType = tokens[basicRelationshipFields.get("DSC_TIPO_RELACION")];

            ConceptSMTK conceptSMTK = conceptSMTKMap.get(idConceptSMTK);
            ConceptSCT conceptSCT = snomedCTManager.getConceptByID(idConceptSCT);

            if(conceptSMTK == null) {
                throw new LoadException(path.toString(), id, "Relación referencia a concepto SMTK inexistente", ERROR);
            }

            if(conceptSCT == null) {
                throw new LoadException(path.toString(), id, "Relación referencia a concepto SCT inexistente", ERROR);
            }

            /**Se obtiene la definición de relacion SNOMED CT**/
            RelationshipDefinition relationshipDefinition = conceptSMTK.getCategory().findRelationshipDefinitionsByName(TargetDefinition.SNOMED_CT).get(0);

            Relationship relationship = new Relationship(conceptSMTK, conceptSCT, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

            /**Para esta definición, se obtiente el atributo tipo de relación**/
            for (RelationshipAttributeDefinition attDef : relationshipDefinition.getRelationshipAttributeDefinitions()) {

                if (attDef.isRelationshipTypeAttribute()) {
                    HelperTable helperTable = (HelperTable) attDef.getTargetDefinition();

                    List<HelperTableRow> relationshipTypes = helperTableManager.searchRows(helperTable, relationshipType);

                    RelationshipAttribute ra;

                    if (relationshipTypes.size() == 0) {
                        throw new LoadException(path.toString(), idConceptSMTK, "No existe un tipo de relación de nombre: "+relationshipType, ERROR);
                    }

                    ra = new RelationshipAttribute(attDef, relationship, relationshipTypes.get(0));

                    relationship.getRelationshipAttributes().add(ra);
                }
            }

            /* Generando Grupo */
            BasicTypeValue group = new BasicTypeValue(0);

            RelationshipAttributeDefinition attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Grupo").get(0);

            RelationshipAttribute ra = new RelationshipAttribute(attDef, relationship, group);
            relationship.getRelationshipAttributes().add(ra);

            conceptSMTK.addRelationship(relationship);
        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: "+e.toString(), ERROR);
        }

    }

    public void loadAllConcepts(SMTKLoader smtkLoader) {

        smtkLoader.logInfo(new LoadLog("Comprobando Conceptos Básicos", INFO));

        try {

            initReader(smtkLoader.getBasicConceptPath());

            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    loadConceptFromFileLine(line);
                }
                catch (LoadException e) {
                    smtkLoader.logError(e);
                    e.printStackTrace();
                }
            }

            haltReader();

            initReader(smtkLoader.getBasicDescriptionPath());

            while ((line = reader.readLine()) != null) {
                try {
                    loadDescriptionFromFileLine(line, smtkLoader.getUser());
                }
                catch (LoadException e) {
                    smtkLoader.logError(e);
                    e.printStackTrace();
                }
            }

            descriptionMap.clear();

            haltReader();

            initReader(smtkLoader.getBasicRelationshipPath());

            while ((line = reader.readLine()) != null) {
                try {
                    loadRelationshipFromFileLine(line);
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

        smtkLoader.logInfo(new LoadLog("Persisitiendo Conceptos Básicos", INFO));

        Iterator it = conceptSMTKMap.entrySet().iterator();

        smtkLoader.setConceptsProcessed(0);

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
