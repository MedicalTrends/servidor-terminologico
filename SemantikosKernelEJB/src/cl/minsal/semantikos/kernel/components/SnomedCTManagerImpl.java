package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.SnomedCTDAO;
import cl.minsal.semantikos.model.businessrules.ConceptSearchBR;
import cl.minsal.semantikos.model.snomedct.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.ArrayList;
import java.util.HashMap;
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

        if (results.isEmpty())
            results = snomedctDAO.findTruncateMatch(patternStandard, group);

        new ConceptSearchBR().applyPostActions(results);

        return results;
    }

    @Override
    public long countConceptByPattern(String pattern, Integer group) {
        String patternStandard = conceptSearchBR.standardizationPattern(pattern);

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


    @Override
    public void chargeSNAPSHOT(List<ConceptSCT> conceptSCTs, List<DescriptionSCT> descriptionSCTs, List<RelationshipSnapshotSCT> relationshipSnapshotSCTs, List<LanguageRefsetSCT> languageRefsetSCTs, List<TransitiveSCT> transitiveSCTs) {


        /*for (ConceptSCT conceptSCT : conceptSCTs) {
            snomedctDAO.persistSnapshotConceptSCT(conceptSCT);
            break;
        }

        for (DescriptionSCT descriptionSCT : descriptionSCTs) {
            snomedctDAO.persistSnapshotDescriptionSCT(descriptionSCT);
        }
        for (RelationshipSnapshotSCT relationshipSnapshotSCT : relationshipSnapshotSCTs) {
            snomedctDAO.persistSnapshotRelationshipSCT(relationshipSnapshotSCT);
        }
        for (LanguageRefsetSCT languageRefsetSCT : languageRefsetSCTs) {
            snomedctDAO.persistSnapshotLanguageRefSetSCT(languageRefsetSCT);
        }
        for (TransitiveSCT transitiveSCT : transitiveSCTs) {
            snomedctDAO.persistSnapshotTransitiveSCT(transitiveSCT);
        }*/

    }

    @Override
    public void persistConceptSCT(List<ConceptSCT> conceptSCTs) {
        long time_start, time_end;
        time_start = System.currentTimeMillis();
        /*for (ConceptSCT conceptSCT : conceptSCTs) {
            if (snomedctDAO.existConceptSCT(conceptSCT)) {

            } else {

            }
        }*/

        time_end = System.currentTimeMillis();
        System.out.println("Revision " + ((time_end - time_start) / 1000) + " seconds");


        time_start = System.currentTimeMillis();
        snomedctDAO.persistSnapshotConceptSCT(conceptSCTs);
        time_end = System.currentTimeMillis();
        System.out.println("Persistir " + ((time_end - time_start) / 1000) + " seconds");


    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void persistSnapshotDescriptionSCT(List<DescriptionSCT> descriptionSCTs) {
        List<DescriptionSCT> forUpdate = new ArrayList<>();
        List<DescriptionSCT> forPersist = new ArrayList<>();
        for (DescriptionSCT descriptionSCT : descriptionSCTs) {
            if (!snomedctDAO.existDescriptionSCT(descriptionSCT)) {
                forPersist.add(descriptionSCT);
            } else {
                forUpdate.add(descriptionSCT);
            }
        }

        Map<Integer, List<DescriptionSCT>> transactionBlocksPersist = transactionBlocks(forPersist);
        Map<Integer, List<DescriptionSCT>> transactionBlocksUpdate = transactionBlocks(forUpdate);

        int i = 1;
        while (transactionBlocksPersist.get(i) != null) {
            persistDescriptionsDAO(transactionBlocksPersist.get(i));
            i++;
        }
        i = 1;
        while (transactionBlocksUpdate.get(i) != null) {
            //snomedctDAO.updateSnapshotDescriptionSCT(transactionBlocksUpdate.get(i));
            i++;
        }
    }


    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void persistDescriptionsDAO(List<DescriptionSCT> descriptionSCTs) {
        snomedctDAO.persistSnapshotDescriptionSCT(descriptionSCTs);
    }




    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void persistSnapshotRelationshipSCT(List<RelationshipSnapshotSCT> relationshipSnapshotSCT) {


        //List<RelationshipSnapshotSCT> forUpdate = new ArrayList<>();
        //List<RelationshipSnapshotSCT> forPersist = new ArrayList<>();


        Map<Integer, List<RelationshipSnapshotSCT>> transactionBlocksPersist = transactionBlocksRelationship(relationshipSnapshotSCT);
        //Map<Integer, List<DescriptionSCT>> transactionBlocksUpdate = transactionBlocks(forUpdate);
        long time_start, time_end;
        time_start = System.currentTimeMillis();
        int i = 1;
        while (transactionBlocksPersist.get(i) != null) {
            persistRelationshipDAO(transactionBlocksPersist.get(i));
            i++;
        }
        /*i = 1;
        while (transactionBlocksUpdate.get(i) != null) {
            //snomedctDAO.updateSnapshotDescriptionSCT(transactionBlocksUpdate.get(i));
            i++;
        }*/

        time_end = System.currentTimeMillis();
        System.out.println("the task has taken " + ((time_end - time_start) / 1000) + " seconds");


    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void persistRelationshipDAO(List<RelationshipSnapshotSCT> relationshipSnapshotSCTs) {
        snomedctDAO.persistSnapshotRelationshipSCT(relationshipSnapshotSCTs);
    }

    @Override
    public void persistSnapshotTransitiveSCT(TransitiveSCT transitiveSCT) {
        snomedctDAO.persistSnapshotTransitiveSCT(transitiveSCT);
    }

    @Override
    public void persistSnapshotLanguageRefSetSCT(LanguageRefsetSCT languageRefsetSCT) {
        snomedctDAO.persistSnapshotLanguageRefSetSCT(languageRefsetSCT);
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

    private Map<Integer, List<DescriptionSCT>> transactionBlocks(List<DescriptionSCT> list) {

        Map<Integer, List<DescriptionSCT>> transactionBlocks = new HashMap<>();
        List<DescriptionSCT> listBlock = null;
        boolean blockSuccesful = false;
        int i = 1;

        while (!blockSuccesful) {

            if (list.size() >= (500000 * i)) {
                listBlock = list.subList((500000 * (i - 1)), (500000 * i));
                transactionBlocks.put(i, listBlock);
            } else {
                listBlock = list.subList((500000 * (i - 1)), list.size());
                transactionBlocks.put(i, listBlock);
                blockSuccesful = true;
            }
            i++;
        }

        return transactionBlocks;
    }

    private Map<Integer, List<RelationshipSnapshotSCT>> transactionBlocksRelationship(List<RelationshipSnapshotSCT> list) {

        Map<Integer, List<RelationshipSnapshotSCT>> transactionBlocks = new HashMap<>();
        List<RelationshipSnapshotSCT> listBlock = null;
        boolean blockSuccesful = false;
        int i = 1;

        while (!blockSuccesful) {

            if (list.size() >= (500000 * i)) {
                listBlock = list.subList((500000 * (i - 1)), (500000 * i));
                transactionBlocks.put(i, listBlock);
            } else {
                listBlock = list.subList((500000 * (i - 1)), list.size());
                transactionBlocks.put(i, listBlock);
                blockSuccesful = true;

            }
            i++;
        }

        return transactionBlocks;
    }

}
