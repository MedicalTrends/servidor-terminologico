<?php

namespace Semantikos\ClientBundle\API;

class refSetsPorIdDescripcion
{

    /**
     * @var PeticionRefSetsPorIdDescripcion $peticionRefSetsPorIdDescripcion
     */
    protected $peticionRefSetsPorIdDescripcion = null;

    /**
     * @param PeticionRefSetsPorIdDescripcion $peticionRefSetsPorIdDescripcion
     */
    public function __construct($peticionRefSetsPorIdDescripcion)
    {
      $this->peticionRefSetsPorIdDescripcion = $peticionRefSetsPorIdDescripcion;
    }

    /**
     * @return PeticionRefSetsPorIdDescripcion
     */
    public function getPeticionRefSetsPorIdDescripcion()
    {
      return $this->peticionRefSetsPorIdDescripcion;
    }

    /**
     * @param PeticionRefSetsPorIdDescripcion $peticionRefSetsPorIdDescripcion
     * @return refSetsPorIdDescripcion
     */
    public function setPeticionRefSetsPorIdDescripcion($peticionRefSetsPorIdDescripcion)
    {
      $this->peticionRefSetsPorIdDescripcion = $peticionRefSetsPorIdDescripcion;
      return $this;
    }

}
