package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCT;
import cl.minsal.semantikos.model.snomedct.RelationshipSCT;

import java.util.List;
import java.util.Map;

/**
 * @author Andrés Farías on 9/26/16.
 */
public interface SnomedCTManager {


    /**
     * Este método es responsable de recuperar las relaciones de un concepto SCT.
     *
     * @param conceptSCT El Identificador único del concepto SCT.
     *
     * @return Una lista de relaciones Snomed-CT donde el concepto Snomed-CT está en el origen de las relaciones.
     */
    public List<RelationshipSCT> getRelationshipsFrom(ConceptSCT conceptSCT);

    /**
     * Este método es responsable de buscar aquellos conceptos que posean al menos una descripción cuyo término
     * coincide con el patrón dado como parámetro.
     *
     * @param pattern El patrón de búsqueda.
     *
     * @return La lista de conceptos que satisfacen el criterio de búsqueda.
     */
    public List<ConceptSCT> findConceptsByPattern(String pattern);

    /**
     * Este método es responsable de buscar aquellos conceptos que posean al menos una descripción cuyo término
     * coincide con el patrón dado como parámetro.
     *
     * @param pattern El patrón de búsqueda.
     * @param group  El grupo por el cual se filtran los resultados.
     *
     * @return La lista de conceptos que satisfacen el criterio de búsqueda.
     */
    public List<ConceptSCT> findConceptsByPattern(String pattern, Integer group, int page, int pageSize);

    /**
     * Este método se encarga de entregar la cantidad de conceptos según patron, categoría y si esta modelado o no.
     * @param pattern    patrón de búsqueda
     * @param group    El grupo por el cual se filtran los resultados.
     * @return cantidad de conceptos según los parámetros ingresados
     */
    public long countConceptByPattern(String pattern, Integer group);

    /**
     * Este método es responsable de buscar aquellos conceptos que posean un CONCEPT_ID que coincida con el
     * <code>conceptIdPattern</code> dado como parámetro. El patron
     *
     * @param conceptIdPattern El concept ID por el cual se realiza la búsqueda.
     *
     * @return La lista de conceptos que satisfacen el criterio de búsqueda.
     */
    public List<ConceptSCT> findConceptsByConceptID(long conceptIdPattern);

    /**
     * Este método es responsable de buscar aquellos conceptos que posean un CONCEPT_ID que coincida con el
     * <code>conceptIdPattern</code> dado como parámetro. El patron
     *
     * @param conceptIdPattern El concept ID por el cual se realiza la búsqueda.
     * @param group  El grupo por el cual se filtran los resultados.
     *
     * @return La lista de conceptos que satisfacen el criterio de búsqueda.
     */
    public List<ConceptSCT> findConceptsByConceptID(long conceptIdPattern, Integer group);

    /**
     * Este método es responsable de buscar aquellos conceptos que posean al menos una descripción cuyo término
     * coincide con el patrón dado como parámetro.
     *
     * @param patron El patrón de búsqueda.
     *
     * @return La lista de las descripciones que coincidieron con el patrón de búsqueda, junto al concepto al que
     * pertenecen (dado que una descripción no conoce el concepto al que está asociada).
     */
    public Map<DescriptionSCT, ConceptSCT> findDescriptionsByPattern(String patron);

    /**
     * Este método es responsable de recuperar un concepto por su CONCEPT_ID.
     *
     * @param conceptID El CONCEPT_ID de negocio.
     *
     * @return El Concepto cuyo CONCEPT_ID corresponde a <code>conceptID</code>.
     */
    public ConceptSCT getConceptByID(long conceptID);

    /**
     * Este método es responsable de recuperar un concepto por su CONCEPT_ID.
     *
     * @param descriptionID El CONCEPT_ID de negocio.
     *
     * @return El Concepto cuyo CONCEPT_ID corresponde a <code>conceptID</code>.
     */
    public ConceptSCT getConceptByDescriptionID(long descriptionID);


    public DescriptionSCT getDescriptionByID(long id);

    /**
     * Este método es responsable de buscar y retornar todas las descripciones SNOMED que contienen el término dado como
     * parámetro.
     *
     * @return Una lista con descripciones que hacen perfect match.
     */
    public List<DescriptionSCT> searchDescriptionsPerfectMatch(String term, int page, int pageSize);

    /**
     * Este método es responsable de buscar y retornar todas las descripciones SNOMED que contienen parte del término
     * dado como parámetro.
     *
     * @return Una lista con descripciones que hacen truncate match.
     */
    public List<DescriptionSCT> searchDescriptionsTruncateMatch(String term, int page, int pageSize);

    /**
     * Este método es responsable de buscar y retornar todas las descripciones que contienen el término dado como
     * parámetro en cada una de las categorías y refsets indicadas.
     *
     * @return Una lista con descripciones que hacen truncate match.
     */
    public List<DescriptionSCT> searchDescriptionsSuggested(String term);

    /**
     * Este método es responsable de buscar y retornar todas las descripciones que contienen el término dado como
     * parámetro en cada una de las categorías y refsets indicadas.
     *
     * @return Una lista con descripciones que hacen truncate match.
     */
    public int countDescriptionsSuggested(String term);
}
