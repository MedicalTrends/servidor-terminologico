package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.model.users.User;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;

/**
 * TODO: Eliminar esta interfaz, no es necesaria.
 */

@Local
public interface UserCreationBRInterface {

    public void br301UniqueDocumentNumber(User user);

    public void br302UniqueEmail(User user);

    public void br303ValidEmail(User user);

    public User br307verificationCodeExists(String key);

    public void verifyPreConditions(User user);

    public void preActions(User user);

    public void postActions(User user, HttpServletRequest request);

}