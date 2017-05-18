package cl.minsal.semantikos.browser;

import cl.minsal.semantikos.clients.RemoteEJBClientFactory;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.users.User;
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

    //@EJB
    DrugsManager drugsManager;

    //@EJB
    private QueryManager queryManager;

    //@EJB
    private CategoryManager categoryManager;

    //@EJB
    private ConceptManager conceptManager;

    @PostConstruct
    public void init(){
        drugsManager = (DrugsManager) RemoteEJBClientFactory.getInstance().getManager(DrugsManager.class);
        conceptManager = (ConceptManager) RemoteEJBClientFactory.getInstance().getManager(ConceptManager.class);
        queryManager = (QueryManager) RemoteEJBClientFactory.getInstance().getManager(QueryManager.class);
        categoryManager = (CategoryManager) RemoteEJBClientFactory.getInstance().getManager(CategoryManager.class);

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
        mapConcepts(conceptHierarchies, root, true);
        //this.conceptSelected = null;
    }

    public void resetConceptSelected(){
        conceptSelected= null;
    }

    public TreeNode mapConcepts(List<ConceptSMTK> concepts, TreeNode treeNode, boolean expanded) {

        ConceptSMTK conceptData = (ConceptSMTK)treeNode.getData();

        if(conceptData.equals(conceptSelected))
            expanded = false;

        treeNode.setExpanded(expanded);

        for (ConceptSMTK concept : concepts) {

            if(!concept.isRelationshipsLoaded())
                return treeNode;

            TreeNode childTreeNode = new DefaultTreeNode(concept, treeNode);

            List<ConceptSMTK> childConcepts = new ArrayList<>();

            try {
                for (Relationship relationship : concept.getRelationships()) {
                    childConcepts.add((ConceptSMTK)relationship.getTarget());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            mapConcepts(childConcepts, childTreeNode, expanded);
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

        int countConcept=conceptManager.countConceptBy(patron,drugsCategories,true);
        concepts = conceptManager.findConceptBy(patron, drugsCategories, 0, countConcept,true);

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

