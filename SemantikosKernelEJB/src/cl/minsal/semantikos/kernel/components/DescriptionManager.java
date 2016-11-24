package cl.minsal.semantikos.kernel.components;


import cl.minsal.semantikos.model.*;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Diego Soto on 07-06-16.
 */
@Local
public interface DescriptionManager {

    /**
     * Este método es responsable de crear en el repositorio terminológico una nueva descripción.
     *
     * @param description La descripción que se desea crear.
     * @param user        El usuario que realiza la acción.
     */
    public void createDescription(Description description, boolean editionMode, User user);

    /**
     * Este método es responsable de asociar (agregar) una descripción a un concepto.
     *
     * @param concept El concepto al cual se agrega la descripción.
     * @param term    El término de la descripción.
     * @param type    El tipo de la descripción.
     * @param user    El usuario que agrega el término
     *
     * @return La descripción creada a partir del término dado.
     */
    public Description bindDescriptionToConcept(ConceptSMTK concept, String term, DescriptionType type, User user);

    /**
     * Este método es responsable de asociar (agregar) una descripción a un concepto.
     *
     * @param concept     El concepto al cual se agrega la descripción.
     * @param description La descripción que será asociada al concepto. Esta puede o no estar persistida.
     * @param user        El usuario que agrega el término
     *
     * @return La descripción creada a partir del término dado.
     */
    public Description bindDescriptionToConcept(ConceptSMTK concept, Description description, boolean editionMode, User user);

    /**
     * Este método es responsable de des-asociar (eliminar) una descripción de un concepto.
     *
     * @param concept     El concepto al cual se agrega la descripción.
     * @param description La descripción que será asociada al concepto. Esta puede o no estar persistida.
     * @param user        El usuario que agrega el término
     *
     * @return La descripción creada a partir del término dado.
     */
    public Description unbindDescriptionToConcept(ConceptSMTK concept, Description description, User user);

    /**
     * Este método es responsable de actualizar la descripción de un concepto.
     *
     * @param conceptSMTK      El concepto al cual se realiza la actualización de una descripción.
     * @param original         La descripción original.
     * @param finalDescription La descripción actualizada.
     * @param user             El usuario que realiza la actualización.
     */
    public void updateDescription(ConceptSMTK conceptSMTK, Description original, Description finalDescription, User user);

    /**
     * Este método es responsable de eliminar lógicamente una descripción.
     *
     * @param description La descripción que se desea eliminar.
     * @param user        El usuario que realiza la eliminación.
     */
    public void deleteDescription(Description description, User user);

    /**
     * Este método es responsable de mover una descripción (<code>description</code>) asociada a un concepto
     * (<code>sourceConcept</code>) a otro concepto (<code>targetConcept</code>)
     *
     * @param sourceConcept Concepto que contiene a la descripcion.
     * @param description   La descripción que se desea trasladar.
     * @param user          El usuario que realiza el traslado.
     */
    public void moveDescriptionToConcept(ConceptSMTK sourceConcept, Description description, User user);

    public String getIdDescription(String tipoDescription);

    /**
     * Método encargado de obtener todos los tipos de descripciones
     *
     * @return Lista de tipos de descripciones
     */
    public List<DescriptionType> getAllTypes();

    public List<Description> findDescriptionsByConcept(int idConcept);

    public DescriptionType getTypeFSN();

    public DescriptionType getTypeFavorite();

    /**
     * Este método es responsable de recuperar las descripciones de un concepto.
     *
     * @param concept El concepto cuyas descripciones deben ser recuperadas.
     *
     * @return Un objeto <code>java.util.List</code> con las descripciones del concepto <code>concept</code>.
     */
    List<Description> getDescriptionsOf(ConceptSMTK concept);

    /**
     * Este método es responsable de generar un description id
     *
     * @return Un objeto <code>java.util.List</code> con las descripciones del concepto <code>concept</code>.
     */
    public String generateDescriptionId();

    /**
     * Este método es responsable de buscar y retornar todas las descripciones que contienen el término dado como
     * parámetro en cada una de las categorías indicadas.
     *
     * @param term       El término buscado.
     * @param categories Las categorías en donde se realiza la búsqueda.
     *
     * @return Todas las descripciones que poseen exactamente el término <code>term</code>.
     */
    public List<Description> searchDescriptionsByTerm(String term, List<Category> categories);

    /**
     * Este método es responsable de buscar y retornar todas las descripciones que contienen el término dado como
     * parámetro en cada una de las categorías y refsets indicadas.
     * @param term
     * @param categories
     * @param refSets
     * @return
     */
    public List<Description> searchDescriptionsByTerm(String term, List<Category> categories, List<RefSet> refSets);

    /**
     * Este método es responsable de hacer que una descripción sea no válida en el sistema.
     *
     * @param noValidDescription La descripción no válida con su observación y conceptos sugeridos.
     * @param user               El usuario que realiza la acción.
     */
    public void invalidateDescription(ConceptSMTK conceptSMTK, NoValidDescription noValidDescription, User user);


    public List<ObservationNoValid> getObservationsNoValid();
}
