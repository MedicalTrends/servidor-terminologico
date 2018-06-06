package cl.minsal.semantikos.ws.component;

import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.NoValidDescription;
import cl.minsal.semantikos.model.descriptions.PendingTerm;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.TargetDefinition;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCT;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.modelws.fault.IllegalInputFault;
import cl.minsal.semantikos.modelws.fault.NotFoundFault;
import cl.minsal.semantikos.modelws.request.*;
import cl.minsal.semantikos.modelws.response.*;
import cl.minsal.semantikos.ws.mapping.BioequivalentMapper;
import cl.minsal.semantikos.ws.mapping.ConceptMapper;
import cl.minsal.semantikos.ws.mapping.ISPRegisterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static cl.minsal.semantikos.kernel.daos.ConceptDAOImpl.NO_VALID_CONCEPT;
import static cl.minsal.semantikos.kernel.daos.ConceptDAOImpl.PENDING_CONCEPT;

/**
 * @author Alfonso Cornejo on 2016-11-17.
 */
@Stateless
public class SnomedController {

    /**
     * El logger para la clase
     */
    private static final Logger logger = LoggerFactory.getLogger(SnomedController.class);

    @EJB
    private SnomedCTManager snomedCTManager;
    @EJB
    private CrossmapsManager crossmapsManager;
    @EJB
    private UserManager userManager;

    /**
     * REQ-WS-001
     * Este método es responsable de buscar un concepto segun una de sus descripciones que coincidan por perfect match
     * con el <em>TERM</em> dado en los REFSETS y Categorias indicadas.
     *
     * @param request            El termino a buscar por perfect Match.
     * @return Conceptos buscados segun especificaciones de REQ-WS-001.
     * @throws NotFoundFault Si uno de los nombres de Categorias o REFSETS no existe.
     */
    public SnomedTermSearchResponse searchTermPerfectMatchSnomed(
            SnomedSearchTermRequest request
    ) throws NotFoundFault, ExecutionException, InterruptedException {

        SnomedTermSearchResponse res = new SnomedTermSearchResponse();

        List<SnomedMatchDescriptionResponse> perfectMatchDescriptions = new ArrayList<>();

        List<DescriptionSCT> descriptions = snomedCTManager.searchDescriptionsPerfectMatch(request.getTerm(), request.getPageNumber(), request.getPageSize());

        for (DescriptionSCT description : descriptions) {
            ConceptSCT conceptSCT = snomedCTManager.getConceptByID(description.getConceptId());
            perfectMatchDescriptions.add(new SnomedMatchDescriptionResponse(description, conceptSCT));
        }

        SnomedMatchDescriptionsResponse perfectMatchDescriptionsResponse = new SnomedMatchDescriptionsResponse();
        perfectMatchDescriptionsResponse.setMatchDescriptionsResponse(perfectMatchDescriptions);
        perfectMatchDescriptionsResponse.setQuantity(perfectMatchDescriptions.size());

        res.setPattern(request.getTerm());
        res.setMatchDescriptions(perfectMatchDescriptionsResponse);

        return res;
    }

    /**
     * REQ-WS-001
     * Este método es responsable de buscar un concepto segun una de sus descripciones que coincidan por perfect match
     * con el <em>TERM</em> dado en los REFSETS y Categorias indicadas.
     *
     * @param request            El termino a buscar por perfect Match.
     * @return Conceptos buscados segun especificaciones de REQ-WS-001.
     * @throws NotFoundFault Si uno de los nombres de Categorias o REFSETS no existe.
     */
    public SnomedTermSearchResponse searchTermTruncatePerfectSnomed(
            SnomedSearchTermRequest request
    ) throws NotFoundFault, ExecutionException, InterruptedException {

        SnomedTermSearchResponse res = new SnomedTermSearchResponse();

        List<SnomedMatchDescriptionResponse> perfectMatchDescriptions = new ArrayList<>();

        List<DescriptionSCT> descriptions = snomedCTManager.searchDescriptionsTruncateMatch(request.getTerm(), request.getPageNumber(), request.getPageSize());

        for (DescriptionSCT description : descriptions) {
            ConceptSCT conceptSCT = snomedCTManager.getConceptByID(description.getConceptId());
            perfectMatchDescriptions.add(new SnomedMatchDescriptionResponse(description, conceptSCT));
        }

        SnomedMatchDescriptionsResponse perfectMatchDescriptionsResponse = new SnomedMatchDescriptionsResponse();
        perfectMatchDescriptionsResponse.setMatchDescriptionsResponse(perfectMatchDescriptions);
        perfectMatchDescriptionsResponse.setQuantity(perfectMatchDescriptions.size());

        res.setPattern(request.getTerm());
        res.setMatchDescriptions(perfectMatchDescriptionsResponse);

        return res;
    }


    /**
     * REQ-WS-001
     * Este método es responsable de buscar un concepto segun una de sus descripciones que coincidan por perfect match
     * con el <em>TERM</em> dado en los REFSETS y Categorias indicadas.
     *
     * @param request            El termino a buscar por perfect Match.
     * @return Conceptos buscados segun especificaciones de REQ-WS-001.
     * @throws NotFoundFault Si uno de los nombres de Categorias o REFSETS no existe.
     */
    public SnomedTermSearchResponse searchTermSuggested(
            SnomedSuggestionsRequest request
    ) throws NotFoundFault, ExecutionException, InterruptedException {

        SnomedTermSearchResponse res = new SnomedTermSearchResponse();

        List<SnomedMatchDescriptionResponse> perfectMatchDescriptions = new ArrayList<>();

        List<DescriptionSCT> descriptions = snomedCTManager.searchDescriptionsSuggested(request.getTerm());

        for (DescriptionSCT description : descriptions) {
            ConceptSCT conceptSCT = snomedCTManager.getConceptByID(description.getConceptId());
            perfectMatchDescriptions.add(new SnomedMatchDescriptionResponse(description, conceptSCT));
        }

        SnomedMatchDescriptionsResponse perfectMatchDescriptionsResponse = new SnomedMatchDescriptionsResponse();
        perfectMatchDescriptionsResponse.setMatchDescriptionsResponse(perfectMatchDescriptions);
        perfectMatchDescriptionsResponse.setQuantity(perfectMatchDescriptions.size());

        res.setPattern(request.getTerm());
        res.setMatchDescriptions(perfectMatchDescriptionsResponse);

        return res;
    }

    public ConceptSCTResponse conceptSCTByConceptID(long conceptId) throws Exception {

        ConceptSCT conceptSCT;

        ConceptSCTResponse res;

        try {
            conceptSCT = this.snomedCTManager.getConceptByID(conceptId);

            conceptSCT.setRelationships(this.snomedCTManager.getRelationshipsFrom(conceptSCT));

            List<IndirectCrossmap> indirectCrossmaps = this.crossmapsManager.getIndirectCrossmaps(conceptSCT);

            res = new ConceptSCTResponse(conceptSCT, indirectCrossmaps);

        } catch (Exception e) {
            throw new NotFoundFault(e.getMessage());
        }

        return res;
    }

    public ConceptSCTResponse conceptSCTByDescriptionID(long descriptionId) throws Exception {

        ConceptSCT conceptSCT;

        ConceptSCTResponse res;

        try {
            conceptSCT = this.snomedCTManager.getConceptByDescriptionID(descriptionId);

            conceptSCT.setRelationships(this.snomedCTManager.getRelationshipsFrom(conceptSCT));

            List<IndirectCrossmap> indirectCrossmaps = this.crossmapsManager.getIndirectCrossmaps(conceptSCT);

            res = new ConceptSCTResponse(conceptSCT, indirectCrossmaps);

        } catch (Exception e) {
            throw new NotFoundFault(e.getMessage());
        }

        return res;
    }

}
