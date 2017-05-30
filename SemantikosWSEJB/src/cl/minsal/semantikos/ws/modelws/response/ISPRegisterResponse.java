package cl.minsal.semantikos.ws.modelws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-12-30.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "registroISP", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RegistroISP", namespace = "http://service.ws.semantikos.minsal.cl/")
public class ISPRegisterResponse implements Serializable {

    @XmlElement(name="registro")
    private String registro;
    @XmlElement(name="nombre")
    private String name;
    @XmlElement(name="descripcion")
    private String description;
    @XmlElement(name="esValido")
    private String isValid;
    @XmlElement(name="validoHasta")
    private String validityUntil;
    @XmlElement(name="estadoDelRegistro")
    private String estadoDelRegistro;
    @XmlElement(name="titular")
    private String titular;
    @XmlElement(name="referenciaDeTramite")
    private String referenciaDeTramite;
    @XmlElement(name="equivalenciaTerapeutica")
    private String equivalenciaTerapeutica;
    @XmlElement(name="resolucionInscribase")
    private String resolucionInscribase;
    @XmlElement(name="fechaIngreso")
    private String fechaIngreso;
    @XmlElement(name="fechaInscribase")
    private String fechaInscribase;
    @XmlElement(name="ultimaRenovacion")
    private String ultimaRenovacion;
    @XmlElement(name="fechaProximaRenovacion")
    private String fechaProximaRenovacion;
    @XmlElement(name="regimen")
    private String regimen;
    @XmlElement(name="viaAdministracion")
    private String viaAdministracion;
    @XmlElement(name="condicionDeVenta")
    private String condicionDeVenta;
    @XmlElement(name="expendeTipoEstablecimiento")
    private String expendeTipoEstablecimiento;
    @XmlElement(name="indicacion")
    private String indicacion;

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValid() {
        return isValid;
    }

    public void setValid(String valid) {
        isValid = valid;
    }

    public String getValidityUntil() {
        return validityUntil;
    }

    public void setValidityUntil(String validityUntil) {
        this.validityUntil = validityUntil;
    }

    public String getEstadoDelRegistro() {
        return estadoDelRegistro;
    }

    public void setEstadoDelRegistro(String estadoDelRegistro) {
        this.estadoDelRegistro = estadoDelRegistro;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getReferenciaDeTramite() {
        return referenciaDeTramite;
    }

    public void setReferenciaDeTramite(String referenciaDeTramite) {
        this.referenciaDeTramite = referenciaDeTramite;
    }

    public String getEquivalenciaTerapeutica() {
        return equivalenciaTerapeutica;
    }

    public void setEquivalenciaTerapeutica(String equivalenciaTerapeutica) {
        this.equivalenciaTerapeutica = equivalenciaTerapeutica;
    }

    public String getResolucionInscribase() {
        return resolucionInscribase;
    }

    public void setResolucionInscribase(String resolucionInscribase) {
        this.resolucionInscribase = resolucionInscribase;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getFechaInscribase() {
        return fechaInscribase;
    }

    public void setFechaInscribase(String fechaInscribase) {
        this.fechaInscribase = fechaInscribase;
    }

    public String getUltimaRenovacion() {
        return ultimaRenovacion;
    }

    public void setUltimaRenovacion(String ultimaRenovacion) {
        this.ultimaRenovacion = ultimaRenovacion;
    }

    public String getFechaProximaRenovacion() {
        return fechaProximaRenovacion;
    }

    public void setFechaProximaRenovacion(String fechaProximaRenovacion) {
        this.fechaProximaRenovacion = fechaProximaRenovacion;
    }

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    public String getViaAdministracion() {
        return viaAdministracion;
    }

    public void setViaAdministracion(String viaAdministracion) {
        this.viaAdministracion = viaAdministracion;
    }

    public String getCondicionDeVenta() {
        return condicionDeVenta;
    }

    public void setCondicionDeVenta(String condicionDeVenta) {
        this.condicionDeVenta = condicionDeVenta;
    }

    public String getExpendeTipoEstablecimiento() {
        return expendeTipoEstablecimiento;
    }

    public void setExpendeTipoEstablecimiento(String expendeTipoEstablecimiento) {
        this.expendeTipoEstablecimiento = expendeTipoEstablecimiento;
    }

    public String getIndicacion() {
        return indicacion;
    }

    public void setIndicacion(String indicacion) {
        this.indicacion = indicacion;
    }
}
