package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.DescriptionManager;
import cl.minsal.semantikos.kernel.components.PendingTermsManager;
import cl.minsal.semantikos.kernel.daos.mappers.RelationshipMapper;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.queries.*;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.NoValidDescription;
import cl.minsal.semantikos.model.descriptions.PendingTerm;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.tags.Tag;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
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

    @EJB
    private RelationshipMapper relationshipMapper;

    public List<Object> executeQuery(IQuery query) {

        List<Object> queryResult = new ArrayList<Object>();

        ConnectionBD connect = new ConnectionBD();

        String QUERY = "";

        if(  query instanceof  GeneralQuery )
            QUERY = "begin ? := stk.stk_pck_query.get_concept_by_general_query(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
        if(  query instanceof  DescriptionQuery )
            QUERY = "begin ? := stk.stk_pck_query.get_description_by_description_query(?,?,?,?,?,?,?,?,?); end;";
        if(  query instanceof  NoValidQuery )
            QUERY = "begin ? := stk.stk_pck_query.get_description_by_no_valid_query(?,?,?,?,?,?,?,?); end;";
        if(  query instanceof  PendingQuery )
            QUERY = "begin ? := stk.stk_pck_query.get_pending_term_by_pending_query(?,?,?,?,?,?,?); end;";
        if(  query instanceof  BrowserQuery )
            QUERY = "begin ? := stk.stk_pck_query.get_concept_by_browser_query(?,?,?,?,?,?,?,?); end;";

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(QUERY)){

            call.registerOutParameter (1, OracleTypes.CURSOR);

            int paramNumber = 2;

            for (QueryParameter queryParameter : query.getQueryParameters()) {
                bindParameter(paramNumber, call, connect.getConnection(), queryParameter);
                paramNumber++;
            }

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {

                if(  query instanceof  GeneralQuery ) {
                    ConceptSMTK recoveredConcept = conceptManager.getConceptByID( rs.getLong(1));
                    queryResult.add(recoveredConcept);
                }
                if(  query instanceof  DescriptionQuery ) {
                    Description recoveredDescription =  descriptionManager.getDescriptionByID(rs.getLong(1));
                    queryResult.add(recoveredDescription);
                }
                if(  query instanceof  NoValidQuery ) {
                    NoValidDescription noValidDescription =  descriptionManager.getNoValidDescriptionByID(rs.getLong(1));
                    queryResult.add(noValidDescription);
                }
                if(  query instanceof  PendingQuery ) {
                    PendingTerm pendingTerm =  pendingTermsManager.getPendingTermById(rs.getLong(1));
                    queryResult.add(pendingTerm);
                }
                if(  query instanceof  BrowserQuery ) {
                    ConceptSMTK recoveredConcept = conceptManager.getConceptByID( rs.getLong(1));
                    queryResult.add(recoveredConcept);
                }

            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return queryResult;
    }


    public long countByQuery(IQuery query) {

        long resultCount = 0;

        ConnectionBD connect = new ConnectionBD();

        String QUERY = "";

        if(  query instanceof  GeneralQuery )
            QUERY = "begin ? := stk.stk_pck_query.count_concept_by_general_query(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
        if(  query instanceof  DescriptionQuery )
            QUERY = "begin ? := stk.stk_pck_query.count_description_by_description_query(?,?,?,?,?,?,?,?,?); end;";
        if(  query instanceof  NoValidQuery )
            QUERY = "begin ? := stk.stk_pck_query.count_description_by_no_valid_query(?,?,?,?,?,?,?,?); end;";
        if(  query instanceof  PendingQuery )
            QUERY = "begin ? := stk.stk_pck_query.count_pending_term_by_no_pending_query(?,?,?,?,?,?,?); end;";
        if(  query instanceof  BrowserQuery )
            QUERY = "begin ? := stk.stk_pck_query.count_concept_by_browser_query(?,?,?,?,?,?,?,?); end;";

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(QUERY)) {

            call.registerOutParameter (1, Types.NUMERIC);

            int paramNumber = 2;

            for (QueryParameter queryParameter : query.getQueryParameters()) {
                bindParameter(paramNumber, call, connect.getConnection(), queryParameter);
                paramNumber++;
            }

            call.execute();

            resultCount =  call.getLong(1);

            call.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultCount;
    }

    @Override
    public List<RelationshipDefinition> getSearchableAttributesByCategory(Category category) {

        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_query.get_view_info_by_relationship_definition(?,?); end;";

        List<RelationshipDefinition> someRelationshipDefinitions = new ArrayList<>();

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions()) {

                boolean searchable;

                call.registerOutParameter (1, OracleTypes.CURSOR);
                call.setLong(2, category.getId());
                call.setLong(3, relationshipDefinition.getId());
                call.execute();

                ResultSet rs = (ResultSet) call.getObject(1);

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

        String sql = "begin ? := stk.stk_pck_query.get_second_order_view_info_by_relationship_definition(?,?); end;";

        List<RelationshipDefinition> someRelationshipDefinitions = new ArrayList<>();

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions()) {

                boolean searchable;

                call.registerOutParameter (1, OracleTypes.CURSOR);
                call.setLong(2, category.getId());
                call.setLong(3, relationshipDefinition.getId());
                call.execute();

                ResultSet rs = (ResultSet) call.getObject(1);

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
    public List<RelationshipAttributeDefinition> getSecondDerivateSearchableAttributesByCategory(Category category) {
        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_query.get_view_info_by_relationship_attribute_definition(?,?); end;";

        List<RelationshipAttributeDefinition> someRelationshipAttributeDefinitions = new ArrayList<>();

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions()) {

                for (RelationshipAttributeDefinition relationshipAttributeDefinition : relationshipDefinition.getRelationshipAttributeDefinitions()) {

                    boolean searchable;

                    call.registerOutParameter (1, OracleTypes.CURSOR);
                    call.setLong(2, category.getId());
                    call.setLong(3, relationshipAttributeDefinition.getId());
                    call.execute();

                    ResultSet rs = (ResultSet) call.getObject(1);

                    if (rs.next()) {

                        searchable = rs.getBoolean("searchable_by_browser");

                        if(searchable)
                            someRelationshipAttributeDefinitions.add(relationshipAttributeDefinition);
                    }

                }

            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar información adicional sobre esta definición desde la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }
        return someRelationshipAttributeDefinitions;
    }

    @Override
    public List<RelationshipDefinition> getShowableAttributesByCategory(Category category) {
        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_query.get_view_info_by_relationship_definition(?,?); end;";

        List<RelationshipDefinition> someRelationshipDefinitions = new ArrayList<>();

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions()) {

                boolean showable;

                call.registerOutParameter (1, OracleTypes.CURSOR);
                call.setLong(2, category.getId());
                call.setLong(3, relationshipDefinition.getId());
                call.execute();

                ResultSet rs = (ResultSet) call.getObject(1);

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

        String sql = "begin ? := stk.stk_pck_query.get_second_order_view_info_by_relationship_definition(?,?); end;";

        List<RelationshipDefinition> someRelationshipDefinitions = new ArrayList<>();

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions()) {

                boolean showable;

                call.registerOutParameter (1, OracleTypes.CURSOR);
                call.setLong(2, category.getId());
                call.setLong(3, relationshipDefinition.getId());
                call.execute();

                ResultSet rs = (ResultSet) call.getObject(1);

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

        String sql = "begin ? := stk.stk_pck_query.get_view_info_by_category(?); end;";

        boolean customFilteringValue = false;

        try (Connection connection = connect.getConnection();

            CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, category.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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

        String sql = "begin ? := stk.stk_pck_query.get_view_info_by_category(?); end;";

        boolean showableRelatedConcepts = false;

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, category.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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

        String sql = "begin ? := stk.stk_pck_query.get_view_info_by_category(?); end;";

        boolean showable = false;

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, category.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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

        String sql = "begin ? := stk.stk_pck_query.get_view_info_by_relationship_definition(?,?); end;";

        boolean multipleFilteringValue = false;

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, category.getId());
            call.setLong(3, relationshipDefinition.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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

    @Override
    public int getCompositeValue(Category category, RelationshipDefinition relationshipDefinition) {

        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_query.get_view_info_by_relationship_definition(?,?); end;";

        int compositeValue = -1;

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, category.getId());
            call.setLong(3, relationshipDefinition.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                compositeValue = rs.getInt("id_composite");
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar información adicional sobre esta categoría desde la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return compositeValue;
    }

    @Override
    public List<Relationship> getRelationshipsByColumns(ConceptSMTK conceptSMTK, Query query) {

        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.get_relationships_by_source_concept_id(?); end;";

        List<Long> definitions = query.getDefinitionIds();

        List<Relationship> relationships = new ArrayList<>();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, conceptSMTK.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                if(definitions.contains(rs.getLong("id_relationship_definition"))) {
                    relationships.add(relationshipMapper.createRelationshipFromResultSet(rs, conceptSMTK));
                }
            }

            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationships;

    }

    @Override
    public List<Relationship> getRelationshipsBySecondOrderColumns(ConceptSMTK conceptSMTK, Query query) {

        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.get_relationships_by_source_concept_id(?); end;";

        List<Long> definitions = query.getSecondOrderDefinitionIds();

        List<Relationship> relationships = new ArrayList<>();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, conceptSMTK.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                if(definitions.contains(rs.getLong("id_relationship_definition"))) {
                    relationships.add(relationshipMapper.createRelationshipFromResultSet(rs, conceptSMTK));
                }
            }

            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationships;

    }

    private void bindParameter(int paramNumber, CallableStatement call, Connection connection, QueryParameter param)
            throws SQLException {

        if(param.getValue() == null){

            if(param.isArray()){
                //call.setNull(paramNumber, Types.ARRAY);
                if(param.getType() == String.class) {
                    call.setNull(paramNumber, Types.ARRAY, "STK.TEXT_ARRAY");
                    //call.setArray(paramNumber, connection.unwrap(OracleConnection.class).createARRAY("STK.TEXT_ARRAY",null));
                }
                if(param.getType() == Long.class) {
                    call.setNull(paramNumber, Types.ARRAY, "STK.NUMBER_ARRAY");
                    //call.setArray(paramNumber, connection.unwrap(OracleConnection.class).createARRAY("STK.NUMBER_ARRAY",null));
                }

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
                    //call.setNull(paramNumber, Types.BOOLEAN);
                    call.setNull(paramNumber, OracleTypes.NUMBER);
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
                    call.setArray(paramNumber, connection.unwrap(OracleConnection.class).createARRAY("STK.TEXT_ARRAY", (String[]) param.getValue()));
                    return;
                }
                if(param.getType() == Long.class) {
                    call.setArray(paramNumber, connection.unwrap(OracleConnection.class).createARRAY("STK.NUMBER_ARRAY", (Long[]) param.getValue()));
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
