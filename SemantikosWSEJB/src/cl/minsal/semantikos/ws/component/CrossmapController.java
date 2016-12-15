package cl.minsal.semantikos.ws.component;

import cl.minsal.semantikos.kernel.components.CrossmapsManager;
import cl.minsal.semantikos.kernel.components.DescriptionManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.ws.response.IndirectCrossMapSearchResponse;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author Andrés Farías on 12/13/16.
 */
@Stateless
public class CrossmapController {

    @EJB
    private CrossmapsManager crossmapManager;

    @EJB
    private DescriptionManager descriptionManager;

    /**
     * Este método es responsable de recuperar los crossmaps indirectos asociados al concepto cuya descripción posee el
     * identificador de negocio dado como parámetro.
     *
     * @param descriptionId El identificador de negocio <em>DESCRIPTION_ID</em> de la descripción.
     *
     * @return La respuesta XML con la lista de los crossmaps indirectos asociados al concepto de la descripción
     * indicada.
     */
    public IndirectCrossMapSearchResponse getIndirectCrossmapsByDescriptionID(String descriptionId) {

        /* Se recupera la descripción a partir de su identificador de negocio, y luego el concepto en la que se encuentra */
        Description theDescription = descriptionManager.getDescriptionByDescriptionID(descriptionId);
        ConceptSMTK conceptSMTK = theDescription.getConceptSMTK();

        /* Luego se recuperan los crossmaps indirectos del concepto */
        List<IndirectCrossmap> indirectCrossmaps = crossmapManager.getIndirectCrossmaps(conceptSMTK);

        return new IndirectCrossMapSearchResponse(indirectCrossmaps);
    }
}
