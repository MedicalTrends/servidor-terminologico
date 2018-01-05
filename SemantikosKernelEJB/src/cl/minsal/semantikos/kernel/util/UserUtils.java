package cl.minsal.semantikos.kernel.util;

import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.modelweb.Pair;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by root on 26-07-16.
 */
public class UserUtils {

    public static List<Profile> getNewProfiles(List<Profile> oldEntities, List<Profile> newEntities) {

        return (List<Profile>) CollectionUtils.subtract(newEntities, oldEntities);
    }

    public static List<Profile> getRemovedProfiles(List<Profile> oldEntities, List<Profile> newEntities) {

        return (List<Profile>) CollectionUtils.subtract(oldEntities, newEntities);
    }

    public static List<Institution> getNewInstitutions(List<Institution> oldEntities, List<Institution> newEntities) {

        return (List<Institution>) CollectionUtils.subtract(newEntities, oldEntities);
    }

    public static List<Institution> getRemovedInstitutions(List<Institution> oldEntities, List<Institution> newEntities) {

        return (List<Institution>) CollectionUtils.subtract(oldEntities, newEntities);
    }
}
