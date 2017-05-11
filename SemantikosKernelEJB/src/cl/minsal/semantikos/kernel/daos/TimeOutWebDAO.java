package cl.minsal.semantikos.kernel.daos;

import javax.ejb.Local;

/**
 * Created by des01c7 on 09-01-17.
 */
@Local
public interface TimeOutWebDAO {

    /**
     * Método encargado de traer el tiempo que dura la sesión desde la BD
     * @return
     */
    public int getTimeOut();
}
