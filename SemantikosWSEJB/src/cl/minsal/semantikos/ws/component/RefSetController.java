package cl.minsal.semantikos.ws.component;

import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.RefSetManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.RefSet;
import cl.minsal.semantikos.ws.fault.NotFoundFault;
import cl.minsal.semantikos.ws.mapping.RefSetMapper;
import cl.minsal.semantikos.ws.response.ConceptResponse;
import cl.minsal.semantikos.ws.response.DescriptionResponse;
import cl.minsal.semantikos.ws.response.RefSetResponse;
import cl.minsal.semantikos.ws.response.TermSearchResponse;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.*;

/**
 * Created by Development on 2016-11-18.
 *
 */
@Stateless
public class RefSetController {

    @EJB
    private RefSetManager refSetManager;

    @EJB
    private ConceptManager conceptManager;

    @EJB
    private ConceptController conceptController;

    public TermSearchResponse findRefSetsByDescriptionIds(List<String> descriptionIds, Boolean includeInstitutions) throws NotFoundFault {
        TermSearchResponse res = new TermSearchResponse();
        // TODO: include institutions

        Set<ConceptSMTK> conceptSMTKS = new HashSet<>(descriptionIds.size());
        for ( String descriptionId : descriptionIds ) {
            ConceptSMTK found = this.conceptManager.getConceptByDescriptionID(descriptionId);
            if ( found != null ) {
                conceptSMTKS.add(found);
            } else {
                throw new NotFoundFault("Descripcion no encontrada: " + descriptionId);
            }
        }

        List<ConceptResponse> conceptResponses = new ArrayList<>(conceptSMTKS.size());
        for ( ConceptSMTK conceptSMTK : conceptSMTKS ) {
            ConceptResponse conceptResponse = new ConceptResponse(conceptSMTK);
            this.conceptController.loadRefSets(conceptResponse, conceptSMTK);

            List<DescriptionResponse> toRemove = new ArrayList<>();
            for (DescriptionResponse descriptionResponse : conceptResponse.getDescriptions() ) {
                if ( !descriptionIds.contains(descriptionResponse.getDescriptionID()) ) {
                    toRemove.add(descriptionResponse);
                }
            }
            conceptResponse.getDescriptions().removeAll(toRemove);

            conceptResponses.add(conceptResponse);
        }
        res.setConcepts(conceptResponses);

        return res;
    }

    public List<RefSet> findRefsets(List<String> refSetsNames) throws NotFoundFault {
        // TODO: Seguridad
        List<RefSet> refSets;
        if ( refSetsNames != null && !refSetsNames.isEmpty() ) {
            try {
                refSets = this.refSetManager.findRefSetsByName(refSetsNames);
            } catch (Exception e) {
                throw new NotFoundFault(e.getMessage());
            }
        } else {
            refSets = this.refSetManager.getAllRefSets();
        }
        return refSets;
    }

    public List<RefSetResponse> refSetList(Boolean includeAllInstitutions) throws NotFoundFault {
        List<RefSetResponse> res = new ArrayList<>();
        List<RefSet> refSets = this.refSetManager.getAllRefSets();
        if ( refSets != null ) {
            for ( RefSet refSet : refSets ) {
                res.add(this.getResponse(refSet));
            }
        }
        // TODO includeAllInstitutions
        return res;
    }


    public RefSetResponse getResponse(RefSet refSet) throws NotFoundFault {
        if ( refSet == null ) {
            throw new NotFoundFault("RefSet no encontrado");
        }
        return RefSetMapper.map(refSet);
    }

}
