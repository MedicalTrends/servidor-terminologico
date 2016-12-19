package cl.minsal.semantikos.ws.service;

import cl.minsal.semantikos.ws.component.ConceptController;
import cl.minsal.semantikos.ws.fault.IllegalInputFault;
import cl.minsal.semantikos.ws.request.NewTermRequest;
import cl.minsal.semantikos.ws.response.DescriptionResponse;
import cl.minsal.semantikos.ws.response.NewTermResponse;

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

    @EJB
    private ConceptController conceptController;

    /**
     * REQ-WS-003: Este servicio web corresponde al formulario de solicitud para la creación de un nuevo término.
     *
     * @param termRequest La solicitud de creación de un nuevo término.
     *
     * @return El <em>ID DESCRIPCIÓN</em> de la descripción creada a partir de la solicitud.
     */
    @WebResult(name = "respuestaCodificacionDeNuevoTermino")
    @WebMethod
    public NewTermResponse codificacionDeNuevoTermino(
            @XmlElement(required = true)
            @WebParam(name = "peticionCodificacionDeNuevoTermino")
            NewTermRequest termRequest) throws IllegalInputFault {

        return conceptController.requestTermCreation(termRequest);
    }

    // REQ-WS-030
    @WebResult(name = "descripcion")
    @WebMethod
    public DescriptionResponse incrementarContadorDescripcionConsumida(
            @XmlElement(required = true)
            @WebParam(name = "idDescripcion")
            String descriptionId
    ) {
        // TODO
        return null;
    }

}
