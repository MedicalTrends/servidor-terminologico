<?php

namespace Semantikos\ClientBundle\API;

class obtenerTerminosPedibles
{

    /**
     * @var PeticionConceptosPedibles $peticionObtenerTerminosPedibles
     */
    protected $peticionObtenerTerminosPedibles = null;

    /**
     * @param PeticionConceptosPedibles $peticionObtenerTerminosPedibles
     */
    public function __construct($peticionObtenerTerminosPedibles)
    {
      $this->peticionObtenerTerminosPedibles = $peticionObtenerTerminosPedibles;
    }

    /**
     * @return PeticionConceptosPedibles
     */
    public function getPeticionObtenerTerminosPedibles()
    {
      return $this->peticionObtenerTerminosPedibles;
    }

    /**
     * @param PeticionConceptosPedibles $peticionObtenerTerminosPedibles
     * @return obtenerTerminosPedibles
     */
    public function setPeticionObtenerTerminosPedibles($peticionObtenerTerminosPedibles)
    {
      $this->peticionObtenerTerminosPedibles = $peticionObtenerTerminosPedibles;
      return $this;
    }

}
