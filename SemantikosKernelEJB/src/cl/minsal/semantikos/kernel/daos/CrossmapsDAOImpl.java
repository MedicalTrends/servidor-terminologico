package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.daos.mappers.CrossmapMapper;
import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.relationships.MultiplicityFactory;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.crossmaps.*;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;

/**
 * @author Andrés Farías on 8/19/16.
 */
@Stateless
public class CrossmapsDAOImpl implements CrossmapsDAO {

    private static final Logger logger = LoggerFactory.getLogger(ConceptDAOImpl.class);

    @EJB
    private ConceptDAO conceptDAO;

    @EJB
    private RelationshipDefinitionDAO relationshipDAO;

    @EJB
    private CrossmapMapper crossmapMapper;

    @Override
    public DirectCrossmap create(DirectCrossmap directCrossmap, User user) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_crossmap.create_direct_crossmap(?,?,?,?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /*
                1. source concept
                2. external term id
                3. validity until.
                4. user id
            */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, directCrossmap.getSourceConcept().getId());
            call.setLong(3, directCrossmap.getTarget().getId());
            call.setTimestamp(4, new Timestamp(currentTimeMillis()));
            call.setLong(5, user.getId());
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                directCrossmap.setId(rs.getLong(1));
            }
            rs.close();
        } catch (SQLException e) {
            String s = "Error al crear un Crossmap en la base de datos";
            logger.error(s);
            throw new EJBException(s, e);
        }

        return directCrossmap;
    }

    @Override
    public DirectCrossmap getDirectCrossmapById(long idCrossmap) {
        //ConnectionBD connect = new ConnectionBD();
        DirectCrossmap directCrossmapFromResultSet;

        String sql = "begin ? := stk.stk_pck_crossmap.get_direct_crossmap(?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, idCrossmap);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                directCrossmapFromResultSet = crossmapMapper.createDirectCrossmapFromResultSet(rs);
            } else {
                throw new EJBException("Error al intentar obtener un crossmap directo de ID= " + idCrossmap);
            }
            rs.close();
        } catch (SQLException e) {
            String s = "Error al crear un Crossmap en la base de datos";
            logger.error(s);
            throw new EJBException(s, e);
        }

        return directCrossmapFromResultSet;
    }

    @Override
    public DirectCrossmap bindConceptSMTKToCrossmapSetMember(ConceptSMTK conceptSMTK, CrossmapSetMember crossmapSetMember) {
        return null;
    }

    @Override
    public CrossmapSetMember getCrossmapSetMemberById(long idCrossmapSetMember) {

        //ConnectionBD connect = new ConnectionBD();
        CrossmapSet crossmapSetFromResultSet;

        String sql = "begin ? := stk.stk_pck_crossmap.get_crossmapsetmember_by_id(?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, idCrossmapSetMember);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                return crossmapMapper.createCrossmapSetMemberFromResultSet(rs, null);
            }

            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        throw new IllegalArgumentException("No existe un crossmapSetMember con ID=" + idCrossmapSetMember);
    }

    @Override
    public List<CrossmapSetMember> getRelatedCrossMapSetMembers(ConceptSCT conceptSCT) {

        List<CrossmapSetMember> crossmapSetMembers = new ArrayList<>();
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_crossmap.get_related_crossmapset_member(?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, conceptSCT.getId());
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                CrossmapSetMember crossmapSetMember = crossmapMapper.createCrossmapSetMemberFromResultSet(rs, null);
                crossmapSetMembers.add(crossmapSetMember);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return crossmapSetMembers;
    }
    
    @Override
    public CrossmapSet getCrossmapSetByID(long id) {

        //ConnectionBD connect = new ConnectionBD();
        CrossmapSet crossmapSetFromResultSet;

        String sql = "begin ? := stk.stk_pck_crossmap.get_crossmapset_by_id(?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, id);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                crossmapSetFromResultSet = crossmapMapper.createCrossmapSetFromResultSet(rs);
            } else {
                throw new EJBException("Error al intentar obtener un crossmap directo de ID= " + id);
            }
            rs.close();
        } catch (SQLException e) {
            String s = "Error al crear un Crossmap en la base de datos";
            logger.error(s);
            throw new EJBException(s, e);
        }

        return crossmapSetFromResultSet;
    }

    @Override
    public List<CrossmapSetMember> findCrossmapSetMemberBy(CrossmapSet crossmapSet, String pattern) {
        List<CrossmapSetMember> crossmapSetMembers = new ArrayList<CrossmapSetMember>();
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_crossmap.find_crossmapsetmember_by_pattern_and_crossmapset(?,?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, crossmapSet.getId());
            call.setString(3, pattern);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                CrossmapSetMember crossmapSetMember = crossmapMapper.createCrossmapSetMemberFromResultSet(rs, crossmapSet);
                crossmapSetMembers.add(crossmapSetMember);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return crossmapSetMembers;
    }

    @Override
    public List<CrossmapSetMember> findCrossmapSetMemberByCod1(CrossmapSet crossmapSet, String cod) {
        List<CrossmapSetMember> crossmapSetMembers = new ArrayList<CrossmapSetMember>();
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_crossmap.find_crossmapsetmember_by_cod1_and_crossmapset(?,?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, crossmapSet.getId());
            call.setString(3, cod);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                CrossmapSetMember crossmapSetMember = crossmapMapper.createCrossmapSetMemberFromResultSet(rs, crossmapSet);
                crossmapSetMembers.add(crossmapSetMember);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return crossmapSetMembers;
    }

    @Override
    public List<CrossmapSet> getCrossmapSets() {
        //ConnectionBD connect = new ConnectionBD();
        List<CrossmapSet> crossmapSets = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_crossmap.get_crossmapsets; end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                crossmapSets.add(getCrossmapSetByID(rs.getLong(1)));
            }
            rs.close();
        } catch (SQLException e) {
            String s = "Error al recuperar los crossmaps";
            logger.error(s);
            throw new EJBException(s, e);
        }

        return crossmapSets;
    }

    @Override
    public List<IndirectCrossmap> getCrossmapsBySCT(long idConceptSCT, ConceptSMTK sourceConcept) {

        List<IndirectCrossmap> indirectCrossmaps = new ArrayList<>();

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_crossmap.get_crossmap_by_sct(?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, idConceptSCT);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {

                IndirectCrossmap indirectCrossmap = crossmapMapper.createIndirectCrossMapFromResultSet(rs, sourceConcept);
                indirectCrossmaps.add(indirectCrossmap);
            }
            rs.close();
        } catch (SQLException e) {
            String s = "Error al recuperar los crossmaps";
            logger.error(s);
            throw new EJBException(s, e);
        }

        return indirectCrossmaps;
    }

    @Override
    public List<CrossmapSetMember> getCrossmapSetMemberByAbbreviatedName(String crossmapSetAbbreviatedName) {
        List<CrossmapSetMember> crossmapSetMembers = new ArrayList<CrossmapSetMember>();

        CrossmapSet crossmapSet = getCrossmapSetByAbbreviatedName(crossmapSetAbbreviatedName);
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_crossmap.get_crossmapsetmember_by_cms_abbreviated_name(?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, crossmapSetAbbreviatedName);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                CrossmapSetMember crossmapSetMember = crossmapMapper.createCrossmapSetMemberFromResultSet(rs, crossmapSet);
                crossmapSetMembers.add(crossmapSetMember);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return crossmapSetMembers;
    }

    private CrossmapSet getCrossmapSetByAbbreviatedName(String crossmapSetAbbreviatedName) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_crossmap.get_crossmapset_by_cms_abbreviated_name(?); end;";

        ResultSet rs;
        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, crossmapSetAbbreviatedName);
            call.execute();

            rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                CrossmapSet crossmapSetFromResultSet = crossmapMapper.createCrossmapSetFromResultSet(rs);
                rs.close();
                return crossmapSetFromResultSet;
            } else {
                rs.close();
                throw new IllegalArgumentException("No existe un Crossmap Set de nombre abreviado " + crossmapSetAbbreviatedName);
            }
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }
    }

}
