package cl.minsal.semantikos.browser;

import cl.minsal.semantikos.clients.ServiceLocator;
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

import static java.util.Collections.EMPTY_LIST;


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

    private List<Category> drugsCategories;

    private transient TreeNode root;

    //@EJB
    DrugsManager drugsManager = (DrugsManager) ServiceLocator.getInstance().getService(DrugsManager.class);

    //@EJB
    CategoryManager categoryManager = (CategoryManager) ServiceLocator.getInstance().getService(CategoryManager.class);

    //@EJB
    ConceptManager conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);

    @PostConstruct
    public void init(){
        root = new DefaultTreeNode(new ConceptSMTK(categoryManager.getCategoryById(39)), null);
        drugsCategories = drugsManager.getDrugsCategories();
    }

    public ConceptSMTK getConceptSelected() {
        return conceptSelected;
    }

    public void setConceptSelected(ConceptSMTK conceptSelected) {
        if(conceptSelected==null) {
            return;
        }
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

        concepts = conceptManager.findConcepts(patron, drugsCategories, null, true);

        return concepts;
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

