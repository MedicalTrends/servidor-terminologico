package cl.minsal.semantikos.converters;

import cl.minsal.semantikos.gmdn.GmdnTypeBean;
import cl.minsal.semantikos.model.gmdn.DeviceType;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.snomed.SCTTypeBean;

import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Created by des01c7 on 04-08-16.
 */

@FacesConverter("deviceTypeConverter")
public class DeviceTypeConverter implements Converter{


    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {

                ELContext elContext = fc.getELContext();
                GmdnTypeBean bean = (GmdnTypeBean) FacesContext.getCurrentInstance().getApplication() .getELResolver().getValue(elContext, null, "gmdnBean");
                return bean.getGmdnManager().getDeviceTypeById(Long.parseLong(value));

            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se seleccionó el concepto de destino."));
            }
        }
        else {
            return null;
        }
    }

    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((DeviceType) object).getId());
        }
        else {
            return null;
        }
    }


}
