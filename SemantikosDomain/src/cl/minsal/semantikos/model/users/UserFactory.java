package cl.minsal.semantikos.model.users;

import cl.minsal.semantikos.model.categories.Category;

import javax.ejb.Singleton;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Andrés Farías
 */
public class UserFactory implements Serializable {

    private static final UserFactory instance = new UserFactory();

    /** La lista de tagSMTK */
    private List<User> users;

    public ConcurrentHashMap<Long, User> getUsersById() {
        return usersById;
    }

    public void setUsersById(ConcurrentHashMap<Long, User> usersById) {
        this.usersById = usersById;
    }

    /** Mapa de tagSMTK por su nombre. */
    private static ConcurrentHashMap<Long, User> usersById;

    /** Mapa de tagSMTK por su email. */
    private static ConcurrentHashMap<String, User> usersByEmail;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private UserFactory() {
        this.users = new ArrayList<>();
        this.usersById = new ConcurrentHashMap<>();
        this.usersByEmail = new ConcurrentHashMap<>();
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
     * Este método es responsable de retornar el tipo de descripción llamado FSN.
     *
     * @return Retorna una instancia de FSN.
     */
    public User findUserByEmail(String email) {

        if (usersByEmail.containsKey(email)) {
            return this.usersByEmail.get(email);
        }

        return null;
    }

    public List<User> getUsers() {
        return users;
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
            this.usersByEmail.put(user.getEmail(), user);
        }
    }

    public void refresh(User user) {
        if(!users.contains(user)) {
            users.add(user);
            usersById.put(user.getId(), user);
            usersByEmail.put(user.getEmail(), user);
        }
        else {
            users.remove(user);
            users.add(user);
            usersById.put(user.getId(), user);
            usersByEmail.put(user.getEmail(), user);
        }
    }

}
