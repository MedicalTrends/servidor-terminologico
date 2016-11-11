package cl.minsal.semantikos.ws.service;

import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.RefSetManager;
import cl.minsal.semantikos.kernel.daos.ConceptDAO;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.RefSet;
import cl.minsal.semantikos.ws.fault.NotFoundFault;
import cl.minsal.semantikos.ws.mapping.ConceptMapper;
import cl.minsal.semantikos.ws.mapping.RefSetMapper;
import cl.minsal.semantikos.ws.response.*;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Development on 2016-10-11.
 *
 */
@WebService
public class RefSetService {

    @EJB
    private RefSetManager refSetManager;
    @EJB
    private ConceptManager conceptManager;

    // REQ-WS-008
    @WebMethod(operationName = "listaRefSet")
    @WebResult(name = "refSet")
    public List<RefSetResponse> listaRefSet() {
        List<RefSetResponse> res = new ArrayList<>();
        List<RefSet> refSets = this.refSetManager.getAllRefSets();
        mapResults(res, refSets);
        return res;
    }

    // REQ-WS-022
    // REQ-WS-023
    @WebMethod(operationName = "conceptosPorRefSet")
    @WebResult(name = "conceptosPorRefSet")
    public ConceptsByRefsetResponse conceptosPorRefSet(
            @XmlElement(required = true)
            @WebParam(name = "nombreRefSet")
                    String refSetName,
            @XmlElement(required = false, defaultValue = "0")
            @WebParam(name = "numeroPagina")
                    Integer pageNumber,
            @XmlElement(required = false, defaultValue = "10")
            @WebParam(name = "tamanoPagina")
                    Integer pageSize
    ) throws NotFoundFault {
        ConceptsByRefsetResponse res = new ConceptsByRefsetResponse();

        RefSet refSet = this.refSetManager.getRefsetByName(refSetName);
        if ( refSet == null ) {
            throw new NotFoundFault("No se encuentra RefSet con ese nombre: " + refSetName);
        }
        res.setRefSet(RefSetMapper.map(refSet));

        List<ConceptResponse> conceptResponses = new ArrayList<>();
        List<ConceptSMTK> concepts = this.conceptManager.findModeledConceptsBy(refSet, pageNumber, pageSize);
        if ( concepts != null ) {
            conceptResponses = new ArrayList<>(concepts.size());
            for (ConceptSMTK conceptSMTK : concepts) {
                conceptManager.loadRelationships(conceptSMTK);
                ConceptResponse concept = ConceptMapper.map(conceptSMTK);
                ConceptMapper.appendDescriptions(concept, conceptSMTK);
                ConceptMapper.appendAttributes(concept, conceptSMTK);
                ConceptMapper.appendCategory(concept, conceptSMTK);
                conceptResponses.add(concept);
            }
        }
        res.setConcepts(conceptResponses);

        Integer count = this.conceptManager.countModeledConceptsBy(refSet);
        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setTotalCount(count);
        paginationResponse.setCurrentPage(pageNumber);
        paginationResponse.setPageSize(pageSize);
        if ( conceptResponses != null && !conceptResponses.isEmpty() ) {
            paginationResponse.setShowingFrom(pageNumber * pageSize);
            paginationResponse.setShowingTo(paginationResponse.getShowingFrom() + conceptResponses.size() - 1);
        }
        res.setPagination(paginationResponse);

        return res;
    }

    // REQ-WS-009
    @WebMethod(operationName = "refSetsPorIdDescripcion")
    @WebResult(name = "refSetsPorIdDescripcion")
    public RefSetsByDescriptionIdResponse refSetsPorIdDescripcion(
            @XmlElement(required = true)
            @WebParam(name = "idDescripcion")
                    List<String> descriptionId
    ) {
        return null;
    }

    private void mapResults(List<RefSetResponse> res, List<RefSet> refSets) {
        if ( refSets != null ) {
            for ( RefSet refSet : refSets ) {
                res.add(RefSetMapper.map(refSet));
            }
        }
    }

}
