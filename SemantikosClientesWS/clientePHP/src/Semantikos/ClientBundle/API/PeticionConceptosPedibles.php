<?php

namespace Semantikos\ClientBundle\API;

class PeticionConceptosPedibles
{

    /**
     * @var string $pedible
     */
    protected $pedible = null;

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
     * @param string $pedible
     * @param string $idEstablecimiento
     */
    public function __construct()
    {      
    }

    /**
     * @return string
     */
    public function getPedible()
    {
      return $this->pedible;
    }

    /**
     * @param string $pedible
     * @return PeticionConceptosPedibles
     */
    public function setPedible($pedible)
    {
      $this->pedible = $pedible;
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
     * @return PeticionConceptosPedibles
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
     * @return PeticionConceptosPedibles
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
     * @return PeticionConceptosPedibles
     */
    public function setIdEstablecimiento($idEstablecimiento)
    {
      $this->idEstablecimiento = $idEstablecimiento;
      return $this;
    }

}
