package cl.minsal.semantikos.ws.service;




import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.categories.Category;

import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.modelweb.Pair;
import cl.minsal.semantikos.modelws.fault.NotFoundFault;
import cl.minsal.semantikos.modelws.request.*;
import cl.minsal.semantikos.modelws.response.*;
import cl.minsal.semantikos.ws.component.CategoryController;
import cl.minsal.semantikos.ws.component.ConceptController;
import cl.minsal.semantikos.ws.component.CrossmapController;
import cl.minsal.semantikos.ws.component.RefSetController;
import cl.minsal.semantikos.modelws.fault.IllegalInputFault;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cl.minsal.semantikos.ws.utils.UtilsWS;

import javax.annotation.Resource;
import javax.ejb.EJB;


import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.*;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Created by Development on 2016-11-18.
 */


@WebService(serviceName = "ServicioDeBusqueda")
public class SearchService {

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

    @Resource
    WebServiceContext wsctx;

    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    /**
     * Metodo de envoltura de los web methods
     * //@param ctx Contexto de invocacion
     * @throws Exception
     */

    @AroundInvoke
    protected Object webMethodWrapper(InvocationContext ctx) throws Exception {

        Request request = (Request) ctx.getParameters()[0];
        String webMethodStatus = "0";
        String webMethodMessage = "OK";

        try {
            Pair credentials = UtilsWS.getCredentials(wsctx.getMessageContext());
            authenticationManager.authenticateWS(credentials.getFirst().toString(), credentials.getSecond().toString());
            authenticationManager.validateInstitution(request.getIdStablishment());

            return ctx.proceed();
        }
        catch (Exception e) {
            logger.error("El web service ha arrojado el siguiente error: "+e.getMessage(),e);
            throw new NotFoundFault(e.getMessage());
            //return ctx.proceed();
            //webMethodStatus = "1";
            //webMethodMessage = e.getMessage();
        }
        finally {
            HttpServletResponse response = (HttpServletResponse) wsctx.getMessageContext().get(MessageContext.SERVLET_RESPONSE);
            response.setStatus(HttpServletResponse.SC_OK);
            //response.addHeader("web-method-status",webMethodStatus);
            //response.addHeader("web-method-message",webMethodMessage);
        }

        //return null;
    }

    // REQ-WS-001
    @WebResult(name = "respuestaBuscarTermino")
    @WebMethod(operationName = "buscarTerminoPerfectMatch")
    public GenericTermSearchResponse buscarTermino( //GenericTermSearchResponse buscarTermino(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionBuscarTermino")
                    SimpleSearchTermRequest request
    ) throws IllegalInputFault, NotFoundFault, ExecutionException, InterruptedException {

        /* Se hace una validación de los parámetros */
        UtilsWS.validateAtLeastOneCategoryOrOneRefSet(request);

        logger.debug("ws-req-001: " + request.getTerm() + ", " + request.getCategoryNames() + " " + request
                .getRefSetNames());

        GenericTermSearchResponse response = this.conceptController.searchTermGeneric(request.getTerm(), request.getCategoryNames(), request.getRefSetNames());

        return  response;

    }

    // REQ-WS-002
    /*
    @WebResult(name = "respuestaConceptos")
    @WebMethod(operationName = "conceptosPorCategoria")
    public ConceptsResponse conceptosPorCategoria(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionConceptosPorCategoria")
                    CategoryRequest request
    ) throws NotFoundFault {
        try {
            return this.conceptController.findConceptsByCategory(request.getCategoryName(), request.getIdStablishment());
        } catch (Exception e) {
            throw new NotFoundFault(e.getMessage());
        }

        return response;
    }
    */

    // REQ-WS-002 Paginados
    @WebResult(name = "respuestaConceptosPorCategoriaPaginados")
    @WebMethod(operationName = "conceptosPorCategoriaPaginados")
    public ConceptsResponse conceptosPorCategoriaPaginados(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionConceptosPorCategoriaPaginados")
                    CategoryRequestPaginated request
    ) throws NotFoundFault {

        return this.conceptController.findConceptsByCategoryPaginated(request.getCategoryName(), request.getIdStablishment(),
                                                                          request.getPageNumber(), request.getPageSize());
    }

    @WebResult(name = "respuestaCategorias")
    @WebMethod(operationName = "listaCategorias")
    public CategoriesResponse listaCategorias(){

        CategoriesResponse response = new CategoriesResponse();
        logger.debug("Se invocado el servicio listaCategorias().");
        try {
            //response = this.categoryController.categoryList();
            response = new CategoriesResponse(CategoryFactory.getInstance().getCategories());
            synchronized(this) {
                wait(500);
            }
            logger.debug("El servicio listaCategorias() a retornado " + response.getCategoryResponses().size()
                    + " categorias Responses.");
        }catch(Exception e){
            logger.error(e.getMessage(), e);

        }
        return response;
    }

    // REQ-WS-004
    @WebResult(name = "respuestaBuscarTermino")
    @WebMethod(operationName = "buscarTerminoTruncatePerfect")
    public GenericTermSearchResponse buscarTruncatePerfect(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionBuscarTermino")
                    SimpleSearchTermRequest request
    ) throws IllegalInputFault, NotFoundFault {

        /* Se hace una validación de los parámetros */
        UtilsWS.validateAtLeastOneCategoryOrOneRefSet(request);

        return this.conceptController.searchTermGeneric2(request.getTerm(), request.getCategoryNames(), request
                .getRefSetNames());
    }

    /**
     * REQ-WS-005: El sistema Semantikos debe disponer un servicio que busque todos los términos que pertenezcan a un
     * concepto que contenga el atributo “Pedible”, esto solo aplica para las categorías de interconsulta, indicaciones
     * generales e indicaciones de laboratorio.
     *
     * @return Los términos solicitados
     * @throws cl.minsal.semantikos.modelws.fault.IllegalInputFault
     */
    @WebResult(name = "respuestaObtenerTerminosPedibles")
    @WebMethod(operationName = "obtenerTerminosPedibles")
    public TermSearchesResponse obtenerTerminosPedibles(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionObtenerTerminosPedibles")
                    RequestableConceptsRequest request
    ) throws IllegalInputFault, NotFoundFault {

        /* Se hace una validación de los parámetros */
        obtenerTerminosPediblesParamValidation(request);

        return conceptController.searchRequestableDescriptions(request.getCategoryNames(), request.getRequestable());
    }

    // REQ-WS-006
    @WebResult(name = "respuestaSugerenciasDeDescripciones")
    @WebMethod(operationName = "sugerenciasDeDescripciones")
    public SuggestedDescriptionsResponse sugerenciasDeDescripciones(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionSugerenciasDeDescripciones")
                    DescriptionsSuggestionsRequest request
    ) throws IllegalInputFault, NotFoundFault {

        UtilsWS.validateAtLeastOneCategory(request);

        if ( request.getTerm().length() < 3 ) {
            throw new IllegalInputFault("El termino a buscar debe tener minimo 3 caracteres de largo");
        }

        return this.conceptController.searchSuggestedDescriptions(request.getTerm(), request.getCategoryNames());
        //return this.conceptController.searchTruncatePerfect(request.getTerm(), request.getCategoryNames());
    }

    /**
     * Este método es responsable de realizar la validación de los parámetros de entrada del servicio REQ-WS-005.
     *
     * @param request La petición con los parámetros de entrada.
     * @throws cl.minsal.semantikos.modelws.fault.IllegalInputFault Arrojado si se solicitan cateogorías distintas a las
     * objetivo de la búsqueda o que
     *                                                         simplemente no existen. También se arroja si existen
     */
    private void obtenerTerminosPediblesParamValidation(RequestableConceptsRequest request) throws IllegalInputFault {

        /* Se valida que haya al menos 1 categoría o 1 refset */
        UtilsWS.validateAtLeastOneCategory(request);

        /* Luego es necesario validar que si hay categorías especificadas, se limiten a "interconsulta",
        "indicaciones generales" e "indicaciones de laboratorio" */
        if (request.getCategoryNames().size() > 0) {

            Category interconsultas = categoryManager.getCategoryByName("Interconsultas");
            Category indicacionesGenerales = categoryManager.getCategoryByName("Indicaciones Generales");
            Category indicacionesLaboratio = categoryManager.getCategoryByName("Indicaciones de Laboratorio");
            List<String> categories = Arrays.asList(interconsultas.getName().toLowerCase(), indicacionesGenerales
                    .getName().toLowerCase(), indicacionesLaboratio.getName().toLowerCase());

            for (String category : request.getCategoryNames()) {
                if (!categories.contains(category.toLowerCase())) {
                    throw new IllegalInputFault("La categoría " + category + " no es una categoría aceptable de " +
                            "búsqueda");
                }
            }
        }
    }


    // REQ-WS-007
    // REQ-WS-009
    @WebResult(name = "respuestaRefSetsPorIdDescripcion")
    @WebMethod(operationName = "refSetsPorIdDescripcion")
    public List<RefSetSearchResponse> refSetsPorIdDescripcion(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionRefSetsPorIdDescripcion")
                    RefSetsByDescriptionIdRequest request
    ) throws NotFoundFault, IllegalInputFault {

            List<String> descriptionIds = request.getDescriptionId();
            Boolean includeInstitutions = request.getIncludeInstitutions();
            String idStablishment = request.getIdStablishment();

            if (descriptionIds == null || descriptionIds.isEmpty()) {
                throw new IllegalInputFault("Debe ingresar por lo menos un idDescripcion");
            }

        return this.refSetController.findRefSetsByDescriptions(descriptionIds, includeInstitutions, idStablishment);

    }

    // REQ-WS-008
    @WebResult(name = "respuestaListaRefSets")
    @WebMethod(operationName = "listaRefSets")
    public RefSetsResponse listaRefSet(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionListaRefSets")
                    RefSetsRequest request
    ) throws NotFoundFault {
        return this.refSetController.refSetList(request.getIncludeInstitutions(), request.getIdStablishment());
    }

    // REQ-WS-022
    @WebResult(name = "respuestaDescripcionesPreferidasPorRefSet")
    @WebMethod(operationName = "descripcionesPreferidasPorRefSet")
    public RefSetLightResponse descripcionesPreferidasPorRefSet(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionDescripcionesPreferidasPorRefSet")
                    ConceptsByRefsetRequest request
    ) throws NotFoundFault {
        return this.conceptController.conceptsLightByRefset(request.getRefSetName());
    }

    // REQ-WS-023
    @WebResult(name = "respuestaConceptosPorRefSet")
    @WebMethod(operationName = "conceptosPorRefSet")
    public RefSetResponse conceptosPorRefSet(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionConceptosPorRefSet")
                    ConceptsByRefsetRequest request
    ) throws NotFoundFault {
        return this.conceptController.conceptsByRefset(request.getRefSetName());
    }

    /**
     * REQ-WS-024: El sistema Semantikos debe disponer un servicio que permita obtener una lista de todos los
     * CrossMapsets (sets/vocabularios Mapeos) Ej.: CIE9; CIE10; CIEO.
     *
     * @param request El objeto base para el request
     * @return Una lista de los crossmapSets existentes.
     */
    @WebResult(name = "respuestaListaCrossmapSets")
    @WebMethod(operationName = "listaCrossmapSets")
    public CrossmapSetsResponse getCrossmapSets(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionListaCrossmapSets")
                    Request request
    ) throws NotFoundFault {
        return this.crossmapsController.getCrossmapSets(request.getIdStablishment());
    }

    /**
     * REQ-WS-025: El sistema Semantikos debe disponer un servicio que permita obtener los CrossMapsetsMembers a partir
     * de un CrossMapset.
     *
     * @param request La peticion de crossmapSetMembers
     * @return Una lista de los crossmapSetMembers del crossmapSet dado como parámetro.
     */
    @WebResult(name = "respuestaCrosmapSetMembersDeCrossmapSet")
    @WebMethod(operationName = "crossmapSetMembersDeCrossmapSet")
    public CrossmapSetMembersResponse crossmapSetMembersDeCrossmapSet(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionCrosmapSetMembersDeCrossmapSet")
                    CrossmapSetMembersRequest request
    ) throws NotFoundFault {
        return this.crossmapsController.getCrossmapSetMembersByCrossmapSetAbbreviatedName(request.getCrossmapSetAbbreviatedName(),
                request.getPageNumber(), request.getPageSize());
    }

    /**
     * REQ-WS-026: El sistema Semantikos debe disponer un servicio que permita obtener los CrossMap indirecto a partir
     * de un Descripción ID o de un CONCEPT ID.
     *
     * @param descripcionIDorConceptIDRequest El valor de negocio <em>DESCRIPTION_ID</em> de la descripción cuyo
     *                                        concepto posee los
     *                                        crossmaps indirectos que se desea recuperar.
     * @return Una lista de crossmaps <em>indirectos</em> del concepto asociado a la descripción encapsulada en un
     * objeto mapeado
     * a un elemento XML.
     * @throws cl.minsal.semantikos.modelws.fault.NotFoundFault Arrojada si no existe una descripción con
     * <em>DESCRIPTION_ID</em> igual al indicado por el
     *                                                     parámetro <code>descriptionId</code>.
     */
    @WebResult(name = "respuestaObtenerCrossmapsIndirectos")
    @WebMethod(operationName = "obtenerCrossmapsIndirectos")
    public IndirectCrossMapSearchResponse crossMapsIndirectosPorDescriptionIDoConceptID(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionObtenerCrossmaps")
                    DescriptionIDorConceptIDRequest descripcionIDorConceptIDRequest
    ) throws NotFoundFault {
        try {
            return this.crossmapsController.getIndirectCrossmapsByDescriptionID(descripcionIDorConceptIDRequest);
        } catch (Exception e) {
            throw new NotFoundFault(e.getMessage());
        }
    }

    /**
     * REQ-WS-027: El sistema Semantikos debe disponer un servicio que permita obtener los CrossMap directo a partir de
     * un ID Descripción.
     *
     * @param request El valor de negocio <em>DESCRIPTION_ID</em> de la descripción cuyo concepto posee los
     *                      crossmaps indirectos que se desea recuperar o del <em>CONCEPT ID</em>.
     * @return Una lista de crossmaps <em>directos</em> del concepto asociado a la descripción encapsulada en un objeto
     * mapeado
     * a un elemento XML.
     * @throws cl.minsal.semantikos.modelws.fault.NotFoundFault Arrojada si no existe una descripción con
     * <em>DESCRIPTION_ID</em> igual al indicado por el
     *                                                     parámetro <code>descriptionId</code>.
     */
    @WebResult(name = "respuestaObtenerCrossmapsDirectos")
    @WebMethod(operationName = "obtenerCrossmapsDirectos")
    public CrossmapSetMembersResponse crossmapSetMembersDirectosPorIDDescripcion(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionObtenerCrossmaps")
                    DescriptionIDorConceptIDRequest request
    ) throws NotFoundFault {
        return this.crossmapsController.getDirectCrossmapsSetMembersByDescriptionID(request);
    }

    // REQ-WS-028
    @WebResult(name = "respuestaConceptoPorIdDescripcion")
    @WebMethod(operationName = "conceptoPorIdDescripcion")
    public ConceptResponse conceptoPorIdDescripcion(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionConceptoPorIdDescripcion")
                    ConceptByDescriptionIDRequest request
    ) throws NotFoundFault {
        try {
            return this.conceptController.conceptByDescriptionId(request.getDescriptionID());
        } catch (Exception e) {
            throw new NotFoundFault(e.getMessage());
        }
    }

    // REQ-WS-???
    @WebResult(name = "respuestaConceptoPorConceptID")
    @WebMethod(operationName = "conceptoPorConceptID")
    public ConceptResponse conceptoPorConceptID(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionConceptoPorIdDescripcion")
                    ConceptByConceptIDRequest request
    ) throws NotFoundFault {
        try {
            return this.conceptController.conceptByConceptID(request.getConceptID());
        } catch (Exception e) {
            throw new NotFoundFault(e.getMessage());
        }
    }

    // REQ-WS-???
    @WebResult(name = "respuestaGTINPorConceptID")
    @WebMethod(operationName = "GTINPorConceptID")
    public GTINByConceptIDResponse GTINPorConceptID(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionGTINPorConceptID")
                    GTINByConceptIDRequest request
    ) throws NotFoundFault {
        try {
            return this.conceptController.searchGTINByConceptID(request);
        } catch (Exception e) {
            throw new NotFoundFault(e.getMessage());
        }
    }

    // REQ-WS-???
    @WebResult(name = "respuestaConceptIDPorGTIN")
    @WebMethod(operationName = "conceptIDPorGTIN")
    public ConceptIDByGTINResponse conceptIDPorGTIN(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionConceptIDPorGTIN")
                    ConceptIDByGTINRequest request
    ) throws NotFoundFault {
        try {
            return this.conceptController.searchConceptIDByGTIN(request);
        } catch (Exception e) {
            throw new NotFoundFault(e.getMessage());
        }
    }

}
