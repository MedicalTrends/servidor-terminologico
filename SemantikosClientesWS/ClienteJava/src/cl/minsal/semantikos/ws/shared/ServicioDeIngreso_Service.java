
package cl.minsal.semantikos.ws.shared;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "ServicioDeIngreso", targetNamespace = "http://service.ws.semantikos.minsal.cl/", wsdlLocation = "file:/home/des01c7/IdeaProjects/servidor-terminologico/SemantikosClientesWS/resources/ServicioDeIngreso.wsdl")
public class ServicioDeIngreso_Service
    extends Service
{

    private final static URL SERVICIODEINGRESO_WSDL_LOCATION;
    private final static WebServiceException SERVICIODEINGRESO_EXCEPTION;
    private final static QName SERVICIODEINGRESO_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "ServicioDeIngreso");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/home/des01c7/IdeaProjects/servidor-terminologico/SemantikosClientesWS/resources/ServicioDeIngreso.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SERVICIODEINGRESO_WSDL_LOCATION = url;
        SERVICIODEINGRESO_EXCEPTION = e;
    }

    public ServicioDeIngreso_Service() {
        super(__getWsdlLocation(), SERVICIODEINGRESO_QNAME);
    }

    public ServicioDeIngreso_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), SERVICIODEINGRESO_QNAME, features);
    }

    public ServicioDeIngreso_Service(URL wsdlLocation) {
        super(wsdlLocation, SERVICIODEINGRESO_QNAME);
    }

    public ServicioDeIngreso_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SERVICIODEINGRESO_QNAME, features);
    }

    public ServicioDeIngreso_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ServicioDeIngreso_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns ServicioDeIngreso
     */
    @WebEndpoint(name = "ServicioDeIngresoPort")
    public ServicioDeIngreso getServicioDeIngresoPort() {
        return super.getPort(new QName("http://service.ws.semantikos.minsal.cl/", "ServicioDeIngresoPort"), ServicioDeIngreso.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ServicioDeIngreso
     */
    @WebEndpoint(name = "ServicioDeIngresoPort")
    public ServicioDeIngreso getServicioDeIngresoPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://service.ws.semantikos.minsal.cl/", "ServicioDeIngresoPort"), ServicioDeIngreso.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SERVICIODEINGRESO_EXCEPTION!= null) {
            throw SERVICIODEINGRESO_EXCEPTION;
        }
        return SERVICIODEINGRESO_WSDL_LOCATION;
    }

}
