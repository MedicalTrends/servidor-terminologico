package cl.minsal.semantikos.ws.component;

import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.InstitutionManager;
import cl.minsal.semantikos.kernel.components.RefSetManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.RefSet;
import cl.minsal.semantikos.model.users.Institution;
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
    private InstitutionManager institutionManager;

    @EJB
    private ConceptController conceptController;

    public List<RefSetSearchResponse> findRefSetsByDescriptions(List<String> descriptionIds, Boolean includeInstitutions, String idStablishment) throws NotFoundFault {

        /* Se recupera el concepto asociado a la descripción */
        List<ConceptSMTK> conceptsByDescriptionID = new ArrayList<>();

        for (String descriptionId : descriptionIds) {
            try {
                conceptsByDescriptionID.add(this.conceptManager.getConceptByDescriptionID(descriptionId));
            }
            catch (EJBException e) {
                throw new NotFoundFault("Descripcion no encontrada: " + descriptionId);
            }
        }

        Institution connectionInstitution = institutionManager.getInstitutionById(Long.parseLong(idStablishment));

        List<RefSetSearchResponse> res = new ArrayList<>();

        for (ConceptSMTK conceptByDescriptionID : conceptsByDescriptionID) {
            RefSetSearchResponse refSetSearchResponse = new RefSetSearchResponse();
            List<RefSet> refSets = refSetManager.findByConcept(conceptByDescriptionID);

            Iterator<RefSet> it = refSets.iterator();

            while (it.hasNext()) {
                RefSet refSet = it.next();
                if(!refSet.isValid()) {
                    it.remove();
                }
            }

            List<String> validInstitutions = new ArrayList<>();

            if(!connectionInstitution.getName().equals("MINSAL") && includeInstitutions ) {
                validInstitutions = Arrays.asList(new String[]{"MINSAL", connectionInstitution.getName()});
            }

            if(!connectionInstitution.getName().equals("MINSAL") && !includeInstitutions ) {
                validInstitutions = Arrays.asList(new String[]{connectionInstitution.getName()});
            }

            if(connectionInstitution.getName().equals("MINSAL") && !includeInstitutions ) {
                validInstitutions = Arrays.asList(new String[]{"MINSAL"});
            }

            it = refSets.iterator();

            if(!validInstitutions.isEmpty()) {
                while (it.hasNext()) {
                    RefSet refSet = it.next();
                    if(!validInstitutions.contains(refSet.getInstitution().getName())) {
                        it.remove();
                    }
                }
            }

            refSetSearchResponse.setConceptId(conceptByDescriptionID.getConceptID());
            refSetSearchResponse.setDescriptionId(conceptByDescriptionID.getDescriptionFavorite().getDescriptionId());
            refSetSearchResponse.setDescription(conceptByDescriptionID.getDescriptionFavorite().getTerm());
            refSetSearchResponse.setCategory(conceptByDescriptionID.getCategory().getName());
            refSetSearchResponse.setRefSetsResponse(new RefSetsResponse(refSets));

            res.add(refSetSearchResponse);
        }

        return res;
    }

    public List<RefSet> findRefsets(List<String> refSetsNames) throws NotFoundFault {

        logger.debug("RefSetController.findRefsets(" + refSetsNames + ")");

        /* Validacion de parámetros */
        List<RefSet> refSets;
        if (refSetsNames == null || refSetsNames.isEmpty()) {
            refSets = this.refSetManager.getAllRefSets();
            logger.debug("RefSetController.findRefsets(" + refSetsNames + ") --> " + refSets.size() + " refsets " +
                    "retornados.");
            return Collections.emptyList();
        }

        /* Se recuperan los refsets solicitados */
        try {
            refSets = this.refSetManager.findRefSetsByName(refSetsNames);
            logger.debug("RefSetController.findRefsets(" + refSetsNames + ") --> " + refSets.size() + " refsets " +
                    "retornados.");
        } catch (Exception e) {
            throw new NotFoundFault(e.getMessage());
        }

        return refSets;
    }

    /**
     * TODO: Claramente este método no fue implementado correctamente.
     *
     * @param includeInstitutions El parametro no considerado
     * @return La lista de RefSets encapsulada.
     * @throws NotFoundFault Arrojada si no ... ???
     */
    public RefSetsResponse refSetList(Boolean includeInstitutions, String idStablishment) throws NotFoundFault {

        Institution connectionInstitution = institutionManager.getInstitutionById(Long.parseLong(idStablishment));

        List<RefSet> refSets = this.refSetManager.getAllRefSets();

        Iterator<RefSet> it = refSets.iterator();

        while (it.hasNext()) {
            RefSet refSet = it.next();
            if(!refSet.isValid()) {
                it.remove();
            }
        }

        List<String> validInstitutions = new ArrayList<>();

        if(!connectionInstitution.getName().equals("MINSAL") && includeInstitutions ) {
            validInstitutions = Arrays.asList(new String[]{"MINSAL", connectionInstitution.getName()});
        }

        if(!connectionInstitution.getName().equals("MINSAL") && !includeInstitutions ) {
            validInstitutions = Arrays.asList(new String[]{connectionInstitution.getName()});
        }

        if(connectionInstitution.getName().equals("MINSAL") && !includeInstitutions ) {
            validInstitutions = Arrays.asList(new String[]{"MINSAL"});
        }

        it = refSets.iterator();

        if(!validInstitutions.isEmpty()) {
            while (it.hasNext()) {
                RefSet refSet = it.next();
                if(!validInstitutions.contains(refSet.getInstitution().getName())) {
                    it.remove();
                }
            }
        }

        return new RefSetsResponse(refSets);
    }

    public RefSetResponse getResponse(RefSet refSet) throws NotFoundFault {
        if (refSet == null) {
            throw new NotFoundFault("RefSet no encontrado");
        }
        return RefSetMapper.map(refSet);
    }

}
