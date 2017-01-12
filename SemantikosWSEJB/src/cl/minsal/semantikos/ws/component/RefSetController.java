package cl.minsal.semantikos.ws.component;

import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.RefSetManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.RefSet;
import cl.minsal.semantikos.ws.fault.NotFoundFault;
import cl.minsal.semantikos.ws.mapping.RefSetMapper;
import cl.minsal.semantikos.ws.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Created by Development on 2016-11-18.
 */
@Stateless
public class RefSetController {

    private static final Logger logger = LoggerFactory.getLogger(RefSetController.class);

    @EJB
    private RefSetManager refSetManager;

    @EJB
    private ConceptManager conceptManager;
    @EJB
    private ConceptController conceptController;

    public List<RefSet> findRefSetsByDescriptions(@NotNull String descriptionId, Boolean includeInstitutions, String idStablishment) throws NotFoundFault {

        /* Se recupera el concepto asociado a la descripción */
        ConceptSMTK conceptByDescriptionID;
        try {
            conceptByDescriptionID = this.conceptManager.getConceptByDescriptionID(descriptionId);
        } catch (EJBException e) {
            throw new NotFoundFault("Descripcion no encontrada: " + descriptionId);
        }

        /* Se retornan los refsets asociados al concepto */
        return refSetManager.findByConcept(conceptByDescriptionID);
    }

    public List<RefSet> findRefsets(List<String> refSetsNames) throws NotFoundFault {

        logger.debug("RefSetController.findRefsets(" + refSetsNames + ")");

        /* Validacion de parámetros */
        List<RefSet> refSets;
        if (refSetsNames == null || refSetsNames.isEmpty()) {
            refSets = this.refSetManager.getAllRefSets();
            logger.debug("RefSetController.findRefsets(" + refSetsNames + ") --> " + refSets.size() + " refsets retornados.");
            return Collections.emptyList();
        }

        /* Se recuperan los refsets solicitados */
        try {
            refSets = this.refSetManager.findRefSetsByName(refSetsNames);
            logger.debug("RefSetController.findRefsets(" + refSetsNames + ") --> " + refSets.size() + " refsets retornados.");
        } catch (Exception e) {
            throw new NotFoundFault(e.getMessage());
        }

        return refSets;
    }

    public List<RefSetResponse> refSetList(Boolean includeAllInstitutions) throws NotFoundFault {
        List<RefSetResponse> res = new ArrayList<>();
        List<RefSet> refSets = this.refSetManager.getAllRefSets();
        if (refSets != null) {
            for (RefSet refSet : refSets) {
                res.add(this.getResponse(refSet));
            }
        }
        return res;
    }


    public RefSetResponse getResponse(RefSet refSet) throws NotFoundFault {
        if (refSet == null) {
            throw new NotFoundFault("RefSet no encontrado");
        }
        return RefSetMapper.map(refSet);
    }

}
