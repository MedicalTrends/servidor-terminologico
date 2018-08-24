package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by des01c7 on 15-12-16.
 */
@Local
public interface ProfileDAO {

    /**
     * Método encargado de obtener las instituciones asociadas a un usuario
     * @param user
     * @return
     */
    public List<Profile> getProfilesBy(User user);

    public Profile getProfileById(long id);

    /**
     * Método encargado de obtener una lista con todas las instituciones
     * @return Lista de instituciones
     */
    public List<Profile> getAllProfiles();

    public void bindProfileToUser(User user, Profile profile);

    public void unbindProfileFromUser(User user, Profile profile);


}
