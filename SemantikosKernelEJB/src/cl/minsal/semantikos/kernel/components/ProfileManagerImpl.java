package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.InstitutionDAO;
import cl.minsal.semantikos.kernel.daos.ProfileDAO;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by des01c7 on 16-12-16.
 */
@Stateless
public class ProfileManagerImpl implements ProfileManager {

    @EJB
    private ProfileDAO profileDAO;

    @EJB
    private AuditManager auditManager;


    @Override
    public List<Profile> getProfilesBy(User user) {
        return profileDAO.getProfilesBy(user);
    }

    @Override
    public Profile getProfileById(long id) {
        return profileDAO.getProfileById(id);
    }

    @Override
    public List<Profile> getAllProfiles() {
        return profileDAO.getAllProfiles();
    }

    @Override
    public Profile bindProfileToUser(User user, Profile profile, User _user) {
        profileDAO.bindProfileToUser(user, profile);

        /* Registrar en el Historial si es preferida (Historial BR) */
        auditManager.recordUserProfileBinding(user, profile, _user);

        /* Se retorna el establecimiento persistido */
        return profile;
    }

    @Override
    public void deleteProfile(Profile profile, User user) {

    }
}
