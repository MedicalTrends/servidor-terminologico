package cl.minsal.semantikos.ws.component;

import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.ws.fault.IllegalInputFault;
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

    @EJB
    private ConceptManager conceptManager;

    /**
     * Este método es responsable de incrementar el uso de una descripción.
     *
     * @param descriptionId El valor de negocio <em>DESCRIPTION_ID</em> de la descripción.
     * @return La descripción, con su contador de uso actualizado.
     */
    public DescriptionResponse incrementDescriptionHits(String descriptionId) throws IllegalInputFault {

        if (descriptionId == null || descriptionId.isEmpty()) {
            throw new IllegalInputFault("Debe ingresar un descriptionID");
        }

        /* Se incrementa la descripción */
        logger.debug("Por incrementar el contador de usos de la descripcion con DESCRIPCION_ID=" + descriptionId);
        Description description = descriptionManager.incrementDescriptionHits(descriptionId);

        /*
        ID Descripción Término buscado
        Término buscado
        Valor de contador
        ID Concepto
        Nombre Categoría
        */

        DescriptionResponse res = new DescriptionResponse(description);

        res.setModeled(null);
        res.setType(null);
        res.setCreatorUser(null);
        res.setCreationDate(null);
        res.setValid(null);
        res.setCaseSensitive(null);
        res.setAutogeneratedName(null);
        res.setValidityUntil(null);

        /* Y se retorna la respuesta */
        logger.debug("Descripcion con DESCRIPCION_ID=" + descriptionId + " tiene " + description.getUses() + " usos.");
        return res;
    }
}
