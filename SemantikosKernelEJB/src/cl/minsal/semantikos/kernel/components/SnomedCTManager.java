package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.snomedct.*;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

/**
 * @author Andrés Farías on 9/26/16.
 */
@Local
public interface SnomedCTManager {

    /**
     * Este método es responsable de procesar un snapshot de Snomed CT.
     *
     * @param snomedCTSnapshot El Snapshot que será procesado.
     *
     * @return El resultado del proceso.
     */
    public SnapshotProcessingResult processSnapshot(SnomedCTSnapshot snomedCTSnapshot);

    /**
     * Este método es responsable de recuperar las relaciones de un concepto SCT.
     *
     * @param idConceptSCT El Identificador único del concepto SCT.
     *
     * @return Una lista de relaciones Snomed-CT donde el concepto Snomed-CT está en el origen de las relaciones.
     */
    public List<RelationshipSCT> getRelationshipsFrom(long idConceptSCT);

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
     * @param patron El patrón de búsqueda.
     *
     * @return La lista de las descripciones que coincidieron con el patrón de búsqueda, junto al concepto al que
     * pertenecen (dado que una descripción no conoce el concepto al que está asociada).
     */
    public Map<DescriptionSCT, ConceptSCT> findDescriptionsByPattern(String patron);

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
     * Este método es responsable de recuperar un concepto por su CONCEPT_ID.
     *
     * @param conceptID El CONCEPT_ID de negocio.
     *
     * @return El Concepto cuyo CONCEPT_ID corresponde a <code>conceptID</code>.
     */
    public ConceptSCT getConceptByID(long conceptID);
}
