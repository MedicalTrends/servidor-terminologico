package cl.minsal.semantikos.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Andrés Farías on 11/22/16.
 */
public class PendingTerm extends PersistentEntity {

    private String term;

    private Date date;

    private boolean sensibility;

    private Category category;

    private String nameProfessional;

    private String profession;

    private String speciality;

    private String subSpeciality;

    private String mail;

    private String observation;

    /** La descripción a la cual está asociado el término pendiente (en cualquier concepto) */
    private Description relatedDescription;

    public PendingTerm(String term, Date date, boolean sensibility, Category category, String nameProfessional, String profession, String speciality, String subSpeciality, String mail, String observation) {
        this.term = term;
        this.date = date;
        this.sensibility = sensibility;
        this.category = category;
        this.nameProfessional = nameProfessional;
        this.profession = profession;
        this.speciality = speciality;
        this.subSpeciality = subSpeciality;
        this.mail = mail;
        this.observation = observation;
        this.relatedDescription = null;
    }

    public PendingTerm(long id, String term, Timestamp submissionDate, boolean sensibility, Category categoryById, String nameProfessional, String profession, String specialty, String subSpecialty, String mail, String observation) {
        this(term, submissionDate, sensibility, categoryById, nameProfessional, profession, specialty, subSpecialty, mail, observation);
        this.setId(id);
    }

    public String getTerm() {
        return term;
    }

    public Date getDate() {
        return date;
    }

    public boolean isSensibility() {
        return sensibility;
    }

    public void setSensibility(boolean sensibility) {
        this.sensibility = sensibility;
    }

    public Category getCategory() {
        return category;
    }

    public String getNameProfessional() {
        return nameProfessional;
    }

    public String getProfession() {
        return profession;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getSubSpeciality() {
        return subSpeciality;
    }

    public String getMail() {
        return mail;
    }

    public String getObservation() {
        return observation;
    }

    public Description getRelatedDescription() {
        return relatedDescription;
    }

    public void setRelatedDescription(Description relatedDescription) {
        this.relatedDescription = relatedDescription;
    }

    @Override
    public String toString() {
        return "PendingTerm{" +
                "term='" + term + '\'' +
                ", enviado el " + date +
                " por '" + nameProfessional + '\'' +
                "('" + mail + "\')" +
                '}';
    }
}
