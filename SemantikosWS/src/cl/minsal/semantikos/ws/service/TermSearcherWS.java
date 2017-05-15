package cl.minsal.semantikos.ws.service;

import cl.minsal.semantikos.modelws.response.GenericTermSearchResponse;
import cl.minsal.semantikos.ws.component.ConceptController;
import cl.minsal.semantikos.modelws.fault.NotFoundFault;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.*;
import java.util.ArrayList;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author Andrés Farías on 27-12-16.
 */
@Path(value = "/termSearch/{term}")
@Singleton
public class TermSearcherWS {

    @EJB
    ConceptController conceptController;

    @GET
    @Produces(APPLICATION_JSON)
    public GenericTermSearchResponse findTerm(@PathParam("term") String term) throws NotFoundFault {

        GenericTermSearchResponse genericTermSearchResponse;
        if (conceptController == null){
            System.out.println("conceptController es null");
            return null;
        } else {
            genericTermSearchResponse = conceptController.searchTermGeneric(term, new ArrayList<String>(), new ArrayList<String>());
        }

        return genericTermSearchResponse;
    }
}

