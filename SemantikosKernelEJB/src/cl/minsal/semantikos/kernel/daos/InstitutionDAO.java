package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by des01c7 on 15-12-16.
 */
@Local
public interface InstitutionDAO {

    /**
     * Método encargado de obtener las instituciones asociadas a un usuario
     * @param user
     * @return
     */
    public List<Institution> getInstitutionBy(User user);

    public Institution getInstitutionById(long id);

    public Institution getInstitutionByCode(long code);

    /**
     * Método encargado de obtener una lista con todas las instituciones
     * @return Lista de instituciones
     */
    public List<Institution> getAllInstitution();

    public void createInstitution(Institution institution);

    public void updateInstitution(Institution institution);

    public void bindInstitutionToUser(User user, Institution institution);

    public void unbindInstitutionFromUser(User user, Institution institution);


}
