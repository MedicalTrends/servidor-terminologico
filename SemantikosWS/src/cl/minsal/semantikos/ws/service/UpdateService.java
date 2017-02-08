package cl.minsal.semantikos.ws.service;

import cl.minsal.semantikos.ws.component.ConceptController;
import cl.minsal.semantikos.ws.component.DescriptionController;
import cl.minsal.semantikos.ws.fault.IllegalInputFault;
import cl.minsal.semantikos.ws.request.NewTermRequest;
import cl.minsal.semantikos.ws.response.DescriptionResponse;
import cl.minsal.semantikos.ws.response.NewTermResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

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

    /**
     * REQ-WS-003: Este servicio web corresponde al formulario de solicitud para la creación de un nuevo término.
     *
     * @param termRequest La solicitud de creación de un nuevo término.
     * @return El <em>ID DESCRIPCIÓN</em> de la descripción creada a partir de la solicitud.
     */
    @WebResult(name = "respuestaCodificacionDeNuevoTermino")
    @WebMethod
    public NewTermResponse codificacionDeNuevoTermino(
            @XmlElement(required = true)
            @WebParam(name = "peticionCodificacionDeNuevoTermino")
                    NewTermRequest termRequest) throws IllegalInputFault {

        NewTermResponse newTermResponse = conceptController.requestTermCreation(termRequest);
        logger.info("codificacionDeNuevoTermino response: " + newTermResponse);

        return newTermResponse;
    }

    // REQ-WS-030

    /**
     * REQ-WS-030: El sistema Semantikos debe disponer un servicio que permita aumentar el hit de una Descripción.
     *
     * @param descriptionId El valor de negocio DESCRIPTION_ID de una descripción.
     * @return
     */
    @WebResult(name = "descripcionCounter")
    @WebMethod
    public DescriptionResponse incrementarContadorDescripcionConsumida(
            @XmlElement(required = true)
            @WebParam(name = "descriptionID")
                    String descriptionId
    ) {
        return descriptionController.incrementDescriptionHits(descriptionId);
    }

}
