package cl.minsal.semantikos.view.daos;

import cl.minsal.semantikos.kernel.daos.TargetDAO;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.ConceptSMTKWeb;
import cl.minsal.semantikos.model.DescriptionWeb;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.Target;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
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

    @Override
    public ExtendedRelationshipDefinitionInfo getCompositeOf(Category category, RelationshipDefinition relationshipDefinition) {

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.get_view_info_by_relationship_definition(?,?)}";
        long idComposite;
        int order;
        long idTarget;
        Target defaultValue = null;

        try (Connection connection = connect.getConnection();

            CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, category.getId());
            call.setLong(2, relationshipDefinition.getId());
            call.execute();
            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                order = rs.getInt(1);
                idComposite = rs.getLong(2);
                idTarget = rs.getLong(3);
                if(idTarget!=0)
                    defaultValue = targetDAO.getDefaultTargetByID(rs.getLong(3));
            } else {
                return ExtendedRelationshipDefinitionInfo.DEFAULT_CONFIGURATION;
            }
        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return new ExtendedRelationshipDefinitionInfo(idComposite, order, defaultValue);
    }

    @Override
    public ConceptSMTKWeb augmentConcept(Category category, ConceptSMTKWeb concept) {

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.get_view_info_by_category(?)}";
        boolean caseSensitive = false;

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, category.getId());
            call.execute();
            ResultSet rs = call.getResultSet();
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
