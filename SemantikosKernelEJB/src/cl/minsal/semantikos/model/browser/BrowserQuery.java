package cl.minsal.semantikos.model.browser;

import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.DescriptionType;
import cl.minsal.semantikos.model.RefSet;
import cl.minsal.semantikos.model.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BluePrints Developer on 21-09-2016.
 */
public class BrowserQuery implements IQuery {

    /**
     * Static filters
     */
    private List<Category> categories = new ArrayList<>();

    private String query;

    private List<Tag> tags = new ArrayList<>();


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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        if(tags == null)
            this.tags.clear();
        else
            this.tags = tags;
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

    public boolean isFiltered(){
        return ( getCategoryValues() != null || getTagValues() != null  || (getQuery() != null && !getQuery().isEmpty()) );
    }

    public List<QueryParameter> getQueryParameters(){

        List<QueryParameter> queryParameters = new ArrayList<>();

        queryParameters.add(new QueryParameter(Long.class, getCategoryValues(), true)); /** ids categorias **/
        queryParameters.add(new QueryParameter(String.class, getQuery(), false)); /** patrón de búsqueda **/
        queryParameters.add(new QueryParameter(Long.class, getTagValues(), true)); /** refsets **/
        queryParameters.add(new QueryParameter(Integer.class, getOrder(), false));
        queryParameters.add(new QueryParameter(String.class, getAsc(), false));
        queryParameters.add(new QueryParameter(Integer.class, getPageNumber(), false));
        queryParameters.add(new QueryParameter(Integer.class, getPageSize(), false));

        return queryParameters;
    }
}
