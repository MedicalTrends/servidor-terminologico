package cl.minsal.semantikos.kernel.componentsweb;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * Created by des01c7 on 09-01-17.
 */

@Remote
public interface TimeOutWeb {

    /**
     * Método encargado de traer el tiempo que dura la sesión desde la BD
     * @return
     */
    public int getTimeOut();
}
