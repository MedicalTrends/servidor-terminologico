package cl.minsal.semantikos.ws.component;

import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.ws.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * @author Andres Farias on 2016-11-17.
 */
@Stateless
public class DescriptionController {

    /**
     * El logger para la clase
     */
    private static final Logger logger = LoggerFactory.getLogger(DescriptionController.class);

    @EJB
    private DescriptionManager descriptionManager;

    /**
     * Este método es responsable de incrementar el uso de una descripción.
     *
     * @param descriptionId El valor de negocio <em>DESCRIPTION_ID</em> de la descripción.
     * @return La descripción, con su contador de uso actualizado.
     */
    public DescriptionResponse incrementDescriptionHits(String descriptionId) {

        /* Se incrementa la descripción */
        logger.debug("Por incrementar el contador de usos de la descripcion con DESCRIPCION_ID=" + descriptionId);
        Description description = descriptionManager.incrementDescriptionHits(descriptionId);

        /* Y se retorna la respuesta */
        logger.debug("Descripcion con DESCRIPCION_ID=" + descriptionId + " tiene " + description.getUses() + " usos.");
        return new DescriptionResponse(description);
    }
}
