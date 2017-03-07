<?php

namespace Semantikos\ClientBundle\API;

class PeticionConceptosPorRefSet
{

    /**
     * @var string $nombreRefSet
     */
    protected $nombreRefSet = null;

    /**
     * @var string $idEstablecimiento
     */
    protected $idEstablecimiento = null;

    /**
     * @param string $nombreRefSet
     * @param string $idEstablecimiento
     */
    public function __construct()
    {      
    }

    /**
     * @return string
     */
    public function getNombreRefSet()
    {
      return $this->nombreRefSet;
    }

    /**
     * @param string $nombreRefSet
     * @return PeticionConceptosPorRefSet
     */
    public function setNombreRefSet($nombreRefSet)
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
     * @return PeticionConceptosPorRefSet
     */
    public function setIdEstablecimiento($idEstablecimiento)
    {
      $this->idEstablecimiento = $idEstablecimiento;
      return $this;
    }

}
