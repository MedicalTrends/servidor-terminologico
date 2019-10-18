package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.categories.Category;

import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.modelweb.ConceptSMTKWeb;
import cl.minsal.semantikos.modelweb.DescriptionWeb;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Andrés Farías on 10/5/16.
 */
@Stateless
public class SemantikosWebDAOImpl implements SemantikosWebDAO {

    private static final Logger logger = LoggerFactory.getLogger(SemantikosWebDAOImpl.class);

    @EJB
    private TargetDAO targetDAO;

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public ExtendedRelationshipDefinitionInfo getCompositeOf(Category category, RelationshipDefinition relationshipDefinition) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_query.get_view_info_by_relationship_definition(?,?); end;";

        long idComposite;
        int order;
        long idTarget;
        boolean isAutogenerate;
        Target defaultValue = null;

        try (Connection connection = dataSource.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, category.getId());
            call.setLong(3, relationshipDefinition.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                order = rs.getInt(1);
                idComposite = rs.getLong(2);
                idTarget = rs.getLong(3);
                isAutogenerate = rs.getBoolean(8);
                if(idTarget!=0)
                    defaultValue = targetDAO.getDefaultTargetByID(relationshipDefinition.getTargetDefinition(), idTarget);
            } else {
                return ExtendedRelationshipDefinitionInfo.DEFAULT_CONFIGURATION;
            }
        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return new ExtendedRelationshipDefinitionInfo(idComposite, order, isAutogenerate, defaultValue);
    }

    @Override
    public ExtendedRelationshipAttributeDefinitionInfo getCompositeOf(Category category, RelationshipAttributeDefinition relationshipAttributeDefinition) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_query.get_view_info_by_relationship_attribute_definition(?,?); end;";

        long idComposite;
        int order;
        long idTarget;
        Target defaultValue = null;

        try (Connection connection = dataSource.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, category.getId());
            call.setLong(3, relationshipAttributeDefinition.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                order = rs.getInt("order_view");
                idComposite = rs.getLong("id_composite");
                idTarget = rs.getLong("id_default_target");
                if(idTarget!=0)
                    defaultValue = targetDAO.getDefaultTargetByID(relationshipAttributeDefinition.getTargetDefinition(), idTarget);
            } else {
                return ExtendedRelationshipAttributeDefinitionInfo.DEFAULT_CONFIGURATION;
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar viewInfo de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return new ExtendedRelationshipAttributeDefinitionInfo(idComposite, order, defaultValue);
    }

    @Override
    public ConceptSMTKWeb augmentConcept(ConceptSMTKWeb concept) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_query.get_view_info_by_category(?); end;";

        boolean caseSensitive = false;

        try (Connection connection = dataSource.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, concept.getCategory().getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                caseSensitive = rs.getBoolean("default_case_sensitive");
            }
        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        for (DescriptionWeb descriptionWeb : concept.getDescriptionsWeb()) {
            descriptionWeb.setCaseSensitive(caseSensitive);
        }

        return concept;
    }

}
