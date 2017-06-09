package cl.minsal.semantikos.loaders;

import cl.minsal.semantikos.clients.RemoteEJBClientFactory;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.LoadException;
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
import java.util.*;

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

    public void loadConceptFromFileLine(String line) throws LoadException {

        String[] tokens = line.split(separator);

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        long id = Long.parseLong(tokens[basicConceptFields.get("STK_CONCEPTO")]);
        String categoryName = tokens[basicConceptFields.get("CATEGORIA_TEXTO")];
        boolean toBeConsulted = tokens[basicConceptFields.get("CONSULTAR")].equals("1");

        Category category = CategoryFactory.getInstance().findCategoryByName(categoryName);

        if(category == null) {
            throw new LoadException(path.toString(), id, "No existe una categoría de nombre: "+categoryName);
        }

        ConceptSMTK conceptSMTK = new ConceptSMTK(category);
        conceptSMTK.setToBeConsulted(toBeConsulted);
        conceptSMTK.setConceptID(conceptManager.generateConceptId(id));

        conceptSMTKMap.put(id, conceptSMTK);
    }

    public void loadDescriptionFromFileLine(String line) throws LoadException {

        String[] tokens = line.split(separator);

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
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
            default:
                throw new LoadException(path.toString(), id, "Tipo de descripción desconocido: "+idDescriptionType);
        }

        ConceptSMTK conceptSMTK = conceptSMTKMap.get(idConcept);

        if(conceptSMTK == null) {
            throw new LoadException(path.toString(), idConcept, "Descripción referencia a concepto inexistente");
        }

        Description description = new Description(conceptSMTK, term, descriptionType);
        description.setDescriptionId(descriptionManager.generateDescriptionId(id));
        description.setCaseSensitive(caseSensitive);

        conceptSMTK.addDescription(description);
    }

    public void loadRelationshipFromFileLine(String line) throws LoadException {

        String[] tokens = line.split(separator);

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        long id = Long.parseLong(tokens[basicRelationshipFields.get("ID_RELACION")]);
        long idConceptSMTK = Long.parseLong(tokens[basicRelationshipFields.get("STK_CONCEPTOORIGEN")]);
        long idConceptSCT = Long.parseLong(tokens[basicRelationshipFields.get("ID_SCT_DESTINO")]);
        String relationshipType = tokens[basicRelationshipFields.get("DESC_TIPO_RELACION")];

        ConceptSMTK conceptSMTK = conceptSMTKMap.get(idConceptSMTK);
        ConceptSCT conceptSCT = snomedCTManager.getConceptByID(idConceptSCT);

        if(conceptSMTK == null) {
            throw new LoadException(path.toString(), idConceptSMTK, "Relación referencia a concepto SMTK inexistente");
        }

        if(conceptSCT == null) {
            throw new LoadException(path.toString(), idConceptSMTK, "Relación referencia a concepto SCT inexistente");
        }

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
                    throw new LoadException(path.toString(), idConceptSMTK, "No existe un tipo de relación de nombre: "+relationshipType);
                }

                ra = new RelationshipAttribute(attDef, relationship, relationshipTypes.get(0));
                relationship.getRelationshipAttributes().add(ra);
            }
        }

        conceptSMTK.addRelationship(relationship);
    }

    public void loadAllConcepts(SMTKLoader smtkLoader) {

        try {

            /**
             * Abrir archivo de conceptos y validar estructura
             */
            initReader(smtkLoader.getBasicConceptPath(), (List<String>) (Object) Arrays.asList(basicConceptFields.keySet().toArray()));

            String line;

            while ((line = reader.readLine()) != null) {
                loadConceptFromFileLine(line);
            }

            /**
             * Cerrar archivo
             */
            haltReader();

            /**
             * Abrir archivo de descripciones y validar estructura
             */
            initReader(smtkLoader.getBasicDescriptionPath(), (List<String>) (Object) Arrays.asList(basicDescriptionFields.keySet().toArray()));

            while ((line = reader.readLine()) != null) {
                loadDescriptionFromFileLine(line);
            }

            /**
             * Cerrar archivo
             */
            haltReader();

            /**
             * Abrir archivo de relaciones y validar estructura
             */
            initReader(smtkLoader.getBasicRelationshipPath(), (List<String>) (Object) Arrays.asList(basicRelationshipFields.keySet().toArray()));

            while ((line = reader.readLine()) != null) {
                loadRelationshipFromFileLine(line);
            }

            haltReader();

        } catch (Exception e) {
            smtkLoader.log(new LoadException(path.toString(), null, e.getMessage()));
            e.printStackTrace();
        } catch (LoadException e) {
            smtkLoader.log(e);
            e.printStackTrace();
        }
    }

    public void persistAllConcepts(SMTKLoader smtkLoader) {

        Iterator it = conceptSMTKMap.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry) it.next();

            try {
                conceptManager.persist((ConceptSMTK)pair.getValue(), smtkLoader.getUser());
            }
            catch (Exception e) {
                smtkLoader.log(new LoadException(path.toString(), (Long) pair.getKey(), e.getMessage()));
                e.printStackTrace();
            }

            it.remove(); // avoids a ConcurrentModificationException
        }

    }
}
