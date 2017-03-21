package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.SnomedCTDAO;
import cl.minsal.semantikos.model.businessrules.ConceptSearchBR;
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
    private ConceptSearchBR conceptSearchBR;

    @Override
    public SnapshotProcessingResult processSnapshot(SnomedCTSnapshot snomedCTSnapshot) {

        List<ConceptSCT> conceptSCTs = SnomedCTSnapshotFactory.getInstance().createConceptsSCTFromPath(snomedCTSnapshot.getConceptSnapshotPath());

        List<DescriptionSCT> descriptionSCTs = SnomedCTSnapshotFactory.getInstance().createDescriptionsSCTFromPath(snomedCTSnapshot.getDescriptionSnapshotPath());

        for (DescriptionSCT descriptionSCT : descriptionSCTs) {

        }

        return new SnapshotProcessingResult();
    }

    @Override
    public List<RelationshipSCT> getRelationshipsFrom(long idConceptSCT) {
        return emptyList();
    }

    @Override
    public List<ConceptSCT> findConceptsByPattern(String pattern) {
        return this.findConceptsByPattern(pattern, null);
    }

    @Override
    public List<ConceptSCT> findConceptsByPattern(String pattern, Integer group) {

        String patternStandard = conceptSearchBR.standardizationPattern(pattern);
        List<ConceptSCT> results;

        results = snomedctDAO.findPerfectMatch(patternStandard, group);

        if(results.isEmpty())
            results = snomedctDAO.findTruncateMatch(patternStandard, group);

        new ConceptSearchBR().applyPostActions(results);

        return results;
    }

    @Override
    public long countConceptByPattern(String pattern, Integer group) {
        String patternStandard = conceptSearchBR.standardizationPattern(pattern);

        long count= countPerfectMatch(patternStandard, group);

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


    @Override
    public void chargeSNAPSHOT(List<ConceptSCT> conceptSCTs, List<DescriptionSCT> descriptionSCTs, List<RelationshipSnapshotSCT> relationshipSnapshotSCTs, List<LanguageRefsetSCT> languageRefsetSCTs, List<TransitiveSCT> transitiveSCTs) {
        validateDescriptionSCT(conceptSCTs,descriptionSCTs);
    }

    private void validateDescriptionSCT(List<ConceptSCT> conceptSCTs, List<DescriptionSCT> descriptionSCTs){
        List<DescriptionSCT> descriptionSCTsWithConcept = new ArrayList<>();

        for (DescriptionSCT descriptionSCT : descriptionSCTs) {
            for (ConceptSCT conceptSCT : conceptSCTs) {
                if(descriptionSCT.getConceptId()== conceptSCT.getId()){
                    descriptionSCTsWithConcept.add(descriptionSCT);
                }
            }
        }

        if(descriptionSCTsWithConcept.size()==descriptionSCTs.size()){
            return;
        }
        List<DescriptionSCT> descriptionSCTsDeprecated= new ArrayList<>();
        for (DescriptionSCT descriptionSCT : descriptionSCTs) {
            if(!descriptionSCTsWithConcept.contains(descriptionSCT)){
                descriptionSCTsDeprecated.add(descriptionSCT);
            }
        }




    }

}
