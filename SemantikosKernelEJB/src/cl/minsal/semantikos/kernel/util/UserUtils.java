package cl.minsal.semantikos.kernel.util;

import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.modelweb.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 26-07-16.
 */
public class UserUtils {

    public static List<Profile> getNewProfiles(List<Profile> oldEntities, List<Profile> newEntities) {

        List<Profile> newEntities1 = new ArrayList<>();

        for (Profile persistentEntity : newEntities) {
            if (!persistentEntity.isPersistent()) {
                newEntities1.add(persistentEntity);
            }
        }

        return newEntities1;
    }

    public static List<Profile> getRemovedProfiles(List<Profile> oldEntities, List<Profile> newEntities) {

        List<Profile> removedEntities = new ArrayList<>();

        boolean isEntityFound;

        //Primero se buscan todas las relaciones persistidas originales
        for (Profile oldEntity : oldEntities) {
            isEntityFound = false;
            //Por cada descripción original se busca su descripcion correlacionada
            for (Profile newEntity : newEntities) {
                //Si la descripcion correlacionada no es encontrada, significa que fué eliminada
                if (oldEntity.getId() == newEntity.getId()) {
                    isEntityFound = true;
                    break;
                }
            }
            if(!isEntityFound) {
                removedEntities.add(oldEntity);
            }
        }

        return removedEntities;
    }

    public static List<Pair<Profile, Profile>> getModifiedProfiles(List<Profile> oldEntities, List<Profile> newEntities) {

        List<Pair<Profile, Profile>> modifiedEntities = new ArrayList<Pair<Profile, Profile>>();

        //Primero se buscan todas las descripciones persistidas originales
        for (Profile oldEntity : oldEntities) {
            //Por cada descripción original se busca su descripcion vista correlacionada
            for (Profile newEntity : newEntities) {
                //Si la descripcion correlacionada sufrio alguna modificación agregar el par (init, final)
                if (oldEntity.getId() == newEntity.getId() && !newEntity.equals(oldEntity) /*finalDescription.hasBeenModified()*/) {
                    modifiedEntities.add(new Pair(oldEntity, newEntity));
                }
            }
        }

        return modifiedEntities;
    }

    public static List<Institution> getNewInstitutions(List<Institution> oldEntities, List<Institution> newEntities) {

        List<Institution> newEntities1 = new ArrayList<>();

        for (Institution persistentEntity : newEntities) {
            if (!persistentEntity.isPersistent()) {
                newEntities1.add(persistentEntity);
            }
        }

        return newEntities1;
    }

    public static List<Institution> getRemovedInstitutions(List<Institution> oldEntities, List<Institution> newEntities) {

        List<Institution> removedEntities = new ArrayList<>();

        boolean isEntityFound;

        //Primero se buscan todas las relaciones persistidas originales
        for (Institution oldEntity : oldEntities) {
            isEntityFound = false;
            //Por cada descripción original se busca su descripcion correlacionada
            for (Institution newEntity : newEntities) {
                //Si la descripcion correlacionada no es encontrada, significa que fué eliminada
                if (oldEntity.getId() == newEntity.getId()) {
                    isEntityFound = true;
                    break;
                }
            }
            if(!isEntityFound) {
                removedEntities.add(oldEntity);
            }
        }

        return removedEntities;
    }

    public static List<Pair<Institution, Institution>> getModifiedInstitutions(List<Institution> oldEntities, List<Institution> newEntities) {

        List<Pair<Institution, Institution>> modifiedEntities = new ArrayList<Pair<Institution, Institution>>();

        //Primero se buscan todas las descripciones persistidas originales
        for (Institution oldEntity : oldEntities) {
            //Por cada descripción original se busca su descripcion vista correlacionada
            for (Institution newEntity : newEntities) {
                //Si la descripcion correlacionada sufrio alguna modificación agregar el par (init, final)
                if (oldEntity.getId() == newEntity.getId() && !newEntity.equals(oldEntity) /*finalDescription.hasBeenModified()*/) {
                    modifiedEntities.add(new Pair(oldEntity, newEntity));
                }
            }
        }

        return modifiedEntities;
    }
}
