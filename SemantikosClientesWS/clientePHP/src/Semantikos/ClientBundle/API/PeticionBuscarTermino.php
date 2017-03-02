<?php

namespace Semantikos\ClientBundle\API;

class PeticionBuscarTermino
{

    /**
     * @var string $termino
     */
    protected $termino = null;

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
     * @param string $idEstablecimiento
     */
    public function __construct()
    {
    }

    /**
     * @return string
     */
    public function getTermino()
    {
      return $this->termino;
    }

    /**
     * @param string $termino
     * @return PeticionBuscarTermino
     */
    public function setTermino($termino)
    {
      $this->termino = $termino;
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
     * @return PeticionBuscarTermino
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
     * @return PeticionBuscarTermino
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
