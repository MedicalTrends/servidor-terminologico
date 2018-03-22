package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.crossmaps.gmdn.CollectiveTerm;
import cl.minsal.semantikos.model.crossmaps.gmdn.GenericDeviceGroup;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Andrés Farías on 10/25/16.
 */
@Local
public interface GMDNDAO {

    /**
     * Este método es responsable de buscar aquellos conceptos que posean un CONCEPT_ID que coincida con el
     * <code>conceptIdPattern</code> dado como parámetro. El patron
     *
     * @param code            El grupo usado como filtro.
     *
     * @return La lista de conceptos que satisfacen el criterio de búsqueda.
     */
    GenericDeviceGroup getGenericDeviceGroupById(long code);

    /**
     * Este método es responsable de recuperar un CrossmapSetMember dada su terminología y un patrón de búsqueda
     *
     * @param pattern     El patrón de búsqueda
     * @return Un CrossmapSetMember fresco.
     */
    public List<GenericDeviceGroup> findGenericDeviceGroupsByPattern(String pattern);

    /**
     * Este método es responsable de obtener la lista de todos los CrossmapSetMembers que pertenecen a un Crossmap Set.
     *
     * @return La lista de los miembros del crossmap set indicado.
     */
    List<GenericDeviceGroup> getGenericDeviceGroups();

    /**
     * Este método es responsable de obtener la lista de todos los CrossmapSetMembers que pertenecen a un Crossmap Set.
     *
     * @return La lista de los miembros del crossmap set indicado.
     */
    List<GenericDeviceGroup> getGenericDeviceGroupsPaginated(int page, int pageSize);

    /**
     * Este método es responsable de buscar aquellos conceptos que posean un CONCEPT_ID que coincida con el
     * <code>conceptIdPattern</code> dado como parámetro. El patron
     *
     * @param code El concept ID por el cual se realiza la búsqueda.
     *
     * @return La lista de conceptos que satisfacen el criterio de búsqueda.
     */
    CollectiveTerm getCollectiveTermByCode(long code);

    /**
     * Este método es responsable de buscar aquellos conceptos que posean un CONCEPT_ID que coincida con el
     * <code>conceptIdPattern</code> dado como parámetro. El patron
     *
     * @param genericDeviceGroup El concept ID por el cual se realiza la búsqueda.
     *
     * @return La lista de conceptos que satisfacen el criterio de búsqueda.
     */
    List<CollectiveTerm> getCollectiveTermsByGenericDeviceGroup(GenericDeviceGroup genericDeviceGroup);

    /**
     * Este método es responsable de buscar aquellos conceptos que posean un CONCEPT_ID que coincida con el
     * <code>conceptIdPattern</code> dado como parámetro. El patron
     *
     * @param collectiveTerm El concept ID por el cual se realiza la búsqueda.
     *
     * @return La lista de conceptos que satisfacen el criterio de búsqueda.
     */
    List<CollectiveTerm> getParentsOf(CollectiveTerm collectiveTerm);

    /**
     * Este método es responsable de buscar aquellos conceptos que posean un CONCEPT_ID que coincida con el
     * <code>conceptIdPattern</code> dado como parámetro. El patron
     *
     * @param collectiveTerm El concept ID por el cual se realiza la búsqueda.
     *
     * @return La lista de conceptos que satisfacen el criterio de búsqueda.
     */
    List<CollectiveTerm> getChildrenOf(CollectiveTerm collectiveTerm);

    /**
     * Este método es responsable de buscar aquellos conceptos que posean un CONCEPT_ID que coincida con el
     * <code>conceptIdPattern</code> dado como parámetro. El patron
     *
     * @param collectiveTerms El concept ID por el cual se realiza la búsqueda.
     *
     * @return La lista de conceptos que satisfacen el criterio de búsqueda.
     */
    List<CollectiveTerm> getParentLines(List<CollectiveTerm> collectiveTerms);

}
