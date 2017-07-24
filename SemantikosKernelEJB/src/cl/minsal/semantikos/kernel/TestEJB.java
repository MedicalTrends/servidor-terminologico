package cl.minsal.semantikos.kernel;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

/**
 * Created by BluePrints Developer on 12-07-2016.
 */

@Stateless
public class TestEJB {

    @PersistenceContext(unitName = "SEMANTIKOS_PU")
    EntityManager em;

    public String getTest(){

        Query q = em.createNativeQuery("call semantikos.get_concept_count()");


        return ""+q.getFirstResult();

    }

}
