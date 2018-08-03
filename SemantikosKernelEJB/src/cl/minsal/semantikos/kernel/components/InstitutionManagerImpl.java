package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.businessrules.InstitutionCreationBR;
import cl.minsal.semantikos.kernel.businessrules.InstitutionDeletionBR;
import cl.minsal.semantikos.kernel.businessrules.UserCreationBR;
import cl.minsal.semantikos.kernel.daos.InstitutionDAO;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.users.*;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by des01c7 on 16-12-16.
 */
@Stateless
@SecurityDomain("SemantikosDomain")
@DeclareRoles({Roles.ADMINISTRATOR_ROLE, Roles.DESIGNER_ROLE, Roles.MODELER_ROLE, Roles.WS_CONSUMER_ROLE, Roles.REFSET_ADMIN_ROLE, Roles.QUERY_ROLE})
public class InstitutionManagerImpl implements InstitutionManager {

    @EJB
    private InstitutionDAO institutionDAO;

    @EJB
    private AuditManager auditManager;

    @EJB
    InstitutionCreationBR institutionCreationBR;

    @EJB
    InstitutionDeletionBR institutionDeletionBR;

    @Override
    @PermitAll
    public List<Institution> getInstitutionsBy(User user) {
        return institutionDAO.getInstitutionBy(user);
    }

    @Override
    @PermitAll
    public Institution getInstitutionById(long id) {
        return institutionDAO.getInstitutionById(id);
    }

    @Override
    @PermitAll
    public Institution getInstitutionByCode(long code) {
        return institutionDAO.getInstitutionByCode(code);
    }

    @Override
    @PermitAll
    public List<Institution> getAllInstitution() {
        return institutionDAO.getAllInstitution();
    }

    @Override
    @PermitAll
    public List<Institution> getValidInstitution() {
        List<Institution> validInstitutions = new ArrayList<>();

        for (Institution institution : institutionDAO.getAllInstitution()) {
            if(institution.getValidityUntil() == null) {
                validInstitutions.add(institution);
            }
        }

        return validInstitutions;
    }

    @Override
    @PermitAll
    public long createInstitution(Institution institution, User user) throws BusinessRuleException {

        /* Se validan las pre-condiciones para crear un usuario */
        try {
            institutionCreationBR.verifyPreConditions(institution);

            /* Se persisten los atributos basicos del usuario*/
            institutionDAO.createInstitution(institution);
            /* Se deja registro en la auditoría */
            auditManager.recordInstitutionCreation(institution, user);

            InstitutionFactory.getInstance().refresh(institution);

            return institution.getId();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    @PermitAll
    public void update(Institution originalInstitution, Institution updatedInstitution, User user) {

        boolean change = false;

        /* Primero de actualizan los campos propios del concepto */
        if(!originalInstitution.equals(updatedInstitution)) {
            /* Se actualiza con el DAO */
            institutionDAO.updateInstitution(updatedInstitution);

            auditManager.recordInstitutiuonUpgrade(updatedInstitution, user);

            InstitutionFactory.getInstance().refresh(updatedInstitution);

            change = true;
        }

        if(!change) {
            throw new EJBException("No es posible actualizar un establecimiento con una instancia idéntica!!");
        }

    }

    @Override
    @PermitAll
    public void deleteInstitution(Institution institution, User user, String deleteCause) {

        institutionDeletionBR.verifyPreConditions(institution);

        institution.setValidityUntil(new Timestamp(System.currentTimeMillis()));
        institutionDAO.updateInstitution(institution);
        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        auditManager.recordInstitutionDelete(institution, user, deleteCause);
        InstitutionFactory.getInstance().refresh(institution);
    }

    @Override
    @PermitAll
    public Institution bindInstitutionToUser(User user, Institution institution, User _user) {

        institutionDAO.bindInstitutionToUser(user, institution);

        /* Registrar en el Historial si es preferida (Historial BR) */
        auditManager.recordUserInstitutionBinding(user, institution, _user);

        /* Se retorna el establecimiento persistido */
        return institution;
    }

    @Override
    @PermitAll
    public void unbindInstitutionFromUser(User user, Institution institution, User _user) {
        institutionDAO.unbindInstitutionFromUser(user, institution);

        /* Registrar en el Historial si es preferida (Historial BR) */
        auditManager.recordUserInstitutionUnbinding(user, institution, _user);
    }

    @Override
    @PermitAll
    public InstitutionFactory getInstitutionFactory() {
        return InstitutionFactory.getInstance();
    }
}
