package cl.minsal.semantikos.model.users;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BluePrints Developer on 19-05-2016.
 */
public class Group {

    String idGroup;
    String name;
    String description;

    List<Profile> profiles;
    List<Permission> permissions;

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
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

    public List<Profile> getProfiles() {
        if(profiles==null)
            profiles= new ArrayList<Profile>();
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }



    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Permission> getPermissions() {
        if(permissions==null)
            permissions= new ArrayList<Permission>();
        return permissions;
    }
}
