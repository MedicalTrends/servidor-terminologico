package cl.minsal.semantikos.kernel.util;

import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.modelweb.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by root on 26-07-16.
 */
public class ConceptUtils {

    public static List<Description> getNewDesciptions(List<Description> oldEntities, List<Description> newEntities) {

        List<Description> newEntities1 = new ArrayList<>();

        for (Description persistentEntity : newEntities) {
            if (!persistentEntity.isPersistent()) {
                newEntities1.add(persistentEntity);
            }
        }

        return newEntities1;
    }

    public static List<Description> getRemovedDescriptions(List<Description> oldEntities, List<Description> newEntities) {

        List<Description> removedEntities = new ArrayList<>();

        boolean isEntityFound;

        //Primero se buscan todas las relaciones persistidas originales
        for (Description oldEntity : oldEntities) {
            isEntityFound = false;
            //Por cada descripción original se busca su descripcion correlacionada
            for (Description newEntity : newEntities) {
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

    public static List<Pair<Description, Description>> getModifiedDescriptions(List<Description> oldEntities, List<Description> newEntities) {

        List<Pair<Description, Description>> modifiedEntities = new ArrayList<Pair<Description, Description>>();

        //Primero se buscan todas las descripciones persistidas originales
        for (Description oldEntity : oldEntities) {
            //Por cada descripción original se busca su descripcion vista correlacionada
            for (Description newEntity : newEntities) {
                //Si la descripcion correlacionada sufrio alguna modificación agregar el par (init, final)
                if (oldEntity.getId() == newEntity.getId() && !newEntity.equals(oldEntity) /*finalDescription.hasBeenModified()*/) {
                    modifiedEntities.add(new Pair(oldEntity, newEntity));
                }
            }
        }

        return modifiedEntities;
    }

    public static List<Relationship> getNewRelationships(List<Relationship> oldEntities, List<Relationship> newEntities) {

        List<Relationship> newEntities1 = new ArrayList<>();

        for (Relationship persistentEntity : newEntities) {
            if (!persistentEntity.isPersistent()) {
                newEntities1.add(persistentEntity);
            }
        }

        return newEntities1;
    }

    public static List<Relationship> getRemovedRelationships(List<Relationship> oldEntities, List<Relationship> newEntities) {

        List<Relationship> removedEntities = new ArrayList<>();

        boolean isEntityFound;

        //Primero se buscan todas las relaciones persistidas originales
        for (Relationship oldEntity : oldEntities) {
            isEntityFound = false;
            //Por cada descripción original se busca su descripcion correlacionada
            for (Relationship newEntity : newEntities) {
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

    public static List<Pair<Relationship, Relationship>> getModifiedRelationships(List<Relationship> oldEntities, List<Relationship> newEntities) {

        List<Pair<Relationship, Relationship>> modifiedEntities = new ArrayList<Pair<Relationship, Relationship>>();

        //Primero se buscan todas las descripciones persistidas originales
        for (Relationship oldEntity : oldEntities) {
            //Por cada descripción original se busca su descripcion vista correlacionada
            for (Relationship newEntity : newEntities) {
                //Si la descripcion correlacionada sufrio alguna modificación agregar el par (init, final)
                if (oldEntity.getId() == newEntity.getId() && !newEntity.equals(oldEntity) /*finalDescription.hasBeenModified()*/) {
                    modifiedEntities.add(new Pair(oldEntity, newEntity));
                }
            }
        }

        return modifiedEntities;
    }
}
