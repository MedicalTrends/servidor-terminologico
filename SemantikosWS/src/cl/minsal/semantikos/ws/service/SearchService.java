package cl.minsal.semantikos.ws.service;

import cl.minsal.semantikos.ws.component.CategoryController;
import cl.minsal.semantikos.ws.component.ConceptController;
import cl.minsal.semantikos.ws.component.RefSetController;
import cl.minsal.semantikos.ws.fault.IllegalInputFault;
import cl.minsal.semantikos.ws.fault.NotFoundFault;
import cl.minsal.semantikos.ws.request.*;
import cl.minsal.semantikos.ws.response.*;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by Development on 2016-11-18.
 *
 */
@WebService(serviceName = "ServicioDeBusqueda")
public class SearchService {

    @EJB
    private ConceptController conceptController;
    @EJB
    private CategoryController categoryController;
    @EJB
    private RefSetController refSetController;

    // REQ-WS-001
    @WebMethod(operationName = "buscarTermino")
    public TermSearchResponse buscarTermino(
        @XmlElement(required = true)
        @WebParam(name = "peticionBuscarTermino")
                SearchTermRequest request
    ) throws IllegalInputFault {
        if ( (request.getCategoryNames() == null && request.getRefSetNames() == null)
                || (request.getCategoryNames().isEmpty() && request.getRefSetNames().isEmpty())) {
            throw new IllegalInputFault("Debe ingresar por lo menos una Categoría o un RefSet");
        }
        // TODO
        return null;
    }

    // REQ-WS-002
    @WebMethod(operationName = "conceptosPorCategoria")
    public ConceptsByCategoryResponse conceptosPorCategoria(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosPorCategoria")
                    ConceptsByCategoryRequest request
    ) throws NotFoundFault {
        return this.conceptController.conceptsByCategory(request.getCategoryName(), request.getPageNumber(), request.getPageSize());
    }

    @WebMethod(operationName = "listaCategorias")
    public List<CategoryResponse> listaCategorias() throws NotFoundFault {
        return this.categoryController.categoryList();
    }

    // REQ-WS-004
    @WebMethod(operationName = "buscarTruncatePerfect")
    public TermSearchResponse buscarTruncatePerfect(
            @XmlElement(required = true)
            @WebParam(name = "peticionBuscarTermino")
                    SearchTermRequest request
    ) throws IllegalInputFault {
        if ( (request.getCategoryNames() == null && request.getRefSetNames() == null)
                || (request.getCategoryNames().isEmpty() && request.getRefSetNames().isEmpty())) {
            throw new IllegalInputFault("Debe ingresar por lo menos una Categoría o un RefSet");
        }
        // TODO
        return null;
    }

    // REQ-WS-005
    @WebMethod(operationName = "obtenerTerminosPedibles")
    public TermSearchResponse obtenerTerminosPedibles(
            @XmlElement(required = true)
            @WebParam(name = "peticionObtenerTerminosPedibles")
                    GetRequestableTermsRequest request
    ) throws IllegalInputFault {
        if ( (request.getCategoryNames() == null && request.getRefSetNames() == null)
                || (request.getCategoryNames().isEmpty() && request.getRefSetNames().isEmpty())) {
            throw new IllegalInputFault("Debe ingresar por lo menos una Categoría o un RefSet");
        }
        // TODO
        return null;
    }

    // REQ-WS-007
    // REQ-WS-009
    @WebMethod(operationName = "refSetsPorIdDescripcion")
    public RefSetsByDescriptionIdResponse refSetsPorIdDescripcion(
            @XmlElement(required = true)
            @WebParam(name = "peticionRefSetsPorIdDescripcion")
                    RefSetsByDescriptionIdRequest request
    ) {
        // TODO
        return null;
    }

    // REQ-WS-008
    @WebMethod(operationName = "listaRefSet")
    public List<RefSetResponse> listaRefSet(
            @XmlElement(required = false, defaultValue = "true")
            @WebParam(name = "incluyeEstablecimientos")
                    Boolean includeInstitutions
    ) throws NotFoundFault {
        return this.refSetController.refSetList(includeInstitutions);
    }

    // REQ-WS-022
    @WebMethod(operationName = "descripcionesPreferidasPorRefSet")
    public ConceptsByRefsetResponse descripcionesPreferidasPorRefSet(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosPorRefSet")
                    ConceptsByRefsetRequest request
    ) throws NotFoundFault {
        return this.conceptController.conceptsByRefsetWithPreferedDescriptions(request.getRefSetName(), request.getPageNumber(), request.getPageSize());
    }

    // REQ-WS-023
    @WebMethod(operationName = "conceptosPorRefSet")
    public ConceptsByRefsetResponse conceptosPorRefSet(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosPorRefSet")
                    ConceptsByRefsetRequest request
    ) throws NotFoundFault {
        return this.conceptController.conceptsByRefset(request.getRefSetName(), request.getPageNumber(), request.getPageSize());
    }

    // REQ-WS-028
    @WebMethod(operationName = "conceptoPorIdDescripcion")
    public ConceptResponse conceptoPorIdDescripcion(
            @XmlElement(required = true)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws NotFoundFault {
        return this.conceptController.conceptByDescriptionId(descriptionId);
    }

}
