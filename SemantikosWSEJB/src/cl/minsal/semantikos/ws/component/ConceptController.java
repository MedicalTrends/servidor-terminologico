package cl.minsal.semantikos.ws.component;

import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.ws.Util;
import cl.minsal.semantikos.ws.fault.IllegalInputFault;
import cl.minsal.semantikos.ws.fault.NotFoundFault;
import cl.minsal.semantikos.ws.mapping.ConceptMapper;
import cl.minsal.semantikos.ws.request.NewTermRequest;
import cl.minsal.semantikos.ws.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Alfonso Cornejo on 2016-11-17.
 */
@Stateless
public class ConceptController {

    /** El logger para la clase */
    private static final Logger logger = LoggerFactory.getLogger(ConceptController.class);

    @EJB
    private ConceptManager conceptManager;
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

    /**
     * Este método es responsable de recperar los conceptos relacionados (hijos...) de un concepto que se encuentran en
     * una cierta categoría.
     *
     * @param descriptionId El DESCRIPTION_ID del concepto cuyos conceptos relacionados se desea buscar.
     * @param conceptId     El CONCEPT_ID del concepto cuyos conceptos relacionados se desea recuperar. Este parámetro
     *                      es considerado únicamente si el DESCRIPTION_ID no fue especificado.
     * @param categoryName  El nombre de la categoría a la cual pertenecen los objetos relacionados que se buscan.
     *
     * @return Los conceptos relacionados en un envelope apropiado.
     *
     * @throws NotFoundFault Arrojada si no se encuentran resultados.
     */
    public RelatedConceptsResponse findRelated(String descriptionId, String conceptId, @NotNull String categoryName) throws NotFoundFault {

        /* Lo primero consiste en recuperar el concepto cuyos conceptos relacionados se quiere recuperar. */
        ConceptSMTK sourceConcept;
        sourceConcept = getSourceConcept(descriptionId, conceptId);

        Category category;
        try {
            category = this.categoryManager.getCategoryByName(categoryName);
        }
        /* Si no hay categoría con ese nombre se obtiene una excepción (pues debiera haberlo encontrado */ catch (EJBException e) {
            logger.error("Se buscó una categoría inexistente: " + categoryName, e);
            throw e;
        }

        List<ConceptResponse> relatedResponses = new ArrayList<>();
        List<ConceptSMTK> relatedConcepts = this.conceptManager.getRelatedConcepts(sourceConcept, category);
        for (ConceptSMTK related : relatedConcepts) {
            relatedResponses.add(new ConceptResponse(related));
        }

        RelatedConceptsResponse res = new RelatedConceptsResponse();
        res.setRelatedConcepts(relatedResponses);

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
     *
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

    public RelatedConceptsResponse findRelatedLite(String conceptId, String descriptionId, String categoryName) throws NotFoundFault {
        RelatedConceptsResponse res = new RelatedConceptsResponse();

        Category category = this.categoryManager.getCategoryByName(categoryName);
        if (category == null) {
            throw new NotFoundFault("Categoria no encontrada");
        }

        ConceptSMTK source = null;
        if (conceptId != null) {
            source = this.conceptManager.getConceptByCONCEPT_ID(conceptId);
        } else if (descriptionId != null) {
            source = this.conceptManager.getConceptByDescriptionID(descriptionId);
        }

        if (source != null) {
            List<ConceptResponse> relatedResponses = new ArrayList<>();
            List<ConceptSMTK> relateds = this.conceptManager.getRelatedConcepts(source);

            if (relateds != null) {
                for (ConceptSMTK related : relateds) {
                    if (category.equals(related.getCategory())) {
                        ConceptResponse relatedResponse = new ConceptResponse(related);
                        relatedResponses.add(relatedResponse);
                    }
                }
            }

            res.setRelatedConcepts(relatedResponses);
        }

        // TODO: atributos
        return res;
    }

    /**
     * REQ-WS-001
     * Este método es responsable de buscar un concepto segun una de sus descripciones que coincidan por perfect match
     * con el <em>TERM</em> dado en los REFSETS y Categorias indicadas.
     *
     * @param term              El termino a buscar por perfect Match.
     * @param categoriesNames   Nombres de las Categorias donde se deben hacer las búsquedas.
     * @param refSetsNames      Nombres de los REFSETS donde se deben hacer las búsquedas.
     * @return                  Conceptos buscados segun especificaciones de REQ-WS-001.
     * @throws NotFoundFault    Si uno de los nombres de Categorias o REFSETS no existe.
     */
    public GenericTermSearchResponse searchTermGeneric(
            String term,
            List<String> categoriesNames,
            List<String> refSetsNames
    ) throws NotFoundFault {
        GenericTermSearchResponse res = new GenericTermSearchResponse();
        List<Category> categories = this.categoryController.findCategories(categoriesNames);
        List<RefSet> refSets = this.refSetController.findRefsets(refSetsNames);

        List<PerfectMatchDescriptionResponse> perfectMatchDescriptions = new ArrayList<>();
        List<NoValidDescriptionResponse> noValidDescriptions = new ArrayList<>();
        List<PendingDescriptionResponse> pendingDescriptions = new ArrayList<>();

        List<Description> descriptions = this.descriptionManager.searchDescriptionsByTerm(term, categories, refSets);

        if ( descriptions != null ) {
            for ( Description description : descriptions ) {
                if ( !description.isValid() ) {
                    List<ConceptSMTK> suggestedConcepts = new ArrayList<>(); // TODO: Obtener
                    List<Description> suggestedDescriptions = new ArrayList<>(); // TODO: Obtener
                    noValidDescriptions.add(new NoValidDescriptionResponse(description, suggestedConcepts, suggestedDescriptions));
                } else if ( !description.isModeled() ) {
                    List<Description> descriptions1 = new ArrayList<>(); // TODO: Obtener
                    pendingDescriptions.add(new PendingDescriptionResponse(description, descriptions1));
                } else {
                    perfectMatchDescriptions.add(new PerfectMatchDescriptionResponse(description));
                }
            }
        }

        res.setPerfectMatchDescriptions(perfectMatchDescriptions);
        res.setNoValidDescriptions(noValidDescriptions);
        res.setPendingDescriptions(pendingDescriptions);

        return res;
    }

    public TermSearchResponse searchTerm(
            String term,
            List<String> categoriesNames,
            List<String> refSetsNames
    ) throws NotFoundFault {
        List<Category> categories = this.categoryController.findCategories(categoriesNames);
        List<RefSet> refSets = this.refSetController.findRefsets(refSetsNames);

        List<ConceptLightResponse> conceptResponses = new ArrayList<>();
        List<Description> descriptions = this.descriptionManager.searchDescriptionsByTerm(term, categories, refSets);
        if (descriptions != null) {
            List<ConceptSMTK> conceptSMTKS = new ArrayList<>(descriptions.size());
            for (Description description : descriptions) {
                if (!conceptSMTKS.contains(description.getConceptSMTK())) {
                    conceptSMTKS.add(description.getConceptSMTK());
                }
            }

            for (ConceptSMTK source : conceptSMTKS) {
                // TODO: Agregar sugeridos
                ConceptLightResponse conceptResponse = new ConceptLightResponse(source);
                conceptResponses.add(conceptResponse);
            }
        }

        TermSearchResponse response = new TermSearchResponse();
        response.setConcepts(conceptResponses);

        if (conceptResponses.isEmpty()) {
            throw new NotFoundFault("Termino no encontrado");
        }

        return response;
    }

    public TermSearchResponse searchTruncatePerfect(
            String term,
            List<String> categoriesNames,
            List<String> refSetsNames,
            Integer pageNumber,
            Integer pageSize
    ) throws NotFoundFault {
        List<Category> categories = this.categoryController.findCategories(categoriesNames);
        List<RefSet> refSets = this.refSetController.findRefsets(refSetsNames);

        Long[] categoriesArray = Util.getIdArray(categories);
        Long[] refSetsArray = Util.getIdArray(refSets);
        List<ConceptSMTK> conceptSMTKS = this.conceptManager.findConceptTruncatePerfect(term, categoriesArray, refSetsArray, pageNumber, pageSize);
        List<ConceptLightResponse> conceptResponses = new ArrayList<>();

        if (conceptSMTKS != null) {
            for (ConceptSMTK source : conceptSMTKS) {
                conceptResponses.add(new ConceptLightResponse(source));
            }
        }

        TermSearchResponse response = new TermSearchResponse();
        response.setConcepts(conceptResponses);
        Integer total = this.conceptManager.countConceptBy(term, categoriesArray, refSetsArray);
        response.setPagination(this.paginationController.getResponse(pageNumber, pageSize, total));

        if (conceptResponses.isEmpty()) {
            throw new NotFoundFault("Termino no encontrado");
        }

        return response;
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
        this.loadRelationships(res, conceptSMTK);
        this.loadRefSets(res, conceptSMTK);
        // TODO: Atributos y Relaciones
        return res;
    }

    public ConceptResponse conceptById(String conceptId) throws NotFoundFault {
        ConceptSMTK conceptSMTK;
        try {
            conceptSMTK = this.conceptManager.getConceptByCONCEPT_ID(conceptId);
        } catch (Exception e) {
            throw new NotFoundFault("Concepto no encontrado: " + conceptId);
        }
        ConceptResponse res = new ConceptResponse(conceptSMTK);
        this.loadRelationships(res, conceptSMTK);
        this.loadRefSets(res, conceptSMTK);
        // TODO: Atributos y Relaciones
        return res;
    }

    public ConceptsByCategoryResponse conceptsByCategory(
            String categoryName,
            Integer pageNumber,
            Integer pageSize
    ) throws NotFoundFault {
        ConceptsByCategoryResponse res = new ConceptsByCategoryResponse();

        Category category = this.categoryManager.getCategoryByName(categoryName);
        CategoryResponse categoryResponse = this.categoryController.getResponse(category);
        res.setCategoryResponse(categoryResponse);

        Integer total = this.conceptManager.countModeledConceptBy(category);
        PaginationResponse paginationResponse = this.paginationController.getResponse(pageSize, pageNumber, total);
        res.setPaginationResponse(paginationResponse);

        List<ConceptSMTK> concepts = this.conceptManager.findModeledConceptBy(category, pageSize, pageNumber);
        List<ConceptResponse> conceptResponses = new ArrayList<>();
        if (concepts != null) {
            for (ConceptSMTK source : concepts) {
                ConceptResponse conceptResponse = new ConceptResponse(source);
                this.loadRelationships(conceptResponse, source);
                this.loadRefSets(conceptResponse, source);
                conceptResponses.add(conceptResponse);
            }
        }
        res.setConceptResponses(conceptResponses);

        // TODO relationships and attributes
        return res;
    }

    public ConceptsByRefsetResponse conceptsByRefset(
            String refSetName,
            Integer pageNumber,
            Integer pageSize
    ) throws NotFoundFault {
        // TODO
        ConceptsByRefsetResponse res = new ConceptsByRefsetResponse();

        RefSet refSet = this.refSetManager.getRefsetByName(refSetName);
        res.setRefSet(this.refSetController.getResponse(refSet));

        Integer total = this.conceptManager.countModeledConceptsBy(refSet);
        PaginationResponse paginationResponse = this.paginationController.getResponse(pageNumber, pageSize, total);
        res.setPagination(paginationResponse);

        List<ConceptSMTK> concepts = this.conceptManager.findModeledConceptsBy(refSet, pageNumber, pageSize);
        List<ConceptResponse> conceptResponses = new ArrayList<>();
        if (concepts != null) {
            for (ConceptSMTK source : concepts) {
                ConceptResponse conceptResponse = new ConceptResponse(source);
//                this.loadAttributes(conceptResponse, source);
//                this.loadCategory(conceptResponse, source);
                conceptResponses.add(conceptResponse);
            }
        }
        res.setConcepts(conceptResponses);

        return res;
    }

    public ConceptsByRefsetResponse conceptsByRefsetWithPreferedDescriptions(
            String refSetName,
            Integer pageNumber,
            Integer pageSize
    ) throws NotFoundFault {

        ConceptsByRefsetResponse res = new ConceptsByRefsetResponse();

        RefSet refSet = this.refSetManager.getRefsetByName(refSetName);
        res.setRefSet(this.refSetController.getResponse(refSet));

        Integer total = this.conceptManager.countModeledConceptsBy(refSet);
        PaginationResponse paginationResponse = this.paginationController.getResponse(pageNumber, pageSize, total);
        res.setPagination(paginationResponse);

        List<ConceptSMTK> concepts = this.conceptManager.findModeledConceptsBy(refSet, pageNumber, pageSize);
        List<ConceptResponse> conceptResponses = new ArrayList<>();
        if (concepts != null) {
            for (ConceptSMTK sourceConcept : concepts) {
                ConceptResponse conceptResponse = new ConceptResponse(sourceConcept);
                conceptResponses.add(conceptResponse);
            }
        }
        res.setConcepts(conceptResponses);

        return res;
    }

    public ConceptResponse loadRelationships(ConceptResponse conceptResponse, ConceptSMTK source) {
        if (conceptResponse.getRelationships() == null || conceptResponse.getRelationships().isEmpty()) {
            if (!source.isRelationshipsLoaded()) {
                conceptManager.loadRelationships(source);
            }
            ConceptMapper.appendRelationships(conceptResponse, source);
        }
        return conceptResponse;
    }

    public ConceptResponse loadRefSets(ConceptResponse conceptResponse, ConceptSMTK source) {
        if (conceptResponse.getRefsets() == null || conceptResponse.getRefsets().isEmpty()) {
            if (source.getRefsets() == null || source.getRefsets().isEmpty()) {
                refSetManager.loadConceptRefSets(source);
            }
            ConceptMapper.appendRefSets(conceptResponse, source);
        }
        return conceptResponse;
    }

    /**
     * Este método es responspable de interactuar con la componente de negocio encargada de los nuevos términos y
     * realizar la solicitud de creación de uno.
     *
     * @param termRequest La solicitud de creación de término.
     *
     * @return La respuesta respecto a la descripción creada.
     */
    public NewTermResponse requestTermCreation(NewTermRequest termRequest) throws IllegalInputFault {

        // TODO: Recuperar el usuario.
        User user = new User(-1, "demo", "Demo User", "demo", false);

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
                termRequest.getObservation());

        /* Se realiza la solicitud */
        Description description = pendingTermManager.addPendingTerm(pendingTerm, user);
        return new NewTermResponse(description.getDescriptionId());
    }

    /**
     * Este método es responsable de recuperar todos los conceptos en las categorías indicadas.
     *
     * @param categoryNames Nombres de las categorías en las que se desea realizar la búsqueda.
     * @param refSetNames   Nombres de los refsets en las que deben pertenecer los conceptos.
     * @param requestable   Indica si el atributo 'Pedible' tiene valor <code>true</code> o <code>false</code>.
     *
     * @return La lista de Conceptos Light que satisfacen la búsqueda.
     */
    public TermSearchResponse searchRequestableDescriptions(List<String> categoryNames, List<String> refSetNames, String requestable) {

        List<ConceptSMTK> allRequestableConcepts = new ArrayList<>();
        for (String categoryName : categoryNames) {
            Category aCategory = categoryManager.getCategoryByName(categoryName);

            /* Se recupera el atributo "Pedible " */
            RelationshipDefinition requestableAttribute = getRequestableAttribute(aCategory);
            List<ConceptSMTK> requestableConcepts = conceptManager.findConcepts(aCategory, refSetNames, requestableAttribute, requestable);
            allRequestableConcepts.addAll(requestableConcepts);
        }

        return new TermSearchResponse(allRequestableConcepts);
    }

    /**
     * Este método es responsable de retornar el atributo 'Pedible' de la categoría dada.
     *
     * @param aCategory La categoría cuyo atributo 'Pedible' se busca.
     *
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
