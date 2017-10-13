package cl.minsal.semantikos.kernel.singletons;

import cl.minsal.semantikos.model.users.User;

import javax.ejb.Lock;
import javax.ejb.Singleton;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static javax.ejb.LockType.READ;
import static javax.ejb.LockType.WRITE;

/**
 * @author Andrés Farías
 */
@Singleton
public class UserSingleton implements Serializable {

    /** La lista de tagSMTK */
    private List<User> users = new ArrayList<>();

    /** Mapa de tagSMTK por su nombre. */
    private static ConcurrentHashMap<Long, User> usersById = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, User> getUsersById() {
        return usersById;
    }

    public void setUsersById(ConcurrentHashMap<Long, User> usersById) {
        this.usersById = usersById;
    }

    /**
     * Este método es responsable de retornar el tipo de descripción llamado FSN.
     *
     * @return Retorna una instancia de FSN.
     */
    @Lock(READ)
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
    @Lock(WRITE)
    public void setUsers(List<User> users) {

        /* Se actualiza la lista */
        this.users = users;

        /* Se actualiza el mapa por nombres */
        this.usersById.clear();
        for (User user : users) {
            this.usersById.put(user.getId(), user);
        }
    }

    @Lock(WRITE)
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
