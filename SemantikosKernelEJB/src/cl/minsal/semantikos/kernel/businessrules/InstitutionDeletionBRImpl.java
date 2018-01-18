package cl.minsal.semantikos.kernel.businessrules;


import cl.minsal.semantikos.kernel.components.InstitutionManager;
import cl.minsal.semantikos.kernel.components.RefSetManager;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.users.UserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Singleton;

/**
 * Este componente es responsable de almacenar las reglas de negocio relacionadas a la persistencia de usuarios.
 *
 * @author Andrés Farías
 */
@Singleton
public class InstitutionDeletionBRImpl implements InstitutionDeletionBR {

    private static final Logger logger = LoggerFactory.getLogger(InstitutionDeletionBRImpl.class);

    @EJB
    RefSetManager refSetManager;

    public void verifyPreConditions(Institution institution) throws BusinessRuleException {

        /* Reglas que aplican para todas las categorías */
        br402ReferenceUser(institution);
        br403ReferenceRefSet(institution);
    }

    /**
     * Esta regla de negocio valida que un rut debe ser único en el sistema.
     *
     * @param institution El usuario
     */
    public void br402ReferenceUser(Institution institution) {

        for (User user : UserFactory.getInstance().getUsers()) {
            if(user.isValid() && user.getInstitutions().contains(institution)) {
                throw new BusinessRuleException("BR-402-ReferenceUser", "Este establecimiento está siendo actualmente por usuarios vigentes");
            }
        }
    }

    /**
     * Esta regla de negocio valida que un rut debe ser único en el sistema.
     *
     * @param institution El usuario
     */
    public void br403ReferenceRefSet(Institution institution) {

        for (RefSet refSet : refSetManager.getRefsetByInstitution(institution)) {
            if(refSet.isValid()) {
                throw new BusinessRuleException("BR-403-ReferenceRefSet", "Este establecimiento es actualmente dueño de RefSets en el sistema");
            }
        }
    }


}
