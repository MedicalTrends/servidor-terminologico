<?php

namespace Semantikos\ClientBundle\API;

class RespuestaConceptosPorCategoria
{

    /**
     * @var conceptos $conceptos
     */
    public $conceptos = null;

    /**
     * @var int $cantidadRegistros
     */
    public $cantidadRegistros = null;

    /**
     * @param int $cantidadRegistros
     */
    public function __construct($cantidadRegistros)
    {
      $this->cantidadRegistros = $cantidadRegistros;
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
     * @return RespuestaConceptosPorCategoria
     */
    public function setConceptos($conceptos)
    {
      $this->conceptos = $conceptos;
      return $this;
    }

    /**
     * @return int
     */
    public function getCantidadRegistros()
    {
      return $this->cantidadRegistros;
    }

    /**
     * @param int $cantidadRegistros
     * @return RespuestaConceptosPorCategoria
     */
    public function setCantidadRegistros($cantidadRegistros)
    {
      $this->cantidadRegistros = $cantidadRegistros;
      return $this;
    }

}
