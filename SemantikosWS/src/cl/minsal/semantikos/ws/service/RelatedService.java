package cl.minsal.semantikos.ws.service;


import cl.minsal.semantikos.kernel.components.AuthenticationManagerImpl;
import cl.minsal.semantikos.modelweb.Pair;
import cl.minsal.semantikos.modelws.fault.NotFoundFault;
import cl.minsal.semantikos.modelws.request.RelatedConceptsByCategoryRequest;
import cl.minsal.semantikos.modelws.request.RelatedConceptsRequest;
import cl.minsal.semantikos.modelws.request.Request;
import cl.minsal.semantikos.modelws.response.BioequivalentSearchResponse;
import cl.minsal.semantikos.modelws.response.ISPRegisterSearchResponse;
import cl.minsal.semantikos.modelws.response.RelatedConceptsLiteResponse;
import cl.minsal.semantikos.modelws.response.RelatedConceptsResponse;
import cl.minsal.semantikos.ws.component.ConceptController;
import cl.minsal.semantikos.modelws.fault.IllegalInputFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cl.minsal.semantikos.ws.utils.UtilsWS;

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
    private AuthenticationManagerImpl authenticationManager;

    @Resource
    WebServiceContext wsctx;

    private static final Logger logger = LoggerFactory.getLogger(RelatedService.class);

    /**
     * Metodo de envoltura de los web methods
     * @param ctx Contexto de invocacion
     * @throws Exception
     */
    @AroundInvoke
    protected Object webMethodWrapper(InvocationContext ctx) throws Exception {

        Request request = (Request) ctx.getParameters()[0];
        String webMethodStatus = "0";
        String webMethodMessage = "OK";

        try {
            Pair credentials = UtilsWS.getCredentials(wsctx.getMessageContext());
            authenticationManager.authenticateWS(credentials.getFirst().toString(), credentials.getSecond().toString());
            authenticationManager.validateInstitution(request.getIdStablishment());

            return ctx.proceed();
        }
        catch (Exception e) {
            logger.error("El web service ha arrojado el siguiente error: "+e.getMessage(),e);
            throw new NotFoundFault(e.getMessage());
            //webMethodStatus = "1";
            //webMethodMessage = e.getMessage();
        }
        /*
        finally {
            HttpServletResponse response = (HttpServletResponse) wsctx.getMessageContext().get(MessageContext.SERVLET_RESPONSE);
            response.addHeader("web-method-status",webMethodStatus);
            response.addHeader("web-method-message",webMethodMessage);
        }
        */

        //return null;
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
        try {
            return this.conceptController.findRelated(request.getDescriptionId(), request.getConceptId(), request.getRelatedCategoryName());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalInputFault(e.getMessage());
        }
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
        try {
            return this.conceptController.getRegistrosISP(request.getConceptId(), request.getDescriptionId());
        } catch (Exception e) {
            throw new NotFoundFault(e.getMessage());
        }
    }

    // REQ-WS-021
    @WebResult(name = "respuestaObtenerBioequivalentes")
    @WebMethod(operationName = "obtenerBioequivalentes")
    public BioequivalentSearchResponse obtenerBioequivalentes(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionObtenerBioequivalentes")
            RelatedConceptsRequest request
    ) throws IllegalInputFault, NotFoundFault {
        try {
            return this.conceptController.getBioequivalentes(request.getConceptId(), request.getDescriptionId());
        } catch (Exception e) {
            throw new NotFoundFault(e.getMessage());
        }
    }

}
