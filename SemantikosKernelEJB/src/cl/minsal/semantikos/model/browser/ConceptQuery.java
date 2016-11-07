package cl.minsal.semantikos.model.browser;

import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.Tag;
import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;
import cl.minsal.semantikos.model.relationships.Target;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by BluePrints Developer on 21-09-2016.
 */
public class ConceptQuery {

    /**
     * Filtros estáticos
     */
    private List<Category> categories;

    private String query;

    private Boolean toBeReviewed;
    private Boolean toBeConsulted;

    private Boolean modeled;

    private Tag tag;

    private boolean customFilterable;

    /**
     * Custom filters
     */
    private Date creationDateSince;
    private Date creationDateTo;
    private User user;

    /**
     * Dynamic filters
     */
    private List<ConceptQueryFilter> filters = new ArrayList<>();

    /**
     * Dynamic columns
     */
    private List<ConceptQueryColumn> columns = new ArrayList<>();

    /**
     * Order
     */
    private int order;
    private String asc;

    /**
     * Pagination
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

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
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

    public Long getUserValue(){
        if(getUser()==null)
            return null;
        else
            return getUser().getIdUser();
    }

    public List<ConceptQueryParameter> getConceptQueryParameters(){

        List<ConceptQueryParameter> conceptQueryParameters = new ArrayList<>();

        conceptQueryParameters.add(new ConceptQueryParameter(Long.class, getCategoryValues(), true));
        conceptQueryParameters.add(new ConceptQueryParameter(String.class, getQuery(), false));
        conceptQueryParameters.add(new ConceptQueryParameter(Boolean.class, getModeled(), false));
        conceptQueryParameters.add(new ConceptQueryParameter(Boolean.class, getToBeReviewed(), false));
        conceptQueryParameters.add(new ConceptQueryParameter(Boolean.class, getToBeConsulted(), false));
        conceptQueryParameters.add(new ConceptQueryParameter(Tag.class, getTag(), false));
        conceptQueryParameters.add(new ConceptQueryParameter(String.class, getBasicTypeValues(), true));
        conceptQueryParameters.add(new ConceptQueryParameter(Long.class, getHelperTableValues(), true));
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
