package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Local;
import javax.ejb.Remote;
import java.util.List;

/**
 * Created by des01c7 on 16-12-16.
 */
@Remote
public interface InstitutionManager {

    /**
     * Método encargado de obtener las instituciones a las que se encuentra asociado un usuario
     * @param user
     * @return Lista de instituciones
     */
    public List<Institution> getInstitutionsBy(User user);

    public Institution getInstitutionById(long id);

    public Institution getInstitutionByCode(long code);

    public long createInstitution(Institution institution, User user);

    public void update(Institution originalInstitution, Institution updatedInstitution, User user);

    public void deleteInstitution(Institution institution, User user);

    /**
     * Método encargado de obtener una lista con todas las instituciones
     * @return Lista de instituciones
     */
    public List<Institution> getAllInstitution();

    /**
     * Este método es responsable de asociar (agregar) un establecimiento a un usuario.
     *
     * @param user     El usuario al cual se agrega el establecimiento.
     * @param institution El establecimiento que será asociado al usuario. Este puede o no estar persistido.
     * @param _user        El usuario que agrega el establecimiento
     * @return El establecimiento creada a partir de la asociacion.
     */
    public Institution bindInstitutionToUser(User user, Institution institution, User _user);

    /**
     * Este método es responsable de eliminar lógicamente un establecimiento.
     *
     * @param institution     El establecimiento que se desea eliminar.
     * @param user            El usuario que realiza la eliminación.
     */
    public void unbindInstitutionFromUser(User user, Institution institution, User _user);
}

