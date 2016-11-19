package cl.minsal.semantikos.ws.component;

import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.RefSetManager;
import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.RefSet;
import cl.minsal.semantikos.ws.fault.NotFoundFault;
import cl.minsal.semantikos.ws.mapping.ConceptMapper;
import cl.minsal.semantikos.ws.mapping.RefSetMapper;
import cl.minsal.semantikos.ws.response.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Development on 2016-11-17.
 *
 */
@Stateless
public class ConceptController {

    @EJB
    private ConceptManager conceptManager;
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

    public ConceptResponse conceptByDescriptionId(String descriptionId)
        throws NotFoundFault {
        ConceptSMTK conceptSMTK = this.conceptManager.getConceptByDescriptionID(descriptionId);
        ConceptResponse res = this.getResponse(conceptSMTK);
        this.loadDescriptions(res, conceptSMTK);
        this.loadAttributes(res, conceptSMTK);
        this.loadRelationships(res, conceptSMTK);
        this.loadCategory(res, conceptSMTK);
        this.loadRefSets(res, conceptSMTK);
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
        if ( concepts != null ) {
            for (ConceptSMTK source : concepts) {
                ConceptResponse conceptResponse = this.getResponse(source);
                this.loadDescriptions(conceptResponse, source);
                this.loadAttributes(conceptResponse, source);
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
        PaginationResponse paginationResponse = this.paginationController.getResponse(pageSize, pageNumber, total);
        res.setPagination(paginationResponse);

        List<ConceptSMTK> concepts = this.conceptManager.findModeledConceptsBy(refSet, pageNumber, pageSize);
        List<ConceptResponse> conceptResponses = new ArrayList<>();
        if ( concepts != null ) {
            for (ConceptSMTK source : concepts) {
                ConceptResponse conceptResponse = this.getResponse(source);
                this.loadDescriptions(conceptResponse, source);
                this.loadAttributes(conceptResponse, source);
                this.loadCategory(conceptResponse, source);
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
        // TODO
        ConceptsByRefsetResponse res = new ConceptsByRefsetResponse();

        RefSet refSet = this.refSetManager.getRefsetByName(refSetName);
        res.setRefSet(this.refSetController.getResponse(refSet));

        Integer total = this.conceptManager.countModeledConceptsBy(refSet);
        PaginationResponse paginationResponse = this.paginationController.getResponse(pageSize, pageNumber, total);
        res.setPagination(paginationResponse);

        List<ConceptSMTK> concepts = this.conceptManager.findModeledConceptsBy(refSet, pageNumber, pageSize);
        List<ConceptResponse> conceptResponses = new ArrayList<>();
        if ( concepts != null ) {
            for (ConceptSMTK source : concepts) {
                ConceptResponse conceptResponse = this.getResponse(source);
                this.loadDescriptions(conceptResponse, source);
                conceptResponses.add(conceptResponse);
            }
        }
        res.setConcepts(conceptResponses);

        return res;
    }

    public ConceptResponse getResponse(ConceptSMTK conceptSMTK) throws NotFoundFault {
        if ( conceptSMTK == null ) {
            throw new NotFoundFault("Concepto no encontrado");
        }
        return ConceptMapper.map(conceptSMTK);
    }

    public ConceptResponse loadDescriptions(ConceptResponse conceptResponse, ConceptSMTK source) {
        if ( conceptResponse.getDescriptions() == null || conceptResponse.getDescriptions().isEmpty() ) {
            if ( source.getDescriptions() == null || source.getDescriptions().isEmpty() ) {
                // TODO: Load descriptions
            }
            ConceptMapper.appendDescriptions(conceptResponse, source);
        }
        return conceptResponse;
    }

    public ConceptResponse loadAttributes(ConceptResponse conceptResponse, ConceptSMTK source) {
        if ( conceptResponse.getAttributes() == null || conceptResponse.getAttributes().isEmpty() ) {
            if ( !source.isRelationshipsLoaded() ) {
                conceptManager.loadRelationships(source);
            }
            ConceptMapper.appendAttributes(conceptResponse, source);
        }
        return conceptResponse;
    }

    public ConceptResponse loadRelationships(ConceptResponse conceptResponse, ConceptSMTK source) {
        if ( conceptResponse.getRelationships() == null || conceptResponse.getRelationships().isEmpty() ) {
            if ( !source.isRelationshipsLoaded() ) {
                conceptManager.loadRelationships(source);
            }
            ConceptMapper.appendRelationships(conceptResponse, source);
        }
        return conceptResponse;
    }

    public ConceptResponse loadCategory(ConceptResponse conceptResponse, ConceptSMTK source) {
        if ( conceptResponse.getCategory() == null ) {
            if ( source.getCategory() == null ) {
                // TODO: Load category
            }
            ConceptMapper.appendCategory(conceptResponse, source);
        }
        return conceptResponse;
    }

    public ConceptResponse loadRefSets(ConceptResponse conceptResponse, ConceptSMTK source) {
        if ( conceptResponse.getRefsets() == null || conceptResponse.getRefsets().isEmpty() ) {
            if ( source.getRefsets() == null || source.getRefsets().isEmpty() ) {
                refSetManager.loadConceptRefSets(source);
            }
            ConceptMapper.appendRefSets(conceptResponse, source);
        }
        return conceptResponse;
    }

}
