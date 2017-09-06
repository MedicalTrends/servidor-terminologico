package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.tags.Tag;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static cl.minsal.semantikos.model.DAO.NON_PERSISTED_ID;
import static java.sql.Types.BIGINT;

/**
 * @author Gustavo Punucura
 */
@Stateless
public class TagDAOImpl implements TagDAO {

    /** El logger de esta clase */
    private static final Logger logger = LoggerFactory.getLogger(ConceptDAOImpl.class);

    @EJB
    TagDAO tagDAO;

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public Tag persist(Tag tag) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_tag.create_tag(?,?,?,?); end;";

        long idTag;
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.NUMERIC);
            call.setString(2, tag.getName());
            call.setString(3, tag.getColorLetter());
            call.setString(4, tag.getColorBackground());

            long id = (tag.getParentTag()==null)?-1:tag.getParentTag().getId();
            if( id != NON_PERSISTED_ID) {
                call.setLong(5, id);
            } else {
                call.setNull(5, BIGINT);
            }
            call.execute();

            //ResultSet rs = (ResultSet) call.getObject(1);

            if (call.getLong(1) > 0) {
                idTag = call.getLong(1);
            } else {
                String errorMsg = "Error al persistir el tag " + tag;
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }

            //rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al persistir el tag " + tag;
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        /* Se establece el ID de la entidad */
        tag.setId(idTag);

        return tag;
    }

    @Override
    public void update(Tag tag) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_tag.update_tag(?,?,?,?,?); end;";

        long idTag;
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setLong(2, tag.getId());
            call.setString(3, tag.getName());
            call.setString(4, tag.getColorLetter());
            call.setString(5, tag.getColorBackground());
            long id = (tag.getParentTag()==null)?-1:tag.getParentTag().getId();
            if( id != NON_PERSISTED_ID) {
                call.setLong(6, tag.getParentTag().getId());
            } else {
                call.setNull(6, BIGINT);
            }
            call.execute();

            //ResultSet rs = (ResultSet) call.getObject(1);

            if (call.getLong(1) > 0) {
                idTag = call.getLong(1);
            }
            else {
                throw new EJBException("No se realizó la actualización del tag " + tag);
            }
            //rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al actualizar el tag " + tag;
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public void remove(Tag tag) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_tag.delete_tag(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setLong(2, tag.getId());
            call.execute();
        } catch (SQLException e) {
            String errorMsg = "Error al persistir el tag " + tag;
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public List<Tag> findTagsBy(String[] namePattern) {
        //ConnectionBD connect = new ConnectionBD();

        List<Tag> tags = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_tag.delete_tag(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setArray(2, connection.createArrayOf("text", namePattern));
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                tags.add(createTagFromResultSet(rs, null));
            }

            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar tag por patrón: " + namePattern;
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return tags;
    }

    @Override
    public void linkTagToTag(Tag tag, Tag tagLink) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_tag.link_parent_tag_to_child_tag(?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setLong(2, tag.getId());
            call.setLong(3, tagLink.getId());
            call.execute();
        } catch (SQLException e) {
            String errorMsg = "Error al asociar el tag " + tagLink + " al Tag " + tag;
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public void unlinkTagToTag(Tag tag, Tag tagUnlink) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_tag.unlink_tag_to_tag(?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setLong(2, tag.getId());
            call.setLong(3, tagUnlink.getId());
            call.execute();
        } catch (SQLException e) {
            String errorMsg = "Error al desasociar el tag " + tagUnlink + " del " + tag;
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public List<Tag> getAllTags() {

        //ConnectionBD connect = new ConnectionBD();

        List<Tag> tags = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_tag.get_all_tags; end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                tags.add(createTagFromResultSet(rs, null));
            }

            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar los hijos del tag ";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return tags;
    }

    @Override
    public List<Tag> getAllTagsWithoutParent() {
        //ConnectionBD connect = new ConnectionBD();

        List<Tag> tags = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_tag.get_all_tags_without; end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                tags.add(createTagFromResultSet(rs, null));
            }

            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar los hijos del tag ";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return tags;
    }

    @Override
    public List<Tag> getTagsByConcept(ConceptSMTK conceptSMTK) {
        //ConnectionBD connect = new ConnectionBD();

        List<Tag> tags = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_tag.get_tags_by_concept_id(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, conceptSMTK.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                tags.add(createTagFromResultSet(rs, null));
            }

            rs.close();
            call.close();
            connection.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar los hijos del tag ";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return tags;

    }

    @Override
    public List<Tag> getChildrenOf(Tag parent) {
        //ConnectionBD connect = new ConnectionBD();

        List<Tag> tags = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_tag.find_tags_by_parent(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, parent.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                tags.add(createTagFromResultSet(rs, parent));
            }

            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar los hijos del tag con ID=" + parent.getId();
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return tags;

    }

    @Override
    public void assignTag(ConceptSMTK conceptSMTK, Tag tag) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_tag.assign_concept_to_tag(?,?); end;";

        logger.debug("Asociando el tag " + tag + " al concepto " + conceptSMTK);
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.NUMERIC);
            call.setLong(2, conceptSMTK.getId());
            call.setLong(3, tag.getId());
            call.execute();
        } catch (SQLException e) {
            String errorMsg = "Error al asociar el tag " + tag + " al concepto " + conceptSMTK;
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public void unassignTag(ConceptSMTK conceptSMTK, Tag tag) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_tag.unassign_concept_to_tag(?,?); end;";

        logger.debug("Desasociando el tag " + tag + " al concepto " + conceptSMTK);
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.NUMERIC);
            call.setLong(2, conceptSMTK.getId());
            call.setLong(3, tag.getId());
            call.execute();
        } catch (SQLException e) {
            String errorMsg = "Error al desasociar el tag " + tag + " al concepto " + conceptSMTK;
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

    }

    @Override
    public Tag findTagByID(long id) {
        if(id==0 || id==NON_PERSISTED_ID) return null;
        //ConnectionBD connect = new ConnectionBD();

        Tag tag;

        String sql = "begin ? := stk.stk_pck_tag.find_tags_by_id(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, id);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                tag = createTagFromResultSet(rs, null);
            } else {
                String errorMsg = "Error imposible!";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar los hijos del tag con ID=" + id;
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return tag;
    }

    @Override
    public boolean containTag(String tagName) {
        //ConnectionBD connect = new ConnectionBD();
        Long contain;

        String sql = "begin ? := stk.stk_pck_tag.contain_tag(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setString(2, tagName);
            call.execute();

            //ResultSet rs = (ResultSet) call.getObject(1);

            contain = call.getLong(1);
            //rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al consultar si contiene el registro";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return (contain>0)?true: false;
    }

    @Override
    public long countConceptContainTag(Tag tag) {
        //ConnectionBD connect = new ConnectionBD();
        long count=0;

        String sql = "begin ? := stk.stk_pck_tag.concept_contain_tag(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.NUMERIC);
            call.setLong(2, tag.getId());
            call.execute();

            count = call.getLong(1);

        } catch (SQLException e) {
            String errorMsg = "Error al consultar si contiene el registro";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return count;
    }

    public Tag createTagFromResultSet(ResultSet rs, Tag parent) {

        try {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            String backgroundColor =  rs.getString("background_color");
            String letterColor = rs.getString("letter_color");

            if(parent == null) {
                parent = tagDAO.findTagByID(rs.getLong("id_parent_tag"));
            }
            List<Tag> children = new ArrayList<>();
            Tag tag = new Tag(id, name, backgroundColor, letterColor, parent);

            children = tagDAO.getChildrenOf(tag);
            tag.setSon(children);

            return tag;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
