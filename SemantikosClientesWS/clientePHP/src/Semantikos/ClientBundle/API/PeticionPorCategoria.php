<?php

namespace Semantikos\ClientBundle\API;

class PeticionPorCategoria
{

    /**
     * @var string $nombreCategoria
     */
    protected $nombreCategoria = null;

    /**
     * @var string $idEstablecimiento
     */
    protected $idEstablecimiento = null;
        
    /**
     * @param string $nombreCategoria
     * @param string $idEstablecimiento
     */
    public function __construct($nombreCategoria, $idEstablecimiento)
    {
      $this->nombreCategoria = $nombreCategoria;
      $this->idEstablecimiento = $idEstablecimiento;
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
