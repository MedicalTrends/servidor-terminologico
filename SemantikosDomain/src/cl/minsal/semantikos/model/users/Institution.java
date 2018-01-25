package cl.minsal.semantikos.model.users;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.audit.AuditableEntity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 9/20/16.
 */
public class Institution extends PersistentEntity implements Serializable, AuditableEntity {

    //public static final Institution DUMMY_INSTITUTION = new Institution();

    /** BR-RefSet-003: Una Institución puede tener cero o más Usuarios Administradores de RefSets. */
    private List<User> administrators;

    /** El nombre de la institución */
    private String name;

    /**
     * El código del establecimiento
     */
    private Long code;

    /**
     * Fecha hasta la cuál es vigente el establecimiento
     */
    private Timestamp validityUntil;

    public Institution(String name, Long code) {
        this.name = name;
        this.code = code;
    }

    public Institution(long id, String name, Long code) {
        super(id);
        this.name = name;
        this.code = code;
    }

    public Institution(Institution institution) {
        super(institution.getId());
        setCode(institution.getId());
        setName(institution.getName());
        setValidityUntil(institution.getValidityUntil());
    }

    public Institution() {
        this.administrators = new ArrayList<>();
    }

    public List<User> getAdministrators() {
        return administrators;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Timestamp getValidityUntil() {
        return validityUntil;
    }

    public void setValidityUntil(Timestamp validityUntil) {
        this.validityUntil = validityUntil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Institution that = (Institution) o;

        /* Si ambas están persistidas comparar sus ids */
        if (this.isPersistent() && that.isPersistent()) return (this.getId() == that.getId());

        if (this.code == null || that.code == null) return false;

        /* Si alguna de ellas no está persistida, comparamos 1. tabla auxiliar, 2. descripción del row, y 3. campos del row */
        return name.equals(that.name) && code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }


    @Override
    public String toString() {
        return name;
    }


    public boolean isValid() {
        return (validityUntil != null);
    }
}
