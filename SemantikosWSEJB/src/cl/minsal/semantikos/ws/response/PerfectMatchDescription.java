package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * @author Andrés Farías on 12/13/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "perfectMatch", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PerfectMatch", namespace = "http://service.ws.semantikos.minsal.cl/")
public class PerfectMatchDescription implements Serializable {


}
