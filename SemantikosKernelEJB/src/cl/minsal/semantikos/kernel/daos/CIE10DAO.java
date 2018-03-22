package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.crossmaps.CrossmapSet;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.crossmaps.DirectCrossmap;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.model.crossmaps.cie10.Disease;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Andrés Farías
 */
@Local
public interface CIE10DAO {

    /**
     * Este método es responsable de recuperar un CrossmapSetMember por su ID de la base de datos.
     *
     * @param id El ID del crossmapSet que se desea recuperar.
     * @return Un CrossmapSetMember fresco.
     */
    public Disease getDiseaseById(long id);

    /**
     * Este método es responsable de recuperar un CrossmapSetMember dada su terminología y un patrón de búsqueda
     *
     * @param pattern     El patrón de búsqueda
     * @return Un CrossmapSetMember fresco.
     */
    public List<Disease> findDiseasesByPattern(String pattern);

    /**
     * Este método es responsable de obtener la lista de todos los CrossmapSetMembers que pertenecen a un Crossmap Set.
     *
     * @return La lista de los miembros del crossmap set indicado.
     */
    List<Disease> getDiseases();

    /**
     * Este método es responsable de obtener la lista de todos los CrossmapSetMembers que pertenecen a un Crossmap Set.
     *
     * @return La lista de los miembros del crossmap set indicado.
     */
    List<Disease> getDiseasesPaginated(int page, int pageSize);
}
