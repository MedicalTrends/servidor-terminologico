package cl.minsal.semantikos.admincategory;

import cl.minsal.semantikos.clients.RemoteEJBClientFactory;
import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.TagSMTKManager;
import cl.minsal.semantikos.users.AuthenticationBean;

import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.relationships.Multiplicity;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.tags.TagSMTK;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by des01c7 on 31-08-16.
 */
@ManagedBean(name="beanCategory")
@ViewScoped
public class beanCategory {

    private Category category;

    private String nameRelationshipDefinition;

    private String descriptionRelationshipDefinition;

    private List<RelationshipDefinition> relationshipDefinitions;

    private List<Category> categories;

    private List<TagSMTK> tagSMTKList;

    private Category categorySelected;

    private boolean isSmtk;

    private boolean isOther;

    private int type;

    private int upperBoundary;

    private int lowerBoundary;

    //@EJB
    private CategoryManager categoryManager;

    //@EJB
    private TagSMTKManager tagSMTKManager;

    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    @PostConstruct
    public void init(){

        categoryManager = (CategoryManager) RemoteEJBClientFactory.getInstance().getManager(CategoryManager.class);
        tagSMTKManager = (TagSMTKManager) RemoteEJBClientFactory.getInstance().getManager(TagSMTKManager.class);

        category= new Category();
        categories= categoryManager.getCategories();
        relationshipDefinitions=new ArrayList<RelationshipDefinition>();
        isSmtk=false;
        isOther=false;
        tagSMTKList= tagSMTKManager.getAllTagSMTKs();
    }

    public void selectType(){
        if(type==1){
            isSmtk=true;
            isOther=false;
        }else{
            if(type==2){
                isOther=true;
                isSmtk=false;
            }else{
                if(type==3){
                    isOther=true;
                    isSmtk=false;
                }
            }
        }
    }

    public void createCategory(){
        category.setRelationshipDefinitions(relationshipDefinitions);
        categoryManager.createCategory(category,authenticationBean.getLoggedUser());
    }

    public void createRelationshipDefinition(){
        if(isSmtk){
            relationshipDefinitions.add(new RelationshipDefinition(nameRelationshipDefinition,descriptionRelationshipDefinition,new Multiplicity(lowerBoundary,upperBoundary),categorySelected));
        }
        if(isOther){
            relationshipDefinitions.add(new RelationshipDefinition(nameRelationshipDefinition,descriptionRelationshipDefinition,new Multiplicity(lowerBoundary,upperBoundary),null));
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('relationshipDefinitionDialog').hide();");
    }



    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<RelationshipDefinition> getRelationshipDefinitions() {
        return relationshipDefinitions;
    }

    public void setRelationshipDefinitions(List<RelationshipDefinition> relationshipDefinitions) {
        this.relationshipDefinitions = relationshipDefinitions;
    }

    public String getNameRelationshipDefinition() {
        return nameRelationshipDefinition;
    }

    public void setNameRelationshipDefinition(String nameRelationshipDefinition) {
        this.nameRelationshipDefinition = nameRelationshipDefinition;
    }

    public String getDescriptionRelationshipDefinition() {
        return descriptionRelationshipDefinition;
    }

    public void setDescriptionRelationshipDefinition(String descriptionRelationshipDefinition) {
        this.descriptionRelationshipDefinition = descriptionRelationshipDefinition;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Category getCategorySelected() {
        return categorySelected;
    }

    public void setCategorySelected(Category categorySelected) {
        this.categorySelected = categorySelected;
    }

    public boolean isSmtk() {
        return isSmtk;
    }

    public void setSmtk(boolean smtk) {
        isSmtk = smtk;
    }

    public boolean isOther() {
        return isOther;
    }

    public void setOther(boolean other) {
        isOther = other;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUpperBoundary() {
        return upperBoundary;
    }

    public void setUpperBoundary(int upperBoundary) {
        this.upperBoundary = upperBoundary;
    }

    public int getLowerBoundary() {
        return lowerBoundary;
    }

    public void setLowerBoundary(int lowerBoundary) {
        this.lowerBoundary = lowerBoundary;
    }

    public List<TagSMTK> getTagSMTKList() {
        return tagSMTKList;
    }

    public void setTagSMTKList(List<TagSMTK> tagSMTKList) {
        this.tagSMTKList = tagSMTKList;
    }

    public AuthenticationBean getAuthenticationBean() {
        return authenticationBean;
    }

    public void setAuthenticationBean(AuthenticationBean authenticationBean) {
        this.authenticationBean = authenticationBean;
    }
}
