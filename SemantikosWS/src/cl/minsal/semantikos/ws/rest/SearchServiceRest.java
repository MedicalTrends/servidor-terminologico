package cl.minsal.semantikos.ws.rest;



import cl.minsal.semantikos.kernel.components.AuthenticationManager;
import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.modelweb.Pair;
import cl.minsal.semantikos.modelws.fault.IllegalInputFault;
import cl.minsal.semantikos.modelws.fault.NotFoundFault;
import cl.minsal.semantikos.modelws.request.*;
import cl.minsal.semantikos.modelws.response.*;
import cl.minsal.semantikos.ws.component.CategoryController;
import cl.minsal.semantikos.ws.component.ConceptController;
import cl.minsal.semantikos.ws.component.CrossmapController;
import cl.minsal.semantikos.ws.component.RefSetController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cl.minsal.semantikos.ws.utils.UtilsWS;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.Collections.EMPTY_LIST;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.apache.commons.lang.ArrayUtils.EMPTY_LONG_ARRAY;


/**
 * Created by Development on 2016-11-18.
 */


//@WebService(serviceName = "ServicioDeBusqueda")
@Path("searchservice")
@Stateless
public class SearchServiceRest {

    @EJB
    private CrossmapController crossmapsController;

    @EJB
    private ConceptController conceptController;

    @EJB
    private CategoryController categoryController;

    @EJB
    private RefSetController refSetController;

    @EJB
    private CategoryManager categoryManager;

    @EJB
    private AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceRest.class);

    @GET
    @Path("helloworld")
    public String helloworld() {
        return "Hello World!";
    }

    // REQ-WS-001
    @GET
    @Path("/perfectmatch")
    @Produces(APPLICATION_JSON)
    //@WebResult(name = "respuestaBuscarTermino")
    //@WebMethod(operationName = "buscarTerminoPerfectMatch")
    public GenericTermSearchResponse buscarTermino( //GenericTermSearchResponse buscarTermino(
            @QueryParam("pattern") String pattern,
            @QueryParam("categories") List<String> categories,
            @QueryParam("refsets") List<String> refsets
    ) throws IllegalInputFault, NotFoundFault, ExecutionException, InterruptedException {

        GenericTermSearchResponse response = this.conceptController.searchTermGeneric(pattern, Arrays.asList(new String[] {"Problemas"}), EMPTY_LIST);

        return  response;

    }


}
