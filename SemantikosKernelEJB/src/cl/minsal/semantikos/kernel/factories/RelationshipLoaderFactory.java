package cl.minsal.semantikos.kernel.factories;

import cl.minsal.semantikos.kernel.components.RelationshipManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.relationships.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.mail.*;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Diego Soto
 */
public class RelationshipLoaderFactory implements Callable<List<Relationship>> {

    private ConceptSMTK conceptSMTK;
    RelationshipManager relationshipManager;
    private static final Logger logger = LoggerFactory.getLogger(RelationshipLoaderFactory.class);

    public RelationshipLoaderFactory(ConceptSMTK conceptSMTK, RelationshipManager relationshipManager) {
        this.conceptSMTK = conceptSMTK;
        this.relationshipManager = relationshipManager;
    }

    @Override
    public List<Relationship> call() throws Exception {
        return relationshipManager.getRelationshipsBySourceConcept(conceptSMTK);
    }
}
