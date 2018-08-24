package cl.minsal.semantikos.model.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Andrés Farías
 */
public class InstitutionFactory implements Serializable {

    private static final InstitutionFactory instance = new InstitutionFactory();

    /** La lista de tagSMTK */
    private List<Institution> institutions;

    /** Mapa de tagSMTK por su nombre. */
    private static ConcurrentHashMap<Long, Institution> institutionsById;

    public static Institution MINSAL;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private InstitutionFactory() {
        this.institutions = new ArrayList<>();
        institutionsById = new ConcurrentHashMap<>();
    }

    public static InstitutionFactory getInstance() {
        return instance;
    }

    public ConcurrentHashMap<Long, Institution> getInstitutionsById() {
        return institutionsById;
    }

    public void setInstitutionsById(ConcurrentHashMap<Long, Institution> institutionsById) {
        institutionsById = institutionsById;
    }

    /**
     * Este método es responsable de retornar el tipo de descripción llamado FSN.
     *
     * @return Retorna una instancia de FSN.
     */
    public Institution findInstitutionsById(long id) {

        if (institutionsById.containsKey(id)) {
            return this.getInstitutionsById().get(id);
        }

        return null;
    }

    /**
     * Este método es responsable de asignar un nuevo conjunto de tagsSMTJ. Al hacerlo, es necesario actualizar
     * los mapas.
     */
    public void setInstitutions(List<Institution> institutions) {

        /* Se actualiza la lista */
        this.institutions = institutions;

        /* Se actualiza el mapa por nombres */
        institutionsById.clear();

        for (Institution institution : institutions) {
            if(institution.getName().equalsIgnoreCase("MINSAL")) {
                MINSAL = institution;
            }
            institutionsById.put(institution.getId(), institution);
        }
    }

    public void refresh(Institution institution) {

        if(!institutions.contains(institution)) {
            institutions.add(institution);
            institutionsById.put(institution.getId(), institution);
        }
        else {
            institutions.remove(institution);
            institutions.add(institution);
            institutionsById.put(institution.getId(), institution);
        }

    }

    public List<Institution> getInstitutions() {
        return institutions;
    }
}
