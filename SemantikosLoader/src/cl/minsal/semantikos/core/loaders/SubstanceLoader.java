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

    static int OFFSET = 0;

    static
    {
        fields = new LinkedHashMap<>();
        fields.put("CONCEPTO_ID", 0);
        fields.put("DESCRIPCION", 1);
        fields.put("DESC_ABREVIADA", 2);
        fields.put("TIPO", 3);
        fields.put("SENSIBLE_MAYUSCULA", 4);
        fields.put("CREAC_NOMBRE", 5);
        fields.put("ESTADO", 6);
        fields.put("REVISADO", 7);
        fields.put("CONSULTAR", 8);
        fields.put("OBSERVACION", 9);
        fields.put("SINONIMO", 10);
        fields.put("SCT_ID", 11);
        fields.put("SCT_TERMINO", 12);
        fields.put("DCI_DID", 13);
        fields.put("DCI_TERMINO", 14);
        fields.put("GRUPOS_JERARQUICOS", 15);
        fields.put("RIESGO_TERATOGENICO", 16);
    }

    static int LENGHT = fields.size();

    public SubstanceLoader(Category category, User user) {
        super(category, user);
        //nonUpdateableDefinitions.add(category.findRelationshipDefinitionsByName());
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
            Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Sustancia");

            /*Recuperando descripcion preferida*/
            String term = StringUtils.normalizeSpaces(tokens[fields.get("DESCRIPCION")]).trim();

            init(type, category, term);

            if(type.equals("M")) {
                return;
            }

            RelationshipDefinition relationshipDefinition;

            /*Recuperando datos Relaciones*/
            String idConceptSCTName = tokens[fields.get("SCT_ID")];

            /*Por defecto se mapea a un concepto SNOMED Genérico*/
            long idConceptSCT = 373873005;

            if(!idConceptSCTName.isEmpty() && !idConceptSCTName.equals("NA")) {
                idConceptSCT = Long.parseLong(idConceptSCTName);
            }

            if(true) {

                String relationshipType = ES_UN;

                ConceptSCT conceptSCT = snomedCTManager.getConceptByID(idConceptSCT);

                if (conceptSCT == null) {
                    throw new LoadException(path.toString(), id, "Relación referencia a concepto SCT inexistente", ERROR, type);
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
                            throw new LoadException(path.toString(), id, "No existe un tipo de relación de nombre: " + relationshipType, ERROR, type);
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

                addRelationship(relationshipSnomed, type);

            }

            /**Obteniendo Comercializado**/
            BasicTypeValue basicTypeValue = new BasicTypeValue(true);
            relationshipDefinition = newConcept.getCategory().findRelationshipDefinitionsByName(TargetDefinition.COMERCIALIZADO).get(0);

            Relationship relationshipMarketed = new Relationship(newConcept, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

            addRelationship(relationshipMarketed, type);

            /* Obteniendo DCI */
            String dciName = tokens[fields.get("DCI_TERMINO")];

            if(!StringUtils.isEmpty(dciName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Mapear a DCI").get(0);

                HelperTable helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<HelperTableRow> dci = helperTableManager.searchRows(helperTable, dciName);

                if(dci.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un dci con glosa: "+dciName, ERROR, type);
                }

                Relationship relationshipDCI = new Relationship(newConcept, dci.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                addRelationship(relationshipDCI, type);
            }

            addConcept(type);
        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, e.toString(), ERROR, type);
        }
    }

}
