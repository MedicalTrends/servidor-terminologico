package cl.minsal.semantikos.ws.service;

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
@WebService(serviceName = "ServicioDeActualizacion")
public class UpdateService {

    // REQ-WS-003
    @WebMethod(operationName = "codificacionDeNuevoTermino")
    public NewTermResponse codificacionDeNuevoTermino(
            @XmlElement(required = true)
            @WebParam(name = "establecimiento")
                    String institutionName,
            @XmlElement(required = true)
            @WebParam(name = "idConcepto")
                    String conceptId,
            @XmlElement(required = true)
            @WebParam(name = "termino")
                    String term,
            @XmlElement(required = false, defaultValue = "Preferida")
            @WebParam(name = "tipoDescripcion")
                    String descriptionTypeName,
            @XmlElement(required = false, defaultValue = "false")
            @WebParam(name = "esSensibleAMayusculas")
                    Boolean isCaseSensitive,
            @XmlElement(required = false)
            @WebParam(name = "email")
                    String email,
            @XmlElement(required = false)
            @WebParam(name = "observacion")
                    String observation,
            @XmlElement(required = false)
            @WebParam(name = "profesional")
                    String professional,
            @XmlElement(required = false)
            @WebParam(name = "profesion")
                    String profesion,
            @XmlElement(required = false)
            @WebParam(name = "especialidad")
                    String specialty
    ) {
        // TODO
        return null;
    }

    // REQ-WS-030
    @WebMethod(operationName = "incrementarContadorDescripcionConsumida")
    public DescriptionResponse incrementarContadorDescripcionConsumida(
            @XmlElement(required = true)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) {
        return null;
    }

}
