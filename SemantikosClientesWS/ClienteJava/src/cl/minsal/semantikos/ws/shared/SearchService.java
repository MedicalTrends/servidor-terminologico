
package cl.minsal.semantikos.ws.shared;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "SearchService", targetNamespace = "http://service.ws.semantikos.minsal.cl/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface SearchService {


    /**
     * 
     * @param nombreAbreviadoCrossmapSet
     * @return
     *     returns cl.minsal.semantikos.ws.shared.CrossmapSetMembersResponse
     */
    @WebMethod
    @WebResult(name = "crossmapSetMember", targetNamespace = "")
    @RequestWrapper(localName = "crossmapSetMembersDeCrossmapSet", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.CrossmapSetMembersDeCrossmapSet")
    @ResponseWrapper(localName = "crossmapSetMembersDeCrossmapSetResponse", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.CrossmapSetMembersDeCrossmapSetResponse")
    public CrossmapSetMembersResponse crossmapSetMembersDeCrossmapSet(
        @WebParam(name = "nombreAbreviadoCrossmapSet", targetNamespace = "")
        String nombreAbreviadoCrossmapSet);

    /**
     * 
     * @param idInstitucion
     * @return
     *     returns cl.minsal.semantikos.ws.shared.CrossmapSetsResponse
     */
    @WebMethod
    @WebResult(name = "crossmapSetResponse", targetNamespace = "")
    @RequestWrapper(localName = "getCrossmapSets", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.GetCrossmapSets")
    @ResponseWrapper(localName = "getCrossmapSetsResponse", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.GetCrossmapSetsResponse")
    public CrossmapSetsResponse getCrossmapSets(
        @WebParam(name = "idInstitucion", targetNamespace = "")
        String idInstitucion);

    /**
     * 
     * @param idDescripcion
     * @return
     *     returns cl.minsal.semantikos.ws.shared.Concepto
     * @throws NotFoundFault_Exception
     */
    @WebMethod
    @WebResult(name = "concepto", targetNamespace = "http://service.ws.semantikos.minsal.cl/")
    @RequestWrapper(localName = "conceptoPorIdDescripcion", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.ConceptoPorIdDescripcion")
    @ResponseWrapper(localName = "conceptoPorIdDescripcionResponse", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.ConceptoPorIdDescripcionResponse")
    public Concepto conceptoPorIdDescripcion(
        @WebParam(name = "idDescripcion", targetNamespace = "")
        String idDescripcion)
        throws NotFoundFault_Exception
    ;

    /**
     * 
     * @param idStablishment
     * @param incluyeEstablecimientos
     * @return
     *     returns cl.minsal.semantikos.ws.shared.RespuestaRefSets
     * @throws NotFoundFault_Exception
     */
    @WebMethod
    @WebResult(name = "refsetResponse", targetNamespace = "")
    @RequestWrapper(localName = "listaRefSet", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.ListaRefSet")
    @ResponseWrapper(localName = "listaRefSetResponse", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.ListaRefSetResponse")
    public RespuestaRefSets listaRefSet(
        @WebParam(name = "incluyeEstablecimientos", targetNamespace = "")
        Boolean incluyeEstablecimientos,
        @WebParam(name = "idStablishment", targetNamespace = "")
        String idStablishment)
        throws NotFoundFault_Exception
    ;

    /**
     * 
     * @param peticionBuscarTermino
     * @return
     *     returns cl.minsal.semantikos.ws.shared.RespuestaConceptosPorCategoria
     * @throws IllegalInputFault_Exception
     * @throws NotFoundFault_Exception
     */
    @WebMethod
    @WebResult(name = "respuestaConceptos", targetNamespace = "")
    @RequestWrapper(localName = "buscarTruncatePerfect", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.BuscarTruncatePerfect")
    @ResponseWrapper(localName = "buscarTruncatePerfectResponse", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.BuscarTruncatePerfectResponse")
    public RespuestaConceptosPorCategoria buscarTruncatePerfect(
        @WebParam(name = "peticionBuscarTermino", targetNamespace = "http://service.ws.semantikos.minsal.cl/")
        PeticionBuscarTermino peticionBuscarTermino)
        throws IllegalInputFault_Exception, NotFoundFault_Exception
    ;

    /**
     * 
     * @param peticionConceptosPorRefSet
     * @return
     *     returns cl.minsal.semantikos.ws.shared.RespuestaConceptosPorRefSet
     * @throws NotFoundFault_Exception
     */
    @WebMethod
    @WebResult(name = "respuestaConceptosPorRefSet", targetNamespace = "http://service.ws.semantikos.minsal.cl/")
    @RequestWrapper(localName = "conceptosPorRefSet", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.ConceptosPorRefSet")
    @ResponseWrapper(localName = "conceptosPorRefSetResponse", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.ConceptosPorRefSetResponse")
    public RespuestaConceptosPorRefSet conceptosPorRefSet(
        @WebParam(name = "peticionConceptosPorRefSet", targetNamespace = "http://service.ws.semantikos.minsal.cl/")
        PeticionConceptosPorRefSet peticionConceptosPorRefSet)
        throws NotFoundFault_Exception
    ;

    /**
     * 
     * @return
     *     returns cl.minsal.semantikos.ws.shared.RespuestaCategorias
     * @throws NotFoundFault_Exception
     */
    @WebMethod
    @WebResult(name = "respuestaCategorias", targetNamespace = "http://service.ws.semantikos.minsal.cl/")
    @RequestWrapper(localName = "listaCategorias", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.ListaCategorias")
    @ResponseWrapper(localName = "listaCategoriasResponse", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.ListaCategoriasResponse")
    public RespuestaCategorias listaCategorias()
        throws NotFoundFault_Exception
    ;

    /**
     * 
     * @param peticionRefSetsPorIdDescripcion
     * @return
     *     returns cl.minsal.semantikos.ws.shared.RespuestaRefSets
     * @throws IllegalInputFault_Exception
     * @throws NotFoundFault_Exception
     */
    @WebMethod
    @WebResult(name = "refsetResponse", targetNamespace = "")
    @RequestWrapper(localName = "refSetsPorIdDescripcion", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.RefSetsPorIdDescripcion")
    @ResponseWrapper(localName = "refSetsPorIdDescripcionResponse", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.RefSetsPorIdDescripcionResponse")
    public RespuestaRefSets refSetsPorIdDescripcion(
        @WebParam(name = "peticionRefSetsPorIdDescripcion", targetNamespace = "http://service.ws.semantikos.minsal.cl/")
        PeticionRefSetsPorIdDescripcion peticionRefSetsPorIdDescripcion)
        throws IllegalInputFault_Exception, NotFoundFault_Exception
    ;

    /**
     * 
     * @param descripcionID
     * @return
     *     returns cl.minsal.semantikos.ws.shared.CrossmapSetMembersResponse
     * @throws NotFoundFault_Exception
     */
    @WebMethod
    @WebResult(name = "crossmapSetMember", targetNamespace = "")
    @RequestWrapper(localName = "crossMapsDirectosPorIDDescripcion", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.CrossMapsDirectosPorIDDescripcion")
    @ResponseWrapper(localName = "crossMapsDirectosPorIDDescripcionResponse", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.CrossMapsDirectosPorIDDescripcionResponse")
    public CrossmapSetMembersResponse crossMapsDirectosPorIDDescripcion(
        @WebParam(name = "DescripcionID", targetNamespace = "")
        DescriptionIDorConceptIDRequest descripcionID)
        throws NotFoundFault_Exception
    ;

    /**
     * 
     * @param peticionConceptosPorCategoria
     * @return
     *     returns cl.minsal.semantikos.ws.shared.RespuestaConceptosPorCategoria
     * @throws NotFoundFault_Exception
     */
    @WebMethod
    @WebResult(name = "respuestaConceptos", targetNamespace = "")
    @RequestWrapper(localName = "conceptosPorCategoria", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.ConceptosPorCategoria")
    @ResponseWrapper(localName = "conceptosPorCategoriaResponse", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.ConceptosPorCategoriaResponse")
    public RespuestaConceptosPorCategoria conceptosPorCategoria(
        @WebParam(name = "peticionConceptosPorCategoria", targetNamespace = "")
        PeticionPorCategoria peticionConceptosPorCategoria)
        throws NotFoundFault_Exception
    ;

    /**
     * 
     * @param descripcionIDorConceptIDRequest
     * @return
     *     returns cl.minsal.semantikos.ws.shared.IndirectCrossmapsSearch
     * @throws NotFoundFault_Exception
     */
    @WebMethod
    @WebResult(name = "indirectCrossmaps", targetNamespace = "")
    @RequestWrapper(localName = "crossMapsIndirectosPorDescripcionIDorConceptID", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.CrossMapsIndirectosPorDescripcionIDorConceptID")
    @ResponseWrapper(localName = "crossMapsIndirectosPorDescripcionIDorConceptIDResponse", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.CrossMapsIndirectosPorDescripcionIDorConceptIDResponse")
    public IndirectCrossmapsSearch crossMapsIndirectosPorDescripcionIDorConceptID(
        @WebParam(name = "descripcionIDorConceptIDRequest", targetNamespace = "")
        DescriptionIDorConceptIDRequest descripcionIDorConceptIDRequest)
        throws NotFoundFault_Exception
    ;

    /**
     * 
     * @param peticionConceptosPorRefSet
     * @return
     *     returns cl.minsal.semantikos.ws.shared.RespuestaConceptosPorRefSet
     * @throws NotFoundFault_Exception
     */
    @WebMethod
    @WebResult(name = "respuestaConceptosPorRefSet", targetNamespace = "http://service.ws.semantikos.minsal.cl/")
    @RequestWrapper(localName = "descripcionesPreferidasPorRefSet", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.DescripcionesPreferidasPorRefSet")
    @ResponseWrapper(localName = "descripcionesPreferidasPorRefSetResponse", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.DescripcionesPreferidasPorRefSetResponse")
    public RespuestaConceptosPorRefSet descripcionesPreferidasPorRefSet(
        @WebParam(name = "peticionConceptosPorRefSet", targetNamespace = "http://service.ws.semantikos.minsal.cl/")
        PeticionConceptosPorRefSet peticionConceptosPorRefSet)
        throws NotFoundFault_Exception
    ;

    /**
     * 
     * @param peticionObtenerTerminosPedibles
     * @return
     *     returns cl.minsal.semantikos.ws.shared.RespuestaBuscarTermino
     * @throws IllegalInputFault_Exception
     */
    @WebMethod
    @WebResult(name = "respuestaBuscarTermino", targetNamespace = "http://service.ws.semantikos.minsal.cl/")
    @RequestWrapper(localName = "obtenerTerminosPedibles", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.ObtenerTerminosPedibles")
    @ResponseWrapper(localName = "obtenerTerminosPediblesResponse", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.ObtenerTerminosPediblesResponse")
    public RespuestaBuscarTermino obtenerTerminosPedibles(
        @WebParam(name = "peticionObtenerTerminosPedibles", targetNamespace = "")
        PeticionConceptosPedibles peticionObtenerTerminosPedibles)
        throws IllegalInputFault_Exception
    ;

    /**
     * 
     * @param peticionBuscarTermino
     * @return
     *     returns cl.minsal.semantikos.ws.shared.RespuestaBuscarTerminoGenerica
     * @throws IllegalInputFault_Exception
     * @throws NotFoundFault_Exception
     */
    @WebMethod
    @WebResult(name = "respuestaBuscarTermino", targetNamespace = "")
    @RequestWrapper(localName = "buscarTermino", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.BuscarTermino")
    @ResponseWrapper(localName = "buscarTerminoResponse", targetNamespace = "http://service.ws.semantikos.minsal.cl/", className = "cl.minsal.semantikos.ws.shared.BuscarTerminoResponse")
    public RespuestaBuscarTerminoGenerica buscarTermino(
        @WebParam(name = "peticionBuscarTermino", targetNamespace = "")
        PeticionBuscarTerminoSimple peticionBuscarTermino)
        throws IllegalInputFault_Exception, NotFoundFault_Exception
    ;

}
