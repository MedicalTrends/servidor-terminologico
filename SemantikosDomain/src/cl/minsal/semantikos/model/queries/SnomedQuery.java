package cl.minsal.semantikos.model.queries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BluePrints Developer on 21-09-2016.
 */
public class SnomedQuery extends Query implements IQuery, Serializable {

    public boolean isFiltered(){
        return ( (getQuery() != null && !getQuery().isEmpty()) );
    }

    public List<QueryParameter> getQueryParameters(){

        List<QueryParameter> queryParameters = new ArrayList<>();

        queryParameters.add(new QueryParameter(String.class, getQuery(), false)); /** patrón de búsqueda **/
        queryParameters.add(new QueryParameter(Boolean.class, getTruncateMatch(), false)); /** tipo de búsqueda **/
        queryParameters.add(new QueryParameter(Integer.class, getOrder(), false));
        queryParameters.add(new QueryParameter(String.class, getAsc(), false));
        queryParameters.add(new QueryParameter(Integer.class, getPageNumber(), false));
        queryParameters.add(new QueryParameter(Integer.class, getPageSize(), false));

        return queryParameters;
    }
}
