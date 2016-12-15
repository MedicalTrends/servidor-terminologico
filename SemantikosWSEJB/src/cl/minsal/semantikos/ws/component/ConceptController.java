package cl.minsal.semantikos.ws.component;

import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.DescriptionManager;
import cl.minsal.semantikos.kernel.components.RefSetManager;
import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.model.RefSet;
import cl.minsal.semantikos.ws.Util;
import cl.minsal.semantikos.ws.fault.NotFoundFault;
import cl.minsal.semantikos.ws.mapping.ConceptMapper;
import cl.minsal.semantikos.ws.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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
    private ConceptSMTK getSourceConcept(String descriptionId, String conceptId) {
        ConceptSMTK sourceConcept;
        if (descriptionId != null && !descriptionId.trim().equals("")) {
            sourceConcept = this.conceptManager.getConceptByDescriptionID(descriptionId);
        }

        /* Sólo si falla lo anterior: CONCEPT_ID */
        else if (conceptId != null && !conceptId.trim().equals("")) {
            sourceConcept = this.conceptManager.getConceptByCONCEPT_ID(conceptId);
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

    public TermSearchResponse searchTerm(
            String term,
            List<String> categoriesNames,
            List<String> refSetsNames
    ) throws NotFoundFault {
        List<Category> categories = this.categoryController.findCategories(categoriesNames);
        List<RefSet> refSets = this.refSetController.findRefsets(refSetsNames);

        List<ConceptResponse> conceptResponses = new ArrayList<>();
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
                ConceptResponse conceptResponse = new ConceptResponse(source);
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
        List<ConceptResponse> conceptResponses = new ArrayList<>();

        if (conceptSMTKS != null) {
            for (ConceptSMTK source : conceptSMTKS) {
                ConceptResponse conceptResponse = new ConceptResponse(source);
                conceptResponses.add(conceptResponse);
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
        ConceptSMTK conceptSMTK = this.conceptManager.getConceptByDescriptionID(descriptionId);
        ConceptResponse res = new ConceptResponse(conceptSMTK);
        this.loadRelationships(res, conceptSMTK);
        this.loadRefSets(res, conceptSMTK);
        // TODO: Atributos y Relaciones
        return res;
    }

    public ConceptResponse conceptById(String conceptId) throws NotFoundFault {
        ConceptSMTK conceptSMTK = this.conceptManager.getConceptByCONCEPT_ID(conceptId);
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

}
