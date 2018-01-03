package cl.minsal.semantikos.model.users;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.audit.AuditableEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 9/20/16.
 */
public class Institution extends PersistentEntity implements Serializable, AuditableEntity {

    public static final Institution DUMMY_INSTITUTION = new Institution();

    /** BR-RefSet-003: Una Institución puede tener cero o más Usuarios Administradores de RefSets. */
    private List<User> administrators;

    /** El nombre de la institución */
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Institution institution = (Institution) o;

        if (name != null ? !name.equals(institution.name) : institution.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }


    @Override
    public String toString() {
        return name;
    }
}
