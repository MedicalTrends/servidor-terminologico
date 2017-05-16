package cl.minsal.semantikos.kernel.components;

import javax.ejb.Local;
import javax.ejb.Remote;
import java.util.Map;

/**
 * @author Francisco Mendez on 16-11-2016.
 */
@Remote
public interface ISPFetcher {

    /**
     * Obtiene mapa con datos del la pagina del ISP a partir del id de registro
     * @param registro
     * @return
     */
    Map<String,String> getISPData(String registro);
}
