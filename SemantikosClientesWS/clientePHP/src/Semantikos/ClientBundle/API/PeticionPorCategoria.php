<?php

namespace Semantikos\ClientBundle\API;

class PeticionPorCategoria
{

    /**
     * @var string $nombreCategoria
     */
    protected $nombreCategoria = null;

    /**
     * @var int $numeroPagina
     */
    protected $numeroPagina = null;

    /**
     * @var int $tamanoPagina
     */
    protected $tamanoPagina = null;

    /**
     * @var string $idEstablecimiento
     */
    protected $idEstablecimiento = null;
        
    /**
     * @param string $nombreCategoria
     * @param string $idEstablecimiento
     */
    public function __construct()
    {
    }

    /**
     * @return int
     */
    public function getNumeroPagina()
    {
        return $this->numeroPagina;
    }

    /**
     * @param int $numeroPagina
     */
    public function setNumeroPagina($numeroPagina)
    {
        $this->numeroPagina = $numeroPagina;
    }

    /**
     * @return int
     */
    public function getTamanoPagina()
    {
        return $this->tamanoPagina;
    }

    /**
     * @param int $tamanoPagina
     */
    public function setTamanoPagina($tamanoPagina)
    {
        $this->tamanoPagina = $tamanoPagina;
    }

    /**
     * @return string
     */
    public function getNombreCategoria()
    {
      return $this->nombreCategoria;
    }

    /**
     * @param string $nombreCategoria
     * @return PeticionPorCategoria
     */
    public function setNombreCategoria($nombreCategoria)
    {
      $this->nombreCategoria = $nombreCategoria;
      return $this;
    }

    /**
     * @return string
     */
    public function getIdEstablecimiento()
    {
      return $this->idEstablecimiento;
    }

    /**
     * @param string $idEstablecimiento
     * @return PeticionPorCategoria
     */
    public function setIdEstablecimiento($idEstablecimiento)
    {
      $this->idEstablecimiento = $idEstablecimiento;
      return $this;
    }

}
