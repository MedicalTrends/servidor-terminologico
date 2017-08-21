package cl.minsal.semantikos.ws.service;

import cl.minsal.semantikos.kernel.components.AuthenticationManagerImpl;
import cl.minsal.semantikos.kernel.components.UserManager;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.modelweb.Pair;
import cl.minsal.semantikos.modelws.request.DescriptionHitRequest;
import cl.minsal.semantikos.modelws.request.NewTermRequest;
import cl.minsal.semantikos.modelws.response.DescriptionResponse;
import cl.minsal.semantikos.modelws.response.NewTermResponse;
import cl.minsal.semantikos.ws.component.ConceptController;
import cl.minsal.semantikos.ws.component.DescriptionController;


import ws.minsal.semantikos.ws.utils.UtilsWS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.WebServiceContext;

/**
 * @author Alfonso Cornejo on 2016-11-18.
 */
@WebService(serviceName = "ServicioDeIngreso",
        name = "ServicioDeIngreso",
        targetNamespace = "http://service.ws.semantikos.minsal.cl/")
public class UpdateService {

    private static final Logger logger = LoggerFactory.getLogger(UpdateService.class);

    @EJB
    private ConceptController conceptController;

    @EJB
    private DescriptionController descriptionController;

    @Resource
    WebServiceContext wsctx;

    @EJB
    private AuthenticationManagerImpl authenticationManager;

    @EJB
    private UserManager userManager;


    User wsUser;

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

    /**
     * REQ-WS-003: Este servicio web corresponde al formulario de solicitud para la creación de un nuevo término.
     *
     * @param termRequest La solicitud de creación de un nuevo término.
     * @return El <em>ID DESCRIPCIÓN</em> de la descripción creada a partir de la solicitud.
     */
    @WebResult(name = "respuestaCodificacionDeNuevoTermino")
    @WebMethod
    public NewTermResponse codificacionDeNuevoTermino(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionCodificacionDeNuevoTermino")
                    NewTermRequest termRequest) {

        NewTermResponse response = new NewTermResponse();
        try {
            this.authenticate(termRequest.getIdStablishment());
            response = conceptController.requestTermCreation(termRequest);
            logger.info("codificacionDeNuevoTermino response: " + response);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            response.setCode(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    // REQ-WS-030

    /**
     * REQ-WS-030: El sistema Semantikos debe disponer un servicio que permita aumentar el hit de una Descripción.
     *
     * @param descriptionHitRequest El valor de negocio DESCRIPTION_ID de una descripción.
     * @return
     */
    @WebResult(name = "descripcion")
    @WebMethod(operationName = "contarDescripcionConsumida")
    public DescriptionResponse incrementarContadorDescripcionConsumida(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionContarDescripcionConsumida")
                    DescriptionHitRequest descriptionHitRequest
    )  {
        DescriptionResponse response = new DescriptionResponse();
        try {
            this.authenticate(descriptionHitRequest.getIdStablishment());
            response = descriptionController.incrementDescriptionHits(descriptionHitRequest.getDescriptionID());
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            response.setCode(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

}
