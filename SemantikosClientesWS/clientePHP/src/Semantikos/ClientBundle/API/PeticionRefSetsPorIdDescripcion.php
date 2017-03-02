<?php

namespace Semantikos\ClientBundle\API;

class PeticionRefSetsPorIdDescripcion
{

    /**
     * @var string $idDescripcion
     */
    protected $idDescripcion = null;

    /**
     * @var boolean $incluyeEstablecimientos
     */
    protected $incluyeEstablecimientos = null;

    /**
     * @var string $idStablishment
     */
    protected $idStablishment = null;

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
    public function getIdDescripcion()
    {
      return $this->idDescripcion;
    }

    /**
     * @param string $idDescripcion
     * @return PeticionRefSetsPorIdDescripcion
     */
    public function setIdDescripcion($idDescripcion)
    {
      $this->idDescripcion = $idDescripcion;
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
    public function getIdStablishment()
    {
      return $this->idStablishment;
    }

    /**
     * @param string $idStablishment
     * @return PeticionRefSetsPorIdDescripcion
     */
    public function setIdStablishment($idStablishment)
    {
      $this->idStablishment = $idStablishment;
      return $this;
    }

}
