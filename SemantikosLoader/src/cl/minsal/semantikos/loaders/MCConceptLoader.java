package cl.minsal.semantikos.loaders;

import cl.minsal.semantikos.clients.RemoteEJBClientFactory;
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

import java.util.*;

import static cl.minsal.semantikos.model.LoadLog.ERROR;
import static cl.minsal.semantikos.model.LoadLog.INFO;
import static cl.minsal.semantikos.model.relationships.SnomedCTRelationship.ES_UN;
import static java.util.Collections.EMPTY_LIST;

/**
 * Created by root on 15-06-17.
 */
public class MCConceptLoader extends EntityLoader {

    ConceptManager conceptManager = (ConceptManager) RemoteEJBClientFactory.getInstance().getManager(ConceptManager.class);
    DescriptionManager descriptionManager = (DescriptionManager) RemoteEJBClientFactory.getInstance().getManager(DescriptionManager.class);
    TagManager tagManager = (TagManager) RemoteEJBClientFactory.getInstance().getManager(TagManager.class);
    SnomedCTManager snomedCTManager = (SnomedCTManager) RemoteEJBClientFactory.getInstance().getManager(SnomedCTManager.class);
    HelperTablesManager helperTableManager = (HelperTablesManager) RemoteEJBClientFactory.getInstance().getManager(HelperTablesManager.class);

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

    Map<Long, List<Integer> > admVias = new HashMap<>();


    public void loadConceptFromFileLine(String line, User user) throws LoadException {

        String[] tokens = line.split(separator);
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
            String term = StringUtils.normalizeSpaces(tokens[mcConceptFields.get("DESCRIPCION")]);
            boolean caseSensitive = tokens[mcConceptFields.get("SENSIBLE_MAYUSCULA")].equals("Sensible");
            DescriptionType descriptionType = DescriptionType.PREFERIDA;

            Description descriptionFavourite = new Description(conceptSMTK, term, descriptionType);
            descriptionFavourite.setCaseSensitive(caseSensitive);
            descriptionFavourite.setCreatorUser(user);
            descriptionFavourite.setAutogeneratedName(autogenerated);

            conceptSMTK.addDescription(descriptionFavourite);

            /*Recuperando descripcion FSN*/
            term = descriptionFavourite.getTerm()+" ("+tagSMTK.getName()+")";
            descriptionType = DescriptionType.FSN;

            Description descriptionFSN = new Description(conceptSMTK, term, descriptionType);
            descriptionFSN.setCaseSensitive(caseSensitive);
            descriptionFSN.setCreatorUser(user);
            descriptionFSN.setAutogeneratedName(autogenerated);

            conceptSMTK.addDescription(descriptionFSN);

            /*Recuperando descripcion Abreviada*/
            term = StringUtils.normalizeSpaces(tokens[mcConceptFields.get("DESC_ABREVIADA")]);

            if(!StringUtils.isEmpty(term)) {
                descriptionType = DescriptionType.ABREVIADA;

                Description descriptionAbbreviated = new Description(conceptSMTK, term, descriptionType);
                descriptionAbbreviated.setCaseSensitive(caseSensitive);
                descriptionAbbreviated.setCreatorUser(user);
                descriptionAbbreviated.setAutogeneratedName(autogenerated);

                conceptSMTK.addDescription(descriptionAbbreviated);
            }

            /*Recuperando Sinónimos*/
            String synonyms = tokens[mcConceptFields.get("SINONIMO")];
            descriptionType = DescriptionType.SYNONYMOUS;

            String[] synonymsTokens = synonyms.split("•");

            for (String synonymsToken : synonymsTokens) {

                if(synonymsToken.isEmpty() || synonymsToken.equals("\"")) {
                    continue;
                }

                term = StringUtils.normalizeSpaces(synonymsToken.split("-")[1]);

                Description description = new Description(conceptSMTK, term, descriptionType);
                description.setCaseSensitive(caseSensitive);
                description.setCreatorUser(user);
                description.setAutogeneratedName(autogenerated);

                conceptSMTK.addDescription(description);
            }

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
            RelationshipDefinition relationshipDefinition = RelationshipDefinitionFactory.getInstance().findRelationshipDefinitionByName(TargetDefinition.SNOMED_CT);

            RelationshipAttributeDefinition attDef;

            RelationshipAttribute ra;

            Relationship relationshipSnomed = new Relationship(conceptSMTK, conceptSCT, relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

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

            conceptSMTK.addRelationship(relationshipSnomed);

            HelperTable helperTable;

            /*Generando Comercializado*/
            BasicTypeValue basicTypeValue = new BasicTypeValue(true);

            relationshipDefinition = RelationshipDefinitionFactory.getInstance().findRelationshipDefinitionByName(TargetDefinition.COMERCIALIZADO);

            Relationship relationshipMarketed = new Relationship(conceptSMTK, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

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

                List<Description> substanceList = descriptionManager.searchDescriptionsPerfectMatch(termFavourite, Arrays.asList(new Category[]{CategoryFactory.getInstance().findCategoryByName("Fármacos - Sustancia")}), EMPTY_LIST);

                if(substanceList.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe una sustancia con preferida: "+termFavourite, ERROR);
                }

                if(!substanceList.get(0).getConceptSMTK().isModeled()) {
                    throw new LoadException(path.toString(), id, "La sustancia: "+termFavourite+" no está modelada, se descarta este MC", ERROR);
                }

                Relationship relationshipSubstance = new Relationship(conceptSMTK, substanceList.get(0).getConceptSMTK(), relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

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
                if(!StringUtils.isEmpty(substanceTokens[1])) {

                    String[] potenciaTokens = substanceTokens[1].split(" ");

                    // Cantidad
                    BasicTypeValue cantidadPotencia = new BasicTypeValue(Integer.parseInt(potenciaTokens[0].trim()));

                    attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Cantidad de Potencia").get(0);

                    ra = new RelationshipAttribute(attDef, relationshipSubstance, cantidadPotencia);
                    relationshipSubstance.getRelationshipAttributes().add(ra);

                    //Unidad
                    attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Unidad de Potencia").get(0);

                    helperTable = (HelperTable) attDef.getTargetDefinition();

                    List<HelperTableRow> unidadPotencia = helperTableManager.searchRows(helperTable, potenciaTokens[1].trim());

                    ra = new RelationshipAttribute(attDef, relationshipSubstance, unidadPotencia.get(0));
                    relationshipSubstance.getRelationshipAttributes().add(ra);

                }

                // Obteniendo PP
                if(!StringUtils.isEmpty(substanceTokens[2])) {

                    String[] partidoPorTokens = substanceTokens[2].split(" ");

                    // Cantidad
                    BasicTypeValue cantidadPartidoPor = new BasicTypeValue(Integer.parseInt(partidoPorTokens[0].trim()));

                    attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Cantidad Partido Por").get(0);

                    ra = new RelationshipAttribute(attDef, relationshipSubstance, cantidadPartidoPor);
                    relationshipSubstance.getRelationshipAttributes().add(ra);

                    //Unidad
                    attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Unidad Partido Por").get(0);

                    helperTable = (HelperTable) attDef.getTargetDefinition();

                    List<HelperTableRow> unidadPartidoPor = helperTableManager.searchRows(helperTable, partidoPorTokens[1].trim());

                    ra = new RelationshipAttribute(attDef, relationshipSubstance, unidadPartidoPor.get(0));
                    relationshipSubstance.getRelationshipAttributes().add(ra);
                }

                mcSpecialValue = false;
            }

            /*Generando MC Especial*/

            basicTypeValue = new BasicTypeValue(mcSpecialValue);

            relationshipDefinition = category.findRelationshipDefinitionsByName("Medicamento clínico especial").get(0);

            Relationship relationshipMCSpecial = new Relationship(conceptSMTK, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

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

                Relationship relationshipPrescriptionState = new Relationship(conceptSMTK, prescriptionState.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

                conceptSMTK.addRelationship(relationshipPrescriptionState);
            }

            /*Recuperando Medicamento Básico*/

            String mbName = tokens[mcConceptFields.get("MEDICAMENTO_BASICO_DESC")];

            if(!StringUtils.isEmpty(mbName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Medicamento Básico").get(0);

                List<Description> mb = descriptionManager.searchDescriptionsPerfectMatch(StringUtils.normalizeSpaces(mbName).trim(), Arrays.asList(new Category[]{CategoryFactory.getInstance().findCategoryByName("Fármacos - Medicamento Básico")}), EMPTY_LIST);

                if(mb.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un Medicamento Básico con preferida: "+mbName, ERROR);
                }

                if(!mb.get(0).getConceptSMTK().isModeled()) {
                    throw new LoadException(path.toString(), id, "EL MB: "+mbName+" no está modelado, se descarta este MC", ERROR);
                }

                Relationship relationshipMB = new Relationship(conceptSMTK, mb.get(0).getConceptSMTK(), relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

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

                Relationship relationshipFFA = new Relationship(conceptSMTK, ffa.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

                //Tipo FFA
                attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Tipo FFA").get(0);

                String ffaTypeName = tokens[mcConceptFields.get("TIPO_FORMA_AGRP_DESC")];

                helperTable = (HelperTable) attDef.getTargetDefinition();

                List<HelperTableRow> ffaType = helperTableManager.searchRows(helperTable, ffaTypeName);

                ra = new RelationshipAttribute(attDef, relationshipFFA, ffaType.get(0));
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

                Relationship relationshipSaleCondition = new Relationship(conceptSMTK, prescriptionState.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

                conceptSMTK.addRelationship(relationshipSaleCondition);
            }

            /*Recuperando Unidad de UAsist*/

            String assistanceUnitName = tokens[mcConceptFields.get("UNI_ASISTENCIAL_UNIDAD_DESC")];

            if(!StringUtils.isEmpty(assistanceUnitName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Unidad de U Asist").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<HelperTableRow> assistanceUnit = helperTableManager.searchRows(helperTable, assistanceUnitName);

                if(assistanceUnit.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe una Unidad de U Asist con glosa: "+assistanceUnitName, ERROR);
                }

                Relationship relationshipSaleCondition = new Relationship(conceptSMTK, assistanceUnit.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

                conceptSMTK.addRelationship(relationshipSaleCondition);
            }

            /*Recuperando Volumen Total*/

            String volumeName = tokens[mcConceptFields.get("VOLUMEN_TOTAL_CANTIDAD")];

            if(!volumeName.isEmpty()) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Cantidad de Volumen Total").get(0);

                basicTypeValue = new BasicTypeValue(new Integer(volumeName.replace(",",".")));

                Relationship relationshipVolume = new Relationship(conceptSMTK, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

                //Unidad Volumen
                attDef = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Unidad de Volumen").get(0);

                String volumeUnitName = tokens[mcConceptFields.get("VOLUMEN_TOTAL_UNIDAD_DESC")];

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

                Relationship relationshipURL = new Relationship(conceptSMTK, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

                conceptSMTK.addRelationship(relationshipURL);
            }

             /*Recuperando ATC*/

            String atcName = tokens[mcConceptFields.get("ATC_DESCRIPCION_DESC")];

            if(!StringUtils.isEmpty(assistanceUnitName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("ATC").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<String> columnNames = new ArrayList<>();

                columnNames.add("codigo atc");

                List<HelperTableRow> atc = helperTableManager.searchRows(helperTable, atcName, columnNames);

                if(atc.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un ATC con glosa: "+atcName, ERROR);
                }

                Relationship relationshipATC = new Relationship(conceptSMTK, atc.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

                conceptSMTK.addRelationship(relationshipATC);
            }

            conceptSMTKMap.put(id, conceptSMTK);
        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: "+e.toString(), ERROR);
        }

    }

    public void loadAdministrationVias(String line) throws LoadException {

        String[] tokens = line.split(separator);

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        long id = Long.parseLong(tokens[admViasFields.get("FK_CCTNU_CONCEPTO_ID")]);

        try {

            Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Medicamento Clínico");

            long idConceptSMTK = Long.parseLong(tokens[admViasFields.get("STK_CONCEPTOORIGEN")]);

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

            String admViaName = tokens[mcConceptFields.get("CCTVA_DESCRIPCION_USUARIO")];

            if(!admViaName.isEmpty()) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Unidad de U Asist").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<HelperTableRow> admVia = helperTableManager.searchRows(helperTable, admViaName);

                if(admVia.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe una Vía Administración con glosa: "+admViaName, ERROR);
                }

                Relationship relationshipAdmVia = new Relationship(conceptSMTK, admVia.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

                // Cantidad
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

        smtkLoader.logInfo(new LoadLog("Comprobando Conceptos Básicos", INFO));

        try {

            initReader(smtkLoader.MC_PATH);

            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    loadConceptFromFileLine(line, smtkLoader.getUser());
                }
                catch (LoadException e) {
                    smtkLoader.logError(e);
                    e.printStackTrace();
                }
            }

            haltReader();

            initReader(smtkLoader.MC_VIAS_ADM_PATH);

            while ((line = reader.readLine()) != null) {
                try {
                    loadAdministrationVias(line);
                }
                catch (LoadException e) {
                    smtkLoader.logError(e);
                    e.printStackTrace();
                }
            }

            haltReader();

            smtkLoader.logTick();

        } catch (Exception e) {
            smtkLoader.logError(new LoadException(path.toString(), null, e.getMessage(), ERROR));
            e.printStackTrace();
        }
    }

    public void persistAllConcepts(SMTKLoader smtkLoader) {

        smtkLoader.logInfo(new LoadLog("Persisitiendo Conceptos Fármacos - Sustancia", INFO));

        Iterator it = conceptSMTKMap.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry) it.next();

            try {
                conceptManager.persist((ConceptSMTK)pair.getValue(), smtkLoader.getUser());
                smtkLoader.incrementConceptsProcessed(1);
            }
            catch (Exception e) {
                smtkLoader.logError(new LoadException(path.toString(), (Long) pair.getKey(), e.getMessage(), ERROR));
                e.printStackTrace();
            }

            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public void processConcepts(SMTKLoader smtkLoader) {
        loadAllConcepts(smtkLoader);
        persistAllConcepts(smtkLoader);
    }

}
