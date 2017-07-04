package cl.minsal.semantikos.model.browser;

import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.tags.Tag;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by BluePrints Developer on 21-09-2016.
 */
/**
 * Esta clase representa la Consulta del Navegador de Categorías
 *
 * @author Diego Soto.
 */
public class GeneralQuery extends Query implements IQuery {

    /**
     * Filtros estáticos
     */
    private List<Category> categories;

    private Boolean toBeReviewed;

    private Boolean toBeConsulted;

    private Boolean modeled;

    private List<Tag> tags = new ArrayList<>();

    private boolean customFilterable;

    private boolean showRelatedConcepts;

    /**
     * Filtros dinámicos
     */
    private List<QueryFilter> filters = new ArrayList<>();

    /**
     * Filtros dinámicos por atributos de relación
     */
    private List<QueryFilterAttribute> attributeFilters = new ArrayList<>();



    /**
     * Definiciones para las columnas de 2o orden
     */
    private List<RelationshipDefinition> sourceSecondOrderShowableAttributes = new ArrayList<>();

    /**
     * Filtros custom
     */
    private Date creationDateSince;
    private Date creationDateTo;
    private User user;

    public List<QueryFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<QueryFilter> filters) {
        this.filters = filters;
    }


    public List<QueryFilterAttribute> getAttributeFilters() {
        return attributeFilters;
    }

    public void setAttributeFilters(List<QueryFilterAttribute> attributeFilters) {
        this.attributeFilters = attributeFilters;
    }

    public Boolean getToBeReviewed() {
        return toBeReviewed;
    }

    public void setToBeReviewed(Boolean toBeReviewed) {
        this.toBeReviewed = toBeReviewed;
    }

    public Boolean getToBeConsulted() {
        return toBeConsulted;
    }

    public void setToBeConsulted(Boolean toBeConsulted) {
        this.toBeConsulted = toBeConsulted;
    }

    public Boolean getModeled() {
        return modeled;
    }

    public void setModeled(Boolean modeled) {
        this.modeled = modeled;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        if(tags == null)
            this.tags.clear();
        else
            this.tags = tags;
    }

    public Date getCreationDateSince() {
        return creationDateSince;
    }

    public void setCreationDateSince(Date creationDateSince) {
        this.creationDateSince = creationDateSince;
    }

    public Date getCreationDateTo() {
        return creationDateTo;
    }

    public void setCreationDateTo(Date creationDateTo) {
        this.creationDateTo = creationDateTo;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isCustomFilterable() {
        return customFilterable;
    }

    public void setCustomFilterable(boolean customFilterable) {
        this.customFilterable = customFilterable;
    }

    public boolean isShowRelatedConcepts() {
        return showRelatedConcepts;
    }

    public void setShowRelatedConcepts(boolean showRelatedConcepts) {
        this.showRelatedConcepts = showRelatedConcepts;
    }

    public List<RelationshipDefinition> getSourceSecondOrderShowableAttributes() {
        return sourceSecondOrderShowableAttributes;
    }

    public void setSourceSecondOrderShowableAttributes(List<RelationshipDefinition> sourceSecondOrderShowableAttributes) {
        this.sourceSecondOrderShowableAttributes = sourceSecondOrderShowableAttributes;
    }


    public List<QueryColumn> getColumnsByRelationshipDefinition(RelationshipDefinition relationshipDefinition){
        List<QueryColumn> someColumns = new ArrayList<>();

        for (QueryColumn column : this.getColumns()) {
            if(column.getRelationshipDefinition().equals(relationshipDefinition))
                someColumns.add(column);
        }

        return someColumns;
    }

    /**
     * Este método es responsable de recuperar las columnas dinámicas que corresponden a relaciones de 2o orden, es decir,
     * relaciones que provienen de una relación a concepto SMTK
     *
     * @return Una lista de definiciones correspondientes a columnas de 2o orden
     */
    public List<RelationshipDefinition> getSecondOrderDefinitions(){
        List<RelationshipDefinition> someDefinitions = new ArrayList<>();

        for (QueryColumn column : this.getColumns()) {
            if(column.isSecondOrder())
                someDefinitions.add(column.getRelationshipDefinition());
        }

        return someDefinitions;
    }

    /**
     * Este método es responsable de recuperar los ids de las categorías filtradas en el objeto de consulta del navegador
     * de categorías
     *
     * @return Una lista de <code>java.util.List</code> de <code>java.lang.Long</code> correspondiente a los ids de las
     * categorías filtradas
     */
    public Long[] getCategoryValues(){

        List<Long> categoryValues = new ArrayList<>();

        for (Category category : getCategories())
            categoryValues.add(category.getId());

        if(categoryValues.isEmpty())
            return null;

        else {
            Long[] array = new Long[categoryValues.size()];
            return categoryValues.toArray(array);
        }
    }

    /**
     * Este método es responsable de recuperar los ids de los tags filtrados en el objeto de consulta del navegador
     * de categorías
     *
     * @return Una lista de <code>java.util.List</code> de <code>java.lang.Long</code> correspondiente a los ids de los
     * tags filtrados
     */
    public Long[] getTagValues(){

        List<Long> tagValues = new ArrayList<>();

        for (Tag tag : getTags())
            tagValues.add(tag.getId());

        if(tagValues.isEmpty())
            return null;

        else {
            Long[] array = new Long[tagValues.size()];
            return tagValues.toArray(array);
        }
    }

    /**
     * Este método es responsable de recuperar los ids de las categorías correspondientes a los conceptos filtrados en
     * el objeto de consulta del navegador de categorías
     *
     * @return Una lista de <code>java.util.List</code> de <code>java.lang.Long</code> correspondiente a los ids de las
     * categorías de los conceptos filtrados
     */
    public Long[] getConceptCategoryValues(){

        List<Long> conceptCategoryValues = new ArrayList<>();

        for (QueryFilter filter : filters)
            conceptCategoryValues.addAll(filter.getCategoryValues());

        if(conceptCategoryValues.isEmpty())
            return null;

        else {
            Long[] array = new Long[conceptCategoryValues.size()];
            return conceptCategoryValues.toArray(array);
        }
    }

    /**
     * Este método es responsable de recuperar los ids de los conceptos filtrados en el objeto de consulta del navegador
     * de categorías
     *
     * @return Una lista de <code>java.util.List</code> de <code>java.lang.Long</code> correspondiente a los ids de los
     * conceptos filtrados
     */
    public Long[] getConceptValues(){

        List<Long> conceptValues = new ArrayList<>();

        for (QueryFilter filter : filters)
            conceptValues.addAll(filter.getConceptValues());

        if(conceptValues.isEmpty())
            return null;

        else {
            Long[] array = new Long[conceptValues.size()];
            return conceptValues.toArray(array);
        }
    }

    /**
     * Este método es responsable de recuperar los ids de las tablas auxiliares correspondientes a los registros de
     * tablas auxiliares filtrados en el objeto de consulta del navegador de categorías
     *
     * @return Una lista de <code>java.util.List</code> de <code>java.lang.Long</code> correspondiente a los ids de las
     * tablas auxiliares de los conceptos filtrados
     */
    public Long[] getHelperTableValues(){

        List<Long> helperTableValues = new ArrayList<>();

        for (QueryFilter filter : filters) {
            if(!filter.isSecondOrder()) {
                helperTableValues.addAll(filter.getHelperTableValues());
            }
        }

        if(helperTableValues.isEmpty())
            return null;

        else {
            Long[] array = new Long[helperTableValues.size()];
            return helperTableValues.toArray(array);
        }
    }

    /**
     * Este método es responsable de recuperar los ids de los registros de tablas auxiliares filtrados en el objeto de
     * consulta del navegador de categorías
     *
     * @return Una lista de <code>java.util.List</code> de <code>java.lang.Long</code> correspondiente a los ids de los
     * registros de tablas auxiliares filtrados
     */
    public Long[] getHelperTableRecordValues(){

        List<Long> helperTableRecordValues = new ArrayList<>();

        for (QueryFilter filter : filters) {
            if(!filter.isSecondOrder()) {
                helperTableRecordValues.addAll(filter.getHelperTableRecordValues());
            }
        }

        if(helperTableRecordValues.isEmpty())
            return null;

        else {
            Long[] array = new Long[helperTableRecordValues.size()];
            return helperTableRecordValues.toArray(array);
        }
    }

    /**
     * Este método es responsable de recuperar los ids de las tablas auxiliares como relaciones de 2o orden
     * correspondientes a los registros de tablas auxiliares filtrados en el objeto de consulta del navegador de categorías
     *
     * @return Una lista de <code>java.util.List</code> de <code>java.lang.Long</code> correspondiente a los ids de las
     * tablas auxiliares de los conceptos filtrados
     */
    public Long[] getSecondOrderHelperTableValues(){

        List<Long> helperTableValues = new ArrayList<>();

        for (QueryFilter filter : filters) {
            if(filter.isSecondOrder()) {
                helperTableValues.addAll(filter.getHelperTableValues());
            }
        }

        if(helperTableValues.isEmpty()) {
            return null;
        }

        else {
            Long[] array = new Long[helperTableValues.size()];
            return helperTableValues.toArray(array);
        }
    }

    /**
     * Este método es responsable de recuperar los ids de los registros de tablas auxiliares como relaciones de 2o orden
     * filtrados en el objeto de consulta del navegador de categorías
     *
     * @return Una lista de <code>java.util.List</code> de <code>java.lang.Long</code> correspondiente a los ids de los
     * registros de tablas auxiliares filtrados
     */
    public Long[] getSecondOrderHelperTableRecordValues(){

        List<Long> helperTableRecordValues = new ArrayList<>();

        for (QueryFilter filter : filters) {
            if(filter.isSecondOrder()) {
                helperTableRecordValues.addAll(filter.getHelperTableRecordValues());
            }
        }

        if(helperTableRecordValues.isEmpty()) {
            return null;
        }

        else {
            Long[] array = new Long[helperTableRecordValues.size()];
            return helperTableRecordValues.toArray(array);
        }
    }

    /**
     * Este método es responsable de recuperar los ids de las tablas auxiliares como atributos de relación
     * correspondientes a los registros de tablas auxiliares filtrados en el objeto de consulta del navegador de categorías
     *
     * @return Una lista de <code>java.util.List</code> de <code>java.lang.Long</code> correspondiente a los ids de las
     * tablas auxiliares de los conceptos filtrados
     */
    public Long[] getAttributeHelperTableValues(){

        List<Long> helperTableValues = new ArrayList<>();

        for (QueryFilterAttribute filter : attributeFilters) {
            if(!filter.isSecondOrder()) {
                helperTableValues.addAll(filter.getHelperTableValues());
            }
        }

        if(helperTableValues.isEmpty()) {
            return null;
        }

        else {
            Long[] array = new Long[helperTableValues.size()];
            return helperTableValues.toArray(array);
        }
    }

    /**
     * Este método es responsable de recuperar los ids de los registros de tablas auxiliares como atributos de relación
     * filtrados en el objeto de consulta del navegador de categorías
     *
     * @return Una lista de <code>java.util.List</code> de <code>java.lang.Long</code> correspondiente a los ids de los
     * registros de tablas auxiliares filtrados
     */
    public Long[] getAttributeHelperTableRecordValues(){

        List<Long> helperTableRecordValues = new ArrayList<>();

        for (QueryFilterAttribute filter : attributeFilters) {
            if(!filter.isSecondOrder()) {
                helperTableRecordValues.addAll(filter.getHelperTableRecordValues());
            }
        }

        if(helperTableRecordValues.isEmpty()) {
            return null;
        }

        else {
            Long[] array = new Long[helperTableRecordValues.size()];
            return helperTableRecordValues.toArray(array);
        }
    }

    /**
     * Este método es responsable de recuperar los ids de las definiciones correspondientes a los tipos básicos
     * filtrados en el objeto de consulta del navegador de categorías
     *
     * @return Una lista de <code>java.util.List</code> de <code>java.lang.Long</code> correspondiente a los ids tipos
     * básicos filtrados en el objeto de consulta del navegador de categorías
     */
    public Long[] getBasicTypeDefinitionValues(){

        List<Long> basicTypeDefinitionValues = new ArrayList<>();

        for (QueryFilter filter : filters)
            basicTypeDefinitionValues.addAll(filter.getBasicTypeDefinitionValues());

        if(basicTypeDefinitionValues.isEmpty())
            return null;

        else {
            Long[] array = new Long[basicTypeDefinitionValues.size()];
            return basicTypeDefinitionValues.toArray(array);
        }
    }

    /**
     * Este método es responsable de recuperar los ids de los tipos básicos filtrados en el objeto de consulta del
     * navegador de categorías
     *
     * @return Una lista de <code>java.util.List</code> de <code>java.lang.Long</code> correspondiente a los ids de los
     * tipos básicos filtrados
     */
    public String[] getBasicTypeValues(){

        List<String> basicTypeValues = new ArrayList<>();

        for (QueryFilter filter : filters)
            basicTypeValues.addAll(filter.getBasicTypeValues());

        if(basicTypeValues.isEmpty())
            return null;

        else {
            String[] array = new String[basicTypeValues.size()];
            return basicTypeValues.toArray(array);
        }
    }

    /**
     * Este método es responsable de recuperar los ids de los usuarios filtrados en el objeto de consulta del navegador
     * de categorías
     *
     * @return Una lista de <code>java.util.List</code> de <code>java.lang.Long</code> correspondiente a los ids de los
     * usuarios
     */
    public Long getUserValue(){
        if(getUser()==null)
            return null;
        else
            return getUser().getId();
    }

    public void resetQuery() {
        this.modeled = null;
        this.toBeConsulted = null;
        this.toBeReviewed = null;
        this.tags = new ArrayList<>();
        this.user = null;
        this.creationDateSince = null;
        this.creationDateTo = null;
        for (QueryFilter filter : filters) {
            filter.getTargets().clear();
        }
        //this.filters = new ArrayList<>();
        super.setQuery(null);;
        super.setTruncateMatch(false);
    }

    /**
     * Este método es responsable de recuperar los parámetros de los filtros del objeto de consulta del navegador de
     * categorías
     *
     * @return Una lista de <code>cl.minsal.semantikos.model.browser.QueryParameter</code> correspondiente a los
     * parámetros de los filtros
     */
    @Override
    public List<QueryParameter> getQueryParameters(){

        List<QueryParameter> queryParameters = new ArrayList<>();

        queryParameters.add(new QueryParameter(Long.class, getCategoryValues(), true)); /** ids categorias **/
        queryParameters.add(new QueryParameter(String.class, getQuery(), false)); /** patrón de búsqueda **/
        queryParameters.add(new QueryParameter(Boolean.class, getTruncateMatch(), false)); /** tipo de búsqueda **/
        queryParameters.add(new QueryParameter(Boolean.class, getModeled(), false)); /** está modelado? **/
        queryParameters.add(new QueryParameter(Boolean.class, getToBeReviewed(), false)); /** para revisar? **/
        queryParameters.add(new QueryParameter(Boolean.class, getToBeConsulted(), false)); /** para consultar? **/
        queryParameters.add(new QueryParameter(Long.class, getTagValues(), true)); /** etiquetas **/
        queryParameters.add(new QueryParameter(Long.class, getConceptCategoryValues(), true));
        queryParameters.add(new QueryParameter(Long.class, getConceptValues(), true));
        queryParameters.add(new QueryParameter(Long.class, getBasicTypeDefinitionValues(), true)); /** ids basicTypeDefinitionValues **/
        queryParameters.add(new QueryParameter(String.class, getBasicTypeValues(), true)); /** ids basicTypeValues **/
        queryParameters.add(new QueryParameter(Long.class, getHelperTableValues(), true)); /** ids helperTableValues **/
        queryParameters.add(new QueryParameter(Long.class, getHelperTableRecordValues(), true)); /** ids helperTableRecordValues **/
        queryParameters.add(new QueryParameter(Long.class, getSecondOrderHelperTableValues(), true)); /** ids helperTableValues de 2o Orden **/
        queryParameters.add(new QueryParameter(Long.class, getSecondOrderHelperTableRecordValues(), true)); /** ids helperTableRecordValues de 2o Orden **/
        queryParameters.add(new QueryParameter(Long.class, getAttributeHelperTableValues(), true)); /** ids atributos de targetDef helperTableValues **/
        queryParameters.add(new QueryParameter(Long.class, getAttributeHelperTableRecordValues(), true)); /** ids atributos de target helperTableRecordValues **/
        queryParameters.add(new QueryParameter(Timestamp.class, getCreationDateSince(), false));
        queryParameters.add(new QueryParameter(Timestamp.class, getCreationDateTo(), false));
        queryParameters.add(new QueryParameter(Long.class, getUserValue(), false));
        queryParameters.add(new QueryParameter(Integer.class, getOrder(), false));
        queryParameters.add(new QueryParameter(String.class, getAsc(), false));
        queryParameters.add(new QueryParameter(Integer.class, getPageNumber(), false));
        queryParameters.add(new QueryParameter(Integer.class, getPageSize(), false));
        //conceptQueryParameters.add(new ConceptQueryParameter(String.class, getOrder(), false));

        return queryParameters;
    }

}
