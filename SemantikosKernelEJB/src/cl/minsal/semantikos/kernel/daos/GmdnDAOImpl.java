package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.crossmaps.*;
import cl.minsal.semantikos.model.gmdn.CollectiveTerm;
import cl.minsal.semantikos.model.gmdn.DeviceCategory;
import cl.minsal.semantikos.model.gmdn.DeviceType;
import cl.minsal.semantikos.model.gmdn.GenericDeviceGroup;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.relationships.MultiplicityFactory;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.users.User;
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

import static java.lang.System.currentTimeMillis;

/**
 * @author Andrés Farías on 8/19/16.
 */
@Stateless
public class GmdnDAOImpl implements GmdnDAO {

    private static final Logger logger = LoggerFactory.getLogger(GmdnDAOImpl.class);

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public List<DeviceCategory> getDeviceCategoriesByGenericDeviceGroup(GenericDeviceGroup genericDeviceGroup) {

        List<DeviceCategory> deviceCategories= new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_gmdn.get_device_categories_by_generic_device_group(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, genericDeviceGroup.getCode());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                deviceCategories.add(createDeviceCategoryFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return deviceCategories;
    }

    @Override
    public GenericDeviceGroup getGenericDeviceGroupByCode(long code) {

        GenericDeviceGroup genericDeviceGroup;

        String sql = "begin ? := stk.stk_pck_gmdn.get_generic_device_group_by_code(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, code);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                genericDeviceGroup = createGenericDeviceGroupFromResultSet(rs);
            } else {
                throw new EJBException("Error al intentar obtener un Grupo de Dispositivo Genérico de code= " + code);
            }
            rs.close();
        } catch (SQLException e) {
            String s = "Error al crear un Grupo de Dispositivo Genérico en la base de datos";
            logger.error(s);
            throw new EJBException(s, e);
        }

        return genericDeviceGroup;
    }

    @Override
    public CollectiveTerm getCollectiveTermByCode(long code) {

        CollectiveTerm collectiveTerm;

        String sql = "begin ? := stk.stk_pck_gmdn.get_collective_term_by_code(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, code);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                collectiveTerm = createCollectiveTermFromResultSet(rs);
            } else {
                throw new EJBException("Error al intentar obtener un Grupo de Dispositivo Genérico de code= " + code);
            }
            rs.close();
        } catch (SQLException e) {
            String s = "Error al crear un Grupo de Dispositivo Genérico en la base de datos";
            logger.error(s);
            throw new EJBException(s, e);
        }

        return collectiveTerm;
    }

    @Override
    public List<CollectiveTerm> getParentsOf(CollectiveTerm collectiveTerm) {
        List<CollectiveTerm> collectiveTerms = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_gmdn.get_parents_of(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, collectiveTerm.getCode());
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                collectiveTerms.add(createCollectiveTermFromResultSet(rs));
            }
            rs.close();
        } catch (SQLException e) {
            String s = "Error al crear un Grupo de Dispositivo Genérico en la base de datos";
            logger.error(s);
            throw new EJBException(s, e);
        }

        return collectiveTerms;
    }

    @Override
    public List<CollectiveTerm> getChildrenOf(CollectiveTerm collectiveTerm) {
        List<CollectiveTerm> collectiveTerms = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_gmdn.get_children_of(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, collectiveTerm.getCode());
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                collectiveTerms.add(createCollectiveTermFromResultSet(rs));
            }
            rs.close();
        } catch (SQLException e) {
            String s = "Error al crear un Grupo de Dispositivo Genérico en la base de datos";
            logger.error(s);
            throw new EJBException(s, e);
        }

        return collectiveTerms;
    }

    @Override
    public List<CollectiveTerm> getParentLines(List<CollectiveTerm> nodes) {
        List<CollectiveTerm> allNodesParentNodes = new ArrayList<>();
        int parents = 0;

        for (CollectiveTerm node : nodes) {

            if(node == null) {
                break;
            }

            List<CollectiveTerm> parentNodes = getParentsOf(node);

            parents = parents + parentNodes.size();

            List<CollectiveTerm> thisNodeParentNodes = new ArrayList<>();

            if(parentNodes.isEmpty() && !allNodesParentNodes.contains(node)) {
                allNodesParentNodes.add(node);
            }

            for (CollectiveTerm parentNode : parentNodes) {

                parentNode.getChildren().addAll(getChildrenOf(parentNode));
                //parentNode.getChildren().addAll(getChildrenOf(node));
                thisNodeParentNodes.add(parentNode);
            }

            allNodesParentNodes.addAll(thisNodeParentNodes);

        }

        if(parents==0) {
            return allNodesParentNodes;
        }
        else {
            if(allNodesParentNodes.isEmpty()) {
                return nodes;
            }
            else {
                return getParentLines(allNodesParentNodes);
            }
        }
    }

    @Override
    public List<CollectiveTerm> getCollectiveTermsByGenericDeviceGroup(GenericDeviceGroup genericDeviceGroup) {
        List<CollectiveTerm> collectiveTerms= new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_gmdn.get_collective_terms_by_generic_device_group(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, genericDeviceGroup.getCode());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                collectiveTerms.add(createCollectiveTermFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return collectiveTerms;
    }

    @Override
    public DeviceType getDeviceTypeById(long id) {
        CollectiveTerm collectiveTerm;

        String sql = "begin ? := stk.stk_pck_gmdn.get_device_type_by_id(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, id);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                collectiveTerm = createDeviceTypeFromResultSet(rs);
            } else {
                throw new EJBException("Error al intentar obtener un Tipo de Dispositivo de code= " + code);
            }
            rs.close();
        } catch (SQLException e) {
            String s = "Error al crear un Grupo de Dispositivo Genérico en la base de datos";
            logger.error(s);
            throw new EJBException(s, e);
        }

        return collectiveTerm;
    }

    /**
     * Este método es responsable de crear un objeto <code>CrossmapSetMember</code> a partir de un ResultSet.
     *
     * @param rs El ResultSet a partir del cual se crea el crossmap.
     *
     * @return Un Crossmap Directo creado a partir del result set.
     */
    public GenericDeviceGroup createGenericDeviceGroupFromResultSet(ResultSet rs) throws SQLException {

        long code = rs.getLong("code");
        String termName = rs.getString("term_name");
        String termDefinition = rs.getString("term_definition");
        String termStatus = rs.getString("term_status");

        char termTypeIdentifier = rs.getString("term_type_identifier").charAt(0);

        String productSpecifier = rs.getString("product_specifier");

        GenericDeviceGroup genericDeviceGroup = new GenericDeviceGroup(code, termName, termDefinition, termStatus, termTypeIdentifier, productSpecifier);

        List<DeviceCategory> deviceCategories = getDeviceCategoriesByGenericDeviceGroup(genericDeviceGroup);

        genericDeviceGroup.setDeviceCategories(deviceCategories);

        List<CollectiveTerm> collectiveTerms = getCollectiveTermsByGenericDeviceGroup(genericDeviceGroup);

        collectiveTerms = getParentLines(collectiveTerms);

        genericDeviceGroup.setCollectiveTerms(collectiveTerms);

        return genericDeviceGroup;
    }

    /**
     * Este método es responsable de crear un objeto <code>CrossmapSetMember</code> a partir de un ResultSet.
     *
     * @param rs El ResultSet a partir del cual se crea el crossmap.
     *
     * @return Un Crossmap Directo creado a partir del result set.
     */
    public CollectiveTerm createCollectiveTermFromResultSet(ResultSet rs) throws SQLException {
        // id bigint, id_concept bigint, id_crossmapset bigint, id_user bigint, id_validity_until timestamp
        long code = rs.getLong("code");
        String termName = rs.getString("term_name");
        String termDefinition = rs.getString("term_definition");

        CollectiveTerm collectiveTerm = new CollectiveTerm(code, termName, termDefinition);

        return collectiveTerm;
    }

    /**
     * Este método es responsable de crear un objeto <code>CrossmapSetMember</code> a partir de un ResultSet.
     *
     * @param rs El ResultSet a partir del cual se crea el crossmap.
     *
     * @return Un Crossmap Directo creado a partir del result set.
     */
    public DeviceCategory createDeviceCategoryFromResultSet(ResultSet rs) throws SQLException {
        // id bigint, id_concept bigint, id_crossmapset bigint, id_user bigint, id_validity_until timestamp
        long code = rs.getLong("code");
        String description = rs.getString("description");

        return new DeviceCategory(code, description);
    }
}
