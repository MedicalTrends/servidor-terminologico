package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.tags.Tag;
import cl.minsal.semantikos.kernel.factories.TagFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.sql.*;
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
    private TagFactory tagFactory;

    @Override
    public Tag persist(Tag tag) {

        long idTag;
        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.create_tag(?,?,?,?)}")) {

            call.setString(1, tag.getName());
            call.setString(2, tag.getColorLetter());
            call.setString(3, tag.getColorBackground());

            long id = (tag.getParentTag()==null)?-1:tag.getParentTag().getId();
            if( id != NON_PERSISTED_ID) {
                call.setLong(4, id);
            } else {
                call.setNull(4, BIGINT);
            }
            call.execute();

            ResultSet rs = call.getResultSet();

            if (rs.next()) {
                idTag = rs.getLong(1);
            } else {
                String errorMsg = "Error al persistir el tag " + tag;
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }

            rs.close();

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
        ConnectionBD connect = new ConnectionBD();

        long idTag;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.update_tag(?,?,?,?,?)}")) {

            call.setLong(1, tag.getId());
            call.setString(2, tag.getName());
            call.setString(3, tag.getColorLetter());
            call.setString(4, tag.getColorBackground());
            long id = (tag.getParentTag()==null)?-1:tag.getParentTag().getId();
            if( id != NON_PERSISTED_ID) {
                call.setLong(5, tag.getParentTag().getId());
            } else {
                call.setNull(5, BIGINT);
            }

            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                idTag = rs.getLong(1);
                if (idTag == 0) {
                    throw new EJBException("No se realizó la actualización del tag " + tag);
                }
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al actualizar el tag " + tag;
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public void remove(Tag tag) {
        ConnectionBD connect = new ConnectionBD();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.delete_tag(?)}")) {

            call.setLong(1, tag.getId());
            call.execute();
        } catch (SQLException e) {
            String errorMsg = "Error al persistir el tag " + tag;
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public List<Tag> findTagsBy(String[] namePattern) {
        ConnectionBD connect = new ConnectionBD();

        String json;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.find_tag_by_pattern(?)}")) {

            call.setArray(1, connect.getConnection().createArrayOf("text", namePattern));
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                json = rs.getString(1);
            } else {
                String errorMsg = "Error imposible!";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar tag por patrón: " + namePattern;
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return tagFactory.createTagsFromJSON(json);
    }

    @Override
    public void linkTagToTag(Tag tag, Tag tagLink) {
        ConnectionBD connect = new ConnectionBD();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.link_parent_tag_to_child_tag(?,?)}")) {

            call.setLong(1, tag.getId());
            call.setLong(2, tagLink.getId());
            call.execute();
        } catch (SQLException e) {
            String errorMsg = "Error al asociar el tag " + tagLink + " al Tag " + tag;
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public void unlinkTagToTag(Tag tag, Tag tagUnlink) {
        ConnectionBD connect = new ConnectionBD();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.unlink_tag_to_tag(?,?)}")) {

            call.setLong(1, tag.getId());
            call.setLong(2, tagUnlink.getId());
            call.execute();
        } catch (SQLException e) {
            String errorMsg = "Error al desasociar el tag " + tagUnlink + " del " + tag;
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public List<Tag> getAllTags() {

        ConnectionBD connect = new ConnectionBD();

        String json;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.semantikos.get_all_tags()}")) {


            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                json = rs.getString(1);
            } else {
                String errorMsg = "Error imposible!";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar los hijos del tag ";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return tagFactory.createTagsFromJSON(json);
    }

    @Override
    public List<Tag> getAllTagsWithoutParent() {
        ConnectionBD connect = new ConnectionBD();

        String json;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.semantikos.get_all_tags_without()}")) {


            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                json = rs.getString(1);
            } else {
                String errorMsg = "Error imposible!";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar los hijos del tag ";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return tagFactory.createTagsFromJSON(json);
    }

    @Override
    public List<Tag> getTagsByConcept(long idConcept) {
        //ConnectionBD connect = new ConnectionBD();

        String json;
        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.semantikos.get_tags_by_concept_id(?)}")) {

            call.setLong(1, idConcept);
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                json = rs.getString(1);
            } else {
                String errorMsg = "Error imposible!";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar los hijos del tag ";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return tagFactory.createTagsFromJSON(json);

    }

    @Override
    public List<Tag> getChildrenOf(Tag parent) {
        ConnectionBD connect = new ConnectionBD();

        String json;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.find_tags_by_parent(?)}")) {

            call.setLong(1, parent.getId());
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                json = rs.getString(1);
            } else {
                String errorMsg = "Error imposible!";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar los hijos del tag con ID=" + parent.getId();
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return tagFactory.createChildrenTagsFromJSON(parent,json);

    }

    @Override
    public void assignTag(ConceptSMTK conceptSMTK, Tag tag) {

        logger.debug("Asociando el tag " + tag + " al concepto " + conceptSMTK);
        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.assign_concept_to_tag(?,?)}")) {

            call.setLong(1, conceptSMTK.getId());
            call.setLong(2, tag.getId());
            call.execute();
        } catch (SQLException e) {
            String errorMsg = "Error al asociar el tag " + tag + " al concepto " + conceptSMTK;
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public void unassignTag(ConceptSMTK conceptSMTK, Tag tag) {
        ConnectionBD connect = new ConnectionBD();

        logger.debug("Desasociando el tag " + tag + " al concepto " + conceptSMTK);
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.unassign_concept_to_tag(?,?)}")) {

            call.setLong(1, conceptSMTK.getId());
            call.setLong(2, tag.getId());
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
        ConnectionBD connect = new ConnectionBD();

        String json;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.find_tags_by_id(?)}")) {

            call.setLong(1, id);
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                json = rs.getString(1);
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

        return tagFactory.createTagFromJSON(json);
    }

    @Override
    public boolean containTag(String tagName) {
        ConnectionBD connect = new ConnectionBD();
        Long contain;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.contain_tag(?)}")) {

            call.setString(1, tagName);
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                contain = rs.getLong(1);
            } else {
                String errorMsg = "Error imposible!";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al consultar si contiene el registro";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return (contain>0)?true: false;
    }

    @Override
    public long countConceptContainTag(Tag tag) {
        ConnectionBD connect = new ConnectionBD();
        long count=0;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.concept_contain_tag(?)}")) {

            call.setLong(1, tag.getId());
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                count = rs.getLong(1);
            }
            rs.close();
        } catch (SQLException e) {
            String errorMsg = "Error al consultar si contiene el registro";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return count;
    }
}
