package cl.minsal.semantikos.kernel.daos.mappers;

import cl.minsal.semantikos.kernel.daos.AuthDAO;
import cl.minsal.semantikos.kernel.daos.InstitutionDAO;
import cl.minsal.semantikos.kernel.daos.QuestionDAO;
import cl.minsal.semantikos.kernel.daos.TagSMTKDAO;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by root on 28-06-17.
 */
@Singleton
public class UserMapper {

    @EJB
    AuthDAO authDAO;

    @EJB
    InstitutionDAO institutionDAO;

    @EJB
    QuestionDAO questionDAO;

    public User makeUserFromResult(ResultSet rs) throws SQLException {

        User u = new User();

        u.setId(rs.getBigDecimal(1).longValue());
        u.setUsername(rs.getString(2));
        u.setPasswordHash(rs.getString(3));
        u.setPasswordSalt(rs.getString(4));
        u.setName(rs.getString(5));
        u.setLastName(rs.getString(6));
        u.setSecondLastName(rs.getString(7));
        u.setEmail(rs.getString(8));

        u.setLocked(rs.getBoolean(9));
        u.setFailedLoginAttempts(rs.getInt(10));
        u.setFailedAnswerAttempts(rs.getInt(11));

        u.setLastLogin(rs.getTimestamp(12));
        u.setLastPasswordChange(rs.getTimestamp(13));

        u.setLastPasswordHash1(rs.getString(14));
        u.setLastPasswordHash2(rs.getString(15));
        u.setLastPasswordHash3(rs.getString(16));
        u.setLastPasswordHash4(rs.getString(17));

        u.setLastPasswordSalt1(rs.getString(18));
        u.setLastPasswordSalt2(rs.getString(19));
        u.setLastPasswordSalt3(rs.getString(20));
        u.setLastPasswordSalt4(rs.getString(21));

        u.setDocumentNumber(rs.getString(22));
        u.setVerificationCode(rs.getString(23));
        u.setValid(rs.getBoolean(24));
        u.setDocumentRut(rs.getBoolean(25));

        u.setProfiles(authDAO.getUserProfiles(u.getId()));

        u.setInstitutions(institutionDAO.getInstitutionBy(u));

        u.setAnswers(questionDAO.getAnswersByUser(u));

        return u;
    }

    /**
     * Este método es responsable de crear un Profile a partir de una fila de un resultset.
     *
     * @param rs  resultset parado en la fila a procesar
     *
     * @return El Profile creado a partir de la fila.
     */
    public Profile makeProfileFromResult(ResultSet rs) throws SQLException {

        long idProfile = ( rs.getBigDecimal(1) ).longValue();
        String name = rs.getString(2);
        String description = rs.getString(3);

        return new Profile(idProfile, name, description);
    }
}
