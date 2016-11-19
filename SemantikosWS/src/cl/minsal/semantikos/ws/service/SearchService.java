package cl.minsal.semantikos.ws.service;

import cl.minsal.semantikos.ws.component.CategoryController;
import cl.minsal.semantikos.ws.component.ConceptController;
import cl.minsal.semantikos.ws.component.RefSetController;
import cl.minsal.semantikos.ws.fault.IllegalInputFault;
import cl.minsal.semantikos.ws.fault.NotFoundFault;
import cl.minsal.semantikos.ws.response.*;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
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
    @WebResult(name = "buscarTermino")
    public TermSearchResponse buscarTermino(
            @XmlElement(required = true)
            @WebParam(name = "termino")
                    String term,
            @XmlElement(required = false)
            @WebParam(name = "nombreCategoria")
                    List<String> categoryNames,
            @XmlElement(required = false)
            @WebParam(name = "nombreRefSet")
                    List<String> refSetNames,
            @XmlElement(required = false, defaultValue = "0")
            @WebParam(name = "numeroPagina")
                    Integer pageNumber,
            @XmlElement(required = false, defaultValue = "10")
            @WebParam(name = "tamanoPagina")
                    Integer pageSize
    ) throws IllegalInputFault {
        if ( (categoryNames == null && refSetNames == null)
                || (categoryNames.isEmpty() && refSetNames.isEmpty())) {
            throw new IllegalInputFault("Debe ingresar por lo menos una Categoría o un RefSet");
        }
        // TODO
        return null;
    }

    // REQ-WS-002
    @WebMethod(operationName = "conceptosPorCategoria")
    @WebResult(name = "conceptosPorCategoria")
    public ConceptsByCategoryResponse conceptosPorCategoria(
            @XmlElement(required = true)
            @WebParam(name = "nombreCategoria")
                    String categoryName,
            @XmlElement(required = false, defaultValue = "0")
            @WebParam(name = "numeroPagina")
                    Integer pageNumber,
            @XmlElement(required = false, defaultValue = "10")
            @WebParam(name = "tamanoPagina")
                    Integer pageSize
    ) throws NotFoundFault {
        return this.conceptController.conceptsByCategory(categoryName, pageNumber, pageSize);
    }

    @WebMethod(operationName = "listaCategorias")
    @WebResult(name = "categoria")
    public List<CategoryResponse> listaCategorias() throws NotFoundFault {
        return this.categoryController.categoryList();
    }

    // REQ-WS-004
    @WebMethod(operationName = "buscarTruncatePerfect")
    @WebResult(name = "buscarTruncatePerfect")
    public TermSearchResponse buscarTruncatePerfect(
            @XmlElement(required = true)
            @WebParam(name = "termino")
                    String term,
            @XmlElement(required = false)
            @WebParam(name = "nombreCategoria")
                    List<String> categoryNames,
            @XmlElement(required = false)
            @WebParam(name = "nombreRefSet")
                    List<String> refSetNames,
            @XmlElement(required = false, defaultValue = "0")
            @WebParam(name = "numeroPagina")
                    Integer pageNumber,
            @XmlElement(required = false, defaultValue = "10")
            @WebParam(name = "tamanoPagina")
                    Integer pageSize
    ) throws IllegalInputFault {
        if ( (categoryNames == null && refSetNames == null)
                || (categoryNames.isEmpty() && refSetNames.isEmpty())) {
            throw new IllegalInputFault("Debe ingresar por lo menos una Categoría o un RefSet");
        }
        // TODO
        return null;
    }

    // REQ-WS-005
    @WebMethod(operationName = "obtenerTerminosPedibles")
    @WebResult(name = "obtenerTerminosPedibles")
    public TermSearchResponse obtenerTerminosPedibles(
            @XmlElement(required = false)
            @WebParam(name = "nombreCategoria")
                    List<String> categoryNames,
            @XmlElement(required = false)
            @WebParam(name = "nombreRefSet")
                    List<String> refSetNames,
            @XmlElement(required = true)
            @WebParam(name = "pedible")
                    String requestable,
            @XmlElement(required = false, defaultValue = "0")
            @WebParam(name = "numeroPagina")
                    Integer pageNumber,
            @XmlElement(required = false, defaultValue = "10")
            @WebParam(name = "tamanoPagina")
                    Integer pageSize
    ) throws IllegalInputFault {
        if ( (categoryNames == null && refSetNames == null)
                || (categoryNames.isEmpty() && refSetNames.isEmpty())) {
            throw new IllegalInputFault("Debe ingresar por lo menos una Categoría o un RefSet");
        }
        // TODO
        return null;
    }

    // REQ-WS-007
    // REQ-WS-009
    @WebMethod(operationName = "refSetsPorIdDescripcion")
    @WebResult(name = "refSetsPorIdDescripcion")
    public RefSetsByDescriptionIdResponse refSetsPorIdDescripcion(
            @XmlElement(required = true)
            @WebParam(name = "idDescripcion")
                    List<String> descriptionId,
            @XmlElement(required = false, defaultValue = "true")
            @WebParam(name = "incluyeEstablecimientos")
                    Boolean includeInstitutions
    ) {
        // TODO
        return null;
    }

    // REQ-WS-008
    @WebMethod(operationName = "listaRefSet")
    @WebResult(name = "refSet")
    public List<RefSetResponse> listaRefSet(
            @XmlElement(required = false, defaultValue = "true")
            @WebParam(name = "incluyeEstablecimientos")
                    Boolean includeInstitutions
    ) throws NotFoundFault {
        return this.refSetController.refSetList(includeInstitutions);
    }

    // REQ-WS-022
    @WebMethod(operationName = "descripcionesPreferidasPorRefSet")
    @WebResult(name = "descripcionesPreferidasPorRefSet")
    public ConceptsByRefsetResponse descripcionesPreferidasPorRefSet(
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
        return this.conceptController.conceptsByRefsetWithPreferedDescriptions(refSetName, pageNumber, pageSize);
    }

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
        return this.conceptController.conceptsByRefset(refSetName, pageNumber, pageSize);
    }

    // REQ-WS-028
    @WebMethod(operationName = "conceptoPorIdDescripcion")
    @WebResult(name = "concepto")
    public ConceptResponse conceptoPorIdDescripcion(
            @XmlElement(required = true)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws NotFoundFault {
        return this.conceptController.conceptByDescriptionId(descriptionId);
    }

}
