package cl.minsal.semantikos.model.users;

import javax.ejb.Singleton;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andrés Farías
 */
public class UserFactory implements Serializable {

    private static final UserFactory instance = new UserFactory();

    /** La lista de tagSMTK */
    private List<User> users;

    public Map<Long, User> getUsersById() {
        return usersById;
    }

    public void setUsersById(Map<Long, User> usersById) {
        this.usersById = usersById;
    }

    /** Mapa de tagSMTK por su nombre. */
    private Map<Long, User> usersById;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private UserFactory() {
        this.users = new ArrayList<>();
        this.usersById = new HashMap<>();
    }

    public static UserFactory getInstance() {
        return instance;
    }

    /**
     * Este método es responsable de retornar el tipo de descripción llamado FSN.
     *
     * @return Retorna una instancia de FSN.
     */
    public User findUserById(long id) {

        if (usersById.containsKey(id)) {
            return this.usersById.get(id);
        }

        return null;
    }

    /**
     * Este método es responsable de asignar un nuevo conjunto de tagsSMTJ. Al hacerlo, es necesario actualizar
     * los mapas.
     */
    public void setUsers(List<User> users) {

        /* Se actualiza la lista */
        this.users = users;

        /* Se actualiza el mapa por nombres */
        this.usersById.clear();
        for (User user : users) {
            this.usersById.put(user.getId(), user);
        }
    }

    public void refresh(User user) {
        if(!users.contains(user)) {
            users.add(user);
            usersById.put(user.getId(), user);
        }
        else {
            users.remove(user);
            users.add(user);
            usersById.put(user.getId(), user);
        }
    }

}
