package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.ProfileFactory;
import cl.minsal.semantikos.model.users.User;

import java.util.List;

import static cl.minsal.semantikos.model.users.ProfileFactory.DESIGNER_PROFILE;

/**
 * @author Andrés Farías on 9/14/16.
 */
public class ConceptDeletionBR {
    public void preconditions(ConceptSMTK conceptSMTK, User user) {
        preCondition001(user);
    }

    /**
     * ﻿BR-CON-006 Solo usuarios con rol Diseñador y Modelador pueden eliminar conceptos.
     *
     * @param user El usuario que realiza la acción.
     */
    private void preCondition001(User user) {
        List<Profile> profiles = user.getProfiles();
        if (!profiles.contains(ProfileFactory.MODELER_PROFILE) && !profiles.contains(DESIGNER_PROFILE)) {
            throw new BusinessRuleException("BR-CON-006", "Un concepto sólo puede ser borrado por usuarios con el perfil Modelador o Diseñador");
        }
    }
}
