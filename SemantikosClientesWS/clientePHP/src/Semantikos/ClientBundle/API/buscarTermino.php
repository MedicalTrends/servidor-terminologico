<?php

namespace Semantikos\ClientBundle\API;

class buscarTermino
{

    /**
     * @var PeticionBuscarTerminoSimple $peticionBuscarTermino
     */
    protected $peticionBuscarTermino = null;

    /**
     * @param PeticionBuscarTerminoSimple $peticionBuscarTermino
     */
    public function __construct($peticionBuscarTermino)
    {
      $this->peticionBuscarTermino = $peticionBuscarTermino;
    }

    /**
     * @return PeticionBuscarTerminoSimple
     */
    public function getPeticionBuscarTermino()
    {
      return $this->peticionBuscarTermino;
    }

    /**
     * @param PeticionBuscarTerminoSimple $peticionBuscarTermino
     * @return buscarTermino
     */
    public function setPeticionBuscarTermino($peticionBuscarTermino)
    {
      $this->peticionBuscarTermino = $peticionBuscarTermino;
      return $this;
    }

}
