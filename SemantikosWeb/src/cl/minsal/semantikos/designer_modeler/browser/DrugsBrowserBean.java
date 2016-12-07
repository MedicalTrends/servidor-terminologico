package cl.minsal.semantikos.designer_modeler.browser;

import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.relationships.Relationship;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by diego on 26/06/2016.
 */

@ManagedBean(name = "drugsBrowserBean")
@ViewScoped
public class DrugsBrowserBean implements Serializable {

    static final Logger logger = LoggerFactory.getLogger(DrugsBrowserBean.class);

    @EJB
    DrugsManager drugsManager;

    /**
     * Lista de usuarios para el despliegue del filtro por usuarios
     */
    private List<User> users = new ArrayList<User>();

    /**
     * Lista de conceptos para el despliegue del resultado de la consulta
     */
    private List<ConceptSMTK> concepts;

    private List<ConceptSMTK> conceptHierarchies;

    private ConceptSMTK conceptSelected = null;

    private Long[] drugsCategories;

    private TreeNode root;

    @EJB
    private QueryManager queryManager;

    @EJB
    private CategoryManager categoryManager;

    @EJB
    private ConceptManager conceptManager;

    @PostConstruct
    public void init(){
        root = new DefaultTreeNode(new ConceptSMTK(categoryManager.getCategoryById(39)), null);
        drugsCategories = getCategoryValues(drugsManager.getDrugsCategories());
    }

    public ConceptSMTK getConceptSelected() {
        return conceptSelected;
    }

    public void setConceptSelected(ConceptSMTK conceptSelected) {
        if(conceptSelected==null)
            return;
        this.conceptSelected = conceptSelected;
        conceptHierarchies = drugsManager.getDrugsConceptHierarchies(this.conceptSelected);
        root = new DefaultTreeNode(new ConceptSMTK(categoryManager.getCategoryById(39)), null);
        mapConcepts(conceptHierarchies, root);
        //this.conceptSelected = null;
    }

    public void resetConceptSelected(){
        conceptSelected= null;
    }

    public TreeNode mapConcepts(List<ConceptSMTK> concepts, TreeNode treeNode) {

        ConceptSMTK conceptData = (ConceptSMTK)treeNode.getData();

        if(conceptData.equals(conceptSelected))
            treeNode.setExpanded(false);
        else
            treeNode.setExpanded(true);

        for (ConceptSMTK concept : concepts) {

            if(!concept.isRelationshipsLoaded())
                return treeNode;

            TreeNode childTreeNode = new DefaultTreeNode(concept, treeNode);

            List<ConceptSMTK> childConcepts = new ArrayList<>();

            for (Relationship relationship : concept.getRelationships()) {
                childConcepts.add((ConceptSMTK)relationship.getTarget());
            }

            mapConcepts(childConcepts, childTreeNode);
        }

        return root;
    }

    public DrugsManager getDrugsManager() {
        return drugsManager;
    }

    public void setDrugsManager(DrugsManager drugsManager) {
        this.drugsManager = drugsManager;
    }

    public List<ConceptSMTK> getConceptSearchInput(String patron) {

        concepts = conceptManager.findConceptBy(patron, drugsCategories, 0, 30);

        return concepts;
    }

    public Long[] getCategoryValues(List<Category> drugsCategories){

        List<Long> categoryValues = new ArrayList<>();

        for (Category category : drugsCategories)
            categoryValues.add(category.getId());

        if(categoryValues.isEmpty())
            return null;

        else {
            Long[] array = new Long[categoryValues.size()];
            return categoryValues.toArray(array);
        }
    }

    public TreeNode getRoot() {
        return root;
    }

    public List<ConceptSMTK> getConceptHierarchies() {
        return conceptHierarchies;
    }

    public void setConceptHierarchies(List<ConceptSMTK> conceptHierarchies) {
        this.conceptHierarchies = conceptHierarchies;
    }
}

