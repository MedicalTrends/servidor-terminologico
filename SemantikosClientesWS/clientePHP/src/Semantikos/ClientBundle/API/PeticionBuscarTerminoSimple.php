<?php

namespace Semantikos\ClientBundle\API;

class PeticionBuscarTerminoSimple
{

    /**
     * @var string $termino
     */
    protected $terminoBuscar = null;

    /**
     * @var string[] $nombreCategoria
     */
    protected $nombreCategoria = null;

    /**
     * @var string[] $nombreRefSet
     */
    protected $nombreRefSet = null;

    /**
     * @var string $idEstablecimiento
     */
    protected $idEstablecimiento = null;

    /**
     * @param string $termino
     */
    public function __construct()
    {      
    }

    /**
     * @return string
     */
    public function getTermino()
    {
      return $this->terminoBuscar;
    }

    /**
     * @param string $termino
     * @return PeticionBuscarTerminoSimple
     */
    public function setTermino($termino)
    {
      $this->terminoBuscar = $termino;
      return $this;
    }

    /**
     * @return string[]
     */
    public function getNombreCategoria()
    {
      return $this->nombreCategoria;
    }

    /**
     * @param string[] $nombreCategoria
     * @return PeticionBuscarTerminoSimple
     */
    public function setNombreCategoria(array $nombreCategoria = null)
    {
      $this->nombreCategoria = $nombreCategoria;
      return $this;
    }

    /**
     * @return string[]
     */
    public function getNombreRefSet()
    {
      return $this->nombreRefSet;
    }

    /**
     * @param string[] $nombreRefSet
     * @return PeticionBuscarTerminoSimple
     */
    public function setNombreRefSet(array $nombreRefSet = null)
    {
      $this->nombreRefSet = $nombreRefSet;
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
     * @return PeticionBuscarTermino
     */
    public function setIdEstablecimiento($idEstablecimiento)
    {
        $this->idEstablecimiento = $idEstablecimiento;
        return $this;
    }

}
