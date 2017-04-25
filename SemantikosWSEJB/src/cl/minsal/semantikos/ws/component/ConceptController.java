package cl.minsal.semantikos.ws.component;

import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.ws.fault.IllegalInputFault;
import cl.minsal.semantikos.ws.fault.NotFoundFault;
import cl.minsal.semantikos.ws.mapping.BioequivalentMapper;
import cl.minsal.semantikos.ws.mapping.ConceptMapper;
import cl.minsal.semantikos.ws.mapping.ISPRegisterMapper;
import cl.minsal.semantikos.ws.request.DescriptionIDorConceptIDRequest;
import cl.minsal.semantikos.ws.request.NewTermRequest;
import cl.minsal.semantikos.ws.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cl.minsal.semantikos.kernel.daos.DescriptionDAOImpl.NO_VALID_TERMS;
import static java.util.Collections.EMPTY_LIST;

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
    private CategoryManager categoryManager;
    @EJB
    private PaginationController paginationController;
    @EJB
    private CategoryController categoryController;
    @EJB
    private RefSetController refSetController;
    @EJB
    private PendingTermsManager pendingTermManager;
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
            throws NotFoundFault {

        /* Lo primero consiste en recuperar el concepto cuyos conceptos relacionados se quiere recuperar. */
        ConceptSMTK sourceConcept;
        sourceConcept = getSourceConcept(descriptionId, conceptId);

        Category category;
        try {
            category = this.categoryManager.getCategoryByName(categoryName);
        }
        /* Si no hay categoría con ese nombre se obtiene una excepción (pues debiera haberlo encontrado */ catch
                (EJBException e) {
            logger.error("Se buscó una categoría inexistente: " + categoryName, e);
            throw e;
        }

        List<ConceptResponse> relatedResponses = new ArrayList<>();
        List<ConceptSMTK> relatedConcepts = this.conceptManager.getRelatedConcepts(sourceConcept, category);
        for (ConceptSMTK related : relatedConcepts) {
            ConceptResponse conceptResponse = new ConceptResponse(related);
            //conceptResponse.setForREQWS002();
            this.loadAttributes(conceptResponse, related);
            this.loadSnomedCTRelationships(conceptResponse, related);
            this.loadIndirectCrossmaps(conceptResponse, related);
            this.loadDirectCrossmaps(conceptResponse, related);
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
        if (conceptId != null && !conceptId.isEmpty()) {
            source = this.conceptManager.getConceptByCONCEPT_ID(conceptId);
        } else if (descriptionId != null && !descriptionId.isEmpty()) {
            source = this.conceptManager.getConceptByDescriptionID(descriptionId);
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
    public GenericTermSearchResponse searchTermGeneric(
            String term,
            List<String> categoriesNames,
            List<String> refSetsNames
    ) throws NotFoundFault {

        if(!categoriesNames.contains("Concepto Especial")) {
            categoriesNames.add("Concepto Especial");
        }
        GenericTermSearchResponse res = new GenericTermSearchResponse();
        List<Category> categories = this.categoryController.findCategories(categoriesNames);
        List<RefSet> refSets = this.refSetController.findRefsets(refSetsNames);

        List<PerfectMatchDescriptionResponse> perfectMatchDescriptions = new ArrayList<>();
        List<NoValidDescriptionResponse> noValidDescriptions = new ArrayList<>();
        List<PendingDescriptionResponse> pendingDescriptions = new ArrayList<>();

        List<Description> descriptions = this.descriptionManager.searchDescriptionsPerfectMatch(term, categories, refSets);
        logger.debug("ws-req-001. descripciones encontradas: " + descriptions);

        for (Description description : descriptions) {

            logger.info("ws-req-001. descripciones encontrada: " + description.fullToString());

            if( NO_VALID_TERMS.contains(description.getTerm()) ) {
                continue;
            }

            /* Caso 1: es una descripcion del concepto especial No valido */
            if ("Concepto no válido".equals(description.getConceptSMTK().getDescriptionFavorite().getTerm()) ) {
                NoValidDescription noValidDescription = this.descriptionManager.getNoValidDescriptionByID(description.getId());
                if (noValidDescription != null) {
                    noValidDescriptions.add(new NoValidDescriptionResponse(noValidDescription));
                } else {
                    perfectMatchDescriptions.add(new PerfectMatchDescriptionResponse(description));
                }
            } else if ("Pendientes".equals(description.getConceptSMTK().getDescriptionFavorite().getTerm())) {
                pendingDescriptions.add(new PendingDescriptionResponse(description));
            } else {
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
        if(!categoriesNames.contains("Concepto Especial")) {
            categoriesNames.add("Concepto Especial");
        }
        List<Category> categories = this.categoryController.findCategories(categoriesNames);
        List<RefSet> refSets = this.refSetController.findRefsets(refSetsNames);

        List<PerfectMatchDescriptionResponse> perfectMatchDescriptions = new ArrayList<>();
        List<NoValidDescriptionResponse> noValidDescriptions = new ArrayList<>();
        List<PendingDescriptionResponse> pendingDescriptions = new ArrayList<>();

        List<Description> descriptions = this.descriptionManager.searchDescriptionsTruncateMatch(term, categories, refSets);
        logger.debug("ws-req-001. descripciones encontradas: " + descriptions);

        for (Description description : descriptions) {

            logger.info("ws-req-001. descripciones encontrada: " + description.fullToString());

            if( NO_VALID_TERMS.contains(description.getTerm()) ) {
                continue;
            }

            /* Caso 1: es una descripcion del concepto especial No valido */
            if ("Concepto no válido".equals(description.getConceptSMTK().getDescriptionFavorite().getTerm())) {
                NoValidDescription noValidDescription = this.descriptionManager.getNoValidDescriptionByID(description
                        .getId());
                if (noValidDescription != null) {
                    noValidDescriptions.add(new NoValidDescriptionResponse(noValidDescription));
                } else {
                    perfectMatchDescriptions.add(new PerfectMatchDescriptionResponse(description));
                }
            } else if ("Pendientes".equals(description.getConceptSMTK().getDescriptionFavorite().getTerm())) {
                pendingDescriptions.add(new PendingDescriptionResponse(description));
            } else {
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

        List<Description> descriptions = this.descriptionManager.searchDescriptionsTruncateMatch(term, categories, EMPTY_LIST);

        logger.debug("ws-req-006. descripciones encontradas: " + descriptions);

        int cont = 0;

        for (Description description : descriptions) {

            if(cont==5) {
                break;
            }

            logger.info("ws-req-006. descripciones encontrada: " + description.fullToString());

            suggestedDescriptions.add(new SuggestedDescriptionResponse(description));

            cont++;
        }

        res.setPattern(term);
        res.setSuggestedDescriptionResponses(suggestedDescriptions);
        res.setQuantity(descriptions.size());

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
        List<Category> categories = this.categoryController.findCategories(categoriesNames);
        List<RefSet> refSets = this.refSetController.findRefsets(refSetsNames);

        /* Se realiza la búsqueda */
        List<ConceptSMTK> conceptSMTKS = this.conceptManager.findConceptTruncatePerfect(term, categories, refSets);

        /* Se encapsulan los conceptos retornados en un wrapper XML */
        List<ConceptResponse> conceptResponses = new ArrayList<>();
        for (ConceptSMTK conceptSMTK : conceptSMTKS) {
            conceptResponses.add(new ConceptResponse(conceptSMTK));
        }

        return new ConceptsResponse(conceptResponses);
    }

    public ConceptResponse conceptByDescriptionId(String descriptionId)
            throws NotFoundFault {
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
    ) throws NotFoundFault {

        /* Logging de invocación del servicio */
        logger.info("SearchService:findConceptsByCategory(" + categoryName + ", " + idEstablecimiento + ")");

        /* Se recupera la categoría */
        Category category;
        try {
            category = this.categoryManager.getCategoryByName(categoryName);
        } catch (IllegalArgumentException iae) {
            throw new NotFoundFault("No se encontró una categoría de nombre '" + categoryName + "'");
        }

        List<ConceptSMTK> concepts = this.conceptManager.findConceptsBy(category);

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
        logger.info("SearchService:findConceptsByCategory(" + categoryName + ", " + idEstablecimiento + ")");

        /* Se recupera la categoría */
        Category category;
        try {
            category = this.categoryManager.getCategoryByName(categoryName);
        } catch (IllegalArgumentException iae) {
            throw new NotFoundFault("No se encontró una categoría de nombre '" + categoryName + "'");
        }

        List<ConceptSMTK> concepts = this.conceptManager.findModeledConceptPaginated(category, pageSize, pageNumber);

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
     * Este método es responsable de recuperar todos los conceptos de un RefSet.
     *
     * @param refSetName El RefSet cuyos conceptos se desea recuperar.
     * @return Una lista de conceptos que pertenecen al refset <code>refSetName</code>.
     * @throws NotFoundFault Arrojada si...
     */
    public RefSetResponse conceptsByRefset(String refSetName) throws NotFoundFault {
        RefSet refSet = this.refSetManager.getRefsetByName(refSetName);
        RefSetResponse res = new RefSetResponse();

        res.setName(refSet.getName());
        res.setInstitution(refSet.getInstitution().getName());
        res.setCreationDate(refSet.getCreationDate());
        res.setValid(refSet.getValidityUntil()==null);
        res.setValidityUntil(refSet.getValidityUntil());

        for (ConceptSMTK conceptSMTK : refSet.getConcepts()) {
            res.getConcepts().add(new ConceptResponse(conceptSMTK));
        }

        return res;
    }

    public BioequivalentSearchResponse getBioequivalentes(String conceptId, String descriptionId) throws
            IllegalInputFault, NotFoundFault {
        if ((conceptId == null || "".equals(conceptId))
                && (descriptionId == null || "".equals(descriptionId))) {
            throw new IllegalInputFault("Debe indicar por lo menos un idConcepto o idDescripcion");
        }

        ConceptSMTK conceptSMTK = getConcept(conceptId, descriptionId);
        this.conceptManager.loadRelationships(conceptSMTK);
        BioequivalentSearchResponse res = new BioequivalentSearchResponse();

        res.setConceptId(conceptSMTK.getConceptID());
        res.setDescription(conceptSMTK.getDescriptionFavorite().getTerm());
        res.setDescriptionId(conceptSMTK.getDescriptionFavorite().getDescriptionId());
        res.setCategory(conceptSMTK.getCategory().getName());

        for (Relationship relationship : conceptSMTK.getRelationships()) {
            if (relationship.getRelationshipDefinition().isBioequivalente()) {
                res.getBioequivalentsResponse().add(BioequivalentMapper.map(relationship, relationshipManager.findRelationshipsLike(relationship)));
            }
        }

        return res;
    }

    public ISPRegisterSearchResponse getRegistrosISP(String conceptId, String descriptionId) throws
            IllegalInputFault, NotFoundFault {
        if ((conceptId == null || "".equals(conceptId))
                && (descriptionId == null || "".equals(descriptionId))) {
            throw new IllegalInputFault("Debe indicar por lo menos un idConcepto o idDescripcion");
        }

        ConceptSMTK conceptSMTK = getConcept(conceptId, descriptionId);
        this.conceptManager.loadRelationships(conceptSMTK);
        ISPRegisterSearchResponse res = new ISPRegisterSearchResponse();

        res.setConceptId(conceptSMTK.getConceptID());
        res.setDescription(conceptSMTK.getDescriptionFavorite().getTerm());
        res.setDescriptionId(conceptSMTK.getDescriptionFavorite().getDescriptionId());
        res.setCategory(conceptSMTK.getCategory().getName());

        for (Relationship relationship : conceptSMTK.getRelationships()) {
            if (relationship.getRelationshipDefinition().isISP()) {
                res.getIspRegistersResponse().add(ISPRegisterMapper.map(relationship));
            }
        }

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
            throw new NotFoundFault("Concepto no encontrado: " + (conceptId != null ? conceptId : "") +
                    (descriptionId != null ? descriptionId : ""));
        }

        if (conceptSMTK == null) {
            throw new NotFoundFault("Concepto no encontrado: " + (conceptId != null ? conceptId : "") +
                    (descriptionId != null ? descriptionId : ""));
        }

        return conceptSMTK;
    }

    public ConceptResponse loadAttributes(@NotNull ConceptResponse conceptResponse, @NotNull ConceptSMTK source) {
        if (conceptResponse.getAttributes() == null || conceptResponse.getAttributes().isEmpty()) {
            if (!source.isRelationshipsLoaded()) {
                conceptManager.loadRelationships(source);
            }
            ConceptMapper.appendAttributes(conceptResponse, source);
        }
        return conceptResponse;
    }

    public ConceptResponse loadRelationships(@NotNull ConceptResponse conceptResponse, @NotNull ConceptSMTK source) {
        if (conceptResponse.getRelationships() == null || conceptResponse.getRelationships().isEmpty()) {
            if (!source.isRelationshipsLoaded()) {
                conceptManager.loadRelationships(source);
            }
            ConceptMapper.appendRelationships(conceptResponse, source);
        }
        return conceptResponse;
    }

    public ConceptResponse loadSnomedCTRelationships(@NotNull ConceptResponse conceptResponse, @NotNull ConceptSMTK
            source) {
        if (conceptResponse.getSnomedCTRelationshipResponses() == null || conceptResponse
                .getSnomedCTRelationshipResponses().isEmpty()) {
            if (!source.isRelationshipsLoaded()) {
                conceptManager.loadRelationships(source);
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

    public ConceptResponse loadIndirectCrossmaps(@NotNull ConceptResponse res, @NotNull ConceptSMTK conceptSMTK) {
        if (res.getIndirectCrossMaps() == null || res.getIndirectCrossMaps().isEmpty()) {

            DescriptionIDorConceptIDRequest request = new DescriptionIDorConceptIDRequest();
            request.setDescriptionId(conceptSMTK.getDescriptionFavorite().getDescriptionId());
            IndirectCrossMapSearchResponse indirectCrossMapSearchResponse = this.crossmapController
                    .getIndirectCrossmapsByDescriptionID(request);

            res.setIndirectCrossMaps(indirectCrossMapSearchResponse.getIndirectCrossMapResponses());
        }

        return res;
    }

    private ConceptResponse loadDirectCrossmaps(@NotNull ConceptResponse conceptResponse, @NotNull ConceptSMTK
            conceptSMTK) {
        if (conceptResponse.getCrossmapSetMember() == null || conceptResponse.getCrossmapSetMember().isEmpty()) {
            DescriptionIDorConceptIDRequest request = new DescriptionIDorConceptIDRequest();
            request.setDescriptionId(conceptSMTK.getDescriptionFavorite().getDescriptionId());
            CrossmapSetMembersResponse crossmapSetMembersResponse = this.crossmapController
                    .getDirectCrossmapsSetMembersByDescriptionID(request);
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
    public NewTermResponse requestTermCreation(NewTermRequest termRequest) throws IllegalInputFault {

        User user = new User(1, "demo", "Demo User", "demo", false);


        Category category = categoryManager.getCategoryByName(termRequest.getCategory());
        if (category == null) {
            throw new IllegalInputFault("Categoria no encontrada: " + termRequest.getCategory());
        }

        PendingTerm pendingTerm = new PendingTerm(
                termRequest.getTerm(),
                new Date(),
                termRequest.getCaseSensitive(),
                category,
                termRequest.getProfessional(),
                termRequest.getProfesion(),
                termRequest.getSpecialty(),
                termRequest.getSubSpecialty(),
                termRequest.getEmail(),
                termRequest.getObservation(),
                termRequest.getIdInstitution());

        /* Se realiza la solicitud */
        Description description = pendingTermManager.addPendingTerm(pendingTerm, user);
        return new NewTermResponse(description.getDescriptionId());
    }

    /**
     * Este método es responsable de recuperar todos los conceptos en las categorías indicadas.
     *
     * @param categoryNames Nombres de las categorías en las que se desea realizar la búsqueda.
     * @param requestable   Indica si el atributo 'Pedible' tiene valor <code>true</code> o <code>false</code>.
     * @return La lista de Conceptos Light que satisfacen la búsqueda.
     */
    public TermSearchResponse searchRequestableDescriptions(List<String> categoryNames, Target requestable) {

        List<ConceptSMTK> allRequestableConcepts = new ArrayList<>();

        for (String categoryName : categoryNames) {

            Category aCategory = categoryManager.getCategoryByName(categoryName);
            RelationshipDefinition theRelationshipDefinition = null;

            for (RelationshipDefinition relationshipDefinition : aCategory.getRelationshipDefinitions()) {
                if(relationshipDefinition.isPedible()){
                    theRelationshipDefinition = relationshipDefinition;
                    break;
                }
            }

            for (Relationship relationship : relationshipManager.findRelationshipsLike(theRelationshipDefinition, requestable)) {

                if(relationship.getSourceConcept().getCategory().equals(aCategory)) {
                    allRequestableConcepts.add(relationship.getSourceConcept());
                }
            }
        }

        return new TermSearchResponse(allRequestableConcepts);
    }

    /**
     * Este método es responsable de retornar el atributo 'Pedible' de la categoría dada.
     *
     * @param aCategory La categoría cuyo atributo 'Pedible' se busca.
     * @return El atributo 'Pedible' de la categoría.
     */
    private RelationshipDefinition getRequestableAttribute(Category aCategory) {

        List<RelationshipDefinition> relationshipDefinitions = aCategory.getRelationshipDefinitions();
        for (RelationshipDefinition relationshipDefinition : relationshipDefinitions) {
            if (relationshipDefinition.getName().equalsIgnoreCase("Pedible")) {
                return relationshipDefinition;
            }
        }

        throw new IllegalArgumentException("La categoría solicitada no posee un atributo 'Pedible'");
    }
}
