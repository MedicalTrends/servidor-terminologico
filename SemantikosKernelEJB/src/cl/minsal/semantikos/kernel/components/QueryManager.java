package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.browser.*;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.NoValidDescription;
import cl.minsal.semantikos.model.descriptions.PendingTerm;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by BluePrints Developer on 21-09-2016.
 */
@Local
public interface QueryManager {

    /**
     * Este método es responsable de inicializar un objeto de consulta para el navegador de categorías,
     * Dada una categoría en particular.
     *
     * @param category La categoría a consultar
     */
    public GeneralQuery getDefaultGeneralQuery(Category category);

    /**
     * Este método es responsable de inicializar un objeto de consulta para el navegador de descripciones,
     *
     */
    public DescriptionQuery getDefaultDescriptionQuery();

    /**
     * Este método es responsable de inicializar un objeto de consulta para el navegador de no válidos,
     *
     */
    public NoValidQuery getDefaultNoValidQuery();

    /**
     * Este método es responsable de inicializar un objeto de consulta para el navegador de pendientes,
     *
     */
    public PendingQuery getDefaultPendingQuery();


    /**
     * Este método es responsable de inicializar un objeto de consulta para el browser,
     *
     */
    public BrowserQuery getDefaultBrowserQuery();

    /**
     * Este método es responsable de ejecutar una consulta en el navegador de categorías,
     * Dado el objeto de consulta correspondiente
     *
     * @param query El objeto de consulta para el navegador de categorías
     */
    public List<ConceptSMTK> executeQuery(GeneralQuery query);

    /**
     * Este método es responsable de ejecutar una consulta en el navegador de descripciones,
     * Dado el objeto de consulta correspondiente
     *
     * @param query El objeto de consulta para el navegador de descripciones
     */
    public List<Description> executeQuery(DescriptionQuery query);

    /**
     * Este método es responsable de ejecutar una consulta en el navegador de no válidos,
     * Dado el objeto de consulta correspondiente
     *
     * @param query El objeto de consulta para el navegador de no válidos
     */
    public List<NoValidDescription> executeQuery(NoValidQuery query);

    /**
     * Este método es responsable de ejecutar una consulta en el navegador de pendientes,
     * Dado el objeto de consulta correspondiente
     *
     * @param query El objeto de consulta para el navegador de pendientes
     */
    public List<PendingTerm> executeQuery(PendingQuery query);

    /**
     * Este método es responsable de ejecutar una consulta en el browser,
     * Dado el objeto de consulta correspondiente
     *
     * @param query El objeto de consulta para el browser
     */
    public List<ConceptSMTK> executeQuery(BrowserQuery query);

    /**
     * Este método es responsable de contabilizar los resultados de una consulta
     * Dado el objeto de consulta
     *
     * @param query El objeto de consulta
     */
    public int countQueryResults(Query query);

    /**
     * Este método es responsable de obtener los atributos filtrables de una categoría para el navegador de categorias,
     * Dado una categoría en particular
     *
     * @param category El objeto de consulta para el navegador de pendientes
     */
    public List<RelationshipDefinition> getSearchableAttributesByCategory(Category category);

    /**
     * Este método es responsable de obtener los atributos desplegables de una categoría para el navegador de categorias,
     * Dado una categoría en particular
     *
     * @param category El objeto de consulta para el navegador de pendientes
     */
    public List<RelationshipDefinition> getShowableAttributesByCategory(Category category);

}
