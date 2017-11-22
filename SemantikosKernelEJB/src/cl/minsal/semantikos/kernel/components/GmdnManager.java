package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.crossmaps.*;
import cl.minsal.semantikos.model.gmdn.DeviceType;
import cl.minsal.semantikos.model.gmdn.GenericDeviceGroup;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Remote;
import java.util.List;

/**
 * @author Andrés Farías on 8/30/16.
 */
@Remote
public interface GmdnManager {

    /**
     * Este método es responsable de crear un Crossmap
     *
     * @param pattern El crossmap directo a crear. No está soportado crear Crossmaps Indirectos aun.
     *
     * @return El crossmap creado.
     */
    public List<DeviceType> findDeviceTypesByPattern(String pattern);

    /**
     * Este método es responsable de eliminar un CrossMap de un concepto.
     *
     * @param genericDeviceGroup El crossmap que se desea eliminar.
     *
     */
    public void loadCollectiveTerms(GenericDeviceGroup genericDeviceGroup);



}
