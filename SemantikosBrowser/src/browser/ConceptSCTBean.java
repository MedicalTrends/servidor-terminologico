package browser;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.RelationshipManager;
import cl.minsal.semantikos.kernel.components.SnomedCTManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.crossmaps.DirectCrossmap;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCTType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
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

}
