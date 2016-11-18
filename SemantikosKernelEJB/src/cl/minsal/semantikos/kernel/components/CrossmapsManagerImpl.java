package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.CrossmapsDAO;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.MultiplicityFactory;
import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.businessrules.CrossMapCreationBR;
import cl.minsal.semantikos.model.businessrules.CrossMapRemovalBR;
import cl.minsal.semantikos.model.crossmaps.*;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 8/30/16.
 */
@Stateless
public class CrossmapsManagerImpl implements CrossmapsManager {

    @EJB
    private AuditManager auditManager;

    @EJB
    private CrossmapsDAO crossmapsDAO;

    @EJB
    private RelationshipManager relationshipManager;

    @Override
    public Crossmap create(DirectCrossmap directCrossmap, User user) {

        /* Se aplican las reglas de negocio */
        new CrossMapCreationBR().applyRules(directCrossmap, user);

        /* Se realiza la creación a nivel de persitencia*/
        crossmapsDAO.create(directCrossmap, user);

        /* Se registra en el historial */
        if (directCrossmap.getSourceConcept().isModeled()) {
            auditManager.recordCrossMapCreation(directCrossmap, user);
        }

        /* Se retorna la instancia creada */
        return directCrossmap;
    }

    @Override
    public Crossmap remove(Crossmap crossmap, User user) {

        /* Se aplican las reglas de negocio */
        new CrossMapRemovalBR().applyRules(crossmap, user);

        /* TODO: Se realiza la eliminación */

        /* Se registra en el historial */
        if (crossmap.getSourceConcept().isModeled()) {
            auditManager.recordCrossMapRemoval(crossmap, user);
        }

        /* Se retorna la instancia creada */
        return crossmap;
    }

    @Override
    public List<Crossmap> getCrossmaps(ConceptSMTK conceptSMTK) {

        List<Crossmap> allCrossmaps = new ArrayList<>();
        allCrossmaps.addAll(this.getDirectCrossmaps(conceptSMTK));
        allCrossmaps.addAll(this.getIndirectCrossmaps(conceptSMTK));

        return allCrossmaps;
    }

    @Override
    public List<CrossmapSet> getCrossmapSets() {
        return crossmapsDAO.getCrossmapSets();
    }

    @Override
    public List<DirectCrossmap> getDirectCrossmaps(ConceptSMTK conceptSMTK) {

        ArrayList<DirectCrossmap> crossmaps = new ArrayList<>();
        // TODO
        return crossmaps;
    }

    @Override
    public CrossmapSetMember getCrossmapSetMemberById(long id) {
        return crossmapsDAO.getCrossmapSetMemberById(id);
    }

    @Override
    public List<IndirectCrossmap> getIndirectCrossmaps(ConceptSMTK conceptSMTK) {

        /* Se valida si el concepto tiene cargada sus relaciones */
        if (conceptSMTK.getRelationships().size() == 0) {
            List<Relationship> relationshipsBySourceConcept = relationshipManager.getRelationshipsBySourceConcept(conceptSMTK);
            conceptSMTK.setRelationships(relationshipsBySourceConcept);
        }

        /* Se recuperan las relaciones a Snomed CT del tipo ES_UN o ES UN MAPEO DE */
        List<CrossmapSetMember> crossmapSetMembers = new ArrayList<>();
        List<SnomedCTRelationship> relationshipsSnomedCT = conceptSMTK.getRelationshipsSnomedCT();
        for (SnomedCTRelationship snomedCTRelationship : relationshipsSnomedCT) {
            if (snomedCTRelationship.isES_UN_MAPEO_DE() || snomedCTRelationship.isES_UN()){
                crossmapSetMembers.addAll(crossmapsDAO.getRelatedCrossMapSetMembers(snomedCTRelationship.getTarget()));
            }
        }

        /* Con la lista de los crossmapSetMembers se pueden construir los Crossmaps indirectos */
        List<IndirectCrossmap> indirectCrossmaps = new ArrayList<>();
        for (CrossmapSetMember crossmapSetMember : crossmapSetMembers) {
            RelationshipDefinition relationshipDefinition = new RelationshipDefinition("Indirect Crossmap", "Un crossmap Indirecto", MultiplicityFactory.ONE_TO_ONE, crossmapSetMember.getCrossmapSet());
            indirectCrossmaps.add(new IndirectCrossmap(conceptSMTK, crossmapSetMember, relationshipDefinition, null));
        }

        return indirectCrossmaps;
    }

    @Override
    public DirectCrossmap bind(ConceptSMTK conceptSMTK, CrossmapSetMember crossmapSetMember) {
        return crossmapsDAO.bindConceptSMTKToCrossmapSetMember(conceptSMTK, crossmapSetMember);
    }

    @Override
    public List<CrossmapSetMember> findByPattern(CrossmapSet crossmapSet, String pattern) {
        return crossmapsDAO.findCrossmapSetMemberBy(crossmapSet, pattern);
    }
}
