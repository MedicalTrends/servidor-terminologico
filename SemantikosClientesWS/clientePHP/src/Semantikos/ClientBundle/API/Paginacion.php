<?php

namespace Semantikos\ClientBundle\API;

class Paginacion
{

    /**
     * @var int $totalRegistros
     */
    protected $totalRegistros = null;

    /**
     * @var int $paginaActual
     */
    protected $paginaActual = null;

    /**
     * @var int $registrosPorPagina
     */
    protected $registrosPorPagina = null;

    /**
     * @var int $mostrandoDesde
     */
    protected $mostrandoDesde = null;

    /**
     * @var int $mostrandoHasta
     */
    protected $mostrandoHasta = null;

    
    public function __construct()
    {
    
    }

    /**
     * @return int
     */
    public function getTotalRegistros()
    {
      return $this->totalRegistros;
    }

    /**
     * @param int $totalRegistros
     * @return Paginacion
     */
    public function setTotalRegistros($totalRegistros)
    {
      $this->totalRegistros = $totalRegistros;
      return $this;
    }

    /**
     * @return int
     */
    public function getPaginaActual()
    {
      return $this->paginaActual;
    }

    /**
     * @param int $paginaActual
     * @return Paginacion
     */
    public function setPaginaActual($paginaActual)
    {
      $this->paginaActual = $paginaActual;
      return $this;
    }

    /**
     * @return int
     */
    public function getRegistrosPorPagina()
    {
      return $this->registrosPorPagina;
    }

    /**
     * @param int $registrosPorPagina
     * @return Paginacion
     */
    public function setRegistrosPorPagina($registrosPorPagina)
    {
      $this->registrosPorPagina = $registrosPorPagina;
      return $this;
    }

    /**
     * @return int
     */
    public function getMostrandoDesde()
    {
      return $this->mostrandoDesde;
    }

    /**
     * @param int $mostrandoDesde
     * @return Paginacion
     */
    public function setMostrandoDesde($mostrandoDesde)
    {
      $this->mostrandoDesde = $mostrandoDesde;
      return $this;
    }

    /**
     * @return int
     */
    public function getMostrandoHasta()
    {
      return $this->mostrandoHasta;
    }

    /**
     * @param int $mostrandoHasta
     * @return Paginacion
     */
    public function setMostrandoHasta($mostrandoHasta)
    {
      $this->mostrandoHasta = $mostrandoHasta;
      return $this;
    }

}
