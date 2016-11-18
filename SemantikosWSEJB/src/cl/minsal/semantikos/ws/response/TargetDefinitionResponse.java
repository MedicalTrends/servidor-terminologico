package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Development on 2016-10-14.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "definicionObjetivo")
public class TargetDefinitionResponse implements Serializable {

    @XmlElement(name="esTipoBasico")
    private Boolean isBasicType;
    @XmlElement(name="esTipoSMTK")
    private Boolean isSMTKType;
    @XmlElement(name="esTablaAuxiliar")
    private Boolean isHelperTable;
    @XmlElement(name="esTipoSnomedCT")
    private Boolean isSnomedCTType;
    @XmlElement(name="esTipoCrossMap")
    private Boolean isCrossMapType;

    public Boolean getBasicType() {
        return isBasicType;
    }

    public void setBasicType(Boolean basicType) {
        isBasicType = basicType;
    }

    public Boolean getSMTKType() {
        return isSMTKType;
    }

    public void setSMTKType(Boolean SMTKType) {
        isSMTKType = SMTKType;
    }

    public Boolean getHelperTable() {
        return isHelperTable;
    }

    public void setHelperTable(Boolean helperTable) {
        isHelperTable = helperTable;
    }

    public Boolean getSnomedCTType() {
        return isSnomedCTType;
    }

    public void setSnomedCTType(Boolean snomedCTType) {
        isSnomedCTType = snomedCTType;
    }

    public Boolean getCrossMapType() {
        return isCrossMapType;
    }

    public void setCrossMapType(Boolean crossMapType) {
        isCrossMapType = crossMapType;
    }
}
