package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.model.relationships.*;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    private HelperTableDAO helperTableDAO;

    @EJB
    private TargetDefinitionDAO targetDefinitionDAO;

    @EJB
    private RelationshipDefinitionDAO relationshipDefinitionDAO;

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public List<RelationshipDefinition> getRelationshipDefinitionsByCategory(long idCategory) {

        List<RelationshipDefinition> relationshipDefinitions = new ArrayList<>();

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship_definition.get_relationship_definitions_by_category(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar las relaciones */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, idCategory);
            call.execute();

            /* Cada Fila del ResultSet trae una relación */
            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                relationshipDefinitions.add(createRelationshipDefinitionFromResultSet(rs));
            }

            rs.close();

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

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship_definition.get_relationship_attribute_definitions_by_id(?); end;";

        List<RelationshipAttributeDefinition> relationshipAttributeDefinitions = new ArrayList<>();

        long id = relationshipDefinition.getId();
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar los atributos de esta relación */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, id);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                relationshipAttributeDefinitions.add(createRelationshipAttributeDefinitionFromResultSet(rs));
            }

        } catch (SQLException e) {
            String errorMsg = "Erro al invocar get_relationship_definition_by_id(" + id + ")";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return relationshipAttributeDefinitions;
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

    public RelationshipDefinition createRelationshipDefinitionFromResultSet(ResultSet rs) {

        try {

            long id = rs.getLong("id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            TargetDefinition targetDefinition = targetDefinitionDAO.getTargetDefinitionById(rs.getLong("id_target_definition"));
            Multiplicity multiplicity = new Multiplicity(rs.getInt("lower_boundary"), rs.getInt("upper_boundary"));

            RelationshipDefinition relationshipDefinition = new RelationshipDefinition(id, name, description, targetDefinition, multiplicity);

            relationshipDefinition.setRelationshipAttributeDefinitions(relationshipDefinitionDAO.getRelationshipAttributeDefinitionsByRelationshipDefinition(relationshipDefinition));

            return relationshipDefinition;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public RelationshipAttributeDefinition createRelationshipAttributeDefinitionFromResultSet(ResultSet rs) {

        try {

            long id = rs.getLong("id");
            String name = rs.getString("name");
            TargetDefinition targetDefinition = targetDefinitionDAO.getTargetDefinitionById(rs.getLong("id_target_definition"));
            Multiplicity multiplicity = new Multiplicity(rs.getInt("lower_boundary"), rs.getInt("upper_boundary"));

            return new RelationshipAttributeDefinition(id, targetDefinition, name, multiplicity);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


}
