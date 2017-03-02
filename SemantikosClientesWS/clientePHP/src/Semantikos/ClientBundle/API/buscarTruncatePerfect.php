<?php

namespace Semantikos\ClientBundle\API;

class buscarTruncatePerfect
{

    /**
     * @var PeticionBuscarTermino $peticionBuscarTermino
     */
    protected $peticionBuscarTermino = null;

    /**
     * @param PeticionBuscarTermino $peticionBuscarTermino
     */
    public function __construct($peticionBuscarTermino)
    {
      $this->peticionBuscarTermino = $peticionBuscarTermino;
    }

    /**
     * @return PeticionBuscarTermino
     */
    public function getPeticionBuscarTermino()
    {
      return $this->peticionBuscarTermino;
    }

    /**
     * @param PeticionBuscarTermino $peticionBuscarTermino
     * @return buscarTruncatePerfect
     */
    public function setPeticionBuscarTermino($peticionBuscarTermino)
    {
      $this->peticionBuscarTermino = $peticionBuscarTermino;
      return $this;
    }

}
