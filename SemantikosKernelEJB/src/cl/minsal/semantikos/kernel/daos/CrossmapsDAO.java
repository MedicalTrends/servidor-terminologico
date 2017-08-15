package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.crossmaps.CrossmapSet;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.crossmaps.DirectCrossmap;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;

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

    /**
     * Este método es responsable de recuperar un CrossMap Directo desde la base de datos.
     *
     * @param id El identificador único en la base de datos.
     * @return Un CrossMap Directo fresco creado a partir de la base de datos.
     */
    public DirectCrossmap getDirectCrossmapById(long id);

    public DirectCrossmap bindConceptSMTKToCrossmapSetMember(ConceptSMTK conceptSMTK, CrossmapSetMember
            crossmapSetMember);

    public CrossmapSet getCrossmapSetByID(long id);

    /**
     * Este método es responsable de recuperar un CrossmapSetMember por su ID de la base de datos.
     *
     * @param idCrossmapSetMember El ID del crossmapSet que se desea recuperar.
     * @return Un CrossmapSetMember fresco.
     */
    public CrossmapSetMember getCrossmapSetMemberById(long idCrossmapSetMember);

    /**
     * Este método es responsable de recuperar todas las relaciones que van desde un concepto Snomed CT hacia registros
     * en otras terminologías (CrossmapSetMembers).
     *
     * @param conceptSCT El concepto Snomed CT del cual salen las referencias a términos en otras terminologías.
     * @return Una lista de terminos de terminologías externas asociadas al concepto Snomed <code>conceptSCT</code>.
     */
    List<CrossmapSetMember> getRelatedCrossMapSetMembers(ConceptSCT conceptSCT);

    /**
     * Este método es responsable de recuperar un CrossmapSetMember dada su terminología y un patrón de búsqueda
     *
     * @param crossmapSet La terminología
     * @param pattern     El patrón de búsqueda
     * @return Un CrossmapSetMember fresco.
     */
    public List<CrossmapSetMember> findCrossmapSetMemberBy(CrossmapSet crossmapSet, String pattern);


    /**
     * Este método es responsable de recuperar un CrossmapSetMember dada su terminología y un patrón de búsqueda
     *
     * @param crossmapSet La terminología
     * @param cod     El código de búsqueda
     *
     * @return Un CrossmapSetMember fresco.
     */
    public List<CrossmapSetMember> findCrossmapSetMemberByCod1(CrossmapSet crossmapSet, String cod);

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
     * @param idConceptSCT  El ID del concepto Snomed CT.
     * @param sourceConcept El concepto base.
     * @return Una lista de todos los crossmaps que van a través del concepto SnomedCT.
     */
    public List<IndirectCrossmap> getCrossmapsBySCT(long idConceptSCT, ConceptSMTK sourceConcept);

    /**
     * Este método es responsable de obtener la lista de todos los CrossmapSetMembers que pertenecen a un Crossmap Set.
     *
     * @param crossmapSetAbbreviatedName El nombre abreviado del crossmap Set cuyos miembros se quieren recuperar.
     * @return La lista de los miembros del crossmap set indicado.
     */
    List<CrossmapSetMember> getCrossmapSetMemberByAbbreviatedName(String crossmapSetAbbreviatedName);

    /**
     * Este método es responsable de obtener la lista de todos los CrossmapSetMembers que pertenecen a un Crossmap Set.
     *
     * @param crossmapSet El crossmap Set cuyos miembros se quieren recuperar.
     * @return La lista de los miembros del crossmap set indicado.
     */
    List<CrossmapSetMember> getCrossmapSetMemberByCrossmapSet(CrossmapSet crossmapSet);
}
