package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.gmdn.CollectiveTerm;
import cl.minsal.semantikos.model.gmdn.DeviceCategory;
import cl.minsal.semantikos.model.gmdn.DeviceType;
import cl.minsal.semantikos.model.gmdn.GenericDeviceGroup;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCT;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

/**
 * @author Andrés Farías on 10/25/16.
 */
@Local
public interface GmdnDAO {


    /**
     * Este método es responsable de recuperar un concepto por su CONCEPT_ID.
     *
     * @param genericDeviceGroup El grupo genérico de dispositivo
     *
     * @return La lista de categorías
     */
    public List<DeviceCategory> getDeviceCategoriesByGenericDeviceGroup(GenericDeviceGroup genericDeviceGroup);

    /**
     * Este método es responsable de buscar aquellos conceptos que posean un CONCEPT_ID que coincida con el
     * <code>conceptIdPattern</code> dado como parámetro. El patron
     *
     * @param code            El grupo usado como filtro.
     *
     * @return La lista de conceptos que satisfacen el criterio de búsqueda.
     */
    GenericDeviceGroup getGenericDeviceGroupByCode(long code);

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


    DeviceType getDeviceTypeById(long id);
}
