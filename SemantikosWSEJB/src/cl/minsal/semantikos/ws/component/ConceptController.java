package cl.minsal.semantikos.ws.component;

import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.RefSetManager;
import cl.minsal.semantikos.kernel.daos.DescriptionDAO;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.ws.fault.NotFoundFault;
import cl.minsal.semantikos.ws.mapping.ConceptMapper;
import cl.minsal.semantikos.ws.response.ConceptResponse;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Created by Development on 2016-11-17.
 *
 */
@Stateless
public class ConceptController {

    @EJB
    private CategoryManager categoryManager;
    @EJB
    private DescriptionDAO descriptionDAO;
    @EJB
    private ConceptManager conceptManager;
    @EJB
    private RefSetManager refSetManager;

    public ConceptResponse getByDescriptionId(String descriptionId) throws NotFoundFault {
        // TODO Relaciones y atributos
        Description description = null;
        try {
            description = this.descriptionDAO.getDescriptionBy(descriptionId);
        } catch (Exception ignored) { }

        if ( description == null ) {
            throw new NotFoundFault("Descripcion no encontrada");
        }

        ConceptSMTK conceptSMTK = description.getConceptSMTK();
        if ( conceptSMTK == null ) {
            throw new NotFoundFault("Concepto no encontrado");
        }

        ConceptResponse res = ConceptMapper.map(conceptSMTK);
        this.loadDescriptions(res, conceptSMTK);
        this.loadAttributes(res, conceptSMTK);
        this.loadRelationships(res, conceptSMTK);
        this.loadCategory(res, conceptSMTK);
        this.loadRefSets(res, conceptSMTK);
        return res;
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
