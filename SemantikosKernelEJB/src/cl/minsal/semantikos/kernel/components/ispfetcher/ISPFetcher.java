package cl.minsal.semantikos.kernel.components.ispfetcher;

import javax.ejb.Local;
import java.util.Map;

/**
 * @author Francisco Mendez on 16-11-2016.
 */
@Local
public interface ISPFetcher {

    /**
     * Obtiene mapa con datos del la pagina del ISP a partir del id de registro
     * @param registro
     * @return
     */
    Map<String,String> getISPData(String registro);
}
