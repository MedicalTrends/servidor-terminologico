package cl.minsal.semantikos.ws.service;

import cl.minsal.semantikos.kernel.auth.AuthenticationManager;
import cl.minsal.semantikos.ws.component.ConceptController;
import cl.minsal.semantikos.ws.fault.IllegalInputFault;
import cl.minsal.semantikos.ws.fault.NotFoundFault;
import cl.minsal.semantikos.ws.request.DescriptionsSuggestionsRequest;
import cl.minsal.semantikos.ws.request.RelatedConceptsByCategoryRequest;
import cl.minsal.semantikos.ws.request.RelatedConceptsRequest;
import cl.minsal.semantikos.ws.response.*;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.WebServiceContext;
import java.util.List;

/**
 * Created by Development on 2016-11-18.
 *
 */
@WebService(serviceName = "ServicioDeRelacionados")
public class RelatedService {

    @EJB
    private ConceptController conceptController;

    @EJB
    private AuthenticationManager authenticationManager;

    @Resource
    WebServiceContext wsctx;

    //Inicializacion del Bean
    //@PostConstruct
    @AroundInvoke
    protected Object authenticate(InvocationContext ctx) throws Exception {

        try {
            authenticationManager.authenticate(wsctx.getMessageContext());
        }
        catch (Exception e) {
            throw new NotFoundFault(e.getMessage());
        }
        return ctx.proceed();
    }

    // REQ-WS-006
    @WebResult(name = "respuestaBuscarTermino")
    @WebMethod(operationName = "sugerenciasDeDescripciones")
    public SuggestedDescriptionsResponse sugerenciasDeDescripciones(
            @XmlElement(required = true)
            @WebParam(name = "peticionSugerenciasDeDescripciones")
            DescriptionsSuggestionsRequest request
    ) throws IllegalInputFault, NotFoundFault {
        if ((request.getCategoryNames() == null || request.getCategoryNames().isEmpty())) {
            throw new IllegalInputFault("Debe ingresar por lo menos una Categoría");
        }
        if (request.getTerm() == null || "".equals(request.getTerm())) {
            throw new IllegalInputFault("Debe ingresar un Termino a buscar");
        }
        if ( request.getTerm().length() < 3 ) {
            throw new IllegalInputFault("El termino a buscar debe tener minimo 3 caracteres de largo");
        }

        return this.conceptController.searchSuggestedDescriptions(request.getTerm(), request.getCategoryNames());
        //return this.conceptController.searchTruncatePerfect(request.getTerm(), request.getCategoryNames());
    }

    // REQ-WS-010...021
    @WebResult(name = "respuestaConceptosRelacionados")
    @WebMethod(operationName = "conceptosRelacionados")
    public RelatedConceptsResponse conceptosRelacionados(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
            RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault, NotFoundFault {

        /* Validación de parámetros */
        if ((request.getConceptId() == null || "".equals(request.getConceptId()))
                && (request.getDescriptionId() == null || "".equals(request.getDescriptionId()))) {
            throw new IllegalInputFault("Debe ingresar un idConcepto o idDescripcion");
        }

        /* Se realiza la búsqueda */
        return this.conceptController.findRelated(request.getDescriptionId(), request.getConceptId(), request.getRelatedCategoryName());
    }

    // REQ-WS-010...021 Lite
    @WebResult(name = "respuestaConceptosRelacionados")
    @WebMethod(operationName = "conceptosRelacionadosLite")
    public RelatedConceptsLiteResponse conceptosRelacionadosLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionadosPorCategoria")
            RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault, NotFoundFault {
        if ((request.getConceptId() == null || "".equals(request.getConceptId()))
                && (request.getDescriptionId() == null || "".equals(request.getDescriptionId()))) {
            throw new IllegalInputFault("Debe ingresar un idConcepto o idDescripcion");
        }
        return this.conceptController.findRelatedLite(request.getConceptId(), request.getDescriptionId(), request.getRelatedCategoryName());
    }

    // REQ-WS-010
    // REQ-WS-011
    @WebResult(name = "respuestaConceptosRelacionados")
    @WebMethod(operationName = "obtenerMedicamentoClinico")
    public RelatedConceptsResponse obtenerMedicamentoClinico(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionados")
            RelatedConceptsRequest request
    ) throws IllegalInputFault, NotFoundFault {
        return this.conceptosRelacionados(makeRequest(request, "Fármacos - Medicamento Clínico"));
    }

    // REQ-WS-010 Lite
    // REQ-WS-011 Lite
    @WebResult(name = "respuestaConceptosRelacionados")
    @WebMethod(operationName = "obtenerMedicamentoClinicoLite")
    public RelatedConceptsLiteResponse obtenerMedicamentoClinicoLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionados")
            RelatedConceptsRequest request
    ) throws IllegalInputFault, NotFoundFault {
        return this.conceptosRelacionadosLite(makeRequest(request, "Fármacos - Medicamento Clínico"));
    }

    // REQ-WS-012
    @WebResult(name = "respuestaConceptosRelacionados")
    @WebMethod(operationName = "obtenerMedicamentoBasico")
    public RelatedConceptsResponse obtenerMedicamentoBasico(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionados")
            RelatedConceptsRequest request
    ) throws IllegalInputFault, NotFoundFault {
        return this.conceptosRelacionados(makeRequest(request, "Fármacos - Medicamento Básico"));
    }

    // REQ-WS-012.1-Lite
    @WebResult(name = "respuestaConceptosRelacionados")
    @WebMethod(operationName = "obtenerMedicamentoBasicoLite")
    public RelatedConceptsLiteResponse obtenerMedicamentoBasicoLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionados")
            RelatedConceptsRequest request
    ) throws IllegalInputFault, NotFoundFault {
        return this.conceptosRelacionadosLite(makeRequest(request, "Fármacos - Medicamento Básico"));
    }

    // REQ-WS-013
    // REQ-WS-017
    @WebResult(name = "respuestaConceptosRelacionados")
    @WebMethod(operationName = "obtenerProductoComercial")
    public RelatedConceptsResponse obtenerProductoComercial(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionados")
            RelatedConceptsRequest request
    ) throws IllegalInputFault, NotFoundFault {
        return this.conceptosRelacionados(makeRequest(request, "Fármacos - Producto Comercial"));
    }

    // REQ-WS-013 Lite
    // REQ-WS-017 Lite
    @WebResult(name = "respuestaConceptosRelacionados")
    @WebMethod(operationName = "obtenerProductoComercialLite")
    public RelatedConceptsLiteResponse obtenerProductoComercialLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionados")
            RelatedConceptsRequest request
    ) throws IllegalInputFault, NotFoundFault {
        return this.conceptosRelacionadosLite(makeRequest(request, "Fármacos - Producto Comercial"));
    }

    // REQ-WS-014
    @WebResult(name = "respuestaConceptosRelacionados")
    @WebMethod(operationName = "obtenerMedicamentoClinicoConEnvase")
    public RelatedConceptsResponse obtenerMedicamentoClinicoConEnvase(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionados")
            RelatedConceptsRequest request
    ) throws IllegalInputFault, NotFoundFault {
        return this.conceptosRelacionados(makeRequest(request, "Fármacos - Medicamento Clínico con Envase"));
    }

    // REQ-WS-014 Lite
    @WebResult(name = "respuestaConceptosRelacionados")
    @WebMethod(operationName = "obtenerMedicamentoClinicoConEnvaseLite")
    public RelatedConceptsLiteResponse obtenerMedicamentoClinicoConEnvaseLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionados")
            RelatedConceptsRequest request
    ) throws IllegalInputFault, NotFoundFault {
        return this.conceptosRelacionadosLite(makeRequest(request, "Fármacos - Medicamento Clínico con Envase"));
    }

    // REQ-WS-015
    // REQ-WS-018
    @WebResult(name = "respuestaConceptosRelacionados")
    @WebMethod(operationName = "obtenerProductoComercialConEnvase")
    public RelatedConceptsResponse obtenerProductoComercialConEnvase(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionados")
            RelatedConceptsRequest request
    ) throws IllegalInputFault, NotFoundFault {
        return this.conceptosRelacionados(makeRequest(request, "Fármacos - Producto Comercial con Envase"));
    }

    // REQ-WS-015 Lite
    // REQ-WS-018 Lite
    @WebResult(name = "respuestaConceptosRelacionados")
    @WebMethod(operationName = "obtenerProductoComercialConEnvaseLite")
    public RelatedConceptsLiteResponse obtenerProductoComercialConEnvaseLite(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionados")
            RelatedConceptsRequest request
    ) throws IllegalInputFault, NotFoundFault {
        return this.conceptosRelacionadosLite(makeRequest(request, "Fármacos - Producto Comercial con Envase"));
    }

    // REQ-WS-016
    @WebResult(name = "respuestaConceptosRelacionados")
    @WebMethod(operationName = "obtenerFamiliaProducto")
    public RelatedConceptsResponse obtenerFamiliaProducto(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionados")
            RelatedConceptsRequest request
    ) throws IllegalInputFault, NotFoundFault {
        return this.conceptosRelacionados(makeRequest(request, "Fármacos - Familia de Productos"));
    }

    // REQ-WS-019
    @WebResult(name = "respuestaConceptosRelacionados")
    @WebMethod(operationName = "obtenerSustancia")
    public RelatedConceptsResponse obtenerSustancia(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionados")
            RelatedConceptsRequest request
    ) throws IllegalInputFault, NotFoundFault {
        return this.conceptosRelacionados(makeRequest(request, "Fármacos - Sustancia"));
    }

    // REQ-WS-020
    @WebResult(name = "registroISP")
    @WebMethod(operationName = "obtenerRegistroISP")
    public List<ISPRegisterResponse> obtenerRegistroISP(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionados")
            RelatedConceptsRequest request
    ) throws IllegalInputFault, NotFoundFault {
        return this.conceptController.getRegistrosISP(request.getConceptId(), request.getDescriptionId());
    }

    // REQ-WS-021
    @WebResult(name = "registroBioequivalente")
    @WebMethod(operationName = "obtenerBioequivalentes")
    public List<ISPRegisterResponse> obtenerBioequivalentes(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosRelacionados")
            RelatedConceptsRequest request
    ) throws IllegalInputFault, NotFoundFault {
        return this.conceptController.getBioequivalentes(request.getConceptId(), request.getDescriptionId());
    }

    private static RelatedConceptsByCategoryRequest makeRequest(RelatedConceptsRequest source, String category) {
        RelatedConceptsByCategoryRequest res = new RelatedConceptsByCategoryRequest();

        res.setConceptId(source.getConceptId());
        res.setDescriptionId(source.getDescriptionId());
        res.setRelatedCategoryName(category);

        return res;
    }

}
