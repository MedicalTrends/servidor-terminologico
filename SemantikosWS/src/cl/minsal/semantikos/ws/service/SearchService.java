package cl.minsal.semantikos.ws.service;

import cl.minsal.semantikos.kernel.auth.AuthenticationManager;
import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.ws.component.CategoryController;
import cl.minsal.semantikos.ws.component.ConceptController;
import cl.minsal.semantikos.ws.component.CrossmapController;
import cl.minsal.semantikos.ws.component.RefSetController;
import cl.minsal.semantikos.ws.fault.IllegalInputFault;
import cl.minsal.semantikos.ws.fault.NotFoundFault;
import cl.minsal.semantikos.ws.request.*;
import cl.minsal.semantikos.ws.response.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.WebServiceContext;
import java.util.Arrays;
import java.util.List;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;


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

    //Inicializacion del Bean
    //@PostConstruct
    @AroundInvoke
    protected Object authenticate(InvocationContext ctx) throws Exception {

        try {
            authenticationManager.authenticate(wsctx.getMessageContext());
        }
        catch (Exception e) {
            throw new NotFoundFault(e.getMessage());
        }
        return ctx.proceed();
    }

    // REQ-WS-001
    @WebResult(name = "respuestaBuscarTerminoPerfectMatch")
    @WebMethod(operationName = "buscarTerminoPerfectMatch")
    public GenericTermSearchResponse buscarTermino(
            @XmlElement(required = true)
            @WebParam(name = "peticionBuscarTermino")
                    SimpleSearchTermRequest request
    ) throws IllegalInputFault, NotFoundFault {

        /* Se hace una validación de los parámetros */
        validateAtLeastOneCategoryOrOneRefSet(request);

        logger.debug("ws-req-001: " + request.getTerm() + ", " + request.getCategoryNames() + " " + request
                .getRefSetNames());
        return this.conceptController.searchTermGeneric(request.getTerm(), request.getCategoryNames(), request
                .getRefSetNames());
    }

    // REQ-WS-002
    @WebResult(name = "respuestaConceptos")
    @WebMethod(operationName = "conceptosPorCategoria")
    public ConceptsResponse conceptosPorCategoria(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosPorCategoria")
                    CategoryRequest request
    ) throws NotFoundFault {
        return this.conceptController.findConceptsByCategory(request.getCategoryName(), request.getIdStablishment());
    }

    // REQ-WS-002 Paginados
    @WebResult(name = "respuestaConceptos")
    @WebMethod(operationName = "conceptosPorCategoriaPaginados")
    public ConceptsResponse conceptosPorCategoriaPaginados(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosPorCategoria")
                    CategoryRequestPaginated request
    ) throws NotFoundFault {
        return this.conceptController.findConceptsByCategoryPaginated(request.getCategoryName(), request.getIdStablishment(),
                                                                      request.getPageNumber(), request.getPageSize());
    }

    @WebResult(name = "respuestaCategorias")
    @WebMethod(operationName = "listaCategorias")
    public CategoriesResponse listaCategorias() throws NotFoundFault {
        logger.debug("Se invocado el servicio listaCategorias().");

        CategoriesResponse categoriesResponse = this.categoryController.categoryList();
        logger.debug("El servicio listaCategorias() a retornado " + categoriesResponse.getCategoryResponses().size()
                + " categorias Responses.");

        return categoriesResponse;
    }

    // REQ-WS-004
    /*
    @WebResult(name = "respuestaConceptos")
    @WebMethod(operationName = "buscarTruncatePerfect")
    public ConceptsResponse buscarTruncatePerfect(
            @XmlElement(required = true)
            @WebParam(name = "peticionBuscarTermino")
                    SearchTermRequest request
    ) throws IllegalInputFault, NotFoundFault {

        if ((request.getCategoryNames() == null || request.getCategoryNames().isEmpty())
                && (request.getRefSetNames() == null || request.getRefSetNames().isEmpty())) {
            throw new IllegalInputFault("Debe ingresar por lo menos una Categoría o un RefSet");
        }
        if (request.getTerm() == null || "".equals(request.getTerm())) {
            throw new IllegalInputFault("Debe ingresar un Termino a buscar");
        }
        return this.conceptController.searchTruncatePerfect(request.getTerm(), request.getCategoryNames(), request
                .getRefSetNames());
    }
    */
    // REQ-WS-004
    @WebResult(name = "respuestaBuscarTerminoTruncatePerfect")
    @WebMethod(operationName = "buscarTerminoTruncatePerfect")
    public GenericTermSearchResponse buscarTruncatePerfect(
            @XmlElement(required = true)
            @WebParam(name = "peticionBuscarTermino")
                    SimpleSearchTermRequest request
    ) throws IllegalInputFault, NotFoundFault {

        /* Se hace una validación de los parámetros */
        validateAtLeastOneCategoryOrOneRefSet(request);

        return this.conceptController.searchTermGeneric2(request.getTerm(), request.getCategoryNames(), request
                .getRefSetNames());
    }

    /**
     * REQ-WS-005: El sistema Semantikos debe disponer un servicio que busque todos los términos que pertenezcan a un
     * concepto que contenga el atributo “Pedible”, esto solo aplica para las categorías de interconsulta, indicaciones
     * generales e indicaciones de laboratorio.
     *
     * @return Los términos solicitados
     * @throws cl.minsal.semantikos.ws.fault.IllegalInputFault
     */
    @WebResult(name = "respuestaBuscarTermino")
    @WebMethod(operationName = "obtenerTerminosPedibles")
    public TermSearchesResponse obtenerTerminosPedibles(
            @XmlElement(required = true)
            @WebParam(name = "peticionObtenerTerminosPedibles")
                    RequestableConceptsRequest request
    ) throws IllegalInputFault {

        /* Se hace una validación de los parámetros */
        obtenerTerminosPediblesParamValidation(request);

        return conceptController.searchRequestableDescriptions(request.getCategoryNames(), new BasicTypeValue(request.getRequestable()));
    }

    /**
     * Este método es responsable de realizar la validación de los parámetros de entrada del servicio REQ-WS-005.
     *
     * @param request La petición con los parámetros de entrada.
     * @throws cl.minsal.semantikos.ws.fault.IllegalInputFault Arrojado si se solicitan cateogorías distintas a las
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

    /**
     * Este método es responsable de validar que una petición posea al menos una categoría o un refset.
     *
     * @param request La petición enviada.
     * @throws cl.minsal.semantikos.ws.fault.IllegalInputFault Se arroja si se viola la condición.
     */
    private void validateAtLeastOneCategory(RequestableConceptsRequest request) throws IllegalInputFault {
        if (isEmpty(request.getCategoryNames())) {
            throw new IllegalInputFault("Debe ingresar por lo menos una Categoría.");
        }
    }

    private void validateAtLeastOneCategoryOrOneRefSet(SimpleSearchTermRequest request) throws IllegalInputFault {
        if (isEmpty(request.getCategoryNames()) && isEmpty(request.getRefSetNames())) {
            throw new IllegalInputFault("Debe ingresar por lo menos una Categoría o un RefSet.");
        }
        if (request.getTerm() == null || request.getTerm().trim().isEmpty()) {
            throw new IllegalInputFault("Debe ingresar un Termino a buscar");
        }
    }

    public boolean isEmpty(List<String> list) {
        return list.isEmpty() || (list.size() == 1 && list.contains(EMPTY_STRING));
    }

    // REQ-WS-007
    // REQ-WS-009
    @WebResult(name = "refsetSearchResponse")
    @WebMethod(operationName = "refSetsPorIdDescripcion")
    public List<RefSetSearchResponse> refSetsPorIdDescripcion(
            @XmlElement(required = true)
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
    @WebResult(name = "refsetResponse")
    @WebMethod(operationName = "listaRefSet")
    public RefSetsResponse listaRefSet(
            @XmlElement(required = false, defaultValue = "true")
            @WebParam(name = "incluyeEstablecimientos")
                    Boolean includeInstitutions,
            @XmlElement(required = true)
            @WebParam(name = "idStablishment")
                    String idStablishment
    ) throws NotFoundFault {
        return this.refSetController.refSetList(includeInstitutions);
    }

    // REQ-WS-022
    @WebResult(name = "respuestaDescripcionesPreferidasPorRefSet")
    @WebMethod(operationName = "descripcionesPreferidasPorRefSet")
    public RefSetLightResponse descripcionesPreferidasPorRefSet(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosPorRefSet")
                    ConceptsByRefsetRequest request
    ) throws NotFoundFault {
        return this.conceptController.conceptsLightByRefset(request.getRefSetName());
    }

    // REQ-WS-023
    @WebResult(name = "respuestaConceptosPorRefSet")
    @WebMethod(operationName = "conceptosPorRefSet")
    public RefSetResponse conceptosPorRefSet(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosPorRefSet")
                    ConceptsByRefsetRequest request
    ) throws NotFoundFault {
        return this.conceptController.conceptsByRefset(request.getRefSetName());
    }

    /**
     * REQ-WS-024: El sistema Semantikos debe disponer un servicio que permita obtener una lista de todos los
     * CrossMapsets (sets/vocabularios Mapeos) Ej.: CIE9; CIE10; CIEO.
     *
     * @param idInstitution Identificador de la institución.
     * @return Una lista de los crossmapSets existentes.
     */
    @WebResult(name = "crossmapSetResponse")
    @WebMethod(operationName = "getCrossmapSets")
    public CrossmapSetsResponse getCrossmapSets(
            @XmlElement(required = true)
            @WebParam(name = "idInstitucion")
                    String idInstitution
    ) {
        return this.crossmapsController.getCrossmapSets(idInstitution);
    }

    /**
     * REQ-WS-025: El sistema Semantikos debe disponer un servicio que permita obtener los CrossMapsetsMembers a partir
     * de un CrossMapset.
     *
     * @param crossmapSetAbbreviatedName El valor nombre abreviado de un crossmapSet.
     * @return Una lista de los crossmapSetMembers del crossmapSet dado como parámetro.
     */
    @WebResult(name = "crossmapSetMember")
    @WebMethod(operationName = "crossmapSetMembersDeCrossmapSet")
    public CrossmapSetMembersResponse crossmapSetMembersDeCrossmapSet(
            @XmlElement(required = true)
            @WebParam(name = "nombreAbreviadoCrossmapSet")
                    String crossmapSetAbbreviatedName
    ) {
        return this.crossmapsController.getCrossmapSetMembersByCrossmapSetAbbreviatedName(crossmapSetAbbreviatedName);
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
     * @throws cl.minsal.semantikos.ws.fault.NotFoundFault Arrojada si no existe una descripción con
     * <em>DESCRIPTION_ID</em> igual al indicado por el
     *                                                     parámetro <code>descriptionId</code>.
     */
    @WebResult(name = "indirectCrossmaps")
    @WebMethod(operationName = "crossMapsIndirectosPorDescripcionIDorConceptID")
    public IndirectCrossMapSearchResponse crossMapsIndirectosPorDescriptionIDoConceptID(
            @XmlElement(required = true)
            @WebParam(name = "descripcionIDorConceptIDRequest")
                    DescriptionIDorConceptIDRequest descripcionIDorConceptIDRequest
    ) throws NotFoundFault {
        return this.crossmapsController.getIndirectCrossmapsByDescriptionID(descripcionIDorConceptIDRequest);
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
     * @throws cl.minsal.semantikos.ws.fault.NotFoundFault Arrojada si no existe una descripción con
     * <em>DESCRIPTION_ID</em> igual al indicado por el
     *                                                     parámetro <code>descriptionId</code>.
     */
    @WebResult(name = "crossmapSetMember")
    @WebMethod(operationName = "crossMapsDirectosPorIDDescripcion")
    public CrossmapSetMembersResponse crossmapSetMembersDirectosPorIDDescripcion(
            @XmlElement(required = true)
            @WebParam(name = "DescripcionID")
                    DescriptionIDorConceptIDRequest request
    ) throws NotFoundFault {
        return this.crossmapsController.getDirectCrossmapsSetMembersByDescriptionID(request);
    }

    // REQ-WS-028
    @WebResult(name = "concepto")
    @WebMethod(operationName = "conceptoPorIdDescripcion")
    public ConceptResponse conceptoPorIdDescripcion(
            @XmlElement(required = true)
            @WebParam(name = "idDescripcion")
                    String descriptionId
    ) throws NotFoundFault {
        return this.conceptController.conceptByDescriptionId(descriptionId);
    }

}
