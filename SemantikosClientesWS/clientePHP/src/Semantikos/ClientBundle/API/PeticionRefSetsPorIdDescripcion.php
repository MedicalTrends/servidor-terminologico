<?php

namespace Semantikos\ClientBundle\API;

class PeticionRefSetsPorIdDescripcion
{

    /**
     * @var string $idDescripcion
     */
    protected $descriptionID = null;

    /**
     * @var boolean $incluyeEstablecimientos
     */
    protected $incluyeEstablecimientos = null;

    /**
     * @var string $idStablishment
     */
    protected $idEstablecimiento = null;

    /**
     * @param string $idDescripcion
     * @param string $idStablishment
     */
    public function __construct()
    {      
    }

    /**
     * @return string
     */
    public function getDescriptionID()
    {
      return $this->descriptionID;
    }

    /**
     * @param string $idDescripcion
     * @return PeticionRefSetsPorIdDescripcion
     */
    public function setDescriptionID($descriptionID)
    {
      $this->descriptionID = $descriptionID;
      return $this;
    }

    /**
     * @return boolean
     */
    public function getIncluyeEstablecimientos()
    {
      return $this->incluyeEstablecimientos;
    }

    /**
     * @param boolean $incluyeEstablecimientos
     * @return PeticionRefSetsPorIdDescripcion
     */
    public function setIncluyeEstablecimientos($incluyeEstablecimientos)
    {
      $this->incluyeEstablecimientos = $incluyeEstablecimientos;
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
     * @param string $idStablishment
     * @return PeticionRefSetsPorIdDescripcion
     */
    public function setIdEstablecimiento($idEstablecimiento)
    {
      $this->idEstablecimiento = $idEstablecimiento;
      return $this;
    }

}
