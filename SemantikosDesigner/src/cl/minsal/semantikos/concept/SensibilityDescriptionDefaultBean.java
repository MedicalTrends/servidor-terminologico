package cl.minsal.semantikos.concept;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * Created by des01c7 on 09-12-16.
 */

@ManagedBean( name = "sensibilityBean")
@ViewScoped
public class SensibilityDescriptionDefaultBean {

    private final static long FAMILIA_PRODUCTOS= 37;
    private final static long GRUPO_FAMILIA_PRODUCTOS= 36;
    private final static long PRODUCTO_COMERCIAL=38;
    private final static long DISPOSITIVO_MEDICO_COMERCIAL=40;

    public boolean sensibility(long idCategory){
        if(idCategory==GRUPO_FAMILIA_PRODUCTOS){
           return true;
        }
        if(idCategory==FAMILIA_PRODUCTOS){
            return true;
        }
        if(idCategory==PRODUCTO_COMERCIAL){
            return true;
        }
        if(idCategory==DISPOSITIVO_MEDICO_COMERCIAL){
            return true;
        }
        return false;
    }
}
