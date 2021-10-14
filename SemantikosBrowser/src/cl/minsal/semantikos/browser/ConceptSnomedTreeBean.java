package cl.minsal.semantikos.browser;

import cl.minsal.semantikos.category.CategoryBean;
import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.CrossmapsManager;
import cl.minsal.semantikos.kernel.components.RelationshipManager;
import cl.minsal.semantikos.kernel.components.SnomedCTManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.crossmaps.DirectCrossmap;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.model.crossmaps.cie10.Disease;
import cl.minsal.semantikos.model.crossmaps.gmdn.GenericDeviceGroup;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.helpertables.HelperTableData;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCT;
import cl.minsal.semantikos.model.snomedct.RelationshipSCT;
import cl.minsal.semantikos.modelweb.MyTreeNode;
import cl.minsal.semantikos.modelweb.Pair;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by BluePrints Developer on 14-07-2016.
 */

@ManagedBean(name = "conceptSnomedTreeBean")
@ViewScoped
public class ConceptSnomedTreeBean implements Serializable {

    static private final Logger logger = LoggerFactory.getLogger(ConceptSnomedTreeBean.class);

    //@EJB
    SnomedCTManager snomedCTManager = (SnomedCTManager) ServiceLocator.getInstance().getService(SnomedCTManager.class);

    Map<String, List<Pair<String,String >>> relationships = new TreeMap<>();

    private transient TreeNode conceptRoot;

    //Inicializacion del Bean
    @PostConstruct
    protected void initialize() {
    }


    public TreeNode getConceptTree(ConceptSCT concept) {

        if(concept == null) {
            return conceptRoot;
        }

        relationships.clear();

        conceptRoot = new DefaultTreeNode(new Object(), null);

        conceptRoot.setExpanded(true);

        DefaultTreeNode rootNode = new DefaultTreeNode(concept.getId(), conceptRoot);

        rootNode.setExpanded(true);

        MyTreeNode descriptionsNode = new MyTreeNode("Descripciones", null, "fa fa-folder-open subfolder");

        DefaultTreeNode descriptionNode = new DefaultTreeNode(descriptionsNode, rootNode);

        descriptionNode.setExpanded(true);

        String icon = "fa fa-file-text-o";

        for (DescriptionSCT description : concept.getDescriptions()) {

            if(description.isFavourite()) {
                icon = "fa fa-star-o favorite";
            }

            MyTreeNode otherDescriptionNode = new MyTreeNode(description.getDescriptionType().getName(), description.getTerm(), icon);

            new DefaultTreeNode(otherDescriptionNode, descriptionNode);
        }

        int cont = 0;

        DefaultTreeNode relationshipNode = null;

        if(concept.getRelationships().isEmpty()) {
            concept.setRelationships(snomedCTManager.getRelationshipsFrom(concept));
        }

        for (RelationshipSCT relationshipSCT : concept.getRelationships()) {

            if(relationshipSCT.getDestinationConcept().getDescriptionFavouriteSynonymous() != null) {

                if(relationships.containsKey(relationshipSCT.getTypeConcept().getDescriptionFavouriteSynonymous().getTerm())) {
                    if(!relationships.get(relationshipSCT.getTypeConcept().getDescriptionFavouriteSynonymous().getTerm()).contains(new Pair<String,String>(relationshipSCT.getDestinationConcept().getDescriptionFavouriteSynonymous().getTerm(), String.valueOf(relationshipSCT.getDestinationConcept().getId())))) {
                        relationships.get(relationshipSCT.getTypeConcept().getDescriptionFavouriteSynonymous().getTerm()).add(new Pair<String,String>(relationshipSCT.getDestinationConcept().getDescriptionFavouriteSynonymous().getTerm(), String.valueOf(relationshipSCT.getDestinationConcept().getId())));
                    }
                }
                else {
                    List<Pair<String, String>> list = new ArrayList<>();
                    list.add(new Pair<String,String>(relationshipSCT.getDestinationConcept().getDescriptionFavouriteSynonymous().getTerm(), String.valueOf(relationshipSCT.getDestinationConcept().getId())));
                    relationships.put(relationshipSCT.getTypeConcept().getDescriptionFavouriteSynonymous().getTerm(), list);
                }

            }

        }

        for (String s : relationships.keySet()) {

            if(cont == 0) {
                MyTreeNode relationshipsNode = new MyTreeNode("Relaciones", null, "fa fa-folder-open subfolder");
                relationshipNode = new DefaultTreeNode(relationshipsNode, rootNode);
                relationshipNode.setExpanded(true);
            }

            MyTreeNode relNode = new MyTreeNode(null, s, "fa fa-folder-open subfolder");
            DefaultTreeNode leafNode0 = new DefaultTreeNode(relNode, relationshipNode);
            leafNode0.setExpanded(true);

            for (Pair<String,String> pair : relationships.get(s)) {
                //System.out.println(pair.getFirst() + " => " + pair.getSecond());

                MyTreeNode relNodeLeaf = new MyTreeNode(null, pair.getFirst(), "ui-icon ui-icon-public");
                relNodeLeaf.setType("ConceptSCT");
                relNodeLeaf.setDetail(pair.getSecond());
                DefaultTreeNode leafNode = new DefaultTreeNode(relNodeLeaf, leafNode0);
                leafNode.setExpanded(true);
            }

            cont++;
        }

        return conceptRoot;

    }


}
