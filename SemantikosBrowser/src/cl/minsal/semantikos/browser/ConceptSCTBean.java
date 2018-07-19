package cl.minsal.semantikos.browser;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.SnomedCTManager;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCTType;
import cl.minsal.semantikos.view.components.GuestPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BluePrints Developer on 14-07-2016.
 */

@ManagedBean(name = "conceptSCT")
@ViewScoped
public class ConceptSCTBean {

    static private final Logger logger = LoggerFactory.getLogger(ConceptSCTBean.class);

    //@EJB
    SnomedCTManager snomedCTManager = (SnomedCTManager) ServiceLocator.getInstance().getService(SnomedCTManager.class);

    ConceptSCT selectedConcept;

    long conceptID;

    @ManagedProperty(value = "#{guestPreferences}")
    GuestPreferences guestPreferences;

    public long getConceptID() {
        return conceptID;
    }

    public void setConceptID(long conceptID) {
        this.conceptID = conceptID;
        selectedConcept = snomedCTManager.getConceptByID(conceptID);
        selectedConcept.setRelationships(snomedCTManager.getRelationshipsFrom(selectedConcept));
    }

    //Inicializacion del Bean
    @PostConstruct
    protected void initialize() {
        guestPreferences.setTheme("teal");
    }

    public ConceptSCT getSelectedConcept() {
        return selectedConcept;
    }

    public void setSelectedConcept(ConceptSCT selectedConcept) {

        this.selectedConcept = selectedConcept;

    }

    public List<DescriptionSCT> getOtherDescriptions() {

        if(selectedConcept == null) {
            return null;
        }

        List<DescriptionSCT> otherDescriptions = new ArrayList<DescriptionSCT>();

        for (DescriptionSCT description : selectedConcept.getDescriptions()) {
            if(description.getDescriptionType().equals(DescriptionSCTType.SYNONYM) ||
                    description.getDescriptionType().equals(DescriptionSCTType.ACCEPTABLE))
                otherDescriptions.add(description);
        }

        return otherDescriptions;
    }

    public GuestPreferences getGuestPreferences() {
        return guestPreferences;
    }

    public void setGuestPreferences(GuestPreferences guestPreferences) {
        this.guestPreferences = guestPreferences;
    }

}
