package cl.minsal.semantikos.ws.service;

import cl.minsal.semantikos.ws.fault.IllegalInputFault;
import cl.minsal.semantikos.ws.request.DescriptionsSuggestionsRequest;
import cl.minsal.semantikos.ws.request.RelatedConceptsByCategoryRequest;
import cl.minsal.semantikos.ws.request.RelatedConceptsRequest;
import cl.minsal.semantikos.ws.response.RelatedConceptsResponse;
import cl.minsal.semantikos.ws.response.TermSearchResponse;

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
@WebService(serviceName = "ServicioDeRelacionados")
public class RelatedService {

    // REQ-WS-006
    @WebMethod(operationName = "sugerenciasDeDescripciones")
    public TermSearchResponse sugerenciasDeDescripciones(
            @XmlElement(required = true)
            @WebParam(name = "peticionSugerenciasDeDescripciones")
                    DescriptionsSuggestionsRequest request
    ) throws IllegalInputFault {
        if ( (request.getCategoryNames() == null && request.getRefSetNames() == null)
                || (request.getCategoryNames().isEmpty() && request.getRefSetNames().isEmpty())) {
            throw new IllegalInputFault("Debe ingresar por lo menos una Categoría o un RefSet");
        }
        // TODO
        return null;
    }

    // REQ-WS-010...021
    @WebMethod(operationName = "conceptosRelacionados")
    public RelatedConceptsResponse conceptosRelacionados(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionados")
                    RelatedConceptsRequest request
    ) throws IllegalInputFault {
        if ( (request.getConceptId() == null || "".equals(request.getConceptId()) )
                && (request.getConceptId() == null || "".equals(request.getDescriptionId())) ) {
            throw new IllegalInputFault("Debe ingresar un idConcepto o idDescripcion");
        }
        // TODO
        return null;
    }

    // REQ-WS-010...021 Lite
    @WebMethod(operationName = "conceptosRelacionadosLite")
    public RelatedConceptsResponse conceptosRelacionadosLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionados")
                    RelatedConceptsRequest request
    ) throws IllegalInputFault {
        if ( (request.getConceptId() == null || "".equals(request.getConceptId()) )
                && (request.getConceptId() == null || "".equals(request.getDescriptionId())) ) {
            throw new IllegalInputFault("Debe ingresar un idConcepto o idDescripcion");
        }
        // TODO
        return null;
    }

    // REQ-WS-010
    // REQ-WS-011
    @WebMethod(operationName = "obtenerMedicamentoClinico")
    public RelatedConceptsResponse obtenerMedicamentoClinico(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-010 Lite
    // REQ-WS-011 Lite
    @WebMethod(operationName = "obtenerMedicamentoClinicoLite")
    public RelatedConceptsResponse obtenerMedicamentoClinicoLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-010
    @WebMethod(operationName = "obtenerMedicamentoClinicoPorMedicamentoBasico")
    public RelatedConceptsResponse obtenerMedicamentoClinicoPorMedicamentoBasico(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-010.1-Lite
    @WebMethod(operationName = "obtenerMedicamentoClinicoPorMedicamentoBasicoLite")
    public RelatedConceptsResponse obtenerMedicamentoClinicoPorMedicamentoBasicoLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-011
    @WebMethod(operationName = "obtenerMedicamentoClinicoPorProductoComercial")
    public RelatedConceptsResponse obtenerMedicamentoClinicoPorProductoComercial(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-011.1-Lite
    @WebMethod(operationName = "obtenerMedicamentoClinicoPorProductoComercialLite")
    public RelatedConceptsResponse obtenerMedicamentoClinicoPorProductoComercialLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-012
    @WebMethod(operationName = "obtenerMedicamentoBasico")
    public RelatedConceptsResponse obtenerMedicamentoBasico(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-012.1-Lite
    @WebMethod(operationName = "obtenerMedicamentoBasicoLite")
    public RelatedConceptsResponse obtenerMedicamentoBasicoLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-012
    @WebMethod(operationName = "obtenerMedicamentoBasicoPorMedicamentoClinico")
    public RelatedConceptsResponse obtenerMedicamentoBasicoPorMedicamentoClinico(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-012.1-Lite
    @WebMethod(operationName = "obtenerMedicamentoBasicoPorMedicamentoClinicoLite")
    public RelatedConceptsResponse obtenerMedicamentoBasicoPorMedicamentoClinicoLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-013
    // REQ-WS-017
    @WebMethod(operationName = "obtenerProductoComercial")
    public RelatedConceptsResponse obtenerProductoComercial(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-013 Lite
    // REQ-WS-017 Lite
    @WebMethod(operationName = "obtenerProductoComercialLite")
    public RelatedConceptsResponse obtenerProductoComercialLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-013
    @WebMethod(operationName = "obtenerProductoComercialPorMedicamentoClinico")
    public RelatedConceptsResponse obtenerProductoComercialPorMedicamentoClinico(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-013.1-Lite
    @WebMethod(operationName = "obtenerProductoComercialPorMedicamentoClinicoLite")
    public RelatedConceptsResponse obtenerProductoComercialPorMedicamentoClinicoLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-017
    @WebMethod(operationName = "obtenerProductoComercialPorFamiliaProducto")
    public RelatedConceptsResponse obtenerProductoComercialPorFamiliaProducto(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-017.1-Lite
    @WebMethod(operationName = "obtenerProductoComercialPorFamiliaProductoLite")
    public RelatedConceptsResponse obtenerProductoComercialPorFamiliaProductoLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-014
    @WebMethod(operationName = "obtenerMedicamentoClinicoConEnvase")
    public RelatedConceptsResponse obtenerMedicamentoClinicoConEnvase(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-014 Lite
    @WebMethod(operationName = "obtenerMedicamentoClinicoConEnvaseLite")
    public RelatedConceptsResponse obtenerMedicamentoClinicoConEnvaseLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-014
    @WebMethod(operationName = "obtenerMedicamentoClinicoConEnvasePorMedicamentoClinico")
    public RelatedConceptsResponse obtenerMedicamentoClinicoConEnvasePorMedicamentoClinico(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-014.1 Lite
    @WebMethod(operationName = "obtenerMedicamentoClinicoConEnvasePorMedicamentoClinicoLite")
    public RelatedConceptsResponse obtenerMedicamentoClinicoConEnvasePorMedicamentoClinicoLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-015
    @WebMethod(operationName = "obtenerProductoComercialConEnvase")
    public RelatedConceptsResponse obtenerProductoComercialConEnvase(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-015 Lite
    @WebMethod(operationName = "obtenerProductoComercialConEnvaseLite")
    public RelatedConceptsResponse obtenerProductoComercialConEnvaseLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-015
    @WebMethod(operationName = "obtenerProductoComercialConEnvasePorMedicamentoClinicoConEnvase")
    public RelatedConceptsResponse obtenerProductoComercialConEnvasePorMedicamentoClinicoConEnvase(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-015.1 Lite
    @WebMethod(operationName = "obtenerProductoComercialConEnvasePorMedicamentoClinicoConEnvaseLite")
    public RelatedConceptsResponse obtenerProductoComercialConEnvasePorMedicamentoClinicoConEnvaseLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-016
    @WebMethod(operationName = "obtenerFamiliaProducto")
    public RelatedConceptsResponse obtenerFamiliaProducto(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-016
    @WebMethod(operationName = "obtenerFamiliaProductoPorGrupoFamiliaProducto")
    public RelatedConceptsResponse obtenerFamiliaProductoPorGrupoFamiliaProducto(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-018
    @WebMethod(operationName = "obtenerProductoClinicoConEnvase")
    public RelatedConceptsResponse obtenerProductoClinicoConEnvase(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-018 Lite
    @WebMethod(operationName = "obtenerProductoClinicoConEnvaseLite")
    public RelatedConceptsResponse obtenerProductoClinicoConEnvaseLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-018
    @WebMethod(operationName = "obtenerProductoClinicoConEnvasePorProductoComercial")
    public RelatedConceptsResponse obtenerProductoClinicoConEnvasePorProductoComercial(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-018.1 Lite
    @WebMethod(operationName = "obtenerProductoClinicoConEnvasePorProductoComercialLite")
    public RelatedConceptsResponse obtenerProductoClinicoConEnvasePorProductoComercialLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-019
    @WebMethod(operationName = "obtenerSustancia")
    public RelatedConceptsResponse obtenerSustancia(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-019
    @WebMethod(operationName = "obtenerSustanciaPorMedicamentoBasico")
    public RelatedConceptsResponse obtenerSustanciaPorMedicamentoBasico(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        // TODO
        return null;
    }

    // REQ-WS-020
    @WebMethod(operationName = "obtenerRegistroISP")
    @WebResult(name = "obtenerRegistroISP")
    public RelatedConceptsResponse obtenerRegistroISP(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        return null;
    }

    // REQ-WS-020
    @WebMethod(operationName = "obtenerRegistroISPPorProductoComercial")
    public RelatedConceptsResponse obtenerRegistroISPPorProductoComercial(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        return null;
    }

    // REQ-WS-021
    @WebMethod(operationName = "obtenerBioequivalentes")
    public RelatedConceptsResponse obtenerBioequivalentes(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        return null;
    }

    // REQ-WS-021
    @WebMethod(operationName = "obtenerBioequivalentesPorProductoComercial")
    public RelatedConceptsResponse obtenerBioequivalentesPorProductoComercial(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault {
        return null;
    }

}
