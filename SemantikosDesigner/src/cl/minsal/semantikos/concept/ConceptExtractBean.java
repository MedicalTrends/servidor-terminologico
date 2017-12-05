package cl.minsal.semantikos.concept;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.RelationshipManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.relationships.Relationship;
import org.omnifaces.util.Ajax;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;

import static java.util.Collections.singletonList;

/**
 * Created by des01c7 on 23-08-16.
 */
@ManagedBean(name = "conceptExtractBean")
@ViewScoped
public class ConceptExtractBean implements Serializable {

    private Map<Long, List<ConceptSMTK>> concepts = new HashMap<>();

    private Map<Long, Integer> sizes = new HashMap<>();

    private Map<Long, Boolean> flags = new HashMap<>();

    private List<Category> categoryList = new ArrayList<>();

    private List<Category> selectedCategories = new ArrayList<>();

    private static final int BLOCK_SIZE = 100;

    private boolean processFinished = false;

    private static Long[] drugs = {13L, 33L, 34L, 35L, 36L, 37L, 38L, 39L};

    //@EJB
    private ConceptManager conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);

    //@EJB
    private CategoryManager categoryManager = (CategoryManager) ServiceLocator.getInstance().getService(CategoryManager.class);

    //@EJB
    private RelationshipManager relationshipManager = (RelationshipManager) ServiceLocator.getInstance().getService(RelationshipManager.class);

    @PostConstruct
    public void init() {

        selectedCategories = new ArrayList<>();
        for (Category category : categoryManager.getCategories()) {
            if(Arrays.asList(drugs).contains(category.getId())) {
                categoryList.add(category);
                concepts.put(category.getId(), new ArrayList<ConceptSMTK>());
                sizes.put(category.getId(), 0);
                flags.put(category.getId(), false);
            }
        }
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Category> getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(List<Category> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public Map<Long, Integer> getSizes() {
        return sizes;
    }

    public void setSizes(Map<Long, Integer> sizes) {
        this.sizes = sizes;
    }

    public Map<Long, List<ConceptSMTK>> getConcepts() {
        return concepts;
    }

    public void setConcepts(Map<Long, List<ConceptSMTK>> concepts) {
        this.concepts = concepts;
    }

    public Map<Long, Boolean> getFlags() {
        return flags;
    }

    public void setFlags(Map<Long, Boolean> flags) {
        this.flags = flags;
    }

    public boolean isProcessFinished() {
        return processFinished;
    }

    public void setProcessFinished(boolean processFinished) {
        this.processFinished = processFinished;
    }

    public void extract() {

        List<ConceptSMTK> tempConcepts = new ArrayList<>();

        for (Category selectedCategory : selectedCategories) {
            sizes.put(selectedCategory.getId(), conceptManager.countConcepts(null, Arrays.asList(selectedCategory), null, null));
        }

        for (Category selectedCategory : selectedCategories) {

            int total_size = sizes.get(selectedCategory.getId());

            for (int i = 0; i <= total_size / BLOCK_SIZE ; ++i) {

                tempConcepts = conceptManager.findConceptsPaginated(selectedCategory, BLOCK_SIZE, i, null);

                for (ConceptSMTK tempConcept : tempConcepts) {
                    tempConcept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(tempConcept));
                }

                concepts.get(selectedCategory.getId()).addAll(tempConcepts);
            }

            if(total_size % BLOCK_SIZE > 0) {

                tempConcepts = conceptManager.findConceptsPaginated(selectedCategory, total_size / BLOCK_SIZE, total_size % BLOCK_SIZE, null);

                for (ConceptSMTK tempConcept : tempConcepts) {
                    tempConcept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(tempConcept));
                }

                concepts.get(selectedCategory.getId()).addAll(tempConcepts);

            }

            flags.put(selectedCategory.getId(), true);

        }

        RequestContext reqCtx = RequestContext.getCurrentInstance();
        reqCtx.execute("PF('poll').stop();");
        Ajax.update("extractorForm:process-state");
        //selectedCategories = new ArrayList<>();
        processFinished = true;
    }

    public int getCurrentProgress(long id) {
        return Math.round(((float)concepts.get(id).size()/(float)sizes.get(id))*100);
    }
}
