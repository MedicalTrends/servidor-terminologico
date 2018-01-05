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

    @EJB
    private AuditManager auditManager;

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

    @Override
    public Institution bindInstitutionToUser(User user, Institution institution, User _user) {

        institutionDAO.bindInstitutionToUser(user, institution);

        /* Registrar en el Historial si es preferida (Historial BR) */
        auditManager.recordUserInstitutionBinding(user, institution, _user);

        /* Se retorna el establecimiento persistido */
        return institution;
    }

    @Override
    public void unbindInstitutionFromUser(User user, Institution institution, User _user) {
        institutionDAO.unbindInstitutionFromUser(user, institution);

        /* Registrar en el Historial si es preferida (Historial BR) */
        auditManager.recordUserInstitutionUnbinding(user, institution, _user);
    }
}
