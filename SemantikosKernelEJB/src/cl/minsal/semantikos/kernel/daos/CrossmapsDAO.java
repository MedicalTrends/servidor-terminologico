package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.crossmaps.*;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Andrés Farías
 */
@Local
public interface CrossmapsDAO {

    /**
     * Este método es responsable de crear un Crossmap
     *
     * @param directCrossmap El crossmap a crear.
     * @param user           El usuario que desea crear el CrossMap
     * @return El crossmap creado.
     */
    public DirectCrossmap create(DirectCrossmap directCrossmap, User user);


    public CrossmapSetMember getCrossmapSetMemberById(CrossmapSet crossmapSet, long idCrossmapSetMember);

    /**
     * Este método es responsable de recuperar un CrossmapSetMember dada su terminología y un patrón de búsqueda
     *
     * @param crossmapSet La terminología
     * @param pattern     El patrón de búsqueda
     * @return Un CrossmapSetMember fresco.
     */
    public List<CrossmapSetMember> findCrossmapSetMemberByPattern(CrossmapSet crossmapSet, String pattern);

    public CrossmapSet getCrossmapSetByID(long id);

    /**
     * Este método es responsable de recuperar todas las terminologías válidas existentes en el sistema
     *
     * @return Un <code>java.util.List</code> de CrossmapSet
     */
    public List<CrossmapSet> getCrossmapSets();

    /**
     * Este método es responsable de recuperar los crossmaps (como relaciones indirectas) ya pobladas con todos sus
     * campos, a partir del concepto SCT.
     *
     * @param snomedCTRelationship  Relacon Snomed CT.
     * @param sourceConcept El concepto base.
     * @return Una lista de todos los crossmaps que van a través del concepto SnomedCT.
     */
    public List<IndirectCrossmap> getCrossmapsBySCT(SnomedCTRelationship snomedCTRelationship, ConceptSMTK sourceConcept);

    /**
     * Este método es responsable de obtener la lista de todos los CrossmapSetMembers que pertenecen a un Crossmap Set.
     *
     * @param crossmapSet El nombre abreviado del crossmap Set cuyos miembros se quieren recuperar.
     * @return La lista de los miembros del crossmap set indicado.
     */
    List<CrossmapSetMember> getCrossmapSetMembers(CrossmapSet crossmapSet);

    /**
     * Este método es responsable de obtener la lista de todos los CrossmapSetMembers que pertenecen a un Crossmap Set.
     *
     * @param crossmapSet El crossmap Set cuyos miembros se quieren recuperar.
     * @return La lista de los miembros del crossmap set indicado.
     */
    List<CrossmapSetMember> getCrossmapSetMembersPaginated(CrossmapSet crossmapSet, int page, int pageSize);
}
