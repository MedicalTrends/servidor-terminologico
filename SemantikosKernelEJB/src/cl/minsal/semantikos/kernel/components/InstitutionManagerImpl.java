package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.InstitutionDAO;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.User;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.util.List;

/**
 * Created by des01c7 on 16-12-16.
 */
@Stateless
public class InstitutionManagerImpl implements InstitutionManager {

    @EJB
    private InstitutionDAO institutionDAO;

    @Override
    public List<Institution> getInstitutionsBy(User user) {
        return institutionDAO.getInstitutionBy(user);
    }

    @Override
    public Institution getInstitutionById(long id) {
        return institutionDAO.getInstitutionById(id);
    }

    @Override
    public List<Institution> getAllInstitution() {
        return institutionDAO.getAllInstitution();
    }
}
