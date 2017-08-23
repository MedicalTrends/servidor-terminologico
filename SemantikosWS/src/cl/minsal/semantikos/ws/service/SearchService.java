package cl.minsal.semantikos.ws.service;




import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.categories.Category;

import cl.minsal.semantikos.modelweb.Pair;
import cl.minsal.semantikos.modelws.request.*;
import cl.minsal.semantikos.modelws.response.*;
import cl.minsal.semantikos.ws.component.CategoryController;
import cl.minsal.semantikos.ws.component.ConceptController;
import cl.minsal.semantikos.ws.component.CrossmapController;
import cl.minsal.semantikos.ws.component.RefSetController;
import cl.minsal.semantikos.modelws.fault.IllegalInputFault;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.minsal.semantikos.ws.utils.UtilsWS;

import javax.annotation.Resource;
import javax.ejb.EJB;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.*;
import javax.xml.ws.WebServiceContext;
import java.util.Arrays;
import java.util.List;


import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;
import static java.lang.System.currentTimeMillis;


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
    private AuthenticationManagerImpl authenticationManager;

    @Resource
    WebServiceContext wsctx;

    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    //Inicializacion del Bean
    //@PostConstruct


    /**
     * Metodo de autenticacion
     * @param idStablishment ID de establecimiento
     * @throws Exception
     */
    private void authenticate(String idStablishment) throws Exception {
        Pair credentials = UtilsWS.getCredentialsFromWSContext(wsctx.getMessageContext());
        authenticationManager.authenticateWS(credentials.getFirst().toString(), credentials.getSecond().toString());
        if(idStablishment!=null)
            authenticationManager.validateInstitution(idStablishment);
    }


    // REQ-WS-001
    @WebResult(name = "respuestaBuscarTermino")
    @WebMethod(operationName = "buscarTerminoPerfectMatch")
    public GenericTermSearchResponse buscarTermino( //GenericTermSearchResponse buscarTermino(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionBuscarTermino")
                    SimpleSearchTermRequest request
    ) {

        long init = currentTimeMillis();
        GenericTermSearchResponse response = new GenericTermSearchResponse();
        try {
            this.authenticate(request.getIdStablishment());

        /* Se hace una validación de los parámetros */
            validateAtLeastOneCategoryOrOneRefSet(request);
            logger.debug("ws-req-001: " + request.getTerm() + ", " + request.getCategoryNames() + " " + request
                    .getRefSetNames());

            logger.info("ws-req-001: {}s", String.format("%.2f", (currentTimeMillis() - init)/1.0));
            response =  this.conceptController.searchTermGeneric(request.getTerm(), request.getCategoryNames(), request.getRefSetNames());

        }catch(Exception e){
            logger.error(e.getMessage(), e);
            response.setCode(1);
            response.setMessage(e.getMessage());
        }

        return response;

    }



    // REQ-WS-002 Paginados
    @WebResult(name = "respuestaConceptosPorCategoriaPaginados")
    @WebMethod(operationName = "conceptosPorCategoriaPaginados")
    public ConceptsResponse conceptosPorCategoriaPaginados(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionConceptosPorCategoriaPaginados")
                    CategoryRequestPaginated request
    ) {
        ConceptsResponse response = new ConceptsResponse();
        try {
            this.authenticate(request.getIdStablishment());
            response = this.conceptController.findConceptsByCategoryPaginated(request.getCategoryName(), request.getIdStablishment(),
                                                                          request.getPageNumber(), request.getPageSize());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.setCode(1);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @WebResult(name = "respuestaCategorias")
    @WebMethod(operationName = "listaCategorias")
    public CategoriesResponse listaCategorias(){

        CategoriesResponse response = new CategoriesResponse();
        logger.debug("Se invocado el servicio listaCategorias().");
        try {
            this.authenticate(null);
            response = this.categoryController.categoryList();
            logger.debug("El servicio listaCategorias() a retornado " + response.getCategoryResponses().size()
                    + " categorias Responses.");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            response.setCode(1);
            response.setMessage(e.getMessage());
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
    ) {
        GenericTermSearchResponse response = new GenericTermSearchResponse();
        try {
            this.authenticate(request.getIdStablishment());
            /* Se hace una validación de los parámetros */
            validateAtLeastOneCategoryOrOneRefSet(request);
            response = this.conceptController.searchTermGeneric2(request.getTerm(), request.getCategoryNames(), request
                    .getRefSetNames());
        }catch(Exception e){
            response.setCode(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    /**
     * REQ-WS-005: El sistema Semantikos debe disponer un servicio que busque todos los términos que pertenezcan a un
     * concepto que contenga el atributo “Pedible”, esto solo aplica para las categorías de interconsulta, indicaciones
     * generales e indicaciones de laboratorio.
     *
     * @return Los términos solicitados
     *
     */
    @WebResult(name = "respuestaObtenerTerminosPedibles")
    @WebMethod(operationName = "obtenerTerminosPedibles")
    public TermSearchesResponse obtenerTerminosPedibles(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionObtenerTerminosPedibles")
                    RequestableConceptsRequest request
    ){
        TermSearchesResponse response = new TermSearchesResponse();
        try {
            this.authenticate(request.getIdStablishment());
            /* Se hace una validación de los parámetros */
            obtenerTerminosPediblesParamValidation(request);
            response = conceptController.searchRequestableDescriptions(request.getCategoryNames(), request.getRequestable());
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            response.setCode(1);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    // REQ-WS-006
    @WebResult(name = "respuestaSugerenciasDeDescripciones")
    @WebMethod(operationName = "sugerenciasDeDescripciones")
    public SuggestedDescriptionsResponse sugerenciasDeDescripciones(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionSugerenciasDeDescripciones")
                    DescriptionsSuggestionsRequest request
    ) {
        SuggestedDescriptionsResponse response = new SuggestedDescriptionsResponse();
        try {
            this.authenticate(request.getIdStablishment());
            if ((request.getCategoryNames() == null || request.getCategoryNames().isEmpty())) {
                throw new IllegalInputFault("Debe ingresar por lo menos una Categoría");
            }
            if (request.getTerm() == null || "".equals(request.getTerm())) {
                throw new IllegalInputFault("Debe ingresar un Termino a buscar");
            }
            if (request.getTerm().length() < 3) {
                throw new IllegalInputFault("El termino a buscar debe tener minimo 3 caracteres de largo");
            }
            response = this.conceptController.searchSuggestedDescriptions(request.getTerm(), request.getCategoryNames());
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            response.setCode(1);
            response.setMessage(e.getMessage());
        }
        return response;

        //return this.conceptController.searchTruncatePerfect(request.getTerm(), request.getCategoryNames());
    }


    // REQ-WS-007
    // REQ-WS-009
    @WebResult(name = "respuestaRefSetsPorIdDescripcion")
    @WebMethod(operationName = "refSetsPorIdDescripcion")
    public List<RefSetSearchResponse> refSetsPorIdDescripcion(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionRefSetsPorIdDescripcion")
                    RefSetsByDescriptionIdRequest request
    ) {
        List<RefSetSearchResponse> response = null;
        try {
            this.authenticate(request.getIdStablishment());
            List<String> descriptionIds = request.getDescriptionId();
            Boolean includeInstitutions = request.getIncludeInstitutions();
            String idStablishment = request.getIdStablishment();

            if (descriptionIds == null || descriptionIds.isEmpty()) {
                throw new IllegalInputFault("Debe ingresar por lo menos un idDescripcion");
            }

            response =  this.refSetController.findRefSetsByDescriptions(descriptionIds, includeInstitutions, idStablishment);
        }catch(Exception e){
            RefSetSearchResponse result = new RefSetSearchResponse();
            result.setCode(1);
            result.setMessage(e.getMessage());
            response = Arrays.asList(result);
        }

        return response;

    }

    // REQ-WS-008
    @WebResult(name = "respuestaListaRefSets")
    @WebMethod(operationName = "listaRefSets")
    public RefSetsResponse listaRefSet(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionListaRefSets")
                    RefSetsRequest request
    ) {
        RefSetsResponse response = new RefSetsResponse();
        try {
            this.authenticate(request.getIdStablishment());
            response = this.refSetController.refSetList(request.getIncludeInstitutions(), request.getIdStablishment());
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            response.setCode(1);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    // REQ-WS-022
    @WebResult(name = "respuestaDescripcionesPreferidasPorRefSet")
    @WebMethod(operationName = "descripcionesPreferidasPorRefSet")
    public RefSetLightResponse descripcionesPreferidasPorRefSet(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionDescripcionesPreferidasPorRefSet")
                    ConceptsByRefsetRequest request
    )  {

        RefSetLightResponse response = new RefSetLightResponse();
        try {
            this.authenticate(request.getIdStablishment());
            response = this.conceptController.conceptsLightByRefset(request.getRefSetName());
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            response.setCode(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    // REQ-WS-023
    @WebResult(name = "respuestaConceptosPorRefSet")
    @WebMethod(operationName = "conceptosPorRefSet")
    public RefSetResponse conceptosPorRefSet(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionConceptosPorRefSet")
                    ConceptsByRefsetRequest request
    ) {
        RefSetResponse response = new RefSetResponse();
        try {
            this.authenticate(request.getIdStablishment());
            response = this.conceptController.conceptsByRefset(request.getRefSetName());
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            response.setCode(1);
            response.setMessage(e.getMessage());
        }
        return response;
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
    ){
        CrossmapSetsResponse response = new CrossmapSetsResponse();
        try {
            this.authenticate(request.getIdStablishment());
            response = this.crossmapsController.getCrossmapSets(request.getIdStablishment());
        }catch(Exception e){
            response.setCode(1);
            response.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }

        return response;
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
    ) {
        CrossmapSetMembersResponse response = new CrossmapSetMembersResponse();
        try {
            this.authenticate(request.getIdStablishment());
            response = this.crossmapsController.getCrossmapSetMembersByCrossmapSetAbbreviatedName(request.getCrossmapSetAbbreviatedName());
        }catch(Exception e){
            response.setCode(1);
            response.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return response;
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
    )  {
        IndirectCrossMapSearchResponse response = new IndirectCrossMapSearchResponse();
        try {
            this.authenticate(descripcionIDorConceptIDRequest.getIdStablishment());
            response = this.crossmapsController.getIndirectCrossmapsByDescriptionID(descripcionIDorConceptIDRequest);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.setCode(1);
            response.setMessage(e.getMessage());
        }
        return response;
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
    ) {
        CrossmapSetMembersResponse response = new CrossmapSetMembersResponse();
        try {
            this.authenticate(request.getIdStablishment());
            response =  this.crossmapsController.getDirectCrossmapsSetMembersByDescriptionID(request);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            response.setCode(1);
            response.setMessage(e.getMessage());
        }
        return response;

    }

    // REQ-WS-028
    @WebResult(name = "respuestaConceptoPorIdDescripcion")
    @WebMethod(operationName = "conceptoPorIdDescripcion")
    public ConceptResponse conceptoPorIdDescripcion(
            @XmlElement(required = true, namespace = "http://service.ws.semantikos.minsal.cl/")
            @WebParam(name = "peticionConceptoPorIdDescripcion")
                    ConceptByDescriptionIDRequest request
    ) {
        ConceptResponse response = new ConceptResponse();
        try {
            this.authenticate(request.getIdStablishment());
            response = this.conceptController.conceptByDescriptionId(request.getDescriptionID());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.setCode(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    /**
     * Este método es responsable de validar que una petición posea al menos una categoría o un refset.
     *
     * @param request La petición enviada.
     * @throws cl.minsal.semantikos.modelws.fault.IllegalInputFault Se arroja si se viola la condición.
     */
    private void validateAtLeastOneCategory(RequestableConceptsRequest request) throws IllegalInputFault {
        if (isEmpty(request.getCategoryNames())) {
            throw new IllegalInputFault("Debe ingresar por lo menos una Categoría.");
        }
    }

    private void validateAtLeastOneCategoryOrOneRefSet(SimpleSearchTermRequest request) throws IllegalInputFault {
        if (isEmpty(request.getCategoryNames()) /*&& isEmpty(request.getRefSetNames())*/) {
            //throw new IllegalInputFault("Debe ingresar por lo menos una Categoría o un RefSet.");
            throw new IllegalInputFault("Debe ingresar por lo menos una Categoría.");
        }
        if (request.getTerm() == null || request.getTerm().trim().isEmpty()) {
            throw new IllegalInputFault("Debe ingresar un Termino a buscar");
        }
    }

    private boolean isEmpty(List<String> list) {
        return list.isEmpty() || (list.size() == 1 && list.contains(EMPTY_STRING));
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
        validateAtLeastOneCategory(request);

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

}
