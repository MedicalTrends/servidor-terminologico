package cl.minsal.semantikos.modelws.response;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.relationships.Target;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Andrés Farías on 11/3/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "crossmapSetRecordResponse", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "CrossmapSetRecordResponse", namespace = "http://service.ws.semantikos.minsal.cl/")
public abstract class CrossmapSetRecordResponse extends PersistentEntity {

    public CrossmapSetRecordResponse() {
    }

    public CrossmapSetRecordResponse(long id) {
        super(id);
    }
}
