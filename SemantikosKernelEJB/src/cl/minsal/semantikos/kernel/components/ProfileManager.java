package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created by des01c7 on 16-12-16.
 */
@Remote
public interface ProfileManager {

    /**
     * Método encargado de obtener las instituciones a las que se encuentra asociado un usuario
     * @param user
     * @return Lista de instituciones
     */
    public List<Profile> getProfilesBy(User user);

    public Profile getProfileById(long id);


    /**
     * Método encargado de obtener una lista con todas las instituciones
     * @return Lista de instituciones
     */
    public List<Profile> getAllProfiles();

    /**
     * Este método es responsable de asociar (agregar) un perfil a un usuario.
     *
     * @param user     El usuario al cual se agrega el perfil.
     * @param profile   El perfil que será asociado al usuario. Este puede o no estar persistido.
     * @param _user        El usuario que agrega el perfil
     * @return   El perfil creada a partir de la asociacion.
     */
    public Profile bindProfileToUser(User user, Profile profile, User _user);

    /**
     * Este método es responsable de eliminar lógicamente un perfil.
     *
     * @param profile     El perfil que se desea eliminar.
     * @param user        El usuario que realiza la eliminación.
     */
    public void deleteProfile(Profile profile, User user);

}

