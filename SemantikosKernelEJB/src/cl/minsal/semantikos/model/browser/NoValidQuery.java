package cl.minsal.semantikos.model.browser;

import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.DescriptionType;
import cl.minsal.semantikos.model.ObservationNoValid;
import cl.minsal.semantikos.model.RefSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BluePrints Developer on 21-09-2016.
 */
public class NoValidQuery extends Query implements IQuery {

    /**
     * Static filters
     */
    private List<DescriptionType> descriptionTypes = new ArrayList<>();

    private List<ObservationNoValid> observationNoValids = new ArrayList<>();

    public List<DescriptionType> getDescriptionTypes() {
        return descriptionTypes;
    }

    public void setDescriptionTypes(List<DescriptionType> descriptionTypes) {
        this.descriptionTypes = descriptionTypes;
    }

    public List<ObservationNoValid> getObservationNoValids() {
        return observationNoValids;
    }

    public void setObservationNoValids(List<ObservationNoValid> observationNoValids) {
        this.observationNoValids = observationNoValids;
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

    public Long[] getObservationNoValidValues(){

        List<Long> observationNoValidValues = new ArrayList<>();

        for (ObservationNoValid observationNoValid : observationNoValids)
            observationNoValidValues.add(observationNoValid.getId());

        if(observationNoValidValues.isEmpty())
            return null;

        else {
            Long[] array = new Long[observationNoValidValues.size()];
            return observationNoValidValues.toArray(array);
        }
    }

    public boolean isFiltered(){
        return ( getObservationNoValidValues() != null || getDescriptionTypeValues() != null  || (getQuery() != null && !getQuery().isEmpty()) );
    }

    public List<QueryParameter> getQueryParameters(){

        List<QueryParameter> queryParameters = new ArrayList<>();

        queryParameters.add(new QueryParameter(String.class, getQuery(), false)); /** patrón de búsqueda **/
        queryParameters.add(new QueryParameter(Boolean.class, getTruncateMatch(), false)); /** tipo de búsqueda **/
        queryParameters.add(new QueryParameter(Long.class, getDescriptionTypeValues(), true)); /** description types **/
        queryParameters.add(new QueryParameter(Long.class, getObservationNoValidValues(), true)); /** observation **/
        queryParameters.add(new QueryParameter(Integer.class, getOrder(), false));
        queryParameters.add(new QueryParameter(String.class, getAsc(), false));
        queryParameters.add(new QueryParameter(Integer.class, getPageNumber(), false));
        queryParameters.add(new QueryParameter(Integer.class, getPageSize(), false));

        return queryParameters;
    }
}
