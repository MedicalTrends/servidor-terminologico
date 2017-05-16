package cl.minsal.semantikos.model.users;

import java.io.Serializable;

/**
 * Created by BluePrints Developer on 19-05-2016.
 */
public class Permission implements Serializable {

    Long idPermission;
    String name;
    String description;

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

    public Long getIdPermission() {
        return idPermission;
    }

    public void setIdPermission(Long idPermission) {
        this.idPermission = idPermission;
    }
}
