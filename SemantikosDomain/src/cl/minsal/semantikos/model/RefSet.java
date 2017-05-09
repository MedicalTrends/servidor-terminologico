package cl.minsal.semantikos.model;

import cl.minsal.semantikos.model.audit.AuditableEntity;
import cl.minsal.semantikos.model.users.Institution;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 9/7/16.
 */
public class RefSet extends PersistentEntity implements AuditableEntity {

    /**
     * Nombre del RefSet. Nombre corto y descriptivo de su contenido, para identificación por humanos
     */
    private String name;

    /**
     * La institución a la cual pertenece el RefSet
     */
    private Institution institution;

    /**
     * Fecha hasta la cuál es vigente el RefSet
     */
    private Timestamp validityUntil;

    /**
     * Fecha de Creación
     */
    private Timestamp creationDate;

    /**
     * Lista de los conceptos en el RefSet
     */
    private List<ConceptSMTK> concepts;

    private long id;

    public RefSet(String name, Institution institution, Timestamp creationDate) {
        this.id = NON_PERSISTED_ID;
        this.institution = institution;
        this.name = name;
        this.creationDate = creationDate;
        this.concepts = new ArrayList<>();
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Timestamp getValidityUntil() {
        return validityUntil;
    }

    public void setValidityUntil(Timestamp validityUntil) {
        this.validityUntil = validityUntil;
    }

    public String getName() {
        return name;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public boolean bindConceptTo(ConceptSMTK conceptSMTK) {
        return concepts.add(conceptSMTK);
    }

    public boolean unbindConceptTo(ConceptSMTK conceptSMTK) {
        return concepts.remove(conceptSMTK);
    }

    public List<ConceptSMTK> getConcepts() {
        return new ArrayList<ConceptSMTK>(this.concepts);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setConcepts(List<ConceptSMTK> concepts) {
        this.concepts = concepts;
    }

    public boolean isValid() {
        return (getValidityUntil() == null || this.getValidityUntil().after(new Timestamp(System.currentTimeMillis())));
    }

    public String getCountConcepts() {
        return String.valueOf(concepts.size());
    }

    public String getDateCreationFormat() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(this.getCreationDate());
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
