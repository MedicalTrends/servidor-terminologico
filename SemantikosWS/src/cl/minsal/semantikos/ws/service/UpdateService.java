package cl.minsal.semantikos.ws.service;

import cl.minsal.semantikos.kernel.auth.AuthenticationManager;
import cl.minsal.semantikos.ws.component.ConceptController;
import cl.minsal.semantikos.ws.component.DescriptionController;
import cl.minsal.semantikos.ws.fault.IllegalInputFault;
import cl.minsal.semantikos.ws.fault.NotFoundFault;
import cl.minsal.semantikos.ws.request.DescriptionHitRequest;
import cl.minsal.semantikos.ws.request.NewTermRequest;
import cl.minsal.semantikos.ws.request.Request;
import cl.minsal.semantikos.ws.response.DescriptionResponse;
import cl.minsal.semantikos.ws.response.NewTermResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private AuthenticationManager authenticationManager;

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
                    NewTermRequest termRequest) throws IllegalInputFault, NotFoundFault {

        NewTermResponse newTermResponse = conceptController.requestTermCreation(termRequest);
        logger.info("codificacionDeNuevoTermino response: " + newTermResponse);

        return newTermResponse;
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
    ) throws IllegalInputFault, NotFoundFault  {
        return descriptionController.incrementDescriptionHits(descriptionHitRequest.getDescriptionID());
    }

}
