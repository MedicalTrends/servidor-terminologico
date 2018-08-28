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
public class MCLoader extends BaseLoader {

    static int OFFSET = SubstanceLoader.fields.size() + MBLoader.fields.size();

    static
    {
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
        fields.put("PRESC_DESC", OFFSET + 13);
        fields.put("MEDICAMENTO_BASICO_ID", OFFSET + 14);
        fields.put("MEDICAMENTO_BASICO_DESC", OFFSET + 15);
        fields.put("FRM_FARMA_AGRP_DESC", OFFSET + 16);
        fields.put("CONDICION_VENTA_DESC", OFFSET + 17);
        fields.put("TIPO_FORMA_AGRP_DESC", OFFSET + 18);
        fields.put("UNIDOSIS_LOGISTICA_CANT", OFFSET + 19);
        fields.put("UNIDOSIS_LOGISTICA_UNIDAD_DESC", OFFSET + 20);
        fields.put("UNIDOSIS_ASISTENCIAL_CANT", OFFSET + 21);
        fields.put("UNI_ASISTENCIAL_UNIDAD_DESC", OFFSET + 22);
        fields.put("VOLUMEN_TOTAL_CANTIDAD", OFFSET + 23);
        fields.put("VOLUMEN_TOTAL_UNIDAD_DESC", OFFSET + 24);
        fields.put("ATC_DESCRIPCION_DESC", OFFSET + 25);
        fields.put("URL_MEDLINE_PLUS", OFFSET + 26);
        fields.put("VIAS_ADMINISTRACION", OFFSET + 27);
        fields.put("MC_SUST", OFFSET + 28);
    }

    static int LENGHT = fields.size();

    public static final Map<String, Integer> admViasFields;
    static
    {
        admViasFields = new HashMap<String, Integer>();
        admViasFields.put("FK_CCTNU_CONCEPTO_ID", 0);
        admViasFields.put("CCTNU_CONCEPTO_ID", 1);
        admViasFields.put("CCTVA_DESCRIPCION_USUARIO", 2);
        admViasFields.put("CRTNU_ORDEN", 3);
        admViasFields.put("ESTADO", 4);
    }

    public MCLoader(User user) {
        super(user);

        Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Medicamento Clínico");
        nonUpdateableDefinitions.add(category.findRelationshipDefinitionsByName(TargetDefinition.MC_SPECIAL).get(0));
        nonUpdateableDefinitions.add(category.findRelationshipDefinitionsByName(TargetDefinition.SUSTANCIA).get(0));
        nonUpdateableDefinitions.add(category.findRelationshipDefinitionsByName(TargetDefinition.CANTIDAD_VOLUMEN_TOTAL).get(0));
        nonUpdateableDefinitions.add(category.findRelationshipDefinitionsByName(TargetDefinition.FFA).get(0));
    }


    public void loadConceptFromFileLine(String line) throws LoadException {

        String[] tokens = line.split(separator,-1);

        /*Recuperando datos Concepto*/

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        String id = StringUtils.normalizeSpaces(tokens[fields.get("CONCEPTO_ID")]).trim();

        try {

            /*Estableciendo categoría*/
            Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Medicamento Clínico");

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

            HelperTable helperTable;

            /*Generando Comercializado*/
            BasicTypeValue basicTypeValue = new BasicTypeValue(true);

            relationshipDefinition = category.findRelationshipDefinitionsByName(TargetDefinition.COMERCIALIZADO).get(0);

            Relationship relationshipMarketed = new Relationship(newConcept, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

            addRelationship(relationshipMarketed, type);

            boolean mcSpecialValue = true;

            /*Recuperando Sustancias*/
            relationshipDefinition = category.findRelationshipDefinitionsByName("Sustancia").get(0);

            String substances = tokens[fields.get("MC_SUST")];

            String[] substancesTokens = substances.split("•");

            for (String substanceToken : substancesTokens) {

                if(substanceToken.isEmpty() || substanceToken.equals("\"")) {
                    continue;
                }

                String[] substanceTokens = substanceToken.split("¦");

                // Obteniendo Sustancia
                String termFavourite = StringUtils.normalizeSpaces(substanceTokens[0]).trim();

                List<Description> substanceList = descriptionManager.searchDescriptionsPerfectMatch(termFavourite, Arrays.asList(new Category[]{CategoryFactory.getInstance().findCategoryByName("Fármacos - Sustancia")}), null);

                if(substanceList.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe una sustancia con preferida: "+termFavourite, ERROR);
                }

                if(!substanceList.get(0).getConceptSMTK().isModeled()) {
                    throw new LoadException(path.toString(), id, "La sustancia: "+termFavourite+" no está modelada, se descarta este MC", ERROR);
                }

                Relationship relationshipSubstance = new Relationship(newConcept, substanceList.get(0).getConceptSMTK(), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                // Obteniendo Orden
                BasicTypeValue order = new BasicTypeValue(Integer.parseInt(substanceTokens[3].trim()));

                /**Para esta definición, se obtiente el atributo orden**/
                for (RelationshipAttributeDefinition attDef1 : relationshipDefinition.getRelationshipAttributeDefinitions()) {

                    if (attDef1.isOrderAttribute()) {
                        ra = new RelationshipAttribute(attDef1, relationshipSubstance, order);
                        relationshipSubstance.getRelationshipAttributes().add(ra);
                    }
                }

                // Obteniendo Cantidad y Unidad Potencia
                if(!StringUtils.isEmpty(substanceTokens[1].trim())) {

                    String[] potenciaTokens = substanceTokens[1].trim().split(" ");

                    BasicTypeValue cantidadPotencia;
                    int unitIndex = 1;

                    // Cantidad
                    try {
                        cantidadPotencia = new BasicTypeValue(Float.parseFloat(potenciaTokens[0].trim().replace(",",".")));
                    }
                    catch (NumberFormatException e) {
                        cantidadPotencia = new BasicTypeValue(1.0);
                        unitIndex = 0;
                    }

                    attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Cantidad Potencia").get(0);

                    ra = new RelationshipAttribute(attDef, relationshipSubstance, cantidadPotencia);
                    relationshipSubstance.getRelationshipAttributes().add(ra);

                    //Unidad
                    attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Unidad Potencia").get(0);

                    helperTable = (HelperTable) attDef.getTargetDefinition();

                    List<String> columnNames = new ArrayList<>();

                    columnNames.add("DESCRIPCION ABREVIADA");

                    List<HelperTableRow> unidadPotencia = helperTableManager.searchRows(helperTable, potenciaTokens[unitIndex].trim(), columnNames);

                    ra = new RelationshipAttribute(attDef, relationshipSubstance, unidadPotencia.get(0));
                    relationshipSubstance.getRelationshipAttributes().add(ra);

                }

                // Obteniendo PP
                if(!StringUtils.isEmpty(substanceTokens[2].trim())) {

                    String[] partidoPorTokens = substanceTokens[2].trim().split(" ");

                    BasicTypeValue cantidadPartidoPor;

                    int unitIndex = 1;

                    // Cantidad
                    try {
                        cantidadPartidoPor = new BasicTypeValue(Float.parseFloat(partidoPorTokens[0].trim().replace(",",".")));
                    }
                    catch (NumberFormatException e) {
                        cantidadPartidoPor = new BasicTypeValue(1.0);
                        unitIndex = 0;
                    }

                    attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Cantidad PP").get(0);

                    ra = new RelationshipAttribute(attDef, relationshipSubstance, cantidadPartidoPor);
                    relationshipSubstance.getRelationshipAttributes().add(ra);

                    //Unidad
                    attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Unidad PP").get(0);

                    helperTable = (HelperTable) attDef.getTargetDefinition();

                    List<String> columnNames = new ArrayList<>();

                    columnNames.add("DESCRIPCION ABREVIADA");

                    List<HelperTableRow> unidadPartidoPor = helperTableManager.searchRows(helperTable, partidoPorTokens[unitIndex].trim(), columnNames);

                    ra = new RelationshipAttribute(attDef, relationshipSubstance, unidadPartidoPor.get(0));
                    relationshipSubstance.getRelationshipAttributes().add(ra);
                }

                mcSpecialValue = false;

                addRelationship(relationshipSubstance, type);
            }

            /*Generando MC Especial*/

            basicTypeValue = new BasicTypeValue(mcSpecialValue);

            relationshipDefinition = category.findRelationshipDefinitionsByName("Medicamento clínico especial").get(0);

            Relationship relationshipMCSpecial = new Relationship(newConcept, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

            addRelationship(relationshipMCSpecial, type);

            /*Recuperando Estado Prescripción*/

            String prescriptionStateName = tokens[fields.get("PRESC_DESC")];

            if(!StringUtils.isEmpty(prescriptionStateName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Estado Prescripción").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<HelperTableRow> prescriptionState = helperTableManager.searchRows(helperTable, prescriptionStateName);

                if(prescriptionState.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un Estado Prescripción con glosa: "+prescriptionStateName, ERROR);
                }

                Relationship relationshipPrescriptionState = new Relationship(newConcept, prescriptionState.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                addRelationship(relationshipPrescriptionState, type);
            }

            /*Recuperando Medicamento Básico*/

            String mbName = tokens[fields.get("MEDICAMENTO_BASICO_DESC")];

            if(!StringUtils.isEmpty(mbName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Medicamento Básico").get(0);

                List<Description> mb = descriptionManager.searchDescriptionsPerfectMatch(StringUtils.normalizeSpaces(mbName).trim(), Arrays.asList(new Category[]{CategoryFactory.getInstance().findCategoryByName("Fármacos - Medicamento Básico")}), null);

                if(mb.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un MB con preferida: "+mbName, ERROR);
                }

                if(!mb.get(0).getConceptSMTK().isModeled()) {
                    throw new LoadException(path.toString(), id, "EL MB: "+mbName+" no está modelado, se descarta este MC", ERROR);
                }

                Relationship relationshipMB = new Relationship(newConcept, mb.get(0).getConceptSMTK(), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                addRelationship(relationshipMB, type);
            }

            /*Recuperando FFA*/

            String ffaName = tokens[fields.get("FRM_FARMA_AGRP_DESC")];

            if(!StringUtils.isEmpty(ffaName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("FFA").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<HelperTableRow> ffa = helperTableManager.searchRows(helperTable, ffaName);

                if(ffa.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe una FFA con glosa: "+ffaName, ERROR);
                }

                Relationship relationshipFFA = new Relationship(newConcept, ffa.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                //Tipo FFA
                attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Tipo FFA").get(0);

                String ffaTypeName = tokens[fields.get("TIPO_FORMA_AGRP_DESC")];

                helperTable = (HelperTable) attDef.getTargetDefinition();

                List<HelperTableRow> ffaType = helperTableManager.searchRows(helperTable, ffaTypeName);

                if(ffaType.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un tipo FFA con glosa: "+ffaTypeName, ERROR);
                }

                ra = new RelationshipAttribute(attDef, relationshipFFA, ffaType.get(0));
                relationshipFFA.getRelationshipAttributes().add(ra);

                // Orden
                BasicTypeValue order = new BasicTypeValue(1);

                attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Orden").get(0);

                ra = new RelationshipAttribute(attDef, relationshipFFA, order);
                relationshipFFA.getRelationshipAttributes().add(ra);

                addRelationship(relationshipFFA, type);
            }

            /*Recuperando Condición de Venta*/

            String saleConditionName = tokens[fields.get("CONDICION_VENTA_DESC")];

            if(!StringUtils.isEmpty(saleConditionName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Condición de Venta").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<HelperTableRow> prescriptionState = helperTableManager.searchRows(helperTable, saleConditionName);

                if(prescriptionState.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe una Condición de Venta con glosa: "+saleConditionName, ERROR);
                }

                Relationship relationshipSaleCondition = new Relationship(newConcept, prescriptionState.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                addRelationship(relationshipSaleCondition, type);
            }

            /*Recuperando Unidad de UAsist*/

            String assistanceUnitName = tokens[fields.get("UNI_ASISTENCIAL_UNIDAD_DESC")];

            if(!StringUtils.isEmpty(assistanceUnitName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Unidad de U_Asist").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<HelperTableRow> assistanceUnit = helperTableManager.searchRows(helperTable, assistanceUnitName);

                if(assistanceUnit.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe una Unidad de U Asist con glosa: "+assistanceUnitName, ERROR);
                }

                Relationship relationshipSaleCondition = new Relationship(newConcept, assistanceUnit.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                addRelationship(relationshipSaleCondition, type);
            }

            /*Recuperando Volumen Total*/

            String volumeName = tokens[fields.get("VOLUMEN_TOTAL_CANTIDAD")];

            if(!StringUtils.isEmpty(volumeName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Cantidad de Volumen Total").get(0);

                basicTypeValue = new BasicTypeValue(Float.parseFloat(volumeName.replace(",",".")));

                Relationship relationshipVolume = new Relationship(newConcept, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                //Unidad Volumen
                attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Unidad de Volumen").get(0);

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

            /*Recuperando URL*/

            String urlName = tokens[fields.get("URL_MEDLINE_PLUS")];

            if(!urlName.isEmpty()) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("URL").get(0);

                basicTypeValue = new BasicTypeValue(new String(urlName));

                Relationship relationshipURL = new Relationship(newConcept, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                addRelationship(relationshipURL, type);
            }

             /*Recuperando ATC*/

            String atcName = tokens[fields.get("ATC_DESCRIPCION_DESC")];

            if(!StringUtils.isEmpty(atcName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("ATC").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<String> columnNames = new ArrayList<>();

                columnNames.add("codigo atc");

                List<HelperTableRow> atc = helperTableManager.searchRows(helperTable, atcName, columnNames);

                if(atc.isEmpty()) {
                    SMTKLoader.logError(new LoadException(path.toString(), id, "No existe un ATC con código: "+atcName, ERROR));
                    //throw new LoadException(path.toString(), id, "No existe un ATC con código: "+atcName, ERROR);
                }
                else {
                    Relationship relationshipATC = new Relationship(newConcept, atc.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                    addRelationship(relationshipATC, type);
                }

            }

            addConcept(type);
        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: "+e.toString(), ERROR);

        }

    }

}
