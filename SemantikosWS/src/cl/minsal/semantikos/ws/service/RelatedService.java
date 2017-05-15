package cl.minsal.semantikos.ws.service;

import cl.minsal.semantikos.kernel.auth.AuthenticationManager;
import cl.minsal.semantikos.modelws.request.RelatedConceptsByCategoryRequest;
import cl.minsal.semantikos.modelws.request.RelatedConceptsRequest;
import cl.minsal.semantikos.modelws.request.Request;
import cl.minsal.semantikos.modelws.response.BioequivalentSearchResponse;
import cl.minsal.semantikos.modelws.response.ISPRegisterSearchResponse;
import cl.minsal.semantikos.modelws.response.RelatedConceptsLiteResponse;
import cl.minsal.semantikos.modelws.response.RelatedConceptsResponse;
import cl.minsal.semantikos.ws.component.ConceptController;
import cl.minsal.semantikos.modelws.fault.IllegalInputFault;
import cl.minsal.semantikos.modelws.fault.NotFoundFault;

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
            Request request = (Request)ctx.getParameters()[0];
            authenticationManager.validateInstitution(request.getIdStablishment());
        }
        catch (Exception e) {
            throw new NotFoundFault(e.getMessage());
        }
        return ctx.proceed();
    }

    // REQ-WS-010...021
    @WebResult(name = "respuestaConceptosRelacionados")
    @WebMethod(operationName = "conceptosRelacionados")
    public RelatedConceptsResponse conceptosRelacionados(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionConceptosRelacionados")
            RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault, NotFoundFault {

        /* Validación de parámetros */
        if ((request.getConceptId() == null || "".equals(request.getConceptId())) && (request.getDescriptionId() == null || "".equals(request.getDescriptionId()))) {
            throw new IllegalInputFault("Debe ingresar un idConcepto o idDescripcion");
        }

        /* Se realiza la búsqueda */
        return this.conceptController.findRelated(request.getDescriptionId(), request.getConceptId(), request.getRelatedCategoryName());
    }

    // REQ-WS-010...021 Lite
    @WebResult(name = "respuestaConceptosRelacionadosLite")
    @WebMethod(operationName = "conceptosRelacionadosLite")
    public RelatedConceptsLiteResponse conceptosRelacionadosLite(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionConceptosRelacionados")
                    RelatedConceptsByCategoryRequest request
    ) throws IllegalInputFault, NotFoundFault {
        if ((request.getConceptId() == null || "".equals(request.getConceptId()))
                && (request.getDescriptionId() == null || "".equals(request.getDescriptionId()))) {
            throw new IllegalInputFault("Debe ingresar un idConcepto o idDescripcion");
        }
        return this.conceptController.findRelatedLite(request.getConceptId(), request.getDescriptionId(), request.getRelatedCategoryName());
    }

    // REQ-WS-020
    @WebResult(name = "respuestaObtenerRegistroISP")
    @WebMethod(operationName = "obtenerRegistroISP")
    public ISPRegisterSearchResponse obtenerRegistroISP(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionObtenerRegistroISP")
                    RelatedConceptsRequest request
    ) throws IllegalInputFault, NotFoundFault {
        return this.conceptController.getRegistrosISP(request.getConceptId(), request.getDescriptionId());
    }

    // REQ-WS-021
    @WebResult(name = "respuestaObtenerBioequivalentes")
    @WebMethod(operationName = "obtenerBioequivalentes")
    public BioequivalentSearchResponse obtenerBioequivalentes(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionObtenerBioequivalentes")
            RelatedConceptsRequest request
    ) throws IllegalInputFault, NotFoundFault {
        return this.conceptController.getBioequivalentes(request.getConceptId(), request.getDescriptionId());
    }

}
