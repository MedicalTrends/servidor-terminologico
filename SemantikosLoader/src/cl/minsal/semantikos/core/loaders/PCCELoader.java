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
public class PCCELoader extends BaseLoader {

    static int OFFSET = SubstanceLoader.LENGHT + MBLoader.LENGHT + MCLoader.LENGHT + MCCELoader.LENGHT + GFPLoader.LENGHT + FPLoader.LENGHT + PCLoader.LENGHT;

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
        fields.put("PACK_MULTI_CANTIDAD", OFFSET + 13);
        fields.put("PACK_MULTI_UNIDAD_DESC", OFFSET + 14);
        fields.put("MED_CLINICO_CON_ENVASE_COD", OFFSET + 15);
        fields.put("MED_CLINICO_CON_ENVASE_DESC", OFFSET + 16);
        fields.put("PRODUCTO_COMERCIAL_COD", OFFSET + 17);
        fields.put("PRODUCTO_COMERCIAL_DESC", OFFSET + 18);
        fields.put("EXISTE_EN_GS1_DESC", OFFSET + 19);
        fields.put("GTIN_GS1", OFFSET + 20);
    }

    static int LENGHT = fields.size();

    public PCCELoader(Category category, User user) {

        super(category, user);

        nonUpdateableDefinitions.add(category.findRelationshipDefinitionsByName("Producto Comercial").get(0));
        nonUpdateableDefinitions.add(category.findRelationshipDefinitionsByName("Medicamento Clínico con Envase").get(0));
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

            /*Recuperando tipo*/
            String type = tokens[fields.get("TIPO")];

            /*Recuperando descripcion preferida*/
            String term = StringUtils.normalizeSpaces(tokens[fields.get("DESCRIPCION")]).trim();

            init(type, category, term);

            if(type.equals("M")) {
                return;
            }

            /*Recuperando datos Relaciones*/
            String idConceptSCTName = tokens[fields.get("SCT_ID")];

            /*Por defecto se mapea a un concepto SNOMED Genérico*/
            long idConceptSCT = 373873005;

            if(!idConceptSCTName.isEmpty()) {
                idConceptSCT = Long.parseLong(tokens[fields.get("SCT_ID")]);
            }

            String relationshipType = ES_UN;

            ConceptSCT conceptSCT = snomedCTManager.getConceptByID(idConceptSCT);

            if(conceptSCT == null) {
                throw new LoadException(path.toString(), id, "Relación referencia a concepto SCT inexistente", ERROR);
            }

            /**Se obtiene la definición de relacion SNOMED CT**/
            RelationshipDefinition relationshipDefinition = category.findRelationshipDefinitionsByName(TargetDefinition.SNOMED_CT).get(0);

            RelationshipAttributeDefinition attDef;

            RelationshipAttribute ra;

            Relationship relationshipSnomed = new Relationship(newConcept, conceptSCT, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

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

            addRelationship(relationshipSnomed, type);

            /* Generando Comercializado */

            BasicTypeValue basicTypeValue = new BasicTypeValue(true);

            relationshipDefinition = category.findRelationshipDefinitionsByName(TargetDefinition.COMERCIALIZADO).get(0);

            Relationship relationshipMarketed = new Relationship(newConcept, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

            addRelationship(relationshipMarketed, type);

            /*Recuperando Producto Comercial*/

            String pcName = tokens[fields.get("PRODUCTO_COMERCIAL_DESC")];

            if(!StringUtils.isEmpty(pcName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Producto Comercial").get(0);

                List<Description> pc = descriptionManager.searchDescriptionsPerfectMatch(StringUtils.normalizeSpaces(pcName).trim(), Arrays.asList(new Category[]{CategoryFactory.getInstance().findCategoryByName("Fármacos - Producto Comercial")}), null);

                if(pc.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un PC con preferida: "+pcName, ERROR);
                }

                if(!pc.get(0).getConceptSMTK().isModeled()) {
                    throw new LoadException(path.toString(), id, "EL PC: "+pcName+" no está modelado, se descarta este MC", ERROR);
                }

                Relationship relationshipPC = new Relationship(newConcept, pc.get(0).getConceptSMTK(), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                addRelationship(relationshipPC, type);
            }

            /*Recuperando Medicamento Clínico con Envase*/

            String mcceName = tokens[fields.get("MED_CLINICO_CON_ENVASE_DESC")];

            if(!StringUtils.isEmpty(mcceName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Medicamento Clínico con Envase").get(0);

                List<Description> mcce = descriptionManager.searchDescriptionsPerfectMatch(StringUtils.normalizeSpaces(mcceName).trim(), Arrays.asList(new Category[]{CategoryFactory.getInstance().findCategoryByName("Fármacos - Medicamento Clínico con Envase")}), null);

                if(mcce.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un MCCE con preferida: "+pcName, ERROR);
                }

                if(!mcce.get(0).getConceptSMTK().isModeled()) {
                    throw new LoadException(path.toString(), id, "EL MCCE: "+pcName+" no está modelado, se descarta este MC", ERROR);
                }

                Relationship relationshipMCCE = new Relationship(newConcept, mcce.get(0).getConceptSMTK(), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                addRelationship(relationshipMCCE, type);
            }

            /* Generando Existe GS1 */

            basicTypeValue = new BasicTypeValue(false);
            relationshipDefinition = category.findRelationshipDefinitionsByName("Existe en GS1").get(0);

            Relationship relationshipExistGS1 = new Relationship(newConcept, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

            addRelationship(relationshipExistGS1, type);

            addConcept(type);
        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: "+e.toString(), ERROR);
        }

    }

}

