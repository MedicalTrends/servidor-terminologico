package cl.minsal.semantikos.designer_modeler.designer;

import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 22-07-16.
 */
@ManagedBean(name = "basicTypeBean")
@ViewScoped
public class BasicTypeBean implements Serializable {

    private String pattern;

    private BasicTypeValue basicTypeValuePlaceHolder = new BasicTypeValue(null);

    @PostConstruct
    protected void initialize() throws ParseException {

    }

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
