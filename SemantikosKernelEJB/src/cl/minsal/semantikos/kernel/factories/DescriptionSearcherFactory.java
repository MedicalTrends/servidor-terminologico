package cl.minsal.semantikos.kernel.factories;

import cl.minsal.semantikos.kernel.daos.DescriptionDAO;
import cl.minsal.semantikos.kernel.daos.RelationshipDAO;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.relationships.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Diego Soto
 */
public class DescriptionSearcherFactory implements Callable<List<Description>> {

    String term;
    List<Category> categories;
    List<RefSet> refSets;
    private int pageNumber;
    private int pageSize;
    DescriptionDAO descriptionDAO;
    private static final Logger logger = LoggerFactory.getLogger(DescriptionSearcherFactory.class);

    public DescriptionSearcherFactory(String term, List<Category> categories, List<RefSet> refSets, int pageNumber, int pageSize, DescriptionDAO descriptionDAO) {
        this.term = term;
        this.categories = categories;
        this.refSets = refSets;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.descriptionDAO = descriptionDAO;
    }

    @Override
    public List<Description> call() throws Exception {
        return descriptionDAO.searchDescriptionsPerfectMatch(term, PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refSets), pageNumber, pageSize);
    }
}
