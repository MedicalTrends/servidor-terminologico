<?php

namespace Semantikos\ClientBundle\API;

class ServicioDeBusqueda extends \SoapClient
{

    /**
     * @var array $classmap The defined classes
     */
    private static $classmap = array (
  'getCrossmapSets' => '\\getCrossmapSets',
  'getCrossmapSetsResponse' => '\\getCrossmapSetsResponse',
  'CrossmapSetsResponse' => '\\CrossmapSetsResponse',
  'crossmapSets' => '\\crossmapSets',
  'CrossmapSet' => '\\CrossmapSet',
  'crossmapSetMembersDeCrossmapSet' => '\\crossmapSetMembersDeCrossmapSet',
  'crossmapSetMembersDeCrossmapSetResponse' => '\\crossmapSetMembersDeCrossmapSetResponse',
  'CrossmapSetMembersResponse' => '\\CrossmapSetMembersResponse',
  'crossmapSetMembers' => '\\crossmapSetMembers',
  'CrossmapSetMember' => '\\CrossmapSetMember',
  'conceptoPorIdDescripcion' => '\\conceptoPorIdDescripcion',
  'conceptoPorIdDescripcionResponse' => '\\conceptoPorIdDescripcionResponse',
  'Concepto' => '\\Concepto',
  'refSets' => '\\refSets',
  'descripciones' => '\\descripciones',
  'atributos' => '\\atributos',
  'relaciones' => '\\relaciones',
  'relacionesSnomedCT' => '\\relacionesSnomedCT',
  'crossmapsIndirectos' => '\\crossmapsIndirectos',
  'crossmapsDirectos' => '\\crossmapsDirectos',
  'Categoria' => '\\Categoria',
  'RefSet' => '\\RefSet',
  'conceptos' => '\\conceptos',
  'Descripcion' => '\\Descripcion',
  'Usuario' => '\\Usuario',
  'Atributo' => '\\Atributo',
  'Relacion' => '\\Relacion',
  'Objetivo' => '\\Objetivo',
  'fields' => '\\fields',
  'entry' => '\\entry',
  'TablaAuxiliar' => '\\TablaAuxiliar',
  'columnaTablaAuxiliar' => '\\columnaTablaAuxiliar',
  'ColumnaTablaAuxiliar' => '\\ColumnaTablaAuxiliar',
  'DefinicionRelacion' => '\\DefinicionRelacion',
  'definicionesAtributoRelacion' => '\\definicionesAtributoRelacion',
  'Multiplicidad' => '\\Multiplicidad',
  'DefinicionObjetivo' => '\\DefinicionObjetivo',
  'DefinicionRelacionAtributo' => '\\DefinicionRelacionAtributo',
  'AtributoRelacion' => '\\AtributoRelacion',
  'RelacionSnomedCT' => '\\RelacionSnomedCT',
  'IndirectCrossmap' => '\\IndirectCrossmap',
  'listaRefSet' => '\\listaRefSet',
  'listaRefSetResponse' => '\\listaRefSetResponse',
  'RespuestaRefSets' => '\\RespuestaRefSets',
  'refsets' => '\\refsets',
  'conceptosPorRefSet' => '\\conceptosPorRefSet',
  'PeticionConceptosPorRefSet' => '\\PeticionConceptosPorRefSet',
  'conceptosPorRefSetResponse' => '\\conceptosPorRefSetResponse',
  'RespuestaConceptosPorRefSet' => '\\RespuestaConceptosPorRefSet',
  'Paginacion' => '\\Paginacion',
  'ConceptoLight' => '\\ConceptoLight',
  'buscarTruncatePerfect' => '\\buscarTruncatePerfect',
  'PeticionBuscarTermino' => '\\PeticionBuscarTermino',
  'buscarTruncatePerfectResponse' => '\\buscarTruncatePerfectResponse',
  'RespuestaConceptosPorCategoria' => '\\RespuestaConceptosPorCategoria',
  'refSetsPorIdDescripcion' => '\\refSetsPorIdDescripcion',
  'PeticionRefSetsPorIdDescripcion' => '\\PeticionRefSetsPorIdDescripcion',
  'refSetsPorIdDescripcionResponse' => '\\refSetsPorIdDescripcionResponse',
  'listaCategorias' => '\\listaCategorias',
  'listaCategoriasResponse' => '\\listaCategoriasResponse',
  'crossMapsDirectosPorIDDescripcion' => '\\crossMapsDirectosPorIDDescripcion',
  'crossMapsDirectosPorIDDescripcionResponse' => '\\crossMapsDirectosPorIDDescripcionResponse',
  'conceptosPorCategoria' => '\\conceptosPorCategoria',
  'PeticionPorCategoria' => '\\PeticionPorCategoria',
  'conceptosPorCategoriaResponse' => '\\conceptosPorCategoriaResponse',
  'descripcionesPreferidasPorRefSet' => '\\descripcionesPreferidasPorRefSet',
  'descripcionesPreferidasPorRefSetResponse' => '\\descripcionesPreferidasPorRefSetResponse',
  'obtenerTerminosPedibles' => '\\obtenerTerminosPedibles',
  'PeticionConceptosPedibles' => '\\PeticionConceptosPedibles',
  'obtenerTerminosPediblesResponse' => '\\obtenerTerminosPediblesResponse',
  'RespuestaBuscarTermino' => '\\RespuestaBuscarTermino',
  'buscarTermino' => '\\buscarTermino',
  'PeticionBuscarTerminoSimple' => '\\PeticionBuscarTerminoSimple',
  'buscarTerminoResponse' => '\\buscarTerminoResponse',
  'RespuestaBuscarTerminoGenerica' => '\\RespuestaBuscarTerminoGenerica',
  'descripcionesPerfectMatch' => '\\descripcionesPerfectMatch',
  'descripcionesNoValidas' => '\\descripcionesNoValidas',
  'descripcionesPendientes' => '\\descripcionesPendientes',
  'DescripcionPerfectMatch' => '\\DescripcionPerfectMatch',
  'DescripcionNoValida' => '\\DescripcionNoValida',
  'descripcionesSugeridas' => '\\descripcionesSugeridas',
  'DescripcionPendiente' => '\\DescripcionPendiente',
  'DescripcionSimplificada' => '\\DescripcionSimplificada',
  'crossMapsIndirectosPorIDDescripcion' => '\\crossMapsIndirectosPorIDDescripcion',
  'crossMapsIndirectosPorIDDescripcionResponse' => '\\crossMapsIndirectosPorIDDescripcionResponse',
  'IndirectCrossmapsSearch' => '\\IndirectCrossmapsSearch',
  'indirectCrossmaps' => '\\indirectCrossmaps',
  'NotFoundFault' => '\\NotFoundFault',
  'IllegalInputFault' => '\\IllegalInputFault',
);

    /**
     * @param array $options A array of config values
     * @param string $wsdl The wsdl file to use
     */
    public function __construct(array $options = array(), $wsdl = null)
    {
    
  foreach (self::$classmap as $key => $value) {
    if (!isset($options['classmap'][$key])) {
      $options['classmap'][$key] = $value;
    }
  }
      $options = array_merge(array (
  'features' => 1,
), $options);
      if (!$wsdl) {
        $wsdl = 'http://192.168.0.226:8080/ws/ServicioDeBusqueda?wsdl';
      }
      parent::__construct($wsdl, $options);
    }

    /**
     * @param crossmapSetMembersDeCrossmapSet $parameters
     * @return crossmapSetMembersDeCrossmapSetResponse
     */
    public function crossmapSetMembersDeCrossmapSet(crossmapSetMembersDeCrossmapSet $parameters)
    {
      return $this->__soapCall('crossmapSetMembersDeCrossmapSet', array($parameters));
    }

    /**
     * @param getCrossmapSets $parameters
     * @return getCrossmapSetsResponse
     */
    public function getCrossmapSets(getCrossmapSets $parameters)
    {
      return $this->__soapCall('getCrossmapSets', array($parameters));
    }

    /**
     * @param conceptoPorIdDescripcion $parameters
     * @return conceptoPorIdDescripcionResponse
     */
    public function conceptoPorIdDescripcion(conceptoPorIdDescripcion $parameters)
    {
      return $this->__soapCall('conceptoPorIdDescripcion', array($parameters));
    }

    /**
     * @param listaRefSet $parameters
     * @return listaRefSetResponse
     */
    public function listaRefSet(listaRefSet $parameters)
    {
      return $this->__soapCall('listaRefSet', array($parameters));
    }

    /**
     * @param buscarTruncatePerfect $parameters
     * @return buscarTruncatePerfectResponse
     */
    public function buscarTruncatePerfect(buscarTruncatePerfect $parameters)
    {
      return $this->__soapCall('buscarTruncatePerfect', array($parameters));
    }

    /**
     * @param conceptosPorRefSet $parameters
     * @return conceptosPorRefSetResponse
     */
    public function conceptosPorRefSet(conceptosPorRefSet $parameters)
    {
      return $this->__soapCall('conceptosPorRefSet', array($parameters));
    }

    /**
     * @param listaCategorias $parameters
     * @return listaCategoriasResponse
     */
    public function listaCategorias(listaCategorias $parameters)
    {
      return $this->__soapCall('listaCategorias', array($parameters));
    }

    /**
     * @param refSetsPorIdDescripcion $parameters
     * @return refSetsPorIdDescripcionResponse
     */
    public function refSetsPorIdDescripcion(refSetsPorIdDescripcion $parameters)
    {
      return $this->__soapCall('refSetsPorIdDescripcion', array($parameters));
    }

    /**
     * @param crossMapsDirectosPorIDDescripcion $parameters
     * @return crossMapsDirectosPorIDDescripcionResponse
     */
    public function crossMapsDirectosPorIDDescripcion(crossMapsDirectosPorIDDescripcion $parameters)
    {
      return $this->__soapCall('crossMapsDirectosPorIDDescripcion', array($parameters));
    }

    /**
     * @param conceptosPorCategoria $parameters
     * @return conceptosPorCategoriaResponse
     */
    public function conceptosPorCategoria(conceptosPorCategoria $parameters)
    {
      return $this->__soapCall('conceptosPorCategoria', array($parameters));
    }

    /**
     * @param descripcionesPreferidasPorRefSet $parameters
     * @return descripcionesPreferidasPorRefSetResponse
     */
    public function descripcionesPreferidasPorRefSet(descripcionesPreferidasPorRefSet $parameters)
    {
      return $this->__soapCall('descripcionesPreferidasPorRefSet', array($parameters));
    }

    /**
     * @param obtenerTerminosPedibles $parameters
     * @return obtenerTerminosPediblesResponse
     */
    public function obtenerTerminosPedibles(obtenerTerminosPedibles $parameters)
    {
      return $this->__soapCall('obtenerTerminosPedibles', array($parameters));
    }

    /**
     * @param buscarTermino $parameters
     * @return buscarTerminoResponse
     */
    public function buscarTermino(buscarTermino $parameters)
    {
      return $this->__soapCall('buscarTermino', array($parameters));
    }

    /**
     * @param crossMapsIndirectosPorIDDescripcion $parameters
     * @return crossMapsIndirectosPorIDDescripcionResponse
     */
    public function crossMapsIndirectosPorIDDescripcion(crossMapsIndirectosPorIDDescripcion $parameters)
    {
      return $this->__soapCall('crossMapsIndirectosPorIDDescripcion', array($parameters));
    }

}
