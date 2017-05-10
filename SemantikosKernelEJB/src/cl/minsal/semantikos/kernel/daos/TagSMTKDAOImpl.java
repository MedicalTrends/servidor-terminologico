package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.validation.constraints.NotNull;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 9/5/16.
 */
@Singleton
@Startup
public class TagSMTKDAOImpl implements TagSMTKDAO {

    private static final Logger logger = LoggerFactory.getLogger(TagSMTKDAOImpl.class);

    @PostConstruct
    private void init() {
        this.refreshTagsSMTK();
    }

    @Override
    public List<TagSMTK> getAllTagSMTKs() {

        List<TagSMTK> tagSMTKs = new ArrayList<>();
        ConnectionBD connect = new ConnectionBD();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_all_tag_smtks()}")) {

            call.execute();
            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                tagSMTKs.add(createTagSMTKFromResultSet(rs));
            }
            rs.close();
        } catch (SQLException e) {
            String errorMsg = "Error al invocar función semantikos.get_all_tag_smtks()";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return tagSMTKs;
    }

    @Override
    public TagSMTK findTagSMTKByID(long idTag) {
        ConnectionBD connect = new ConnectionBD();

        TagSMTK tagSMTK;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_tag_smtks_by_id(?)}")) {

            call.setLong(1, idTag);
            call.execute();
            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                tagSMTK = createTagSMTKFromResultSet(rs);
            } else {
                String errorMsg = "Error al invocar función semantikos.get_tag_smtks_by_id()";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();
        } catch (SQLException e) {
            String errorMsg = "Error al invocar función semantikos.get_tag_smtks_by_id()";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return tagSMTK;
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
}
