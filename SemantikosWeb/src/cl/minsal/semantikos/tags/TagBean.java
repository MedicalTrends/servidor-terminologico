package cl.minsal.semantikos.tags;

import cl.minsal.semantikos.clients.RemoteEJBClientFactory;
import cl.minsal.semantikos.concept.ConceptBean;
import cl.minsal.semantikos.kernel.components.TagManager;
import cl.minsal.semantikos.kernel.components.UserManager;
import cl.minsal.semantikos.model.tags.Tag;
import org.primefaces.event.CellEditEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gustavo Punucura
 */

@ManagedBean(name = "tagBean")
@ViewScoped
public class TagBean implements Serializable{


    private List<Tag> tagList;

    private List<Tag> tagListToConcept;

    private List<Tag> tagListTable;

    private List<Tag> findSonTagList;

    private List<Tag> listTagSon;


    private Tag selectTagListTags;

    private Tag tagCreate;

    private Tag parentTagToCreate;

    private Tag tagSelected;

    private Tag tagEdit;

    private Tag parentTag;

    private Tag parentTagSelect;

    private Tag sonTagSelect;

    private Tag addSonSelect;


    private String nameTag;

    private String colorText;

    private String colorBackground;


    //@EJB
    private TagManager tagManager = (TagManager) RemoteEJBClientFactory.getInstance().getManager(TagManager.class);

    @ManagedProperty(value="#{conceptBean}")
    private ConceptBean conceptBean;

    public ConceptBean getConceptBean() {
        return conceptBean;
    }

    public void setConceptBean(ConceptBean conceptBean) {
        this.conceptBean = conceptBean;
    }

    @PostConstruct
    public void init() {
        tagListTable= tagManager.getAllTags();
        tagList= tagManager.getAllTags();
        tagListToConcept=  tagManager.getAllTags();
        findSonTagList=tagManager.getAllTagsWithoutParent();
        listTagSon=tagManager.getAllTagsWithoutParent();
        tagCreate= new Tag(-1,null,null,null,null);
        parentTagToCreate= new Tag(-1,null,null,null,null);
        tagEdit= new Tag(-1,null,null,null,null);

    }

    /**
     *    Methods
     */

    /**
     * Método encargado de buscar etiquetas por el nombre de la etiqueta, menos las
     * que estén relacionadas a la etiqueta que se da como parámetro al manager. (Etiqueta a crear)
     * @param nameTag nombre de la etiqueta
     * @return retorna una lista de etiquetas
     */
    public List<Tag> findTagByName(String nameTag) {

        if (nameTag != null) {
            return tagManager.findTag(tagCreate,nameTag);
        }
        return new ArrayList<Tag>();
    }

    /**
     * Método encargado de buscar etiquetas por el nombre de la etiqueta, menos las
     * que estén relacionadas a la etiqueta que se da como parámetro al manager. (Etiqueta a editar)
     * @param nameTag
     * @return retorna una lista de etiquetas
     */
    public List<Tag> findTagByNameEditTag(String nameTag) {
        if (nameTag != null) {
            return tagManager.findTag(tagEdit,nameTag);
        }
        return new ArrayList<Tag>();
    }

    /**
     * Método encargado de buscar una lista de etiquetas que pueden ser hijos de la etiqueta que se está creando.
     * @return retorna una lista de los posibles hijos.
     */
    public List<Tag> findTagSon() {

        findSonTagList = tagManager.getAllTagsWithoutParent();

        if(tagCreate.getParentTag()!=null){
            if (tagCreate.getParentTag().getId() !=-1) {
                findSonTagList = tagManager.getOtherTags(tagCreate.getParentTag());
            }
        }
        if(tagCreate.getSonTag().size()>0){
            for (Tag tagSonToCreate: tagCreate.getSonTag()) {
                for (Tag tagSonList : findSonTagList) {
                    if (tagSonList.equals(tagSonToCreate)) {
                        findSonTagList.remove(tagSonList);
                        break;
                    }
                }
            }
            return findSonTagList;
        }
        return findSonTagList;
    }

    /**
     * Método encargado de buscar una lista de etiquetas que pueden ser hijos de la etiqueta que se está editando.
     * @return retorna una lista de los posibles hijos.
     */
    public List<Tag> listTagSonEdit() {
        return listTagSon= tagManager.getOtherTags(tagEdit);
    }

    /**
     * Se agrega una etiqueta padre que ha sido creada a la etiqueta que se está creando.
     */
    public void createTagParent(){
        tagCreate.setParentTag(parentTagToCreate);
    }

    /**
     * Se agrega una etiqueta hijo que ha sido creada a la etiqueta que se está creando.
     */
    public void createTagSon(){
        tagCreate.addSon(new Tag(-1,nameTag,colorBackground,colorText,null));
    }

    /**
     * Se agrega una etiqueta padre que ha sido seleccionada a la etiqueta que se está creando.
     */
    public void addTagParent(){
        tagCreate.setParentTag(parentTagSelect);
        findTagSon();
    }

    /**
     * Se agrega una etiqueta hijo que ha sido seleccionada a la etiqueta que se está creando.
     */
    public void addTagSon(){
        tagCreate.addSon(sonTagSelect);
        findTagSon();

    }

    /**
     * Se remueve una etiqueta hijo de la etiqueta que se está creando.
     * @param son etiqueta a remover
     */
    public void removeTagSon(Tag son){
        tagCreate.getSonTag().remove(son);
    }

    /**
     * Se remueve la etiqueta padre de la etiqueta que se está creando.
     */
    public void removeTagParent(){
        tagCreate.setParentTag(null);
        parentTagSelect=null;
        parentTagToCreate= new Tag(-1,null,null,null,null);
    }

    /**
     * Método encargado de persistir la etiqueta que se está creando.
     */
    public void createTag(){
        tagManager.persist(tagCreate);
        tagListTable= tagManager.getAllTags();
        tagList= tagManager.getAllTags();
        findSonTagList=tagManager.getAllTagsWithoutParent();
        listTagSon=tagManager.getAllTagsWithoutParent();
        tagCreate= new Tag(-1,null,null,null,null);
        parentTagToCreate= new Tag(-1,null,null,null,null);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Etiqueta creada", "La etiqueta se creó exitosamente");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /**
     * Método encargado de agregar la etiqueta que se está creando al concepto.
     */
    public void createTagToConcept(){
        FacesContext context = FacesContext.getCurrentInstance();
        if(tagCreate.getParentTag()!=null){
            tagCreate.setName(tagCreate.getParentTag().getName()+"/"+tagCreate.getName());
        }
        if( tagManager.containTag(tagCreate.getName()) ){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La etiqueta " +tagCreate.getName()+" ya existe"));
            return;
        }
        if( tagCreate.getName().length()<3 ){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La etiqueta debe contener al menos 3 caracteres"));
            return;
        }
        if(tagCreate.getName()== null || tagCreate.getName().length()==0 || tagCreate.getColorBackground()==null || tagCreate.getColorLetter()==null){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Complete la información de la etiqueta"));

        }else{

            if(conceptBean.getConcept().isPersistent()){
                tagCreate.setId(tagManager.persist(tagCreate));
                tagManager.assignTag(conceptBean.getConcept(),tagCreate);
            }
            conceptBean.getConcept().getTags().add(tagCreate);
            tagListTable= tagManager.getAllTags();
            tagList= tagManager.getAllTags();
            findSonTagList=tagManager.getAllTagsWithoutParent();
            listTagSon=tagManager.getAllTagsWithoutParent();
            tagCreate= new Tag(-1,null,null,null,null);
            parentTagSelect= null;
            parentTagToCreate= new Tag(-1,null,null,null,null);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Etiqueta creada", "La etiqueta se creó exitosamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }



    }

    /**
     * Método encargado de agregar la etiqueta que se selecciona al concepto.
     */
    public void addTagToConcept(){
        if(tagSelected!=null){
            if(conceptBean.getConcept().isPersistent()){
                tagManager.assignTag(conceptBean.getConcept(),tagSelected);
            }
            conceptBean.getConcept().getTags().add(tagSelected);
            tagSelected= null;
            tagListToConcept=  tagManager.getAllTags();
        }else{
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se seleccionó una etiqueta para agregar"));
        }


    }

    /**
     * Método encargado de eliminar la etiqueta del concepto.
     * @param tagToDelete etiqueta a eliminar.
     */
    public void deleteTagToConcept(Tag tagToDelete){
        if(conceptBean.getConcept().isPersistent()){
            tagManager.unassignTag(conceptBean.getConcept(),tagToDelete);
        }

        for (int i = 0; i < conceptBean.getConcept().getTags().size(); i++) {
            if(conceptBean.getConcept().getTags().get(i).getId()==tagToDelete.getId())
                conceptBean.getConcept().getTags().remove(i);
        }

    }



    /**
     * Método encargado de vincular una etiqueta hijo a la etiqueta que se está editando.
     */
    public void linkSon() {
        tagEdit.addSon(addSonSelect);
        tagManager.link(tagEdit,addSonSelect);
        listTagSonEdit();
        tagListTable= tagManager.getAllTags();
    }

    /**
     * Método encargado de vincular una etiqueta padre a la etiqueta que se está editando.
     */
    public void linkParent(){
        tagEdit.setParentTag(parentTag);
        tagManager.link(parentTag,tagEdit);
        listTagSonEdit();
        tagListTable= tagManager.getAllTags();
        parentTag=null;
    }

    /**
     * Método encargado de desvicular una etiqueta hijo de la etiqueta que se está editando.
     * @param tagUnlink etiqueta hijo
     */
    public void unlinkSon(Tag tagUnlink) {
        tagManager.unlink(tagEdit, tagUnlink);
        tagEdit.getSonTag().remove(tagUnlink);
        tagListTable= tagManager.getAllTags();
    }

    /**
     * Método encargado de desvicular una etiqueta padre de la etiqueta que se está editando
     */
    public void unlinkParent() {
        tagManager.unlink(tagEdit.getParentTag(), tagEdit);
        tagEdit.setParentTag(null);
        tagListTable= tagManager.getAllTags();
    }

    /**
     * Método encargado de eliminar la etiqueta.
     */
    public void deleteTag() {
        tagManager.removeTag(tagEdit);
        tagListTable= tagManager.getAllTags();
    }

    public void removeTag() {

        tagManager.removeTag(tagEdit);
        conceptBean.getConcept().setTags(tagManager.getTagByConcept( conceptBean.getConcept()));
        tagListTable= tagManager.getAllTags();

    }

    public void onRowEdit(CellEditEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        Tag tagToEdit = context.getApplication().evaluateExpressionGet(context, "#{tag}", Tag.class);
        tagManager.update(tagToEdit);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Etiqueta actualizada", "La etiqueta se actualizo exitosamente");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }


    public void updateTag(){
        tagManager.update(tagEdit);

        tagListToConcept= tagManager.getAllTags();

        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Etiqueta actualizada", "La etiqueta se actualizo exitosamente"));

    }


    public  boolean containTagToConcept(Tag tagcontain){
        for (Tag tagcontainconcept: conceptBean.getConcept().getTags()
             ) {
            if(tagcontain.getId()== tagcontainconcept.getId()){
                return true;
            }

        }return false;
    }

    public long conceptContain( Tag tag){
        return tagManager.conceptContain(tag);
    }


    /**
     * Getter & Setter
     */

    public List<Tag> getTagListToConcept() {
        return tagListToConcept;
    }

    public void setTagListToConcept(List<Tag> tagListToConcept) {
        this.tagListToConcept = tagListToConcept;
    }

    public List<Tag> getFindSonTagList() {
        return findSonTagList;
    }

    public List<Tag> getListTagSon() {
        return listTagSon;
    }

    public void setListTagSon(List<Tag> listTagSon) {
        this.listTagSon = listTagSon;
    }

    public void setFindSonTagList(List<Tag> findSonTagList) {
        this.findSonTagList = findSonTagList;
    }

    public Tag getTagCreate() {
        return tagCreate;
    }

    public void setTagCreate(Tag tagCreate) {
        this.tagCreate = tagCreate;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public Tag getSelectTagListTags() {
        return selectTagListTags;
    }

    public void setSelectTagListTags(Tag selectTagListTags) {
        this.selectTagListTags = selectTagListTags;
    }

    public Tag getTagSelected() {
        return tagSelected;
    }

    public void setTagSelected(Tag tagSelected) {
        this.tagSelected = tagSelected;
    }

    public String getColorText() {
        return colorText;
    }

    public void setColorText(String colorText) {
        this.colorText = colorText;
    }

    public String getColorBackground() {
        return colorBackground;
    }

    public void setColorBackground(String colorBackground) {
        this.colorBackground = colorBackground;
    }

    public String getNameTag() {
        return nameTag;
    }

    public void setNameTag(String nameTag) {
        this.nameTag = nameTag;
    }

    public Tag getParentTag() {
        return parentTag;
    }

    public void setParentTag(Tag parentTag) {
        this.parentTag = parentTag;
    }

    public Tag getTagEdit() {
        return tagEdit;
    }

    public void setTagEdit(Tag tagEdit) {
        this.tagEdit = tagEdit;
        listTagSonEdit();
    }

    public Tag getParentTagToCreate() {
        return parentTagToCreate;
    }

    public void setParentTagToCreate(Tag parentTagToCreate) {
        this.parentTagToCreate = parentTagToCreate;
    }

    public Tag getParentTagSelect() {
        return parentTagSelect;
    }

    public void setParentTagSelect(Tag parentTagSelect) {
        this.parentTagSelect = parentTagSelect;
    }

    public Tag getSonTagSelect() {
        return sonTagSelect;
    }

    public void setSonTagSelect(Tag sonTagSelect) {
        this.sonTagSelect = sonTagSelect;
    }

    public Tag getAddSonSelect() {
        return addSonSelect;
    }

    public void setAddSonSelect(Tag addSonSelect) {
        this.addSonSelect = addSonSelect;
    }

    public List<Tag> getTagListTable() {
        return tagListTable;
    }

    public void setTagListTable(List<Tag> tagListTable) {
        this.tagListTable = tagListTable;
    }

    public TagManager getTagManager() {
        return tagManager;
    }


}
