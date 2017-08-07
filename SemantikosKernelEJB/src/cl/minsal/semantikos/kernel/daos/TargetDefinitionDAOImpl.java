package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.kernel.factories.TargetDefinitionFactory;
import cl.minsal.semantikos.model.relationships.TargetDefinition;

import cl.minsal.semantikos.model.snomedct.SnomedCT;
import oracle.jdbc.OracleTypes;
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
 * @author Andrés Farías
 */
@Stateless
public class TargetDefinitionDAOImpl implements TargetDefinitionDAO {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(TargetDefinitionDAOImpl.class);

    @EJB
    TargetDefinitionFactory targetDefinitionFactory;

    @EJB
    HelperTableDAO helperTableDAO;

    @EJB
    CrossmapsDAO crossmapsDAO;

    @EJB
    SnomedCTDAO snomedCTDAO;

    @EJB
    ConceptDAO conceptDAO;

    @EJB
    BasicTypeDefinitionDAO basicTypeDefinitionDAO;

    @EJB
    CategoryDAO categoryDAO;

    private static final int BASIC_TYPE_ID = 1;
    private static final int SMTK_TYPE_ID = 2;
    private static final int SCT_TYPE_ID = 3;
    private static final int HELPER_TABLE_TYPE_ID = 4;
    private static final int CROSSMAP_TYPE_ID = 5;

    @Override
    public TargetDefinition getTargetDefinitionById(long idTargetDefinition) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_target_definition.get_target_definition_by_id(?); end;";

        TargetDefinition targetDefinition = null;

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);

            /* Se invoca la consulta para recuperar las relaciones */
            call.setLong(2, idTargetDefinition);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                targetDefinition = createTargetDefinitionFromResultSet(rs);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al invocar get_target_definition_by_id(" + idTargetDefinition + ")";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return targetDefinition;
    }

    public TargetDefinition createTargetDefinitionFromResultSet(ResultSet rs) {

        TargetDefinition targetDefinition = null;

        try {
            switch ((int) rs.getLong("id_target_type")) {

                case BASIC_TYPE_ID:
                    return basicTypeDefinitionDAO.getBasicTypeDefinitionById(rs.getLong("id_basic_type"));

                case SMTK_TYPE_ID:
                    return categoryDAO.getCategoryById(rs.getLong("id_category"));

                case SCT_TYPE_ID:
                    return new SnomedCT("1.0");

                case HELPER_TABLE_TYPE_ID:
                    return helperTableDAO.getHelperTableByID(rs.getLong("id_helper_table_name"));

                case CROSSMAP_TYPE_ID:
                    return crossmapsDAO.getCrossmapSetByID(rs.getLong("id_extern_table_name"));

                default:
                    throw new EJBException("TIPO DE DEFINICION INCORRECTO. ID Target Type=" + rs.getLong("id_target_type"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return targetDefinition;
    }
}
