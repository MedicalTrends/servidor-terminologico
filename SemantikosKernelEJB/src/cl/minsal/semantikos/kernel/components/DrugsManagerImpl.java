package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.RelationshipDAO;
import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.MultiplicityFactory;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by BluePrints Developer on 21-09-2016.
 */
@Stateless
public class DrugsManagerImpl implements DrugsManager {


    @EJB
    private ConceptManager conceptManager;

    @EJB
    private CategoryManager categoryManager;

    @EJB
    private RelationshipDAO relationshipDAO;


    @Override
    public List<Category> getDrugsCategories() {

        List<Category> drugsCategories = new ArrayList<>();

        for (Category category : categoryManager.getCategories()) {
            if(category.getName().toLowerCase().startsWith("fármacos"))
                drugsCategories.add(category);
        }

        return drugsCategories;
    }

    @Override
    public List<ConceptSMTK> getDrugsConceptHierarchies(ConceptSMTK concept) {
        concept.setRelationships(new ArrayList<Relationship>());
        traverseDown(concept);
        return traverseUp(Arrays.asList(concept));

    }

    private ConceptSMTK traverseDown(ConceptSMTK node){

        List<Relationship> relationships = conceptManager.getRelationships(node);
        List<Relationship> edges = new ArrayList<>();


        for (Relationship relationship : relationships) {
            if(relationship.getRelationshipDefinition().getTargetDefinition().isSMTKType())
                edges.add(relationship);
        }

        if(edges.isEmpty()) {
            return node;
        }
        else {
            node.setRelationships(edges);

            for (Relationship edge : edges) {
                ConceptSMTK childNode = (ConceptSMTK) edge.getTarget();
                if(childNode.isModeled()) {
                    childNode.setRelationships(new ArrayList<Relationship>());
                    traverseDown(childNode);
                }
            }
        }
        return node;
    }

    private List<ConceptSMTK> traverseUp(List<ConceptSMTK> nodes){

        List<ConceptSMTK> allNodesParentNodes = new ArrayList<>();
        int parents = 0;

        for (ConceptSMTK node : nodes) {

            if(node == null)
                break;

            List<ConceptSMTK> parentNodes = conceptManager.getRelatedConcepts(node);

            parents = parents + parentNodes.size();

            List<ConceptSMTK> thisNodeParentNodes = new ArrayList<>();

            if(conceptManager.getRelatedConcepts(node).isEmpty() && !allNodesParentNodes.contains(node)) {
                allNodesParentNodes.add(node);
            }

            for (ConceptSMTK parentNode : parentNodes) {

                RelationshipDefinition rd = new RelationshipDefinition(node.getCategory().getName(), node.getCategory().getName(),MultiplicityFactory.ONE_TO_ONE, node.getCategory());
                Relationship r = new Relationship(parentNode, node, rd, new ArrayList<RelationshipAttribute>(), null);
                parentNode.setRelationships(Arrays.asList(r));

                List<Relationship> relationships = conceptManager.getRelationships(parentNode);
                //parentNode.setRelationships(new ArrayList<Relationship>());

                for (Relationship relationship : relationships) {
                    if(relationship.getRelationshipDefinition().getTargetDefinition().isSMTKType()) {
                        ConceptSMTK conceptSMTK = (ConceptSMTK) relationship.getTarget();
                        if(!node.equals(conceptSMTK) && node.isModeled()) {
                            traverseDown(conceptSMTK);
                            conceptSMTK.setRelationships(new ArrayList<Relationship>());
                            relationship.setTarget(conceptSMTK);
                            parentNode.addRelationship(relationship);
                        }
                    }
                }

                if(parentNode.isModeled())
                    thisNodeParentNodes.add(parentNode);
            }

            allNodesParentNodes.addAll(thisNodeParentNodes);

        }

        if(parents==0)
            return  allNodesParentNodes;
        else
            return traverseUp(allNodesParentNodes);
    }

}
