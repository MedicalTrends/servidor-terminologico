package cl.minsal.semantikos.ws.service;

import cl.minsal.semantikos.ws.request.NewTermRequest;
import cl.minsal.semantikos.ws.response.DescriptionResponse;
import cl.minsal.semantikos.ws.response.NewTermResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Development on 2016-11-18.
 *
 */
@WebService(serviceName = "ServicioDeIngreso",
        name = "ServicioDeIngreso",
        targetNamespace = "http://service.ws.semantikos.minsal.cl/")
public class UpdateService {

    // REQ-WS-003
    @WebResult(name = "respuestaCodificacionDeNuevoTermino")
    @WebMethod
    public NewTermResponse codificacionDeNuevoTermino(
            @XmlElement(required = true)
            @WebParam(name = "peticionCodificacionDeNuevoTermino")
            NewTermRequest request
    ) {
        // TODO
        return null;
    }

    // REQ-WS-030
    @WebResult(name = "descripcion")
    @WebMethod
    public DescriptionResponse incrementarContadorDescripcionConsumida(
            @XmlElement(required = true)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) {
        return null;
    }

}
