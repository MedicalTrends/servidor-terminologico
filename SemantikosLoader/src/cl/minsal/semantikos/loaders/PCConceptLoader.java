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
import cl.minsal.semantikos.model.helpertables.HelperTableColumn;
import cl.minsal.semantikos.model.helpertables.HelperTableData;
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
import static java.util.Collections.EMPTY_LIST;

/**
 * Created by root on 15-06-17.
 */
public class PCConceptLoader extends EntityLoader {

    ConceptManager conceptManager = (ConceptManager) RemoteEJBClientFactory.getInstance().getManager(ConceptManager.class);
    DescriptionManager descriptionManager = (DescriptionManager) RemoteEJBClientFactory.getInstance().getManager(DescriptionManager.class);
    RelationshipManager relationshipManager = (RelationshipManager) RemoteEJBClientFactory.getInstance().getManager(RelationshipManager.class);
    TagManager tagManager = (TagManager) RemoteEJBClientFactory.getInstance().getManager(TagManager.class);
    ISPFetcher ispFetcher = (ISPFetcher) RemoteEJBClientFactory.getInstance().getManager(ISPFetcher.class);
    SnomedCTManager snomedCTManager = (SnomedCTManager) RemoteEJBClientFactory.getInstance().getManager(SnomedCTManager.class);
    HelperTablesManager helperTableManager = (HelperTablesManager) RemoteEJBClientFactory.getInstance().getManager(HelperTablesManager.class);

    public static final Map<String, Integer> pcConceptFields;
    static
    {
        pcConceptFields = new HashMap<String, Integer>();
        pcConceptFields.put("CONCEPTO_ID", 0);
        pcConceptFields.put("DESCRIPCION", 1);
        pcConceptFields.put("DESC_ABREVIADA", 2);
        pcConceptFields.put("ESTADO", 3);
        pcConceptFields.put("SENSIBLE_MAYUSCULA", 4);
        pcConceptFields.put("CREAC_NOMBRE", 5);
        pcConceptFields.put("REVISADO", 6);
        pcConceptFields.put("CONSULTAR", 7);
        pcConceptFields.put("SCT_ID", 8);
        pcConceptFields.put("SCT_TERMINO", 9);
        pcConceptFields.put("SINONIMO", 10);
        pcConceptFields.put("GRUPOS_JERARQUICOS", 11);
        pcConceptFields.put("MEDICAMENTO_CLINICO", 12);
        pcConceptFields.put("LABORATORIO_FK", 13);
        pcConceptFields.put("LABORATORIO_DESC", 14);
        pcConceptFields.put("FORM_FARM_EXT_FK", 15);
        pcConceptFields.put("FORM_FARM_EXT_DESC", 16);
        pcConceptFields.put("SABOR_FK", 17);
        pcConceptFields.put("SABOR_DESC", 18);
        pcConceptFields.put("FAMILIA_PRODUCTO_FK", 19);
        pcConceptFields.put("FAMILIA_PRODUCTO_DESC", 20);
        pcConceptFields.put("COMERCIALIZADO_CHILE_FK", 21);
        pcConceptFields.put("COMERCIALIZADO_CHILE_DESC", 22);
        pcConceptFields.put("PRODUCTO_ISP", 23);
        pcConceptFields.put("BIOEQUIVALENTES", 24);
    }

    Map<Long, ConceptSMTK> conceptSMTKMap = new HashMap<>();

    Map<String, Description> descriptionMap = new HashMap<>();

    private HelperTableRow ispRecord = null;

    private Map<String,String> fetchedData;

    public void loadConceptFromFileLine(String line, User user) throws LoadException {

        String[] tokens = line.split(separator,-1);
        long id = Long.parseLong(tokens[pcConceptFields.get("CONCEPTO_ID")]);

        try {

            /*Recuperando datos Concepto*/

            /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
            boolean toBeReviewed = tokens[pcConceptFields.get("REVISADO")].equals("Si");
            boolean toBeConsulted = tokens[pcConceptFields.get("CONSULTAR")].equals("Si");
            boolean autogenerated = tokens[pcConceptFields.get("CREAC_NOMBRE")].equals("Autogenerado");

            Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Producto Comercial");
            TagSMTK tagSMTK = TagSMTKFactory.getInstance().findTagSMTKByName("producto");

            ConceptSMTK conceptSMTK = new ConceptSMTK(category);
            conceptSMTK.setToBeConsulted(toBeConsulted);
            conceptSMTK.setToBeReviewed(toBeReviewed);
            conceptSMTK.setCategory(category);
            conceptSMTK.setTagSMTK(tagSMTK);

            /*Recuperando datos Descripciones*/

            /*Recuperando descripcion preferida*/
            String term = StringUtils.normalizeSpaces(tokens[pcConceptFields.get("DESCRIPCION")]);
            boolean caseSensitive = tokens[pcConceptFields.get("SENSIBLE_MAYUSCULA")].equals("Sensible");
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

            conceptSMTK.addDescription(descriptionFSN);

            /*Recuperando descripcion Abreviada*/
            term = StringUtils.normalizeSpaces(tokens[pcConceptFields.get("DESC_ABREVIADA")]);

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
            String synonyms = tokens[pcConceptFields.get("SINONIMO")];
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
            String idConceptSCTName = tokens[pcConceptFields.get("SCT_ID")];

            /*Por defecto se mapea a un concepto SNOMED Genérico*/
            long idConceptSCT = 373873005;

            if(!idConceptSCTName.isEmpty()) {
                idConceptSCT = Long.parseLong(tokens[pcConceptFields.get("SCT_ID")]);
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

            /* Generando Grupo */
            BasicTypeValue group = new BasicTypeValue(0);

            RelationshipAttributeDefinition attDefGroup = relationshipDefinition.findRelationshipAttributeDefinitionsByName("Grupo").get(0);

            RelationshipAttribute raGroup = new RelationshipAttribute(attDefGroup, relationshipSnomed, group);
            relationshipSnomed.getRelationshipAttributes().add(raGroup);

            conceptSMTK.addRelationship(relationshipSnomed);

            HelperTable helperTable;

            BasicTypeValue basicTypeValue;

            /*Recuperando Comercializado*/

            String marketedName = tokens[pcConceptFields.get("COMERCIALIZADO_CHILE_DESC")];

            if(!marketedName.isEmpty()) {

                basicTypeValue = new BasicTypeValue(marketedName.trim().equals("Si"));
                relationshipDefinition = category.findRelationshipDefinitionsByName("Comercializado").get(0);

                Relationship relationshipMarketed = new Relationship(conceptSMTK, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

                conceptSMTK.addRelationship(relationshipMarketed);
            }

            /*Recuperando Medicamento Clínico*/

            String mcName = tokens[pcConceptFields.get("MEDICAMENTO_CLINICO")].replace("•","");

            if(!StringUtils.isEmpty(mcName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Medicamento Clínico").get(0);

                List<Description> mc = descriptionManager.searchDescriptionsPerfectMatch(StringUtils.normalizeSpaces(mcName).trim(), Arrays.asList(new Category[]{CategoryFactory.getInstance().findCategoryByName("Fármacos - Medicamento Clínico")}), EMPTY_LIST);

                if(mc.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un MC con preferida: "+mcName, ERROR);
                }

                if(!mc.get(0).getConceptSMTK().isModeled()) {
                    throw new LoadException(path.toString(), id, "EL MC: "+mcName+" no está modelado, se descarta este MC", ERROR);
                }

                Relationship relationshipMC = new Relationship(conceptSMTK, mc.get(0).getConceptSMTK(), relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

                conceptSMTK.addRelationship(relationshipMC);
            }

            /*Recuperando FP*/

            String gfpName = tokens[pcConceptFields.get("FAMILIA_PRODUCTO_DESC")];

            if(!StringUtils.isEmpty(gfpName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Familia de Producto").get(0);

                List<Description> fp = descriptionManager.searchDescriptionsPerfectMatch(StringUtils.normalizeSpaces(gfpName).trim(), Arrays.asList(new Category[]{CategoryFactory.getInstance().findCategoryByName("Fármacos - Familia de Productos")}), EMPTY_LIST);

                if(fp.isEmpty()) {
                    //SMTKLoader.logError(new LoadException(path.toString(), id, "No existe una FP con preferida: "+gfpName, ERROR));
                    throw new LoadException(path.toString(), id, "No existe una FP con preferida: "+gfpName, ERROR);
                }

                if(!fp.get(0).getConceptSMTK().isModeled()) {
                    throw new LoadException(path.toString(), id, "La FP: "+gfpName+" no está modelado, se descarta este MC", ERROR);
                }

                Relationship relationshipFP = new Relationship(conceptSMTK, fp.get(0).getConceptSMTK(), relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);
                conceptSMTK.addRelationship(relationshipFP);
            }

            /*Recuperando Sabor*/

            String flavourName = tokens[pcConceptFields.get("SABOR_DESC")];

            if(!StringUtils.isEmpty(flavourName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Sabor").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<HelperTableRow> flavour = helperTableManager.searchRows(helperTable, flavourName);

                if(flavour.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un sabor con glosa: "+flavourName, ERROR);
                }

                Relationship relationshipFlavour = new Relationship(conceptSMTK, flavour.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

                conceptSMTK.addRelationship(relationshipFlavour);
            }

            /*Recuperando Laboratorio Comercial*/

            String commercialLabName = tokens[pcConceptFields.get("LABORATORIO_DESC")];

            if(!StringUtils.isEmpty(commercialLabName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Laboratorio Comercial").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<HelperTableRow> commercialLab = helperTableManager.searchRows(helperTable, commercialLabName);

                if(commercialLab.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un laboratorio comercial con glosa: "+commercialLabName, ERROR);
                }

                Relationship relationshipCommercialLab = new Relationship(conceptSMTK, commercialLab.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

                conceptSMTK.addRelationship(relationshipCommercialLab);
            }

            /*Recuperando FFE*/

            String ffeName = tokens[pcConceptFields.get("FORM_FARM_EXT_DESC")].trim();

            if(!StringUtils.isEmpty(ffeName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("FFE").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<HelperTableRow> ffe = helperTableManager.searchRows(helperTable, ffeName);

                if(ffe.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe una FFE con glosa: "+ffeName, ERROR);
                }

                Relationship relationshipFFE = new Relationship(conceptSMTK, ffe.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);

                conceptSMTK.addRelationship(relationshipFFE);
            }

            /*Recuperando registros ISP*/

            relationshipDefinition = category.findRelationshipDefinitionsByName("ISP").get(0);

            String ispNames = tokens[pcConceptFields.get("PRODUCTO_ISP")];

            String[] ispTokens = ispNames.split("•");

            for (String ispToken : ispTokens) {

                if(ispToken.isEmpty() || ispToken.equals("\"")) {
                    continue;
                }

                String[] ispName = ispToken.split(" - ");

                // Obteniendo regnum/regano
                String regnumRegano = StringUtils.normalizeSpaces(ispName[0]).trim();

                ispRecord = null;

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                /**
                 * Primero se busca un registro isp local
                 */
                for (HelperTableRow helperTableRecord : helperTableManager.searchRows(helperTable,regnumRegano)) {
                    ispRecord = helperTableRecord;
                    break;
                }

                /**
                 * Si no existe, se va a buscar a la página del registro isp
                 */
                if(ispRecord==null) {

                    ispRecord = new HelperTableRow(helperTable);
                    int count = 1;

                    while(true) {

                        try {
                            //SMTKLoader.logWarning(new LoadLog((count)+"° intento solicitud registro ISP para: "+regnumRegano, INFO));
                            fetchedData = ispFetcher.getISPData(regnumRegano);
                            count++;
                            if(!fetchedData.isEmpty()) {
                                //SMTKLoader.logWarning(new LoadLog("Registro ISP OK para: "+regnumRegano, INFO));
                                break;
                            }
                            if(count==2) {
                                SMTKLoader.logWarning(new LoadLog("Registro ISP Falló para: "+regnumRegano, INFO));
                                break;
                            }
                        } catch (Exception e) {
                            // handle exception
                            SMTKLoader.logWarning(new LoadLog((count+1)+"° intento solicitud registro ISP para: "+regnumRegano, INFO));
                        }
                    }

                    if(!fetchedData.isEmpty()) {
                        mapIspRecord(helperTable, ispRecord, fetchedData);
                        helperTableManager.insertRow(ispRecord,user.getEmail());
                        ispRecord = helperTableManager.searchRows(helperTable,regnumRegano).get(0);
                        Relationship relationshipISP = new Relationship(conceptSMTK, ispRecord, relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);
                        conceptSMTK.addRelationship(relationshipISP);
                    }

                }
                else {
                    /**
                     * Si se encuentra, se verifica que no exista actualmente una relación con este destino
                     */
                    for (Relationship relationship : relationshipManager.findRelationshipsLike(relationshipDefinition,ispRecord)) {
                        if(relationship.getRelationshipDefinition().isISP()) {
                            throw new LoadException(path.toString(), id, "Para agregar una relación a ISP, la dupla ProductoComercial-Regnum/RegAño deben ser únicos. Registro referenciado por concepto " + relationship.getSourceConcept().getDescriptionFavorite(), ERROR);
                        }
                        conceptSMTK.addRelationship(relationship);
                    }
                }
            }

            conceptSMTKMap.put(id, conceptSMTK);
        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: "+e.toString(), ERROR);
        }

    }

    public void mapIspRecord(HelperTable ispHelperTable, HelperTableRow ispRecord, Map<String,String> fetchedRecord){

        for (HelperTableColumn helperTableColumn : ispHelperTable.getColumns()) {
            for (String s : fetchedRecord.keySet()) {
                if(helperTableColumn.getDescription().toLowerCase().contains(s.toLowerCase())) {
                    HelperTableData cell = new HelperTableData();
                    cell.setColumn(helperTableColumn);
                    if(helperTableColumn.getDescription().toLowerCase().contains("fecha") ||
                            helperTableColumn.getDescription().toLowerCase().contains("ultima") ) {
                        if(!fetchedRecord.get(s).trim().isEmpty()) {
                            cell.setDateValue(new Timestamp(Date.parse(fetchedRecord.get(s))));
                        }
                    }
                    else {
                        cell.setStringValue(fetchedRecord.get(s));
                    }
                    ispRecord.getCells().add(cell);
                    break;
                }
            }
        }
        ispRecord.setDescription(ispRecord.getCellByColumnName("registro").toString());
        ispRecord.setValid(true);
    }

    public void loadBioequivalents(String line) throws LoadException {

        String[] tokens = line.split(separator,-1);

        /*Se recuperan los datos relevantes. El resto serán calculados por el componente de negocio*/
        long id = Long.parseLong(tokens[pcConceptFields.get("CONCEPTO_ID")]);

        try {

            Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Producto Comercial");

            long idConceptSMTK = id;

            RelationshipDefinition relationshipDefinition;

            RelationshipAttributeDefinition attDef;

            RelationshipAttribute ra;

            HelperTable helperTable;

            ConceptSMTK conceptSMTK = conceptSMTKMap.get(idConceptSMTK);

            if(conceptSMTK == null) {
                throw new LoadException(path.toString(), id, "Relación referencia a concepto SMTK inexistente", ERROR);
            }

            /*Recuperando bioequivalentes*/

            relationshipDefinition = category.findRelationshipDefinitionsByName("Bioequivalente").get(0);

            String bioequivalentNames = tokens[pcConceptFields.get("BIOEQUIVALENTES")];

            String[] bioequivalentTokens = bioequivalentNames.split("•");

            for (String bioequivalentToken : bioequivalentTokens) {

                if(bioequivalentToken.isEmpty() || bioequivalentToken.equals("\"")) {
                    continue;
                }

                String[] bioequivalentName = bioequivalentToken.split(" - ");

                // Obteniendo regnum/regano
                String regnumRegano = StringUtils.normalizeSpaces(bioequivalentName[0]).trim();

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                /**
                 * Primero se busca un registro isp local
                 */
                for (HelperTableRow helperTableRecord : helperTableManager.searchRows(helperTable,regnumRegano)) {
                    ispRecord = helperTableRecord;
                    Relationship relationshipISP = new Relationship(conceptSMTK, ispRecord, relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);
                    conceptSMTK.addRelationship(relationshipISP);
                    break;
                }

                /**
                 * Si no existe, se va a buscar a la página del registro isp
                 */
                if(ispRecord.getCells().isEmpty()) {
                    SMTKLoader.logError(new LoadException(path.toString(), id, "No existe un bioequivalente de Regnum/Regaño "+regnumRegano, ERROR));
                }

            }
        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: "+e.toString(), ERROR);
        }
    }


    public void loadAllConcepts(SMTKLoader smtkLoader) {

        smtkLoader.logInfo(new LoadLog("Comprobando Conceptos PC", INFO));
        smtkLoader.setConceptsProcessed(0);

        try {

            initReader(smtkLoader.PC_PATH);

            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    loadConceptFromFileLine(line, smtkLoader.getUser());
                    smtkLoader.incrementConceptsProcessed(1);
                }
                catch (LoadException e) {
                    smtkLoader.logError(e);
                    e.printStackTrace();
                }
            }

            haltReader();

            smtkLoader.setConceptsProcessed(0);

            initReader(smtkLoader.PC_PATH);

            while ((line = reader.readLine()) != null) {
                try {loadBioequivalents(line);
                    smtkLoader.incrementConceptsProcessed(1);
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

        smtkLoader.logInfo(new LoadLog("Persisitiendo Conceptos PC", INFO));

        smtkLoader.setConceptsProcessed(0);

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

        smtkLoader.logTick();
    }

    public void processConcepts(SMTKLoader smtkLoader) {
        loadAllConcepts(smtkLoader);
        persistAllConcepts(smtkLoader);
    }

}