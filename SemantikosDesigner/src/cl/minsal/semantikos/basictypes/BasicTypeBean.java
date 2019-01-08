package cl.minsal.semantikos.basictypes;

import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 22-07-16.
 */
@ManagedBean(name = "basicTypeBean")
@ViewScoped
public class BasicTypeBean implements Serializable {

    private String pattern;

    private BasicTypeValue basicTypeValuePlaceHolder = new BasicTypeValue(null);


    public List<String> getRecordSearchInput(String patron) {

        FacesContext context = FacesContext.getCurrentInstance();

        BasicTypeDefinition basicTypeDefinition = (BasicTypeDefinition) UIComponent.getCurrentComponent(context).getAttributes().get("basicTypeDefinition");

        List<String> someRecords = new ArrayList<String>();

        for (Object basicTypeValue : basicTypeDefinition.getDomain()) {
            if(basicTypeValue.toString().toLowerCase().contains(patron.trim().toLowerCase()))
                someRecords.add(basicTypeValue.toString());
        }

        return someRecords;
    }


}
