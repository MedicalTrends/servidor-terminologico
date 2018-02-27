package cl.minsal.semantikos.converters;

import cl.minsal.semantikos.designer.CrossmapBean;
import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;
import cl.minsal.semantikos.model.crossmaps.CrossmapSet;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetRecord;

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

//@FacesConverter("helperTableRecordConverter")
@FacesConverter(value="crossmapSetMemberConverter",forClass = CrossmapSetRecord.class)
public class CrossmapSetMemberConverter implements Converter {


    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {

        if(value != null /*&& value.trim().length() > 0*/ ) {
            try {

                ELContext elContext = fc.getELContext();

                CrossmapSet crossmapSet = (CrossmapSet) UIComponent.getCurrentComponent(fc).getAttributes().get("crossmapSet");

                CrossmapBean bean = (CrossmapBean) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "crossmapBean");
                return bean.getCrossmapsManager().getCrossmapSetMemberById(crossmapSet, Long.parseLong(value));
                //return bean.getRecordById(helperTable, Long.parseLong(value));

            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar un valor."));
            }
        }
        else {
            return null;
        }
    }

    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((CrossmapSetRecord) object).getId());
        }
        else {
            return "";
        }
    }


}
