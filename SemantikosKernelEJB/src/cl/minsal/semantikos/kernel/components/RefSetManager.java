package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Local;
import javax.ejb.Remote;
import java.util.List;

/**
 * @author Andrés Farías on 9/20/16.
 */
@Remote
public interface RefSetManager {

    /**
     * Este método es responsable de crear un RefSet.
     *
     * @param refSet refset para persistir
     * @param user   El usuario que crea el RefSet.
     * @return El RefSet creado.
     */
    public RefSet createRefSet(RefSet refSet, User user);

    /**
     * Este método es responsable de crear un RefSet.
     *
     * @param name        Nombre del RefSet. Nombre corto y descriptivo de su contenido, para identificación por
     *                    humanos
     *
     * @param institution La institución asociada.
     * @param user        El usuario que crea el RefSet.
     * @return El RefSet creado.
     */
    public RefSet createRefSet(String name, Institution institution, User user);

    /**
     * Este método es responsable de crear un RefSet.
     *
     * @param refSet El RefSet que se desea actualizar.
     * @param user   El usuario que crea el RefSet.
     *
     * @return El RefSet creado.
     */
    public RefSet updateRefSet(RefSet refSet, User user);

    /**
     * Este método es responsable de asociar una descripción a un RefSet.
     *
     * @param conceptSMTK La descripción que se desea agregar.
     * @param refSet      El RefSet al cual se desea asociar.
     * @param user        El usuario que realiza la acción.
     */
    public void bindConceptToRefSet(ConceptSMTK conceptSMTK, RefSet refSet, User user);

    /**
     * Este método es responsable de des-asociar una descripción a un RefSet.
     *
     * @param conceptSMTK La descripción que se desea agregar.
     * @param refSet      El RefSet al cual se desea asociar.
     * @param user        El usuario que realiza la acción.
     */
    public void unbindConceptToRefSet(ConceptSMTK conceptSMTK, RefSet refSet, User user);

    /**
     * Este método es responsable de dejar inválido un RefSet.
     *
     * @param refSet El RefSet que se desea invalidar.
     */
    public void invalidate(RefSet refSet, User user);

    /**
     * Este método es responsable de retornar la lista completa de RefSets.
     *
     * @return Una lista de RefSets.
     */
    public List<RefSet> getAllRefSets();

    /**
     * Este método es responsable de retornar la lista completa de RefSets válidos.
     *
     * @return Una lista de RefSets.
     */
    public List<RefSet> getValidRefSets();


    public List<RefSet> getRefsetsBy(ConceptSMTK conceptSMTK);

    public List<RefSet> getRefsetsBy(List<Long> categories, String pattern);

    /**
     * @param pattern Patron de nombre del REFSET buscado
     * @return Lista de REFSETs con nombre LIKE el patron ingresado (se espera lista con un solo elemento)
     */
    public List<RefSet> findRefsetsByName(String pattern);

    /**
     * Busca por un REFSET con el nombre dado y lo retorna
     * @param pattern nombre del refset
     * @return REFSET con el nombre buscado o null si no lo encuentra
     */
    public RefSet getRefsetByName(String pattern);

    /**
     * Entrega la lista de RefSets por la lista de nombres de refsets ingresados.
     * @param refSetNames
     * @return
     */
    public List<RefSet> findRefSetsByName(List<String> refSetNames);

    /**
     * Carga los RefSets a los que pertenece el concepto en el atributo refsets del concepto
     * @param conceptSMTK
     */
    public void loadConceptRefSets(ConceptSMTK conceptSMTK);

    /**
     * Busca los RefSets a los que pertenece el concepto
     * @param conceptSMTK
     * @return
     */
    public List<RefSet> findByConcept(ConceptSMTK conceptSMTK);

    List<RefSet> getRefsetByInstitution(Institution institution);
}
