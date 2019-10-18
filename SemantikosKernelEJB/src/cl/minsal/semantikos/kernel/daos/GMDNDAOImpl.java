package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.crossmaps.CrossmapSet;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetFactory;
import cl.minsal.semantikos.model.crossmaps.gmdn.CollectiveTerm;
import cl.minsal.semantikos.model.crossmaps.gmdn.GenericDeviceGroup;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 8/19/16.
 */
@Stateless
public class GMDNDAOImpl implements GMDNDAO {

    private static final Logger logger = LoggerFactory.getLogger(GMDNDAOImpl.class);

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public GenericDeviceGroup getGenericDeviceGroupById(long code) {

        GenericDeviceGroup genericDeviceGroup;

        String sql = "begin ? := stk.stk_pck_gmdn.get_generic_device_group_by_id(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, code);
            call.execute();

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
    public List<GenericDeviceGroup> findGenericDeviceGroupsByPattern(String pattern) {

        List<GenericDeviceGroup> genericDeviceGroups = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_gmdn.find_generic_device_groups_by_pattern(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, pattern);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                genericDeviceGroups.add(createGenericDeviceGroupFromResultSet(rs));
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return genericDeviceGroups;
    }

    @Override
    public List<GenericDeviceGroup> getGenericDeviceGroups() {

        List<GenericDeviceGroup> genericDeviceGroups = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_gmdn.get_generic_device_groups; end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                genericDeviceGroups.add(createGenericDeviceGroupFromResultSet(rs));
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return genericDeviceGroups;
    }

    @Override
    public List<GenericDeviceGroup> getGenericDeviceGroupsPaginated(int page, int pageSize) {

        List<GenericDeviceGroup> genericDeviceGroups = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_gmdn.get_generic_device_group_paginated(?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setInt(2, page);
            call.setInt(3, pageSize);

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                genericDeviceGroups.add(createGenericDeviceGroupFromResultSet(rs));
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return genericDeviceGroups;
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

                parentNode.getChildren().add(node);
                //parentNode.getChildren().addAll(getChildrenOf(parentNode));
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

    /**
     * Este método es responsable de crear un objeto <code>CrossmapSetMember</code> a partir de un ResultSet.
     *
     * @param rs El ResultSet a partir del cual se crea el crossmap.
     *
     * @return Un Crossmap Directo creado a partir del result set.
     */
    public GenericDeviceGroup createGenericDeviceGroupFromResultSet(ResultSet rs) throws SQLException {

        long code = rs.getLong("code");
        String termName  = rs.getString("term_name");
        String termDefinition = rs.getString("term_definition");
        String termStatus = rs.getString("term_status");
        Timestamp createdDate = rs.getTimestamp("created_date");
        Timestamp modifiedDate = rs.getTimestamp("modified_date");
        Timestamp obsoletedDate = rs.getTimestamp("obsoleted_date");
        CrossmapSet crossmapSet = CrossmapSetFactory.getInstance().findCrossmapSetsById(rs.getLong("id_cross_map_set"));

        GenericDeviceGroup genericDeviceGroup = new GenericDeviceGroup(crossmapSet, code, code, termName, termDefinition, termStatus, createdDate, modifiedDate, obsoletedDate);

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

        return new CollectiveTerm(code, termName, termDefinition);
    }


}
