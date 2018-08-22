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
import cl.minsal.semantikos.model.tags.Tag;
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
public class MBLoader extends BaseLoader {

    static int OFFSET = SubstanceLoader.fields.size();

    static
    {
        fields.put("CONCEPTO_ID", OFFSET + 0);
        fields.put("DESCRIPCION", OFFSET + 1);
        fields.put("TIPO", OFFSET + 2);
        fields.put("DESC_ABREVIADA", OFFSET + 3);
        fields.put("ESTADO", OFFSET + 4);
        fields.put("SENSIBLE_MAYUSCULA", OFFSET + 5);
        fields.put("CREAC_NOMBRE", OFFSET + 6);
        fields.put("REVISADO", OFFSET + 7);
        fields.put("CONSULTAR", OFFSET + 8);
        fields.put("SCT_ID", OFFSET + 9);
        fields.put("SCT_TERMINO", OFFSET + 10);
        fields.put("SINONIMO", OFFSET + 11);
        fields.put("GRUPOS_JERARQUICOS", OFFSET + 12);
        fields.put("SUSTANCIAS", OFFSET + 13);
    }

    public MBLoader(User user) {
        super(user);

        Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Sustancia");
        nonUpdateableDefinitions.add(category.findRelationshipDefinitionsByName(TargetDefinition.SUSTANCIA).get(0));
    }

    public void loadConceptFromFileLine(String line) throws LoadException {

        tokens = line.split(separator,-1);

        /*Recuperando datos Concepto*/

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        String id = StringUtils.normalizeSpaces(tokens[fields.get("CONCEPTO_ID")]).trim();

        try {

            /*Estableciendo categoría*/
            Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Medicamento Básico");

            /*Recuperando tipo*/
            String type = tokens[fields.get("TIPO")];

            /*Recuperando descripcion preferida*/
            String term = StringUtils.normalizeSpaces(tokens[fields.get("DESCRIPCION")]).trim();

            init(type, category, term);

            /*Recuperando datos Relaciones*/
            String idConceptSCTName = tokens[fields.get("SCT_ID")];

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
            RelationshipDefinition relationshipDefinition = category.findRelationshipDefinitionsByName(TargetDefinition.SNOMED_CT).get(0);

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

            RelationshipAttributeDefinition attDefGroup = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Grupo").get(0);

            RelationshipAttribute raGroup = new RelationshipAttribute(attDefGroup, relationshipSnomed, group);
            relationshipSnomed.getRelationshipAttributes().add(raGroup);

            addRelationship(relationshipSnomed, type);

            BasicTypeValue basicTypeValue = new BasicTypeValue(true);

            relationshipDefinition = newConcept.getCategory().findRelationshipDefinitionsByName(TargetDefinition.COMERCIALIZADO).get(0);

            Relationship relationshipMarketed = new Relationship(newConcept, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

            addRelationship(relationshipMarketed, type);

            relationshipDefinition = category.findRelationshipDefinitionsByName(TargetDefinition.SUSTANCIA).get(0);

            /*Recuperando Sustancias*/
            String substances = tokens[fields.get("SUSTANCIAS")];

            String[] substancesTokens = substances.split("•");

            for (String substanceToken : substancesTokens) {

                if(substanceToken.isEmpty() || substanceToken.equals("\"")) {
                    continue;
                }

                String[] substanceTokens = substanceToken.split("¦");

                String termFavourite = StringUtils.normalizeSpaces(substanceTokens[1]).trim();

                List<Description> substanceList = descriptionManager.searchDescriptionsPerfectMatch(termFavourite, Arrays.asList(new Category[]{CategoryFactory.getInstance().findCategoryByName("Fármacos - Sustancia")}), null);

                if(substanceList.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe una sustancia con preferida: " + termFavourite, ERROR);
                }

                if(!substanceList.get(0).getConceptSMTK().isModeled()) {
                    throw new LoadException(path.toString(), id, "La sustancia: " + termFavourite + " no está modelada, se descarta este MB", ERROR);
                }

                Relationship relationshipSubstance = new Relationship(newConcept, substanceList.get(0).getConceptSMTK(), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                BasicTypeValue order = new BasicTypeValue(Integer.parseInt(substanceTokens[2].trim()));

                /**Para esta definición, se obtiente el atributo orden**/
                for (RelationshipAttributeDefinition attDef : relationshipDefinition.getRelationshipAttributeDefinitions()) {

                    if (attDef.isOrderAttribute()) {
                        RelationshipAttribute ra;

                        ra = new RelationshipAttribute(attDef, relationshipSubstance, order);
                        relationshipSubstance.getRelationshipAttributes().add(ra);
                    }
                }
                addRelationship(relationshipSubstance, type);
            }

            addConcept(type);
        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: " + e.toString(), ERROR);
        }

    }

}
