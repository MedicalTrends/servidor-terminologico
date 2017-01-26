<?php

namespace Semantikos\ClientBundle\API;

class listaRefSet
{

    /**
     * @var boolean $incluyeEstablecimientos
     */
    protected $incluyeEstablecimientos = null;

    /**
     * @var string $idStablishment
     */
    protected $idStablishment = null;

    /**
     * @param string $idStablishment
     */
    public function __construct($idStablishment)
    {
      $this->idStablishment = $idStablishment;
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
     * @return listaRefSet
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
     * @return listaRefSet
     */
    public function setIdStablishment($idStablishment)
    {
      $this->idStablishment = $idStablishment;
      return $this;
    }

}
