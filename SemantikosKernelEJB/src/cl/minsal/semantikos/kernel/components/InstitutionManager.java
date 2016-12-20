package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.Institution;
import cl.minsal.semantikos.model.User;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by des01c7 on 16-12-16.
 */
@Local
public interface InstitutionManager {

    /**
     * Método encargado de obtener las instituciones a las que se encuentra asociado un usuario
     * @param user
     * @return Lista de instituciones
     */
    public List<Institution> getInstitutionsBy(User user);


    /**
     * Método encargado de obtener una lista con todas las instituciones
     * @return Lista de instituciones
     */
    public List<Institution> getAllInstitution();
}

