package cl.minsal.semantikos.kernel.factories;

import cl.minsal.semantikos.kernel.daos.CrossmapsDAO;
import cl.minsal.semantikos.model.crossmaps.CrossmapSet;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.crossmaps.DirectCrossmap;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.Target;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Gustavo Punucura on 18-11-16.
 */
@Singleton
public class CrossmapFactory {

    /**
     * El logger para esta clase
     */
    private static final Logger logger = LoggerFactory.getLogger(CrossmapFactory.class);

    @EJB
    private CrossmapsDAO crossmapsDAO;


    public DirectCrossmap createDirectCrossmap(Relationship relationship) {

        /* Se transforma el target a un CrossmapSetMember */
        Target target = relationship.getTarget();
        if (!(target instanceof CrossmapSetMember)){
            throw new IllegalArgumentException("La relación no es de tipo DirectCrossmap.");
        }
        CrossmapSetMember crossmapSetMember = (CrossmapSetMember) target;

        /* Se retorna una instancia fresca */
        return new DirectCrossmap(relationship.getId(), relationship.getSourceConcept(), crossmapSetMember, relationship.getRelationshipDefinition(), relationship.getValidityUntil());
    }
}
