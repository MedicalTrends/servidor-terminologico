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
public class MCConceptLoader extends EntityLoader {

    ConceptManager conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);
    DescriptionManager descriptionManager = (DescriptionManager) ServiceLocator.getInstance().getService(DescriptionManager.class);
    TagManager tagManager = (TagManager) ServiceLocator.getInstance().getService(TagManager.class);
    SnomedCTManager snomedCTManager = (SnomedCTManager) ServiceLocator.getInstance().getService(SnomedCTManager.class);
    HelperTablesManager helperTableManager = (HelperTablesManager) ServiceLocator.getInstance().getService(HelperTablesManager.class);

    public static final Map<String, Integer> mcConceptFields;
    static
    {
        mcConceptFields = new HashMap<String, Integer>();
        mcConceptFields.put("CONCEPTO_ID", 0);
        mcConceptFields.put("DESCRIPCION", 1);
        mcConceptFields.put("DESC_ABREVIADA", 2);
        mcConceptFields.put("ESTADO", 3);
        mcConceptFields.put("SENSIBLE_MAYUSCULA", 4);
        mcConceptFields.put("CREAC_NOMBRE", 5);
        mcConceptFields.put("REVISADO", 6);
        mcConceptFields.put("CONSULTAR", 7);
        mcConceptFields.put("SCT_ID", 8);
        mcConceptFields.put("SCT_TERMINO", 9);
        mcConceptFields.put("SINONIMO", 10);
        mcConceptFields.put("GRUPOS_JERARQUICOS", 11);
        mcConceptFields.put("PRESC_FK", 12);
        mcConceptFields.put("PRESC_DESC", 13);
        mcConceptFields.put("MEDICAMENTO_BASICO_FK", 14);
        mcConceptFields.put("MEDICAMENTO_BASICO_DESC", 15);
        mcConceptFields.put("FRM_FARMA_AGRP_FK", 16);
        mcConceptFields.put("FRM_FARMA_AGRP_DESC", 17);
        mcConceptFields.put("CONDICION_VENTA_FK", 18);
        mcConceptFields.put("CONDICION_VENTA_DESC", 19);
        mcConceptFields.put("ESTADO_PRESCRIPCION_FK", 20);
        mcConceptFields.put("ESTADO_PRESCRIPCION_DESC", 21);
        mcConceptFields.put("TIPO_FORMA_AGRP_FK", 22);
        mcConceptFields.put("TIPO_FORMA_AGRP_DESC", 23);
        mcConceptFields.put("UNIDOSIS_LOGISTICA_CANT", 24);
        mcConceptFields.put("UNIDOSIS_LOGISTICA_UNIDAD_FK", 25);
        mcConceptFields.put("UNIDOSIS_LOGISTICA_UNIDAD_DESC", 26);
        mcConceptFields.put("UNIDOSIS_ASISTENCIAL_CANT", 27);
        mcConceptFields.put("UNI_ASISTENCIAL_UNIDAD_FK", 28);
        mcConceptFields.put("UNI_ASISTENCIAL_UNIDAD_DESC", 29);
        mcConceptFields.put("VOLUMEN_TOTAL_CANTIDAD", 30);
        mcConceptFields.put("VOLUMEN_TOTAL_UNIDAD_FK", 31);
        mcConceptFields.put("VOLUMEN_TOTAL_UNIDAD_DESC", 32);
        mcConceptFields.put("ATC_DESCRIPCION_FK", 33);
        mcConceptFields.put("ATC_DESCRIPCION_DESC", 34);
        mcConceptFields.put("URL_MEDLINE_PLUS", 35);
        mcConceptFields.put("VIAS_ADMINISTRACION", 36);
        mcConceptFields.put("MC_SUST", 37);
    }

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

    Map<Long, ConceptSMTK> conceptSMTKMap = new HashMap<>();

    Map<String, Description> descriptionMap = new HashMap<>();

    Map<Long, List<Integer> > admVias = new HashMap<>();

    private static String MC_LOG = "Fármacos - Medicamento Clínico.log";


    public void loadConceptFromFileLine(String line, User user) throws LoadException {

        String[] tokens = line.split(separator,-1);
        long id = Long.parseLong(tokens[mcConceptFields.get("CONCEPTO_ID")]);

        try {

            /*Recuperando datos Concepto*/

            /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
            boolean toBeReviewed = tokens[mcConceptFields.get("REVISADO")].equals("Si");
            boolean toBeConsulted = tokens[mcConceptFields.get("CONSULTAR")].equals("Si");
            boolean autogenerated = tokens[mcConceptFields.get("CREAC_NOMBRE")].equals("Autogenerado");

            Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Medicamento Clínico");
            TagSMTK tagSMTK = TagSMTKFactory.getInstance().findTagSMTKByName("producto");

            ConceptSMTK conceptSMTK = new ConceptSMTK(category);
            conceptSMTK.setToBeConsulted(toBeConsulted);
            conceptSMTK.setToBeReviewed(toBeReviewed);
            conceptSMTK.setCategory(category);
            conceptSMTK.setTagSMTK(tagSMTK);

            /*Recuperando datos Descripciones*/

            /*Recuperando descripcion preferida*/
            String term = StringUtils.normalizeSpaces(tokens[mcConceptFields.get("DESCRIPCION")]).trim();
            boolean caseSensitive = tokens[mcConceptFields.get("SENSIBLE_MAYUSCULA")].equals("Sensible");
            DescriptionType descriptionType = DescriptionType.PREFERIDA;

            Description descriptionFavourite = new Description(conceptSMTK, term, descriptionType);
            descriptionFavourite.setCaseSensitive(caseSensitive);
            descriptionFavourite.setCreatorUser(user);
            descriptionFavourite.setAutogeneratedName(autogenerated);

            if(descriptionMap.containsKey(descriptionFavourite.getTerm())) {
                SMTKLoader.logWarning(new LoadLog("Término repetido para descripción "+descriptionFavourite.toString()+". Se descarta descripción", INFO));
            }
            else {
                descriptionMap.put(descriptionFavourite.getTerm(), descriptionFavourite);
                conceptSMTK.addDescription(descriptionFavourite);
            }

            /*Recuperando descripcion FSN*/
            term = descriptionFavourite.getTerm()+" ("+tagSMTK.getName()+")";
            descriptionType = DescriptionType.FSN;

            Description descriptionFSN = new Description(conceptSMTK, term, descriptionType);
            descriptionFSN.setCaseSensitive(caseSensitive);
            descriptionFSN.setCreatorUser(user);
            descriptionFSN.setAutogeneratedName(autogenerated);

            if(descriptionMap.containsKey(descriptionFSN.getTerm())) {
                SMTKLoader.logWarning(new LoadLog("Término repetido para descripción "+descriptionFSN.toString()+". Se descarta descripción", INFO));
            }
            else {
                descriptionMap.put(descriptionFSN.getTerm(), descriptionFSN);
                conceptSMTK.addDescription(descriptionFSN);
            }

            /*Recuperando descripcion Abreviada*/
            term = StringUtils.normalizeSpaces(tokens[mcConceptFields.get("DESC_ABREVIADA")]).trim();

            if(!StringUtils.isEmpty(term)) {
                descriptionType = DescriptionType.ABREVIADA;

                Description descriptionAbbreviated = new Description(conceptSMTK, term, descriptionType);
                descriptionAbbreviated.setCaseSensitive(caseSensitive);
                descriptionAbbreviated.setCreatorUser(user);
                descriptionAbbreviated.setAutogeneratedName(autogenerated);

                if(descriptionMap.containsKey(descriptionAbbreviated.getTerm())) {
                    SMTKLoader.logWarning(new LoadLog("Término repetido para descripción "+descriptionAbbreviated.toString()+". Se descarta descripción", INFO));
                }
                else {
                    descriptionMap.put(descriptionAbbreviated.getTerm(), descriptionAbbreviated);
                    conceptSMTK.addDescription(descriptionAbbreviated);
                }
            }

            /*Recuperando Sinónimos*/
            String synonyms = tokens[mcConceptFields.get("SINONIMO")];
            descriptionType = DescriptionType.SYNONYMOUS;

            String[] synonymsTokens = synonyms.split("•");

            for (String synonymsToken : synonymsTokens) {

                if(synonymsToken.isEmpty() || synonymsToken.equals("\"")) {
                    continue;
                }

                //term = StringUtils.normalizeSpaces(synonymsToken.split("-")[1]).trim();
                term = StringUtils.normalizeSpaces(synonymsToken).trim();

                Description description = new Description(conceptSMTK, term, descriptionType);
                description.setCaseSensitive(caseSensitive);
                description.setCreatorUser(user);
                description.setAutogeneratedName(autogenerated);

                if(descriptionMap.containsKey(description.getTerm())) {
                    SMTKLoader.logWarning(new LoadLog("Término repetido para descripción "+description.toString()+". Se descarta descripción", INFO));
                }
                else {
                    descriptionMap.put(description.getTerm(), description);
                    conceptSMTK.addDescription(description);
                }
            }

            descriptionMap.clear();

            /*Recuperando datos Relaciones*/
            String idConceptSCTName = tokens[mcConceptFields.get("SCT_ID")];

            /*Por defecto se mapea a un concepto SNOMED Genérico*/
            long idConceptSCT = 373873005;

            if(!idConceptSCTName.isEmpty()) {
                idConceptSCT = Long.parseLong(tokens[mcConceptFields.get("SCT_ID")]);
            }

            String relationshipType = ES_UN;

            ConceptSCT conceptSCT = snomedCTManager.getConceptByID(idConceptSCT);

            if(conceptSCT == null) {
                throw new LoadException(path.toString(), id, "Relación referencia a concepto SCT inexistente", ERROR);
            }

            /**Se obtiene la definición de relacion SNOMED CT**/
            RelationshipDefinition relationshipDefinition = conceptSMTK.getCategory().findRelationshipDefinitionsByName(TargetDefinition.SNOMED_CT).get(0);

            RelationshipAttributeDefinition attDef;

            RelationshipAttribute ra;

            Relationship relationshipSnomed = new Relationship(conceptSMTK, conceptSCT, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

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

            conceptSMTK.addRelationship(relationshipSnomed);

            HelperTable helperTable;

            /*Generando Comercializado*/
            BasicTypeValue basicTypeValue = new BasicTypeValue(true);

            relationshipDefinition = conceptSMTK.getCategory().findRelationshipDefinitionsByName(TargetDefinition.COMERCIALIZADO).get(0);

            Relationship relationshipMarketed = new Relationship(conceptSMTK, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

            conceptSMTK.addRelationship(relationshipMarketed);

            boolean mcSpecialValue = true;

            /*Recuperando Sustancias*/
            relationshipDefinition = category.findRelationshipDefinitionsByName("Sustancia").get(0);

            String substances = tokens[mcConceptFields.get("MC_SUST")];

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

                Relationship relationshipSubstance = new Relationship(conceptSMTK, substanceList.get(0).getConceptSMTK(), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                conceptSMTK.addRelationship(relationshipSubstance);

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
            }

            /*Generando MC Especial*/

            basicTypeValue = new BasicTypeValue(mcSpecialValue);

            relationshipDefinition = category.findRelationshipDefinitionsByName("Medicamento clínico especial").get(0);

            Relationship relationshipMCSpecial = new Relationship(conceptSMTK, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

            conceptSMTK.addRelationship(relationshipMCSpecial);

            /*Recuperando Estado Prescripción*/

            String prescriptionStateName = tokens[mcConceptFields.get("ESTADO_PRESCRIPCION_DESC")];

            if(!StringUtils.isEmpty(prescriptionStateName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Estado Prescripción").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<HelperTableRow> prescriptionState = helperTableManager.searchRows(helperTable, prescriptionStateName);

                if(prescriptionState.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un Estado Prescripción con glosa: "+prescriptionStateName, ERROR);
                }

                Relationship relationshipPrescriptionState = new Relationship(conceptSMTK, prescriptionState.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                conceptSMTK.addRelationship(relationshipPrescriptionState);
            }

            /*Recuperando Medicamento Básico*/

            String mbName = tokens[mcConceptFields.get("MEDICAMENTO_BASICO_DESC")];

            if(!StringUtils.isEmpty(mbName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Medicamento Básico").get(0);

                List<Description> mb = descriptionManager.searchDescriptionsPerfectMatch(StringUtils.normalizeSpaces(mbName).trim(), Arrays.asList(new Category[]{CategoryFactory.getInstance().findCategoryByName("Fármacos - Medicamento Básico")}), null);

                if(mb.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un MB con preferida: "+mbName, ERROR);
                }

                if(!mb.get(0).getConceptSMTK().isModeled()) {
                    throw new LoadException(path.toString(), id, "EL MB: "+mbName+" no está modelado, se descarta este MC", ERROR);
                }

                Relationship relationshipMB = new Relationship(conceptSMTK, mb.get(0).getConceptSMTK(), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                conceptSMTK.addRelationship(relationshipMB);
            }

            /*Recuperando FFA*/

            String ffaName = tokens[mcConceptFields.get("FRM_FARMA_AGRP_DESC")];

            if(!StringUtils.isEmpty(ffaName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("FFA").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<HelperTableRow> ffa = helperTableManager.searchRows(helperTable, ffaName);

                if(ffa.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe una FFA con glosa: "+ffaName, ERROR);
                }

                Relationship relationshipFFA = new Relationship(conceptSMTK, ffa.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                //Tipo FFA
                attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Tipo FFA").get(0);

                String ffaTypeName = tokens[mcConceptFields.get("TIPO_FORMA_AGRP_DESC")];

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

                conceptSMTK.addRelationship(relationshipFFA);
            }

            /*Recuperando Condición de Venta*/

            String saleConditionName = tokens[mcConceptFields.get("CONDICION_VENTA_DESC")];

            if(!StringUtils.isEmpty(saleConditionName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Condición de Venta").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<HelperTableRow> prescriptionState = helperTableManager.searchRows(helperTable, saleConditionName);

                if(prescriptionState.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe una Condición de Venta con glosa: "+saleConditionName, ERROR);
                }

                Relationship relationshipSaleCondition = new Relationship(conceptSMTK, prescriptionState.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                conceptSMTK.addRelationship(relationshipSaleCondition);
            }

            /*Recuperando Unidad de UAsist*/

            String assistanceUnitName = tokens[mcConceptFields.get("UNI_ASISTENCIAL_UNIDAD_DESC")];

            if(!StringUtils.isEmpty(assistanceUnitName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Unidad de U_Asist").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<HelperTableRow> assistanceUnit = helperTableManager.searchRows(helperTable, assistanceUnitName);

                if(assistanceUnit.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe una Unidad de U Asist con glosa: "+assistanceUnitName, ERROR);
                }

                Relationship relationshipSaleCondition = new Relationship(conceptSMTK, assistanceUnit.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                conceptSMTK.addRelationship(relationshipSaleCondition);
            }

            /*Recuperando Volumen Total*/

            String volumeName = tokens[mcConceptFields.get("VOLUMEN_TOTAL_CANTIDAD")];

            if(!StringUtils.isEmpty(volumeName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Cantidad de Volumen Total").get(0);

                basicTypeValue = new BasicTypeValue(Float.parseFloat(volumeName.replace(",",".")));

                Relationship relationshipVolume = new Relationship(conceptSMTK, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                //Unidad Volumen
                attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Unidad de Volumen").get(0);

                String volumeUnitName = tokens[mcConceptFields.get("VOLUMEN_TOTAL_UNIDAD_DESC")];

                if(volumeUnitName.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No se ha especificado una unidad para esta cantidad de volumen total: "+volumeName, ERROR);
                }

                helperTable = (HelperTable) attDef.getTargetDefinition();

                List<HelperTableRow> volumneUnit = helperTableManager.searchRows(helperTable, volumeUnitName);

                ra = new RelationshipAttribute(attDef, relationshipVolume, volumneUnit.get(0));
                relationshipVolume.getRelationshipAttributes().add(ra);

                conceptSMTK.addRelationship(relationshipVolume);
            }

            /*Recuperando URL*/

            String urlName = tokens[mcConceptFields.get("URL_MEDLINE_PLUS")];

            if(!urlName.isEmpty()) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("URL").get(0);

                basicTypeValue = new BasicTypeValue(new String(urlName));

                Relationship relationshipURL = new Relationship(conceptSMTK, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                conceptSMTK.addRelationship(relationshipURL);
            }

             /*Recuperando ATC*/

            String atcName = tokens[mcConceptFields.get("ATC_DESCRIPCION_DESC")];

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
                    Relationship relationshipATC = new Relationship(conceptSMTK, atc.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                    conceptSMTK.addRelationship(relationshipATC);
                }

            }

            conceptSMTKMap.put(id, conceptSMTK);
        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: "+e.toString(), ERROR);

        }

    }

    public void loadAdministrationVias(String line) throws LoadException {

        String[] tokens = line.split(separator,-1);

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        long id = Long.parseLong(tokens[admViasFields.get("FK_CCTNU_CONCEPTO_ID")]);

        try {

            Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Medicamento Clínico");

            long idConceptSMTK = id;

            RelationshipDefinition relationshipDefinition;

            RelationshipAttributeDefinition attDef;

            RelationshipAttribute ra;

            HelperTable helperTable;

            ConceptSMTK conceptSMTK = conceptSMTKMap.get(idConceptSMTK);

            if(conceptSMTK == null) {
                throw new LoadException(path.toString(), id, "Relación referencia a concepto SMTK inexistente", ERROR);
            }

            if(!admVias.containsKey(idConceptSMTK)) {
                admVias.put(idConceptSMTK, new ArrayList<Integer>());
            }
            else {
                admVias.get(idConceptSMTK).add(1);
            }

            /*Recuperando Unidad de UAsist*/

            String admViaName = tokens[admViasFields.get("CCTVA_DESCRIPCION_USUARIO")];

            if(!admViaName.trim().isEmpty()) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Vía Administración").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<HelperTableRow> admVia = helperTableManager.searchRows(helperTable, admViaName);

                if(admVia.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe una Vía Administración con glosa: "+admViaName, ERROR);
                }

                Relationship relationshipAdmVia = new Relationship(conceptSMTK, admVia.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                // Orden
                BasicTypeValue order = new BasicTypeValue(admVias.get(idConceptSMTK).size()+1);

                attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Orden").get(0);

                ra = new RelationshipAttribute(attDef, relationshipAdmVia, order);
                relationshipAdmVia.getRelationshipAttributes().add(ra);

                conceptSMTK.addRelationship(relationshipAdmVia);
            }
        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: "+e.toString(), ERROR);
        }
    }


    public void loadAllConcepts(SMTKLoader smtkLoader) {

        //smtkLoader.logInfo(new LoadLog("Comprobando Conceptos Fármacos - Medicamento Clínico", INFO));
        smtkLoader.printInfo(new LoadLog("Comprobando Conceptos Fármacos - Medicamento Clínico", INFO));

        try {

            initReader(smtkLoader.MC_PATH);
            initWriter(MC_LOG);

            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    loadConceptFromFileLine(line, smtkLoader.getUser());
                    //smtkLoader.incrementConceptsProcessed(1);
                    smtkLoader.printIncrementConceptsProcessed(1);
                }
                catch (LoadException e) {
                    //smtkLoader.logError(e);
                    //smtkLoader.printError(e);
                    //e.printStackTrace();
                    log(e);
                }
            }

            haltReader();

            initReader(smtkLoader.MC_VIAS_ADM_PATH);

            while ((line = reader.readLine()) != null) {
                try {
                    loadAdministrationVias(line);
                }
                catch (LoadException e) {
                    //smtkLoader.logError(e);
                    //smtkLoader.printError(e);
                    //e.printStackTrace();
                    log(e);
                }
            }

            haltReader();

            //smtkLoader.logTick();
            smtkLoader.printTick();

        } catch (Exception e) {
            //smtkLoader.logError(new LoadException(path.toString(), null, e.getMessage(), ERROR));
            //smtkLoader.printError(new LoadException(path.toString(), null, e.getMessage(), ERROR));
            log(new LoadException(path.toString(), "", e.getMessage(), ERROR));
            //e.printStackTrace();
        } catch (LoadException e) {
            //e.printStackTrace();
            log(new LoadException(path.toString(), "", e.getMessage(), ERROR));
        }
    }

    public void persistAllConcepts(SMTKLoader smtkLoader) {

        //smtkLoader.logInfo(new LoadLog("Persisitiendo Conceptos Fármacos - Medicamento Clínico", INFO));
        smtkLoader.printInfo(new LoadLog("Persisitiendo Conceptos Fármacos - Medicamento Clínico", INFO));

        //smtkLoader.setConceptsProcessed(0);
        smtkLoader.setProcessed(0);

        Iterator it = conceptSMTKMap.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry) it.next();

            try {
                conceptManager.persist((ConceptSMTK)pair.getValue(), smtkLoader.getUser());
                //smtkLoader.incrementConceptsProcessed(1);
                smtkLoader.printIncrementConceptsProcessed(1);
            }
            catch (Exception e) {
                //smtkLoader.logError(new LoadException(path.toString(), (Long) pair.getKey(), e.getMessage(), ERROR));
                //smtkLoader.printError(new LoadException(path.toString(), (Long) pair.getKey(), e.getMessage(), ERROR));
                //e.printStackTrace();
                log(new LoadException(path.toString(), "", e.getMessage(), ERROR));
            }

            it.remove(); // avoids a ConcurrentModificationException
        }

        //smtkLoader.logTick();
        smtkLoader.printTick();
        haltWriter();
    }

    public void processConcepts(SMTKLoader smtkLoader) {
        loadAllConcepts(smtkLoader);
        smtkLoader.setProcessed(0);
        persistAllConcepts(smtkLoader);
    }

}
