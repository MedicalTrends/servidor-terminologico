<?php

namespace Semantikos\ClientBundle\API;

class RespuestaConceptosPorRefSet
{

    /**
     * @var RefSet $refSet
     */
    protected $refSet = null;

    /**
     * @var Paginacion $paginacion
     */
    protected $paginacion = null;

    /**
     * @var conceptos $conceptos
     */
    protected $conceptos = null;

    /**
     * @param RefSet $refSet
     * @param Paginacion $paginacion
     */
    public function __construct($refSet, $paginacion)
    {
      $this->refSet = $refSet;
      $this->paginacion = $paginacion;
    }

    /**
     * @return RefSet
     */
    public function getRefSet()
    {
      return $this->refSet;
    }

    /**
     * @param RefSet $refSet
     * @return RespuestaConceptosPorRefSet
     */
    public function setRefSet($refSet)
    {
      $this->refSet = $refSet;
      return $this;
    }

    /**
     * @return Paginacion
     */
    public function getPaginacion()
    {
      return $this->paginacion;
    }

    /**
     * @param Paginacion $paginacion
     * @return RespuestaConceptosPorRefSet
     */
    public function setPaginacion($paginacion)
    {
      $this->paginacion = $paginacion;
      return $this;
    }

    /**
     * @return conceptos
     */
    public function getConceptos()
    {
      return $this->conceptos;
    }

    /**
     * @param conceptos $conceptos
     * @return RespuestaConceptosPorRefSet
     */
    public function setConceptos($conceptos)
    {
      $this->conceptos = $conceptos;
      return $this;
    }

}
