package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.InstitutionDAO;
import cl.minsal.semantikos.model.Institution;
import cl.minsal.semantikos.model.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
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
    public List<Institution> getAllInstitution() {
        return institutionDAO.getAllInstitution();
    }
}
