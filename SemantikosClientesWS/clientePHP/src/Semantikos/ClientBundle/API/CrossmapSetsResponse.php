<?php

namespace Semantikos\ClientBundle\API;

class CrossmapSetsResponse
{

    /**
     * @var int $cantidadRegistros
     */
    protected $cantidadRegistros = null;

    /**
     * @var crossmapSets $crossmapSets
     */
    protected $crossmapSets = null;

    /**
     * @param int $cantidadRegistros
     */
    public function __construct($cantidadRegistros)
    {
      $this->cantidadRegistros = $cantidadRegistros;
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
     * @return CrossmapSetsResponse
     */
    public function setCantidadRegistros($cantidadRegistros)
    {
      $this->cantidadRegistros = $cantidadRegistros;
      return $this;
    }

    /**
     * @return crossmapSets
     */
    public function getCrossmapSets()
    {
      return $this->crossmapSets;
    }

    /**
     * @param crossmapSets $crossmapSets
     * @return CrossmapSetsResponse
     */
    public function setCrossmapSets($crossmapSets)
    {
      $this->crossmapSets = $crossmapSets;
      return $this;
    }

}
