package cl.minsal.semantikos.kernel.components_web;

import javax.ejb.Local;

/**
 * Created by des01c7 on 09-01-17.
 */

@Local
public interface TimeOutWeb {

    /**
     * Método encargado de traer el tiempo que dura la sesión desde la BD
     * @return
     */
    public int getTimeOut();
}
