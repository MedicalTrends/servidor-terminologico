package cl.minsal.semantikos.ws.service;


import cl.minsal.semantikos.kernel.components.AuthenticationManagerImpl;
import cl.minsal.semantikos.modelweb.Pair;
import cl.minsal.semantikos.modelws.request.RelatedConceptsByCategoryRequest;
import cl.minsal.semantikos.modelws.request.RelatedConceptsRequest;
import cl.minsal.semantikos.modelws.response.BioequivalentSearchResponse;
import cl.minsal.semantikos.modelws.response.ISPRegisterSearchResponse;
import cl.minsal.semantikos.modelws.response.RelatedConceptsLiteResponse;
import cl.minsal.semantikos.modelws.response.RelatedConceptsResponse;
import cl.minsal.semantikos.ws.component.ConceptController;
import cl.minsal.semantikos.modelws.fault.IllegalInputFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.minsal.semantikos.ws.utils.UtilsWS;

import javax.annotation.Resource;
import javax.ejb.EJB;
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
     * Metodo de autenticacion
     * @param idStablishment ID de establecimiento
     * @throws Exception
     */
    private void authenticate(String idStablishment) throws Exception {
        Pair credentials = UtilsWS.getCredentialsFromWSContext(wsctx.getMessageContext());
        authenticationManager.authenticateWS(credentials.getFirst().toString(), credentials.getSecond().toString());
        if(idStablishment!=null)
            authenticationManager.validateInstitution(idStablishment);
    }

    // REQ-WS-010...021
    @WebResult(name = "respuestaConceptosRelacionados")
    @WebMethod(operationName = "conceptosRelacionados")
    public RelatedConceptsResponse conceptosRelacionados(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionConceptosRelacionados")
            RelatedConceptsByCategoryRequest request
    )  {
        RelatedConceptsResponse response = new RelatedConceptsResponse();

        try {
            this.authenticate(request.getIdStablishment());
            /* Validación de parámetros */
            if ((request.getConceptId() == null || "".equals(request.getConceptId())) && (request.getDescriptionId() == null || "".equals(request.getDescriptionId()))) {
                throw new IllegalInputFault("Debe ingresar un idConcepto o idDescripcion");
            }
            /* Se realiza la búsqueda */
            response =  this.conceptController.findRelated(request.getDescriptionId(), request.getConceptId(), request.getRelatedCategoryName());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.setCode(1);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    // REQ-WS-010...021 Lite
    @WebResult(name = "respuestaConceptosRelacionadosLite")
    @WebMethod(operationName = "conceptosRelacionadosLite")
    public RelatedConceptsLiteResponse conceptosRelacionadosLite(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionConceptosRelacionados")
                    RelatedConceptsByCategoryRequest request
    ) {
        RelatedConceptsLiteResponse response = new RelatedConceptsLiteResponse();
        try {
            this.authenticate(request.getIdStablishment());
            if ((request.getConceptId() == null || "".equals(request.getConceptId()))
                    && (request.getDescriptionId() == null || "".equals(request.getDescriptionId()))) {
                logger.debug("Debe ingresar un idConcepto o idDescripcion");
                throw new IllegalInputFault("Debe ingresar un idConcepto o idDescripcion");
            }
            response = this.conceptController.findRelatedLite(request.getConceptId(), request.getDescriptionId(), request.getRelatedCategoryName());
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            response.setCode(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    // REQ-WS-020
    @WebResult(name = "respuestaObtenerRegistroISP")
    @WebMethod(operationName = "obtenerRegistroISP")
    public ISPRegisterSearchResponse obtenerRegistroISP(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionObtenerRegistroISP")
                    RelatedConceptsRequest request
    ) {
        ISPRegisterSearchResponse response = new ISPRegisterSearchResponse();
        try {
            this.authenticate(request.getIdStablishment());
            response = this.conceptController.getRegistrosISP(request.getConceptId(), request.getDescriptionId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.setCode(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    // REQ-WS-021
    @WebResult(name = "respuestaObtenerBioequivalentes")
    @WebMethod(operationName = "obtenerBioequivalentes")
    public BioequivalentSearchResponse obtenerBioequivalentes(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionObtenerBioequivalentes")
            RelatedConceptsRequest request
    ) {
        BioequivalentSearchResponse response = new BioequivalentSearchResponse();
        try {
            this.authenticate(request.getIdStablishment());
            response = this.conceptController.getBioequivalentes(request.getConceptId(), request.getDescriptionId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.setCode(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

}
