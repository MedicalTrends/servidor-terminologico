package cl.minsal.semantikos.browser;

import cl.minsal.semantikos.category.CategoryBean;
import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.components.GuestPreferences;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.crossmaps.DirectCrossmap;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.model.crossmaps.cie10.Disease;
import cl.minsal.semantikos.model.crossmaps.gmdn.GenericDeviceGroup;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableData;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.modelweb.MyTreeNode;
import cl.minsal.semantikos.modelweb.Pair;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.krb5.internal.crypto.Des;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by BluePrints Developer on 14-07-2016.
 */

@ManagedBean(name = "conceptTreeBean")
@ViewScoped
public class ConceptTreeBean implements Serializable {

    static private final Logger logger = LoggerFactory.getLogger(ConceptTreeBean.class);

    //@EJB
    RelationshipManager relationshipManager = (RelationshipManager) ServiceLocator.getInstance().getService(RelationshipManager.class);

    //@EJB
    CrossmapsManager crossmapsManager = (CrossmapsManager) ServiceLocator.getInstance().getService(CrossmapsManager.class);

    @ManagedProperty(value = "#{categoryBean}")
    CategoryBean categoryBean;

    List<IndirectCrossmap> indirectCrossmaps = new ArrayList<>();

    Map<String, List<Pair<String,String >>> diseases = new HashMap<>();

    ConceptSMTK selectedConcept;

    public void setCategoryBean(CategoryBean categoryBean) {
        this.categoryBean = categoryBean;
    }

    private transient TreeNode conceptRoot;

    //Inicializacion del Bean
    @PostConstruct
    protected void initialize() {
        indirectCrossmaps = new ArrayList<>();
    }


    public TreeNode getConceptTree(ConceptSMTK concept) {

        if(concept == null) {
            return conceptRoot;
        }

        conceptRoot = new DefaultTreeNode(new Object(), null);

        conceptRoot.setExpanded(true);

        DefaultTreeNode rootNode = new DefaultTreeNode(concept.getConceptID(), conceptRoot);

        rootNode.setExpanded(true);

        MyTreeNode categoryNode = new MyTreeNode("Categoría", concept.getCategory().getName(), "fa fa-tag");

        new DefaultTreeNode(categoryNode, rootNode);

        MyTreeNode descriptionsNode = new MyTreeNode("Descripciones", null, "fa fa-folder-open subfolder");

        DefaultTreeNode descriptionNode = new DefaultTreeNode(descriptionsNode, rootNode);

        descriptionNode.setExpanded(true);

        MyTreeNode favouriteDescriptionNode = new MyTreeNode("Preferida", concept.getDescriptionFavorite().getTerm(), "fa fa-star-o");

        new DefaultTreeNode(favouriteDescriptionNode, descriptionNode);

        MyTreeNode fsnDescriptionNode = new MyTreeNode("FSN", concept.getDescriptionFSN().getTerm(), "fa fa-file-text-o");

        new DefaultTreeNode(fsnDescriptionNode, descriptionNode);

        for (Description description : getOtherDescriptions(concept)) {

            MyTreeNode otherDescriptionNode = new MyTreeNode(description.getDescriptionType().getName(), description.getTerm(), "fa fa-file-text-o");

            new DefaultTreeNode(otherDescriptionNode, descriptionNode);
        }

        MyTreeNode attributesNode = new MyTreeNode("Atributos", null, "fa fa-folder-open subfolder");

        DefaultTreeNode attributeNode = null;

        for (RelationshipDefinition relationshipDefinition: categoryBean.getNotEmptySMTKDefinitionsByCategory(concept)) {

            if(attributeNode == null) {
                attributeNode = new DefaultTreeNode(attributesNode, rootNode);
                attributeNode.setExpanded(true);
            }

            if(relationshipDefinition.getTargetDefinition().isSMTKType()) {

                if(relationshipDefinition.getMultiplicity().isSimple() && !concept.getValidRelationshipsByRelationDefinition(relationshipDefinition).isEmpty()) {
                    for (Relationship relationship: concept.getValidRelationshipsByRelationDefinition(relationshipDefinition)) {
                        MyTreeNode relationshipNode = new MyTreeNode(relationshipDefinition.getName(), formatSMTKRelationship(relationship), "fa fa-edit");
                        ConceptSMTK conceptSMTK = (ConceptSMTK) relationship.getTarget();
                        relationshipNode.setDetail(conceptSMTK.getConceptID());
                        relationshipNode.setType("ConceptSMTK");
                        new DefaultTreeNode(relationshipNode, attributeNode);
                    }
                }


                if(relationshipDefinition.getMultiplicity().isCollection() && !concept.getValidRelationshipsByRelationDefinition(relationshipDefinition).isEmpty()) {

                    if(relationshipDefinition.getOrderAttributeDefinition() != null) {
                        MyTreeNode relationshipsNode = new MyTreeNode(relationshipDefinition.getName(), null, "fa fa-folder-open subfolder");
                        DefaultTreeNode relationshipsTreeNode = new DefaultTreeNode(relationshipsNode, attributeNode);
                        relationshipsTreeNode.setExpanded(true);

                        int cont = 1;

                        for (Relationship relationship: concept.getValidRelationshipsByRelationDefinition(relationshipDefinition)) {
                            MyTreeNode relationshipNode = new MyTreeNode(null, formatSMTKRelationship(relationship) + " " + formatSMTKRelationshipAttributes(relationship), "fa fa-edit");
                            ConceptSMTK conceptSMTK = (ConceptSMTK) relationship.getTarget();
                            relationshipNode.setDetail(conceptSMTK.getConceptID());
                            relationshipNode.setType("ConceptSMTK");
                            new DefaultTreeNode(relationshipNode, relationshipsTreeNode);
                            cont++;
                        }
                    }
                    else {
                        MyTreeNode relationshipsNode = new MyTreeNode(relationshipDefinition.getName(), null, "fa fa-folder-open subfolder");
                        DefaultTreeNode relationshipsTreeNode = new DefaultTreeNode(relationshipsNode, attributeNode);
                        relationshipsTreeNode.setExpanded(true);

                        for (Relationship relationship: concept.getValidRelationshipsByRelationDefinition(relationshipDefinition)) {
                            MyTreeNode relationshipNode = new MyTreeNode(null, formatSMTKRelationship(relationship) + " " + formatSMTKRelationshipAttributes(relationship), "fa fa-file-text-o");
                            new DefaultTreeNode(relationshipNode, relationshipsTreeNode);
                        }
                    }

                }
            }

            if(relationshipDefinition.getTargetDefinition().isBasicType()) {

                BasicTypeDefinition basicTypeDefinition = (BasicTypeDefinition) relationshipDefinition.getTargetDefinition();

                if(basicTypeDefinition.getType().getTypeName().equals("boolean")) {

                    if(relationshipDefinition.getMultiplicity().isSimple() && !concept.getValidRelationshipsByRelationDefinition(relationshipDefinition).isEmpty()) {

                        for (Relationship relationship : concept.getValidRelationshipsByRelationDefinition(relationshipDefinition)) {

                            BasicTypeValue basicTypeValue = (BasicTypeValue) relationship.getTarget();
                            String value;

                            if((Boolean)basicTypeValue.getValue()) {
                                value = "sí";
                            }
                            else {
                                value = "no";
                            }

                            if((Boolean)basicTypeValue.getValue()) {
                                MyTreeNode relationshipNode = new MyTreeNode(relationshipDefinition.getName(), value, "fa fa-list-alt");
                                new DefaultTreeNode(relationshipNode, attributeNode);
                            }

                        }

                    }
                }

                if(Arrays.asList(new String[]{"int","float","string","date"}).contains(basicTypeDefinition.getType().getTypeName())) {

                    for (Relationship relationship : concept.getValidRelationshipsByRelationDefinition(relationshipDefinition)) {
                        MyTreeNode relationshipNode = new MyTreeNode(relationshipDefinition.getName(), formatSMTKRelationship(relationship) + " " + formatSMTKRelationshipAttributes(relationship), "fa fa-file-text-o");
                        new DefaultTreeNode(relationshipNode, attributeNode);
                    }

                }

            }

            if(relationshipDefinition.getTargetDefinition().isHelperTable()) {


                if(relationshipDefinition.getMultiplicity().isSimple() && !concept.getValidRelationshipsByRelationDefinition(relationshipDefinition).isEmpty()) {
                    for (Relationship relationship : concept.getValidRelationshipsByRelationDefinition(relationshipDefinition)) {
                        MyTreeNode relationshipNode = new MyTreeNode(relationshipDefinition.getName(), formatSMTKRelationship(relationship) + " " + formatSMTKRelationshipAttributes(relationship), "fa fa-list-alt");
                        new DefaultTreeNode(relationshipNode, attributeNode);
                    }
                }

                if(relationshipDefinition.getMultiplicity().isCollection() && !concept.getValidRelationshipsByRelationDefinition(relationshipDefinition).isEmpty()) {

                    if(relationshipDefinition.getOrderAttributeDefinition() != null) {
                        MyTreeNode relationshipsNode = new MyTreeNode(relationshipDefinition.getName(), null, "fa fa-folder-open subfolder");
                        DefaultTreeNode relationshipsTreeNode = new DefaultTreeNode(relationshipsNode, attributeNode);
                        relationshipsTreeNode.setExpanded(true);

                        int cont = 1;

                        for (Relationship relationship: concept.getValidRelationshipsByRelationDefinition(relationshipDefinition)) {
                            MyTreeNode relationshipNode = new MyTreeNode(null, formatSMTKRelationship(relationship) + " " + formatSMTKRelationshipAttributes(relationship), "fa fa-list-alt");
                            new DefaultTreeNode(relationshipNode, relationshipsTreeNode);
                            cont++;
                        }
                    }
                    else {
                        MyTreeNode relationshipsNode = new MyTreeNode(relationshipDefinition.getName(), null, "fa fa-folder-open subfolder");
                        DefaultTreeNode relationshipsTreeNode = new DefaultTreeNode(relationshipsNode, attributeNode);
                        relationshipsTreeNode.setExpanded(true);

                        for (Relationship relationship: concept.getValidRelationshipsByRelationDefinition(relationshipDefinition)) {
                            MyTreeNode relationshipNode = new MyTreeNode(null, formatSMTKRelationship(relationship) + " " + formatSMTKRelationshipAttributes(relationship), "fa fa-list-alt");
                            new DefaultTreeNode(relationshipNode, relationshipsTreeNode);
                        }
                    }

                }

            }

        }


        if(!getSnomedCTRelationships(concept).isEmpty()) {

            MyTreeNode snomedCTNode = new MyTreeNode("Snomed-CT", null, "fa fa-folder-open subfolder");
            DefaultTreeNode snomedNode = new DefaultTreeNode(snomedCTNode, rootNode);
            snomedNode.setExpanded(true);

            for (Relationship relationship: getSnomedCTRelationships(concept)) {

                HelperTableRow helperTableRow = (HelperTableRow) relationship.getRelationshipTypeAttribute().getTarget();
                ConceptSCT conceptSCT = (ConceptSCT) relationship.getTarget();

                MyTreeNode relationshipNode = new MyTreeNode(helperTableRow.getDescription(), conceptSCT.getDescriptionFavouriteSynonymous().getTerm(), "ui-icon ui-icon-public");
                relationshipNode.setDetail(String.valueOf(conceptSCT.getId()));
                relationshipNode.setType("ConceptSCT");
                new DefaultTreeNode(relationshipNode, snomedNode);
            }
        }

        List<Relationship> indirectCrossmaps = getIndirectCrossmapsRelationships(concept);

        if(!getDirectCrossmapsRelationships(concept).isEmpty() || !indirectCrossmaps.isEmpty()) {
            MyTreeNode crossmapsNode = new MyTreeNode("Crossmaps", null, "fa fa-folder-open subfolder");
            DefaultTreeNode crossmapNode = new DefaultTreeNode(crossmapsNode, rootNode);
            crossmapNode.setExpanded(true);

            if(!getDirectCrossmapsRelationships(concept).isEmpty()) {

                MyTreeNode directCrossmapNode = new MyTreeNode("Crossmaps Directos", null, "fa fa-folder-open subfolder");
                DefaultTreeNode directCrossmapsNode = new DefaultTreeNode(directCrossmapNode, crossmapNode);
                directCrossmapsNode.setExpanded(true);

                for (Relationship relationship : getDirectCrossmapsRelationships(concept)) {

                    DirectCrossmap directCrossmap = (DirectCrossmap) relationship;

                    if(directCrossmap.getTarget() instanceof Disease) {
                        Disease disease = (Disease) directCrossmap.getTarget();
                        MyTreeNode cie10Node = new MyTreeNode(directCrossmap.getTarget().getCrossmapSet().getAbbreviatedName(), ((Disease) directCrossmap.getTarget()).getGloss(), "fa fa-list-alt");
                        DefaultTreeNode diseaseNode = new DefaultTreeNode(cie10Node, directCrossmapsNode);
                        directCrossmapsNode.setExpanded(true);
                    }

                    if(directCrossmap.getTarget() instanceof GenericDeviceGroup) {
                        GenericDeviceGroup genericDeviceGroup = (GenericDeviceGroup) directCrossmap.getTarget();
                        MyTreeNode gmdnNode = new MyTreeNode(directCrossmap.getTarget().getCrossmapSet().getAbbreviatedName(), ((Disease) directCrossmap.getTarget()).getGloss(), "fa fa-ñost-alt");
                        DefaultTreeNode genericDeviceGroupNode = new DefaultTreeNode(gmdnNode, directCrossmapsNode);
                        directCrossmapsNode.setExpanded(true);
                    }

                }
            }

            if(!indirectCrossmaps.isEmpty()) {

                MyTreeNode indirectCrossmapNode = new MyTreeNode("Crossmaps Indirectos", null, "fa fa-folder-open subfolder");
                DefaultTreeNode indirectCrossmapsNode = new DefaultTreeNode(indirectCrossmapNode, crossmapNode);
                indirectCrossmapsNode.setExpanded(true);

                int cont = 0;

                DefaultTreeNode diseaseNode = null;

                diseases.clear();

                for (Relationship relationship : indirectCrossmaps) {

                    IndirectCrossmap indirectCrossmap = (IndirectCrossmap) relationship;

                    if(indirectCrossmap.getTarget() instanceof Disease) {

                        Disease disease = (Disease) indirectCrossmap.getTarget();

                        if(cont == 0) {
                            MyTreeNode cie10Node = new MyTreeNode(disease.getCrossmapSet().getAbbreviatedName(), disease.getCode1(), "fa fa-folder-open subfolder");
                            diseaseNode = new DefaultTreeNode(cie10Node, indirectCrossmapsNode);
                            diseaseNode.setExpanded(true);
                        }

                        if(diseases.containsKey(disease.getCode() + " >> " + disease.getGloss())) {
                            if(!diseases.get(disease.getCode() + " >> " + disease.getGloss()).contains(new Pair<String,String>(indirectCrossmap.getMapRule(), indirectCrossmap.getMapAdvice()))) {
                                diseases.get(disease.getCode() + " >> " + disease.getGloss()).add(new Pair<String,String>(indirectCrossmap.getMapRule(), indirectCrossmap.getMapAdvice()));
                            }
                        }
                        else {
                            List<Pair<String, String>> list = new ArrayList<>();
                            list.add(new Pair<>(indirectCrossmap.getMapRule(), indirectCrossmap.getMapAdvice()));
                            diseases.put(disease.getCode() + " >> " + disease.getGloss(), list);
                        }

                        cont++;
                    }

                }


                for (String s : diseases.keySet()) {

                    MyTreeNode codNode = new MyTreeNode(null, s, "fa fa-folder-open subfolder");
                    DefaultTreeNode leafNode0 = new DefaultTreeNode(codNode, diseaseNode);
                    //leafNode0.setExpanded(true);

                    for (Pair<String,String> pair : diseases.get(s)) {
                        //System.out.println(pair.getFirst() + " => " + pair.getSecond());

                        MyTreeNode nodeX = new MyTreeNode(pair.getFirst(), pair.getSecond(), "fa fa-list-alt");
                        DefaultTreeNode leafNodeX = new DefaultTreeNode(nodeX, leafNode0);
                        leafNodeX.setExpanded(true);
                    }
                }

            }

        }

        return conceptRoot;

    }


    public List<Description> getOtherDescriptions(ConceptSMTK concept) {

        List<Description> otherDescriptions = new ArrayList<Description>();

        for (Description description : concept.getDescriptions()) {
            if(DescriptionTypeFactory.getInstance().getDescriptionTypesButFSNandFavorite().contains(description.getDescriptionType()))
                otherDescriptions.add(description);
        }

        return otherDescriptions;
    }


    public List<Relationship> getSMTKRelationships(ConceptSMTK concept) {


        if(!concept.isRelationshipsLoaded()) {
            concept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(concept));
        }

        List<Relationship> smtkRelationships = new ArrayList<Relationship>();

        for (Relationship relationship : concept.getRelationships()) {
            if(!relationship.getRelationshipDefinition().getTargetDefinition().isSnomedCTType() && !relationship.getRelationshipDefinition().getTargetDefinition().isCrossMapType()) {
                smtkRelationships.add(relationship);
            }
        }

        return smtkRelationships;
    }

    public String formatSMTKRelationship(Relationship relationship) {

        String term = "";

        if(relationship.getRelationshipDefinition().getTargetDefinition().isSMTKType()) {
            ConceptSMTK conceptSMTK = (ConceptSMTK) relationship.getTarget();
            term = conceptSMTK.getDescriptionFavorite().getTerm() + " ";
        }
        if(relationship.getRelationshipDefinition().getTargetDefinition().isHelperTable()) {
            HelperTableRow helperTableRow = (HelperTableRow) relationship.getTarget();

            if(relationship.getRelationshipDefinition().isATC()) {
                HelperTableData helperTableData = helperTableRow.getCells().get(1);
                term = helperTableData.getStringValue();
            }
            else {
                term = helperTableRow.getDescription() + " ";
            }
        }
        if(relationship.getRelationshipDefinition().getTargetDefinition().isBasicType()) {
            BasicTypeValue basicTypeValue = (BasicTypeValue) relationship.getTarget();
            term = basicTypeValue.getValue().toString();
        }

        return term;
    }

    public String formatSMTKRelationshipAttributes(Relationship relationship) {

        String term = "";

        if(relationship.getRelationshipDefinition().getTargetDefinition().isSMTKType()) {
            if (relationship.getRelationshipDefinition().isSubstance()) {
                ConceptSMTK conceptSMTK = (ConceptSMTK) relationship.getTarget();
                //term = conceptSMTK.getDescriptionFavorite().getTerm() + " ";

                for (RelationshipAttribute relationshipAttribute : relationship.getRelationshipAttributes()) {

                    if(relationshipAttribute.getRelationAttributeDefinition().isOrderAttribute()) {
                        continue;
                    }

                    if(relationshipAttribute.getRelationAttributeDefinition().isCantidadPPAttribute()) {
                        term = term + "/";
                    }

                    if(relationshipAttribute.getRelationAttributeDefinition().isUnidadPotenciaAttribute() ||
                            relationshipAttribute.getRelationAttributeDefinition().isUnidadPPAttribute() ||
                            relationshipAttribute.getRelationAttributeDefinition().isUnidadAttribute() ||
                            relationshipAttribute.getRelationAttributeDefinition().isUnidadPackMultiAttribute() ||
                            relationshipAttribute.getRelationAttributeDefinition().isUnidadVolumenTotalAttribute() ||
                            relationshipAttribute.getRelationAttributeDefinition().isUnidadVolumenAttribute()) {
                        HelperTableRow helperTableRow = (HelperTableRow) relationshipAttribute.getTarget();
                        term = term + helperTableRow.getCellByColumnName("descripcion abreviada").getStringValue() + " ";
                    }
                    else {
                        if(relationshipAttribute.getRelationAttributeDefinition().isCantidadPPAttribute() &&
                                Float.parseFloat(relationshipAttribute.getTarget().toString()) == 1) {
                            continue;
                        }

                        BasicTypeValue basicTypeValue = (BasicTypeValue) relationshipAttribute.getTarget();
                        BasicTypeDefinition basicTypeDefinition = (BasicTypeDefinition) relationshipAttribute.getRelationAttributeDefinition().getTargetDefinition();

                        if (basicTypeDefinition.getType().equals(BasicTypeType.FLOAT_TYPE)) {
                            DecimalFormat df = new DecimalFormat("###,###.##");
                            term = term + df.format(Float.parseFloat(basicTypeValue.getValue().toString())) + " ";
                        }
                    }
                }
            }
        }
        if(relationship.getRelationshipDefinition().getTargetDefinition().isHelperTable()) {
            HelperTableRow helperTableRow = (HelperTableRow) relationship.getTarget();
            //term = helperTableRow.getDescription() + " ";

            for (RelationshipAttribute relationshipAttribute : relationship.getRelationshipAttributes()) {

                if(relationshipAttribute.getRelationAttributeDefinition().isOrderAttribute()) {
                    continue;
                }

                term = " " + term + relationshipAttribute.getTarget().toString() + " ";
            }
        }
        if(relationship.getRelationshipDefinition().getTargetDefinition().isBasicType()) {
            BasicTypeValue basicTypeValue = (BasicTypeValue) relationship.getTarget();
            //term = helperTableRow.getDescription() + " ";

            for (RelationshipAttribute relationshipAttribute : relationship.getRelationshipAttributes()) {

                if(relationshipAttribute.getRelationAttributeDefinition().isOrderAttribute()) {
                    continue;
                }

                term = " " + term + relationshipAttribute.getTarget().toString() + " ";
            }
        }



        return term;

    }

    public List<Relationship> getSnomedCTRelationships(ConceptSMTK concept) {

        if (!concept.isRelationshipsLoaded()) {
            concept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(concept));
        }

        List<Relationship> snomedCTRelationships = new ArrayList<Relationship>();

        for (SnomedCTRelationship relationship : concept.getRelationshipsSnomedCT()) {
            snomedCTRelationships.add(relationship);
        }

        return snomedCTRelationships;
    }

    public List<Relationship> getDirectCrossmapsRelationships(ConceptSMTK concept) {

        if(!concept.isRelationshipsLoaded()) {
            concept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(concept));
        }

        List<Relationship> smtkRelationships = new ArrayList<Relationship>();

        for (Relationship relationship : concept.getRelationships()) {
            if(relationship instanceof DirectCrossmap) {
                smtkRelationships.add(relationship);
            }
        }

        return smtkRelationships;
    }

    public List<Relationship> getIndirectCrossmapsRelationships(ConceptSMTK concept) {

        if(!concept.isRelationshipsLoaded()) {
            concept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(concept));
        }

        List<Relationship> smtkRelationships = new ArrayList<Relationship>();


        try {
            if(!concept.equals(selectedConcept)) {
                indirectCrossmaps = crossmapsManager.getIndirectCrossmaps(concept);
                diseases.clear();
            }

            selectedConcept = concept;

            concept.getRelationships().addAll(indirectCrossmaps);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        for (Relationship relationship : concept.getRelationships()) {
            if(relationship instanceof IndirectCrossmap) {
                smtkRelationships.add(relationship);
            }
        }

        return smtkRelationships;
    }

}
