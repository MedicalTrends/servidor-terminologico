package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.helpertables.HelperTableColumn;
import cl.minsal.semantikos.model.helpertables.HelperTableRecordFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static cl.minsal.semantikos.kernel.util.StringUtils.underScoreToCamelCaseJSON;

/**
 * @author Andrés Farías on 9/5/16.
 */
@Singleton
@Startup
public class InitFactoriesDAOImpl implements InitFactoriesDAO {

    private static final Logger logger = LoggerFactory.getLogger(InitFactoriesDAOImpl.class);

    @EJB
    HelperTableRecordFactory helperTableRecordFactory;

    @PostConstruct
    private void init() {
        this.refreshDescriptionTypes();
        this.refreshTagsSMTK();
        this.refreshColumns();
    }

    /**
     * Este método es responsable de crear un Tag STMK a partir de un raw de un resultset.
     *
     * @param rs El resultset posicionado en una Raw.
     *
     * @return El objeto fresco construido desde el Resultset.
     *
     * @throws SQLException Arrojada si hay un problema.
     */
    private TagSMTK createTagSMTKFromResultSet(@NotNull ResultSet rs) throws SQLException {

        long id = rs.getLong("id");
        String name = rs.getString("name");

        return new TagSMTK(id, name);
    }


    @Override
    public TagSMTKFactory refreshTagsSMTK() {

        ConnectionBD connect = new ConnectionBD();

        List<TagSMTK> tagsSMTK = new ArrayList<>();

        String sql = "{call semantikos.get_all_tag_smtks()}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.execute();
            ResultSet rs = call.getResultSet();

            /* Se recuperan los tagsSMTK */
            while (rs.next()) {
                tagsSMTK.add(createTagSMTKFromResultSet(rs));
            }

            /* Se setea la lista de tagsSMTK */
            TagSMTKFactory.getInstance().setTagsSMTK(tagsSMTK);

        } catch (SQLException e) {
            String errorMsg = "Error al intentar recuperar tagsSMTK de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return TagSMTKFactory.getInstance();
    }

    @Override
    public DescriptionTypeFactory refreshDescriptionTypes() {

        ConnectionBD connect = new ConnectionBD();
        ObjectMapper mapper = new ObjectMapper();

        List<DescriptionType> descriptionTypes = new ArrayList<>();

        String sql = "{call semantikos.get_description_types()}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.execute();
            ResultSet rs = call.getResultSet();

            /* Se recuperan los description types */
            DescriptionTypeDTO[] theDescriptionTypes = new DescriptionTypeDTO[0];
            if (rs.next()) {
                String resultJSON = rs.getString(1);
                theDescriptionTypes = mapper.readValue(underScoreToCamelCaseJSON(resultJSON), DescriptionTypeDTO[].class);
            }

            if (theDescriptionTypes.length > 0) {
                for (DescriptionTypeDTO aDescriptionType : theDescriptionTypes) {
                    DescriptionType descriptionType = aDescriptionType.getDescriptionType();
                    descriptionTypes.add(descriptionType);
                }
            }

            /* Se setea la lista de Tipos de descripción */
            DescriptionTypeFactory.getInstance().setDescriptionTypes(descriptionTypes);
        } catch (SQLException e) {
            String errorMsg = "Error al intentar recuperar Description Types de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        } catch (IOException e) {
            String errorMsg = "Error al intentar parsear Description Types en JSON.";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return DescriptionTypeFactory.getInstance();
    }

    @Override
    public HelperTableColumnFactory refreshColumns() {
        ConnectionBD connect = new ConnectionBD();

        List<HelperTableColumn> helperTableColumns = new ArrayList<>();

        String sql = "{call semantikos.get_all_helper_table_columns()}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.execute();
            ResultSet rs = call.getResultSet();

            /* Se recuperan las columnas */
            if (rs.next()) {

                String json = rs.getString(1);
                if(json==null)
                    return null;

                try {
                    helperTableColumns = helperTableRecordFactory.createHelperTableColumnsFromJSON(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                throw new EJBException("Error imposible en HelperTableDAOImpl");
            }

            /* Se setea la lista de tagsSMTK */
            HelperTableColumnFactory.getInstance().setHelperTableColumns(helperTableColumns);

        } catch (SQLException e) {
            String errorMsg = "Error al intentar recuperar la lista de columnas de tablas auxiliares de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return HelperTableColumnFactory.getInstance();
    }
}
