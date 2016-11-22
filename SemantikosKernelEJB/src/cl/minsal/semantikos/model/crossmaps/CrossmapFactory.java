package cl.minsal.semantikos.model.crossmaps;

import cl.minsal.semantikos.kernel.daos.CrossmapsDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Gustavo Punucura on 18-11-16.
 */
@Singleton
public class CrossmapFactory {

    /**
     * El logger para esta clase
     */
    private static final Logger logger = LoggerFactory.getLogger(CrossmapFactory.class);

    @EJB
    private CrossmapsDAO crossmapsDAO;

    /**
     * Este método crea un CrossmapSetMember con un Resulset
     *
     * @param resultSet El resultset a partir del cual se crea el CrossmapSetMember
     *
     * @return Un CrossmapSetMember fresco, creado a partir del ResultSet.
     */
    public CrossmapSetMember createCrossmapSetMemberFromResultSet(ResultSet resultSet) throws SQLException {

        logger.debug("creando CrossmapSetMember de ResultSet: ", resultSet);

        CrossmapSet crossmapSet = crossmapsDAO.getCrossmapSetByID(resultSet.getLong("id_cross_map_set"));
        return createCrossmapSetMemberFromResultSet(resultSet, crossmapSet);
    }


    /**
     * Este método es responsable de crear un objeto <code>DirectCrossmap</code> a partir de un ResultSet.
     *
     * @param rs El ResultSet a partir del cual se crea el crossmap.
     *
     * @return Un Crossmap Directo creado a partir del result set.
     */
    public CrossmapSetMember createCrossmapSetMemberFromResultSet(ResultSet rs, CrossmapSet crossmapSet) throws SQLException {
        long id = rs.getLong("id");
        String code = rs.getString("code");
        String gloss = rs.getString("gloss");

        return new CrossmapSetMember(id, crossmapSet, code, gloss);
    }

    public IndirectCrossmap createIndirectCrossmapFromResultSet(ResultSet rs) {
        throw new EJBException("NO IMPLEMENTADO: createIndirectCrossmapFromResultSet");
    }
}
