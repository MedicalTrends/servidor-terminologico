package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.audit.AuditActionType;
import cl.minsal.semantikos.model.audit.ConceptAuditAction;
import cl.minsal.semantikos.model.audit.InstitutionAuditAction;
import cl.minsal.semantikos.model.audit.UserAuditAction;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.crossmaps.Crossmap;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Local;
import javax.ejb.Remote;
import java.util.List;

/**
 * @author Andrés Farías
 */
@Remote
public interface AuditManager {


    /**
     * API Para Objeto auditable ConceptSMTK
     */

    /**
     * Este método es responsable de registrar en el log de auditoría la creación del concepto, por el usuario.
     * Este método solo registra la creación del concepto, y no de cada una de sus relaciones o descripciones.
     *
     * @param conceptSMTK El concepto que se creo.
     * @param user        El usuario que creó el concepto.
     */
    public void recordNewConcept(ConceptSMTK conceptSMTK, User user);

    /**
     * Este método es responsable de registrar en el log de auditoría la actualización del concepto, por el usuario.
     * Este método solo registra la creación del concepto, y no de cada una de sus relaciones o descripciones.
     *
     * @param conceptSMTK El concepto que se creo.
     * @param user        El usuario que creó el concepto.
     */
    public void recordUpdateConcept(ConceptSMTK conceptSMTK, User user);

    /**
     * Este método es responsable de registrar en el log de auditoría la el traslado de un concepto.
     *
     * @param sourceConcept El concepto en donde se encuentra la descripción inicialmente.
     * @param targetConcept El concepto al cual se quiere mover la descripción.
     * @param description   La descripción que se desea trasladar.
     */
    public void recordDescriptionMovement(ConceptSMTK sourceConcept, ConceptSMTK targetConcept, Description description, User user);

    /**
     * Este método es responsable de registrar en el log de auditoría la el traslado de un concepto.
     *
     * @param sourceConcept El concepto en donde se encuentra la descripción inicialmente.
     * @param user          El usuario que publicó el concepto.
     */
    public void recordConceptPublished(ConceptSMTK sourceConcept, User user);

    /**
     * Este método es responsable de registrar en el historial el cambio de la descripción
     * <code>originalDescription</code>.
     *
     * @param conceptSMTK         El concepto al cual pertenece la descripción actualizada.
     * @param originalDescription La descripción original, sin el cambio.
     * @param user                El usuario que realizó la actualización.
     */
    public void recordFavouriteDescriptionUpdate(ConceptSMTK conceptSMTK, Description originalDescription, User user);

    /**
     * Este método es responsable de registrar en el historial el cambio de categoría de un concepto.
     *
     * @param conceptSMTK      El concepto cuya categoría cambia.
     * @param originalCategory La categoría original, antes del cambio.
     * @param user             El usuario que realiza el cambio.
     */
    public void recordConceptCategoryChange(ConceptSMTK conceptSMTK, Category originalCategory, User user);

    /**
     * Este método es responsable de registrar en el historial el cambio de una relación atributo del concepto.
     *
     * @param conceptSMTK          El concepto cuya relación se actualizó.
     * @param originalRelationship La relación antes de la modificación.
     * @param user                 El usuario que realizó la modificación.
     */
    public void recordAttributeChange(ConceptSMTK conceptSMTK, Relationship originalRelationship, User user);

    /**
     * API Para Objeto auditable Relationship
     */

    /**
     * Este método es responsable de registrar en el historial las creación de una relación.
     *
     * @param relationship La relación que se ha persistido.
     * @param user         El usuario responsable de la acción.
     */
    public void recordRelationshipCreation(Relationship relationship, User user);

    /**
     * Este método es responsable de registrar en el historial la eliminación de una relación.
     *
     * @param relationship La relación que se desea eliminar.
     * @param user         El usuario que elimina la relación.
     */
    public void recordRelationshipRemoval(Relationship relationship, User user);

    /**
     * API Para Objeto auditable CrossMap
     */

    /**
     * Este método es responsable de registrar en el historial la creación de un CrossMap.
     *
     * @param crossmap El CrossMap que se desea crear.
     * @param user     El usuario que elimina la relación.
     */
    public void recordCrossMapCreation(Crossmap crossmap, User user);

    /**
     * Este método es responsable de registrar en el historial la eliminación de un CrossMap.
     *
     * @param crossmap El CrossMap que se desea eliminar.
     * @param user     El usuario que elimina la relación.
     */
    public void recordCrossMapRemoval(Crossmap crossmap, User user);

    /**
     * API Para Objeto auditable Description
     */

    /**
     * Este método es responsable de registrar en el historial la eliminación de un CrossMap.
     *
     * @param description La descripción qeu se está creando.
     * @param user        El usuario que realiza la acción.
     */
    public void recordDescriptionCreation(Description description, User user);

    /**
     * Este método es responsable de registrar en el historial la eliminación de una Descripción.
     *
     * @param conceptSMTK El concepto cuya descripción se desea eliminar.
     * @param description La descripción que se desea eliminar.
     * @param user        El usuario que desea eliminar la descripción.
     */
    public void recordDescriptionDeletion(ConceptSMTK conceptSMTK, Description description, User user);

    /**
     * Este método es responsable de registrar en el historial la eliminación de una Descripción.
     *
     * @param conceptSMTK El concepto que se desea eliminar.
     * @param user        El usuario que desea eliminar el concepto.
     */
    public void recordConceptInvalidation(ConceptSMTK conceptSMTK, User user);

    /**
     * API Para Objeto auditable RefSet
     */

    /**
     * Este método es responsable de registrar en el historial la creación de un RefSet.
     *
     * @param refSet El RefSet que se crea.
     * @param user   El usuario que crea el RefSet.
     */
    public void recordRefSetCreation(RefSet refSet, User user);

    /**
     * Este método es responsable de registrar en el historial la actualización de un RefSet.
     *
     * @param refSet El RefSet que se actualiza.
     * @param user   El usuario que crea el RefSet.
     */
    public void recordRefSetUpdate(RefSet refSet, User user);

    /**
     * Este método es responsable de registrar en el historia la asociación de una descripción a un RefSet.
     *
     * @param refSet      El RefSet que se registra en el historial.
     * @param conceptSMTK El concepto que se asocia al RefSet.
     * @param user        El usuario que realiza la acción.
     */
    public void recordRefSetBinding(RefSet refSet, ConceptSMTK conceptSMTK, User user);

    /**
     * Este método es responsable de registrar en el historia la des-asociación de una descripción a un RefSet.
     *
     * @param refSet      El RefSet que se registra en el historial.
     * @param conceptSMTK La Descripción que se des-asocia al RefSet.
     * @param user        El usuario que realiza la acción.
     */
    public void recordRefSetUnbinding(RefSet refSet, ConceptSMTK conceptSMTK, User user);

    /**
     * Este método es responsable de registrar en el historia cuando un RefSet se deje no vigente.
     *
     * @param refSet      El RefSet que se registra en el historial.
     * @param user        El usuario que realiza la acción.
     */
    public void recordRefSetInvalidate(RefSet refSet, User user);

    /**
     * API Para Objeto auditable User
     */

    /**
     * Este método es responsable de registrar en el log de auditoría la creación del usuario, por el usuario.
     * Este método solo registra la creación del usuario, y no de cada una de sus perfiles o establecimientos.
     *
     * @param user        El usuario que se creo.
     * @param user        El usuario que creó el usuario.
     */
    public void recordUserCreation(User user, User _user);

    /**
     * Este método es responsable de registrar en el log de auditoría la actualización del usuario, por el usuario.
     * Este método solo registra la creación del usuario, y no de cada una de sus perfiles o establecimientos.
     *
     * @param user         El usuario que se creo.
     * @param _user        El usuario que creó el usuario.
     */
    public void recordUserUpgrade(User user, User _user);

    /**
     * Este método es responsable de registrar en el log de auditoría el enlace de un perfil a un usuario
     *
     * @param user          El usuario al cual se enlaza el perfil
     * @param profile       El perfil
     * @param _user         El usuario que hace el enlace.
     */
    public void recordUserProfileBinding(User user, Profile profile, User _user);

    /**
     * Este método es responsable de registrar en el log de auditoría la desenlace de un perfil a un usuario
     *
     * @param user          El usuario al cual se desenlaza el perfil
     * @param profile       El perfil
     * @param _user         El usuario que hace el desenlace.
     */
    public void recordUserProfileUnbinding(User user, Profile profile, User _user);

    /**
     * Este método es responsable de registrar en el log de auditoría el enlace de un establecimiento a un usuario
     *
     * @param user          El usuario al cual se enlaza el establecimiento
     * @param institution   El establecimiento
     * @param _user         El usuario que hace el enlace.
     */
    public void recordUserInstitutionBinding(User user, Institution institution, User _user);

    /**
     * Este método es responsable de registrar en el log de auditoría la desenlace de un establecimiento a un usuario
     *
     * @param user          El usuario al cual se enlaza el establecimiento
     * @param institution   El establecimiento
     * @param _user         El usuario que hace el desenlace.
     */
    public void recordUserInstitutionUnbinding(User user, Institution institution, User _user);

    /**
     * Este método es responsable de registrar en el log de auditoría la activacion de un usuario
     *
     * @param user          El usuario al cual activa
     * @param _user         El usuario que hace la activacion
     */
    public void recordUserActivation(User user, User _user);

    /**
     * Este método es responsable de registrar en el log de auditoría la modificacion de contraseña de un usuario
     *
     * @param user          El usuario al cual se cambia contraseña
     * @param _user         El usuario que hace el cambio
     */
    public void recordUserPasswordChange(User user, User _user);

    /**
     * Este método es responsable de registrar en el log de auditoría la recuperacion de contraseña de un usuario
     *
     * @param user          El usuario al cual se recupera contraseña
     * @param _user         El usuario que hace la recuperacion
     */
    public void recordUserPasswordRecover(User user, User _user);

    /**
     * Este método es responsable de registrar en el log de auditoría el bloqueo de la cuenta de un usuario
     *
     * @param user          El usuario al cual se bloquea su cuenta
     * @param _user         El usuario que hace el bloqueo
     */
    public void recordUserLocking(User user, User _user);

    /**
     * Este método es responsable de registrar en el log de auditoría el bloqueo de la cuenta de un usuario
     *
     * @param user          El usuario al cual se bloquea su cuenta
     * @param _user         El usuario que hace el bloqueo
     */
    public void recordUserUnlocking(User user, User _user);

    /**
     * Este método es responsable de registrar en el log de auditoría la eliminacion de un usuario
     *
     * @param user          El usuario al cual se elimina
     * @param _user         El usuario que hace la eliminacion
     */
    public void recordUserDelete(User user, User _user);

    /**
     * Este método es responsable de registrar en el log de auditoría el reseteo de la cuenta de un usuario
     *
     * @param user          El usuario al cual se resetea su cuenta
     * @param _user         El usuario que hace el reseteo
     */
    public void recordUserAccountReset(User user, User _user);


    /**
     * API Para Objeto auditable Institution
     */

    /**
     * Este método es responsable de registrar en el log de auditoría la creación del establecimiento, por el usuario.
     *
     * @param institution        El establecimiento que se creo.
     * @param user        El usuario que creó el establecimiento.
     */
    public void recordInstitutionCreation(Institution institution, User user);

    /**
     * Este método es responsable de registrar en el log de auditoría la actualización del establecimiento, por el usuario.
     *
     * @param institution         El establecimiento que se actualizo.
     * @param user        El usuario que actualizo el establecimiento.
     */
    public void recordInstitutiuonUpgrade(Institution institution, User user);

    /**
     * Este método es responsable de registrar en el log de auditoría la eliminacion de un establecimiento
     *
     * @param institution          El establecimiento al cual se elimina
     * @param user         El usuario que hace la eliminacion
     */
    public void recordInstitutionDelete(Institution institution, User user, String deleteCause);

    /**
     * Este método es responsable de obtener y agrupar en una lista todos los tipos de cambios existentes.
     *
     * @return Una <code>List</code> con los tipos de cambio.
     */
    public List<AuditActionType> getAllAuditActionTypes();

    /**
     * Este método es responsable de recuperar y retornar en una lista los últimos <code>numberOfChanges</code> cambios
     * que ha tenido un concepto.
     *
     * @param conceptSMTK     El concepto cuyos cambios se desean recuperar.
     * @param changes         Indica si se desean las acciones auditables registradas que son cambios
     *
     * @return Una lista con los últimos <code>numberOfChanges</code> realizados sobre el concepto
     * <code>conceptSMTK</code>
     */
    public List<ConceptAuditAction> getConceptAuditActions(ConceptSMTK conceptSMTK,  boolean changes);

    /**
     * Este método es responsable de recuperar y retornar en una lista los últimos <code>numberOfChanges</code> cambios
     * que ha tenido un usuario.
     *
     * @param user     El usuario cuyos cambios se desean recuperar.
     *
     * @return Una lista con los últimos <code>numberOfChanges</code> realizados sobre el concepto
     * <code>conceptSMTK</code>
     */
    public List<UserAuditAction> getUserAuditActions(User user);

    /**
     * Este método es responsable de recuperar y retornar en una lista los últimos <code>numberOfChanges</code> cambios
     * que ha tenido un establecimiento.
     *
     * @param institution     El establecimiento cuyos cambios se desean recuperar.
     *
     * @return Una lista con los últimos <code>numberOfChanges</code> realizados sobre el establecimiento
     * <code>Institution</code>
     */
    public List<InstitutionAuditAction> getInstitutionAuditActions(Institution institution);

    /**
     * Este método es responsable de recuperar y retornar en una lista los últimos <code>numberOfChanges</code> cambios
     * que ha tenido un concepto.
     *
     * @param conceptSMTK     El concepto cuyos cambios se desean recuperar.
     * @param changes         Indica si se desean las acciones auditables registradas que son cambios
     *
     * @return Una lista con los últimos <code>numberOfChanges</code> realizados sobre el concepto
     * <code>conceptSMTK</code>
     */
    public ConceptAuditAction getConceptCreationAuditAction(ConceptSMTK conceptSMTK,  boolean changes);

    /**
     * Este método es responsable de recuperar y retornar en una lista los últimos <code>numberOfChanges</code> cambios
     * que ha tenido un concepto.
     *
     * @param conceptSMTK     El concepto cuyos cambios se desean recuperar.
     * @param changes         Indica si se desean las acciones auditables registradas que son cambios
     *
     * @return Una lista con los últimos <code>numberOfChanges</code> realizados sobre el concepto
     * <code>conceptSMTK</code>
     */
    public ConceptAuditAction getConceptPublicationAuditAction(ConceptSMTK conceptSMTK,  boolean changes);


}
