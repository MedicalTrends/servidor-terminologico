package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.SnomedCTDAO;
import cl.minsal.semantikos.model.businessrules.ConceptSCTSearchBR;
import cl.minsal.semantikos.model.snomedct.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

/**
 * @author Andrés Farías on 9/26/16.
 */
@Stateless
public class SnomedCTManagerImpl implements SnomedCTManager {

    @EJB
    private SnomedCTDAO snomedctDAO;

    @EJB
    private ConceptSCTSearchBR conceptSCTSearchBR;

    @Override
    public List<RelationshipSCT> getRelationshipsFrom(ConceptSCT conceptSCT) {
        return snomedctDAO.getRelationshipsBySourceConcept(conceptSCT);
    }

    @Override
    public List<ConceptSCT> findConceptsByPattern(String pattern) {
        return this.findConceptsByPattern(pattern, null, 0, 100);
    }

    @Override
    public List<ConceptSCT> findConceptsByPattern(String pattern, Integer group, int page, int pageSize) {

        String patternStandard = conceptSCTSearchBR.standardizationPattern(pattern);
        List<ConceptSCT> results;

        results = snomedctDAO.findPerfectMatch(patternStandard, group, page, pageSize);

        if (results.isEmpty()) {
            results = snomedctDAO.findTruncateMatch(patternStandard, group, page, pageSize);
        }

        new ConceptSCTSearchBR().applyPostActions(results);

        return results;
    }

    @Override
    public long countConceptByPattern(String pattern, Integer group) {
        String patternStandard = conceptSCTSearchBR.standardizationPattern(pattern);

        long count = countPerfectMatch(patternStandard, group);

        if (count != 0) {
            return count;
        } else {
            return countTruncateMatch(patternStandard, group);
        }
    }

    public long countPerfectMatch(String pattern, Integer group) {
        return snomedctDAO.countPerfectMatch(pattern, group);
    }

    public long countTruncateMatch(String pattern, Integer group) {
        return snomedctDAO.countTruncateMatch(pattern, group);
    }

    @Override
    public List<ConceptSCT> findConceptsByConceptID(long conceptIdPattern) {
        return this.findConceptsByConceptID(conceptIdPattern, null);
    }

    @Override
    public List<ConceptSCT> findConceptsByConceptID(long conceptIdPattern, Integer group) {
        return snomedctDAO.findConceptsByConceptID(conceptIdPattern, group);
    }

    @Override
    public Map<DescriptionSCT, ConceptSCT> findDescriptionsByPattern(String patternID) {
        return snomedctDAO.findDescriptionsByPattern(patternID);
    }

    @Override
    public ConceptSCT getConceptByID(long conceptID) {
        return snomedctDAO.getConceptByID(conceptID);
    }


    private void validateDescriptionSCT(List<ConceptSCT> conceptSCTs, List<DescriptionSCT> descriptionSCTs) {
        List<DescriptionSCT> descriptionSCTsWithConcept = new ArrayList<>();
        List<Long> idsConcept = new ArrayList<>();
        for (ConceptSCT conceptSCT : conceptSCTs) {
            idsConcept.add(conceptSCT.getIdSnomedCT());
        }

        for (DescriptionSCT descriptionSCT : descriptionSCTs) {

            if (idsConcept.contains(descriptionSCT.getConceptId())) {
                descriptionSCTsWithConcept.add(descriptionSCT);

            }
        }

        if (descriptionSCTsWithConcept.size() == descriptionSCTs.size()) {
            return;
        }
        List<DescriptionSCT> descriptionSCTsDeprecated = new ArrayList<>();
        for (DescriptionSCT descriptionSCT : descriptionSCTs) {
            if (!descriptionSCTsWithConcept.contains(descriptionSCT)) {
                descriptionSCTsDeprecated.add(descriptionSCT);
            }
        }


    }

    private void validateRelationshipSCT(List<ConceptSCT> conceptSCTs, List<RelationshipSnapshotSCT> relationshipSnapshotSCTs) {
        List<RelationshipSnapshotSCT> relationshipSnapshotSCTs1 = new ArrayList<>();
        List<Long> idsConcept = new ArrayList<>();
        for (ConceptSCT conceptSCT : conceptSCTs) {
            idsConcept.add(conceptSCT.getIdSnomedCT());
        }

        for (RelationshipSnapshotSCT relationshipSnapshotSCT : relationshipSnapshotSCTs) {

            if (idsConcept.contains(relationshipSnapshotSCT.getSourceId()) && idsConcept.contains(relationshipSnapshotSCT.getDestinationId())) {
                relationshipSnapshotSCTs1.add(relationshipSnapshotSCT);
            }
        }

        for (ConceptSCT conceptSCT : conceptSCTs) {

        }


    }


}
