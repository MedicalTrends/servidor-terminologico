package cl.minsal.semantikos.loaders;

import cl.minsal.semantikos.clients.RemoteEJBClientFactory;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.SMTKLoader;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionType;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cl.minsal.semantikos.model.relationships.SnomedCTRelationship.ES_UN_MAPEO_DE;

/**
 * Created by root on 08-06-17.
 */
public class BasicConceptLoader extends EntityLoader {

    private static final Logger logger = LoggerFactory.getLogger(BasicConceptLoader.class);


    ConceptManager conceptManager = (ConceptManager) RemoteEJBClientFactory.getInstance().getManager(ConceptManager.class);
    DescriptionManager descriptionManager = (DescriptionManager) RemoteEJBClientFactory.getInstance().getManager(DescriptionManager.class);
    SnomedCTManager snomedCTManager = (SnomedCTManager) RemoteEJBClientFactory.getInstance().getManager(SnomedCTManager.class);
    HelperTablesManager helperTableManager = (HelperTablesManager) RemoteEJBClientFactory.getInstance().getManager(HelperTablesManager.class);

    public BasicConceptLoader(Path path, BufferedReader reader) {
        super(path, reader);
    }

    private static final Map<String, Integer> basicConceptFields;
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

    private static final Map<String, Integer> basicDescriptionFields;
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

    private static final Map<String, Integer> basicRelationshipFields;
    static
    {
        basicRelationshipFields = new HashMap<String, Integer>();
        basicRelationshipFields.put("ID_RELACION", 0);
        basicRelationshipFields.put("FECHA", 1);
        basicRelationshipFields.put("ESTADO", 2);
        basicRelationshipFields.put("STK_CONCEPTOORIGEN", 3);
        basicRelationshipFields.put("ID_TIPO_RELACION", 4);
        basicRelationshipFields.put("ID_SCT_DESTINO", 5);
        basicRelationshipFields.put("RELATIONSHIPGROUP", 6);
        basicRelationshipFields.put("FECHA_REGISTRO", 7);
        basicRelationshipFields.put("USUARIO_REGISTRO", 8);
        basicRelationshipFields.put("MODELADO", 9);
    }

    Map<Long, ConceptSMTK> conceptSMTKMap = new HashMap<>();

    public void loadConceptFromFileLine(String line) {

        String[] tokens = line.split(separator);

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        long id = Long.parseLong(tokens[basicConceptFields.get("STK_DESCRIPCION")]);
        String categoryName = tokens[basicConceptFields.get("CATEGORIA_TEXTO")];
        boolean toBeConsulted = tokens[basicConceptFields.get("CONSULTAR")].equals("1");

        Category category = CategoryFactory.getInstance().findCategoryByName(categoryName);

        ConceptSMTK conceptSMTK = new ConceptSMTK(category);
        conceptSMTK.setToBeConsulted(toBeConsulted);
        conceptSMTK.setConceptID(conceptManager.generateConceptId(id));

        conceptSMTKMap.put(id, conceptSMTK);
    }

    public void loadDescriptionFromFileLine(String line) {

        String[] tokens = line.split(separator);

        long id = Long.parseLong(tokens[basicDescriptionFields.get("STK_DESCRIPCION")]);
        long idConcept = Long.parseLong(tokens[basicDescriptionFields.get("STK_CONCEPTO")]);

        String term = tokens[basicConceptFields.get("TERMINO")];
        int idDescriptionType = Integer.parseInt(tokens[basicDescriptionFields.get("STK_CONCEPTO")]);
        boolean caseSensitive = tokens[basicConceptFields.get("SENS_MAYUSC")].equals("1");

        DescriptionType descriptionType = DescriptionTypeFactory.TYPELESS_DESCRIPTION_TYPE;

        switch(idDescriptionType) {
            case 1:
                descriptionType = DescriptionTypeFactory.getInstance().getFavoriteDescriptionType();
                break;
            case 2:
                descriptionType = DescriptionTypeFactory.getInstance().getSynonymDescriptionType();
                break;
            case 3:
                descriptionType = DescriptionTypeFactory.getInstance().getFSNDescriptionType();
                break;
        }

        ConceptSMTK conceptSMTK = conceptSMTKMap.get(idConcept);

        Description description = new Description(conceptSMTK, term, descriptionType);
        description.setDescriptionId(descriptionManager.generateDescriptionId(id));
        description.setCaseSensitive(caseSensitive);

        conceptSMTK.addDescription(description);
    }

    public void loadRelationshipFromFileLine(String line) {

        String[] tokens = line.split(separator);

        long id = Long.parseLong(tokens[basicRelationshipFields.get("ID_RELACION")]);
        long idConceptSMTK = Long.parseLong(tokens[basicRelationshipFields.get("STK_CONCEPTOORIGEN")]);
        long idConceptSCT = Long.parseLong(tokens[basicRelationshipFields.get("ID_SCT_DESTINO")]);
        String relationshipType = tokens[basicRelationshipFields.get("DESC_TIPO_RELACION")];

        ConceptSMTK conceptSMTK = conceptSMTKMap.get(idConceptSMTK);
        ConceptSCT conceptSCT = snomedCTManager.getConceptByID(idConceptSCT);
        /**Se obtiene la definición de relacion SNOMED CT**/
        RelationshipDefinition relationshipDefinition = RelationshipDefinitionFactory.getInstance().findRelationshipDefinitionByName(TargetDefinition.SNOMED_CT);

        Relationship relationship = new Relationship(conceptSMTK, conceptSCT, relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

        /**Para esta definición, se obtiente el atributo tipo de relación**/
        for (RelationshipAttributeDefinition attDef : relationshipDefinition.getRelationshipAttributeDefinitions()) {
            if (attDef.isRelationshipTypeAttribute()) {
                HelperTable helperTable = (HelperTable) attDef.getTargetDefinition();

                List<HelperTableRow> relationshipTypes = helperTableManager.searchRows(helperTable, relationshipType);

                RelationshipAttribute ra;

                if (relationshipTypes.size() == 0) {
                    logger.error("No hay datos en la tabla de TIPOS DE RELACIONES.");
                }

                ra = new RelationshipAttribute(attDef, relationship, relationshipTypes.get(0));
                relationship.getRelationshipAttributes().add(ra);
            }
        }

        conceptSMTK.addRelationship(relationship);
    }

    public void loadAllConcepts(SMTKLoader smtkLoader) {

        initReader(smtkLoader.getBasicConceptPath());

        try {

            String line;

            while ((line = reader.readLine()) != null) {

                loadConceptFromFileLine(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        haltReader();

        initReader(smtkLoader.getBasicDescriptionPath());

        try {

            String line;

            while ((line = reader.readLine()) != null) {

                loadDescriptionFromFileLine(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        haltReader();

        initReader(smtkLoader.getBasicRelationshipPath());

        try {

            String line;

            while ((line = reader.readLine()) != null) {

                loadDescriptionFromFileLine(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        haltReader();
    }

    public void persistAllConcepts(SMTKLoader smtkLoader) {
        for (ConceptSMTK conceptSMTK : conceptSMTKMap.values()) {
            conceptManager.persist(conceptSMTK, smtkLoader.getUser());
        }
    }
}
