package cl.minsal.semantikos.ws.component;

import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.descriptions.*;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.TargetDefinition;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.modelws.request.*;
import cl.minsal.semantikos.modelws.response.*;
import cl.minsal.semantikos.modelws.fault.IllegalInputFault;
import cl.minsal.semantikos.modelws.fault.NotFoundFault;
import cl.minsal.semantikos.util.StringUtils;
import cl.minsal.semantikos.ws.mapping.BioequivalentMapper;
import cl.minsal.semantikos.ws.mapping.ConceptMapper;
import cl.minsal.semantikos.ws.mapping.ISPRegisterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ManagedBean;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static cl.minsal.semantikos.kernel.daos.ConceptDAOImpl.NO_VALID_CONCEPT;
import static cl.minsal.semantikos.kernel.daos.ConceptDAOImpl.PENDING_CONCEPT;

/**
 * @author Alfonso Cornejo on 2016-11-17.
 */
@Stateless
public class ConceptController {

    /**
     * El logger para la clase
     */
    private static final Logger logger = LoggerFactory.getLogger(ConceptController.class);

    @EJB
    private ConceptManager conceptManager;
    @EJB
    private RelationshipManager relationshipManager;
    @EJB
    private DescriptionManager descriptionManager;
    @EJB
    private RefSetManager refSetManager;
    @EJB
    private TagManager tagManager;
    @EJB
    private CategoryManager categoryManager;
    @EJB
    private AuditManager auditManager;
    @EJB
    private PendingTermsManager pendingTermManager;
    @EJB
    private UserManager userManager;

    @EJB
    private PaginationController paginationController;
    @EJB
    private CategoryController categoryController;
    @EJB
    private RefSetController refSetController;
    @EJB
    private CrossmapController crossmapController;

    @Resource
    private SessionContext ctx;

    /**
     * Este método es responsable de recperar los conceptos relacionados (hijos...) de un concepto que se encuentran en
     * una cierta categoría.
     *
     * @param descriptionId El DESCRIPTION_ID del concepto cuyos conceptos relacionados se desea buscar.
     * @param conceptId     El CONCEPT_ID del concepto cuyos conceptos relacionados se desea recuperar. Este parámetro
     *                      es considerado únicamente si el DESCRIPTION_ID no fue especificado.
     * @param categoryName  El nombre de la categoría a la cual pertenecen los objetos relacionados que se buscan.
     * @return Los conceptos relacionados en un envelope apropiado.
     * @throws NotFoundFault Arrojada si no se encuentran resultados.
     */
    public RelatedConceptsResponse findRelated(String descriptionId, String conceptId, @NotNull String categoryName)
            throws Exception {

        /* Lo primero consiste en recuperar la categoría dentro de la cual se debe realizar la búsqueda. */
        Category category;
        try {
            category = this.categoryManager.getCategoryByName(categoryName);
        }
        /* Si no hay categoría con ese nombre se obtiene una excepción (pues debiera haberlo encontrado */
        catch (EJBException e) {
            logger.error("Se buscó una categoría inexistente: " + categoryName, e);
            throw e;
        }

        /* Lo siguiente consiste en recuperar el concepto cuyos conceptos relacionados se quiere recuperar. */
        ConceptSMTK sourceConcept;
        sourceConcept = getSourceConcept(descriptionId, conceptId);

        List<ConceptResponse> relatedResponses = new ArrayList<>();
        List<ConceptSMTK> relatedConcepts = this.conceptManager.getRelatedConcepts(sourceConcept, category);

        //relatedConcepts = relationshipManager.loadRelationshipsInParallel(relatedConcepts);

        for (ConceptSMTK related : relatedConcepts) {
            ConceptResponse conceptResponse = new ConceptResponse(related);
            //conceptResponse.setForREQWS002();
            this.loadAttributes(conceptResponse, related);
            this.loadSnomedCTRelationships(conceptResponse, related);
            this.loadIndirectCrossmaps(conceptResponse, related);
            this.loadDirectCrossmaps(conceptResponse, related);

            for (DescriptionResponse descriptionResponse : conceptResponse.getDescriptions()) {
                descriptionResponse.setAutogeneratedName(null);
                descriptionResponse.setValid(null);
                descriptionResponse.setModeled(null);
                descriptionResponse.setValidityUntil(null);
                descriptionResponse.setCreatorUser(null);
            }

            this.loadRefSets(conceptResponse, related);
            this.loadTags(conceptResponse, related);

            relatedResponses.add(conceptResponse);
        }

        RelatedConceptsResponse res = new RelatedConceptsResponse(sourceConcept);
        res.setRelatedConcepts(relatedResponses);
        res.setQuantity(relatedResponses.size());

        return res;
    }

    /**
     * Este método es responsable de recuperar un concepto dado el <em>DESCRIPTION_ID</em> de una de sus descripciones,
     * o bien, directamente a partir del <em>CONCEPT_ID</em> del concepto.
     *
     * @param descriptionId El <em>DESCRIPTION_ID</em> de la descripción contenida en el concepto que se desea
     *                      recuperar.
     * @param conceptId     El <em>CONCEPT_ID</em> del concepto que se desea recuperar, sólo si
     *                      <code>descriptionId</code> es nulo.
     * @return El concepto buscado.
     */
    private ConceptSMTK getSourceConcept(String descriptionId, String conceptId) throws NotFoundFault {
        ConceptSMTK sourceConcept;
        if (descriptionId != null && !descriptionId.trim().equals("")) {
            try {
                sourceConcept = this.conceptManager.getConceptByDescriptionID(descriptionId);
            } catch (Exception e) {
                throw new NotFoundFault("Descripcion no encontrada: " + descriptionId);
            }
        }

        /* Sólo si falla lo anterior: CONCEPT_ID */
        else if (conceptId != null && !conceptId.trim().equals("")) {
            try {
                sourceConcept = this.conceptManager.getConceptByCONCEPT_ID(conceptId);
            } catch (Exception e) {
                throw new NotFoundFault("Concepto no encontrado: " + conceptId);
            }
        }

        /* Si no hay ninguno de los dos, se arroja una excepción */
        else {
            throw new IllegalArgumentException("Tanto el DESCRIPTION_ID como el CONCEPT_ID eran nulos.");
        }

        return sourceConcept;
    }

    public RelatedConceptsLiteResponse findRelatedLite(String conceptId, String descriptionId, String categoryName)
            throws NotFoundFault {
        RelatedConceptsLiteResponse res = new RelatedConceptsLiteResponse();

        Category category = this.categoryManager.getCategoryByName(categoryName);
        if (category == null) {
            throw new NotFoundFault("Categoria no encontrada");
        }

        ConceptSMTK source = null;

        if (descriptionId != null && !descriptionId.isEmpty()) {
            source = this.conceptManager.getConceptByDescriptionID(descriptionId);
        } else if (conceptId != null && !conceptId.isEmpty()) {
            source = this.conceptManager.getConceptByCONCEPT_ID(conceptId);
        }

        if (source != null) {
            res = new RelatedConceptsLiteResponse(source);
            List<ConceptLightResponse> relatedResponses = new ArrayList<>();
            List<ConceptSMTK> relatedConcepts = this.conceptManager.getRelatedConcepts(source, category);

            if (relatedConcepts != null) {
                for (ConceptSMTK related : relatedConcepts) {
                    if (category.equals(related.getCategory())) {
                        relatedResponses.add(new ConceptLightResponse(related));
                    }
                }
            }
            res.setConceptId(source.getConceptID());
            res.setDescription(source.getDescriptionFavorite().getTerm());
            res.setDescriptionId(source.getDescriptionFavorite().getDescriptionId());
            res.setCategory(source.getCategory().getName());
            res.setRelatedConcepts(relatedResponses);
            res.setQuantity(relatedResponses.size());
        }

        return res;
    }

    /**
     * REQ-WS-001
     * Este método es responsable de buscar un concepto segun una de sus descripciones que coincidan por perfect match
     * con el <em>TERM</em> dado en los REFSETS y Categorias indicadas.
     *
     * @param term            El termino a buscar por perfect Match.
     * @param categoriesNames Nombres de las Categorias donde se deben hacer las búsquedas.
     * @param refSetsNames    Nombres de los REFSETS donde se deben hacer las búsquedas.
     * @return Conceptos buscados segun especificaciones de REQ-WS-001.
     * @throws NotFoundFault Si uno de los nombres de Categorias o REFSETS no existe.
     */
    //@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public GenericTermSearchResponse searchTermGeneric(
            String term,
            List<String> categoriesNames,
            List<String> refSetsNames
    ) throws NotFoundFault, ExecutionException, InterruptedException {

        List<String> categoryNames = new ArrayList<>(categoriesNames);

        GenericTermSearchResponse res = new GenericTermSearchResponse();
        List<Category> categories = this.categoryController.findCategories(categoryNames);
        List<RefSet> refSets = this.refSetController.findRefsets(refSetsNames);

        if(categories.isEmpty()) {
            categories = null;
        }

        if(refSets.isEmpty()) {
            refSets = null;
        }

        List<PerfectMatchDescriptionResponse> perfectMatchDescriptions = new ArrayList<>();
        List<NoValidDescriptionResponse> noValidDescriptions = new ArrayList<>();
        List<PendingDescriptionResponse> pendingDescriptions = new ArrayList<>();

        List<Description> descriptions = descriptionManager.searchDescriptionsPerfectMatch(term, categories, refSets, null);

        if(!categoriesNames.contains("Concepto Especial")) {
            descriptions.addAll(descriptionManager.searchDescriptionsPerfectMatch(term, Arrays.asList(CategoryFactory.SPECIAL_CONCEPT), null, null));
        }

        //List<Description> descriptions = this.descriptionManager.searchDescriptionsPerfectMatchInParallel(term, categories, refSets);
        //logger.debug("ws-req-001. descripciones encontradas: " + descriptions);
        for (Description description : descriptions) {
            if(description.getConceptSMTK().equals(NO_VALID_CONCEPT)) {
                NoValidDescription noValidDescription = descriptionManager.getNoValidDescriptionByID(description.getId());
                if (noValidDescription != null) {
                    noValidDescriptions.add(new NoValidDescriptionResponse(noValidDescription));
                } else {
                    perfectMatchDescriptions.add(new PerfectMatchDescriptionResponse(description));
                }
            }
            if(description.getConceptSMTK().equals(PENDING_CONCEPT)) {
                pendingDescriptions.add(new PendingDescriptionResponse(description));
            }
            else {
                perfectMatchDescriptions.add(new PerfectMatchDescriptionResponse(description));
            }
        }

        PerfectMatchDescriptionsResponse perfectMatchDescriptionsResponse = new PerfectMatchDescriptionsResponse();
        perfectMatchDescriptionsResponse.setPerfectMatchDescriptionsResponse(perfectMatchDescriptions);
        perfectMatchDescriptionsResponse.setQuantity(perfectMatchDescriptions.size());

        NoValidDescriptionsResponse noValidDescriptionsResponse = new NoValidDescriptionsResponse();
        noValidDescriptionsResponse.setNoValidDescriptionsResponse(noValidDescriptions);
        noValidDescriptionsResponse.setQuantity(noValidDescriptions.size());

        PendingDescriptionsResponse pendingDescriptionsResponse = new PendingDescriptionsResponse();
        pendingDescriptionsResponse.setPendingDescriptionsResponse(pendingDescriptions);
        pendingDescriptionsResponse.setQuantity(pendingDescriptions.size());

        res.setPattern(term);

        res.setPerfectMatchDescriptions(perfectMatchDescriptionsResponse);
        res.setNoValidDescriptions(noValidDescriptionsResponse);
        res.setPendingDescriptions(pendingDescriptionsResponse);

        return res;
    }

    /**
     * REQ-WS-004
     * Este método es responsable de buscar un concepto segun una de sus descripciones que coincidan por truncate match
     * con el <em>TERM</em> dado en los REFSETS y Categorias indicadas.
     *
     * @param term            El termino a buscar por perfect Match.
     * @param categoriesNames Nombres de las Categorias donde se deben hacer las búsquedas.
     * @param refSetsNames    Nombres de los REFSETS donde se deben hacer las búsquedas.
     * @return Conceptos buscados segun especificaciones de REQ-WS-001.
     * @throws NotFoundFault Si uno de los nombres de Categorias o REFSETS no existe.
     */
    public GenericTermSearchResponse searchTermGeneric2(
            String term,
            List<String> categoriesNames,
            List<String> refSetsNames
    ) throws NotFoundFault {
        GenericTermSearchResponse res = new GenericTermSearchResponse();

        List<Category> categories = this.categoryController.findCategories(categoriesNames);
        List<RefSet> refSets = this.refSetController.findRefsets(refSetsNames);

        if(categories.isEmpty()) {
            categories = null;
        }

        if(refSets.isEmpty()) {
            refSets = null;
        }

        List<PerfectMatchDescriptionResponse> perfectMatchDescriptions = new ArrayList<>();
        List<NoValidDescriptionResponse> noValidDescriptions = new ArrayList<>();
        List<PendingDescriptionResponse> pendingDescriptions = new ArrayList<>();

        List<Description> descriptions = descriptionManager.searchDescriptionsTruncateMatch(term, categories, refSets, null);

        if(!categoriesNames.contains("Concepto Especial")) {
            descriptions.addAll(descriptionManager.searchDescriptionsTruncateMatch(term, Arrays.asList(CategoryFactory.SPECIAL_CONCEPT), null, null));
        }

        //logger.debug("ws-req-001. descripciones encontradas: " + descriptions);

        for (Description description : descriptions) {
            if(description.getConceptSMTK().equals(NO_VALID_CONCEPT)) {
                NoValidDescription noValidDescription = descriptionManager.getNoValidDescriptionByID(description.getId());
                if (noValidDescription != null) {
                    noValidDescriptions.add(new NoValidDescriptionResponse(noValidDescription));
                } else {
                    perfectMatchDescriptions.add(new PerfectMatchDescriptionResponse(description));
                }
            }
            if(description.getConceptSMTK().equals(PENDING_CONCEPT)) {
                pendingDescriptions.add(new PendingDescriptionResponse(description));
            }
            else {
                perfectMatchDescriptions.add(new PerfectMatchDescriptionResponse(description));
            }
        }

        res.setPattern(term);

        PerfectMatchDescriptionsResponse perfectMatchDescriptionsResponse = new PerfectMatchDescriptionsResponse();
        perfectMatchDescriptionsResponse.setPerfectMatchDescriptionsResponse(perfectMatchDescriptions);
        perfectMatchDescriptionsResponse.setQuantity(perfectMatchDescriptions.size());

        NoValidDescriptionsResponse noValidDescriptionsResponse = new NoValidDescriptionsResponse();
        noValidDescriptionsResponse.setNoValidDescriptionsResponse(noValidDescriptions);
        noValidDescriptionsResponse.setQuantity(noValidDescriptions.size());

        PendingDescriptionsResponse pendingDescriptionsResponse = new PendingDescriptionsResponse();
        pendingDescriptionsResponse.setPendingDescriptionsResponse(pendingDescriptions);
        pendingDescriptionsResponse.setQuantity(pendingDescriptions.size());

        res.setPerfectMatchDescriptions(perfectMatchDescriptionsResponse);
        res.setNoValidDescriptions(noValidDescriptionsResponse);
        res.setPendingDescriptions(pendingDescriptionsResponse);

        return res;
    }

    /**
     * REQ-WS-004
     * Este método es responsable de buscar un concepto segun una de sus descripciones que coincidan por truncate match
     * con el <em>TERM</em> dado en los REFSETS y Categorias indicadas.
     *
     * @param term            El termino a buscar por perfect Match.
     * @param categoriesNames Nombres de las Categorias donde se deben hacer las búsquedas.
     * @return Conceptos buscados segun especificaciones de REQ-WS-001.
     * @throws NotFoundFault Si uno de los nombres de Categorias o REFSETS no existe.
     */
    public SuggestedDescriptionsResponse searchSuggestedDescriptions(
            String term,
            List<String> categoriesNames
    ) throws NotFoundFault {

        SuggestedDescriptionsResponse res = new SuggestedDescriptionsResponse();

        List<Category> categories = this.categoryController.findCategories(categoriesNames);

        List<SuggestedDescriptionResponse> suggestedDescriptions = new ArrayList<>();

        List<Description> descriptions = this.descriptionManager.searchDescriptionsSuggested(term, categories, null, null);
        int count = this.descriptionManager.countDescriptionsSuggested(term, categories, null, null);

        //logger.debug("ws-req-006. descripciones encontradas: " + descriptions);

        int cont = 0;

        for (Description description : descriptions) {

            if(cont==5) {
                break;
            }

            //logger.info("ws-req-006. descripciones encontrada: " + description.fullToString());

            suggestedDescriptions.add(new SuggestedDescriptionResponse(description));

            cont++;
        }

        res.setPattern(term);
        res.setSuggestedDescriptionResponses(suggestedDescriptions);
        res.setQuantity(count);

        return res;
    }

    /**
     * REQ-WS-004
     * Este método es responsable de buscar un concepto segun una de sus descripciones que coincidan por truncate match
     * con el <em>TERM</em> dado en los REFSETS y Categorias indicadas.
     *
     * @return Conceptos buscados segun especificaciones de REQ-WS-001.
     * @throws NotFoundFault Si uno de los nombres de Categorias o REFSETS no existe.
     */
    public SuggestedDescriptionsResponse searchSuggestedDescriptions2(
            DescriptionsSuggestionsRequest2 request2
    ) throws Exception {

        SuggestedDescriptionsResponse res = new SuggestedDescriptionsResponse();

        List<Category> categories = this.categoryController.findCategories(request2.getCategoryNames());
        List<RefSet> refSets = this.refSetController.findRefsets(request2.getRefSetNames());
        List<DescriptionType> descriptionTypes = new ArrayList<>();

        if(!StringUtils.isEmpty(request2.getDescriptionTypeNames())) {
            descriptionTypes = DescriptionTypeFactory.getInstance().getDescritptionTypesByNames(request2.getDescriptionTypeNames());
        }

        if(refSets.isEmpty()) {
            refSets = null;
        }
        else {
            for (RefSet refSet : refSets) {
                if(!refSet.isValid()) {
                    throw new NotFoundFault("El refset: '"+ refSet +"' no está vigente");
                }
            }
        }

        if(descriptionTypes.isEmpty()) {
            descriptionTypes = null;
        }

        List<SuggestedDescriptionResponse> suggestedDescriptions = new ArrayList<>();

        List<Description> descriptions = this.descriptionManager.searchDescriptionsSuggested(request2.getTerm(), categories, refSets, descriptionTypes);
        int count = this.descriptionManager.countDescriptionsSuggested(request2.getTerm(), categories, refSets, descriptionTypes);

        int cont = 0;

        for (Description description : descriptions) {

            if(cont==5) {
                break;
            }
            suggestedDescriptions.add(new SuggestedDescriptionResponse(description));

            cont++;
        }

        res.setPattern(request2.getTerm());
        res.setSuggestedDescriptionResponses(suggestedDescriptions);
        res.setQuantity(count);

        return res;
    }

    /**
     * Este método... no fue comentado por Alfonso.
     *
     * @param term            El término buscado.
     * @param categoriesNames Las categorías asociadas a los conceptos del dominio.
     * @param refSetsNames    Los refsets asociados a los conceptos del dominio.
     * @return Una lista de conceptos que satisfieron la búsqueda.
     * @throws NotFoundFault Arrojada si... ?
     */
    public ConceptsResponse searchTruncatePerfect(String term, List<String> categoriesNames, List<String> refSetsNames)
            throws NotFoundFault {

        /* Se recuperan los objetos de negocio de las categorías y refsets */
        List<Category> categories = categoryController.findCategories(categoriesNames);
        List<RefSet> refSets = refSetController.findRefsets(refSetsNames);

        /* Se realiza la búsqueda */
        List<ConceptSMTK> conceptSMTKS = conceptManager.truncateMatch(term, categories, refSets, true);

        /* Se encapsulan los conceptos retornados en un wrapper XML */
        List<ConceptResponse> conceptResponses = new ArrayList<>();
        for (ConceptSMTK conceptSMTK : conceptSMTKS) {
            conceptResponses.add(new ConceptResponse(conceptSMTK));
        }

        return new ConceptsResponse(conceptResponses);
    }

    public ConceptResponse conceptByDescriptionId(String descriptionId)
            throws Exception {
        ConceptSMTK conceptSMTK;
        try {
            conceptSMTK = this.conceptManager.getConceptByDescriptionID(descriptionId);
        } catch (Exception e) {
            throw new NotFoundFault("Descripcion no encontrada: " + descriptionId);
        }

        ConceptResponse res = new ConceptResponse(conceptSMTK);
        this.loadSnomedCTRelationships(res, conceptSMTK);
        this.loadAttributes(res, conceptSMTK);
        this.loadRefSets(res, conceptSMTK);
        this.loadIndirectCrossmaps(res, conceptSMTK);
        this.loadDirectCrossmaps(res, conceptSMTK);
        res.setForREQWS028();
        res.setCreationDate(auditManager.getConceptCreationAuditAction(conceptSMTK, true).getActionDate());

        for (DescriptionResponse descriptionResponse : res.getDescriptions()) {
            descriptionResponse.setValid(null);
            descriptionResponse.setValidityUntil(null);
            descriptionResponse.setCreationDate(null);
        }

        for (RefSetResponse refSetResponse : res.getRefsets()) {
            refSetResponse.setConcepts(null);
        }

        /*
        if(res.getPublished()) {
            res.setPublishingDate(auditManager.getConceptPublicationAuditAction(conceptSMTK, true).getActionDate());
        }
        */

        return res;
    }

    public ConceptResponse conceptByConceptID(String conceptId)
            throws Exception {
        ConceptSMTK conceptSMTK;
        try {
            conceptSMTK = this.conceptManager.getConceptByCONCEPT_ID(conceptId);
        } catch (Exception e) {
            throw new NotFoundFault("Concepto no encontrado: " + conceptId);
        }

        ConceptResponse res = new ConceptResponse(conceptSMTK);
        this.loadSnomedCTRelationships(res, conceptSMTK);
        this.loadAttributes(res, conceptSMTK);
        this.loadRefSets(res, conceptSMTK);
        this.loadIndirectCrossmaps(res, conceptSMTK);
        this.loadDirectCrossmaps(res, conceptSMTK);
        res.setForREQWS028();
        res.setCreationDate(auditManager.getConceptCreationAuditAction(conceptSMTK, true).getActionDate());

        for (DescriptionResponse descriptionResponse : res.getDescriptions()) {
            descriptionResponse.setValid(null);
            descriptionResponse.setValidityUntil(null);
            descriptionResponse.setCreationDate(null);
        }

        for (RefSetResponse refSetResponse : res.getRefsets()) {
            refSetResponse.setConcepts(null);
        }

        return res;
    }

    /**
     * Este metodo recupera los conceptos de una categoria
     *
     * @param categoryName      La Categoría
     * @param idEstablecimiento El establecimiento.
     * @return La lista de conceptos.
     * @throws NotFoundFault
     */
    public ConceptsResponse findConceptsByCategory(
            String categoryName,
            String idEstablecimiento
    ) throws Exception {

        /* Logging de invocación del servicio */
        logger.info("SearchService:findConceptsByCategory(" + categoryName + ", " + idEstablecimiento + ")");

        /* Se recupera la categoría */
        Category category;
        try {
            category = this.categoryManager.getCategoryByName(categoryName);
        } catch (IllegalArgumentException iae) {
            throw new NotFoundFault("No se encontró una categoría de nombre '" + categoryName + "'");
        }

        List<ConceptSMTK> concepts = this.conceptManager.findConcepts(null, Collections.singletonList(category), null, null);

        List<ConceptResponse> conceptResponses = new ArrayList<>();

        for (ConceptSMTK source : concepts) {
            ConceptResponse conceptResponse = new ConceptResponse(source);
            conceptResponse.setForREQWS002();
            this.loadAttributes(conceptResponse, source);
            this.loadSnomedCTRelationships(conceptResponse, source);

            conceptResponses.add(conceptResponse);
        }

        ConceptsResponse res = new ConceptsResponse();
        res.setConceptResponses(conceptResponses);
        res.setQuantity(conceptResponses.size());

        return res;
    }

    /**
     * Este metodo recupera los conceptos de una categoria
     *
     * @param categoryName      La Categoría
     * @param idEstablecimiento El establecimiento.
     * @return La lista de conceptos.
     * @throws NotFoundFault
     */
    public ConceptsResponse findConceptsByCategoryPaginated(
            String categoryName,
            String idEstablecimiento,
            int pageNumber,
            int pageSize
    ) throws NotFoundFault {

        /* Logging de invocación del servicio */
        //logger.info("SearchService:findConceptsByCategory(" + categoryName + ", " + idEstablecimiento + ")");

        /* Se recupera la categoría */
        Category category;
        try {
            category = categoryManager.getCategoryByName(categoryName);
        } catch (Exception e) {
            throw new NotFoundFault("No se encontró una categoría de nombre '" + categoryName + "'");
        }

        List<ConceptSMTK> concepts = conceptManager.findConceptsPaginated(category, pageSize, pageNumber, true);

        /*
        if(category.getRelationshipDefinitions().size() > 2) {
            concepts = relationshipManager.loadRelationshipsInParallel(concepts);
        }
        */

        List<ConceptResponse> conceptResponses = new ArrayList<>();

        for (ConceptSMTK source : concepts) {
            ConceptResponse conceptResponse = new ConceptResponse(source);
            conceptResponse.setForREQWS002();
            this.loadAttributes(conceptResponse, source);
            try {
                this.loadSnomedCTRelationships(conceptResponse, source);
            } catch (Exception e) {
                e.printStackTrace();
                throw new NotFoundFault(e.getMessage());
            }
            conceptResponse.setCategory(null);
            conceptResponses.add(conceptResponse);
        }

        ConceptsResponse res = new ConceptsResponse();
        res.setCategory(categoryName);
        res.setConceptResponses(conceptResponses);
        res.setQuantity(conceptResponses.size());

        return res;
    }

    /**
     * Este método es responsable de recuperar todos los conceptos de un RefSet.
     *
     * @param refSetName El RefSet cuyos conceptos se desea recuperar.
     * @return Una lista de conceptos que pertenecen al refset <code>refSetName</code>.
     * @throws NotFoundFault Arrojada si...
     */
    public RefSetResponse conceptsByRefset(String refSetName) throws NotFoundFault {
        RefSet refSet = this.refSetManager.getRefsetByName(refSetName);

        if(refSet == null) {
            throw new NotFoundFault("No existe RefSet con nombre: " + refSetName);
        }

        if(!refSet.isValid()) {
            throw new NotFoundFault("Este RefSet no está vigente");
        }

        RefSetResponse res = new RefSetResponse();

        res.setName(refSet.getName());
        res.setInstitution(refSet.getInstitution().getName());
        res.setCreationDate(refSet.getCreationDate());
        res.setValid(refSet.getValidityUntil()==null);
        res.setValidityUntil(refSet.getValidityUntil());

        for (ConceptSMTK conceptSMTK : refSet.getConcepts()) {
            ConceptResponse conceptResponse = new ConceptResponse(conceptSMTK);
            conceptResponse.setModeled(null);
            conceptResponse.setRelationships(null);
            conceptResponse.setAttributes(null);
            conceptResponse.setCrossmapSetMember(null);
            conceptResponse.setIndirectCrossMaps(null);
            conceptResponse.setSnomedCTRelationshipResponses(null);
            conceptResponse.setValid(null);
            conceptResponse.setValidUntil(null);
            conceptResponse.setPublished(null);
            conceptResponse.setRefsets(null);
            for (DescriptionResponse description : conceptResponse.getDescriptions()) {
                description.setCreatorUser(null);
                description.setPublished(null);
                description.setValid(null);
                description.setValidityUntil(null);
                description.setAutogeneratedName(null);
                description.setModeled(null);
                description.setPublished(null);
                description.setCreationDate(null);
            }
            res.getConcepts().add(conceptResponse);
        }

        res.setQuantity(res.getConcepts().size());

        return res;
    }

    /**
     * Este método es responsable de recuperar todos los conceptos de un RefSet.
     *
     * @param refSetName El RefSet cuyos conceptos se desea recuperar.
     * @return Una lista de conceptos que pertenecen al refset <code>refSetName</code>.
     * @throws NotFoundFault Arrojada si...
     */
    public RefSetLightResponse conceptsLightByRefset(String refSetName) throws NotFoundFault {

        RefSet refSet = this.refSetManager.getRefsetByName(refSetName);

        if(refSet == null) {
            throw new NotFoundFault("No existe RefSet con nombre: " + refSetName);
        }

        if(!refSet.isValid()) {
            throw new NotFoundFault("Este RefSet no está vigente");
        }

        RefSetLightResponse res = new RefSetLightResponse();

        res.setName(refSet.getName());
        res.setInstitution(refSet.getInstitution().getName());
        res.setCreationDate(refSet.getCreationDate());
        res.setValid(refSet.getValidityUntil()==null);
        res.setValidityUntil(refSet.getValidityUntil());

        for (ConceptSMTK conceptSMTK : refSet.getConcepts()) {
            res.getConcepts().add(new ConceptLightResponse(conceptSMTK));
        }

        res.setQuantity(res.getConcepts().size());

        return res;
    }

    public BioequivalentSearchResponse getBioequivalentes(String conceptId, String descriptionId) throws
            Exception {
        if ((conceptId == null || "".equals(conceptId))
                && (descriptionId == null || "".equals(descriptionId))) {
            throw new IllegalInputFault("Debe indicar por lo menos un idConcepto o idDescripcion");
        }

        ConceptSMTK conceptSMTK = getConcept(conceptId, descriptionId);

        if(!conceptSMTK.getCategory().getName().equalsIgnoreCase("Fármacos - Producto Comercial")) {
            throw new IllegalInputFault("Concepto "+conceptSMTK.toString()+" no corresponde a un Producto Comercial");
        };

        BioequivalentSearchResponse res = new BioequivalentSearchResponse();

        res.setConceptId(conceptSMTK.getConceptID());
        res.setDescription(conceptSMTK.getDescriptionFavorite().getTerm());
        res.setDescriptionId(conceptSMTK.getDescriptionFavorite().getDescriptionId());
        res.setCategory(conceptSMTK.getCategory().getName());

        RelationshipDefinition ispRelationshipDefinition = conceptSMTK.getCategory().findRelationshipDefinitionsByName(TargetDefinition.ISP).get(0);

        for (Relationship relationship : relationshipManager.getRelationshipsBySourceConcept(conceptSMTK)) {
            if (relationship.getRelationshipDefinition().isBioequivalente()) {
                res.getBioequivalentsResponse().add(BioequivalentMapper.map(relationship, relationshipManager.findRelationshipsLike(ispRelationshipDefinition,relationship.getTarget())));
            }
        }

        res.setQuantity(res.getBioequivalentsResponse().size());

        return res;
    }

    public ISPRegisterSearchResponse getRegistrosISP(String conceptId, String descriptionId) throws
            Exception {
        if ((conceptId == null || "".equals(conceptId))
                && (descriptionId == null || "".equals(descriptionId))) {
            throw new IllegalInputFault("Debe indicar por lo menos un idConcepto o idDescripcion");
        }

        ConceptSMTK conceptSMTK = getConcept(conceptId, descriptionId);
        conceptSMTK.setRelationships(relationshipManager.getRelationshipsBySourceConcept(conceptSMTK));
        ISPRegisterSearchResponse res = new ISPRegisterSearchResponse();

        res.setConceptId(conceptSMTK.getConceptID());
        res.setDescription(conceptSMTK.getDescriptionFavorite().getTerm());
        res.setDescriptionId(conceptSMTK.getDescriptionFavorite().getDescriptionId());
        res.setCategory(conceptSMTK.getCategory().getName());

        for (Relationship relationship : conceptSMTK.getValidRelationships()) {
            if (relationship.getRelationshipDefinition().isISP()) {
                res.getIspRegistersResponse().add(ISPRegisterMapper.map(relationship));
            }
        }

        res.setQuantity(res.getIspRegistersResponse().size());

        return res;
    }

    private ConceptSMTK getConcept(String conceptId, String descriptionId) throws NotFoundFault {
        ConceptSMTK conceptSMTK = null;

        try {
            if (descriptionId != null && !descriptionId.isEmpty()) {
                conceptSMTK = this.conceptManager.getConceptByDescriptionID(descriptionId);
            } else {
                conceptSMTK = this.conceptManager.getConceptByCONCEPT_ID(conceptId);
            }
        } catch (Exception e) {
            throw new NotFoundFault(e.getMessage());
        }

        if (conceptSMTK == null) {
            throw new NotFoundFault("Concepto no encontrado: " + (conceptId != null ? conceptId : "") +
                    (descriptionId != null ? descriptionId : ""));
        }

        return conceptSMTK;
    }

    public ConceptResponse loadAttributes(@NotNull ConceptResponse conceptResponse, @NotNull ConceptSMTK source) {
        //if (conceptResponse.getAttributes() == null || conceptResponse.getAttributes().isEmpty()) {
            if (!source.isRelationshipsLoaded()) {
                source.setRelationships(relationshipManager.getRelationshipsBySourceConcept(source));
                //conceptManager.loadRelationships(source);
            }
            ConceptMapper.appendAttributes(conceptResponse, source);
        //}
        return conceptResponse;
    }

    public ConceptResponse loadRelationships(@NotNull ConceptResponse conceptResponse, @NotNull ConceptSMTK source) throws Exception {
        if (conceptResponse.getRelationships() == null || conceptResponse.getRelationships().isEmpty()) {
            if (!source.isRelationshipsLoaded()) {
                source.setRelationships(relationshipManager.getRelationshipsBySourceConcept(source));
            }
            ConceptMapper.appendRelationships(conceptResponse, source);
        }
        return conceptResponse;
    }

    public ConceptResponse loadSnomedCTRelationships(@NotNull ConceptResponse conceptResponse, @NotNull ConceptSMTK
            source) throws Exception {
        if (conceptResponse.getSnomedCTRelationshipResponses() == null || conceptResponse
                .getSnomedCTRelationshipResponses().isEmpty()) {
            if (!source.isRelationshipsLoaded()) {
                source.setRelationships(relationshipManager.getRelationshipsBySourceConcept(source));
            }
            ConceptMapper.appendSnomedCTRelationships(conceptResponse, source);
        }

        return conceptResponse;
    }

    public ConceptResponse loadRefSets(@NotNull ConceptResponse conceptResponse, @NotNull ConceptSMTK source) {
        if (conceptResponse.getRefsets() == null || conceptResponse.getRefsets().isEmpty()) {
            if (source.getRefsets() == null || source.getRefsets().isEmpty()) {
                refSetManager.loadConceptRefSets(source);
            }
            ConceptMapper.appendRefSets(conceptResponse, source);
        }
        return conceptResponse;
    }

    public ConceptResponse loadTags(@NotNull ConceptResponse conceptResponse, @NotNull ConceptSMTK source) {
        if (conceptResponse.getTags() == null || conceptResponse.getTags().isEmpty()) {
            if (source.getRefsets() == null || source.getRefsets().isEmpty()) {
                refSetManager.loadConceptRefSets(source);
            }
            ConceptMapper.appendTags(conceptResponse, source);
        }
        return conceptResponse;
    }

    public ConceptResponse loadIndirectCrossmaps(@NotNull ConceptResponse res, @NotNull ConceptSMTK conceptSMTK) throws Exception {
        if (res.getIndirectCrossMaps() == null || res.getIndirectCrossMaps().isEmpty()) {

            DescriptionIDorConceptIDRequest request = new DescriptionIDorConceptIDRequest();
            request.setDescriptionId(conceptSMTK.getDescriptionFavorite().getDescriptionId());
            IndirectCrossMapSearchResponse indirectCrossMapSearchResponse = this.crossmapController.getIndirectCrossmapsByDescriptionID(request);

            res.setIndirectCrossMaps(indirectCrossMapSearchResponse.getIndirectCrossMapResponses());
        }

        return res;
    }

    private ConceptResponse loadDirectCrossmaps(@NotNull ConceptResponse conceptResponse, @NotNull ConceptSMTK conceptSMTK) {
        if (conceptResponse.getCrossmapSetMember() == null || conceptResponse.getCrossmapSetMember().isEmpty()) {
            DescriptionIDorConceptIDRequest request = new DescriptionIDorConceptIDRequest();
            request.setDescriptionId(conceptSMTK.getDescriptionFavorite().getDescriptionId());
            CrossmapSetMembersResponse crossmapSetMembersResponse = this.crossmapController.getDirectCrossmapsSetMembersByDescriptionID(conceptSMTK);
            conceptResponse.setCrossmapSetMember(crossmapSetMembersResponse.getCrossmapSetMemberResponses());
        }

        return conceptResponse;
    }

    /**
     * Este método es responspable de interactuar con la componente de negocio encargada de los nuevos términos y
     * realizar la solicitud de creación de uno.
     *
     * @param termRequest La solicitud de creación de término.
     * @return La respuesta respecto a la descripción creada.
     */
    public NewTermResponse requestTermCreation(NewTermRequest termRequest, User user, Institution institution) throws IllegalInputFault, NotFoundFault {

        //User user = new User(1, "demo", "Demo User", "demo", false);

        //User user = userManager.getUserByEmail("user@admin.cl");

        if (termRequest.getTerm() == null || termRequest.getTerm().isEmpty()) {
            throw new IllegalInputFault("Debe ingresar un término propuesto");
        }

        Category category = categoryManager.getCategoryByName(termRequest.getCategory());

        if (category == null) {
            throw new IllegalInputFault("Categoria no encontrada: " + termRequest.getCategory());
        }

        PendingTerm pendingTerm = new PendingTerm(
                termRequest.getTerm(),
                new Date(),
                false, /*Por defecto un término pendiente es insensible a mayúscula*/
                category,
                termRequest.getProfessional(),
                termRequest.getProfesion().isEmpty()?" ":termRequest.getProfesion(),
                termRequest.getSpecialty().isEmpty()?" ":termRequest.getSpecialty(),
                termRequest.getSubSpecialty().isEmpty()?" ":termRequest.getSubSpecialty(),
                termRequest.getEmail(),
                termRequest.getObservation().isEmpty()?" ":termRequest.getObservation(),
                institution.getName());

        /* Se realiza la solicitud */
        Description description;

        try {
            description = pendingTermManager.addPendingTerm(pendingTerm, user);
        }
        catch (EJBException e) {
            throw new NotFoundFault(e.getMessage());
        }

        return new NewTermResponse(description.getDescriptionId(), description.getTerm());
    }

    /**
     * Este método es responsable de recuperar todos los conceptos en las categorías indicadas.
     *
     * @param categoryNames Nombres de las categorías en las que se desea realizar la búsqueda.
     * @param requestable   Indica si el atributo 'Pedible' tiene valor <code>true</code> o <code>false</code>.
     * @return La lista de Conceptos Light que satisfacen la búsqueda.
     */
    public TermSearchesResponse searchRequestableDescriptions(List<String> categoryNames, boolean requestable) {

        TermSearchesResponse res = new TermSearchesResponse();

        for (String categoryName : categoryNames) {

            List<ConceptSMTK> allRequestableConcepts = new ArrayList<>();

            Category aCategory = categoryManager.getCategoryByName(categoryName);
            RelationshipDefinition theRelationshipDefinition = null;

            for (RelationshipDefinition relationshipDefinition : aCategory.getRelationshipDefinitions()) {
                if(relationshipDefinition.isPedible()){
                    theRelationshipDefinition = relationshipDefinition;
                    break;
                }
            }

            for (Relationship relationship : relationshipManager.findRelationshipsLike(theRelationshipDefinition, new BasicTypeValue(requestable))) {

                if(relationship.getSourceConcept().getCategory().equals(aCategory) && relationship.getSourceConcept().isModeled()) {
                    allRequestableConcepts.add(relationship.getSourceConcept());
                }
            }

            TermSearchResponse termSearchResponse = new TermSearchResponse(allRequestableConcepts);

            termSearchResponse.setQuantity(allRequestableConcepts.size());

            termSearchResponse.setCategory(aCategory.getName());

            res.getTermSearchResponses().add(termSearchResponse);

            res.setQuantity(res.getQuantity()+allRequestableConcepts.size());
        }

        res.setRequestable(requestable);

        return res;
    }

    /**
     * Este método es responsable de recuperar todos los conceptos en las categorías indicadas.
     *
     * @param request Nombres de las categorías en las que se desea realizar la búsqueda.
     * @return La lista de Conceptos Light que satisfacen la búsqueda.
     */
    public GTINByConceptIDResponse searchGTINByConceptID(GTINByConceptIDRequest request) throws NotFoundFault, IllegalInputFault {

        ConceptSMTK conceptSMTK = getConcept(request.getConceptID(), request.getDescriptionID());

        conceptSMTK.setRelationships(relationshipManager.getRelationshipsBySourceConcept(conceptSMTK));

        //Si la categoría no tiene la definición de atributo GS1
        if(conceptSMTK.getCategory().findRelationshipDefinitionsByName(TargetDefinition.GTINGS1).isEmpty()) {
            throw new NotFoundFault("El concepto: "+ conceptSMTK +" perteneciente a categoría: "+conceptSMTK.getCategory()+" no posee la definición GTIN");
        }

        //Si esta ok, construir respuesta
        GTINByConceptIDResponse res = new GTINByConceptIDResponse(conceptSMTK);

        return res;
    }

    /**
     * Este método es responsable de recuperar todos los conceptos en las categorías indicadas.
     *
     * @param request Nombres de las categorías en las que se desea realizar la búsqueda.
     * @return La lista de Conceptos Light que satisfacen la búsqueda.
     */
    public ConceptIDByGTINResponse searchConceptIDByGTIN(ConceptIDByGTINRequest request) throws NotFoundFault, IllegalInputFault {

        BasicTypeValue GS1 = new BasicTypeValue(request.getGTIN());

        Category category = CategoryFactory.getInstance().findCategoryByName("Fármacos - Producto Comercial con Envase");

        RelationshipDefinition relationshipDefinition = category.findRelationshipDefinitionsByName(TargetDefinition.GTINGS1).get(0);

        ConceptIDByGTINResponse res = null;

        List<Relationship> relationships = relationshipManager.findRelationshipsLike(relationshipDefinition, GS1);

        if(relationships.isEmpty()) {
            throw new NotFoundFault("No existen conceptos pertencecientes a "+category.getName()+" con atributo "+relationshipDefinition.getName()+" con valor: '"+GS1+"'");
        }

        for (Relationship relationship : relationshipManager.findRelationshipsLike(relationshipDefinition, GS1)) {
            ConceptSMTK conceptSMTK = relationship.getSourceConcept();
            conceptSMTK.setRelationships(relationshipManager.getRelationshipsBySourceConcept(conceptSMTK));
            res = new ConceptIDByGTINResponse(conceptSMTK);
        }

        return res;
    }

}
