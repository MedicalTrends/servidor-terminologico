package cl.minsal.semantikos.designer_modeler.converters;

import cl.minsal.semantikos.designer_modeler.designer.HelperTableBean;
import cl.minsal.semantikos.designer_modeler.designer.SMTKTypeBean;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;

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
@FacesConverter(value="helperTableRecordConverter",forClass = HelperTableRecord.class)
public class HelperTableRecordConverter implements Converter{


    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {

        HelperTable helperTable = (HelperTable) UIComponent.getCurrentComponent(fc).getAttributes().get("helperTable");

        if(value != null /*&& value.trim().length() > 0*/ ) {
            try {

                ELContext elContext = fc.getELContext();
                HelperTableBean bean = (HelperTableBean) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "helperTableBean");
                return bean.getHelperTableManager().getRecord(helperTable, Long.parseLong(value));
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
            return String.valueOf(((HelperTableRecord) object).getId());
        }
        else {
            return "";
        }
    }


}
