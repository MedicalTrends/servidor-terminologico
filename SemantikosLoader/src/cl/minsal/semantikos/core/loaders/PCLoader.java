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
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static cl.minsal.semantikos.model.LoadLog.ERROR;
import static cl.minsal.semantikos.model.LoadLog.INFO;
import static cl.minsal.semantikos.model.LoadLog.WARNING;
import static cl.minsal.semantikos.model.relationships.SnomedCTRelationship.ES_UN;

/**
 * Created by root on 15-06-17.
 */
public class PCLoader extends BaseLoader {

    ISPFetcher ispFetcher = (ISPFetcher) ServiceLocator.getInstance().getService(ISPFetcher.class);

    static int OFFSET = SubstanceLoader.LENGHT + MBLoader.LENGHT + MCLoader.LENGHT + MCCELoader.LENGHT + GFPLoader.LENGHT + FPLoader.LENGHT;

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
        fields.put("MEDICAMENTO_CLINICO_COD", OFFSET + 13);
        fields.put("MEDICAMENTO_CLINICO_DESC", OFFSET + 14);
        fields.put("LABORATORIO_DESC", OFFSET + 15);
        fields.put("FORM_FARM_EXT_DESC", OFFSET + 16);
        fields.put("SABOR_DESC", OFFSET + 17);
        //fields.put("FAMILIA_PRODUCTO_COD", OFFSET + 18);
        fields.put("FAMILIA_PRODUCTO_DESC", OFFSET + 18);
        fields.put("COMERCIALIZADO_CHILE_DESC", OFFSET + 19);
        fields.put("PRODUCTO_ISP", OFFSET + 20);
        fields.put("BIOEQUIVALENTES", OFFSET + 21);
    }

    static int LENGHT = fields.size();

    public PCLoader(Category category, User user) {
        super(category, user);
        nonUpdateableDefinitions.add(category.findRelationshipDefinitionsByName("Medicamento Clínico").get(0));
        nonUpdateableDefinitions.add(category.findRelationshipDefinitionsByName("Laboratorio Comercial").get(0));
    }

    private HelperTableRow ispRecord = null;

    private Map<String,String> fetchedData;

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
            Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Producto Comercial");

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
                throw new LoadException(path.toString(), id, "Relación referencia a concepto SCT inexistente", ERROR, type);
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
                        throw new LoadException(path.toString(), id, "No existe un tipo de relación de nombre: "+relationshipType, ERROR, type);
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

            BasicTypeValue basicTypeValue;

            /*Recuperando Comercializado*/

            String marketedName = tokens[fields.get("COMERCIALIZADO_CHILE_DESC")];

            if(!marketedName.isEmpty()) {

                basicTypeValue = new BasicTypeValue(marketedName.trim().equals("Si"));
                relationshipDefinition = category.findRelationshipDefinitionsByName("Comercializado").get(0);

                Relationship relationshipMarketed = new Relationship(newConcept, basicTypeValue, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                addRelationship(relationshipMarketed, type);
            }

            /*Recuperando Medicamento Clínico*/

            String mcName = tokens[fields.get("MEDICAMENTO_CLINICO_DESC")].replace("•","");

            if(!StringUtils.isEmpty(mcName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Medicamento Clínico").get(0);

                List<Description> mc = descriptionManager.searchDescriptionsPerfectMatch(StringUtils.normalizeSpaces(mcName).trim(), Arrays.asList(new Category[]{CategoryFactory.getInstance().findCategoryByName("Fármacos - Medicamento Clínico")}), null);

                if(mc.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un MC con preferida: "+mcName, ERROR, type);
                }

                if(!mc.get(0).getConceptSMTK().isModeled()) {
                    throw new LoadException(path.toString(), id, "EL MC: "+mcName+" no está modelado, se descarta este MC", ERROR, type);
                }

                Relationship relationshipMC = new Relationship(newConcept, mc.get(0).getConceptSMTK(), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                addRelationship(relationshipMC, type);
            }

            /*Recuperando FP*/

            String gfpName = tokens[fields.get("FAMILIA_PRODUCTO_DESC")];

            if(!StringUtils.isEmpty(gfpName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Familia de Producto").get(0);

                List<Description> fp = descriptionManager.searchDescriptionsPerfectMatch(StringUtils.normalizeSpaces(gfpName).trim(), Arrays.asList(new Category[]{CategoryFactory.getInstance().findCategoryByName("Fármacos - Familia de Productos")}), null);

                if(fp.isEmpty()) {
                    //SMTKLoader.logError(new LoadException(path.toString(), id, "No existe una FP con preferida: "+gfpName, ERROR));
                    throw new LoadException(path.toString(), id, "No existe una FP con preferida: "+gfpName, ERROR, type);
                }

                if(!fp.get(0).getConceptSMTK().isModeled()) {
                    throw new LoadException(path.toString(), id, "La FP: "+gfpName+" no está modelado, se descarta este MC", ERROR, type);
                }

                Relationship relationshipFP = new Relationship(newConcept, fp.get(0).getConceptSMTK(), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));
                addRelationship(relationshipFP, type);
            }

            /*Recuperando Sabor*/

            String flavourName = tokens[fields.get("SABOR_DESC")];

            if(!StringUtils.isEmpty(flavourName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Sabor").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<HelperTableRow> flavour = helperTableManager.searchRows(helperTable, flavourName);

                if(flavour.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un sabor con glosa: "+flavourName, ERROR, type);
                }

                Relationship relationshipFlavour = new Relationship(newConcept, flavour.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                addRelationship(relationshipFlavour, type);
            }

            /*Recuperando Laboratorio Comercial*/

            String commercialLabName = tokens[fields.get("LABORATORIO_DESC")];

            if(!StringUtils.isEmpty(commercialLabName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("Laboratorio Comercial").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<HelperTableRow> commercialLab = helperTableManager.searchRows(helperTable, commercialLabName);

                if(commercialLab.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe un laboratorio comercial con glosa: "+commercialLabName, ERROR, type);
                }

                Relationship relationshipCommercialLab = new Relationship(newConcept, commercialLab.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                addRelationship(relationshipCommercialLab, type);
            }

            /*Recuperando FFE*/

            String ffeName = tokens[fields.get("FORM_FARM_EXT_DESC")].trim();

            if(!StringUtils.isEmpty(ffeName)) {

                relationshipDefinition = category.findRelationshipDefinitionsByName("FFE").get(0);

                helperTable = (HelperTable) relationshipDefinition.getTargetDefinition();

                List<HelperTableRow> ffe = helperTableManager.searchRows(helperTable, ffeName);

                if(ffe.isEmpty()) {
                    throw new LoadException(path.toString(), id, "No existe una FFE con glosa: "+ffeName, ERROR, type);
                }

                Relationship relationshipFFE = new Relationship(newConcept, ffe.get(0), relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));

                addRelationship(relationshipFFE, type);
            }

            /*Recuperando registros ISP*/

            relationshipDefinition = category.findRelationshipDefinitionsByName("ISP").get(0);

            String ispNames = tokens[fields.get("PRODUCTO_ISP")];

            String[] ispTokens = ispNames.split(relSeparator);

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
                                log(new LoadException(path.toString(), id, "Registro ISP Falló para: '" + regnumRegano + "'", WARNING, type));
                                //break;
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
                        Relationship relationshipISP = new Relationship(newConcept, ispRecord, relationshipDefinition, new ArrayList<RelationshipAttribute>(), new Timestamp(System.currentTimeMillis()));
                        addRelationship(relationshipISP, type);
                    }

                }
                else {
                    /**
                     * Si se encuentra, se verifica que no exista actualmente una relación con este destino
                     */
                    for (Relationship relationship : relationshipManager.findRelationshipsLike(relationshipDefinition,ispRecord)) {
                        if(relationship.getRelationshipDefinition().isISP()) {
                            throw new LoadException(path.toString(), id, "Para agregar una relación a ISP, el par ProductoComercial-Regnum/RegAño deben ser únicos. Registro referenciado por concepto " + relationship.getSourceConcept().getDescriptionFavorite(), ERROR, type);
                        }
                        addRelationship(relationship, type);
                        break;
                    }
                }
            }

            addConcept(type);

        }
        catch (Exception e) {
            throw new LoadException(path.toString(), id, "Error desconocido: "+e.toString(), ERROR, type);
        }

    }

    public void mapIspRecord(HelperTable ispHelperTable, HelperTableRow ispRecord, Map<String,String> fetchedRecord) throws ParseException {

        final Collator instance = Collator.getInstance();

        // This strategy mean it'll ignore the accents
        instance.setStrength(Collator.NO_DECOMPOSITION);

        for (HelperTableColumn helperTableColumn : ispHelperTable.getColumns()) {
            for (String s : fetchedRecord.keySet()) {
                if(instance.compare(helperTableColumn.getDescription().trim().toLowerCase(), s.trim().toLowerCase()) == 0) {
                    HelperTableData cell = new HelperTableData();
                    cell.setColumn(helperTableColumn);
                    if(helperTableColumn.getDescription().toLowerCase().contains("fecha") ||
                            helperTableColumn.getDescription().toLowerCase().contains("ultima") ) {
                        if(!fetchedRecord.get(s).trim().isEmpty()) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            Date parsedDate = dateFormat.parse(fetchedRecord.get(s));
                            cell.setDateValue(new Timestamp(parsedDate.getTime()));
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

}
