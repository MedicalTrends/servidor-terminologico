package cl.minsal.semantikos.model.users;

import cl.minsal.semantikos.model.PersistentEntity;

import java.util.List;

/**
 * @author Francisco Mendez.
 */
public class Profile extends PersistentEntity {

    /** Nombre del perfil */
    String name;

    /** Descripción del perfil */
    String description;

    /** Permisos asociados al perfil */
    List<Permission> permissions;

    /**
     * Este es el constructor único y mínimo para un Profile.
     *
     * @param id          Identificador único del Profile.
     * @param name        Nombre del Profile.
     * @param description Descripción del Perfil.
     */
    public Profile(long id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
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

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profile profile = (Profile) o;

        if (name != null ? !name.equals(profile.name) : profile.name != null) return false;

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
