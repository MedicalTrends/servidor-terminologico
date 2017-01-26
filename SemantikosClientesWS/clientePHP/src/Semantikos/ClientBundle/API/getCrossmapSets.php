<?php

namespace Semantikos\ClientBundle\API;

class getCrossmapSets
{

    /**
     * @var string $idInstitucion
     */
    protected $idInstitucion = null;

    /**
     * @param string $idInstitucion
     */
    public function __construct($idInstitucion)
    {
      $this->idInstitucion = $idInstitucion;
    }

    /**
     * @return string
     */
    public function getIdInstitucion()
    {
      return $this->idInstitucion;
    }

    /**
     * @param string $idInstitucion
     * @return getCrossmapSets
     */
    public function setIdInstitucion($idInstitucion)
    {
      $this->idInstitucion = $idInstitucion;
      return $this;
    }

}
