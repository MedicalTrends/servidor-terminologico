package cl.minsal.semantikos.kernel.businessrules;


import cl.minsal.semantikos.kernel.components.AuthenticationManager;
import cl.minsal.semantikos.kernel.components.InstitutionManager;
import cl.minsal.semantikos.kernel.components.UserManager;
import cl.minsal.semantikos.kernel.factories.EmailFactory;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.exceptions.PasswordChangeException;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.users.UserFactory;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Hashtable;

import static cl.minsal.semantikos.model.users.ProfileFactory.MODELER_PROFILE;

/**
 * Este componente es responsable de almacenar las reglas de negocio relacionadas a la persistencia de usuarios.
 *
 * @author Andrés Farías
 */
@Singleton
public class InstitutionCreationBRImpl implements InstitutionCreationBR {

    private static final Logger logger = LoggerFactory.getLogger(InstitutionCreationBRImpl.class);

    @EJB
    InstitutionManager institutionManager;

    public void verifyPreConditions(Institution institution) throws BusinessRuleException {

        /* Reglas que aplican para todas las categorías */
        br401UniqueInstitutionCode(institution);
        /* Creación de acuerdo al rol */
        //br001creationRights(conceptSMTK, IUser);
    }

    /**
     * Esta regla de negocio valida que un rut debe ser único en el sistema.
     *
     * @param institution El usuario
     */
    public void br401UniqueInstitutionCode(Institution institution) {

        Institution found = institutionManager.getInstitutionByCode(institution.getCode());

        if(found != null) {
            throw new BusinessRuleException("BR-401-UniqueInstitutionCode", "Ya existe un establecimiento con este código en el sistema.");
        }
    }


}
