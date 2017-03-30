package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.RefSet;
import cl.minsal.semantikos.model.snomedct.*;

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
     * @param group   El grupo utilizado como filtro.
     *
     * @return Una lista
     */
    List<ConceptSCT> findConceptsBy(String pattern, Integer group);

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
     * @param group            El grupo usado como filtro.
     *
     * @return La lista de conceptos que satisfacen el criterio de búsqueda.
     */
    List<ConceptSCT> findConceptsByConceptID(long conceptIdPattern, Integer group);

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

    /**
     * Este método es responsable de buscar aquellos conceptos que posean un CONCEPT_ID que coincida con el
     * <code>conceptIdPattern</code> dado como parámetro. El patron
     *
     * @param pattern El concept ID por el cual se realiza la búsqueda.
     * @param group            El grupo usado como filtro.
     *
     * @return La lista de conceptos que satisfacen el criterio de búsqueda.
     */
    List<ConceptSCT> findPerfectMatch(String pattern, Integer group);

    /**
     * Este método es responsable de buscar aquellos conceptos que posean un CONCEPT_ID que coincida con el
     * <code>conceptIdPattern</code> dado como parámetro. El patron
     *
     * @param pattern El concept ID por el cual se realiza la búsqueda.
     * @param group            El grupo usado como filtro.
     *
     * @return La lista de conceptos que satisfacen el criterio de búsqueda.
     */
    List<ConceptSCT> findTruncateMatch(String pattern, Integer group);

    /**
     * Este método es responsable de buscar aquellos conceptos que posean un CONCEPT_ID que coincida con el
     * <code>conceptIdPattern</code> dado como parámetro. El patron
     *
     * @param pattern El concept ID por el cual se realiza la búsqueda.
     * @param group            El grupo usado como filtro.
     *
     * @return La lista de conceptos que satisfacen el criterio de búsqueda.
     */
    long countPerfectMatch(String pattern, Integer group);

    /**
     * Este método es responsable de buscar aquellos conceptos que posean un CONCEPT_ID que coincida con el
     * <code>conceptIdPattern</code> dado como parámetro. El patron
     *
     * @param pattern El concept ID por el cual se realiza la búsqueda.
     * @param group            El grupo usado como filtro.
     *
     * @return La lista de conceptos que satisfacen el criterio de búsqueda.
     */
    long countTruncateMatch(String pattern, Integer group);

    /**
     * Método encargado de persistir un concepto SNOMED CT
     */
    public void persistSnapshotConceptSCT(List<ConceptSCT> conceptSCTs);

    /**
     * Método encargado de persistir una descripción SNOMED CT
     * @param descriptionSCT
     */
    public void persistSnapshotDescriptionSCT(List<DescriptionSCT> descriptionSCTs);

    /**
     * Método encargado de persistir una relación SNOMED CT
     * @param relationshipSnapshotSCT
     */
    public void persistSnapshotRelationshipSCT(List<RelationshipSnapshotSCT> relationshipSnapshotSCT);

    /**
     * Método encargado de persistir transitivos de SNOMED CT
     * @param transitiveSCT
     */
    public void persistSnapshotTransitiveSCT(TransitiveSCT transitiveSCT);

    /**
     * Método encargado de persisitir un lenguaje de RefSet SNOMED CT
     * @param languageRefsetSCT
     */
    public void persistSnapshotLanguageRefSetSCT(LanguageRefsetSCT languageRefsetSCT);


    /**
     * Método encargado de actualizar los atributos de un concepto SNOMED CT
     * @param conceptSCT
     */
    public void updateSnapshotConceptSCT(ConceptSCT conceptSCT);

    /**
     * Método encargado de actualizar los atributos de una descripción SNOMED CT
     * @param descriptionSCT
     */
    public void updateSnapshotDescriptionSCT(DescriptionSCT descriptionSCT);

    /**
     * Método encargado de actualizar una relación de SNOMED CT
     * @param relationshipSnapshotSCT
     */
    public void updateSnapshotRelationshipSCT(RelationshipSnapshotSCT relationshipSnapshotSCT);

    /**
     * Método encargado de actualizar los atributos de un lenguaje RefSet de SNOMED CT
     * @param languageRefsetSCT
     */
    public void updateSnapshotLanguageRefSetSCT(LanguageRefsetSCT languageRefsetSCT);

    /**
     * Metodo encargado de eliminar un registro de transitivos de SNOMED CT
     */
    public void deleteSnapshotTransitiveSCT(TransitiveSCT transitiveSCT);


    /**
     * Método encargado de verificar si existe el concepto SNOMED CT dado por parámetro
     * @param conceptSCT
     * @return
     */
    public boolean existConceptSCT(ConceptSCT conceptSCT);

    public boolean existDescriptionSCT(DescriptionSCT descriptionSCT);

    public DescriptionSCT getDescriptionSCTBy(long idDescriptionSCT);

}
