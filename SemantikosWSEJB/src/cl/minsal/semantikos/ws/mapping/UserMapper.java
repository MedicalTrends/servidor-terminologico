package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.ws.response.UserResponse;

/**
 * Created by Development on 2016-10-13.
 *
 */
public class UserMapper {

    public static UserResponse map(User user) {
        if ( user != null ) {
            UserResponse res = new UserResponse();
            res.setName(user.getName());
            res.setEmail(user.getEmail());
            res.setLastLogin(user.getLastLogin());
            res.setLastName(user.getLastName());
            res.setLastPasswordChange(user.getLastPasswordChange());
            res.setRut(user.getRut());
            res.setSecondLastName(user.getSecondLastName());
            res.setUsername(user.getUsername());
            return res;
        } else {
            return null;
        }
    }

}
