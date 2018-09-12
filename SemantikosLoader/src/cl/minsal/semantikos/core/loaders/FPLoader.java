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
public class FPLoader extends BaseLoader {

    static int OFFSET = SubstanceLoader.LENGHT + MBLoader.LENGHT + MCLoader.LENGHT + MCCELoader.LENGHT + GFPLoader.LENGHT;

    static
    {
        fields = new LinkedHashMap<>();
        fields.put("CONCEPTO_ID", OFFSET + 0);
        fields.put("DESCRIPCION", OFFSET + 1);
        fields.put("DESC_ABREVIADA", OFFSET + 2);
        fields.put("TIPO", OFFSET + 3);
        fields.put("ESTADO", OFFSET + 4);
        fields.put("SENSIBLE_MAYUSCULA", OFFSET + 5);
        fields.put("CREAC_NOMBRE", OFFSET + 6);
        fields.put("REVISADO", OFFSET + 7);
        fields.put("CONSULTAR", OFFSET + 8);
        fields.put("SCT_ID", OFFSET + 9);
        fields.put("SCT_TERMINO", OFFSET + 10);
        fields.put("SINONIMO", OFFSET + 11);
        fields.put("GRUPOS_JERARQUICOS", OFFSET + 12);
        fields.put("GRUPO_FAMILIA_PRODUCTO_FK", OFFSET + 13);
        fields.put("GRUPO_FAMILIA_PRODUCTO_DESC", OFFSET + 14);
        fields.put("GRUPO_FAMILIA_GENERICA_DESC", OFFSET + 15);
    }

    static int LENGHT = fields.size();

    public FPLoader(Category category, User user) {
        super(category, user);
    }

    public void loadConceptFromFileLine(String line) throws LoadException {

        tokens = line.split(separator,-1);

        /*Recuperando datos Concepto*/

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        String id = StringUtils.normalizeSpaces(tokens[fields.get("CONCEPTO_ID")]).trim();

        // Si esta linea no contiene un concepto retornar inmediatamente
        if(StringUtils.isEmpty(id)) {
            return;
        }

        try {

            /*Estableciendo categoría*/
            Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Familia de Productos");

            /*Recuperando tipo*/
            String type = tokens[fields.get("TIPO")];

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
                    throw new LoadException(path.toString(), id, "Relación referencia a concepto SCT inexistente", ERROR);
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

            }

            BasicTypeValue basicTypeValue = new BasicTypeValue(true);

            relationshipDefinition = category.findRelationshipDefinitionsByName(TargetDefinition.COMERCIALIZADO).get(0);

            Relationship relationshipMarketed = new Relationship(newConcept, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

            addRelationship(relationshipMarketed, type);

            /*Recuperando Familia Genérica*/

            String genericFamilyName = tokens[fields.get("GRUPO_FAMILIA_GENERICA_DESC")];

            if(!genericFamilyName.isEmpty()) {

                basicTypeValue = new BasicTypeValue(genericFamilyName.trim().equalsIgnoreCase("Si"));
                relationshipDefinition = category.findRelationshipDefinitionsByName("Familia Genérica").get(0);

                Relationship relationshipSaleCondition = new Relationship(newConcept, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                addRelationship(relationshipSaleCondition, type);
            }

            /*Recuperando GFP*/

            String gfpName = tokens[fields.get("GRUPO_FAMILIA_PRODUCTO_DESC")];

            if(!StringUtils.isEmpty(gfpName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Grupo de Familia de Producto").get(0);

                List<Description> gfp = descriptionManager.searchDescriptionsPerfectMatch(StringUtils.normalizeSpaces(gfpName).trim(), Arrays.asList(new Category[]{CategoryFactory.getInstance().findCategoryByName("Fármacos - Grupo de Familia de Producto")}), null);

                if(gfp.isEmpty()) {
                    SMTKLoader.logError(new LoadException(path.toString(), id, "No existe un GFP con preferida: "+gfpName, ERROR));
                    //throw new LoadException(path.toString(), id, "No existe un GFP con preferida: "+gfpName, ERROR);
                }
                else {
                    if(!gfp.get(0).getConceptSMTK().isModeled()) {
                        throw new LoadException(path.toString(), id, "EL GFP: "+gfpName+" no está modelado, se descarta esta FP", ERROR);
                    }

                    Relationship relationshipGFP = new Relationship(newConcept, gfp.get(0).getConceptSMTK(), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));
                    addRelationship(relationshipGFP, type);
                }
            }

            addConcept(type);

        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: "+e.toString(), ERROR);
        }
    }

}
