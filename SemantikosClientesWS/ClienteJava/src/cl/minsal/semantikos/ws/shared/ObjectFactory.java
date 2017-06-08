
package cl.minsal.semantikos.ws.shared;

import cl.minsal.semantikos.ws.request.DescriptionIDorConceptIDRequest;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cl.minsal.semantikos.ws.shared package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RespuestaBuscarTerminoGenerica_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "respuestaBuscarTerminoGenerica");
    private final static QName _BuscarTermino_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "buscarTermino");
    private final static QName _DescripcionPerfectMatch_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "descripcionPerfectMatch");
    private final static QName _IndirectCrossmapsSearch_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "indirectCrossmapsSearch");
    private final static QName _ConceptosPedibles_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "conceptosPedibles");
    private final static QName _CrossMapsDirectosPorIDDescripcion_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "crossMapsDirectosPorIDDescripcion");
    private final static QName _ColumnaTablaAuxiliar_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "columnaTablaAuxiliar");
    private final static QName _RespuestaBuscarTermino_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "respuestaBuscarTermino");
    private final static QName _ConceptoPorIdDescripcionResponse_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "conceptoPorIdDescripcionResponse");
    private final static QName _RefSetsPorIdDescripcion_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "refSetsPorIdDescripcion");
    private final static QName _RespuestaConceptosPorRefSet_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "respuestaConceptosPorRefSet");
    private final static QName _PeticionPorCategoria_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "peticionPorCategoria");
    private final static QName _Paginacion_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "paginacion");
    private final static QName _CrossMapsIndirectosPorDescripcionIDorConceptID_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "crossMapsIndirectosPorDescripcionIDorConceptID");
    private final static QName _DescripcionesPreferidasPorRefSet_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "descripcionesPreferidasPorRefSet");
    private final static QName _DescripcionNoValida_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "descripcionNoValida");
    private final static QName _DescripcionSimplificada_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "descripcionSimplificada");
    private final static QName _Atributo_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "atributo");
    private final static QName _GetCrossmapSets_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "getCrossmapSets");
    private final static QName _CrossmapSetMember_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "crossmapSetMember");
    private final static QName _ListaRefSetResponse_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "listaRefSetResponse");
    private final static QName _Multiplicidad_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "multiplicidad");
    private final static QName _ConceptosPorCategoria_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "conceptosPorCategoria");
    private final static QName _AtributoRelacion_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "atributoRelacion");
    private final static QName _ConceptosPorCategoriaResponse_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "conceptosPorCategoriaResponse");
    private final static QName _CrossmapSet_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "crossmapSet");
    private final static QName _RespuestaConceptosPorCategoria_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "respuestaConceptosPorCategoria");
    private final static QName _PeticionBuscarTerminoSimple_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "peticionBuscarTerminoSimple");
    private final static QName _ConceptosPorRefSet_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "conceptosPorRefSet");
    private final static QName _PeticionBuscarTermino_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "peticionBuscarTermino");
    private final static QName _Usuario_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "usuario");
    private final static QName _Objetivo_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "objetivo");
    private final static QName _CrossmapSetsResponse_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "crossmapSetsResponse");
    private final static QName _TablaAuxiliar_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "tablaAuxiliar");
    private final static QName _ObtenerTerminosPediblesResponse_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "obtenerTerminosPediblesResponse");
    private final static QName _ListaCategorias_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "listaCategorias");
    private final static QName _BuscarTruncatePerfect_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "buscarTruncatePerfect");
    private final static QName _ObtenerTerminosPedibles_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "obtenerTerminosPedibles");
    private final static QName _ConceptoPorIdDescripcion_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "conceptoPorIdDescripcion");
    private final static QName _BuscarTruncatePerfectResponse_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "buscarTruncatePerfectResponse");
    private final static QName _IndirectCrossmap_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "indirectCrossmap");
    private final static QName _CrossmapSetMembersDeCrossmapSet_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "crossmapSetMembersDeCrossmapSet");
    private final static QName _ListaCategoriasResponse_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "listaCategoriasResponse");
    private final static QName _BuscarTerminoResponse_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "buscarTerminoResponse");
    private final static QName _Categoria_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "categoria");
    private final static QName _DescriptionIDorConceptIDRequest_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "descriptionIDorConceptIDRequest");
    private final static QName _DefinicionRelacion_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "definicionRelacion");
    private final static QName _IllegalInputFault_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "IllegalInputFault");
    private final static QName _NotFoundFault_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "NotFoundFault");
    private final static QName _DefinicionObjetivo_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "definicionObjetivo");
    private final static QName _CrossMapsIndirectosPorDescripcionIDorConceptIDResponse_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "crossMapsIndirectosPorDescripcionIDorConceptIDResponse");
    private final static QName _RespuestaRefSets_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "respuestaRefSets");
    private final static QName _DescripcionPendiente_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "descripcionPendiente");
    private final static QName _CrossmapSetMembersResponse_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "crossmapSetMembersResponse");
    private final static QName _ConceptosPorRefSetResponse_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "conceptosPorRefSetResponse");
    private final static QName _CrossMapsDirectosPorIDDescripcionResponse_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "crossMapsDirectosPorIDDescripcionResponse");
    private final static QName _RelacionSnomedCT_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "relacionSnomedCT");
    private final static QName _DefinicionRelacionAtributo_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "definicionRelacionAtributo");
    private final static QName _Concepto_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "concepto");
    private final static QName _PeticionRefSetsPorIdDescripcion_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "peticionRefSetsPorIdDescripcion");
    private final static QName _ListaRefSet_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "listaRefSet");
    private final static QName _CrossmapSetMembersDeCrossmapSetResponse_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "crossmapSetMembersDeCrossmapSetResponse");
    private final static QName _Descripcion_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "descripcion");
    private final static QName _RefSetsPorIdDescripcionResponse_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "refSetsPorIdDescripcionResponse");
    private final static QName _RespuestaCategorias_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "respuestaCategorias");
    private final static QName _RefSet_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "refSet");
    private final static QName _Relacion_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "relacion");
    private final static QName _GetCrossmapSetsResponse_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "getCrossmapSetsResponse");
    private final static QName _ConceptoLight_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "conceptoLight");
    private final static QName _PeticionConceptosPorRefSet_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "peticionConceptosPorRefSet");
    private final static QName _DescripcionesPreferidasPorRefSetResponse_QNAME = new QName("http://service.ws.semantikos.minsal.cl/", "descripcionesPreferidasPorRefSetResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cl.minsal.semantikos.ws.shared
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RespuestaCategorias }
     * 
     */
    public RespuestaCategorias createRespuestaCategorias() {
        return new RespuestaCategorias();
    }

    /**
     * Create an instance of {@link RefSet }
     * 
     */
    public RefSet createRefSet() {
        return new RefSet();
    }

    /**
     * Create an instance of {@link Relacion }
     * 
     */
    public Relacion createRelacion() {
        return new Relacion();
    }

    /**
     * Create an instance of {@link CrossmapSetMembersResponse }
     * 
     */
    public CrossmapSetMembersResponse createCrossmapSetMembersResponse() {
        return new CrossmapSetMembersResponse();
    }

    /**
     * Create an instance of {@link DescripcionPendiente }
     * 
     */
    public DescripcionPendiente createDescripcionPendiente() {
        return new DescripcionPendiente();
    }

    /**
     * Create an instance of {@link Concepto }
     * 
     */
    public Concepto createConcepto() {
        return new Concepto();
    }

    /**
     * Create an instance of {@link DefinicionRelacion }
     * 
     */
    public DefinicionRelacion createDefinicionRelacion() {
        return new DefinicionRelacion();
    }

    /**
     * Create an instance of {@link RespuestaRefSets }
     * 
     */
    public RespuestaRefSets createRespuestaRefSets() {
        return new RespuestaRefSets();
    }

    /**
     * Create an instance of {@link Objetivo }
     * 
     */
    public Objetivo createObjetivo() {
        return new Objetivo();
    }

    /**
     * Create an instance of {@link Objetivo.Fields }
     * 
     */
    public Objetivo.Fields createObjetivoFields() {
        return new Objetivo.Fields();
    }

    /**
     * Create an instance of {@link CrossmapSetsResponse }
     * 
     */
    public CrossmapSetsResponse createCrossmapSetsResponse() {
        return new CrossmapSetsResponse();
    }

    /**
     * Create an instance of {@link TablaAuxiliar }
     * 
     */
    public TablaAuxiliar createTablaAuxiliar() {
        return new TablaAuxiliar();
    }

    /**
     * Create an instance of {@link RespuestaConceptosPorCategoria }
     * 
     */
    public RespuestaConceptosPorCategoria createRespuestaConceptosPorCategoria() {
        return new RespuestaConceptosPorCategoria();
    }

    /**
     * Create an instance of {@link DescripcionNoValida }
     * 
     */
    public DescripcionNoValida createDescripcionNoValida() {
        return new DescripcionNoValida();
    }

    /**
     * Create an instance of {@link RespuestaConceptosPorRefSet }
     * 
     */
    public RespuestaConceptosPorRefSet createRespuestaConceptosPorRefSet() {
        return new RespuestaConceptosPorRefSet();
    }

    /**
     * Create an instance of {@link RespuestaBuscarTermino }
     * 
     */
    public RespuestaBuscarTermino createRespuestaBuscarTermino() {
        return new RespuestaBuscarTermino();
    }

    /**
     * Create an instance of {@link IndirectCrossmapsSearch }
     * 
     */
    public IndirectCrossmapsSearch createIndirectCrossmapsSearch() {
        return new IndirectCrossmapsSearch();
    }

    /**
     * Create an instance of {@link RespuestaBuscarTerminoGenerica }
     * 
     */
    public RespuestaBuscarTerminoGenerica createRespuestaBuscarTerminoGenerica() {
        return new RespuestaBuscarTerminoGenerica();
    }

    /**
     * Create an instance of {@link DescripcionPerfectMatch }
     * 
     */
    public DescripcionPerfectMatch createDescripcionPerfectMatch() {
        return new DescripcionPerfectMatch();
    }

    /**
     * Create an instance of {@link BuscarTermino }
     * 
     */
    public BuscarTermino createBuscarTermino() {
        return new BuscarTermino();
    }

    /**
     * Create an instance of {@link PeticionConceptosPedibles }
     * 
     */
    public PeticionConceptosPedibles createPeticionConceptosPedibles() {
        return new PeticionConceptosPedibles();
    }

    /**
     * Create an instance of {@link CrossMapsDirectosPorIDDescripcion }
     * 
     */
    public CrossMapsDirectosPorIDDescripcion createCrossMapsDirectosPorIDDescripcion() {
        return new CrossMapsDirectosPorIDDescripcion();
    }

    /**
     * Create an instance of {@link cl.minsal.semantikos.ws.shared.ColumnaTablaAuxiliar }
     * 
     */
    public cl.minsal.semantikos.ws.shared.ColumnaTablaAuxiliar createColumnaTablaAuxiliar() {
        return new cl.minsal.semantikos.ws.shared.ColumnaTablaAuxiliar();
    }

    /**
     * Create an instance of {@link PeticionPorCategoria }
     * 
     */
    public PeticionPorCategoria createPeticionPorCategoria() {
        return new PeticionPorCategoria();
    }

    /**
     * Create an instance of {@link Paginacion }
     * 
     */
    public Paginacion createPaginacion() {
        return new Paginacion();
    }

    /**
     * Create an instance of {@link CrossMapsIndirectosPorIDDescripcionResponse }
     * 
     */
    public CrossMapsIndirectosPorIDDescripcionResponse createCrossMapsIndirectosPorDescripcionIDorConceptID() {
        return new CrossMapsIndirectosPorIDDescripcionResponse();
    }

    /**
     * Create an instance of {@link DescripcionesPreferidasPorRefSet }
     * 
     */
    public DescripcionesPreferidasPorRefSet createDescripcionesPreferidasPorRefSet() {
        return new DescripcionesPreferidasPorRefSet();
    }

    /**
     * Create an instance of {@link RefSetsPorIdDescripcion }
     * 
     */
    public RefSetsPorIdDescripcion createRefSetsPorIdDescripcion() {
        return new RefSetsPorIdDescripcion();
    }

    /**
     * Create an instance of {@link ConceptoPorIdDescripcionResponse }
     * 
     */
    public ConceptoPorIdDescripcionResponse createConceptoPorIdDescripcionResponse() {
        return new ConceptoPorIdDescripcionResponse();
    }

    /**
     * Create an instance of {@link Atributo }
     * 
     */
    public Atributo createAtributo() {
        return new Atributo();
    }

    /**
     * Create an instance of {@link DescripcionSimplificada }
     * 
     */
    public DescripcionSimplificada createDescripcionSimplificada() {
        return new DescripcionSimplificada();
    }

    /**
     * Create an instance of {@link GetCrossmapSets }
     * 
     */
    public GetCrossmapSets createGetCrossmapSets() {
        return new GetCrossmapSets();
    }

    /**
     * Create an instance of {@link CrossmapSetMember }
     * 
     */
    public CrossmapSetMember createCrossmapSetMember() {
        return new CrossmapSetMember();
    }

    /**
     * Create an instance of {@link ConceptosPorCategoriaResponse }
     * 
     */
    public ConceptosPorCategoriaResponse createConceptosPorCategoriaResponse() {
        return new ConceptosPorCategoriaResponse();
    }

    /**
     * Create an instance of {@link AtributoRelacion }
     * 
     */
    public AtributoRelacion createAtributoRelacion() {
        return new AtributoRelacion();
    }

    /**
     * Create an instance of {@link ConceptosPorCategoria }
     * 
     */
    public ConceptosPorCategoria createConceptosPorCategoria() {
        return new ConceptosPorCategoria();
    }

    /**
     * Create an instance of {@link ListaRefSetResponse }
     * 
     */
    public ListaRefSetResponse createListaRefSetResponse() {
        return new ListaRefSetResponse();
    }

    /**
     * Create an instance of {@link Multiplicidad }
     * 
     */
    public Multiplicidad createMultiplicidad() {
        return new Multiplicidad();
    }

    /**
     * Create an instance of {@link PeticionBuscarTerminoSimple }
     * 
     */
    public PeticionBuscarTerminoSimple createPeticionBuscarTerminoSimple() {
        return new PeticionBuscarTerminoSimple();
    }

    /**
     * Create an instance of {@link ConceptosPorRefSet }
     * 
     */
    public ConceptosPorRefSet createConceptosPorRefSet() {
        return new ConceptosPorRefSet();
    }

    /**
     * Create an instance of {@link CrossmapSet }
     * 
     */
    public CrossmapSet createCrossmapSet() {
        return new CrossmapSet();
    }

    /**
     * Create an instance of {@link Usuario }
     * 
     */
    public Usuario createUsuario() {
        return new Usuario();
    }

    /**
     * Create an instance of {@link PeticionBuscarTermino }
     * 
     */
    public PeticionBuscarTermino createPeticionBuscarTermino() {
        return new PeticionBuscarTermino();
    }

    /**
     * Create an instance of {@link BuscarTruncatePerfectResponse }
     * 
     */
    public BuscarTruncatePerfectResponse createBuscarTruncatePerfectResponse() {
        return new BuscarTruncatePerfectResponse();
    }

    /**
     * Create an instance of {@link IndirectCrossmap }
     * 
     */
    public IndirectCrossmap createIndirectCrossmap() {
        return new IndirectCrossmap();
    }

    /**
     * Create an instance of {@link CrossmapSetMembersDeCrossmapSet }
     * 
     */
    public CrossmapSetMembersDeCrossmapSet createCrossmapSetMembersDeCrossmapSet() {
        return new CrossmapSetMembersDeCrossmapSet();
    }

    /**
     * Create an instance of {@link ObtenerTerminosPedibles }
     * 
     */
    public ObtenerTerminosPedibles createObtenerTerminosPedibles() {
        return new ObtenerTerminosPedibles();
    }

    /**
     * Create an instance of {@link ConceptoPorIdDescripcion }
     * 
     */
    public ConceptoPorIdDescripcion createConceptoPorIdDescripcion() {
        return new ConceptoPorIdDescripcion();
    }

    /**
     * Create an instance of {@link BuscarTruncatePerfect }
     * 
     */
    public BuscarTruncatePerfect createBuscarTruncatePerfect() {
        return new BuscarTruncatePerfect();
    }

    /**
     * Create an instance of {@link ObtenerTerminosPediblesResponse }
     * 
     */
    public ObtenerTerminosPediblesResponse createObtenerTerminosPediblesResponse() {
        return new ObtenerTerminosPediblesResponse();
    }

    /**
     * Create an instance of {@link ListaCategorias }
     * 
     */
    public ListaCategorias createListaCategorias() {
        return new ListaCategorias();
    }

    /**
     * Create an instance of {@link BuscarTerminoResponse }
     * 
     */
    public BuscarTerminoResponse createBuscarTerminoResponse() {
        return new BuscarTerminoResponse();
    }

    /**
     * Create an instance of {@link Categoria }
     * 
     */
    public Categoria createCategoria() {
        return new Categoria();
    }

    /**
     * Create an instance of {@link ListaCategoriasResponse }
     * 
     */
    public ListaCategoriasResponse createListaCategoriasResponse() {
        return new ListaCategoriasResponse();
    }

    /**
     * Create an instance of {@link IllegalInputFault }
     * 
     */
    public IllegalInputFault createIllegalInputFault() {
        return new IllegalInputFault();
    }

    /**
     * Create an instance of {@link NotFoundFault }
     * 
     */
    public NotFoundFault createNotFoundFault() {
        return new NotFoundFault();
    }


    /**
     * Create an instance of {@link  }
     * 
     */
    /*
    public CrossMapsIndirectosPorDescripcionIDorConceptIDResponse createCrossMapsIndirectosPorDescripcionIDorConceptIDResponse() {
        return new CrossMapsIndirectosPorDescripcionIDorConceptIDResponse();
    }
    */

    /**
     * Create an instance of {@link DefinicionObjetivo }
     * 
     */
    public DefinicionObjetivo createDefinicionObjetivo() {
        return new DefinicionObjetivo();
    }

    /**
     * Create an instance of {@link DescriptionIDorConceptIDRequest }
     * 
     */
    public DescriptionIDorConceptIDRequest createDescriptionIDorConceptIDRequest() {
        return new DescriptionIDorConceptIDRequest();
    }

    /**
     * Create an instance of {@link DefinicionRelacionAtributo }
     * 
     */
    public DefinicionRelacionAtributo createDefinicionRelacionAtributo() {
        return new DefinicionRelacionAtributo();
    }

    /**
     * Create an instance of {@link RelacionSnomedCT }
     * 
     */
    public RelacionSnomedCT createRelacionSnomedCT() {
        return new RelacionSnomedCT();
    }

    /**
     * Create an instance of {@link CrossMapsDirectosPorIDDescripcionResponse }
     * 
     */
    public CrossMapsDirectosPorIDDescripcionResponse createCrossMapsDirectosPorIDDescripcionResponse() {
        return new CrossMapsDirectosPorIDDescripcionResponse();
    }

    /**
     * Create an instance of {@link ConceptosPorRefSetResponse }
     * 
     */
    public ConceptosPorRefSetResponse createConceptosPorRefSetResponse() {
        return new ConceptosPorRefSetResponse();
    }

    /**
     * Create an instance of {@link Descripcion }
     * 
     */
    public Descripcion createDescripcion() {
        return new Descripcion();
    }

    /**
     * Create an instance of {@link ListaRefSet }
     * 
     */
    public ListaRefSet createListaRefSet() {
        return new ListaRefSet();
    }

    /**
     * Create an instance of {@link CrossmapSetMembersDeCrossmapSetResponse }
     * 
     */
    public CrossmapSetMembersDeCrossmapSetResponse createCrossmapSetMembersDeCrossmapSetResponse() {
        return new CrossmapSetMembersDeCrossmapSetResponse();
    }

    /**
     * Create an instance of {@link PeticionRefSetsPorIdDescripcion }
     * 
     */
    public PeticionRefSetsPorIdDescripcion createPeticionRefSetsPorIdDescripcion() {
        return new PeticionRefSetsPorIdDescripcion();
    }

    /**
     * Create an instance of {@link RefSetsPorIdDescripcionResponse }
     * 
     */
    public RefSetsPorIdDescripcionResponse createRefSetsPorIdDescripcionResponse() {
        return new RefSetsPorIdDescripcionResponse();
    }

    /**
     * Create an instance of {@link DescripcionesPreferidasPorRefSetResponse }
     * 
     */
    public DescripcionesPreferidasPorRefSetResponse createDescripcionesPreferidasPorRefSetResponse() {
        return new DescripcionesPreferidasPorRefSetResponse();
    }

    /**
     * Create an instance of {@link PeticionConceptosPorRefSet }
     * 
     */
    public PeticionConceptosPorRefSet createPeticionConceptosPorRefSet() {
        return new PeticionConceptosPorRefSet();
    }

    /**
     * Create an instance of {@link ConceptoLight }
     * 
     */
    public ConceptoLight createConceptoLight() {
        return new ConceptoLight();
    }

    /**
     * Create an instance of {@link GetCrossmapSetsResponse }
     * 
     */
    public GetCrossmapSetsResponse createGetCrossmapSetsResponse() {
        return new GetCrossmapSetsResponse();
    }

    /**
     * Create an instance of {@link RespuestaCategorias.Categorias }
     * 
     */
    public RespuestaCategorias.Categorias createRespuestaCategoriasCategorias() {
        return new RespuestaCategorias.Categorias();
    }

    /**
     * Create an instance of {@link RefSet.Conceptos }
     * 
     */
    public RefSet.Conceptos createRefSetConceptos() {
        return new RefSet.Conceptos();
    }

    /**
     * Create an instance of {@link Relacion.Atributos }
     * 
     */
    public Relacion.Atributos createRelacionAtributos() {
        return new Relacion.Atributos();
    }

    /**
     * Create an instance of {@link CrossmapSetMembersResponse.CrossmapSetMembers }
     * 
     */
    public CrossmapSetMembersResponse.CrossmapSetMembers createCrossmapSetMembersResponseCrossmapSetMembers() {
        return new CrossmapSetMembersResponse.CrossmapSetMembers();
    }

    /**
     * Create an instance of {@link DescripcionPendiente.Descripciones }
     * 
     */
    public DescripcionPendiente.Descripciones createDescripcionPendienteDescripciones() {
        return new DescripcionPendiente.Descripciones();
    }

    /**
     * Create an instance of {@link Concepto.RefSets }
     * 
     */
    public Concepto.RefSets createConceptoRefSets() {
        return new Concepto.RefSets();
    }

    /**
     * Create an instance of {@link Concepto.Descripciones }
     * 
     */
    public Concepto.Descripciones createConceptoDescripciones() {
        return new Concepto.Descripciones();
    }

    /**
     * Create an instance of {@link Concepto.Atributos }
     * 
     */
    public Concepto.Atributos createConceptoAtributos() {
        return new Concepto.Atributos();
    }

    /**
     * Create an instance of {@link Concepto.Relaciones }
     * 
     */
    public Concepto.Relaciones createConceptoRelaciones() {
        return new Concepto.Relaciones();
    }

    /**
     * Create an instance of {@link Concepto.RelacionesSnomedCT }
     * 
     */
    public Concepto.RelacionesSnomedCT createConceptoRelacionesSnomedCT() {
        return new Concepto.RelacionesSnomedCT();
    }

    /**
     * Create an instance of {@link Concepto.CrossmapsIndirectos }
     * 
     */
    public Concepto.CrossmapsIndirectos createConceptoCrossmapsIndirectos() {
        return new Concepto.CrossmapsIndirectos();
    }

    /**
     * Create an instance of {@link Concepto.CrossmapsDirectos }
     * 
     */
    public Concepto.CrossmapsDirectos createConceptoCrossmapsDirectos() {
        return new Concepto.CrossmapsDirectos();
    }

    /**
     * Create an instance of {@link DefinicionRelacion.DefinicionesAtributoRelacion }
     * 
     */
    public DefinicionRelacion.DefinicionesAtributoRelacion createDefinicionRelacionDefinicionesAtributoRelacion() {
        return new DefinicionRelacion.DefinicionesAtributoRelacion();
    }

    /**
     * Create an instance of {@link RespuestaRefSets.Refsets }
     * 
     */
    public RespuestaRefSets.Refsets createRespuestaRefSetsRefsets() {
        return new RespuestaRefSets.Refsets();
    }

    /**
     * Create an instance of {@link Objetivo.Fields.Entry }
     * 
     */
    public Objetivo.Fields.Entry createObjetivoFieldsEntry() {
        return new Objetivo.Fields.Entry();
    }

    /**
     * Create an instance of {@link CrossmapSetsResponse.CrossmapSets }
     * 
     */
    public CrossmapSetsResponse.CrossmapSets createCrossmapSetsResponseCrossmapSets() {
        return new CrossmapSetsResponse.CrossmapSets();
    }

    /**
     * Create an instance of {@link TablaAuxiliar.ColumnaTablaAuxiliar }
     * 
     */
    public TablaAuxiliar.ColumnaTablaAuxiliar createTablaAuxiliarColumnaTablaAuxiliar() {
        return new TablaAuxiliar.ColumnaTablaAuxiliar();
    }

    /**
     * Create an instance of {@link RespuestaConceptosPorCategoria.Conceptos }
     * 
     */
    public RespuestaConceptosPorCategoria.Conceptos createRespuestaConceptosPorCategoriaConceptos() {
        return new RespuestaConceptosPorCategoria.Conceptos();
    }

    /**
     * Create an instance of {@link DescripcionNoValida.DescripcionesSugeridas }
     * 
     */
    public DescripcionNoValida.DescripcionesSugeridas createDescripcionNoValidaDescripcionesSugeridas() {
        return new DescripcionNoValida.DescripcionesSugeridas();
    }

    /**
     * Create an instance of {@link RespuestaConceptosPorRefSet.Conceptos }
     * 
     */
    public RespuestaConceptosPorRefSet.Conceptos createRespuestaConceptosPorRefSetConceptos() {
        return new RespuestaConceptosPorRefSet.Conceptos();
    }

    /**
     * Create an instance of {@link RespuestaBuscarTermino.Conceptos }
     * 
     */
    public RespuestaBuscarTermino.Conceptos createRespuestaBuscarTerminoConceptos() {
        return new RespuestaBuscarTermino.Conceptos();
    }

    /**
     * Create an instance of {@link IndirectCrossmapsSearch.IndirectCrossmaps }
     * 
     */
    public IndirectCrossmapsSearch.IndirectCrossmaps createIndirectCrossmapsSearchIndirectCrossmaps() {
        return new IndirectCrossmapsSearch.IndirectCrossmaps();
    }

    /**
     * Create an instance of {@link RespuestaBuscarTerminoGenerica.DescripcionesPerfectMatch }
     * 
     */
    public RespuestaBuscarTerminoGenerica.DescripcionesPerfectMatch createRespuestaBuscarTerminoGenericaDescripcionesPerfectMatch() {
        return new RespuestaBuscarTerminoGenerica.DescripcionesPerfectMatch();
    }

    /**
     * Create an instance of {@link RespuestaBuscarTerminoGenerica.DescripcionesNoValidas }
     * 
     */
    public RespuestaBuscarTerminoGenerica.DescripcionesNoValidas createRespuestaBuscarTerminoGenericaDescripcionesNoValidas() {
        return new RespuestaBuscarTerminoGenerica.DescripcionesNoValidas();
    }

    /**
     * Create an instance of {@link RespuestaBuscarTerminoGenerica.DescripcionesPendientes }
     * 
     */
    public RespuestaBuscarTerminoGenerica.DescripcionesPendientes createRespuestaBuscarTerminoGenericaDescripcionesPendientes() {
        return new RespuestaBuscarTerminoGenerica.DescripcionesPendientes();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaBuscarTerminoGenerica }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "respuestaBuscarTerminoGenerica")
    public JAXBElement<RespuestaBuscarTerminoGenerica> createRespuestaBuscarTerminoGenerica(RespuestaBuscarTerminoGenerica value) {
        return new JAXBElement<RespuestaBuscarTerminoGenerica>(_RespuestaBuscarTerminoGenerica_QNAME, RespuestaBuscarTerminoGenerica.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BuscarTermino }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "buscarTermino")
    public JAXBElement<BuscarTermino> createBuscarTermino(BuscarTermino value) {
        return new JAXBElement<BuscarTermino>(_BuscarTermino_QNAME, BuscarTermino.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DescripcionPerfectMatch }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "descripcionPerfectMatch")
    public JAXBElement<DescripcionPerfectMatch> createDescripcionPerfectMatch(DescripcionPerfectMatch value) {
        return new JAXBElement<DescripcionPerfectMatch>(_DescripcionPerfectMatch_QNAME, DescripcionPerfectMatch.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IndirectCrossmapsSearch }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "indirectCrossmapsSearch")
    public JAXBElement<IndirectCrossmapsSearch> createIndirectCrossmapsSearch(IndirectCrossmapsSearch value) {
        return new JAXBElement<IndirectCrossmapsSearch>(_IndirectCrossmapsSearch_QNAME, IndirectCrossmapsSearch.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PeticionConceptosPedibles }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "conceptosPedibles")
    public JAXBElement<PeticionConceptosPedibles> createConceptosPedibles(PeticionConceptosPedibles value) {
        return new JAXBElement<PeticionConceptosPedibles>(_ConceptosPedibles_QNAME, PeticionConceptosPedibles.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrossMapsDirectosPorIDDescripcion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "crossMapsDirectosPorIDDescripcion")
    public JAXBElement<CrossMapsDirectosPorIDDescripcion> createCrossMapsDirectosPorIDDescripcion(CrossMapsDirectosPorIDDescripcion value) {
        return new JAXBElement<CrossMapsDirectosPorIDDescripcion>(_CrossMapsDirectosPorIDDescripcion_QNAME, CrossMapsDirectosPorIDDescripcion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link cl.minsal.semantikos.ws.shared.ColumnaTablaAuxiliar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "columnaTablaAuxiliar")
    public JAXBElement<cl.minsal.semantikos.ws.shared.ColumnaTablaAuxiliar> createColumnaTablaAuxiliar(cl.minsal.semantikos.ws.shared.ColumnaTablaAuxiliar value) {
        return new JAXBElement<cl.minsal.semantikos.ws.shared.ColumnaTablaAuxiliar>(_ColumnaTablaAuxiliar_QNAME, cl.minsal.semantikos.ws.shared.ColumnaTablaAuxiliar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaBuscarTermino }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "respuestaBuscarTermino")
    public JAXBElement<RespuestaBuscarTermino> createRespuestaBuscarTermino(RespuestaBuscarTermino value) {
        return new JAXBElement<RespuestaBuscarTermino>(_RespuestaBuscarTermino_QNAME, RespuestaBuscarTermino.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConceptoPorIdDescripcionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "conceptoPorIdDescripcionResponse")
    public JAXBElement<ConceptoPorIdDescripcionResponse> createConceptoPorIdDescripcionResponse(ConceptoPorIdDescripcionResponse value) {
        return new JAXBElement<ConceptoPorIdDescripcionResponse>(_ConceptoPorIdDescripcionResponse_QNAME, ConceptoPorIdDescripcionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefSetsPorIdDescripcion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "refSetsPorIdDescripcion")
    public JAXBElement<RefSetsPorIdDescripcion> createRefSetsPorIdDescripcion(RefSetsPorIdDescripcion value) {
        return new JAXBElement<RefSetsPorIdDescripcion>(_RefSetsPorIdDescripcion_QNAME, RefSetsPorIdDescripcion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaConceptosPorRefSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "respuestaConceptosPorRefSet")
    public JAXBElement<RespuestaConceptosPorRefSet> createRespuestaConceptosPorRefSet(RespuestaConceptosPorRefSet value) {
        return new JAXBElement<RespuestaConceptosPorRefSet>(_RespuestaConceptosPorRefSet_QNAME, RespuestaConceptosPorRefSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PeticionPorCategoria }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "peticionPorCategoria")
    public JAXBElement<PeticionPorCategoria> createPeticionPorCategoria(PeticionPorCategoria value) {
        return new JAXBElement<PeticionPorCategoria>(_PeticionPorCategoria_QNAME, PeticionPorCategoria.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Paginacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "paginacion")
    public JAXBElement<Paginacion> createPaginacion(Paginacion value) {
        return new JAXBElement<Paginacion>(_Paginacion_QNAME, Paginacion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrossMapsIndirectosPorDescripcionIDorConceptID }{@code >}}
     * 
     */
    /*
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "crossMapsIndirectosPorDescripcionIDorConceptID")
    public JAXBElement<CrossMapsIndirectosPorDescripcionIDorConceptID> createCrossMapsIndirectosPorDescripcionIDorConceptID(CrossMapsIndirectosPorDescripcionIDorConceptID value) {
        return new JAXBElement<CrossMapsIndirectosPorDescripcionIDorConceptID>(_CrossMapsIndirectosPorDescripcionIDorConceptID_QNAME, CrossMapsIndirectosPorDescripcionIDorConceptID.class, null, value);
    }
    */

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DescripcionesPreferidasPorRefSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "descripcionesPreferidasPorRefSet")
    public JAXBElement<DescripcionesPreferidasPorRefSet> createDescripcionesPreferidasPorRefSet(DescripcionesPreferidasPorRefSet value) {
        return new JAXBElement<DescripcionesPreferidasPorRefSet>(_DescripcionesPreferidasPorRefSet_QNAME, DescripcionesPreferidasPorRefSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DescripcionNoValida }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "descripcionNoValida")
    public JAXBElement<DescripcionNoValida> createDescripcionNoValida(DescripcionNoValida value) {
        return new JAXBElement<DescripcionNoValida>(_DescripcionNoValida_QNAME, DescripcionNoValida.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DescripcionSimplificada }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "descripcionSimplificada")
    public JAXBElement<DescripcionSimplificada> createDescripcionSimplificada(DescripcionSimplificada value) {
        return new JAXBElement<DescripcionSimplificada>(_DescripcionSimplificada_QNAME, DescripcionSimplificada.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Atributo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "atributo")
    public JAXBElement<Atributo> createAtributo(Atributo value) {
        return new JAXBElement<Atributo>(_Atributo_QNAME, Atributo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCrossmapSets }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "getCrossmapSets")
    public JAXBElement<GetCrossmapSets> createGetCrossmapSets(GetCrossmapSets value) {
        return new JAXBElement<GetCrossmapSets>(_GetCrossmapSets_QNAME, GetCrossmapSets.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrossmapSetMember }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "crossmapSetMember")
    public JAXBElement<CrossmapSetMember> createCrossmapSetMember(CrossmapSetMember value) {
        return new JAXBElement<CrossmapSetMember>(_CrossmapSetMember_QNAME, CrossmapSetMember.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListaRefSetResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "listaRefSetResponse")
    public JAXBElement<ListaRefSetResponse> createListaRefSetResponse(ListaRefSetResponse value) {
        return new JAXBElement<ListaRefSetResponse>(_ListaRefSetResponse_QNAME, ListaRefSetResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Multiplicidad }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "multiplicidad")
    public JAXBElement<Multiplicidad> createMultiplicidad(Multiplicidad value) {
        return new JAXBElement<Multiplicidad>(_Multiplicidad_QNAME, Multiplicidad.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConceptosPorCategoria }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "conceptosPorCategoria")
    public JAXBElement<ConceptosPorCategoria> createConceptosPorCategoria(ConceptosPorCategoria value) {
        return new JAXBElement<ConceptosPorCategoria>(_ConceptosPorCategoria_QNAME, ConceptosPorCategoria.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AtributoRelacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "atributoRelacion")
    public JAXBElement<AtributoRelacion> createAtributoRelacion(AtributoRelacion value) {
        return new JAXBElement<AtributoRelacion>(_AtributoRelacion_QNAME, AtributoRelacion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConceptosPorCategoriaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "conceptosPorCategoriaResponse")
    public JAXBElement<ConceptosPorCategoriaResponse> createConceptosPorCategoriaResponse(ConceptosPorCategoriaResponse value) {
        return new JAXBElement<ConceptosPorCategoriaResponse>(_ConceptosPorCategoriaResponse_QNAME, ConceptosPorCategoriaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrossmapSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "crossmapSet")
    public JAXBElement<CrossmapSet> createCrossmapSet(CrossmapSet value) {
        return new JAXBElement<CrossmapSet>(_CrossmapSet_QNAME, CrossmapSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaConceptosPorCategoria }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "respuestaConceptosPorCategoria")
    public JAXBElement<RespuestaConceptosPorCategoria> createRespuestaConceptosPorCategoria(RespuestaConceptosPorCategoria value) {
        return new JAXBElement<RespuestaConceptosPorCategoria>(_RespuestaConceptosPorCategoria_QNAME, RespuestaConceptosPorCategoria.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PeticionBuscarTerminoSimple }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "peticionBuscarTerminoSimple")
    public JAXBElement<PeticionBuscarTerminoSimple> createPeticionBuscarTerminoSimple(PeticionBuscarTerminoSimple value) {
        return new JAXBElement<PeticionBuscarTerminoSimple>(_PeticionBuscarTerminoSimple_QNAME, PeticionBuscarTerminoSimple.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConceptosPorRefSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "conceptosPorRefSet")
    public JAXBElement<ConceptosPorRefSet> createConceptosPorRefSet(ConceptosPorRefSet value) {
        return new JAXBElement<ConceptosPorRefSet>(_ConceptosPorRefSet_QNAME, ConceptosPorRefSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PeticionBuscarTermino }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "peticionBuscarTermino")
    public JAXBElement<PeticionBuscarTermino> createPeticionBuscarTermino(PeticionBuscarTermino value) {
        return new JAXBElement<PeticionBuscarTermino>(_PeticionBuscarTermino_QNAME, PeticionBuscarTermino.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Usuario }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "usuario")
    public JAXBElement<Usuario> createUsuario(Usuario value) {
        return new JAXBElement<Usuario>(_Usuario_QNAME, Usuario.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Objetivo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "objetivo")
    public JAXBElement<Objetivo> createObjetivo(Objetivo value) {
        return new JAXBElement<Objetivo>(_Objetivo_QNAME, Objetivo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrossmapSetsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "crossmapSetsResponse")
    public JAXBElement<CrossmapSetsResponse> createCrossmapSetsResponse(CrossmapSetsResponse value) {
        return new JAXBElement<CrossmapSetsResponse>(_CrossmapSetsResponse_QNAME, CrossmapSetsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TablaAuxiliar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "tablaAuxiliar")
    public JAXBElement<TablaAuxiliar> createTablaAuxiliar(TablaAuxiliar value) {
        return new JAXBElement<TablaAuxiliar>(_TablaAuxiliar_QNAME, TablaAuxiliar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerTerminosPediblesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "obtenerTerminosPediblesResponse")
    public JAXBElement<ObtenerTerminosPediblesResponse> createObtenerTerminosPediblesResponse(ObtenerTerminosPediblesResponse value) {
        return new JAXBElement<ObtenerTerminosPediblesResponse>(_ObtenerTerminosPediblesResponse_QNAME, ObtenerTerminosPediblesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListaCategorias }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "listaCategorias")
    public JAXBElement<ListaCategorias> createListaCategorias(ListaCategorias value) {
        return new JAXBElement<ListaCategorias>(_ListaCategorias_QNAME, ListaCategorias.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BuscarTruncatePerfect }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "buscarTruncatePerfect")
    public JAXBElement<BuscarTruncatePerfect> createBuscarTruncatePerfect(BuscarTruncatePerfect value) {
        return new JAXBElement<BuscarTruncatePerfect>(_BuscarTruncatePerfect_QNAME, BuscarTruncatePerfect.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerTerminosPedibles }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "obtenerTerminosPedibles")
    public JAXBElement<ObtenerTerminosPedibles> createObtenerTerminosPedibles(ObtenerTerminosPedibles value) {
        return new JAXBElement<ObtenerTerminosPedibles>(_ObtenerTerminosPedibles_QNAME, ObtenerTerminosPedibles.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConceptoPorIdDescripcion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "conceptoPorIdDescripcion")
    public JAXBElement<ConceptoPorIdDescripcion> createConceptoPorIdDescripcion(ConceptoPorIdDescripcion value) {
        return new JAXBElement<ConceptoPorIdDescripcion>(_ConceptoPorIdDescripcion_QNAME, ConceptoPorIdDescripcion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BuscarTruncatePerfectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "buscarTruncatePerfectResponse")
    public JAXBElement<BuscarTruncatePerfectResponse> createBuscarTruncatePerfectResponse(BuscarTruncatePerfectResponse value) {
        return new JAXBElement<BuscarTruncatePerfectResponse>(_BuscarTruncatePerfectResponse_QNAME, BuscarTruncatePerfectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IndirectCrossmap }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "indirectCrossmap")
    public JAXBElement<IndirectCrossmap> createIndirectCrossmap(IndirectCrossmap value) {
        return new JAXBElement<IndirectCrossmap>(_IndirectCrossmap_QNAME, IndirectCrossmap.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrossmapSetMembersDeCrossmapSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "crossmapSetMembersDeCrossmapSet")
    public JAXBElement<CrossmapSetMembersDeCrossmapSet> createCrossmapSetMembersDeCrossmapSet(CrossmapSetMembersDeCrossmapSet value) {
        return new JAXBElement<CrossmapSetMembersDeCrossmapSet>(_CrossmapSetMembersDeCrossmapSet_QNAME, CrossmapSetMembersDeCrossmapSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListaCategoriasResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "listaCategoriasResponse")
    public JAXBElement<ListaCategoriasResponse> createListaCategoriasResponse(ListaCategoriasResponse value) {
        return new JAXBElement<ListaCategoriasResponse>(_ListaCategoriasResponse_QNAME, ListaCategoriasResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BuscarTerminoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "buscarTerminoResponse")
    public JAXBElement<BuscarTerminoResponse> createBuscarTerminoResponse(BuscarTerminoResponse value) {
        return new JAXBElement<BuscarTerminoResponse>(_BuscarTerminoResponse_QNAME, BuscarTerminoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Categoria }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "categoria")
    public JAXBElement<Categoria> createCategoria(Categoria value) {
        return new JAXBElement<Categoria>(_Categoria_QNAME, Categoria.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DescriptionIDorConceptIDRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "descriptionIDorConceptIDRequest")
    public JAXBElement<DescriptionIDorConceptIDRequest> createDescriptionIDorConceptIDRequest(DescriptionIDorConceptIDRequest value) {
        return new JAXBElement<DescriptionIDorConceptIDRequest>(_DescriptionIDorConceptIDRequest_QNAME, DescriptionIDorConceptIDRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefinicionRelacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "definicionRelacion")
    public JAXBElement<DefinicionRelacion> createDefinicionRelacion(DefinicionRelacion value) {
        return new JAXBElement<DefinicionRelacion>(_DefinicionRelacion_QNAME, DefinicionRelacion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IllegalInputFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "IllegalInputFault")
    public JAXBElement<IllegalInputFault> createIllegalInputFault(IllegalInputFault value) {
        return new JAXBElement<IllegalInputFault>(_IllegalInputFault_QNAME, IllegalInputFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotFoundFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "NotFoundFault")
    public JAXBElement<NotFoundFault> createNotFoundFault(NotFoundFault value) {
        return new JAXBElement<NotFoundFault>(_NotFoundFault_QNAME, NotFoundFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefinicionObjetivo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "definicionObjetivo")
    public JAXBElement<DefinicionObjetivo> createDefinicionObjetivo(DefinicionObjetivo value) {
        return new JAXBElement<DefinicionObjetivo>(_DefinicionObjetivo_QNAME, DefinicionObjetivo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link /*CrossMapsIndirectosPorDescripcionIDorConceptIDResponse }{@code >}}
     * 
     */
    /*
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "crossMapsIndirectosPorDescripcionIDorConceptIDResponse")
    public JAXBElement<CrossMapsIndirectosPorDescripcionIDorConceptIDResponse> createCrossMapsIndirectosPorDescripcionIDorConceptIDResponse(CrossMapsIndirectosPorDescripcionIDorConceptIDResponse value) {
        return new JAXBElement<CrossMapsIndirectosPorDescripcionIDorConceptIDResponse>(_CrossMapsIndirectosPorDescripcionIDorConceptIDResponse_QNAME, CrossMapsIndirectosPorDescripcionIDorConceptIDResponse.class, null, value);
    }
    */

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaRefSets }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "respuestaRefSets")
    public JAXBElement<RespuestaRefSets> createRespuestaRefSets(RespuestaRefSets value) {
        return new JAXBElement<RespuestaRefSets>(_RespuestaRefSets_QNAME, RespuestaRefSets.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DescripcionPendiente }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "descripcionPendiente")
    public JAXBElement<DescripcionPendiente> createDescripcionPendiente(DescripcionPendiente value) {
        return new JAXBElement<DescripcionPendiente>(_DescripcionPendiente_QNAME, DescripcionPendiente.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrossmapSetMembersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "crossmapSetMembersResponse")
    public JAXBElement<CrossmapSetMembersResponse> createCrossmapSetMembersResponse(CrossmapSetMembersResponse value) {
        return new JAXBElement<CrossmapSetMembersResponse>(_CrossmapSetMembersResponse_QNAME, CrossmapSetMembersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConceptosPorRefSetResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "conceptosPorRefSetResponse")
    public JAXBElement<ConceptosPorRefSetResponse> createConceptosPorRefSetResponse(ConceptosPorRefSetResponse value) {
        return new JAXBElement<ConceptosPorRefSetResponse>(_ConceptosPorRefSetResponse_QNAME, ConceptosPorRefSetResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrossMapsDirectosPorIDDescripcionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "crossMapsDirectosPorIDDescripcionResponse")
    public JAXBElement<CrossMapsDirectosPorIDDescripcionResponse> createCrossMapsDirectosPorIDDescripcionResponse(CrossMapsDirectosPorIDDescripcionResponse value) {
        return new JAXBElement<CrossMapsDirectosPorIDDescripcionResponse>(_CrossMapsDirectosPorIDDescripcionResponse_QNAME, CrossMapsDirectosPorIDDescripcionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RelacionSnomedCT }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "relacionSnomedCT")
    public JAXBElement<RelacionSnomedCT> createRelacionSnomedCT(RelacionSnomedCT value) {
        return new JAXBElement<RelacionSnomedCT>(_RelacionSnomedCT_QNAME, RelacionSnomedCT.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefinicionRelacionAtributo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "definicionRelacionAtributo")
    public JAXBElement<DefinicionRelacionAtributo> createDefinicionRelacionAtributo(DefinicionRelacionAtributo value) {
        return new JAXBElement<DefinicionRelacionAtributo>(_DefinicionRelacionAtributo_QNAME, DefinicionRelacionAtributo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Concepto }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "concepto")
    public JAXBElement<Concepto> createConcepto(Concepto value) {
        return new JAXBElement<Concepto>(_Concepto_QNAME, Concepto.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PeticionRefSetsPorIdDescripcion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "peticionRefSetsPorIdDescripcion")
    public JAXBElement<PeticionRefSetsPorIdDescripcion> createPeticionRefSetsPorIdDescripcion(PeticionRefSetsPorIdDescripcion value) {
        return new JAXBElement<PeticionRefSetsPorIdDescripcion>(_PeticionRefSetsPorIdDescripcion_QNAME, PeticionRefSetsPorIdDescripcion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListaRefSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "listaRefSet")
    public JAXBElement<ListaRefSet> createListaRefSet(ListaRefSet value) {
        return new JAXBElement<ListaRefSet>(_ListaRefSet_QNAME, ListaRefSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrossmapSetMembersDeCrossmapSetResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "crossmapSetMembersDeCrossmapSetResponse")
    public JAXBElement<CrossmapSetMembersDeCrossmapSetResponse> createCrossmapSetMembersDeCrossmapSetResponse(CrossmapSetMembersDeCrossmapSetResponse value) {
        return new JAXBElement<CrossmapSetMembersDeCrossmapSetResponse>(_CrossmapSetMembersDeCrossmapSetResponse_QNAME, CrossmapSetMembersDeCrossmapSetResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Descripcion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "descripcion")
    public JAXBElement<Descripcion> createDescripcion(Descripcion value) {
        return new JAXBElement<Descripcion>(_Descripcion_QNAME, Descripcion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefSetsPorIdDescripcionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "refSetsPorIdDescripcionResponse")
    public JAXBElement<RefSetsPorIdDescripcionResponse> createRefSetsPorIdDescripcionResponse(RefSetsPorIdDescripcionResponse value) {
        return new JAXBElement<RefSetsPorIdDescripcionResponse>(_RefSetsPorIdDescripcionResponse_QNAME, RefSetsPorIdDescripcionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RespuestaCategorias }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "respuestaCategorias")
    public JAXBElement<RespuestaCategorias> createRespuestaCategorias(RespuestaCategorias value) {
        return new JAXBElement<RespuestaCategorias>(_RespuestaCategorias_QNAME, RespuestaCategorias.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "refSet")
    public JAXBElement<RefSet> createRefSet(RefSet value) {
        return new JAXBElement<RefSet>(_RefSet_QNAME, RefSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Relacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "relacion")
    public JAXBElement<Relacion> createRelacion(Relacion value) {
        return new JAXBElement<Relacion>(_Relacion_QNAME, Relacion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCrossmapSetsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "getCrossmapSetsResponse")
    public JAXBElement<GetCrossmapSetsResponse> createGetCrossmapSetsResponse(GetCrossmapSetsResponse value) {
        return new JAXBElement<GetCrossmapSetsResponse>(_GetCrossmapSetsResponse_QNAME, GetCrossmapSetsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConceptoLight }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "conceptoLight")
    public JAXBElement<ConceptoLight> createConceptoLight(ConceptoLight value) {
        return new JAXBElement<ConceptoLight>(_ConceptoLight_QNAME, ConceptoLight.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PeticionConceptosPorRefSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "peticionConceptosPorRefSet")
    public JAXBElement<PeticionConceptosPorRefSet> createPeticionConceptosPorRefSet(PeticionConceptosPorRefSet value) {
        return new JAXBElement<PeticionConceptosPorRefSet>(_PeticionConceptosPorRefSet_QNAME, PeticionConceptosPorRefSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DescripcionesPreferidasPorRefSetResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.semantikos.minsal.cl/", name = "descripcionesPreferidasPorRefSetResponse")
    public JAXBElement<DescripcionesPreferidasPorRefSetResponse> createDescripcionesPreferidasPorRefSetResponse(DescripcionesPreferidasPorRefSetResponse value) {
        return new JAXBElement<DescripcionesPreferidasPorRefSetResponse>(_DescripcionesPreferidasPorRefSetResponse_QNAME, DescripcionesPreferidasPorRefSetResponse.class, null, value);
    }

}
