package cl.minsal.semantikos.ws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-11-22.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionCodificacionDeNuevoTermino", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionCodificacionDeNuevoTermino", namespace = "http://service.ws.semantikos.minsal.cl/")
public class NewTermRequest implements Serializable {

    @XmlElement(required = true, name = "establecimiento")
    private String institutionName;
    @XmlElement(required = true, name = "idConcepto")
    private String conceptId;
    @XmlElement(required = true, name = "termino")
    private String term;
    @XmlElement(required = false, defaultValue = "Preferida", name = "tipoDescripcion")
    private String descriptionTypeName;
    @XmlElement(required = false, defaultValue = "false", name = "esSensibleAMayusculas")
    private Boolean isCaseSensitive;
    @XmlElement(required = false, name = "email")
    private String email;
    @XmlElement(required = false, name = "observacion")
    private String observation;
    @XmlElement(required = false, name = "profesional")
    private String professional;
    @XmlElement(required = false, name = "profesion")
    private String profesion;
    @XmlElement(required = false, name = "especialidad")
    private String specialty;

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getConceptId() {
        return conceptId;
    }

    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDescriptionTypeName() {
        return descriptionTypeName;
    }

    public void setDescriptionTypeName(String descriptionTypeName) {
        this.descriptionTypeName = descriptionTypeName;
    }

    public Boolean getCaseSensitive() {
        return isCaseSensitive;
    }

    public void setCaseSensitive(Boolean caseSensitive) {
        isCaseSensitive = caseSensitive;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}
