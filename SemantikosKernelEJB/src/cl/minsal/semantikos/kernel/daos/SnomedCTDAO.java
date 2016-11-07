package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCT;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

/**
 * @author Andrés Farías on 10/25/16.
 */
@Local
public interface SnomedCTDAO {

    /**
     * Este método es responsable de buscar Conceptos SnomedCT a partir de un patrón de texto.
     *
     * @param pattern El patrón por el cual se busca el Concept Snomed.
     *
     * @return Una lista
     */
    List<ConceptSCT> findConceptsBy(String pattern);

    /**
     * Este método es responsable de recuperar un concepto por su CONCEPT_ID.
     *
     * @param conceptID El CONCEPT_ID de negocio.
     *
     * @return El Concepto cuyo CONCEPT_ID corresponde a <code>conceptID</code>.
     */
    public ConceptSCT getConceptByID(long conceptID);

    /**
     * Este método es responsable de buscar aquellos conceptos que posean un CONCEPT_ID que coincida con el
     * <code>conceptIdPattern</code> dado como parámetro. El patron
     *
     * @param conceptIdPattern El concept ID por el cual se realiza la búsqueda.
     *
     * @return La lista de conceptos que satisfacen el criterio de búsqueda.
     */
    List<ConceptSCT> findConceptsByConceptID(long conceptIdPattern);

    /**
     * Este método es responsable de buscar aquellos conceptos que posean al menos una descripción cuyo término
     * coincide con el patrón dado como parámetro.
     *
     * @param pattern El patrón de búsqueda.
     *
     * @return La lista de las descripciones que coincidieron con el patrón de búsqueda, junto al concepto al que
     * pertenecen (dado que una descripción no conoce el concepto al que está asociada).
     */
    Map<DescriptionSCT, ConceptSCT> findDescriptionsByPattern(String pattern);
}
