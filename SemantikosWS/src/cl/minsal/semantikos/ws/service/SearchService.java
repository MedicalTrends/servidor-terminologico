package cl.minsal.semantikos.ws.service;

import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.ws.component.CategoryController;
import cl.minsal.semantikos.ws.component.ConceptController;
import cl.minsal.semantikos.ws.component.CrossmapController;
import cl.minsal.semantikos.ws.component.RefSetController;
import cl.minsal.semantikos.ws.fault.IllegalInputFault;
import cl.minsal.semantikos.ws.fault.NotFoundFault;
import cl.minsal.semantikos.ws.request.*;
import cl.minsal.semantikos.ws.response.*;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Development on 2016-11-18.
 *
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

    // REQ-WS-001
    @WebResult(name = "respuestaBuscarTermino")
    @WebMethod(operationName = "buscarTermino")
    public GenericTermSearchResponse buscarTermino(
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
        return this.conceptController.searchTermGeneric(request.getTerm(), request.getCategoryNames(), request.getRefSetNames());
    }

    // REQ-WS-002
    @WebResult(name = "respuestaConceptosPorCategoria")
    @WebMethod(operationName = "conceptosPorCategoria")
    public ConceptsByCategoryResponse conceptosPorCategoria(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosPorCategoria")
            ConceptsByCategoryRequest request
    ) throws NotFoundFault {
        return this.conceptController.conceptsByCategory(request.getCategoryName(), request.getPageNumber(), request.getPageSize());
    }

    @WebResult(name = "categoria")
    @WebMethod(operationName = "listaCategorias")
    public List<CategoryResponse> listaCategorias() throws NotFoundFault {
        return this.categoryController.categoryList();
    }

    // REQ-WS-004
    @WebResult(name = "respuestaBuscarTermino")
    @WebMethod(operationName = "buscarTruncatePerfect")
    public TermSearchResponse buscarTruncatePerfect(
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
        return this.conceptController.searchTruncatePerfect(request.getTerm(), request.getCategoryNames(), request.getRefSetNames(), request.getPageNumber(), request.getPageSize());
    }

    /**
     * REQ-WS-005: El sistema Semantikos debe disponer un servicio que busque todos los términos que pertenezcan a un
     * concepto que contenga el atributo “Pedible”, esto solo aplica para las categorías de interconsulta, indicaciones
     * generales e indicaciones de laboratorio.
     *
     * @return Los términos solicitados
     *
     * @throws IllegalInputFault
     */
    @WebResult(name = "respuestaBuscarTermino")
    @WebMethod(operationName = "obtenerTerminosPedibles")
    public TermSearchResponse obtenerTerminosPedibles(
            @XmlElement(required = true)
            @WebParam(name = "peticionObtenerTerminosPedibles")
            GetRequestableTermsRequest request
    ) throws IllegalInputFault {

        /* Se hace una validación de los parámetros */
        obtenerTerminosPediblesParamValidation(request);

        return conceptController.searchRequestableDescriptions(request.getCategoryNames(), request.getRefSetNames(), request.getRequestable());
    }

    /**
     * Este método es responsable de realizar la validación de los parámetros de entrada del servicio REQ-WS-005.
     *
     * @param request La petición con los parámetros de entrada.
     *
     * @throws IllegalInputFault Arrojado si se solicitan cateogorías distintas a las objetivo de la búsqueda o que
     *                           simplemente no existen. También se arroja si existen
     */
    private void obtenerTerminosPediblesParamValidation(GetRequestableTermsRequest request) throws IllegalInputFault {

        /* Se valida que haya al menos 1 categoría o 1 refset */
        validateAtLeastOneCategoryOrOneRefSet(request);

        /* Luego es necesario validar que si hay categorías especificadas, se limiten a "interconsulta", "indicaciones generales" e "indicaciones de laboratorio" */
        if (request.getCategoryNames().size() > 0) {

            Category interconsultas = categoryManager.getCategoryByName("Interconsultas");
            Category indicacionesGenerales = categoryManager.getCategoryByName("Indicaciones Generales");
            Category indicacionesLaboratio = categoryManager.getCategoryByName("Indicaciones de Laboratorio");
            List<String> categories = Arrays.asList(interconsultas.getName().toLowerCase(), indicacionesGenerales.getName().toLowerCase(), indicacionesLaboratio.getName().toLowerCase());

            for (String category : request.getCategoryNames()) {
                if (!categories.contains(category.toLowerCase())) {
                    throw new IllegalInputFault("La categoría " + category + " no es una categoría aceptable de búsqueda");
                }
            }
        }
    }

    /**
     * Este método es responsable de validar que una petición posea al menos una categoría o un refset.
     *
     * @param request La petición enviada.
     *
     * @throws IllegalInputFault Se arroja si se viola la condición.
     */
    private void validateAtLeastOneCategoryOrOneRefSet(GetRequestableTermsRequest request) throws IllegalInputFault {
        if (!(request.getCategoryNames().size() > 0 || request.getRefSetNames().size() > 0)) {
            throw new IllegalInputFault("Debe ingresar por lo menos una Categoría o un RefSet.");
        }
    }

    // REQ-WS-007
    // REQ-WS-009: TODO: solo retornar el String del nombre del refset.
    @WebResult(name = "respuestaRefSetsPorIdDescripcion")
    @WebMethod(operationName = "refSetsPorIdDescripcion")
    public TermSearchResponse refSetsPorIdDescripcion(
            @XmlElement(required = true)
            @WebParam(name = "peticionRefSetsPorIdDescripcion")
            RefSetsByDescriptionIdRequest request
    ) throws NotFoundFault, IllegalInputFault {
        if (request.getDescriptionId() == null || request.getDescriptionId().isEmpty()) {
            throw new IllegalInputFault("Debe ingresar por lo menos un idDescripcion");
        }
        return this.refSetController.findRefSetsByDescriptionIds(request.getDescriptionId(), request.getIncludeInstitutions());
    }

    // REQ-WS-008
    @WebResult(name = "refSet")
    @WebMethod(operationName = "listaRefSet")
    public List<RefSetResponse> listaRefSet(
            @XmlElement(required = false, defaultValue = "true")
            @WebParam(name = "incluyeEstablecimientos")
            Boolean includeInstitutions
    ) throws NotFoundFault {
        return this.refSetController.refSetList(includeInstitutions);
    }

    // REQ-WS-022
    @WebResult(name = "respuestaConceptosPorRefSet")
    @WebMethod(operationName = "descripcionesPreferidasPorRefSet")
    public ConceptsByRefsetResponse descripcionesPreferidasPorRefSet(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosPorRefSet")
            ConceptsByRefsetRequest request
    ) throws NotFoundFault {
        return this.conceptController.conceptsByRefsetWithPreferedDescriptions(request.getRefSetName(), request.getPageNumber(), request.getPageSize());
    }

    // REQ-WS-023
    @WebResult(name = "respuestaConceptosPorRefSet")
    @WebMethod(operationName = "conceptosPorRefSet")
    public ConceptsByRefsetResponse conceptosPorRefSet(
            @XmlElement(required = true)
            @WebParam(name = "peticionConceptosPorRefSet")
            ConceptsByRefsetRequest request
    ) throws NotFoundFault {
        return this.conceptController.conceptsByRefset(request.getRefSetName(), request.getPageNumber(), request.getPageSize());
    }

    /**
     * REQ-WS-024: El sistema Semantikos debe disponer un servicio que permita obtener una lista de todos los
     * CrossMapsets (sets/vocabularios Mapeos) Ej.: CIE9; CIE10; CIEO.
     *
     * @param idInstitution Identificador de la institución.
     *
     * @return Una lista de los crossmapSets existentes.
     */
    @WebResult(name = "crossmapSet")
    @WebMethod(operationName = "crossmapSets")
    public CrossmapSetsResponse crossmapSets(
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
     *
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
     * de un ID Descripción.
     *
     * @param descriptionId El valor de negocio <em>DESCRIPTION_ID</em> de la descripción cuyo concepto posee los
     *                      crossmaps indirectos que se desea recuperar.
     *
     * @return Una lista de crossmaps <em>indirectos</em> del concepto asociado a la descripción encapsulada en un
     * objeto mapeado
     * a un elemento XML.
     *
     * @throws NotFoundFault Arrojada si no existe una descripción con <em>DESCRIPTION_ID</em> igual al indicado por el
     *                       parámetro <code>descriptionId</code>.
     */
    @WebResult(name = "indirectCrossmaps")
    @WebMethod(operationName = "crossMapsIndirectosPorIDDescripcion")
    public IndirectCrossMapSearchResponse crossMapsIndirectosPorIDDescripcion(
            @XmlElement(required = true)
            @WebParam(name = "DescripcionID")
            String descriptionId
    ) throws NotFoundFault {
        return this.crossmapsController.getIndirectCrossmapsByDescriptionID(descriptionId);
    }

    /**
     * REQ-WS-027: El sistema Semantikos debe disponer un servicio que permita obtener los CrossMap directo a partir de
     * un ID Descripción.
     *
     * @param descriptionId El valor de negocio <em>DESCRIPTION_ID</em> de la descripción cuyo concepto posee los
     *                      crossmaps indirectos que se desea recuperar.
     *
     * @return Una lista de crossmaps <em>directos</em> del concepto asociado a la descripción encapsulada en un objeto
     * mapeado
     * a un elemento XML.
     *
     * @throws NotFoundFault Arrojada si no existe una descripción con <em>DESCRIPTION_ID</em> igual al indicado por el
     *                       parámetro <code>descriptionId</code>.
     */
    @WebResult(name = "crossmapSetMember")
    @WebMethod(operationName = "crossMapsDirectosPorIDDescripcion")
    public CrossmapSetMembersResponse crossmapSetMembersDirectosPorIDDescripcion(
            @XmlElement(required = true)
            @WebParam(name = "DescripcionID")
            String descriptionId
    ) throws NotFoundFault {
        return this.crossmapsController.getDirectCrossmapsSetMembersByDescriptionID(descriptionId);
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

    @WebResult(name = "concepto")
    @WebMethod(operationName = "conceptoPorId")
    public ConceptResponse conceptoPorId(
            @XmlElement(required = true)
            @WebParam(name = "idConcepto")
            String conceptId
    ) throws NotFoundFault {
        return this.conceptController.conceptById(conceptId);
    }

}
