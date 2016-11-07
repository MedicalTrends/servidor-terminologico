package cl.minsal.semantikos.kernel.auth;

import cl.minsal.semantikos.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by BluePrints Developer on 18-05-2016.
 */
@Stateless(name = "DummyAuthenticationEJB")
public class DummyAuthenticationBean extends AuthenticationMethod{

    static private final Logger logger = LoggerFactory.getLogger(DummyAuthenticationBean.class);

    public boolean authenticate(String username, String password, HttpServletRequest request) throws AuthenticationException{
        if("bpadmin".equals(username) && "bpadmin".equals(password))
            return true;

        throw new AuthenticationException("Usuario y/o Contraseña incorrecta");
    }

    public User getUser(String username) {

        if("bpadmin".equals(username) )
            return makeDummyAdminUser();

        return null;
    }

    private User makeDummyAdminUser() {
        User user = new User();

        user.setUsername("bpadmin");
        user.setEmail("admin@blueprintsit.cl");
        user.setName("Usuario Dummy Admin");
        //user.getRoles().add("admins");
        //user.getGroups().add("admins");

        return user;
    }

    @Override
    public void setUserPassword(String username, String password) throws PasswordChangeException {

    }


}
