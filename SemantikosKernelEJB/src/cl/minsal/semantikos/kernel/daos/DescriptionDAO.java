package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.*;

import javax.ejb.Local;
import java.util.List;


/**
 * @author Andrés Farías.
 */
@Local
public interface DescriptionDAO {

    public List<DescriptionType> getDescriptionTypes();

    public Description getDescriptionBy(long id);

    public Description getDescriptionBy(String businessId);

    /**
     * Este método es responsable de recuperar todas las descripciones de un concepto.
     *
     * @param conceptSMTK El Concepto cuyas descripciones se desea recuperar.
     *
     * @return La lista de las descripciones del concepto cuyo ID fue dado.
     */
    public List<Description> getDescriptionsByConcept(ConceptSMTK conceptSMTK);

    /**
     * Este método es responsable de buscar y retornar todas las descripciones que contienen el término dado como
     * parámetro en cada una de las categorías indicadas.
     *
     * @param term       El término buscado.
     * @param categories Las categorías en donde se realiza la búsqueda.
     *
     * @return Todas las descripciones que poseen exactamente el término <code>term</code>.
     */
    List<Description> searchDescriptionsByTerm(String term, List<Category> categories);

    /**
     * Este método es responsable de retornar un Factory.
     *
     * @return El factory adecuado... //TODO: WHAT?!
     */
    public DescriptionTypeFactory refreshDescriptionTypes();

    /**
     * Este método es responsable de persistir una descripción en la BDD. Luego de ser persistida, la descripción es
     * actualizada con su nuevo Identificador único.
     *
     * @param description La descripción a persistir.
     * @param user        El usuario que persiste la descripción.
     */
    public Description persist(Description description, User user);

    /**
     * Este método es responsable de persistir y asociar a un concepto todas las descripciones que no están
     * persistidas,
     * del conjunto de descripciones dadas como parámetro.
     *
     * @param descriptions Las descripciones que se deben persistir. Esta lista de descripciones puede contener
     *                     descripciones que ya se encuentran persistidas. Estas descripciones persistentes son
     *                     ignoradas y ningún cambio en ellas es realizado.
     * @param conceptSMTK  El concepto al cual se asocian las descripciones que se persisten.
     * @param user         El usuario que realiza la acción.
     *
     * @return La lista de descripciones persistidas.
     */
    public List<Description> persistNonPersistent(List<Description> descriptions, ConceptSMTK conceptSMTK, User user);

    /**
     * Este método es responsable de invalidar la descripción.
     *
     * @param description La descripción que se deben actualizar.
     */
    public void invalidate(Description description);

    /**
     * Este método es responsable de eliminar físicamente una descripción.
     *
     * @param description La descripción que se desea eliminar.
     */
    public void deleteDescription(Description description);

    /**
     * Este método es responsable de actualizar los datos de una descripción.
     *
     * @param description La descripción que será actualizada.
     */
    public void update(Description description);

    /**
     * Este método es responsable de persistir la información asociada al traslado de una descripción no válida al
     * concepto especial No Valido.
     *
     * @param noValidDescription La descripción que se mueve.
     */
    public void setInvalidDescription(NoValidDescription noValidDescription);

    public List<ObservationNoValid> getObservationsNoValid();
}
