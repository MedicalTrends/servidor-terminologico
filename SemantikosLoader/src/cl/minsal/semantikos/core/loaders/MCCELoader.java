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
public class MCCELoader extends BaseLoader {

    static int OFFSET = SubstanceLoader.fields.size() + MBLoader.fields.size() + MCLoader.fields.size();

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
        fields.put("MEDICAMENTO_CLINICO_FK", OFFSET + 13);
        fields.put("MEDICAMENTO_CLINICO_DESC", OFFSET + 14);
        fields.put("TIPO_DESC", OFFSET + 15);
        fields.put("CANTIDAD", OFFSET + 16);
        fields.put("UNIDAD_MEDIDA_CANTIDAD_DESC", OFFSET + 17);
        fields.put("VOLUMEN_TOTAL_CANTIDAD", OFFSET + 18);
        fields.put("VOLUMEN_TOTAL_UNIDAD_DESC", OFFSET + 19);
        fields.put("PACK_MULTI_CANTIDAD", OFFSET + 20);
        fields.put("PACK_MULTI_UNIDAD_DESC", OFFSET + 21);
    }

    public MCCELoader(User user) {
        super(user);

        Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Medicamento Clínico con Envase");
        nonUpdateableDefinitions.add(category.findRelationshipDefinitionsByName("Medicamento Clínico").get(0));
    }

    public void loadConceptFromFileLine(String line) throws LoadException {

        String[] tokens = line.split(separator,-1);

        /*Recuperando datos Concepto*/

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        String id = StringUtils.normalizeSpaces(tokens[fields.get("CONCEPTO_ID")]).trim();

        try {

            /*Estableciendo categoría*/
            Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Medicamento Clínico con Envase");

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
                        throw new LoadException(path.toString(), id, "No existe un tipo de relación de nombre: " + relationshipType, ERROR);
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

            HelperTable helperTable;

            /*Generando Comercializado*/
            BasicTypeValue basicTypeValue = new BasicTypeValue(true);

            relationshipDefinition = category.findRelationshipDefinitionsByName(TargetDefinition.COMERCIALIZADO).get(0);

            Relationship relationshipMarketed = new Relationship(newConcept, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

            addRelationship(relationshipMarketed, type);

            /*Recuperando Medicamento Clínico*/

            String mcName = tokens[fields.get("MEDICAMENTO_CLINICO_DESC")];

            if(!StringUtils.isEmpty(mcName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Medicamento Clínico").get(0);

                List<Description> mc = descriptionManager.searchDescriptionsPerfectMatch(StringUtils.normalizeSpaces(mcName).trim(), Arrays.asList(new Category[]{CategoryFactory.getInstance().findCategoryByName("Fármacos - Medicamento Clínico")}), null);

                if(mc.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un MC con preferida: "+mcName, ERROR);
                }

                if(!mc.get(0).getConceptSMTK().isModeled()) {
                    throw new LoadException(path.toString(), id, "EL MC: "+mcName+" no está modelado, se descarta este MC", ERROR);
                }

                Relationship relationshipMC = new Relationship(newConcept, mc.get(0).getConceptSMTK(), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                addRelationship(relationshipMC, type);
            }

            /*Recuperando Condición de Venta*/

            String mcceTypeName = tokens[fields.get("TIPO_DESC")];

            if(!StringUtils.isEmpty(mcceTypeName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Tipo de MCCE").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<HelperTableRow> prescriptionState = helperTableManager.searchRows(helperTable, mcceTypeName);

                if(prescriptionState.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un Tipo de MCCE con glosa: "+mcceTypeName, ERROR);
                }

                Relationship relationshipSaleCondition = new Relationship(newConcept, prescriptionState.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                addRelationship(relationshipSaleCondition, type);
            }

            /*Recuperando Cantidad*/

            String quantityName = tokens[fields.get("CANTIDAD")];

            if(!quantityName.trim().isEmpty()) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Cantidad").get(0);

                basicTypeValue = new BasicTypeValue(Integer.parseInt(quantityName.trim()));

                Relationship relationshipQuantity = new Relationship(newConcept, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                //Unidad
                attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Unidad").get(0);

                String quantityUnitName = tokens[fields.get("UNIDAD_MEDIDA_CANTIDAD_DESC")];

                helperTable = (HelperTable) attDef.getTargetDefinition();

                List<HelperTableRow> volumneUnit = helperTableManager.searchRows(helperTable, quantityUnitName);

                ra = new RelationshipAttribute(attDef, relationshipQuantity, volumneUnit.get(0));
                relationshipQuantity.getRelationshipAttributes().add(ra);

                addRelationship(relationshipQuantity, type);
            }

            /*Recuperando Cantidad Pack Multi*/

            String quantityPackMultiName = tokens[fields.get("PACK_MULTI_CANTIDAD")];

            if(!quantityPackMultiName.trim().isEmpty()) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Cantidad Pack Multi").get(0);

                basicTypeValue = new BasicTypeValue(Integer.parseInt(quantityName.trim()));

                Relationship relationshipQuantityPackMulti = new Relationship(newConcept, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                //Unidad
                attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Unidad Pack Multi").get(0);

                String quantityPackMultiUnitName = tokens[fields.get("PACK_MULTI_UNIDAD_DESC")];

                helperTable = (HelperTable) attDef.getTargetDefinition();

                List<HelperTableRow> volumneUnit = helperTableManager.searchRows(helperTable, quantityPackMultiUnitName);

                ra = new RelationshipAttribute(attDef, relationshipQuantityPackMulti, volumneUnit.get(0));
                relationshipQuantityPackMulti.getRelationshipAttributes().add(ra);

                addRelationship(relationshipQuantityPackMulti, type);
            }

            /*Recuperando Volumen Total*/

            String volumeName = tokens[fields.get("VOLUMEN_TOTAL_CANTIDAD")];

            if(!StringUtils.isEmpty(volumeName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Cantidad Volumen Total").get(0);

                basicTypeValue = new BasicTypeValue(Float.parseFloat(volumeName.replace(",",".")));

                Relationship relationshipVolume = new Relationship(newConcept, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                //Unidad Volumen
                attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Unidad Volumen Total").get(0);

                String volumeUnitName = tokens[fields.get("VOLUMEN_TOTAL_UNIDAD_DESC")];

                if(volumeUnitName.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No se ha especificado una unidad para esta cantidad de volumen total: "+volumeName, ERROR);
                }

                helperTable = (HelperTable) attDef.getTargetDefinition();

                List<HelperTableRow> volumneUnit = helperTableManager.searchRows(helperTable, volumeUnitName);

                ra = new RelationshipAttribute(attDef, relationshipVolume, volumneUnit.get(0));
                relationshipVolume.getRelationshipAttributes().add(ra);

                addRelationship(relationshipVolume, type);
            }

            addConcept(type);

        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: "+e.toString(), ERROR);
        }

    }

}
