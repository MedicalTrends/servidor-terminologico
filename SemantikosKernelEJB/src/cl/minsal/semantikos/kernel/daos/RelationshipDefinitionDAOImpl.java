package cl.minsal.semantikos.kernel.daos;


import cl.minsal.semantikos.kernel.factories.RelationshipAttributeDefinitionFactory;
import cl.minsal.semantikos.kernel.factories.RelationshipDefinitionFactory;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.relationships.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * @author Andrés Farías
 */

@Stateless
public class RelationshipDefinitionDAOImpl implements RelationshipDefinitionDAO {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(RelationshipDefinitionDAOImpl.class);

    @EJB
    CategoryDAO categoryDAO;

    @EJB
    DescriptionDAO descriptionDAO;

    @EJB
    private TargetTypeDAO targetTypeDAO;

    @EJB
    private RelationshipDefinitionFactory relationshipDefinitionFactory;

    @EJB
    private RelationshipAttributeDefinitionFactory factory;

    @EJB
    private HelperTableDAO helperTableDAO;

    @Override
    public List<RelationshipDefinition> getRelationshipDefinitionsByCategory(long idCategory) {

        List<RelationshipDefinition> relationshipDefinitions;

        ConnectionBD connect = new ConnectionBD();
        String sqlQuery = "{call semantikos.get_relationship_definitions_by_category(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sqlQuery)) {

            /* Se invoca la consulta para recuperar las relaciones */
            call.setLong(1, idCategory);
            call.execute();

            /* Cada Fila del ResultSet trae una relación */
            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                if (rs.getString(1) != null) {
                    relationshipDefinitions = relationshipDefinitionFactory.createRelDefinitionsFromJSON(rs.getString(1));
                } else {
                    return emptyList();
                }

            } else {
                String errorMsg = "Un error imposible acaba de ocurrir";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();

            /* Para cada relationshipDefinition se deben recuperar sus atributos */
            for (RelationshipDefinition relationshipDefinition : relationshipDefinitions) {
                relationshipDefinition.setRelationshipAttributeDefinitions(getRelationshipAttributeDefinitionsByRelationshipDefinition(relationshipDefinition));
            }
        } catch (SQLException e) {
            String errorMsg = "Erro al invocar get_relationship_definitions_by_category(" + idCategory + ")";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return relationshipDefinitions;

    }

    /**
     * Este método es responsable de recuperar la definición de los atributos
     *
     * @return Una lista de Definición de Atributos.
     */
    public List<RelationshipAttributeDefinition> getRelationshipAttributeDefinitionsByRelationshipDefinition(RelationshipDefinition relationshipDefinition) {

        ConnectionBD connect = new ConnectionBD();
        String sqlQuery = "{call semantikos.get_relationship_attribute_definitions_json_by_id(?)}";
        List<RelationshipAttributeDefinition> relationshipAttributeDefinitions;
        long id = relationshipDefinition.getId();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sqlQuery)) {

            /* Se invoca la consulta para recuperar los atributos de esta relación */
            call.setLong(1, id);
            call.execute();

            ResultSet resultSet = call.getResultSet();
            resultSet.next();
            String jsonExpression = resultSet.getString(1);
            resultSet.close();

            if (jsonExpression == null) {
                return emptyList();
            } else {
                relationshipAttributeDefinitions = factory.createFromJSON(jsonExpression);
            }
        } catch (SQLException e) {
            String errorMsg = "Erro al invocar get_relationship_definition_by_id(" + id + ")";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return relationshipAttributeDefinitions;
    }

    @Override
    public RelationshipDefinition getRelationshipDefinitionByID(long idRelationshipDefinition) {

        ConnectionBD connect = new ConnectionBD();
        String sqlQuery = "{call semantikos.get_relationship_definition_by_id(?)}";

        RelationshipDefinition relationshipDefinition;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sqlQuery)) {

            /* Se invoca la consulta para recuperar las relaciones */
            call.setLong(1, idRelationshipDefinition);
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                String jsonResult = rs.getString(1);
                relationshipDefinition = relationshipDefinitionFactory.createFromJSON(jsonResult);
            } else {
                String errorMsg = "No se obtuvo ningún resultado de invocar get_relationship_definition_by_id(" + idRelationshipDefinition + ")";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Erro al invocar get_relationship_definition_by_id(" + idRelationshipDefinition + ")";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return relationshipDefinition;
    }

    /**
     * Este método es responsable de retornar una instancia del target Definition adecuado.
     *
     * @param idCategory      Identificador de la categoría destino.
     * @param idHelperTable Identificador de la Tabla auxiliar.
     * @param idBasicType     Identificador del tipo básico.
     * @param isSCTType       Indicador booleano (<code>"true"</code> o "<code>false</code>").
     *
     * @return Una instancia del Target Definition concreto.
     *
     * @throws IllegalArgumentException Arrojado si todos los parámetros son nulos.
     */
    protected TargetDefinition getTargetDefinition(String idCategory, Long idHelperTable, String idExternTable, String idBasicType, String isSCTType) throws IllegalArgumentException {

        /* Se testea si es un tipo básico */

        if (idBasicType != null) {
            long id = new BigInteger(idBasicType).longValue();
            return targetTypeDAO.findByID(id);
        }

        if (idCategory != null) {
            long id = new BigInteger(idCategory).longValue();
            return categoryDAO.getCategoryById(id);
        }

        if (idHelperTable != null) {
            return helperTableDAO.getHelperTableByID(idHelperTable);
        }

        throw new IllegalArgumentException("Todos los parámetros eran nulos.");
    }

    @Override
    public RelationshipAttributeDefinition getRelationshipAttributeDefinitionBy(long id) {
        ConnectionBD connect = new ConnectionBD();
        String sqlQuery = "{call semantikos.get_relationship_attribute_definition_by_id(?)}";
        RelationshipAttributeDefinition relationshipAttributeDefinition;

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sqlQuery)) {

            /* Se invoca la consulta para recuperar el atributo */
            call.setLong(1, id);
            call.execute();

            ResultSet resultSet = call.getResultSet();
            resultSet.next();
            String jsonExpression = resultSet.getString(1);
            resultSet.close();
            relationshipAttributeDefinition = factory.createFromRelationshipAttributeDefinitionJSON(jsonExpression);

        } catch (SQLException e) {
            String errorMsg = "Erro al invocar get_relationship_attribute_definition_by_id(" + id + ")";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return relationshipAttributeDefinition;
    }
}
