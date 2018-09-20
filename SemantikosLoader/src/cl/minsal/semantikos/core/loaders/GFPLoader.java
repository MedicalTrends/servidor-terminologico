package cl.minsal.semantikos.core.loaders;

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

/**
 * Created by root on 15-06-17.
 */
public class GFPLoader extends BaseLoader {

    static int OFFSET = SubstanceLoader.LENGHT + MBLoader.LENGHT + MCLoader.LENGHT + MCCELoader.LENGHT;

    static
    {
        fields = new LinkedHashMap<>();
        fields.put("CONCEPTO_ID", OFFSET + 0);
        fields.put("DESCRIPCION", OFFSET + 1);
        fields.put("DESC_ABREVIADA", OFFSET + 2);
        fields.put("TIPO", OFFSET + 3);
        fields.put("SENSIBLE_MAYUSCULA", OFFSET + 4);
        fields.put("CREAC_NOMBRE", OFFSET + 5);
        fields.put("ESTADO", OFFSET + 6);
        fields.put("REVISADO", OFFSET + 7);
        fields.put("CONSULTAR", OFFSET + 8);
        fields.put("OBSERVACION", OFFSET + 9);
        fields.put("SINONIMO", OFFSET + 10);
        fields.put("SCT_ID", OFFSET + 11);
        fields.put("SCT_TERMINO", OFFSET + 12);
        fields.put("GRUPOS_JERARQUICOS", OFFSET + 13);
    }

    static int LENGHT = fields.size();

    public GFPLoader(Category category, User user) {
        super(category, user);
    }

    public void loadConceptFromFileLine(String line) throws LoadException {

        tokens = line.split(separator,-1);

        /*Recuperando datos Concepto*/

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        String id = StringUtils.normalizeSpaces(tokens[fields.get("CONCEPTO_ID")]).trim();

        /*Recuperando tipo*/
        String type = tokens[fields.get("TIPO")];

        // Si esta linea no contiene un concepto retornar inmediatamente
        if(StringUtils.isEmpty(id)) {
            return;
        }

        try {

            /*Estableciendo categoría*/
            Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Grupo de Familia de Producto");

            /*Recuperando descripcion preferida*/
            String term = StringUtils.normalizeSpaces(tokens[fields.get("DESCRIPCION")]).trim();

            init(type, category, term);

            if(type.equals("M")) {
                return;
            }

            RelationshipDefinition relationshipDefinition;

            /*Recuperando datos Relaciones*/
            String idConceptSCTName = tokens[fields.get("SCT_ID")];

            /*Por defecto se mapea a un concepto SNOMED Genérico (GRUPO)*/
            long idConceptSCT = 246261001;

            if(!idConceptSCTName.isEmpty()) {
                idConceptSCT = Long.parseLong(idConceptSCTName);
            }

            if(true) {

                String relationshipType = ES_UN;

                ConceptSCT conceptSCT = snomedCTManager.getConceptByID(idConceptSCT);

                if (conceptSCT == null) {
                    throw new LoadException(path.toString(), id, "Relación referencia a concepto SCT inexistente", ERROR, type);
                }

                /**Se obtiene la definición de relacion SNOMED CT**/
                relationshipDefinition = category.findRelationshipDefinitionsByName(TargetDefinition.SNOMED_CT).get(0);

                Relationship relationshipSnomed = new Relationship(newConcept, conceptSCT, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                /**Para esta definición, se obtiente el atributo tipo de relación**/
                for (RelationshipAttributeDefinition attDef : relationshipDefinition.getRelationshipAttributeDefinitions()) {

                    if (attDef.isRelationshipTypeAttribute()) {
                        HelperTable helperTable = (HelperTable) attDef.getTargetDefinition();

                        List<HelperTableRow> relationshipTypes = helperTableManager.searchRows(helperTable, relationshipType);

                        RelationshipAttribute ra;

                        if (relationshipTypes.size() == 0) {
                            throw new LoadException(path.toString(), id, "No existe un tipo de relación de nombre: " + relationshipType, ERROR, type);
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

                addRelationship(relationshipSnomed, type);

            }

            BasicTypeValue basicTypeValue = new BasicTypeValue(true);

            relationshipDefinition = category.findRelationshipDefinitionsByName(TargetDefinition.COMERCIALIZADO).get(0);

            Relationship relationshipMarketed = new Relationship(newConcept, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

            addRelationship(relationshipMarketed, type);

            addConcept(type);
        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: "+e.toString(), ERROR, type);
        }
    }

}
