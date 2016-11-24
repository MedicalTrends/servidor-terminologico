package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.Profile;
import cl.minsal.semantikos.model.ProfileFactory;
import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;

import java.util.List;

import static cl.minsal.semantikos.model.ProfileFactory.DESIGNER_PROFILE;
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
