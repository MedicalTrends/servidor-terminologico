package cl.minsal.semantikos.model.browser;

import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.Tag;
import cl.minsal.semantikos.model.User;
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
public class GeneralQuery {

    /**
     * Filtros estáticos
     */
    private List<Category> categories;

    private String query;

    private Boolean toBeReviewed;
    private Boolean toBeConsulted;

    private Boolean modeled;

    private List<Tag> tags = new ArrayList<>();

    private boolean customFilterable;

    /**
     * Filtros dinámicos
     */
    private List<QueryFilter> filters = new ArrayList<>();

    /**
     * Columnas dinámicas
     */
    private List<QueryColumn> columns = new ArrayList<>();

    /**
     * Filtros custom
     */
    private Date creationDateSince;
    private Date creationDateTo;
    private User user;

    /**
     * Orden
     */
    private int order;
    private String asc;

    /**
     * Paginación
     */
    private int pageSize;
    private int pageNumber;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<ConceptQueryFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<ConceptQueryFilter> filters) {
        this.filters = filters;
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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getAsc() {
        return asc;
    }

    public void setAsc(String asc) {
        this.asc = asc;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<ConceptQueryColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<ConceptQueryColumn> columns) {
        this.columns = columns;
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

    public List<ConceptQueryColumn> getColumnsByRelationshipDefinition(RelationshipDefinition relationshipDefinition){
        List<ConceptQueryColumn> someColumns = new ArrayList<>();

        for (ConceptQueryColumn column : this.getColumns()) {
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

        for (ConceptQueryColumn column : this.getColumns()) {
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

        for (ConceptQueryFilter filter : filters)
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

        for (ConceptQueryFilter filter : filters)
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
     * categorías de los conceptos filtrados
     */
    public Long[] getHelperTableValues(){

        List<Long> helperTableValues = new ArrayList<>();

        for (ConceptQueryFilter filter : filters)
            helperTableValues.addAll(filter.getHelperTableValues());

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

        for (ConceptQueryFilter filter : filters)
            helperTableRecordValues.addAll(filter.getHelperTableRecordValues());

        if(helperTableRecordValues.isEmpty())
            return null;

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

        for (ConceptQueryFilter filter : filters)
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

        for (ConceptQueryFilter filter : filters)
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
            return getUser().getIdUser();
    }

    /**
     * Este método es responsable de recuperar los parámetros de los filtros del objeto de consulta del navegador de
     * categorías
     *
     * @return Una lista de <code>cl.minsal.semantikos.model.browser.QueryParameter</code> correspondiente a los
     * parámetros de los filtros
     */
    public List<QueryParameter> getQueryParameters(){

        List<ConceptQueryParameter> conceptQueryParameters = new ArrayList<>();

        conceptQueryParameters.add(new ConceptQueryParameter(Long.class, getCategoryValues(), true)); /** ids categorias **/
        conceptQueryParameters.add(new ConceptQueryParameter(String.class, getQuery(), false)); /** patrón de búsqueda **/
        conceptQueryParameters.add(new ConceptQueryParameter(Boolean.class, getModeled(), false)); /** está modelado? **/
        conceptQueryParameters.add(new ConceptQueryParameter(Boolean.class, getToBeReviewed(), false)); /** para revisar? **/
        conceptQueryParameters.add(new ConceptQueryParameter(Boolean.class, getToBeConsulted(), false)); /** para consultar? **/
        conceptQueryParameters.add(new ConceptQueryParameter(Long.class, getTagValues(), true)); /** etiquetas **/
        conceptQueryParameters.add(new ConceptQueryParameter(Long.class, getConceptCategoryValues(), true));
        conceptQueryParameters.add(new ConceptQueryParameter(Long.class, getConceptValues(), true));
        conceptQueryParameters.add(new ConceptQueryParameter(Long.class, getBasicTypeDefinitionValues(), true)); /** ids basicTypeDefinitionValues **/
        conceptQueryParameters.add(new ConceptQueryParameter(String.class, getBasicTypeValues(), true)); /** ids basicTypeValues **/
        conceptQueryParameters.add(new ConceptQueryParameter(Long.class, getHelperTableValues(), true)); /** ids helperTableValues **/
        conceptQueryParameters.add(new ConceptQueryParameter(Long.class, getHelperTableRecordValues(), true)); /** ids helperTableRecordValues **/
        conceptQueryParameters.add(new ConceptQueryParameter(Timestamp.class, getCreationDateSince(), false));
        conceptQueryParameters.add(new ConceptQueryParameter(Timestamp.class, getCreationDateTo(), false));
        conceptQueryParameters.add(new ConceptQueryParameter(Long.class, getUserValue(), false));
        conceptQueryParameters.add(new ConceptQueryParameter(Integer.class, getOrder(), false));
        conceptQueryParameters.add(new ConceptQueryParameter(String.class, getAsc(), false));
        conceptQueryParameters.add(new ConceptQueryParameter(Integer.class, getPageNumber(), false));
        conceptQueryParameters.add(new ConceptQueryParameter(Integer.class, getPageSize(), false));
        //conceptQueryParameters.add(new ConceptQueryParameter(String.class, getOrder(), false));

        return conceptQueryParameters;
    }
}
