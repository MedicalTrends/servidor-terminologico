package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.DescriptionManager;
import cl.minsal.semantikos.kernel.components.PendingTermsManager;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.browser.*;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BluePrints Developer on 22-09-2016.
 */
@Stateless
public class QueryDAOImpl implements QueryDAO {

    private static final Logger logger = LoggerFactory.getLogger(QueryDAOImpl.class);

    @EJB
    ConceptManager conceptManager;

    @EJB
    DescriptionManager descriptionManager;

    @EJB
    PendingTermsManager pendingTermsManager;

    @Override
    public List<ConceptSMTK> executeQuery(GeneralQuery query) {

        List<ConceptSMTK> concepts = new ArrayList<ConceptSMTK>();

        ConnectionBD connect = new ConnectionBD();

        //TODO: hacer funcion en pg
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_concept_by_general_query(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}" )){

            /*
                1. p_id_category integer, --static
                2. p_pattern text, --static
                3. p_modeled boolean, --static
                4. p_review boolean, --static
                5. p_consult boolean, --static
                6. p_tag_id integer, --static
                7. p_basic_type_values text[], --dynamic
                8. p_helper_table_records integer[], --dynamic
                9. p_creation_date_from date, --dynamic
                10. p_creation_date_to date, --dynamic
                11. p_orden text, --static
                12. p_page integer, --static
                13. p_page_size integer --static
            */

            //bindParameter();

            int paramNumber = 1;

            for (QueryParameter queryParameter : query.getQueryParameters()) {
                bindParameter(paramNumber, call, connect.getConnection(), queryParameter);
                paramNumber++;
            }

            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {

                ConceptSMTK recoveredConcept = conceptManager.getConceptByID( rs.getLong(1));
                concepts.add(recoveredConcept);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return concepts;
    }

    @Override
    public List<Description> executeQuery(DescriptionQuery query) {

        List<Description> descriptions = new ArrayList<Description>();

        ConnectionBD connect = new ConnectionBD();

        //TODO: hacer funcion en pg
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_description_by_description_query(?,?,?,?,?,?,?,?)}" )){

            /*
                1. p_id_category integer, --static
                2. p_pattern text, --static
                3. p_refset integer, --static
                4. p_refset integer, --static
                11. p_orden text, --static
                12. p_page integer, --static
                13. p_page_size integer --static
            */

            //bindParameter();

            int paramNumber = 1;

            for (QueryParameter queryParameter : query.getQueryParameters()) {
                bindParameter(paramNumber, call, connect.getConnection(), queryParameter);
                paramNumber++;
            }

            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {

                Description recoveredDescription =  descriptionManager.getDescriptionByID(rs.getLong(1));
                descriptions.add(recoveredDescription);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return descriptions;
    }

    @Override
    public List<NoValidDescription> executeQuery(NoValidQuery query) {

        List<NoValidDescription> noValidDescriptions = new ArrayList<NoValidDescription>();

        ConnectionBD connect = new ConnectionBD();

        //TODO: hacer funcion en pg
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_description_by_no_valid_query(?,?,?,?,?,?,?)}" )){

            /*
                1. p_id_category integer, --static
                2. p_pattern text, --static
                3. p_refset integer, --static
                4. p_refset integer, --static
                11. p_orden text, --static
                12. p_page integer, --static
                13. p_page_size integer --static
            */

            //bindParameter();

            int paramNumber = 1;

            for (QueryParameter queryParameter : query.getQueryParameters()) {
                bindParameter(paramNumber, call, connect.getConnection(), queryParameter);
                paramNumber++;
            }

            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {

                NoValidDescription noValidDescription =  descriptionManager.getNoValidDescriptionByID(rs.getLong(1));
                noValidDescriptions.add(noValidDescription);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return noValidDescriptions;
    }

    @Override
    public List<PendingTerm> executeQuery(PendingQuery query) {

        List<PendingTerm> pendingTerms = new ArrayList<PendingTerm>();

        ConnectionBD connect = new ConnectionBD();

        //TODO: hacer funcion en pg
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_pending_term_by_pending_query(?,?,?,?,?,?)}" )){

            /*
                1. p_id_category integer, --static
                2. p_pattern text, --static
                11. p_orden text, --static
                12. p_page integer, --static
                13. p_page_size integer --static
            */

            //bindParameter();

            int paramNumber = 1;

            for (QueryParameter queryParameter : query.getQueryParameters()) {
                bindParameter(paramNumber, call, connect.getConnection(), queryParameter);
                paramNumber++;
            }

            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {

                PendingTerm pendingTerm =  pendingTermsManager.getPendingTermById(rs.getLong(1));
                pendingTerms.add(pendingTerm);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pendingTerms;
    }

    @Override
    public List<ConceptSMTK> executeQuery(BrowserQuery query) {
        List<ConceptSMTK> concepts = new ArrayList<ConceptSMTK>();

        ConnectionBD connect = new ConnectionBD();

        //TODO: hacer funcion en pg
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_concept_by_browser_query(?,?,?,?,?,?,?)}" )){

            /*
                1. p_id_category integer, --static
                2. p_pattern text, --static
                3. p_modeled boolean, --static
                4. p_review boolean, --static
                5. p_consult boolean, --static
                6. p_tag_id integer, --static
                7. p_basic_type_values text[], --dynamic
                8. p_helper_table_records integer[], --dynamic
                9. p_creation_date_from date, --dynamic
                10. p_creation_date_to date, --dynamic
                11. p_orden text, --static
                12. p_page integer, --static
                13. p_page_size integer --static
            */

            //bindParameter();

            int paramNumber = 1;

            for (QueryParameter queryParameter : query.getQueryParameters()) {
                bindParameter(paramNumber, call, connect.getConnection(), queryParameter);
                paramNumber++;
            }

            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {

                ConceptSMTK recoveredConcept = conceptManager.getConceptByID( rs.getLong(1));
                concepts.add(recoveredConcept);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return concepts;
    }


    @Override
    public long countByQuery(GeneralQuery query) {

        long conceptsNumber = 0;

        ConnectionBD connect = new ConnectionBD();

        //TODO: hacer funcion en pg
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.count_concept_by_general_query(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}" )){

            int paramNumber = 1;

            for (QueryParameter queryParameter : query.getQueryParameters()) {
                bindParameter(paramNumber, call, connect.getConnection(), queryParameter);
                paramNumber++;
            }

            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {

                conceptsNumber = rs.getLong(1);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conceptsNumber;
    }

    @Override
    public long countByQuery(DescriptionQuery query) {
        long descriptionsNumber = 0;

        ConnectionBD connect = new ConnectionBD();

        //TODO: hacer funcion en pg
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.count_description_by_description_query(?,?,?,?,?,?,?,?)}" )){

            int paramNumber = 1;

            for (QueryParameter queryParameter : query.getQueryParameters()) {
                bindParameter(paramNumber, call, connect.getConnection(), queryParameter);
                paramNumber++;
            }

            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {

                descriptionsNumber = rs.getLong(1);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return descriptionsNumber;
    }

    @Override
    public long countByQuery(NoValidQuery query) {

        long descriptionsNumber = 0;

        ConnectionBD connect = new ConnectionBD();

        //TODO: hacer funcion en pg
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.count_description_by_no_valid_query(?,?,?,?,?,?,?,?)}" )){

            int paramNumber = 1;

            for (QueryParameter queryParameter : query.getQueryParameters()) {
                bindParameter(paramNumber, call, connect.getConnection(), queryParameter);
                paramNumber++;
            }

            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {

                descriptionsNumber = rs.getLong(1);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return descriptionsNumber;
    }

    @Override
    public long countByQuery(PendingQuery query) {

        long pendingTermNumber = 0;

        ConnectionBD connect = new ConnectionBD();

        //TODO: hacer funcion en pg
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.count_pending_term_by_no_pending_query(?,?,?,?,?,?)}" )){

            int paramNumber = 1;

            for (QueryParameter queryParameter : query.getQueryParameters()) {
                bindParameter(paramNumber, call, connect.getConnection(), queryParameter);
                paramNumber++;
            }

            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {

                pendingTermNumber = rs.getLong(1);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pendingTermNumber;
    }

    @Override
    public long countByQuery(BrowserQuery query) {
        long pendingTermNumber = 0;

        ConnectionBD connect = new ConnectionBD();

        //TODO: hacer funcion en pg
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.count_concept_by_browser_query(?,?,?,?,?,?,?)}" )){

            int paramNumber = 1;

            for (QueryParameter queryParameter : query.getQueryParameters()) {
                bindParameter(paramNumber, call, connect.getConnection(), queryParameter);
                paramNumber++;
            }

            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {

                pendingTermNumber = rs.getLong(1);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pendingTermNumber;
    }

    @Override
    public List<RelationshipDefinition> getSearchableAttributesByCategory(Category category) {

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.get_view_info_by_relationship_definition(?,?)}";

        List<RelationshipDefinition> someRelationshipDefinitions = new ArrayList<>();

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions()) {

                boolean searchable;

                call.setLong(1, category.getId());
                call.setLong(2, relationshipDefinition.getId());
                call.execute();

                ResultSet rs = call.getResultSet();

                if (rs.next()) {

                    searchable = rs.getBoolean("searchable_by_browser");

                    if(searchable)
                        someRelationshipDefinitions.add(relationshipDefinition);
                }
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar información adicional sobre esta definición desde la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }
        return someRelationshipDefinitions;
    }

    @Override
    public List<RelationshipDefinition> getSecondOrderSearchableAttributesByCategory(Category category) {
        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.get_second_order_view_info_by_relationship_definition(?,?)}";

        List<RelationshipDefinition> someRelationshipDefinitions = new ArrayList<>();

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions()) {

                boolean searchable;

                call.setLong(1, category.getId());
                call.setLong(2, relationshipDefinition.getId());
                call.execute();

                ResultSet rs = call.getResultSet();

                if (rs.next()) {

                    searchable = rs.getBoolean("second_order_searchable_by_browser");

                    if(searchable)
                        someRelationshipDefinitions.add(relationshipDefinition);
                }
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar información adicional sobre esta definición desde la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }
        return someRelationshipDefinitions;
    }

    @Override
    public List<RelationshipDefinition> getShowableAttributesByCategory(Category category) {
        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.get_view_info_by_relationship_definition(?,?)}";

        List<RelationshipDefinition> someRelationshipDefinitions = new ArrayList<>();

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions()) {

                boolean showable;

                call.setLong(1, category.getId());
                call.setLong(2, relationshipDefinition.getId());
                call.execute();

                ResultSet rs = call.getResultSet();

                if (rs.next()) {

                    showable = rs.getBoolean("showable_by_browser");

                    if(showable)
                        someRelationshipDefinitions.add(relationshipDefinition);
                }
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar información adicional sobre esta definición desde la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }
        return someRelationshipDefinitions;
    }

    @Override
    public List<RelationshipDefinition> getSecondOrderShowableAttributesByCategory(Category category) {
        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.get_second_order_view_info_by_relationship_definition(?,?)}";

        List<RelationshipDefinition> someRelationshipDefinitions = new ArrayList<>();

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions()) {

                boolean showable;

                call.setLong(1, category.getId());
                call.setLong(2, relationshipDefinition.getId());
                call.execute();

                ResultSet rs = call.getResultSet();

                if (rs.next()) {

                    showable = rs.getBoolean("second_order_showable_by_browser");

                    if(showable)
                        someRelationshipDefinitions.add(relationshipDefinition);
                }
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar información adicional sobre esta definición desde la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }
        return someRelationshipDefinitions;
    }

    @Override
    public boolean getCustomFilteringValue(Category category) {

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.get_view_info_by_category(?)}";

        boolean customFilteringValue = false;

        try (Connection connection = connect.getConnection();

            CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, category.getId());
            call.execute();

            ResultSet rs = call.getResultSet();

            if (rs.next()) {

                customFilteringValue = rs.getBoolean("custom_filterable");

            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar información adicional sobre esta categoría desde la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return customFilteringValue;
    }

    @Override
    public boolean getShowableRelatedConceptsValue(Category category) {

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.get_view_info_by_category(?)}";

        boolean showableRelatedConcepts = false;

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, category.getId());
            call.execute();

            ResultSet rs = call.getResultSet();

            if (rs.next()) {

                showableRelatedConcepts = rs.getBoolean("showable_related_concepts_by_browser");

            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar información adicional sobre esta categoría desde la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return showableRelatedConcepts;
    }

    @Override
    public boolean getShowableValue(Category category) {

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.get_view_info_by_category(?)}";

        boolean showable = false;

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, category.getId());
            call.execute();

            ResultSet rs = call.getResultSet();

            if (rs.next()) {

                showable = rs.getBoolean("showable_by_browser");

            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar información adicional sobre esta categoría desde la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return showable;
    }


    @Override
    public boolean getMultipleFilteringValue(Category category, RelationshipDefinition relationshipDefinition) {

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.get_view_info_by_relationship_definition(?,?)}";

        boolean multipleFilteringValue = false;

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, category.getId());
            call.setLong(2, relationshipDefinition.getId());
            call.execute();

            ResultSet rs = call.getResultSet();

            if (rs.next()) {

                multipleFilteringValue = rs.getBoolean("multiple_filterable");

            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar información adicional sobre esta categoría desde la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return multipleFilteringValue;
    }

    private void bindParameter(int paramNumber, CallableStatement call, Connection connection, QueryParameter param)
            throws SQLException {


        if(param.getValue() == null){

            if(param.isArray()){
                call.setNull(paramNumber, Types.ARRAY);
                return;
            }
            else{
                if(param.getType() == String.class) {
                    call.setNull(paramNumber, Types.VARCHAR);
                    return;
                }

                if(param.getType() == Long.class) {
                    call.setNull(paramNumber, Types.BIGINT);
                    return;
                }

                if(param.getType() == Tag.class) {
                    call.setNull(paramNumber, Types.BIGINT);
                    return;
                }

                if(param.getType() == Boolean.class) {
                    call.setNull(paramNumber, Types.BOOLEAN);
                    return;
                }

                if(param.getType() == Timestamp.class) {
                    call.setNull(paramNumber, Types.TIMESTAMP);
                    return;
                }
            }
        }
        else{
            if(param.isArray()){
                if(param.getType() == String.class) {
                    call.setArray(paramNumber, connection.createArrayOf("text", (String[]) param.getValue()));
                    return;
                }
                if(param.getType() == Long.class) {
                    call.setArray(paramNumber, connection.createArrayOf("bigint", (Long[]) param.getValue()));
                    return;
                }
            }
            else{
                if(param.getType() == String.class) {
                    call.setString(paramNumber, param.getValue().toString());
                    return;
                }
                if(param.getType() == Long.class) {
                    call.setLong(paramNumber, (Long) param.getValue());
                    return;
                }

                if(param.getType() == Tag.class) {
                    Tag tag = (Tag) param.getValue();
                    call.setLong(paramNumber, tag.getId());
                    return;
                }

                if(param.getType() == Boolean.class) {
                    call.setBoolean(paramNumber, (Boolean) param.getValue());
                    return;
                }

                if(param.getType() == Timestamp.class) {
                    java.util.Date date = (java.util.Date)param.getValue();
                    call.setTimestamp(paramNumber, new Timestamp(date.getTime()));
                    return;
                }

                if(param.getType() == Integer.class) {
                    call.setInt(paramNumber, (Integer) param.getValue());
                    return;
                }
            }
        }
    }
}
