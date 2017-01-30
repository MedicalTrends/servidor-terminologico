package cl.minsal.semantikos.model.browser;

import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by BluePrints Developer on 21-09-2016.
 */
public class DescriptionQuery implements IQuery {

    /**
     * Static filters
     */
    private List<Category> categories = new ArrayList<>();

    private String query;

    private List<RefSet> refSets = new ArrayList<>();

    private List<DescriptionType> descriptionTypes = new ArrayList<>();

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

    public List<RefSet> getRefSets() {
        return refSets;
    }

    public void setRefSets(List<RefSet> refSets) {
        if(refSets == null)
            this.refSets.clear();
        else
            this.refSets = refSets;
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
        if(categories == null)
            this.categories.clear();
        else
            this.categories = categories;
    }

    public List<DescriptionType> getDescriptionTypes() {
        return descriptionTypes;
    }

    public void setDescriptionTypes(List<DescriptionType> descriptionTypes) {
        this.descriptionTypes = descriptionTypes;
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

    public Long[] getRefSetValues(){

        List<Long> refSetValues = new ArrayList<>();

        for (RefSet refSet : refSets)
            refSetValues.add(refSet.getId());

        if(refSetValues.isEmpty())
            return null;

        else {
            Long[] array = new Long[refSetValues.size()];
            return refSetValues.toArray(array);
        }
    }

    public Long[] getDescriptionTypeValues(){

        List<Long> descriptionTypeValues = new ArrayList<>();

        for (DescriptionType descriptionType : descriptionTypes)
            descriptionTypeValues.add(descriptionType.getId());

        if(descriptionTypeValues.isEmpty())
            return null;

        else {
            Long[] array = new Long[descriptionTypeValues.size()];
            return descriptionTypeValues.toArray(array);
        }
    }

    public boolean isFiltered(){
        return ( getCategoryValues() != null || getRefSetValues() != null || getDescriptionTypeValues() != null  ||
                (getQuery() != null && !getQuery().isEmpty()) );
    }

    public List<QueryParameter> getQueryParameters(){

        List<QueryParameter> queryParameters = new ArrayList<>();

        queryParameters.add(new QueryParameter(Long.class, getCategoryValues(), true)); /** ids categorias **/
        queryParameters.add(new QueryParameter(String.class, getQuery(), false)); /** patrón de búsqueda **/
        queryParameters.add(new QueryParameter(Long.class, getRefSetValues(), true)); /** refsets **/
        queryParameters.add(new QueryParameter(Long.class, getDescriptionTypeValues(), true)); /** description types **/
        queryParameters.add(new QueryParameter(Integer.class, getOrder(), false));
        queryParameters.add(new QueryParameter(String.class, getAsc(), false));
        queryParameters.add(new QueryParameter(Integer.class, getPageNumber(), false));
        queryParameters.add(new QueryParameter(Integer.class, getPageSize(), false));

        return queryParameters;
    }
}
