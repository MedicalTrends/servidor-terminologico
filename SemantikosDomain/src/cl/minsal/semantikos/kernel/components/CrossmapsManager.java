package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.crossmaps.*;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Remote;
import java.util.List;

/**
 * @author Andrés Farías on 8/30/16.
 */
@Remote
public interface CrossmapsManager {

    /**
     * Este método es responsable de crear un Crossmap
     *
     * @param directCrossmap El crossmap directo a crear. No está soportado crear Crossmaps Indirectos aun.
     * @param user           El usuario que desea crear el CrossMap
     *
     * @return El crossmap creado.
     */
    public Crossmap create(DirectCrossmap directCrossmap, User user);

    /**
     * Este método es responsable de eliminar un CrossMap de un concepto.
     *
     * @param crossmap El crossmap que se desea eliminar.
     * @param user     El usuario que elimina el crossmap.
     *
     * @return El crossmap eliminado y actualizado.
     */
    public Crossmap remove(Crossmap crossmap, User user);

    /**
     * Este método es responsable de recuperar todos los crossmapSets.
     *
     * @return La lista de crossmap sets.
     */
    public List<CrossmapSet> getCrossmapSets();

    /**
     * Este método es responsable de recuperar los crossmaps de un concepto y actualizarle su lista de crossmaps. Si el
     * <code>conceptSMTK</code> no es persistente, se recuperan los crossmaps asociados a su <code>CONCEPT_ID</code>.
     *
     * @param conceptSMTK El concepto cuyos Crossmaps se desea recuperar.
     *
     * @return La lista de Crossmaps asociados al concepto <code>conceptSMTK</code>.
     */
    public List<Crossmap> getCrossmaps(ConceptSMTK conceptSMTK);

    /**
     * Este método es responsable de recuperar los crossmaps de un concepto y actualizarle su lista de crossmaps. Si el
     * <code>conceptSMTK</code> no es persistente, se recuperan los crossmaps asociados a su <code>CONCEPT_ID</code>.
     *
     * @param conceptSMTK El concepto cuyos Crossmaps se desea recuperar.
     *
     * @return La lista de Crossmaps asociados al concepto <code>conceptSMTK</code>.
     */
    public List<DirectCrossmap> getDirectCrossmaps(ConceptSMTK conceptSMTK);

    /**
     * Este método es responsable de recuperar los crossmapSetMembers directos de un concepto y actualizarle su lista de
     * crossmaps. Si el <code>conceptSMTK</code> no es persistente, se recuperan los crossmaps asociados a su
     * <code>CONCEPT_ID</code>.
     *
     * @param conceptSMTK El concepto cuyos Crossmaps se desea recuperar.
     *
     * @return La lista de crossmapSetMembers asociados al concepto <code>conceptSMTK</code>.
     */
    public List<CrossmapSetMember> getDirectCrossmapsSetMembersOf(ConceptSMTK conceptSMTK);

    /**
     * Este método es responsable de recuperar los crossmaps indirectos de un concepto y actualizarle su lista de
     * crossmaps. Si el <code>conceptSMTK</code> no es persistente, se recuperan los crossmaps asociados a su
     * <code>CONCEPT_ID</code>.
     *
     * @param conceptSMTK El concepto cuyos Crossmaps se desea recuperar.
     *
     * @return La lista de Crossmaps asociados al concepto <code>conceptSMTK</code>.
     */
    public List<IndirectCrossmap> getIndirectCrossmaps(ConceptSMTK conceptSMTK);

    /**
     * Este método es responsable de recuperar un crossmapSetMember dado su
     * <code>conceptSMTK</code> no es persistente, se recuperan los crossmaps asociados a su <code>CONCEPT_ID</code>.
     *
     * @param id El id del crossmapsetmember
     *
     * @return La lista de CrossmapSetMembers asociados al concepto <code>conceptSMTK</code>.
     */
    public CrossmapSetMember getCrossmapSetMemberById(long id);

    /**
     * Este método es repsonsable de recuperar los crossmapSetMembers de un crossmapSet dado por su nombre abreviado.
     * @param crossmapSetAbbreviatedName El nombre abreviado del crossmapSet que se quiere recuperar.
     * @return Una lista con los crossmapSetMembers del crossmapSet dado <code>crossmapSetAbbreviatedName</code>.
     */
    public List<CrossmapSetMember> getCrossmapSetByAbbreviatedName(String crossmapSetAbbreviatedName);

    /**
     * Este método es responsable de establecer un crossmap directo hacia un término de otra terminología.
     *
     * @param conceptSMTK       El concepto SMTK.
     * @param crossmapSetMember El término en la terminología externa.
     *
     * @return El crossmap creado.
     */
    public DirectCrossmap bind(ConceptSMTK conceptSMTK, CrossmapSetMember crossmapSetMember);

    /**
     * Este método busca registros en las terminologías externas términos que cumplan con el patrón.
     *
     * @param crossmapSet La terminología donde se busca el patrón.
     * @param pattern     El patrón de búsqueda.
     *
     * @return Una lista de registros
     */
    public List<CrossmapSetMember> findByPattern(CrossmapSet crossmapSet, String pattern);
    /**
     * Este método busca registros en las terminologías externas términos que cumplan con el código.
     *
     * @param crossmapSet La terminología donde se busca el código.
     * @param code     El código de búsqueda.
     *
     * @return Una lista de registros
     */
    public List<CrossmapSetMember> findByPatternCode1(CrossmapSet crossmapSet, String code);


}
